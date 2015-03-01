package atc;

import java.lang.Object;

public class Zap extends Command{
	public int x;
	public int y;
	public Position pos = new Position(x,y);
	
	public Zap(int myX, int myY){
		x = myX;
		y = myY;
	}
}
