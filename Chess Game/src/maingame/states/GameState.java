package maingame.states;

import java.awt.Graphics;

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
		
		//pieceBoard = new PieceArrangeBoard(handler,"/saves/test.txt");
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
		uiManager.addObject(new UIImageButton(544,256,64,64,Assets.btn_start,new ClickListener() {

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
		
		for(Piece piece:handler.getPieceArrangeBoard().getPieceList()) {
			piece.checkProtects();
			piece.checkMoves();
			handler.getPieceArrangeBoard().controlRangeUpdate();
		}
	}
	

		

}
