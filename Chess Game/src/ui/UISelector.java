package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import maingame.Handler;
import pieces.Piece;

public class UISelector extends UIObject {
	
	private BufferedImage[] images;
	private ClickListener clicker;
	private Handler handler;
	private static boolean selected = false;
	private Piece pieceSelected = null;
	private Rectangle selectBox;

	public UISelector(float x, float y, int width, int height,ClickListener clicker,Handler handler) {
		super(x, y, width, height);
		this.clicker = clicker;
		this.handler = handler;
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		if(pieceSelected != null) {
		g.setColor(new Color(0, 133, 86));
		g.fillRect(pieceSelected.getPixX(),pieceSelected.getPixY(),pieceSelected.getSize(),pieceSelected.getSize());
		}
	}

	@Override
	public void onClick() {
		
	}
	

	public void select(Piece piece) {
			
	}
	
	@Override
	public void onMouseMove(MouseEvent e) {	
		//Selector
		
	}
	
	@Override
	public void onMouseRelease(MouseEvent e) {
		//Buttons
		ArrayList<Piece> pl = handler.getPieceArrangeBoard().getPieceList();
		for(int x = 0; x < pl.size();x++)
			if(pl.get(x).getBounds().contains(e.getX(),e.getY())) {
				pieceSelected = pl.get(x);
			}
		onClick();
	}

}
