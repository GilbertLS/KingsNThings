TEAM18

Type: 		Server-Client
Language: 	Java 8
Library: 	JavaFX 8

How To Run:
If you do not have Java 8 go to https://jdk8.java.net/download.html 
and download either the JDK or JRE for the correct version of windows.
Install Java 8.

Also, get the latest version of Eclipse.

In Eclipse, go to File -> Import and select our source directory.

Select PrimaryServer in Game.Networking and Run As Java Application,
 with command line argument 5000.

Select PrimaryClient and Run As JavaApplication, with command line 
arguments 127.0.0.1 5000. In the console write "HostServer" (without 
the "").

(Do this step 3 times)
Select PrimaryClient and Run As JavaApplication, with command line 
arguments 127.0.0.1 5000. In the console write "JoinServer 127.0.0.1" 
(without the "").

Now that all the clients are running, you can play the game. Read the 
instructions in the info box and have fun.