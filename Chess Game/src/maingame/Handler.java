package maingame;


import board.BackgroundBoard;
import board.HighlightBoard;
import board.PieceArrangeBoard;
import input.KeyManager;
import input.MouseManager;


public class Handler {
	
	private Game game;
	private BackgroundBoard backgroungBoard;
	private PieceArrangeBoard pieceArrangeBoard;
	private HighlightBoard highlightBoard;
	
	public Handler(Game game) {
		this.game = game;
	}
	
	public PieceArrangeBoard getPieceArrangeBoard() {
		return pieceArrangeBoard;
	}
	
	public void setPieceArrangeBoard(PieceArrangeBoard pieceArrangeBoard) {
		this.pieceArrangeBoard = pieceArrangeBoard;
	}
	
	
	public BackgroundBoard getBackgroundBoard() {
		return backgroungBoard;
	}

	public void setBackgroungBoard(BackgroundBoard backgroungBoard) {
		this.backgroungBoard = backgroungBoard;
	}

	public HighlightBoard getHighlightBoard() {
		return highlightBoard;
	}

	public void setHighlightBoard(HighlightBoard highlightBoard) {
		this.highlightBoard = highlightBoard;
	}

	public KeyManager getKeyManager() {
		return game.getKeyManager();
	}
	
	public MouseManager getMouseManager() {
		return game.getMouseManager();
	}
	
	public int getWidth() {
		return game.getWidth();
	}
	
	public int getHeight() {
		return game.getHeight();
	}


	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}


}
