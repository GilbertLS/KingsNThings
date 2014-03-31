package Game.Networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import Game.GameController;

public class GameServer implements Runnable {
	InetAddress address;
	public static ArrayList<GameRouter> servers = new ArrayList<GameRouter>();
	
	public GameServer() throws UnknownHostException {
		this.address = InetAddress.getLocalHost();
	}
	
	public InetAddress GetAddress(){
		return address;
	}
	
	public void startServer() {
		try {
			ServerSocket serverSocket = new ServerSocket(Protocol.GAMEPORT);
			while (!checkStartGame()) {
		       Socket clientSocket = serverSocket.accept();
		       System.out.println("A client has joined the game");
		        
		       GameRouter gameRouter = new GameRouter(clientSocket);
		       Thread thread = new Thread(gameRouter);
		       thread.start();
		       
		       while(!gameRouter.ready()){}      
			}
		
			serverSocket.close();
		} catch (IOException e) {}
		
		GameController gameController = new GameController(
			servers.size()
		);
		
		gameController.StartGame();
	}
	
	private boolean checkStartGame() {
		return servers.size() == 1;
	}
	
	public void run(){
		this.startServer();
	}
	
	public static void AddClient( GameRouter c ){
		servers.add(c);
	}
}
