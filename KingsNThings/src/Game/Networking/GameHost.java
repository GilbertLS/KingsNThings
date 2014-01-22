package Game.Networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameHost implements Runnable {
	
	private static boolean gameStarted = false;
	public String address;
	
	public GameHost() throws UnknownHostException {
		this.address = InetAddress.getLocalHost().getHostAddress();
	}
	
	public String GetAddress(){
		return address;
	}
	
	public void StartServer(){
	    try {
	    	 ServerSocket serverSocket = new ServerSocket(Protocol.GAMEPORT);
	    	 while (!gameStarted) {
		        Socket clientSocket = serverSocket.accept();
		        System.out.println("A client has joined the game");
		        
		        Runnable runnable = new GameServer(clientSocket);
		        Thread thread = new Thread(runnable);
		        thread.start();
	        }
	    } catch (IOException e) {
	    	System.out.println("IO Error");
	    }
	}
	
	public void run(){
		this.StartServer();
	}
}
