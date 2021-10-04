package board;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import maingame.Handler;

public class HighlightBoard {

	private Handler handler;
	private int width,height;
	protected int size = 64;
	private static Square[][] highlightBoard;
	private static Square[][] controlHighlight;
	
	
	public HighlightBoard(Handler handler,int width,int height,int size) {
		this.handler = handler;
		this.width = width;
		this.height = height;
		this.size = size;
		
		highlightBoard = new Square[width][height];
		for(int y = 0; y < width;y++ ) {
			for(int x = 0; x < height; x++) {
				if((x%2 == 0 && y%2 == 0)||((x%2 == 1 && y%2 == 1)))
					highlightBoard[x][y] = new Square(x,y,size, null,null,"l");
				else
					highlightBoard[x][y] = new Square(x,y,size, null,null,"d");
			}
		}
		controlHighlight = new Square[width][height];
		for(int y = 0; y < width;y++ ) {
			for(int x = 0; x < height; x++) {
				if((x%2 == 0 && y%2 == 0)||((x%2 == 1 && y%2 == 1)))
					controlHighlight[x][y] = new Square(x,y,size, null,null,"l");
				else
					controlHighlight[x][y] = new Square(x,y,size, null,null,"d");
			}
		}
	}
	
	
	public void tick() {
		
		}
	
	
	public void render(Graphics g) {
		for(Square[] listSquares:controlHighlight) {
			for(Square square:listSquares) {
				square.render(g);			
			}
		}
		for(Square[] listSquares:highlightBoard) {
			for(Square square:listSquares) {
				square.render(g);			
			}
		}
	}
	
	public Square[][] getHighlights() {
		return highlightBoard;
	}
	public Square[][] getControlHighlights() {
		return controlHighlight;
	}
}
