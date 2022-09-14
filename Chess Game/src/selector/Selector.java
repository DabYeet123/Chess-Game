package selector;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import board.PieceArrangeBoard;
import board.HighlightBoard;
import board.Square;
import gfx.Assets;
import maingame.Handler;
import moveinfo.MoveInfo;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

public class Selector {
	
	private Handler handler;
	private MoveInfo moveInfo;
	private Piece pieceSelected = null;
	private boolean isDragging = false;
	private int clickCount = 0;
	private int[] originalPos;
	private int[] movedTo;
	private Piece pieceMoved;
	private String action;
	private Piece captured = null;
	
	private String turn = "w";
	private String checkMate = null;
	private boolean staleMate = false;
	
	private boolean controlHighlightOn = false;
	private boolean promoting = false;
	
	private ArrayList<String[]> rewinds = new ArrayList<String[]>();
	
	public Selector(Handler handler) {
		this.handler = handler;
		moveInfo = new MoveInfo(handler);
	}
	
	public void onMouseDrag(MouseEvent e) {
		if(pieceSelected!=null) {
			pieceSelected.setPixX(e.getX()-pieceSelected.getSize()/2);
			pieceSelected.setPixY(e.getY()-pieceSelected.getSize()/2);
		}
	}
	
	public void onMousePress(MouseEvent e) {
		
		Piece[][] pb = handler.getPieceArrangeBoard().getPieceBoard();
		
		int x = e.getX()/64;
		int y = e.getY()/64;
		
		if(!isDragging) {
			onMouseRelease(e);
		}
		
		if(Piece.inRange(x,y)) {
			if(pb[x][y] != null) {
				if(((turn.equals("w"))&&(pb[x][y].getC().equals("w"))&&(handler.getColor()=='w')) || 
						((turn.equals("b"))&&(pb[x][y].getC().equals("b")&&(handler.getColor()=='b')))) {
					if(pb[x][y].getBounds().contains(e.getX(),e.getY())) {
						if(pb[x][y]!=pieceSelected)
							clickCount = 0;
						pieceSelected = pb[x][y];
						originalPos = new int[] {x,y};
					}
				}else if(!handler.isConnected()){
					if(((turn.equals("w"))&&(pb[x][y].getC().equals("w")))|| 
						((turn.equals("b"))&&(pb[x][y].getC().equals("b")))) {
						if(pb[x][y].getBounds().contains(e.getX(),e.getY())) {
							if(pb[x][y]!=pieceSelected)
								clickCount = 0;
							pieceSelected = pb[x][y];
							originalPos = new int[] {x,y};
						}
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
	
	public void onMouseRelease(MouseEvent e) {
			if(pieceSelected!=null) {
				pieceSelected.refresh();
			}
			//System.out.println(clickCount);
			isDragging = false;
			clickCount++;
			
			Piece[][] pb = handler.getPieceArrangeBoard().getPieceBoard();
			captured = null;
			
			int x = e.getX()/64;
			int y = e.getY()/64;
			//Moving and Capturing
			if(pieceSelected != null) {
				pieceMoved = (Piece) pieceSelected;
				for(int[] pos:pieceSelected.getMovables()) {
					if(Arrays.equals(pos, new int[]{x,y})) {
						if(pieceSelected.getId().equals("k")) {
							King k = (King)pieceSelected;
							if(k.getShortCastle() == pos) {
								k.shortCastle();
								movedTo = pos;
								action = "sc";
								turnUpdate(null);
								highlightUpdate();
								return;
							}else if(k.getLongCastle() == pos) {
								k.longCastle();
								movedTo = pos;
								action = "lc";
								turnUpdate(null);
								highlightUpdate();
								return;
							}
						}
						pieceSelected.moveTo(x,y);
						movedTo = pos;
						action = "m";
						checkForPromotes();
						if(promoting) {
							action = "p";
						}
						if(!promoting) {
							turnUpdate(null);
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
								movedTo = pos;
								action = "c";
								captured = pb[0][1];
								turnUpdate(null);
								return;
							}
						}
						pieceSelected.moveTo(x,y);
						movedTo = pos;
						action = "c";
						captured = pb[0][1];
						checkForPromotes();
						if(promoting) {
							action = "p";
						}
						if(!promoting) {
							turnUpdate(null);
						}
						return;
					}
				}
				if(pieceSelected.getBounds().contains(e.getX(),e.getY())) {
					if(!isDragging) {
						if(clickCount >=2) {
							pieceSelected = null;
							highlightUpdate();
							clickCount = 0;
							return;
						}
					}
				}
			}
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

		
		if(turn.equals("w")) {
			for(Piece piece:handler.getPieceArrangeBoard().getWPieces()) {
				piece.checkBlocks();
			}
		}else if(turn.equals("b")) {
			for(Piece piece:handler.getPieceArrangeBoard().getBPieces()) {
				piece.checkBlocks();
			}
		}

		
		highlightUpdate();
	}
	
	public void turnUpdate(String input) {
		
		String move = "";
		move += pieceMoved.getC()+ pieceMoved.getId()+originalPos[0]+originalPos[1]+action+movedTo[0]+movedTo[1];
		moveInfo.addMoveInfo(move);

		interpretMove(input);
		addToRewinds(pieceSelected, originalPos, movedTo, action, captured);
		
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

		
		if(turn.equals("w")) {
			for(Piece piece:handler.getPieceArrangeBoard().getWPieces()) {
				piece.checkBlocks();
			}
		}else if(turn.equals("b")) {
			for(Piece piece:handler.getPieceArrangeBoard().getBPieces()) {
				piece.checkBlocks();
			}
		}

		checkForMates();
		highlightUpdate();
		handler.getGame().render();

		
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
	
	public void checkForMates() {
		boolean breaking = false;
		ArrayList<Piece> pieceList = null;
		King king = null;
		if(turn.equals("w")) {
			king = handler.getPieceArrangeBoard().getWKing();
			pieceList = handler.getPieceArrangeBoard().getWPieces();
		}else if(turn.equals("b")) {
			king = handler.getPieceArrangeBoard().getBKing();
			pieceList = handler.getPieceArrangeBoard().getBPieces();
		}
		for(Piece piece:pieceList) {
			if(!breaking) {
				for(int[] mov:piece.getMovables()) {
					if(mov!=null) {
						breaking = true;
						break;
					}
				}
				if(!breaking) {
					for(int[] cap:piece.getCapturables()) {
						if(cap!=null) {
							breaking = true;
							break;
						}
					}
				}
			}else {
				break;
			}
		}
		if(!breaking) {
			if(king.isInCheck()) {
				System.out.println("checkworked");
				checkMate = king.getC();
			}else {
				System.out.println("staleworked");
				staleMate = true;
			}
		}
		
	}
	
	public void interpretMove(String input) {
		if(handler.isConnected()) {
		int posX = 0;
		int posY = 0;
		int moveX = 0;
		int moveY = 0;
		if(originalPos!=null) {
			posX = originalPos[0];
			posY = originalPos[1];
		}
		if(movedTo!=null) {
			moveX = movedTo[0];
			moveY = movedTo[1];
		}
		String data = Integer.toString(posX)+Integer.toString(posY)+Integer.toString(moveX)+Integer.toString(moveY);
		int intData = Integer.parseInt(data);
		
		if((turn.equals("w") && handler.getColor() != 'w')||(turn.equals("b") && handler.getColor() != 'b')) {
			if(input!=null) {
				posX = 7 - Integer.parseInt(input.substring(0, 1));
				posY = 7 - Integer.parseInt(input.substring(1,2));
				moveX = 7 - Integer.parseInt(input.substring(2,3));
				moveY = 7 - Integer.parseInt(input.substring(3,4));
			}
			pieceSelected = handler.getPieceArrangeBoard().getPieceBoard()[posX][posY];
			executeRecievedMove(pieceSelected,moveX,moveY);
		}else if((turn.equals("w") && handler.getColor() == 'w')||(turn.equals("b") && handler.getColor() == 'b')) {
			if(handler.isConnected()) {
				try {
					handler.getOPS().writeInt(intData);
					handler.getOPS().flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		}
	}
	
	public void executeRecievedMove(Piece piece,int x,int y) {
		if(piece != null) {
			for(int[] pos:piece.getMovables()) {
				if(Arrays.equals(pos, new int[]{x,y})) {
					if(piece.getId().equals("k")) {
						King k = (King)piece;
						if(k.getShortCastle() == pos) {
							k.shortCastle();
							return;
						}else if(k.getLongCastle() == pos) {
							k.longCastle();
							return;
						}
					}
					piece.moveTo(x,y);
					return;
					
				}
			}
			for(int[] pos:piece.getCapturables()) {
				if(Arrays.equals(pos, new int[]{x,y})) {
					if(piece.getId().equals("p")) {
						Pawn p = (Pawn)piece;
						if(p.getEnPassant() == pos) {
							p.enPassant(pos[0],pos[1]);
							return;
						}
					}
					piece.moveTo(x,y);
					return;
				}
			}
		}
	}
	
	public void rewind() {

		if(turn.equals("w")) {
			String bMove = rewinds.get(rewinds.size()-1)[1];
			//String wMove = rewinds.get(rewinds.size()-1)[0];
			rewindMove(bMove);
			turnUpdate();
			/*rewindMove(wMove);
			turnUpdate();
			rewinds.remove(rewinds.size()-1);
			rewinds.trimToSize();*/
		}else if(turn.equals("b")) {
			String wMove = rewinds.get(rewinds.size()-1)[0];
			//String bMove = rewinds.get(rewinds.size()-2)[1];
			rewindMove(wMove);
			turnUpdate();
			/*rewindMove(bMove);
			turnUpdate();*/
			rewinds.remove(rewinds.size()-1);
			rewinds.trimToSize();
		}
	}
	
	public void rewindMove(String move) {
		Piece[][] pb = handler.getPieceArrangeBoard().getPieceBoard();
		ArrayList<Piece> prm = handler.getPieceArrangeBoard().getPieceRemoved();
		Piece capture = null;
		int[] original = boardPosToInt(move.substring(2,4));
		int[] piecePos = boardPosToInt(move.substring(5,7));
		int originalX = original[0];
		int originalY = original[1];
		int piecePosX = piecePos[0];
		int piecePosY = piecePos[1];
		
		for(int x = prm.size()-1;x>=0;x--) {
			if(Arrays.equals(prm.get(x).getPos(),piecePos)){
				capture = prm.get(x);
				break;
			}
		}
		
		pb[piecePosX][piecePosY].moveTo(originalX, originalY,false);
		if(capture!=null) {
			capture.reAdd();
		}
	}
	
	public void addToRewinds(Piece pieceSelected,int[] originalPos,int[] movedTo,String action,Piece captured) {
		String idcap = null;
		String id = null;
		String move = null;
		
		char originalX = convertToLetter(originalPos[0]);
		String originalY = Integer.toString(converToBoardNum(originalPos[1]));
		char movedToX = convertToLetter(movedTo[0]);
		String movedToY = Integer.toString(converToBoardNum(movedTo[1]));
		if(pieceSelected!=null) {
			id = pieceSelected.getC()+pieceSelected.getId();
		}
		if(captured!=null) {
			idcap = captured.getC()+captured.getId();
			move = id+originalX+originalY+action+movedToX+movedToY+ idcap;
		}else {
			move = id+originalX+originalY+action+movedToX+movedToY;
		}

		if(turn.equals("w")) {
			rewinds.add(new String[] {move,""});
		}else if(turn.equals("b")) {
			rewinds.get(rewinds.size()-1)[1] = move;
		}
		
		//System.out.println(move);

	}
	
	public char convertToLetter(int num) {
		int letterNum = (int)'a'+ num;
		char letter = (char)letterNum;
		if(handler.getPieceArrangeBoard().isWhiteOnTop()) {
			letterNum = (int)'h'- num;
			letter = (char)letterNum;
		}
		
		return letter;
	}
	
	public int converToBoardNum(int num) {
		int boardNum = 8-num;
		if(handler.getPieceArrangeBoard().isWhiteOnTop()) {
			boardNum = num+1;
		}
		
		return boardNum;
	}
	
	public int[] boardPosToInt(String pos) {
		int x = (int)pos.charAt(0)-(int)'a';
		int y = 8 - Integer.parseInt(pos.substring(1, 2));
		if(handler.getPieceArrangeBoard().isWhiteOnTop()) {
			x = 7-((int)pos.charAt(0)-(int)'a');
			y = Integer.parseInt(pos.substring(1, 2)) -1;
		}
		//System.out.println(x+" "+y);
		return new int[] {x,y};
	}
		
	public void mateDisplay(Graphics g) {
		g.setFont(new Font("Dialog", Font.PLAIN, 50));
		g.setColor(Color.BLACK);
		if(staleMate) {
			g.drawString("Stale Mate!", 265, 600);
		}else if(checkMate!=null) {
			if(checkMate.equals("w")) {
				g.drawString("Black Wins!", 265, 600);
			}else if(checkMate.equals("b")) {
				g.drawString("White Wins!", 265, 600);
			}
		}
	}
	
	
	
	
	
	
	public void tick() {

	}
	
	public void render(Graphics g) {
		handler.getHighlightBoard().render(g);
		mateDisplay(g);
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


	public String getCheckMate() {
		return checkMate;
	}


	public void setCheckMate(String checkMate) {
		this.checkMate = checkMate;
	}


	public boolean isStaleMate() {
		return staleMate;
	}


	public void setStaleMate(boolean staleMate) {
		this.staleMate = staleMate;
	}


	public boolean isDragging() {
		return isDragging;
	}


	public void setDragging(boolean isDragging) {
		this.isDragging = isDragging;
	}

	public int[] getOriginalPos() {
		return originalPos;
	}

	public void setOriginalPos(int[] originalPos) {
		this.originalPos = originalPos;
	}

	public int[] getMovedTo() {
		return movedTo;
	}

	public void setMovedTo(int[] movedTo) {
		this.movedTo = movedTo;
	}

	public Piece getPieceMoved() {
		return pieceMoved;
	}

	public void setPieceMoved(Piece pieceMoved) {
		this.pieceMoved = pieceMoved;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Piece getCaptured() {
		return captured;
	}

	public void setCaptured(Piece captured) {
		this.captured = captured;
	}

	
	
	
	
	
}
