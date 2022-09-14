package pieces;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;


import board.PieceArrangeBoard;
import gfx.Assets;
import maingame.Handler;

public abstract class Piece {
	
	
	protected Handler handler;
	protected int posX,posY;
	protected int pixX,pixY;
	protected int[] pos = new int[2];
	protected int size;
	protected Rectangle bounds;
	protected final String id;
	protected final String c;
	protected BufferedImage texture;
	protected boolean deliveringCheck;
	protected boolean isPinned;
	protected int[] blockMove;
	protected String restricted;
	protected String pRestriction;
	
	private boolean isBreak = false;
	private boolean isBreak2 = false;
	
	private Piece pieceBlocking;
	private String pieceBlockType;
	
	protected ArrayList<int[]> movables = new ArrayList<int[]>();
	protected ArrayList<int[]> capturables = new ArrayList<int[]>();
	protected ArrayList<int[]> protects = new ArrayList<int[]>();
	protected ArrayList<int[]> pProtects = new ArrayList<int[]>();
	protected ArrayList<int[]> kingRestricts = new ArrayList<int[]>();

	
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
		
		pos[0] = posX;
		pos[1] = posY;
		
		deliveringCheck = false;
		isPinned = false;
		blockMove = null;
		restricted = null;
		pRestriction = null;
		
