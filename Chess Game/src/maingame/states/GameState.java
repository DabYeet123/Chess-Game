package maingame.states;

import java.awt.Graphics;
import java.io.IOException;

import board.BackgroundBoard;
import board.HighlightBoard;
import board.PieceArrangeBoard;
import gfx.Assets;
import maingame.Game;
import maingame.Handler;
import pieces.King;
import pieces.Piece;
import selector.Selector;
import ui.ClickListener;
import ui.UIImageButton;
import ui.UIManager;
import ui.UISelector;


public class GameState extends State{
	
	private BackgroundBoard backgroundBoard;
	private PieceArrangeBoard pieceBoard;
	private HighlightBoard highlightBoard;
	private UIManager uiManager;
	private Selector selector;
	
	public GameState(Handler handler) {
		super(handler);
		
		backgroundBoard = new BackgroundBoard(handler,8,8,64);
		handler.setBackgroungBoard(backgroundBoard);
		
		pieceBoard = new PieceArrangeBoard(handler,"/saves/default.txt");
		handler.setPieceArrangeBoard(pieceBoard);
		
		highlightBoard = new HighlightBoard(handler, 8, 8, 64);
		handler.setHighlightBoard(highlightBoard);
		
		
		selector = new Selector(handler);
		handler.getMouseManager().setSelector(selector);
		
	}


		



	@Override
	public void tick() {
		uiManager.tick();
		if(handler.isConnected()) {
			if((handler.getMouseManager().getSelector().getTurn().equals("w") && handler.getColor() != 'w')||
				(handler.getMouseManager().getSelector().getTurn().equals("b") && handler.getColor() != 'b')) {
				try {
					int intData = handler.getIPS().readInt();
					handler.getMouseManager().getSelector().turnUpdate(Integer.toString(intData));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		
	}

	@Override
	public void render(Graphics g) {
		backgroundBoard.render(g);
		selector.render(g);
		pieceBoard.render(g);
		uiManager.render(g);
		
	}
	
	@Override
	public void Init() {
		uiManager = new UIManager(handler);
		handler.getMouseManager().setUIManager(uiManager);
		uiManager.addObject(new UIImageButton(544,256,64,64,Assets.btn_start,0,1,null,null,new ClickListener() {

			@Override
			public void onClick() {
				if(!handler.getMouseManager().getSelector().isControlHighlightOn()) {
					handler.getMouseManager().getSelector().setControlHighlightOn(true);
				}else {
					handler.getMouseManager().getSelector().setControlHighlightOn(false);
					handler.getMouseManager().getSelector().controlledHighlightUpdate();
				}
			}
			
			}));
		
		uiManager.addObject(new UIImageButton(544,100,64,64,Assets.btn_queen,0,1,null,null,new ClickListener() {

			@Override
			public void onClick() {
				handler.getMouseManager().getSelector().rewind();
			}
			
			}));
	}
	
	public void resetUI() {
		uiManager = new UIManager(handler);
		handler.getMouseManager().setUIManager(uiManager);
		uiManager.addObject(new UIImageButton(544,256,64,64,Assets.btn_start,0,1,null,null,new ClickListener() {

			@Override
			public void onClick() {
				if(!handler.getMouseManager().getSelector().isControlHighlightOn()) {
					handler.getMouseManager().getSelector().setControlHighlightOn(true);
				}else {
					handler.getMouseManager().getSelector().setControlHighlightOn(false);
					handler.getMouseManager().getSelector().controlledHighlightUpdate();
				}
			}
			
			}));
		uiManager.addObject(new UIImageButton(544,100,64,64,Assets.btn_queen,0,1,null,null,new ClickListener() {

			@Override
			public void onClick() {
				handler.getMouseManager().getSelector().rewind();
			}
			
			}));
	}
	
	public void reloadBoard() {
		//pieceBoard = new PieceArrangeBoard(handler,"/saves/default.txt");
		pieceBoard = new PieceArrangeBoard(handler,"/saves/test.txt");
		handler.setPieceArrangeBoard(pieceBoard);
		for(Piece piece:handler.getPieceArrangeBoard().getPieceList()) {
			piece.checkProtects();
			piece.checkMoves();
			handler.getPieceArrangeBoard().controlRangeUpdate();
		}
	}
	

		

}
