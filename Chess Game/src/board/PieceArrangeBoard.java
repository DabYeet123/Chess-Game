package board;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;

import gfx.Assets;
import maingame.Handler;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;
import utils.Utils;

public class PieceArrangeBoard {
	
	private Handler handler;
	private int width,height;
	private int size;
	private Piece[][] pieceBoard;
	private ArrayList<Piece> pieceList;
	private ArrayList<Piece> wPieces;
	private ArrayList<Piece> bPieces;
	private ArrayList<Pawn> pawnPieces;
	
	private King wKing;
	private King bKing;
	
	private ArrayList<int[]> wControlRange = new ArrayList<int[]>();
	private ArrayList<int[]> bControlRange = new ArrayList<int[]>();
	
	private ArrayList<Piece> deliveringCheckList = new ArrayList<Piece>();
	
	
	public PieceArrangeBoard(Handler handler, String path) {
		this.handler = handler;
		loadBoard(path);
	}
	
	private void loadBoard(String path) {
		String file = Utils.loadFileAsString(path);
		String[] tokens = file.split("\\s+");
		width = Utils.parseInt(tokens[0]);
		height = Utils.parseInt(tokens[1]);
		size = Utils.parseInt(tokens[2]);
		

			
		pieceBoard = new Piece[width][height];
		for(int y = 0; y<height;y++) {
			for(int x = 0;x<width;x++) {
				String t = tokens[(x + y * width) + 3];
				String c = t.substring(0,1);
				String id = t.substring(1,2);
				if(c .equals("w")) {
					if(id.equals("k")) {wKing = new King(handler,x,y,size,id,c,Assets.w_king); pieceBoard[x][y] = wKing;}
					else if (id.equals("q")){pieceBoard[x][y] = new Queen(handler,x,y,size,id,c,Assets.w_queen);}
					else if (id.equals("r")){pieceBoard[x][y] = new Rook(handler,x,y,size,id,c,Assets.w_rook);}
					else if (id.equals("b")) {pieceBoard[x][y] = new Bishop(handler,x,y,size,id,c,Assets.w_bishop);}
					else if (id.equals("n")) {pieceBoard[x][y] = new Knight(handler,x,y,size,id,c,Assets.w_knight);}
					else if (id.equals("p")) {pieceBoard[x][y] = new Pawn(handler,x,y,size,id,c,Assets.w_pawn);}
	
				}else if(c .equals("b")) {
					if(id.equals("k")) {bKing = new King(handler,x,y,size,id,c,Assets.b_king);pieceBoard[x][y] = bKing;}
					else if (id.equals("q")){pieceBoard[x][y] = new Queen(handler,x,y,size,id,c,Assets.b_queen);}
					else if (id.equals("r")){pieceBoard[x][y] = new Rook(handler,x,y,size,id,c,Assets.b_rook);}
					else if (id.equals("b")) {pieceBoard[x][y] = new Bishop(handler,x,y,size,id,c,Assets.b_bishop);}
					else if (id.equals("n")) {pieceBoard[x][y] = new Knight(handler,x,y,size,id,c,Assets.b_knight);}
					else if (id.equals("p")) {pieceBoard[x][y] = new Pawn(handler,x,y,size,id,c,Assets.b_pawn);}
					}
				}
			}
		
		pieceList = new ArrayList<Piece>();
		wPieces = new ArrayList<Piece>();
		bPieces = new ArrayList<Piece>();
		pawnPieces = new ArrayList<Pawn>();
		for(int y = 0; y<height;y++) {
			for(int x = 0;x<width;x++) {
				if(pieceBoard[x][y] != null) {
					pieceList.add(pieceBoard[x][y]);
					if(pieceBoard[x][y].getC().equals("w")) {
						wPieces.add(pieceBoard[x][y]);
					}else {
						bPieces.add(pieceBoard[x][y]);
					}
					if(pieceBoard[x][y].getId().equals("p")){
						pawnPieces.add((Pawn)pieceBoard[x][y]);
					}
				}	
				}
			}
		}
	
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		for(int y = 0;y < pieceBoard.length;y++) {
			for(int x = 0; x < pieceBoard[y].length;x++) {
				if(getPiece(x,y) != null)
					getPiece(x,y).render(g,x*size,y*size);
			}
	}
	
}
	
	public Piece getPiece(int x, int y) {
		if(x <0 || y<0 || x >= height || y>= height) {
			return null;}
		Piece[][] board = pieceBoard;
		Piece p = board[x][y];
		if(p == null) {
			return null;}
		return p;
	}
	
	public void controlRangeUpdate() {
		//String turn = handler.getMouseManager().getSelector().getTurn();
		
		ArrayList<int[]> wcr = new ArrayList<int[]>();
		ArrayList<int[]> bcr = new ArrayList<int[]>();
		ArrayList<Piece> dcl = new ArrayList<Piece>();
		
		int[] posW = new int[] {wKing.getPosX(),wKing.getPosY()};
		int[] posB = new int[] {bKing.getPosX(),bKing.getPosY()};
		
		//if(turn.equals("w")) {
			for(Piece piece: wPieces) {
				piece.setDeliveringCheck(false);
				piece.checkProtects();
				for(int[] pos:piece.getProtects()) {
					if(Arrays.equals(pos, posB)) {
						piece.setDeliveringCheck(true);
						dcl.add(piece);
					}
					
					if(!wcr.contains(pos)) {
						wcr.add(pos);
					}
				}
			setWControlRange(wcr);
			}
		//}
		//if(turn.equals("b")) {
			for(Piece piece: bPieces) {
				piece.setDeliveringCheck(false);
				piece.checkProtects();
				for(int[] pos:piece.getProtects()) {
					if(Arrays.equals(pos, posW)) {
						piece.setDeliveringCheck(true);
						dcl.add(piece);
					}
					
					if(!bcr.contains(pos)) {
						bcr.add(pos);
					}
				}
			setBControlRange(bcr);
			}
		//}
		
		deliveringCheckList = dcl;
	}
	
	public King getWKing() {
		return wKing;
	}
	
	public King getBKing() {
		return bKing;
	}
	
	public ArrayList<Piece> getPieceList(){
		return pieceList;
	}
	public ArrayList<Piece> getWPieces(){
		return wPieces;
	}
	public ArrayList<Piece> getBPieces(){
		return bPieces;
	}
	public ArrayList<Pawn> getPawnPieces(){
		return pawnPieces;
	}
	
	public ArrayList<int[]> getWControlRange(){
		return wControlRange;
	}
	public void setWControlRange(ArrayList<int[]> wControlRange) {
		this.wControlRange = wControlRange;
	}
	public ArrayList<int[]> getBControlRange(){
		return bControlRange;
	}
	public void setBControlRange(ArrayList<int[]> bControlRange) {
		this.bControlRange = bControlRange;
	}


	public ArrayList<Piece> getDeliveringCheckList() {
		return deliveringCheckList;
	}

	public void setDeliveringCheckList(ArrayList<Piece> deliveringCheckList) {
		this.deliveringCheckList = deliveringCheckList;
	}

	public Piece[][] getPieceBoard(){
		return pieceBoard;
	}
	
	
	
	public int getSize() {
		return size;
	}
}