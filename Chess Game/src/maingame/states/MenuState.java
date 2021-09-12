package maingame.states;

import java.awt.Graphics;

import gfx.Assets;
import maingame.Handler;
import ui.ClickListener;
import ui.UIImageButton;
import ui.UIManager;

public class MenuState extends State{
	
	private UIManager uiManager;
	
	public MenuState(Handler handler) {
		super(handler);
		uiManager = new UIManager(handler);
		handler.getMouseManager().setUIManager(uiManager);
		
		uiManager.addObject(new UIImageButton(200,200,128,64,Assets.btn_start,new ClickListener() {

			@Override
			public void onClick() {
				State.setState(handler.getGame().gameState);
			}
			
			}));
	}
	
	@Override
	public void tick() {
		uiManager.tick();

	}

	@Override
	public void render(Graphics g) {
		uiManager.render(g);
	}

}
