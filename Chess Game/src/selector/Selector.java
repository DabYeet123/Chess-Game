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
					pieceSelected.moveTo(x,y);
					handler.getPieceArrangeBoard().controlRangeUpdate();
					checkForChecks();
					turnUpdate();
					highlightUpdate();
					return;
				}
			}
			for(int[] pos:pieceSelected.getCapturables()) {
				if(Arrays.equals(pos, new int[]{x,y})) {
					if(pieceSelected.getId().equals("p")) {
						Pawn p = (Pawn)pieceSelected;
						if(p.getEnPassant() == pos) {
							p.enPassant(pos[0],pos[1]);
							handler.getPieceArrangeBoard().controlRangeUpdate();
							checkForChecks();
							turnUpdate();
							highlightUpdate();
							return;
						}
					}
					pieceSelected.moveTo(x,y);
					handler.getPieceArrangeBoard().controlRangeUpdate();
					checkForChecks();
					turnUpdate();
					highlightUpdate();
					return;
				}
			}
		}
		
		
		//Selecting Piece
		if(Piece.inRange(x,y)) {
			if(pb[x][y] != null) {
				if(((turn.equals("w"))&&(pb[x][y].getC().equals("w"))) || 
						((turn.equals("b"))&&(pb[x][y].getC().equals("b")))) {
					if(pb[x][y].getBounds().contains(e.getX(),e.getY())) {
						pieceSelected = pb[x][y];
						pieceSelected.checkMoves(true);
					}
				}else {
						pieceSelected = null;
					}
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
		handler.getPieceArrangeBoard().controlRangeUpdate();
		if(controlHighlightOn) {	
			for(Square[] listSquares:chl) {
				for(Square square:listSquares) {
					square.setC(null);
				}
			}
			
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
	}
	
	public void checkForChecks() {
		if(turn.equals("w")) {
			King bKing = handler.getPieceArrangeBoard().getBKing();
			bKing.setInCheck(false);
			for(Piece piece:handler.getPieceArrangeBoard().getWPieces()) {
				if(piece.isDeliveringCheck()) {	
					bKing.setInCheck(true);
					break;
				}
			}
		}else if(turn.equals("b")) {		
			King wKing = handler.getPieceArrangeBoard().getWKing();
			wKing.setInCheck(false);
			for(Piece piece:handler.getPieceArrangeBoard().getBPieces()) {
				if(piece.isDeliveringCheck()) {	
					wKing.setInCheck(true);
					break;
				}
			}
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
	
	
}
