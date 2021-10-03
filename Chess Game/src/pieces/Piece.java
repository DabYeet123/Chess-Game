package pieces;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import board.PieceArrangeBoard;
import maingame.Handler;

public abstract class Piece {
	
	
	protected Handler handler;
	protected int posX,posY;
	protected int pixX,pixY;
	protected int size;
	protected Rectangle bounds;
	protected final String id;
	protected final String c;
	protected BufferedImage texture;
	protected boolean deliveringCheck = false;
	
	private boolean isBreak = false;
	
	protected ArrayList<int[]> movables = new ArrayList<int[]>();
	protected ArrayList<int[]> capturables = new ArrayList<int[]>();
	protected ArrayList<int[]> protects = new ArrayList<int[]>();

	
	public Piece(Handler handler,int posX, int posY, int size,String id,String c,BufferedImage texture) {
		this.handler = handler;
		this.posX = posX;
		this.posY = posY;
		this.size = size;
		this.id = id;
		this.c = c;
		this.texture = texture;
		this.pixX = posX * size;
		this.pixY = posY * size;
		
		deliveringCheck = false;
		
		bounds = new Rectangle(pixX,pixY,size,size);
	}
	
	public abstract void checkMoves(boolean block);
	
	public abstract void checkProtects();
	
	
	public void update() {
		Piece[][] pieceBoard = handler.getPieceArrangeBoard().getPieceBoard();
		if(pieceBoard[posX][posY] != null) {
			if(pieceBoard[posX][posY].getC().equals("w")) {
				handler.getPieceArrangeBoard().getWPieces().remove(pieceBoard[posX][posY]);
			}else if(pieceBoard[posX][posY].getC().equals("b")) {
				handler.getPieceArrangeBoard().getBPieces().remove(pieceBoard[posX][posY]);
			}
			handler.getPieceArrangeBoard().getPieceList().remove(pieceBoard[posX][posY]);
		}
		pixX = posX * size;
		pixY = posY * size;
		bounds.x = pixX;
		bounds.y = pixY;
		bounds.height = size;
		bounds.width = size;
		pieceBoard[posX][posY] = this;
	}
	
	
	public void tick() {
		
	}
	
	public void render(Graphics g,int x,int y) {
		g.drawImage(texture, x, y, size, size, null);
	}
	
	
	//HELPING METHODS
	public void addMoves(int x,int y) {
		
		if(inRange(x,y)) {
		
			int[] pos = new int[2];
			Piece[][] pb = handler.getPieceArrangeBoard().getPieceBoard();
					
			
			if(pb[x][y] == null) {
				pos[0] = x;
				pos[1] = y;
				movables.add(pos);
				
			}else {
				isBreak = true;
			}
		}
	}
	
	public void addCaptures(int x, int y) {
		
		if(inRange(x,y)) {
			
			int[] pos = new int[2];
			Piece[][] pb = handler.getPieceArrangeBoard().getPieceBoard();
			
			if(pb[x][y] != null) {
				if(!pb[x][y].c.equals(this.c)) {
					pos[0] = x;
					pos[1] = y;
					isBreak = true;
					capturables.add(pos);
				}
			}
		}	
	}
	
	public void addProtects(int x, int y) {
		
		if(inRange(x,y)) {
			
			int[] pos = new int[2];
			Piece[][] pb = handler.getPieceArrangeBoard().getPieceBoard();
			
			if(pb[x][y] != null) {
				pos[0] = x;
				pos[1] = y;
				isBreak = true;
				protects.add(pos);	
			}else {
				pos[0] = x;
				pos[1] = y;
				protects.add(pos);
			}
		}	
	}
	
		
	public void moveTo(int x, int y) {
		Piece p = handler.getMouseManager().getSelector().getPieceSelected();
		if(p.getId().equals("p")) {
			Pawn pawn = (Pawn)p;
			if(!pawn.hasMoved) {
				pawn.hasMoved = true;
				pawn.timesPassed += 1;
			}
		}
		handler.getPieceArrangeBoard().getPieceBoard()[posX][posY] = null;
		setPosX(x);
		setPosY(y);
		update();
	}
	
	
	/*public void capture(int x, int y) {
		handler.getPieceArrangeBoard().getPieceBoard()[posX][posY] = null;
		setPosX(x);
		setPosY(y);
		update();
	}*/
	
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
	
	
	
	
	public static boolean inRange(int x,int y) {
	
		if((x >= 0 && x <= 7) && (y >= 0 && y <= 7)) {
			return true;
		}else {
			return false;
		}
	}
	
	
	//SQUARES CHECKING
	public void checkVertical(int space,boolean M,boolean C,boolean P){
		
		int posX = this.getPosX();
		int posY = this.getPosY();
		int x = posX;
		
		int yUpper = 0;
		int yLower = 7;
		
		if(space != 0) {
			yUpper = posY - space;
			yLower = posY + space;
		}
		

		//Upper Region
		for(int y = posY - 1; y >= yUpper;y--) {
			if(M) {addMoves(x, y);}
			if(C) {addCaptures(x,y);}
			if(P) {addProtects(x,y);}
			if(isBreak) {isBreak = false;break;}
		}
			
		//Lower Region
		for(int y = posY + 1; y <= yLower;y++) {
			if(M) {addMoves(x, y);}
			if(C) {addCaptures(x,y);}
			if(P) {addProtects(x,y);}
			if(isBreak) {isBreak = false;break;}
		}

			
	}
	
