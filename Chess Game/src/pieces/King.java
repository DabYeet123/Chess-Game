package pieces;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import maingame.Handler;

public class King extends Piece {
	
	protected boolean hasMoved;
	private boolean inCheck;
	
	public King(Handler handler, int posX, int posY, int size,String id,String c,BufferedImage texture) {
			super(handler, posX, posY, size,id,c,texture);
			
			hasMoved = false;
			inCheck = false;
		
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
		
		checkRTP(1,1,"both");
		checkRTP(1,-1,"both");
		checkRTP(-1,1,"both");
		checkRTP(-1,-1,"both");
		checkRTP(0,1,"both");
		checkRTP(0,-1,"both");
		checkRTP(1,0,"both");
		checkRTP(-1,0,"both");
		
	}
	
	public void checkProtects() {
		protects = new ArrayList<int[]>();
		
		checkRTP(1,1,"both");
		checkRTP(1,-1,"both");
		checkRTP(-1,1,"both");
		checkRTP(-1,-1,"both");
		checkRTP(0,1,"both");
		checkRTP(0,-1,"both");
		checkRTP(1,0,"both");
		checkRTP(-1,0,"both");
	}
	
	@Override
	public void checkRTP(int xSpace, int ySpace,String action) {
		int posX = this.getPosX();
		int posY = this.getPosY();
		int x = posX + xSpace;
		int y = posY + ySpace;
		int[] pos = new int[2];
		pos[0] = x;
		pos[1] = y;
		
		boolean contains = false;

			
		if(c.equals("w")) {
			for(int[] pos1:handler.getPieceArrangeBoard().getBControlRange()) {
				if(Arrays.equals(pos, pos1)) {
					contains = true;
					break;
				}
			}
		}else if(c.equals("b")) {
			for(int[] pos1:handler.getPieceArrangeBoard().getWControlRange()) {
				if(Arrays.equals(pos, pos1)) {
					contains = true;
					break;
				}
			}
		}
		
		if(!contains) {
			addMoves(x,y);
			addCaptures(x,y);
			addProtects(x,y);
		}

	}


	public boolean isInCheck() {
		return inCheck;
	}


	public void setInCheck(boolean inCheck) {
		this.inCheck = inCheck;
	}
	
	

}
