package pieces;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import maingame.Handler;

public class King extends Piece {
	
	protected boolean hasMoved;
	protected boolean inCheck;
	private ArrayList<int[]> kingProtects;
	private int[] shortCastle;
	private int[] longCastle;
	
	public King(Handler handler, int posX, int posY, int size,String id,String c,BufferedImage texture) {
			super(handler, posX, posY, size,id,c,texture);
			
			hasMoved = false;
			inCheck = false;
			shortCastle = null;
			longCastle = null;
		
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
		checkCastle();
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
	
	
	public void checkCastle() {
		if(!hasMoved) {
			boolean canCastle = true;
			Piece[][] pb = handler.getPieceArrangeBoard().getPieceBoard();
			ArrayList<int[]> controlRange = null;
			int rowCheck = -1;
			
			if(c.equals("w")) {
				controlRange = handler.getPieceArrangeBoard().getBControlRange();
				rowCheck = 7;
			}else if(c.equals("b")) {
				controlRange = handler.getPieceArrangeBoard().getWControlRange();
				rowCheck = 0;
			}
				
			if(pb[0][rowCheck]!=null) {
				if(pb[0][rowCheck].id.equals("r")) {
					Rook rook = (Rook)pb[0][rowCheck];
					if(!rook.isHasMoved()) {
						if((pb[1][rowCheck]==null)&&(pb[2][rowCheck]==null)&&(pb[3][rowCheck]==null)) {
							for(int[] con:controlRange) {
								if((Arrays.equals(con, new int[] {1,rowCheck}))||(Arrays.equals(con, new int[] {2,rowCheck}))||(Arrays.equals(con, new int[] {3,rowCheck}))) {
									canCastle = false;
									break;
								}
							}
							if(canCastle) {
								longCastle = new int[] {2,rowCheck};
								movables.add(longCastle);
							}else {
								longCastle = null;
							}
						}
					}
				}
			}
			if(pb[7][rowCheck]!= null) {
				if(pb[7][rowCheck].id.equals("r")) {
					Rook rook = (Rook)pb[7][rowCheck];
					if(!rook.isHasMoved()) {
						if((pb[5][rowCheck]==null)&&(pb[6][rowCheck]==null)) {
							for(int[] con:controlRange) {
								if((Arrays.equals(con, new int[] {5,rowCheck}))||(Arrays.equals(con, new int[] {6,rowCheck}))) {
									canCastle = false;
									break;
								}
							}
							if(canCastle) {
								shortCastle = new int[] {6,rowCheck};
								movables.add(shortCastle);
							}else {
								shortCastle = null;
							}
						}
					}
				}
			}
		}
	}
	
	public void shortCastle() {
		if(shortCastle!=null) {
			Piece[][] pb = handler.getPieceArrangeBoard().getPieceBoard();
			if(c.equals("w")) {
				moveTo(shortCastle[0],shortCastle[1]);
				pb[7][7].moveTo(5, 7);
			}else if(c.equals("b")) {
				moveTo(shortCastle[0],shortCastle[1]);
				pb[7][0].moveTo(5, 0);
			}		
		}
	}
	
	public void longCastle() {
		if(longCastle!=null) {
			Piece[][] pb = handler.getPieceArrangeBoard().getPieceBoard();
			if(c.equals("w")) {
				moveTo(longCastle[0],longCastle[1]);
				pb[0][7].moveTo(3, 7);
			}else if(c.equals("b")) {
				moveTo(longCastle[0],longCastle[1]);
				pb[0][0].moveTo(3, 0);
			}		
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


	public boolean isHasMoved() {
		return hasMoved;
	}


	public void setHasMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}


	public int[] getShortCastle() {
		return shortCastle;
	}


	public void setShortCastle(int[] shortCastle) {
		this.shortCastle = shortCastle;
	}


	public int[] getLongCastle() {
		return longCastle;
	}


	public void setLongCastle(int[] longCastle) {
		this.longCastle = longCastle;
	}
	
	
	
	
	

}
