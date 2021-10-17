package pieces;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import maingame.Handler;

public class Pawn extends Piece {
	
	protected boolean hasMoved;
	protected int timesPassed;
	protected int[] enPassant;
	
	public Pawn(Handler handler, int posX, int posY, int size,String id,String c,BufferedImage texture) {
			super(handler, posX, posY, size,id,c,texture);
		
			hasMoved = false;
			timesPassed = 0;
			enPassant = null;
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
			enPassant = null;
			pawnMoveCheck(true,"both",false);
			enPassantCheck();

		}else if(restricted.equals("V")) {
			pawnMoveCheck(true,null,false);
		}else if(restricted.equals("LS")) {
			pawnMoveCheck(false,"left",false);
		}else if(restricted.equals("RS")) {
			pawnMoveCheck(false,"right",false);
		}


		
	}
	
	public void checkProtects() {
		protects = new ArrayList<int[]>();
		
		pawnMoveCheck(false,null,true);
	}
	
	
	public void pawnMoveCheck(boolean M,String slope,boolean P) {
		Piece[][] pb = handler.getPieceArrangeBoard().getPieceBoard();

		if(c.equals("w")) {
			if(hasMoved) {
				if(M) {
					checkRTP(0,-1,"moves");
					}
				if(slope!=null) {
					if((slope.equals("left"))||(slope.equals("both"))) {
						checkRTP(-1,-1,"captures");
					}
					if((slope.equals("right"))||(slope.equals("both"))) {
						checkRTP(1,-1,"captures");
					}
				}
				if(P) {
					checkRTP(-1,-1,"protects");
					checkRTP(1,-1,"protects");
				}
			}else {
				if(M) {
					if(pb[posX][posY-1]==null) {
						checkRTP(0,-2,"moves");
					}
					checkRTP(0,-1,"moves");
				}
				if(slope!=null) {
					if((slope.equals("left"))||(slope.equals("both"))) {
						checkRTP(-1,-1,"captures");
					}
					if((slope.equals("right"))||(slope.equals("both"))) {
						checkRTP(1,-1,"captures");
					}
				}
				if(P) {
					checkRTP(-1,-1,"protects");
					checkRTP(1,-1,"protects");
				}
			}
			
		}else if(c.equals("b")){
			if(hasMoved) {
				if(M) {
					checkRTP(0,1,"moves");
				}
				if(slope!=null) {
					if((slope.equals("right"))||(slope.equals("both"))) {
						checkRTP(-1,1,"captures");
					}
					if((slope.equals("left"))||(slope.equals("both"))) {
						checkRTP(1,1,"captures");
					}
				}
				if(P) {
					checkRTP(-1,1,"protects");
					checkRTP(1,1,"protects");
				}
			}else {
				if(M) {
					if(pb[posX][posY+1]==null) {
						checkRTP(0,2,"moves");
					}
					checkRTP(0,1,"moves");
				}
				if(slope!=null) {
					if((slope.equals("right"))||(slope.equals("both"))) {
						checkRTP(-1,1,"captures");
					}
					if((slope.equals("left"))||(slope.equals("both"))) {
						checkRTP(1,1,"captures");
					}
				}
				if(P) {
					checkRTP(-1,1,"protects");
					checkRTP(1,1,"protects");
				}
			}
		}
	}

	
	public void enPassantCheck() {
		int x = getPosX();
		int y = getPosY();
		Piece[][] pb = handler.getPieceArrangeBoard().getPieceBoard();
		
		int[] checking = new int[] {x+1,x-1};
		
		for(int i = 0; i<2;i++) {
			if(inRange(checking[i],y)) {
				int[] pos = new int[] {checking[i],y};
				if(pb[checking[i]][y] != null) {
					Piece piece = pb[checking[i]][y];
					if(piece.getId().equals("p")) {
						Pawn p = (Pawn)pb[checking[i]][y];
						if(c.equals("w")) {
							if(posY == 3) {
								if(((p.getId().equals(id))&&(!p.getC().equals(c)))&&
										(p.getTimesPassed() == 1)) {
									pos[1] = y-1;
									capturables.add(pos);
									enPassant = pos;
								}
							}
						}else if(c.equals("b")) {
							if(posY == 4) {
								if(((p.getId().equals(id))&&(!p.getC().equals(c)))&&
										(p.getTimesPassed() == 1)) {
									pos[1] = y+1;
									capturables.add(pos);
									enPassant = pos;
								}
							}
						}
					}
				}
			}
		}
	}
	
	


	public boolean isHasMoved() {
		return hasMoved;
	}


	public void setHasMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}


	public int getTimesPassed() {
		return timesPassed;
	}


	public void setTimesPassed(int timesPassed) {
		this.timesPassed = timesPassed;
	}


	public int[] getEnPassant() {
		return enPassant;
	}


	public void setEnPassant(int[] enPassant) {
		this.enPassant = enPassant;
	}
	
	
	
	
}
