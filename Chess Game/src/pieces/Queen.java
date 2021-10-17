package pieces;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import maingame.Handler;

public class Queen extends Piece {
	
	public Queen(Handler handler, int posX, int posY, int size,String id,String c,BufferedImage texture) {
			super(handler, posX, posY, size,id,c,texture);
		
	}


	@Override
	public void tick() {

		
	}

	@Override
	public void render(Graphics g,int x,int y) {
		g.drawImage(texture, x, y, size, size, null);
	}
	
	
	@Override
	public void checkMoves() {

		movables = new ArrayList<int[]>();
		capturables = new ArrayList<int[]>();
		if(!isPinned) {
			checkLeftSlope(0,true,true,false,false);
			checkRightSlope(0,true,true,false,false);
			checkHorizontal(0,true,true,false,false);
			checkVertical(0,true,true,false,false);
			

		}else if(restricted.equals("LS")) {
			checkLeftSlope(0,true,true,false,false);
		}else if(restricted.equals("RS")) {
			checkRightSlope(0,true,true,false,false);
		}else if(restricted.equals("H")) {
			checkHorizontal(0,true,true,false,false);
		}else if(restricted.equals("V")) {
			checkVertical(0,true,true,false,false);
		}

	}
	
	public void checkProtects() {
		protects = new ArrayList<int[]>();
		pProtects = new ArrayList<int[]>();
		kingRestricts = new ArrayList<int[]>();
		
		checkLeftSlope(0,false,false,true,true);
		checkRightSlope(0,false,false,true,true);
		checkHorizontal(0,false,false,true,true);
		checkVertical(0,false,false,true,true);
	}

}