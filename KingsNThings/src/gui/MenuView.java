package gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import org.controlsfx.dialog.Dialogs;
import Game.Networking.Protocol;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MenuView extends Stage {
	private MenuView 	self		= this;
	private Scene 		mainMenu;
	private Scene		joinMenu;
	private String		command		= null;
	private String		argIp 		= null;
	private String 		argPort 	= null;
	
	public MenuView(String[] args) {
		super();
				
		if(args.length > 1) {
			argIp 	= args[0];
			argPort = args[1];
		}
		
		this.setupMainMenu();
		this.setupJoinMenu();
		this.setScene(mainMenu);
		this.setWidth(400);
		this.setHeight(400);
		this.setResizable(false);
		this.setTitle("KingsNThings");
	}
	
	private void setupMainMenu() {
		BorderPane 	root 		= new BorderPane();
		HBox 		buttonBox	= new HBox();
		Button 		hostGame 	= new Button("Host Game");
		Button 		joinGame 	= new Button("Join Game");
		
		mainMenu = new Scene(root);
		mainMenu.getStylesheets().add("gui/menu.css");
		
		root.setBottom(buttonBox);
		buttonBox.getStyleClass().add("hbox");
		buttonBox.setSpacing(30);
		buttonBox.getChildren().add(joinGame);
		buttonBox.getChildren().add(hostGame);
		
		joinGame.setOnMouseClicked(new EventHandler<MouseEvent>() {
    		@Override public void handle(MouseEvent e) {
    			//self.setScene(joinMenu);
    			String ip = Dialogs.create()
  				      .owner(self)
  				      .title("Join Game")
  				      .masthead(null)
  				      .message("Enter Host's IP")
  				      .showTextInput("127.0.0.1");
  			
	  			if(ip != null) {
	  				if(gameFound(ip)) {
	  					command = Protocol.JOINSERVER + " " + ip;
	  					self.hide();
	  				}
	  				else
	  				{
	  					Dialogs.create()
	  						.owner(self)
	  	    				.title("Error")
	  	    				.masthead(null)
	  	    				.message("Could not find a game at: " + ip)
	  	    				.showWarning();
	  				}
	  			}
    		}
    	});
		
		hostGame.setOnMouseClicked(new EventHandler<MouseEvent>() {
    		@Override public void handle(MouseEvent e) {
    			String tempCommand = Protocol.HOSTSERVER;
    			if(canHost(tempCommand)) {
    				command = tempCommand;
    				self.hide();
    			}
    			else {
    				Dialogs.create()
						.owner(self)
	    				.title("Error")
	    				.masthead(null)
	    				.message("Could not host a game.")
	    				.showWarning();
    			}
    		}
    	});
	}
	
	private void setupJoinMenu() {
		BorderPane 					root 		= new BorderPane();
		HBox						buttonBox	= new HBox();
		Button 						joinGame 	= new Button("Join Game");
		Button 						joinIP 		= new Button("Join By IP");
		Button 						refresh		= new Button("Refresh");
		Button						back		= new Button("Back");
		TableView<String>			table		= new TableView<String>();
		TableColumn<String, String> ip 			= new TableColumn<String, String>("IP");
		TableColumn<String, String> port		= new TableColumn<String, String>("Port");
		this.joinMenu 							= new Scene(root);
		
		root.setBottom(buttonBox);
		root.setCenter(table);
		
		buttonBox.getChildren().add(joinGame);
		buttonBox.getChildren().add(refresh);
		buttonBox.getChildren().add(joinIP);
		buttonBox.getChildren().add(back);
		
		table.getColumns().add(ip);
		table.getColumns().add(port);
		
		back.setOnMouseClicked(new EventHandler<MouseEvent>() {
    		@Override public void handle(MouseEvent e) {
    			self.setScene(mainMenu);
    		}
    	});
		
	}
	
	private boolean gameFound(String ip) {
		try {
		Socket socket = new Socket(ip, Integer.parseInt(argPort));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        String fromServer = in.readLine();
        System.out.println("Server: " + fromServer);
        
    	out.println(Protocol.JOINSERVER + " " + ip);

    	try { 
			if (in.readLine().equals(Protocol.FOUND)) {
				out.println(Protocol.EXIT);
				socket.close(); 
				return true;
			}
			else {
				System.out.println("Game not Found.");
			}
		} catch (IOException e) {}
        
	    } catch (UnknownHostException e) {
	        System.err.println("Don't know about host " + ip);
	    } catch (IOException e) {
	        System.err.println("Couldn't get I/O for the connection to " + ip);
	    }
		
		return false;
	}
	
	private boolean canHost(String c) {
		try {
			Socket socket = new Socket(argIp,  Integer.parseInt(argPort));
	        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        
	        String fromServer = in.readLine();
	
	        System.out.println("Server: " + fromServer);
	
	        	System.out.println("Client: " + Protocol.HOSTSERVER);
	    		out.println(Protocol.HOSTSERVER);
	    	
	    	if(c.equals(Protocol.HOSTSERVER)) {
	    		try {
	    			out.println(Protocol.HOSTSERVER);
	    			socket.close(); 
	    			return true;
	    		}
	    		catch (IOException e) {}
	    	}
	    } catch (UnknownHostException e) {
	        System.err.println("Don't know about host " + argIp);
	    } catch (IOException e) {
	        System.err.println("Couldn't get I/O for the connection to " + argIp);
	    }
		return false;
	}
		
	public String showInput() {
		this.showAndWait();
		
		return command;
	}
}