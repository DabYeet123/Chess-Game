package selector;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

import board.PieceArrangeBoard;
import board.HighlightBoard;
import board.Square;
import maingame.Handler;
import pieces.King;
import pieces.Pawn;
import pieces.Piece;

public class Selector {
	
	private Handler handler;
	private Piece pieceSelected = null;
	private String turn = "w";
	private boolean controlHighlightOn = true;
	private boolean promoting = false;
	
	public Selector(Handler handler) {
		this.handler = handler;
	}
	
	
	public void onMouseMove(MouseEvent e) {
		
	}
	
	public void onMouseClicked(MouseEvent e) {
		Piece[][] pb = handler.getPieceArrangeBoard().getPieceBoard();
		
		int x = e.getX()/64;
		int y = e.getY()/64;
		
		//Moving and Capturing
		if(pieceSelected != null) {
			for(int[] pos:pieceSelected.getMovables()) {
				if(Arrays.equals(pos, new int[]{x,y})) {
					if(pieceSelected.getId().equals("k")) {
						King k = (King)pieceSelected;
						if(k.getShortCastle() == pos) {
							k.shortCastle();
							turnUpdate();
							highlightUpdate();
							return;
						}else if(k.getLongCastle() == pos) {
							k.longCastle();
							turnUpdate();
							highlightUpdate();
							return;
						}
					}
					pieceSelected.moveTo(x,y);
					checkForPromotes();
					if(!promoting) {
						turnUpdate();
						highlightUpdate();
					}
					return;
					
				}
			}
			for(int[] pos:pieceSelected.getCapturables()) {
				if(Arrays.equals(pos, new int[]{x,y})) {
					if(pieceSelected.getId().equals("p")) {
						Pawn p = (Pawn)pieceSelected;
						if(p.getEnPassant() == pos) {
							p.enPassant(pos[0],pos[1]);
							turnUpdate();
							highlightUpdate();
							return;
						}
					}
					pieceSelected.moveTo(x,y);
					checkForPromotes();
					if(!promoting) {
						turnUpdate();
						highlightUpdate();
					}
					return;
				}
			}
			if(pieceSelected.getBounds().contains(e.getX(),e.getY())) {
				pieceSelected = null;
				highlightUpdate();
				return;
			}
		}
		
		
		//Selecting Piece
		if(Piece.inRange(x,y)) {
			if(pb[x][y] != null) {
				if(((turn.equals("w"))&&(pb[x][y].getC().equals("w"))) || 
						((turn.equals("b"))&&(pb[x][y].getC().equals("b")))) {
					if(pb[x][y].getBounds().contains(e.getX(),e.getY())) {
						pieceSelected = pb[x][y];
					}
				}else {
						pieceSelected = null;
					}
			}else {
				pieceSelected = null;
			}
		}
		
		highlightUpdate();
	}
	
	
	
	
	public void highlightUpdate() {
		Square[][] highlights = handler.getHighlightBoard().getHighlights();

		if(pieceSelected != null) {
			
			for(Square[] listSquares:highlights) {
				for(Square square:listSquares) {
					square.setC(null);
				}
			}
			
			controlledHighlightUpdate();
			
			highlights[pieceSelected.getPosX()][pieceSelected.getPosY()].setC(new Color(23, 133, 86));
			
			for(int[] pos:pieceSelected.getMovables()) {
				if(highlights[pos[0]][pos[1]].getId().equals("l")) {
					highlights[pos[0]][pos[1]].setC(new Color(232, 240, 91));
				}else if(highlights[pos[0]][pos[1]].getId().equals("d")) {
					highlights[pos[0]][pos[1]].setC(new Color(104, 166, 12));
				}
			}
				
			for(int[] pos:pieceSelected.getCapturables()) {
				if(highlights[pos[0]][pos[1]].getId().equals("l")) {
					highlights[pos[0]][pos[1]].setC(new Color(255, 117, 102));
				}else if(highlights[pos[0]][pos[1]].getId().equals("d")) {
					highlights[pos[0]][pos[1]].setC(new Color(194, 69, 60));
				}
			}
			
		}else {
			for(Square[] listSquares:highlights) {
				for(Square square:listSquares) {
					square.setC(null);
				}
			}
			controlledHighlightUpdate();
		}
	}
	
