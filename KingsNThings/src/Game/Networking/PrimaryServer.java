package Game.Networking;


import java.net.*;
import java.io.*;
import java.util.*;

public class PrimaryServer implements Runnable {
	private Socket connection;
	private static List<String> hostingClients = new ArrayList<String>();
	
	PrimaryServer(Socket s) {
		this.connection = s;
	}
	
	public static void main(String[] args) {
	    if (args.length != 1) {
	        System.err.println("Usage: java EchoServer <port number>");
            System.exit(1);
        }
	 
	    int portNumber = Integer.parseInt(args[0]);
	    try {
		    ServerSocket socket1 = new ServerSocket(portNumber);
	        while (true) {
		        Socket s = socket1.accept();
		        Runnable runnable = new PrimaryServer(s);
		        Thread thread = new Thread(runnable);
		        thread.start();
	        }
	    } catch (IOException e){
	    	System.err.println(
	    		"IO Exception in primary server when connecting to port" 
	    		+ portNumber 
	    	);
	    }
	}
	  
 	public void run(){
 		try {
			PrintWriter out = new PrintWriter(
				connection.getOutputStream(),
				true
 			);
            BufferedReader in = new BufferedReader(
        		new InputStreamReader(
    				connection.getInputStream()
				)
        	);
 	         
        	String inputLine;
        
        	out.println("");
 	    	
 	        while ((inputLine = in.readLine()) != null){
	        	if (!HandleClientResponse( inputLine, in, out )){
	        		break;
	        	}
 	        }
 	    } catch (IOException e) {
 	        System.out.println("Exception caught when trying to listen on port "
 	            + connection.getPort() + " or listening for a connection");
 	        System.out.println(e.getMessage());
 	    } finally {
 	    	try {
 	    		connection.close();
 	    	}
 	    	catch (IOException e){}
 	    }
 	}
 	
 	private boolean HandleClientResponse( 
 			String inputLine,
 			BufferedReader in,
 			PrintWriter out
 	) {
 		String[] input;
 		if (inputLine.contains(Protocol.DELIMITER)){
 			 input = inputLine.split("\\" + Protocol.DELIMITER);
 		} else {
 			input = new String[]{ inputLine };
 		}
 		
 		if (input[0].equals(Protocol.GETHOSTEDSERVERS) ){
    		String hostedServers = "";
    		
    		for(String address : hostingClients){
    			hostedServers += address + "/";
    		}
		
    		System.out.print(hostedServers);
    		out.println(hostedServers);
    	} else if (input[0].equals(Protocol.HOSTSERVER)){
    		
    		System.out.println("Hosting server - " +
    							connection.getInetAddress().getHostAddress() +
    							" on port " + connection.getPort() );
    		
			hostingClients.add(
				connection.getInetAddress().getHostAddress()
			);
			
			return false;
    	} else if (input[0].equals(Protocol.EXIT)){
        	out.println("Exit");
        	return false;
        } else if (input[0].equals(Protocol.JOINSERVER)) {
        	
        	String findIp = input[1];
        	boolean foundHost = false;
        	
        	for(String address : hostingClients){
    			if (address.equals(findIp)) {
    				foundHost = true;
    			} 
    		}
        	
        	if (foundHost){
        		out.println(Protocol.FOUND);
        		System.out.println("Found server, sending found");
        		return false;
        	} else {
        		System.out.println("Did not find server");
        		out.println(Protocol.NOTFOUND);
        	}
        } else {
    		out.println("Could not understand response - " + input[0]);
    	}
 		
 		return true;
 	}
}
