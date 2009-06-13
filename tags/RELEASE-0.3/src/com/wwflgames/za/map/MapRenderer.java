package com.wwflgames.za.map;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;

import com.wwflgames.za.map.Room.Door;
import com.wwflgames.za.map.effect.TileEffect;
import com.wwflgames.za.mob.Hero;
import com.wwflgames.za.mob.Mobile;
import com.wwflgames.za.slick.SlickEntity;

public class MapRenderer extends SlickEntity implements MapChangeListener {

	public static final Color FLOOR_COLOR = Color.darkGray;
	public static final Color WALL_COLOR = Color.gray;
	public static final Color TRANS_WALL_COLOR = new Color(Color.gray.r,
			Color.gray.g, Color.gray.b, .5f);

	public static final Color EXIT_LOCATION_COLOR = new Color(Color.green.r,
			Color.green.g, Color.green.b, .5f);

	public static final int TILE_WIDTH = 32;
	public static final int TILE_HEIGHT = 16;
	public static final int BOTTOM_TILE_SHIFT = 16;
	public static final int WALL_HEIGHT = 42;

	public static final int START_DRAW_X = 0;
	public static final int START_DRAW_Y = 250;

	private float translateX = 0; 
	private float translateY = 0;
	
	FloorMap currentMap;
	Hero hero;

	public MapRenderer() {
	}

	@Override
	public void update(int delta) throws SlickException {
		
		// see if we need to move translate x based
		// on the hero's current position
		int heromapx = hero.getMobx();
		translateX = (heromapx/20) * TILE_WIDTH * 20 * -1;
		
		// update any tile effects
		MapSquare[][] mapSquares = currentMap.getMap();
		for (int y = 0; y <= currentMap.getHeight(); y++) {
			for (int x = currentMap.getWidth(); x >= 0; x--) {
				MapSquare ms = mapSquares[x][y];
				TileEffect te = ms.getTileEffect();
				if ( te != null ) {
					te.update(delta);
					// remove it from the tile if it's complete
					if ( te.isComplete() ) {
						ms.setTileEffect(null);
					}
				}
			}
		}
	}

	// we have to render the map from right to left,
	// top to bottom to get the isometric stuff to look
	// right
	public void renderFloor(Graphics g) throws SlickException {
		
		// translate
		g.translate(translateX, translateY);
		
		MapSquare[][] mapSquares = currentMap.getMap();

		Room heroRoom = currentMap.findRoomForXY(hero.getMobx(), 
				hero.getMoby());

		// draw the floors
		for (int y = 0; y <= currentMap.getHeight(); y++) {
			for (int x = currentMap.getWidth(); x >= 0; x--) {
				MapSquare ms = currentMap.getMapSquare(x, y);
				Shape p = ms.getFloorPoly();
				g.setColor(FLOOR_COLOR);
				g.fill(p);
				g.setColor(Color.black);
				g.draw(p);
				
				//if this is the exit location, paint a green overlay
				//TODO: Draw the exit
				if ( ms.isExitLocation() ) {
					g.setColor(EXIT_LOCATION_COLOR);
					g.fill(p);				
				}
			}
		}
		
		// draw the walls, and the floor stuff
		for (int y = 0; y <= currentMap.getHeight(); y++) {
			for (int x = currentMap.getWidth(); x >= 0; x--) {

				boolean roomStuffVisible = false;
				
				MapSquare ms = mapSquares[x][y];

				Color wallColor = WALL_COLOR;
				if (heroRoom != null && heroRoom.getMapSquares().contains(ms)) {
					wallColor = TRANS_WALL_COLOR;
					roomStuffVisible = true;
				}
				
				// find the current room
				Room currentRoom = currentMap.findRoomForXY(x, y);
				if ( currentRoom != null ) {
					// see if the hero is behind the room 
					if ( hero.getMoby() < currentRoom.y2 && 
							hero.getMobx() >= currentRoom.x1 &&
							hero.getMobx() <= currentRoom.x2+1 ) {
						wallColor = TRANS_WALL_COLOR;
					}
					// see if a door is open
					for ( Door d : currentRoom.doors ) {
						if ( d.open ) {
							wallColor = TRANS_WALL_COLOR;
							roomStuffVisible = true;
							break;
						}
					}
					// see if the hero is in the room
					if ( heroRoom == currentRoom ) {
						roomStuffVisible = true;
					}
				}
				
				// let's see if this works
				wallColor = TRANS_WALL_COLOR;
				
				// x1 and x1 will be used as the base for all drawing, they
				// are the top left corner of the floor tile
				float x1 = x * TILE_WIDTH + START_DRAW_X + xoffset(y);
				float y1 = y * TILE_HEIGHT + START_DRAW_Y;

				// Need to draw the north wall before we draw any of
				// the mobs
				// NORTH wall
				if ( ms.hasWall(Dir.NORTH) ) {
					Wall w = ms.findWall(Dir.NORTH);
					Polygon doorPoly = w.hasDoor() ? w.getDoor().doorPoly : null;
					Shape[] wallPolys = w.getWallShapes();
					drawWall(w,doorPoly,wallPolys,wallColor,g);
				}

				// draw any item that may exist on the floor
				// TODO temp code here, need to draw the item image
				if ( roomStuffVisible ) {
					FloorItem fi = ms.getFloorItem();
					if (fi != null) {
						g.setColor(Color.white);
						g.drawString(fi.getItem().getName(), x1 + 12, y1);
					}
				}

				// Render any tile effects before we render the mob
				TileEffect te = ms.getTileEffect();
				if ( te != null ) {
					te.render(g);
				}
				
				// TODO right-to-left movement animations are
				// messed up because the square the mob just
				// left is getting rendered before the mob, need to
				// fix that
				// render any mobs that are on the square
				Mobile m = ms.getMobile();
				if ( m != null ) {
					// only render a mob if:
					// 1) We're not rendering a room right now
					// 2) We're rendering a room and roomStuffVisible is true
					// 3) we're rendering the player
					
					if ( m == hero ||
							currentRoom == null ||
							roomStuffVisible ) {
						m.render(g);
					}
				}

				// draw the other walls
				Dir [] drawOrder = { Dir.EAST , Dir.SOUTH , Dir.WEST };
				for ( Dir dir : drawOrder ) {
					if ( ms.hasWall(dir) ) {
						Wall w = ms.findWall(dir);
						Polygon doorPoly = w.hasDoor() ? w.getDoor().doorPoly : null;
						Shape[] wallPolys = w.getWallShapes();
						drawWall(w,doorPoly,wallPolys,wallColor,g);
					}
				}
			}
		}
		
		// untranslate
		g.translate(0,0);
		g.resetTransform();
	}

