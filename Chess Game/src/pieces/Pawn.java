package pieces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import board.Square;
import gfx.Assets;
import maingame.Handler;
import ui.ClickListener;
import ui.UIImageButton;
import ui.UIManager;

public class Pawn extends Piece {
	
	protected boolean hasMoved;
	protected int timesPassed;
	protected int[] enPassant;
	
	public static UIImageButton queen;
	public UIImageButton bishop;
	public UIImageButton knight;
	public UIImageButton rook;
	
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
			if(!hasMoved&&posY == 6) {
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
			}else {
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
			}
			
		}else if(c.equals("b")){
			if(!hasMoved&&posY == 1) {
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
			}else {
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
	
	
	public void promotionCheck() {
		if(c.equals("w")) {
			if(posY == 0) {
				handler.getMouseManager().getSelector().setPromoting(true);
				promote();
			}
		}else if(c.equals("b")) {
			if(posY == 7) {
				handler.getMouseManager().getSelector().setPromoting(true);
				promote();
			}
		}
	}
	
	public void enPassant(int x, int y) {
		if(c.equals("w")) {
			handler.getPieceArrangeBoard().getPieceBoard()[posX][posY] = null;
			handler.getPieceArrangeBoard().getPieceBoard()[x][y+1] = null;
		}else if(c.equals("b")) {
			handler.getPieceArrangeBoard().getPieceBoard()[posX][posY] = null;
			handler.getPieceArrangeBoard().getPieceBoard()[x][y-1] = null;
		}
		setPosX(x);
		setPosY(y);
		update();
	}
	
	public void promote() {
		UIManager uiManager = handler.getMouseManager().getUIManager();
		int pc = 0;
		int b = 0;
		int n = 0;
		int r = 0;
		if(c.equals("w")) {
			pc = 0;
			b = 1;
			n = 2;
			r = 3;
		}else if(c.equals("b")) {
			pc = 1;
			b = -1;
			n = -2;
			r = -3;
		}
		
		Color hovered = new Color(23,52,200);
		Color notHovered = new Color(12,69,10);
		
		
		queen = new UIImageButton(pixX,pixY,64,64,Assets.btn_queen,pc,pc,notHovered,hovered,new ClickListener() {
			@Override
			public void onClick() {
				setTo("q");
			}
			});
		bishop = new UIImageButton((posX)*size,(posY+b)*size,64,64,Assets.btn_bishop,pc,pc,notHovered,hovered,new ClickListener() {
			@Override
			public void onClick() {
				setTo("b");
			}
			});
		knight = new UIImageButton((posX)*size,(posY+n)*size,64,64,Assets.btn_knight,pc,pc,notHovered,hovered,new ClickListener() {
			@Override
			public void onClick() {
				setTo("n");
			}
			});
		rook = new UIImageButton((posX)*size,(posY+r)*size,64,64,Assets.btn_rook,pc,pc,notHovered,hovered,new ClickListener() {
			@Override
			public void onClick() {
				setTo("r");
			}
			});
		
		
		
		
		uiManager.addObject(queen);
		uiManager.addObject(bishop);
		uiManager.addObject(knight);
		uiManager.addObject(rook);
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
