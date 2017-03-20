import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.MouseInfo;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
	
public class WinServer {
	
	private static ServerSocket server = null;
	private static Socket client = null;
	private static BufferedReader in = null;
	private static String line;
	private static boolean isConnected=false;
	private static Robot robot;
	private static final int SERVER_PORT = 8998;
 
	public static void main(String[] args) {
		boolean leftpressed=false;
		boolean rightpressed=false;
	    try{
    		robot = new Robot();
	    }catch (AWTException e) {
			System.out.println("Error in creating robot instance");
			System.exit(-1);
	    }		
		while (!isConnected){
		    try{
				server = new ServerSocket(SERVER_PORT); //Create a server socket on port 8998
				client = server.accept(); //Listens for a connection to be made to this socket and accepts it
				in = new BufferedReader(new InputStreamReader(client.getInputStream())); //the input stream where data will come from client
				isConnected = true;
				System.out.println("Connected. Now listening for keys.");
			}catch (IOException e) {
				System.out.println("Still waiting for connection");
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//System.exit(-1);
			}
		}
		//read input from client while it is connected
	    while(isConnected){
	        try{
				line = in.readLine(); //read input from client
				System.out.println(line); //print whatever we get from client
				
				//if user clicks on next
				if(line.equalsIgnoreCase("q")){
					//Simulate press and release of key 'n'
					robot.keyPress(KeyEvent.VK_Q);
					robot.keyRelease(KeyEvent.VK_Q);
				}
				else if(line.equalsIgnoreCase("w")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_W);
					robot.keyRelease(KeyEvent.VK_W);		        	
				}
				else if(line.equalsIgnoreCase("e")){
					//Simulate press and release of spacebar
					robot.keyPress(KeyEvent.VK_E);
					robot.keyRelease(KeyEvent.VK_E);
				}
				else if(line.equalsIgnoreCase("r")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_R);
					robot.keyRelease(KeyEvent.VK_R);		        	
				}
				//if user clicks on play/pause
				else if(line.equalsIgnoreCase("t")){
					//Simulate press and release of spacebar
					robot.keyPress(KeyEvent.VK_T);
					robot.keyRelease(KeyEvent.VK_T);
				}
				else if(line.equalsIgnoreCase("y")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_Y);
					robot.keyRelease(KeyEvent.VK_Y);		        	
				}
				else if(line.equalsIgnoreCase("u")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_U);
					robot.keyRelease(KeyEvent.VK_U);		        	
				}
				//if user clicks on play/pause
				else if(line.equalsIgnoreCase("i")){
					//Simulate press and release of spacebar
					robot.keyPress(KeyEvent.VK_I);
					robot.keyRelease(KeyEvent.VK_I);
				}
				else if(line.equalsIgnoreCase("o")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_O);
					robot.keyRelease(KeyEvent.VK_O);		        	
				}
				//if user clicks on play/pause
				else if(line.equalsIgnoreCase("p")){
					//Simulate press and release of spacebar
					robot.keyPress(KeyEvent.VK_P);
					robot.keyRelease(KeyEvent.VK_P);
				}
				else if(line.equalsIgnoreCase("a")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_A);
					robot.keyRelease(KeyEvent.VK_A);		        	
				}
				else if(line.equalsIgnoreCase("s")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_S);
					robot.keyRelease(KeyEvent.VK_S);		        	
				}
				else if(line.equalsIgnoreCase("d")){
					//Simulate press and release of spacebar
					robot.keyPress(KeyEvent.VK_D);
					robot.keyRelease(KeyEvent.VK_D);
				}
				else if(line.equalsIgnoreCase("f")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_F);
					robot.keyRelease(KeyEvent.VK_F);		        	
				}
				else if(line.equalsIgnoreCase("g")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_G);
					robot.keyRelease(KeyEvent.VK_G);		        	
				}
				else if(line.equalsIgnoreCase("h")){
					//Simulate press and release of spacebar
					robot.keyPress(KeyEvent.VK_H);
					robot.keyRelease(KeyEvent.VK_H);
				}
				else if(line.equalsIgnoreCase("j")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_J);
					robot.keyRelease(KeyEvent.VK_J);		        	
				}
				else if(line.equalsIgnoreCase("k")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_K);
					robot.keyRelease(KeyEvent.VK_K);		        	
				}
				else if(line.equalsIgnoreCase("l")){
					//Simulate press and release of spacebar
					robot.keyPress(KeyEvent.VK_L);
					robot.keyRelease(KeyEvent.VK_L);
				}
				else if(line.equalsIgnoreCase("z")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_Z);
					robot.keyRelease(KeyEvent.VK_Z);		        	
				}
				else if(line.equalsIgnoreCase("x")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_X);
					robot.keyRelease(KeyEvent.VK_X);		        	
				}
				else if(line.equalsIgnoreCase("c")){
					//Simulate press and release of spacebar
					robot.keyPress(KeyEvent.VK_C);
					robot.keyRelease(KeyEvent.VK_C);
				}
				else if(line.equalsIgnoreCase("v")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_V);
					robot.keyRelease(KeyEvent.VK_V);		        	
				}
				else if(line.equalsIgnoreCase("b")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_B);
					robot.keyRelease(KeyEvent.VK_B);		        	
				}
				else if(line.equalsIgnoreCase("n")){
					//Simulate press and release of spacebar
					robot.keyPress(KeyEvent.VK_N);
					robot.keyRelease(KeyEvent.VK_N);
				}
				else if(line.equalsIgnoreCase("m")){
					//Simulate press and release of spacebar
					robot.keyPress(KeyEvent.VK_M);
					robot.keyRelease(KeyEvent.VK_M);
				}		
				else if(line.equalsIgnoreCase(" ")){
					//Simulate press and release of key 'n'
					robot.keyPress(KeyEvent.VK_SPACE);
					robot.keyRelease(KeyEvent.VK_SPACE);
				}
				else if(line.equalsIgnoreCase("enter")){
					//Simulate press and release of key 'n'
					robot.keyPress(KeyEvent.VK_ENTER);
					robot.keyRelease(KeyEvent.VK_ENTER);
				}
				//Exit if user ends the connection
				else if(line.equalsIgnoreCase("exit")){
					isConnected=false;
					System.out.println("Connection terminated. Closing server.");
					//Close server and client socket
					server.close();
					client.close();
				}
	        } catch (IOException e) {
				System.out.println("Read failed. Waiting a bit before trying again.");
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        }
      	}
	}
}