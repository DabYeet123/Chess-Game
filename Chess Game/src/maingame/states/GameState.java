package maingame.states;

import java.awt.Graphics;


import gfx.Assets;
import maingame.Game;
import maingame.Handler;


public class GameState extends State{
	


	
	public GameState(Handler handler) {
		super(handler);
		
	}


		



	@Override
	public void tick() {



		
	}

	@Override
	public void render(Graphics g) {
		handler.getBackgroundBoard().render(g);
	}
	
	

}
