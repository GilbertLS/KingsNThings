package Game.Networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

import Game.GameController;

public class GameServer implements Runnable {
	InetAddress address;
	public static ArrayList<GameRouter> servers = new ArrayList<GameRouter>();
	private Semaphore gameSetup;
	
	public GameServer(Semaphore setupSem) throws UnknownHostException {
		this.address = InetAddress.getLocalHost();
		gameSetup = setupSem;
	}
	
	public InetAddress GetAddress(){
		return address;
	}
	
	public void startServer() {
		try {
			ServerSocket serverSocket = new ServerSocket(Protocol.GAMEPORT);
			gameSetup.release();
			while (!checkStartGame()) {
		       Socket clientSocket = serverSocket.accept();
		       System.out.println("A client has joined the game");
		        
		       Semaphore waitSem = new Semaphore(0);
		       GameRouter gameRouter = new GameRouter(clientSocket, waitSem);
		       Thread thread = new Thread(gameRouter);
		       thread.start();
		       
		       try {
		    	   waitSem.acquire();
				} catch (InterruptedException e) {} 
			}
		
			serverSocket.close();
		} catch (IOException e) {}
		
		GameController gameController = new GameController(
			servers.size()
		);
		
		gameController.StartGame();
	}
	
	private boolean checkStartGame() {
		synchronized(servers) {
			return servers.size() == 2;
		}
	}
	
	public void run(){
		this.startServer();
	}
	
	public static void AddClient( GameRouter c ){
		synchronized(servers) {
			servers.add(c);
		}
	}
}
