package pieces;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import maingame.Handler;

public class Rook extends Piece {
	
	protected boolean hasMoved;
	
	public Rook(Handler handler, int posX, int posY, int size,String id,String c,BufferedImage texture) {
			super(handler, posX, posY, size,id,c,texture);
			
			hasMoved = false;
		
	}


	@Override
	public void tick() {

		
	}

	@Override
	public void render(Graphics g,int x,int y) {
		g.drawImage(texture, x, y, size, size, null);
	}
	
	
	@Override
	public void checkMoves(boolean block) {
		movables = new ArrayList<int[]>();
		capturables = new ArrayList<int[]>();
		protects = new ArrayList<int[]>();
		checkHorizontal(0,true,true,false);
		checkVertical(0,true,true,false);
		
		if(block) {
			checkBlocks();
		}
	}
	
	public void checkProtects() {
		protects = new ArrayList<int[]>();
		
		checkHorizontal(0,false,false,true);
		checkVertical(0,false,false,true);
	}

}
