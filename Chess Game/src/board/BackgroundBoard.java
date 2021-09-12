package board;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import maingame.Handler;

public class BackgroundBoard {
	
	private Handler handler;
	private int width,height;
	private int size = 64;
	private static int[][] backBoard;
	

	

	
	public BackgroundBoard(Handler handler,int width,int height,int size) {
		this.handler = handler;
		this.width = width;
		this.height = height;
		this.size = size;
		
		backBoard = new int[width][height];
		for(int y = 0; y < width;y++ ) {
			for(int x = 0; x < height; x++) {
				if((x%2 == 0 && y%2 == 0)||((x%2 == 1 && y%2 == 1)))
					backBoard[x][y] = 0;
				else
					backBoard[x][y] = 1;
			}
		}
	}
	
	public static void defaultBoard(int size) {
		backBoard = new int[][] {
					 {0,1,0,1,0,1,0,1},
		             {1,0,1,0,1,0,1,0},
		             {0,1,0,1,0,1,0,1},
		             {1,0,1,0,1,0,1,0},
		             {0,1,0,1,0,1,0,1},
		             {1,0,1,0,1,0,1,0},
		             {0,1,0,1,0,1,0,1},
		             {1,0,1,0,1,0,1,0},
		             };
	}
	

	

	public void tick() {
		
	}
	
	
	public void render(Graphics g) {
		for(int y = 0; y < width;y++ ) {
			for(int x = 0; x < height; x++) {
				if(backBoard[x][y] == 0) {
					System.out.println("0");
					g.setColor(Color.black);
					g.fillRect(x*size, y*size, size, size);
				}
				else if(backBoard[x][y] == 1) {
					System.out.println("1");
					g.setColor(Color.white);
					g.fillRect(x*size, y*size, size, size);
				}
			}
		}

	}
}
