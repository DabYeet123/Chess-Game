package gfx;

import java.awt.image.BufferedImage;

public class Assets {
	
	private static final int width = 64, height = 64;
	
	public static BufferedImage w_king,w_queen,w_bishop,w_knight,w_rook,w_pawn;
	public static BufferedImage b_king,b_queen,b_bishop,b_knight,b_rook,b_pawn;
	public static BufferedImage server,client;
	public static BufferedImage[] btn_start;
	public static BufferedImage[] btn_queen = new BufferedImage[2];
	public static BufferedImage[] btn_bishop = new BufferedImage[2];
	public static BufferedImage[] btn_knight = new BufferedImage[2];
	public static BufferedImage[] btn_rook = new BufferedImage[2];
	public static BufferedImage[] btn_server = new BufferedImage[2];
	public static BufferedImage[] btn_client = new BufferedImage[2];
	
	public static void init() {
		SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage("/textures/chess_sprite_sheet(64x).png"));
		
		//SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage("/textures/chess_sprite_sheet(64x).png"));
		
		w_king = sheet.crop(0, 0, width, height);
		w_queen = sheet.crop(width, 0, width, height);
		w_bishop = sheet.crop(2*width, 0, width, height);
		w_knight = sheet.crop(3*width, 0, width, height);
		w_rook = sheet.crop(4*width, 0, width, height);
		w_pawn = sheet.crop(5*width, 0, width, height);
		
		b_king = sheet.crop(0, height, width, height);
		b_queen = sheet.crop(width, height, width, height);
		b_bishop = sheet.crop(2*width, height, width, height);
		b_knight = sheet.crop(3*width, height, width, height);
		b_rook = sheet.crop(4*width, height, width, height);
		b_pawn = sheet.crop(5*width, height, width, height);
		
		server = ImageLoader.loadImage("/textures/s.jpg");
		client = ImageLoader.loadImage("/textures/client.png");

		btn_start = new BufferedImage[2];
		btn_start[0] = b_king;
		btn_start[1] = w_king;
		
		btn_queen[0] = w_queen;
		btn_queen[1] = b_queen;
		
		btn_bishop[0] = w_bishop;
		btn_bishop[1] = b_bishop;
		
		btn_knight[0] = w_knight;
		btn_knight[1] = b_knight;
		
		btn_rook[0] = w_rook;
		btn_rook[1] = b_rook;
		
		btn_server[0] = server;
		btn_server[1] = server;
		
		btn_client[0] = client;
		btn_client[1] = client;
		

	}

}