	public void controlledHighlightUpdate() { //Updates the area in which the pieces control
		Square[][] chl = handler.getHighlightBoard().getControlHighlights();
		//handler.getPieceArrangeBoard().controlRangeUpdate();
		if(controlHighlightOn) {	
			for(Square[] listSquares:chl) {
				for(Square square:listSquares) {
					square.setC(null);
				}
			}
			/*for(int[] pos:handler.getPieceArrangeBoard().getwPControlRange()) {
				if(chl[pos[0]][pos[1]].getId().equals("l")) {
					chl[pos[0]][pos[1]].setC(new Color(161, 154, 156));
				}else if(chl[pos[0]][pos[1]].getId().equals("d")) {
					chl[pos[0]][pos[1]].setC(new Color(120, 111, 114));
				}
			}*/
			
			for(int[] pos:handler.getPieceArrangeBoard().getWControlRange()) {
				if(chl[pos[0]][pos[1]].getId().equals("l")) {
					chl[pos[0]][pos[1]].setC(new Color(230, 117, 102));
				}else if(chl[pos[0]][pos[1]].getId().equals("d")) {
					chl[pos[0]][pos[1]].setC(new Color(160, 69, 60));
				}
			}
		}else {
			for(Square[] listSquares:chl) {
				for(Square square:listSquares) {
					square.setC(null);
				}
			}
		}
	}
	
	public void turnSwitch() {
		if(turn.equals("w")) {
			turn = "b";
		}else {
			turn = "w";
		}
	}
	
	public void turnUpdate() {
		pieceSelected = null;
		turnSwitch();
		
		for(Pawn pawn:handler.getPieceArrangeBoard().getPawnPieces()) {
			if(((turn.equals("w"))&&(pawn.getC().equals("w"))) || 
					((turn.equals("b"))&&(pawn.getC().equals("b")))){
				if(pawn.isHasMoved()) {
					pawn.setTimesPassed(2);
				}
			}
		}
		for(Piece piece:handler.getPieceArrangeBoard().getPieceList()) {
			piece.setRestricted(null);
			piece.setPinned(false);
		}
		
		if(turn.equals("w")) {
			for(Piece piece:handler.getPieceArrangeBoard().getPieceList()) {
				piece.checkProtects();
			}
			handler.getPieceArrangeBoard().controlRangeUpdate();
			checkForChecks();
			for(Piece piece:handler.getPieceArrangeBoard().getWPieces()) {
				piece.checkMoves();
			}
		}else if(turn.equals("b")) {
			for(Piece piece:handler.getPieceArrangeBoard().getPieceList()) {
				piece.checkProtects();
			}
			handler.getPieceArrangeBoard().controlRangeUpdate();
			checkForChecks();
			for(Piece piece:handler.getPieceArrangeBoard().getBPieces()) {
				piece.checkMoves();
				

			}
		}
		
		/*handler.getPieceArrangeBoard().controlRangeUpdate();
		checkForChecks();*/
		
		if(turn.equals("w")) {
			for(Piece piece:handler.getPieceArrangeBoard().getWPieces()) {
				piece.checkBlocks();
			}
		}else if(turn.equals("b")) {
			for(Piece piece:handler.getPieceArrangeBoard().getBPieces()) {
				piece.checkBlocks();
			}
		}

		

		
		/*for(Piece piece:handler.getPieceArrangeBoard().getPieceList()) {
			piece.checkMoves(true);
			piece.checkProtects();
			if(piece.isPinned()) {
				System.out.println(piece+" : is pinned");
				System.out.println(piece.getPosX()+" "+piece.getPosY());
				System.out.println(piece.getRestricted());
			}
		}*/
		
	}
	
	public void checkForChecks() {
		if(turn.equals("b")) {
			King bKing = handler.getPieceArrangeBoard().getBKing();
			bKing.setInCheck(false);
			for(Piece piece:handler.getPieceArrangeBoard().getWPieces()) {
				if(piece.isDeliveringCheck()) {	
					bKing.setInCheck(true);
					System.out.println("Black is in Check!");
					break;
				}
			}
		}else if(turn.equals("w")) {		
			King wKing = handler.getPieceArrangeBoard().getWKing();
			wKing.setInCheck(false);
			for(Piece piece:handler.getPieceArrangeBoard().getBPieces()) {
				if(piece.isDeliveringCheck()) {	
					wKing.setInCheck(true);
					System.out.println("White is in Check!");
					break;
				}
			}
		}
	}
	
	public void checkForPromotes() {
		for(Pawn pawn:handler.getPieceArrangeBoard().getPawnPieces()) {
			pawn.promotionCheck();
		}
	}
	

		
	
	
	public void tick() {

	}
	
	public void render(Graphics g) {
		handler.getHighlightBoard().render(g);

	}
	
	
	
	
	public String getTurn() {
		return turn;
	}


	public void setTurn(String turn) {
		this.turn = turn;
	}


	public Piece getPieceSelected() {
		return pieceSelected;
	}


	public boolean isControlHighlightOn() {
		return controlHighlightOn;
	}


	public void setControlHighlightOn(boolean controlHighlightOn) {
		this.controlHighlightOn = controlHighlightOn;
	}


	public boolean isPromoting() {
		return promoting;
	}


	public void setPromoting(boolean promoting) {
		this.promoting = promoting;
	}

	
	
	
	
	
}
