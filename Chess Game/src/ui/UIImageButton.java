package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import board.Square;

public class UIImageButton extends UIObject{
	
	private BufferedImage[] images;
	private ClickListener clicker;
	private int notHovered;
	private int hovered;
	private Color notHoveredSquare;
	private Color hoveredSquare;

	public UIImageButton(float x, float y, int width, int height, 
			BufferedImage[] images,int notHovered,int hovered,
			Color notHoveredSquare,Color hoveredSquare, ClickListener clicker) {
		
		super(x, y, width, height);
		this.images = images;
		this.clicker = clicker;
		this.notHovered = notHovered;
		this.hovered = hovered;
		this.notHoveredSquare = notHoveredSquare;
		this.hoveredSquare = hoveredSquare;
	}

	@Override
	public void tick() {}

	@Override
	public void render(Graphics g) {
		if(hovering) {
			if(hoveredSquare!=null) {
				g.setColor(hoveredSquare);
				g.fillRect((int) x,(int) y, width, height);
			}
			g.drawImage(images[hovered],(int) x,(int) y, width, height, null);
		}else {
			if(notHoveredSquare!=null) {
				g.setColor(notHoveredSquare);
				g.fillRect((int) x,(int) y, width, height);
			}
			g.drawImage(images[notHovered],(int) x,(int) y, width, height, null);
		}
	}

	@Override
	public void onClick() {
		clicker.onClick();
		
	}

}
