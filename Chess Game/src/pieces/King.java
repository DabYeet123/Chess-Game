package pieces;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import maingame.Handler;

public class King extends Piece {
	
	protected boolean hasMoved;
	protected boolean inCheck;
	protected String facing;
	private ArrayList<int[]> kingProtects;
	private int[] shortCastle;
	private int[] longCastle;
	private Rook shortCastleRook;
	private Rook longCastleRook;
	
	public King(Handler handler, int posX, int posY, int size,String id,String c,BufferedImage texture) {
			super(handler, posX, posY, size,id,c,texture);
			
			hasMoved = false;
			inCheck = false;
			shortCastle = null;
			longCastle = null;
			if(handler.getPieceArrangeBoard().isWhiteOnTop()) {
				if(c.equals("w")) {
					facing = "d";
				}else if(c.equals("b")) {
					facing = "u";
				}
			}else if(!handler.getPieceArrangeBoard().isWhiteOnTop()) {
				if(c.equals("w")) {
					facing = "u";
				}else if(c.equals("b")) {
					facing = "d";
				}
			}
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
			
			int[] longList = null;
			int[] shortList = null;
			int shortRook = -1;
			int longRook = -1;
			
			if(c.equals("w")) {
				controlRange = handler.getPieceArrangeBoard().getBControlRange();
			}else if(c.equals("b")) {
				controlRange = handler.getPieceArrangeBoard().getWControlRange();
			}
			
			if(facing.equals("u")) {
				rowCheck = 7;
			}else if(facing.equals("d")) {
				rowCheck = 0;
			}
			
			if(handler.getPieceArrangeBoard().isWhiteOnTop()) {
				longList = new int[] {4,5,6};
				shortList = new int[] {1,2};
				longRook = 7;
				shortRook = 0;
			}else {
				longList = new int[] {1,2,3};
				shortList = new int[] {6,5};
				longRook = 0;
				shortRook = 7;
			}
				
			if(pb[longRook][rowCheck]!=null) {
				if(pb[longRook][rowCheck].id.equals("r")) {
					int l1 = longList[0];
					int l2 = longList[1];
					int l3 = longList[2];
					Rook rook = (Rook)pb[longRook][rowCheck];
					if(!rook.isHasMoved()) {
						if((pb[l1][rowCheck]==null)&&(pb[l2][rowCheck]==null)&&(pb[l3][rowCheck]==null)) {
							for(int[] con:controlRange) {
								if((Arrays.equals(con, new int[] {l1,rowCheck}))||(Arrays.equals(con, new int[] {l2,rowCheck}))||(Arrays.equals(con, new int[] {l3,rowCheck}))) {
									canCastle = false;
									break;
								}
							}
							if(canCastle) {
								longCastle = new int[] {l2,rowCheck};
								movables.add(longCastle);
								longCastleRook = rook;
							}else {
								longCastle = null;
							}
						}
					}
				}
			}
			if(pb[shortRook][rowCheck]!= null) {
				if(pb[shortRook][rowCheck].id.equals("r")) {
					int s1 = shortList[0];
					int s2 = shortList[1];
					Rook rook = (Rook)pb[shortRook][rowCheck];
					if(!rook.isHasMoved()) {
						if((pb[s1][rowCheck]==null)&&(pb[s2][rowCheck]==null)) {
							for(int[] con:controlRange) {
								if((Arrays.equals(con, new int[] {s1,rowCheck}))||(Arrays.equals(con, new int[] {s2,rowCheck}))) {
									canCastle = false;
									break;
								}
							}
							if(canCastle) {
								shortCastle = new int[] {s1,rowCheck};
								movables.add(shortCastle);
								shortCastleRook = rook;
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
			moveTo(shortCastle[0],shortCastle[1]);
			if(handler.getPieceArrangeBoard().isWhiteOnTop()) {
				shortCastleRook.moveTo(2, shortCastle[1]);
			}else {
				shortCastleRook.moveTo(5, shortCastle[1]);
			}
		}
	}
	
	public void longCastle() {
		if(longCastle!=null) {
			Piece[][] pb = handler.getPieceArrangeBoard().getPieceBoard();
			moveTo(longCastle[0],longCastle[1]);
			if(handler.getPieceArrangeBoard().isWhiteOnTop()) {
				longCastleRook.moveTo(4, longCastle[1]);
			}else {
				longCastleRook.moveTo(3, longCastle[1]);
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
