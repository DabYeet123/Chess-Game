package board;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import maingame.Handler;

public class BackgroundBoard {
	
	private Handler handler;
	private int width,height;
	protected int size = 64;
	private static Square[][] backBoard;

	

	
	public BackgroundBoard(Handler handler,int width,int height,int size) {
		this.handler = handler;
		this.width = width;
		this.height = height;
		this.size = size;
		
		backBoard = defaultBoard(size);
			
	}
	
	public Square[][] defaultBoard(int size) {
		backBoard = new Square[8][8];
		for(int y = 0; y < 8;y++ ) {
			for(int x = 0; x < 8; x++) {
				if((x%2 == 0 && y%2 == 0)||((x%2 == 1 && y%2 == 1)))
					backBoard[x][y] = new Square(x,y,size, new Color(240, 214, 182),null,"l");
				else
					backBoard[x][y] = new Square(x,y,size, new Color(179, 133, 86),null,"d");
			}
		}
		
		return backBoard;
	}
	

	

	public void tick() {
		
	}
	
	
	public void render(Graphics g) {
		for(Square[] listSquares:backBoard) {
			for(Square square:listSquares) {
				square.render(g);			
			}
		}
	}
	
	public Square[][] getBackBoard(){
		return backBoard;
	}
}