	private void drawWall(Wall w, Polygon doorPoly , Shape[] wallPolys, 
			Color wallColor , Graphics g  ) {
		for ( Shape s : wallPolys ) {
			g.setColor(wallColor);
			g.fill(s);
			g.setColor(Color.black);
			g.draw(s);
		}
		
		if ( doorPoly != null && !w.isDoorOpen() ) {
			g.setColor(Color.white);
			g.fill(doorPoly);
			
		}		
	}
	
	
	private Polygon createDoorPoly(Wall w, float swx, float swy, float ewx ,
			float ewy) {

		float[] doorPoints = new float[8];
		if ( w.direction() == Dir.EAST || w.direction() == Dir.WEST ) {
			float drx1 = swx + 3;
			float dry1 = swy + 3;
			float drx2 = ewx - 3;
			float dry2 = ewy - 3;
			doorPoints[0] = drx1; doorPoints[1] = dry1;
			doorPoints[2] = drx2; doorPoints[3] = dry2;
			doorPoints[4] = drx2; doorPoints[5] = dry2 - WALL_HEIGHT + 6;
			doorPoints[6] = drx1; doorPoints[7] = dry1 - WALL_HEIGHT + 6;
		} else {
			float drx1 = swx + 4;
			float dry1 = swy + 1;
			float drx2 = ewx - 4;
			float dry2 = ewy + 1;

			doorPoints[0] = drx1; doorPoints[1] = dry1;
			doorPoints[2] = drx2; doorPoints[3] = dry2;
			doorPoints[4] = drx2; doorPoints[5] = dry2 - WALL_HEIGHT + 6;
			doorPoints[6] = drx1; doorPoints[7] = dry1 - WALL_HEIGHT + 6;
		}
		
		Polygon doorPoly = new Polygon(doorPoints);
		return doorPoly;
		
	}

	private Polygon createFloorPolyFromCoor(float x1, float y1) {
		int width = TILE_WIDTH;
		float[] points = { x1, y1, // 0,1
				x1 + width, y1, // 2,3
				x1 + width + BOTTOM_TILE_SHIFT, y1 + TILE_HEIGHT, // 4,5
				x1 + BOTTOM_TILE_SHIFT, y1 + TILE_HEIGHT // 6,7
		};
		return new Polygon(points);
	}

