package maingame;


import board.BackgroundBoard;
import input.KeyManager;
import input.MouseManager;


public class Handler {
	
	private Game game;
	
	public Handler(Game game) {
		this.game = game;
	}
	
	
	public BackgroundBoard getBackgroundBoard() {
		return game.getBackgroundBoard();
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