		bounds = new Rectangle(pixX,pixY,size,size);
	}
	
	public abstract void checkMoves();
	
	public abstract void checkProtects();
	
	public void checkRTPKing(int xSpace, int ySpace, boolean M, boolean C, boolean P,boolean KP) {
		
	}
	public void refresh() {
		pixX = posX * size;
		pixY = posY * size;
		//System.out.println("refreshed");
	}
	
	public void update() {
		Piece[][] pieceBoard = handler.getPieceArrangeBoard().getPieceBoard();
		if(pieceBoard[posX][posY] != null) {
			if(pieceBoard[posX][posY].getC().equals("w")) {
				handler.getPieceArrangeBoard().getWPieces().remove(pieceBoard[posX][posY]);
			}else if(pieceBoard[posX][posY].getC().equals("b")) {
				handler.getPieceArrangeBoard().getBPieces().remove(pieceBoard[posX][posY]);
			}
			handler.getPieceArrangeBoard().getPieceList().remove(pieceBoard[posX][posY]);
			handler.getPieceArrangeBoard().getPieceRemoved().add(pieceBoard[posX][posY]);
		}
		pixX = posX * size;
		pixY = posY * size;
		bounds.x = pixX;
		bounds.y = pixY;
		bounds.height = size;
		bounds.width = size;
		pos[0] = posX;
		pos[1] = posY;
		pieceBoard[posX][posY] = this;
	}
	
	public void reAdd() {
		Piece[][] pieceBoard = handler.getPieceArrangeBoard().getPieceBoard();
		pieceBoard[posX][posY] = this;
		if(c.equals("w")) {
			handler.getPieceArrangeBoard().getWPieces().add(this);
		}else if(c.equals("b")) {
			handler.getPieceArrangeBoard().getBPieces().add(this);
		}
		handler.getPieceArrangeBoard().getPieceList().add(this);
		handler.getPieceArrangeBoard().getPieceRemoved().remove(this);
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
			}else if(pb[x][y] != null) {
				pos[0] = x;
				pos[1] = y;
				isBreak = true;
			}
		}else {
			isBreak = true;
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
		}else {
			isBreak = true;
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
				pieceBlocking = pb[x][y];
				protects.add(pos);	
			}else {
				pos[0] = x;
				pos[1] = y;
				protects.add(pos);
			}
		}else {
			isBreak = true;
		}
	}
	
	public void addPProtects(int x, int y) {
		
		if(inRange(x,y)) {
			
			int[] pos = new int[2];
			Piece[][] pb = handler.getPieceArrangeBoard().getPieceBoard();
			
			if(pb[x][y] != null) {
				pos[0] = x;
				pos[1] = y;
				if(pieceBlocking!=null) {
					if(pieceBlocking.c.equals(pb[x][y].c)) {
						if((pb[x][y].getId().equals("k"))&&(!c.equals(pb[x][y].getC()))) {//Set Pins
							pieceBlocking.isPinned = true;
							pieceBlocking.restricted = pieceBlockType;
							//System.out.println(pieceBlocking.isPinned+pieceBlocking.restricted);
							//System.out.println(pieceBlocking);
						}
					}
				}
				isBreak2 = true;
				pProtects.add(pos);	
			}else {
				pos[0] = x;
				pos[1] = y;
				pProtects.add(pos);
				if(pieceBlocking.id.equals("k")) {
					kingRestricts.add(pos);
				}
			}
		}else {
			isBreak2 = true;
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
		}else if(p.getId().equals("r")) {
			Rook rook = (Rook)p;
			rook.hasMoved = true;
		}else if(p.getId().equals("k")) {
			King king = (King)p;
			king.hasMoved = true;
		}
		
		
		handler.getPieceArrangeBoard().getPieceBoard()[posX][posY] = null;
		setPosX(x);
		setPosY(y);
		update();
	}
	
	public void moveTo(int x, int y,boolean notRewind) {
		
		Piece p = this;
		if(p.getId().equals("p")) {
			Pawn pawn = (Pawn)p;
			if(pawn.getFacing().equals("u")){
				if(y == 6) {
					pawn.hasMoved = false;
					pawn.timesPassed = 0;
				}
			}else if(pawn.getFacing().equals("d")){
				if(y == 1) {
					pawn.hasMoved = false;
					pawn.timesPassed = 0;
				}
			}
		}else if(p.getId().equals("r")) {
			Rook rook = (Rook)p;
			rook.hasMoved = true;
		}else if(p.getId().equals("k")) {
			King king = (King)p;
			king.hasMoved = true;
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
	
	public void setTo(String type) {
		Piece[][] pieceBoard = handler.getPieceArrangeBoard().getPieceBoard();
		if(pieceBoard[posX][posY].getC().equals("w")) {
			handler.getPieceArrangeBoard().getWPieces().remove(pieceBoard[posX][posY]);
		}else if(pieceBoard[posX][posY].getC().equals("b")) {
			handler.getPieceArrangeBoard().getBPieces().remove(pieceBoard[posX][posY]);
		}
		handler.getPieceArrangeBoard().getPieceList().remove(pieceBoard[posX][posY]);
		handler.getPieceArrangeBoard().getPieceRemoved().add(pieceBoard[posX][posY]);
		handler.getPieceArrangeBoard().getPawnPieces().remove(pieceBoard[posX][posY]);
		
		if(c.equals("w")) {
			if(type.equals("q")) {
				pieceBoard[posX][posY] = new Queen(handler,posX,posY,size,"q",c,Assets.w_queen);;
			}else if(type.equals("b")) {
				pieceBoard[posX][posY] = new Bishop(handler,posX,posY,size,"b",c,Assets.w_bishop);
			}else if(type.equals("n")) {
				pieceBoard[posX][posY] = new Knight(handler,posX,posY,size,"n",c,Assets.w_knight);
			}else if(type.equals("r")) {
				pieceBoard[posX][posY] = new Rook(handler,posX,posY,size,"r",c,Assets.w_rook);
			}
			handler.getPieceArrangeBoard().getWPieces().add(pieceBoard[posX][posY]);
			handler.getPieceArrangeBoard().getPieceList().add(pieceBoard[posX][posY]);
		}else if(c.equals("b")) {
			if(type.equals("q")) {
				pieceBoard[posX][posY] = new Queen(handler,posX,posY,size,"q",c,Assets.b_queen);;
			}else if(type.equals("b")) {
				pieceBoard[posX][posY] = new Bishop(handler,posX,posY,size,"b",c,Assets.b_bishop);
			}else if(type.equals("n")) {
				pieceBoard[posX][posY] = new Knight(handler,posX,posY,size,"n",c,Assets.b_knight);
			}else if(type.equals("r")) {
				pieceBoard[posX][posY] = new Rook(handler,posX,posY,size,"r",c,Assets.b_rook);
			}
			handler.getPieceArrangeBoard().getBPieces().add(pieceBoard[posX][posY]);
			handler.getPieceArrangeBoard().getPieceList().add(pieceBoard[posX][posY]);
		}
		
		handler.getGame().getGameState().resetUI();
		handler.getMouseManager().getSelector().setPromoting(false);
		handler.getMouseManager().getSelector().turnUpdate(null);
		handler.getMouseManager().getSelector().highlightUpdate();
	}
		
	
	public static boolean inRange(int x,int y) {
	
		if((x >= 0 && x <= 7) && (y >= 0 && y <= 7)) {
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean isInBetween(int[] moving, int[] checking, int[] attacked, String type) {
		if(type.equals("HV")) {
			if( ((attacked[0]==moving[0])&&(attacked[0]==checking[0])) && 
				(((moving[1]>attacked[1])&&(moving[1]<checking[1]))||((moving[1]<attacked[1])&&(moving[1]>checking[1])))
				) {
				return true;
			}else if( ((attacked[1]==moving[1])&&(attacked[1]==checking[1])) && 
					(((moving[0]>attacked[0])&&(moving[0]<checking[0]))||((moving[0]<attacked[0])&&(moving[0]>checking[0])))
					) {
				return true;
			}else {
				return false;
			}
		}else if(type.equals("Diag")) {
			if(	(((moving[0]>attacked[0])&&(moving[0]<checking[0]))&&((moving[1]>attacked[1])&&(moving[1]<checking[1])))||
				(((moving[0]>attacked[0])&&(moving[0]<checking[0]))&&((moving[1]<attacked[1])&&(moving[1]>checking[1])))||
				(((moving[0]<attacked[0])&&(moving[0]>checking[0]))&&((moving[1]>attacked[1])&&(moving[1]<checking[1])))||
				(((moving[0]<attacked[0])&&(moving[0]>checking[0]))&&((moving[1]<attacked[1])&&(moving[1]>checking[1])))
				) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
		
	
	//SQUARES CHECKING
	public void checkVertical(int space,boolean M,boolean C,boolean P,boolean PP){
		
		pieceBlockType = "V";
		pieceBlocking = null;
		
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
		for(int y = posY - 1; y >= yUpper-1; y--) {
			
			if(M) {addMoves(x, y);}
			if(C) {addCaptures(x,y);};
			if(P) {addProtects(x,y);}
			if(isBreak) {
				isBreak = false;
				if(PP) {
					for(int y1 = y - 1; y1 >= yUpper-1; y1--) {
						if(inRange(x,y1)) {
						addPProtects(x,y1);
						if(isBreak2) {isBreak2 = false;break;}
						}
					}
				}
				break;
			}
		}
			
		//Lower Region
		for(int y = posY + 1; y <= yLower+1;y++) {
			//System.out.println(x+" "+y);
			if(M) {addMoves(x, y);}
			if(C) {addCaptures(x,y);}
			if(P) {addProtects(x,y);}
			if(isBreak) {
				isBreak = false;
				if(PP) {
					for(int y1 = y + 1; y1 <= yLower+1; y1++) {
						if(inRange(x,y1)) {
						addPProtects(x,y1);
						if(isBreak2) {isBreak2 = false;break;}
						}
					}
				}
				break;
			}
		}

			
	}
	
	public void checkHorizontal(int space,boolean M,boolean C,boolean P,boolean PP){
		
		pieceBlockType = "H";
		pieceBlocking = null;
		
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
		for(int x = posX - 1; x >= xLeft-1;x--) {
			if(M) {addMoves(x, y);}
			if(C) {addCaptures(x,y);}
			if(P) {addProtects(x,y);}
			if(isBreak) {
				isBreak = false;
				if(PP) {
					for(int x1 = x - 1; x1 >= xLeft-1; x1--) {
						if(inRange(x1,y)) {
						addPProtects(x1,y);
						if(isBreak2) {isBreak2 = false;break;}
						}
					}
				}
				break;
			}
		}
		
		//Right Region
		for(int x = posX + 1; x  <= xRight+1;x++) {
			if(M) {addMoves(x, y);}
			if(C) {addCaptures(x,y);}
			if(P) {addProtects(x,y);}
			if(isBreak) {
				isBreak = false;
				if(PP) {
					for(int x1 = x + 1; x1 <= xRight+1; x1++) {
						if(inRange(x1,y)) {
						addPProtects(x1,y);
						if(isBreak2) {isBreak2 = false;break;}
						}
					}
				}
				break;
			}
			
		}
	}
	
	public void checkLeftSlope(int space,boolean M,boolean C,boolean P,boolean PP){
		
		pieceBlockType = "LS";
		pieceBlocking = null;
		
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
		//System.out.println("Top Left");
		for(int x = posX - 1; x >= xLeft-1; x--) { //Top Left
			y -= 1;
			if(M) {addMoves(x, y);}
			if(C) {addCaptures(x,y);}
			if(P) {addProtects(x,y);}
			
				if(isBreak) {
					isBreak = false;
					if(PP) {
						for(int x1 = x - 1; x1 >= xLeft-1; x1--) {
							y -= 1;
							if(inRange(x1,y)) {
							addPProtects(x1,y);
							if(isBreak2) {isBreak2 = false;break;}
							}
						}
					}
					break;
				}
		}

		y = posY;
		//System.out.println("Bottom Right");
		for(int x = posX + 1; x  <= xRight+1;x++) { //Bottom Right
			y += 1;
			if(M) {addMoves(x, y);}
			if(C) {addCaptures(x,y);}
			if(P) {addProtects(x,y);}

				if(isBreak) {
					isBreak = false;
					if(PP) {
						for(int x1 = x + 1; x1 <= xRight+1; x1++) {
							y += 1;
							if(inRange(x1,y)) {
								addPProtects(x1,y);
								if(isBreak2) {isBreak2 = false;break;}
							}
						}
					}
					break;
				}
		}
	}

	public void checkRightSlope(int space,boolean M,boolean C,boolean P,boolean PP){
		
		pieceBlockType = "RS";
		pieceBlocking = null;
		
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
		//System.out.println("Bottom Left");
		for(int x = posX - 1; x >= xLeft-1; x--) { //Bottom Left
			y += 1;
			if(M) {addMoves(x, y);}
			if(C) {addCaptures(x,y);}
			if(P) {addProtects(x,y);}

			if(isBreak) {
				isBreak = false;
				if(PP) {
					for(int x1 = x - 1; x1 >= xLeft-1; x1--) {
						y += 1;
						if(inRange(x1,y)) {
							addPProtects(x1,y);
							if(isBreak2) {isBreak2 = false;break;}
						}
					}
				}
				break;
			}
		}
			
		y = posY;
		//System.out.println("Top Right");
		for(int x = posX + 1; x  <= xRight+1;x++) { //Top Right
			y -= 1;
			if(M) {addMoves(x, y);}
			if(C) {addCaptures(x,y);}
			if(P) {addProtects(x,y);}

			if(isBreak) {
				isBreak = false;
				if(PP) {
					for(int x1 = x + 1; x1 <= xRight+1; x1++) {
						y -= 1;
						if(inRange(x1,y)) {
							addPProtects(x1,y);
							if(isBreak2) {isBreak2 = false;break;}
						}
					}
				}
				break;
			}	
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
		if(!id.equals("k")) {
		if(((handler.getPieceArrangeBoard().getBKing().isInCheck())&&(c.equals("b"))) 
		||((handler.getPieceArrangeBoard().getWKing().isInCheck())&&(c.equals("w")))) {
		
		String turn = handler.getMouseManager().getSelector().getTurn();
		King bKing = handler.getPieceArrangeBoard().getBKing();
		King wKing = handler.getPieceArrangeBoard().getWKing();
		King king = null;
		blockMove = null;
		ArrayList<int[]> mov = new ArrayList<int[]>();
		ArrayList<int[]> cap = new ArrayList<int[]>();
		
		boolean canBlock = false;
		
		if(handler.getPieceArrangeBoard().getDeliveringCheckList().size() == 1) {
			
			if(turn.equals("w")) {
				king = wKing;
			}else if (turn.equals("b")) {
				king = bKing;
			}
			
			for(Piece piece:handler.getPieceArrangeBoard().getDeliveringCheckList()) {
				for(int[] pos:movables) {
					
					if((piece.getId().equals("r"))||(piece.getId().equals("q"))){ //Checks for blocking vertical and horizontal checks
						canBlock = isInBetween(pos,new int[] {piece.posX,piece.posY},new int[] {king.posX,king.posY},"HV");
						if(canBlock){
						mov.add(pos);
						blockMove = pos;
						}
					}
					if((piece.getId().equals("b"))||(piece.getId().equals("q"))){ // checks for blocking diagonal checks
						canBlock = isInBetween(pos,new int[] {piece.posX,piece.posY},new int[] {king.posX,king.posY},"Diag");
						for(int[] prot:piece.protects) {
							if(canBlock){
								if(Arrays.equals(pos, prot)) {
									mov.add(pos);
									blockMove = pos;
								}			
							}
						}
					}			
				}
				for(int[] pos: capturables) { //Checks if the checking piece can be captured

					if((pos[0]==piece.posX)&&(pos[1]==piece.posY)) {
						cap.add(pos);
						
					}
				}
			}
		}
		
		
		movables = mov;
		capturables = cap;
	}
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

	public boolean isPinned() {
		return isPinned;
	}

	public void setPinned(boolean isPinned) {
		this.isPinned = isPinned;
	}

	public int[] getBlockMove() {
		return blockMove;
	}

	public void setBlockMove(int[] blockMove) {
		this.blockMove = blockMove;
	}

	public String getRestricted() {
		return restricted;
	}

	public void setRestricted(String restricted) {
		this.restricted = restricted;
	}

	public String getpRestriction() {
		return pRestriction;
	}

	public void setpRestriction(String pRestriction) {
		this.pRestriction = pRestriction;
	}

	public ArrayList<int[]> getpProtects() {
		return pProtects;
	}

	public void setpProtects(ArrayList<int[]> pProtects) {
		this.pProtects = pProtects;
	}

	public void setProtects(ArrayList<int[]> protects) {
		this.protects = protects;
	}

	public ArrayList<int[]> getKingRestricts() {
		return kingRestricts;
	}

	public void setKingRestricts(ArrayList<int[]> kingRestricts) {
		this.kingRestricts = kingRestricts;
	}

	public int[] getPos() {
		return pos;
	}

	public void setPos(int[] pos) {
		this.pos = pos;
	}
	
	
	



	
	
	
	
	
	
	


	
	
	
	


}	