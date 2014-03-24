package Game.Networking;

import gui.GameView;
import gui.InputDialog;
import gui.MenuView;

import java.io.*;
import java.net.*;

import org.controlsfx.dialog.Dialogs;

import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import Game.GameController;
 
public class PrimaryClient extends Application {
	static GameView gameView;
	static String[] a;
	
    public static void main(String[] args) {
    	a = args;
    	launch(args);
    }
    
    public static void begin(String[] args) {
        if (args.length != 2) {
            System.err.println(
                "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }
 
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
 
        try {
            Socket socket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
       
            BufferedReader stdIn =
                new BufferedReader(new InputStreamReader(System.in));
            
            
            boolean exit = false;
            while ( !exit ) {
            	String fromServer = in.readLine();

                System.out.println("Server: " + fromServer);
                if (fromServer.equals("Exit"))
                    break;
                
                System.out.println("Write something");
                String fromUser = stdIn.readLine();
                
                if (fromUser != null) {
                	System.out.println("Client: " + fromUser );  
                	out.println(fromUser);
                	exit = HandleInput( fromUser, socket, out, in );
                }
            }            
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        }
    }
    
    public static void beginUI(String[] args, String[] commands) {
    	if (args.length != 2) {
            System.err.println("Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }
 
        String hostName = args[0];
        String command = commands[0];
        int portNumber = Integer.parseInt(args[1]);
        
        if(command.equals(Protocol.HOSTSERVER)) {
    		try {
    			HostGame();
    		}
    		catch(Exception e) {
    			System.out.println("Failed to host game.");
    		}
    	}
        else if(command.equals(Protocol.JOINSERVER))
        {
        	GameClient client = new GameClient(gameView);
        	if(commands.length > 1)
        		client.ConnectToHost(commands[1]);
        	else
        		client.ConnectToHost(hostName);
        }
        else
        {
        	System.err.println("Something went horribly wrong.");
        	System.exit(0);
        }
    }
       
    private static boolean HandleInput( 
    		String inputLine,
    		Socket socket,
    		PrintWriter out,
    		BufferedReader in
    ){
    	String[] input;
    	// split for multi-command on cmd prompt, ex. JoinServer 127.0.0.1
 		input = inputLine.split("\\" + Protocol.DELIMITER);
 		
    	String command = input[0];
    	System.out.println("Client command: " + command);
    	if (input.length == 2 ){
    		System.out.println("with arguments: " + input[1]);
    	}
    	
    	if (command.equals(Protocol.HOSTSERVER)){
    		try { 
    			out.println(Protocol.HOSTSERVER);
    			socket.close(); 
    		}
    		catch (IOException e) {}
    		try { 
	    		HostGame();
    		} catch (Exception e ){}
    		
    		return true;
    	} else if (command.equals(Protocol.GETHOSTEDSERVERS)) {
    		
    	} else if (command.equals(Protocol.EXIT)) {
    	
    	} else if (command.equals(Protocol.JOINSERVER)) {
    		try { 
	    		if (in.readLine().equals(Protocol.FOUND)) {
	    			System.out.println("Joining game " + input[1] );
					out.println(Protocol.EXIT);
					socket.close(); 
					
					GameClient client = new GameClient(gameView);
					client.ConnectToHost(input[1]);
					
					return true;
	    		} else {
	    			System.out.println("Game not found");
	    		}
    		} catch (IOException e) {}
    	}
    	return false;
    }
    
    private static void HostGame() throws Exception {
    	GameServer server = new GameServer();
    	Thread thread = new Thread(server);
    	thread.start();
    	Thread.sleep(2000);
    	GameClient client = new GameClient(gameView);
    	client.ConnectToHost(server.address.getHostName());
    }

	@Override
	public void start(Stage primaryStage) throws Exception {		
		// TODO Auto-generated method stub
		gameView = new GameView(new HBox(), primaryStage);
		
		//UIJoin/Host
		//---------------------------------------//
		MenuView menu = new MenuView(a);
		String command = menu.showInput();
		
		if(command == null) {
			System.exit(0);
		}
		
		beginUI(a, command.split(" "));
		//---------------------------------------//
		
		//Console Join/Host
        //begin(a);
		
		primaryStage.setTitle("Kings N Things");
        primaryStage.setScene(gameView);
        primaryStage.show();
        
        primaryStage.setOnCloseRequest(new javafx.event.EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
            	//Add more stuff to end in a cleaner way?
            	System.out.println("CLOSING APPLICATION!!!");
                System.exit(0);
            }
        });
	}
}
