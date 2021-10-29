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
	private Font font;
	
	private int port;
	private String ip;
	
	private Server server;
	private Client client;
	
	
	public MenuState(Handler handler) {
		super(handler);
		
		font = new Font("Dialog", Font.PLAIN, 50);
		
		uiManager = new UIManager(handler);
		handler.getMouseManager().setUIManager(uiManager);
		uiManager.addObject(new UIImageButton(128, 256, 128, 128, Assets.btn_client, 0, 0, null, null, new ClickListener() {

			@Override
			public void onClick() {
				ip = JOptionPane.showInputDialog(handler.getGame().getDisplay().getFrame(),"Enter IP: ");
				port = Integer.parseInt(JOptionPane.showInputDialog(handler.getGame().getDisplay().getFrame(),"Enter a Port: "));
				State.setState(handler.getGame().gameState);
				handler.getGame().gameState.Init();
				if(port!=-1) {
				client = new Client(ip,port);
				handler.setIPS(client.getIn());
				handler.setOPS(client.getOut());
				handler.setConnected(true);
				}
				handler.setColor('b');
				GameState gameState = (GameState)handler.getGame().getGameState();
				gameState.reloadBoard();
			}
			
		}));
		uiManager.addObject(new UIImageButton(384, 256, 128, 128, Assets.btn_server, 0, 0, null, null, new ClickListener() {

			@Override
			public void onClick() {
				port = Integer.parseInt(JOptionPane.showInputDialog(handler.getGame().getDisplay().getFrame(),"Enter a Port: "));
				State.setState(handler.getGame().gameState);
				handler.getGame().gameState.Init();
				if(port!=-1) {
					server = new Server(port);
					handler.setIPS(server.getIn());
					handler.setOPS(server.getOut());
					handler.setConnected(true);
				}
				handler.setColor('w');
				GameState gameState = (GameState)handler.getGame().getGameState();
				gameState.reloadBoard();
				System.out.println("board loaded");
			}
			
		}));
		
		/*uiManager.addObject(new UIImageButton(256,256,128,64,Assets.btn_start,0,1,null,null,new ClickListener() {

			@Override
			public void onClick() {
				State.setState(handler.getGame().gameState);
				handler.getGame().gameState.Init();
			}
			
			}));*/
	}
	
	@Override
	public void tick() {
		uiManager.tick();

	}

	@Override
	public void render(Graphics g) {
		
		uiManager.render(g);
		g.setFont(font);
		g.drawString("Start", 265, 380);
	}
	
	@Override
	public void Init() {
		
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