	public void mapChanged(FloorMap newMap) {
		currentMap = newMap;
		
		// create all of the floor polys for the new map
		for (int y = 0; y <= currentMap.getHeight(); y++) {
			for (int x = currentMap.getWidth(); x >= 0; x--) {
				
				// x1 and x1 will be used as the base for all drawing, they
				// are the top left corner of the floor tile
				float x1 = x * TILE_WIDTH + START_DRAW_X + xoffset(y);
				float y1 = y * TILE_HEIGHT + START_DRAW_Y;

				Polygon p = createFloorPolyFromCoor(x1, y1);
				currentMap.getMapSquare(x, y).setFloorPoly(p);
			}
		}
		
		// create all the wall polys for the new map
		for (int y = 0; y <= currentMap.getHeight(); y++) {
			for (int x = currentMap.getWidth(); x >= 0; x--) {

				MapSquare ms = newMap.getMapSquare(x, y);

				// x1 and x1 will be used as the base for all drawing, they
				// are the top left corner of the floor tile
				float x1 = x * TILE_WIDTH + START_DRAW_X + xoffset(y);
				float y1 = y * TILE_HEIGHT + START_DRAW_Y;

				Shape p = ms.getFloorPoly();

				// we need the floor points from the poly to draw
				// walls
				float[] floorPoints = p.getPoints();

				// Need to draw the north wall before we draw any of
				// the mobs
				// NORTH wall
				if ( ms.hasWall(Dir.NORTH) ) {
					float swx1 = floorPoints[0];
					float swy1 = floorPoints[1];
					float swx2 = floorPoints[2];
					float swy2 = floorPoints[3];

					float[] wallPoints = { 
							swx1, swy1, // 0,1
							swx2, swy2, // 2,3
							swx2, swy2 - WALL_HEIGHT, // 4,5
							swx1, swy2 - WALL_HEIGHT // 6,7
					};
					
					Polygon doorPoly = null;
					Wall w = ms.findWall(Dir.NORTH);
					if ( w.hasDoor() ) {
						doorPoly = createDoorPoly(w,swx1,swy1,swx2,swy2);
					}
					
					Polygon wallPoly = new Polygon(wallPoints);
					Shape[] wallShapes = null;
					if ( doorPoly != null ) {
						wallShapes = wallPoly.subtract(doorPoly);
					} else {
						wallShapes = new Shape[] { wallPoly };
					}

					w.setWallShapes(wallShapes);
					if ( w.hasDoor() ) {
						w.getDoor().doorPoly = doorPoly;
					}
				}

				// EAST walls
				if ( ms.hasWall(Dir.EAST ) ) {
					float wx1 = floorPoints[2];
					float wy1 = floorPoints[3];
					float wx2 = floorPoints[4];
					float wy2 = floorPoints[5];
					float[] wallPoints = { wx1, wy1, // 0,1
							wx1, wy1 - WALL_HEIGHT, // 2,3
							wx2, wy2 - WALL_HEIGHT, // 4,5
							wx2, wy2 // 6,7
					};
					
					
					Polygon doorPoly = null;
					Wall w = ms.findWall(Dir.EAST);
					if ( w.hasDoor() ) {
						doorPoly = createDoorPoly(w,wx1,wy1,wx2,wy2);
					}
					
					Polygon wallPoly = new Polygon(wallPoints);
					Shape[] wallShapes = null;
					if ( doorPoly != null ) {
						wallShapes = wallPoly.subtract(doorPoly);
					} else {
						wallShapes = new Shape[] { wallPoly };
					}

					w.setWallShapes(wallShapes);
					if ( w.hasDoor() ) {
						w.getDoor().doorPoly = doorPoly;
					}
					
				}

				// SOUTH wall
				if (ms.hasWall(Dir.SOUTH)) {
					float swx1 = floorPoints[6];
					float swy1 = floorPoints[7];
					float swx2 = floorPoints[4];
					float swy2 = floorPoints[5];
					float[] wallPoints = { swx1, swy1, // 0,1
							swx2, swy2, // 2,3
							swx2, swy2 - WALL_HEIGHT, // 4,5
							swx1, swy2 - WALL_HEIGHT // 6,7
					};
					
					Polygon doorPoly = null;
					Wall w = ms.findWall(Dir.SOUTH);
					if ( w.hasDoor() ) {
						doorPoly = createDoorPoly(w,swx1,swy1,swx2,swy2);
					}
					

					Polygon wallPoly = new Polygon(wallPoints);
					Shape[] wallShapes = null;
					if ( doorPoly != null ) {
						wallShapes = wallPoly.subtract(doorPoly);
					} else {
						wallShapes = new Shape[] { wallPoly };
					}

					w.setWallShapes(wallShapes);
					if ( w.hasDoor() ) {
						w.getDoor().doorPoly = doorPoly;
					}
	
				}

				// WEST walls
				if (ms.hasWall(Dir.WEST)) {
					float wx1 = floorPoints[0];
					float wy1 = floorPoints[1];
					float wx2 = floorPoints[6];
					float wy2 = floorPoints[7];
					float[] wallPoints = { wx1, wy1, // 0,1
							wx1, wy1 - WALL_HEIGHT, // 2,3
							wx2, wy2 - WALL_HEIGHT, // 4,5
							wx2, wy2 // 6,7
					};
					
					Polygon doorPoly = null;
					Wall w = ms.findWall(Dir.WEST);
					if ( w.hasDoor() ) {
						doorPoly = createDoorPoly(w,wx1,wy1,wx2,wy2);
					}
					

					Polygon wallPoly = new Polygon(wallPoints);
					Shape[] wallShapes = null;
					if ( doorPoly != null ) {
						wallShapes = wallPoly.subtract(doorPoly);
					} else {
						wallShapes = new Shape[] { wallPoly };
					}

					w.setWallShapes(wallShapes);
					if ( w.hasDoor() ) {
						w.getDoor().doorPoly = doorPoly;
					}
				}
			}
		}		
		
	}
	
	public static float xoffset(int y) {
		return TILE_WIDTH / 2 * y;
	}

	public Hero getHero() {
		return hero;
	}

	public void setHero(Hero hero) {
		this.hero = hero;
	}

}
