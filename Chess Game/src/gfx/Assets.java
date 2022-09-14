package gfx;

import java.awt.image.BufferedImage;

public class Assets {
	
	private static final int width = 64, height = 64;
	
	public static BufferedImage w_king,w_queen,w_bishop,w_knight,w_rook,w_pawn;
	public static BufferedImage b_king,b_queen,b_bishop,b_knight,b_rook,b_pawn;
	public static BufferedImage sp_blue,sp_aqua,sp_orange,mp_blue,mp_aqua,mp_orange,opt_blue,opt_aqua,opt_orange,back_black,back_gray;
	public static BufferedImage server,client;
	public static BufferedImage chessBackground;
	public static BufferedImage[] btn_start;
	public static BufferedImage[] btn_queen = new BufferedImage[2];
	public static BufferedImage[] btn_bishop = new BufferedImage[2];
	public static BufferedImage[] btn_knight = new BufferedImage[2];
	public static BufferedImage[] btn_rook = new BufferedImage[2];
	public static BufferedImage[] btn_server = new BufferedImage[2];
	public static BufferedImage[] btn_client = new BufferedImage[2];
	public static BufferedImage[] btn_single = new BufferedImage[2];
	public static BufferedImage[] btn_multi = new BufferedImage[2];
	public static BufferedImage[] btn_opt = new BufferedImage[2];
	public static BufferedImage[] btn_back = new BufferedImage[2];
	
	public static void init() {
		SpriteSheet chess_sheet = new SpriteSheet(ImageLoader.loadImage("/textures/chess_sprite_sheet(64x).png"));
		SpriteSheet button_sheet = new SpriteSheet(ImageLoader.loadImage("/textures/menuButtons.png"));
		
		//SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage("/textures/chess_sprite_sheet(64x).png"));
		
		w_king = chess_sheet.crop(0, 0, width, height);
		w_queen = chess_sheet.crop(width, 0, width, height);
		w_bishop = chess_sheet.crop(2*width, 0, width, height);
		w_knight = chess_sheet.crop(3*width, 0, width, height);
		w_rook = chess_sheet.crop(4*width, 0, width, height);
		w_pawn = chess_sheet.crop(5*width, 0, width, height);
		
		b_king = chess_sheet.crop(0, height, width, height);
		b_queen = chess_sheet.crop(width, height, width, height);
		b_bishop = chess_sheet.crop(2*width, height, width, height);
		b_knight = chess_sheet.crop(3*width, height, width, height);
		b_rook = chess_sheet.crop(4*width, height, width, height);
		b_pawn = chess_sheet.crop(5*width, height, width, height);
		
		sp_blue = button_sheet.crop(10, 241, 300, 80);
		sp_aqua = button_sheet.crop(440, 241, 300, 80);
		sp_orange = button_sheet.crop(831, 238, 300, 80);
		mp_blue = button_sheet.crop(10, 378, 300, 80);
		mp_aqua = button_sheet.crop(440, 378, 300, 80);
		mp_orange = button_sheet.crop(831, 377, 300, 80);
		opt_blue = button_sheet.crop(11, 496, 300, 80);
		opt_aqua = button_sheet.crop(441, 496, 300, 80);
		opt_orange = button_sheet.crop(831, 496, 300, 80);
		
		
		server = ImageLoader.loadImage("/textures/s.jpg");
		client = ImageLoader.loadImage("/textures/client.png");
		chessBackground = ImageLoader.loadImage("/textures/chessbg_2.jpg");
		back_black = ImageLoader.loadImage("/textures/backArrow.png");
		back_gray = ImageLoader.loadImage("/textures/backArrow2.png");

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
		
		btn_single[0] = sp_blue;
		btn_single[1] = sp_aqua;
		
		btn_multi[0] = mp_blue;
		btn_multi[1] = mp_aqua;
		
		btn_opt[0] = opt_aqua;
		btn_opt[1] = opt_orange;
		
		btn_back[0] = back_black;
		btn_back[1] = back_gray;

	}

}
