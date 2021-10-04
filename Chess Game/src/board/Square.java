package board;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Square {
	
	protected Rectangle bounds;
	protected int x, y;
	protected int size;
	protected BufferedImage texture;
	protected Color c;
	protected String id;
	
	public Square(int x, int y, int size,Color c,BufferedImage texture,String id) {
		
		this.x = x;
		this.y = y;
		this.size = size;
		this.c = c;
		this.texture = texture;
		this.id = id;
		
		bounds = new Rectangle(x*size,y*size,size,size);
	}
	
	public void tick() {
		
	}
	
	
	public void render(Graphics g) {
		if(texture != null) {
			g.drawImage(texture, x, y, size, size, null);
		}else {
			if(c != null) {
			g.setColor(c);
			g.fillRect(x*size, y*size, size, size);
			}
		}
	}
	
	

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public BufferedImage getTexture() {
		return texture;
	}

	public void setTexture(BufferedImage texture) {
		this.texture = texture;
	}

	public Color getC() {
		return c;
	}

	public void setC(Color c) {
		this.c = c;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