	public void checkHorizontal(int space,boolean M,boolean C,boolean P){
		
		int posX = this.getPosX();
		int posY = this.getPosY();
		int y = posY;
		
		int xLeft = 0;
		int xRight = 7;
		
		if(space != 0) {
			xLeft = posX - space;
			xRight = posX + space;
		}
		
		//Left Region
		for(int x = posX - 1; x >= xLeft;x--) {
			if(M) {addMoves(x, y);}
			if(C) {addCaptures(x,y);}
			if(P) {addProtects(x,y);}
			if(isBreak) {isBreak = false;break;}
		}
		
		//Right Region
		for(int x = posX + 1; x  <= xRight;x++) {
			if(M) {addMoves(x, y);}
			if(C) {addCaptures(x,y);}
			if(P) {addProtects(x,y);}
			if(isBreak) {isBreak = false;break;}
			
		}
	}
	
	public void checkLeftSlope(int space,boolean M,boolean C,boolean P){
		
		int posX = this.getPosX();
		int posY = this.getPosY();
		int y = posY;
		
		int xLeft = 0;
		int xRight = 7;
		
		if(space != 0) {
			xLeft = posX - space;
			xRight = posX + space;
		}
		
		y = posY;
		for(int x = posX - 1; x >= xLeft; x--) {
			y -= 1;
			if(M) {addMoves(x, y);}
			if(C) {addCaptures(x,y);}
			if(P) {addProtects(x,y);}
			if(isBreak) {isBreak = false;break;}
		}

		y = posY;
		for(int x = posX + 1; x  <= xRight;x++) {
			y += 1;
			if(M) {addMoves(x, y);}
			if(C) {addCaptures(x,y);}
			if(P) {addProtects(x,y);}
			if(isBreak) {isBreak = false;break;}	
		}
	}

	public void checkRightSlope(int space,boolean M,boolean C,boolean P){
		
		int posX = this.getPosX();
		int posY = this.getPosY();
		
		int y = posY;
		
		int xLeft = 0;
		int xRight = 7;
		
		if(space != 0) {
			xLeft = posX - space;
			xRight = posX + space;
		}
		
		y = posY;
		for(int x = posX - 1; x >= xLeft; x--) {
			y += 1;
			if(M) {addMoves(x, y);}
			if(C) {addCaptures(x,y);}
			if(P) {addProtects(x,y);}
			if(isBreak) {isBreak = false;break;}
		}
			
		y = posY;
		for(int x = posX + 1; x  <= xRight;x++) {
			y -= 1;
			if(M) {addMoves(x, y);}
			if(C) {addCaptures(x,y);}
			if(P) {addProtects(x,y);}
			if(isBreak) {isBreak = false;break;}	
			
		}
	}
	
	public void checkRTP(int xSpace, int ySpace,String action) {
		int posX = this.getPosX();
		int posY = this.getPosY();
		
		
		if(action.equals("moves")){
			addMoves(posX + xSpace, posY + ySpace);
		}else if(action.equals("captures")){
			addCaptures(posX + xSpace, posY + ySpace);
		}else if(action.equals("protects")) {
			addProtects(posX + xSpace, posY + ySpace);
		}
	}
	
