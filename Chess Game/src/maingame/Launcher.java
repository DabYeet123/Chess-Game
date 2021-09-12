package maingame;

import board.BackgroundBoard;
import display.Display;

public class Launcher {
	
	public static void main(String[] args) {
		Game game = new Game("Chess",640,640);
		game.start();
	}

}
