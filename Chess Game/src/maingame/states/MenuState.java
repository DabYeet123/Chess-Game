package maingame.states;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import gfx.Assets;
import input.TextBox;
import maingame.Handler;
import multiplayer.Client;
import multiplayer.Server;
import ui.ClickListener;
import ui.UIImageButton;
import ui.UIManager;

public class MenuState extends State{
	
	private UIManager uiManager;
	//private Font font;
	
	private int port;
	private String ip;
	
	private Server server;
	private Client client;
	
	
	public MenuState(Handler handler) {
		super(handler);
		
		//font = new Font("Dialog", Font.PLAIN, 50);
		
		uiManager = new UIManager(handler);
		handler.getMouseManager().setUIManager(uiManager);
		
		
		/*uiManager.addObject(new UIImageButton(256,256,128,64,Assets.btn_start,0,1,null,null,new ClickListener() {

			@Override
			public void onClick() {
				State.setState(handler.getGame().gameState);
				handler.getGame().gameState.Init();
			}
			
			}));*/
		
		uiManager.addObject(new UIImageButton(200, 256, 240, 64, Assets.btn_single, 0, 1, null, null, new ClickListener() {

			@Override
			public void onClick() {
				handler.setColor('w');
				State.setState(handler.getGame().gameState);
				handler.getGame().gameState.Init();
				GameState gameState = (GameState)handler.getGame().getGameState();
				gameState.reloadBoard();
			}
			
		}));
		
		uiManager.addObject(new UIImageButton(200, 353, 240, 64, Assets.btn_multi, 0, 1, null, null, new ClickListener() {

			@Override
			public void onClick() {
				handler.getGame().multiplayerMenu.Init();
				State.setState(handler.getGame().multiplayerMenu);
			}
			
		}));
		
		uiManager.addObject(new UIImageButton(504, 592, 120, 32, Assets.btn_opt, 0, 1, null, null, new ClickListener() {

			@Override
			public void onClick() {
				
			}
			
		}));
		
	}
	
	@Override
	public void tick() {
		uiManager.tick();

	}

	@Override
	public void render(Graphics g) {
		g.drawImage(Assets.chessBackground,0,0,640,640,null);
		uiManager.render(g);
		//g.setFont(font);
		//g.drawString("Start", 265, 380);
	}
	
	@Override
	public void Init() {
		
		uiManager = new UIManager(handler);
		handler.getMouseManager().setUIManager(uiManager);
		
		/*uiManager.addObject(new UIImageButton(256,256,128,64,Assets.btn_start,0,1,null,null,new ClickListener() {

			@Override
			public void onClick() {
				State.setState(handler.getGame().gameState);
				handler.getGame().gameState.Init();
			}
			
			}));*/
		
		uiManager.addObject(new UIImageButton(200, 256, 240, 64, Assets.btn_single, 0, 1, null, null, new ClickListener() {

			@Override
			public void onClick() {
				
			}
			
		}));
		
		uiManager.addObject(new UIImageButton(200, 353, 240, 64, Assets.btn_multi, 0, 1, null, null, new ClickListener() {

			@Override
			public void onClick() {
				handler.getGame().multiplayerMenu.Init();
				State.setState(handler.getGame().multiplayerMenu);
			}
			
		}));
		
		uiManager.addObject(new UIImageButton(504, 592, 120, 32, Assets.btn_opt, 0, 1, null, null, new ClickListener() {

			@Override
			public void onClick() {
				
			}
			
		}));
	}
	
	@Override
	public void resetUI() {
		
	}
	
	public void chat() {
		String message = JOptionPane.showInputDialog(handler.getGame().getDisplay().getFrame(),"Say Something!: ");
		if(client!=null) {
			try {
				client.out.writeUTF(message);
				client.out.flush();
				System.out.println(client.in.readUTF());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(server!=null) {
			try {
				server.out.writeUTF(message);
				server.out.flush();
				System.out.println(server.in.readUTF());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
