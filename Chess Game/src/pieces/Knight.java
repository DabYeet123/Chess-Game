package pieces;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import maingame.Handler;

public class Knight extends Piece {
	
	public Knight(Handler handler, int posX, int posY, int size,String id,String c,BufferedImage texture) {
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
			checkRTP(1,2,"moves");
			checkRTP(1,-2,"moves");
			checkRTP(-1,2,"moves");
			checkRTP(-1,-2,"moves");
			checkRTP(2,1,"moves");
			checkRTP(2,-1,"moves");
			checkRTP(-2,1,"moves");
			checkRTP(-2,-1,"moves");
			
			checkRTP(1,2,"captures");
			checkRTP(1,-2,"captures");
			checkRTP(-1,2,"captures");
			checkRTP(-1,-2,"captures");
			checkRTP(2,1,"captures");
			checkRTP(2,-1,"captures");
			checkRTP(-2,1,"captures");
			checkRTP(-2,-1,"captures");

		}
		
	}
	
	public void checkProtects() {
		protects = new ArrayList<int[]>();
		
		checkRTP(1,2,"protects");
		checkRTP(1,-2,"protects");
		checkRTP(-1,2,"protects");
		checkRTP(-1,-2,"protects");
		checkRTP(2,1,"protects");
		checkRTP(2,-1,"protects");
		checkRTP(-2,1,"protects");
		checkRTP(-2,-1,"protects");
	}

}
