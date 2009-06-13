package com.wwflgames.za.map;

import java.util.ArrayList;

import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.util.FastTrig;

public class MapUtils {

	public static Polygon createFloorPolyFromCoor(int x , int y ) {
		float x1 = x * MapRenderer.TILE_WIDTH + MapRenderer.START_DRAW_X;
		float y1 = y * MapRenderer.TILE_HEIGHT + MapRenderer.START_DRAW_Y;

		// shift the rows over
		x1+=y*MapRenderer.TILE_WIDTH;
		
		float[] points = {
				x1, y1, // 0,1
				x1 + MapRenderer.TILE_WIDTH, y1, // 2,3
				x1 + MapRenderer.TILE_WIDTH + MapRenderer.BOTTOM_TILE_SHIFT, y1 + MapRenderer.TILE_HEIGHT, // 4,5
				x1 + MapRenderer.BOTTOM_TILE_SHIFT, y1 + MapRenderer.TILE_HEIGHT // 6,7
		};
		return new Polygon(points);
	}
	
	public static ScreenDelta calculateDelta( float x , float y , float tx , float ty ) {
		ScreenDelta d = new ScreenDelta();
		double angRad = Math.atan2(y - ty, x - tx);
		d.angle = angRad/(Math.PI/180);
		d.x = -1*(float)FastTrig.cos(angRad);
		d.y = -1*(float)FastTrig.sin(angRad); 
		return d;
	}
	
	public static Wall newWall(Dir direction) {
		Wall w = new Wall(direction);
		return w;
	}

	public static Path makePath(int sx, int sy, int ex, int ey , 
			FloorMap map ) {
		BresenhamLine line = new BresenhamLine();
		int length = line.plot(sx, sy, ex, ey);
		Path p = new Path();
		for (int i = 0; i < length; i++) {
			int x = line.getX();
			int y = line.getY();
			p.addStep(x, y);
			line.next();
		}
		return p;
	}
	
	// if a path exists from sx,sy to ex,ey, this method will return it
	// otherwise, it will return null;
	public static Path findLOSPath(int sx, int sy, int ex, int ey , FloorMap map ) {

		Path p = makePath(sx,sy,ex,ey,map);
		
		// now walk the path and look for obstacles along the way
		Path.Step previous = null;
		ArrayList<Wall> checkWalls = new ArrayList<Wall>();
		for ( Path.Step step : p.getSteps() ) {
			checkWalls.clear();
			MapSquare stepms = map.getMapSquare(step.x, step.y);
			if ( previous != null ) {
				MapSquare previousms = map.getMapSquare(previous.x,previous.y);
				// determine the directions between this step and
				// the previous step, and check any walls that need
				// to be checked
				int dx = step.x - previous.x;
				int dy = step.y - previous.y;
				if ( dx == 1 ) {
					// path moved one step east, check previous for
					// an east wall, and step for a west wall
					
					if ( !isPassable(previousms,Dir.EAST) ||
							!isPassable(stepms,Dir.WEST)) {
						return null;
					}
				}
				else if ( dx == -1 ) {
					// path moved one step west, check previous for
					// a west wall, and step for an east wall
					if ( !isPassable(previousms,Dir.WEST) ||
							!isPassable(stepms,Dir.EAST)) {
						return null;
					}
				}

				// now check y direction
				if ( dy == 1 ) {
					// path moved one step south, check previous for 
					// a south wall, and step for a north wall
					if ( !isPassable(previousms,Dir.SOUTH) ||
							!isPassable(stepms,Dir.NORTH)) {
						return null;
					}
				} else if ( dy == -1 ) {
					// path moved one step north, check previous for 
					// a north wall, and step for a south wall
					if ( !isPassable(previousms,Dir.NORTH) ||
							!isPassable(stepms,Dir.SOUTH)) {
						return null;
					}
				}		
			}
			previous = step;
		}
		return p;
	}
	
	// checks for a wall in the
	private static boolean isPassable(MapSquare ms ,Dir dir ) {
		Wall w = ms.findWall(dir);
		if ( w == null ) {
			return true;
		}
		else if ( w.hasDoor() && w.isDoorOpen() ) {
			return true;
		}
		else {
			return false;
		}
	}
	
}
