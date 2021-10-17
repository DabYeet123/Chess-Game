package pieces;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import maingame.Handler;

public class King extends Piece {
	
	protected boolean hasMoved;
	private boolean inCheck;
	private ArrayList<int[]> kingProtects;
	
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
	public void checkMoves() {
		movables = new ArrayList<int[]>();
		capturables = new ArrayList<int[]>();
		
		checkRTPKing(1,1,true,true,false,false);
		checkRTPKing(1,-1,true,true,false,false);
		checkRTPKing(-1,1,true,true,false,false);
		checkRTPKing(-1,-1,true,true,false,false);
		checkRTPKing(0,1,true,true,false,false);
		checkRTPKing(0,-1,true,true,false,false);
		checkRTPKing(1,0,true,true,false,false);
		checkRTPKing(-1,0,true,true,false,false);		
	}
	
	public void checkProtects() {
		protects = new ArrayList<int[]>();
		kingProtects = new ArrayList<int[]>();
		kingRestricts = new ArrayList<int[]>();
		
		checkRTPKing(1,1,false,false,true,true);
		checkRTPKing(1,-1,false,false,true,true);
		checkRTPKing(-1,1,false,false,true,true);
		checkRTPKing(-1,-1,false,false,true,true);
		checkRTPKing(0,1,false,false,true,true);
		checkRTPKing(0,-1,false,false,true,true);
		checkRTPKing(1,0,false,false,true,true);
		checkRTPKing(-1,0,false,false,true,true);
	}
	
	@Override
	public void checkRTPKing(int xSpace, int ySpace,boolean M,boolean C,boolean P,boolean KP) {
		int posX = this.getPosX();
		int posY = this.getPosY();
		int x = posX + xSpace;
		int y = posY + ySpace;
		int[] pos = new int[2];
		pos[0] = x;
		pos[1] = y;
		
		boolean contains = false;
		ArrayList<int[]> controlRange = null;
		ArrayList<int[]> opKControlRange = null;

			
		if(c.equals("w")) {
			controlRange = handler.getPieceArrangeBoard().getBControlRange();
			opKControlRange = handler.getPieceArrangeBoard().getBKing().getKingProtects();
		}else if(c.equals("b")) {
			controlRange = handler.getPieceArrangeBoard().getWControlRange();
			opKControlRange = handler.getPieceArrangeBoard().getWKing().getKingProtects();
		}
		
		if(inCheck) {
			for(Piece piece:handler.getPieceArrangeBoard().getDeliveringCheckList()) {
				for(int[] ppro:piece.getKingRestricts()) {
					if(Arrays.equals(pos, ppro)) {
						contains = true;
						break;
					}
				}
			}
		}
		
		
		if(controlRange != null) {
			for(int[] con:controlRange) {
				if(Arrays.equals(pos, con)) {
					contains = true;
					break;
				}
			}
		}

		if(opKControlRange != null) {
			for(int[] con:opKControlRange) {
				if(Arrays.equals(pos, con)) {
					contains = true;
					break;
				}
			}
		}
		
		

		
		if(!contains) {
			if(M) {addMoves(x,y);}
			if(C) {addCaptures(x,y);}
			if(P) {addProtects(x,y);}
		}
		if(KP) {
			kingProtects.add(new int[] {x,y});
		}
	}


	public boolean isInCheck() {
		return inCheck;
	}


	public void setInCheck(boolean inCheck) {
		this.inCheck = inCheck;
	}


	public ArrayList<int[]> getKingProtects() {
		return kingProtects;
	}


	public void setKingProtects(ArrayList<int[]> kingProtects) {
		this.kingProtects = kingProtects;
	}
	
	
	

}