	public void checkBlocks() {
		if(((handler.getPieceArrangeBoard().getBKing().isInCheck())&&(c.equals("b"))) 
		||((handler.getPieceArrangeBoard().getWKing().isInCheck())&&(c.equals("w")))) {
		
		String turn = handler.getMouseManager().getSelector().getTurn();
		King bKing = handler.getPieceArrangeBoard().getBKing();
		King wKing = handler.getPieceArrangeBoard().getWKing();
		ArrayList<int[]> mov = new ArrayList<int[]>();
		ArrayList<int[]> cap = new ArrayList<int[]>();
		
		for(Piece piece:handler.getPieceArrangeBoard().getDeliveringCheckList()) {
			for(int[] pos:movables) {
				if((turn.equals("w")) && (wKing.isInCheck())) {
					if((piece.getId().equals("r")||(piece.getId().equals("q")))){
						if( ((wKing.posX==piece.posX)&&(pos[0]==wKing.posX)) && 
								(((pos[1]>wKing.posY)&&(pos[1]<piece.posY))||((pos[1]<wKing.posY)&&(pos[1]>piece.posY)))
								) {
							mov.add(pos);
						}else if( ((wKing.posY==piece.posY)&&(pos[1]==wKing.posY)) && 
								(((pos[0]>wKing.posX)&&(pos[0]<piece.posX))||((pos[0]<wKing.posX)&&(pos[0]>piece.posX)))
								) {
							mov.add(pos);
						}
					}
					if((piece.getId().equals("b")||(piece.getId().equals("q")))){
						for(int[] prot:piece.protects) {
							
						
							if(
								(((pos[0]>wKing.posX)&&(pos[0]<piece.posX))||((pos[0]<wKing.posX)&&(pos[0]>piece.posX)))||
								(((pos[1]>wKing.posY)&&(pos[1]<piece.posY))||((pos[1]<wKing.posY)&&(pos[1]>piece.posY)))
								) {

								if(Arrays.equals(pos, prot)) {
									mov.add(pos);
								}
							
						}
					}
					}
				}else if((turn.equals("b")) && (bKing.isInCheck())) {
					if((piece.getId().equals("r")||(piece.getId().equals("q")))){
						if( ((bKing.posX==piece.posX)&&(pos[0]==bKing.posX)) && 
								(((pos[1]>bKing.posY)&&(pos[1]<piece.posY))||((pos[1]<bKing.posY)&&(pos[1]>piece.posY)))
								) {
							mov.add(pos);
						}else if( ((bKing.posY==piece.posY)&&(pos[1]==bKing.posY)) && 
								(((pos[0]>bKing.posX)&&(pos[0]<piece.posX))||((pos[0]<bKing.posX)&&(pos[0]>piece.posX)))
								) {
							mov.add(pos);
						}
					}
				}
			}
		}
		
		movables = mov;
		capturables = cap;
	}
	}


	
	//SETTERS AND GETTERS

	public ArrayList<int[]> getMovables(){
		return movables;
	}
	
	public ArrayList<int[]> getCapturables(){
		return capturables;
	}
	
	public ArrayList<int[]> getProtects(){
		return protects;
	}
	
	
	
	public int getPosX() {
		return posX;
	}



	public void setPosX(int posX) {
		this.posX = posX;
	}



	public int getPosY() {
		return posY;
	}



	public void setPosY(int posY) {
		this.posY = posY;
	}



	public int getPixX() {
		return pixX;
	}



	public void setPixX(int pixX) {
		this.pixX = pixX;
	}



	public int getPixY() {
		return pixY;
	}



	public void setPixY(int pixY) {
		this.pixY = pixY;
	}



	public int getSize() {
		return size;
	}



	public void setSize(int size) {
		this.size = size;
	}



	public Rectangle getBounds() {
		return bounds;
	}



	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public BufferedImage getTexture() {
		return texture;
	}

	public void setTexture(BufferedImage texture) {
		this.texture = texture;
	}

	public String getId() {
		return id;
	}

	public String getC() {
		return c;
	}

	public void setMovables(ArrayList<int[]> movables) {
		this.movables = movables;
	}

	public void setCapturables(ArrayList<int[]> capturables) {
		this.capturables = capturables;
	}

	public boolean isDeliveringCheck() {
		return deliveringCheck;
	}

	public void setDeliveringCheck(boolean deliveringCheck) {
		this.deliveringCheck = deliveringCheck;
	}


	
	
	
	


}	