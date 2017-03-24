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
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class WinServer {
	
	private static ServerSocket server = null;
	private static Socket client = null;
	private static BufferedReader in = null;
	private static String line;
	private static boolean isConnected=true;
	private static Robot robot;
	private static final int SERVER_PORT = 8998;
 
	public static void main(String[] args) {
		boolean leftpressed=false;
		boolean rightpressed=false;
//		Scanner theS = new Scanner(System.in);
//		System.out.println(theS.next());
	    try{
		    System.out.println("Creating ui hook");
	    	robot = new Robot();
//	    	robot.mouseMove(50, 50);
//	    	TimeUnit.SECONDS.sleep(2);
//	    	robot.mouseMove(100, 100);
//	    	TimeUnit.SECONDS.sleep(2);
//	    	robot.mouseMove(500, 500);
		    System.out.println("Creating socket ");
	    	server = new ServerSocket(SERVER_PORT); //Create a server socket on port 8998
		    System.out.println("Waiting for connection...");
	    	client = server.accept();
			//Listens for a connection to be made to this socket and accepts it
		    System.out.println("Connected");
			//in = new BufferedReader(new InputStreamReader(client.getInputStream())); //the input stream where data will come from client
		    InputStream theIn = client.getInputStream();
		    in = new BufferedReader(new InputStreamReader(theIn));
	    }catch (IOException e) {
			System.out.println("Error in opening Socket");
			System.exit(-1);
		}catch (AWTException e) {
			System.out.println("Error in creating robot instance");
			System.exit(-1);
		}
	
		//read input from client while it is connected
	    while(isConnected){
//	    	try {
//				TimeUnit.SECONDS.sleep(1);
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
		    
	        try{
	        	//System.out.println("We are here");
				line = in.readLine(); //read input from client
				//System.out.println("Now we are here");
				//System.out.println(line); //print whatever we get from client
				
				//if user clicks on next
				if(line.equalsIgnoreCase("a")){
					//Simulate press and release of key 'n'
					robot.keyPress(KeyEvent.VK_A);
					robot.keyRelease(KeyEvent.VK_A);
					System.out.print("a");
				}
				//if user clicks on previous
				else if(line.equalsIgnoreCase("s")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_S);
					robot.keyRelease(KeyEvent.VK_S);
					System.out.print("s");
				}
				if(line.equalsIgnoreCase("d")){
					//Simulate press and release of key 'n'
					robot.keyPress(KeyEvent.VK_D);
					robot.keyRelease(KeyEvent.VK_D);
					System.out.print("d");
				}
				//if user clicks on previous
				else if(line.equalsIgnoreCase("f")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_F);
					robot.keyRelease(KeyEvent.VK_F);
					System.out.print("f");
				}
				if(line.equalsIgnoreCase("g")){
					//Simulate press and release of key 'n'
					robot.keyPress(KeyEvent.VK_G);
					robot.keyRelease(KeyEvent.VK_G);
					System.out.print("a");
				}
				//if user clicks on previous
				else if(line.equalsIgnoreCase("h")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_H);
					robot.keyRelease(KeyEvent.VK_H);
					System.out.print("h");
				}
				if(line.equalsIgnoreCase("j")){
					//Simulate press and release of key 'n'
					robot.keyPress(KeyEvent.VK_J);
					robot.keyRelease(KeyEvent.VK_J);
					System.out.print("j");
				}
				//if user clicks on previous
				else if(line.equalsIgnoreCase("k")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_K);
					robot.keyRelease(KeyEvent.VK_K);
					System.out.print("k");
				}
				if(line.equalsIgnoreCase("l")){
					//Simulate press and release of key 'n'
					robot.keyPress(KeyEvent.VK_L);
					robot.keyRelease(KeyEvent.VK_L);
					System.out.print("l");
				}
				//if user clicks on previous
				else if(line.equalsIgnoreCase("z")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_Z);
					robot.keyRelease(KeyEvent.VK_Z);
					System.out.print("z");
				}
				if(line.equalsIgnoreCase("x")){
					//Simulate press and release of key 'n'
					robot.keyPress(KeyEvent.VK_X);
					robot.keyRelease(KeyEvent.VK_X);
					System.out.print("x");
				}
				//if user clicks on previous
				else if(line.equalsIgnoreCase("c")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_C);
					robot.keyRelease(KeyEvent.VK_C);
					System.out.print("c");
				}
				if(line.equalsIgnoreCase("v")){
					//Simulate press and release of key 'n'
					robot.keyPress(KeyEvent.VK_V);
					robot.keyRelease(KeyEvent.VK_V);
					System.out.print("v");
				}
				//if user clicks on previous
				else if(line.equalsIgnoreCase("b")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_B);
					robot.keyRelease(KeyEvent.VK_B);
					System.out.print("b");
				}
				if(line.equalsIgnoreCase("n")){
					//Simulate press and release of key 'n'
					robot.keyPress(KeyEvent.VK_N);
					robot.keyRelease(KeyEvent.VK_N);
					System.out.print("n");
				}
				//if user clicks on previous
				else if(line.equalsIgnoreCase("m")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_M);
					robot.keyRelease(KeyEvent.VK_M);
					System.out.print("m");
				}
				if(line.equalsIgnoreCase("q")){
					//Simulate press and release of key 'n'
					robot.keyPress(KeyEvent.VK_Q);
					robot.keyRelease(KeyEvent.VK_Q);
					System.out.print("q");
				}
				//if user clicks on previous
				else if(line.equalsIgnoreCase("w")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_W);
					robot.keyRelease(KeyEvent.VK_W);
					System.out.print("w");
				}
				if(line.equalsIgnoreCase("e")){
					//Simulate press and release of key 'n'
					robot.keyPress(KeyEvent.VK_E);
					robot.keyRelease(KeyEvent.VK_E);
					System.out.print("e");
				}
				//if user clicks on previous
				else if(line.equalsIgnoreCase("r")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_R);
					robot.keyRelease(KeyEvent.VK_R);
					System.out.print("r");
				}
				if(line.equalsIgnoreCase("t")){
					//Simulate press and release of key 'n'
					robot.keyPress(KeyEvent.VK_T);
					robot.keyRelease(KeyEvent.VK_T);
					System.out.print("t");
				}
				//if user clicks on previous
				else if(line.equalsIgnoreCase("y")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_Y);
					robot.keyRelease(KeyEvent.VK_Y);
					System.out.print("y");
				}
				if(line.equalsIgnoreCase("u")){
					//Simulate press and release of key 'n'
					robot.keyPress(KeyEvent.VK_U);
					robot.keyRelease(KeyEvent.VK_U);
					System.out.print("u");
				}
				//if user clicks on previous
				else if(line.equalsIgnoreCase("i")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_I);
					robot.keyRelease(KeyEvent.VK_I);
					System.out.print("i");
				}
				if(line.equalsIgnoreCase("o")){
					//Simulate press and release of key 'n'
					robot.keyPress(KeyEvent.VK_O);
					robot.keyRelease(KeyEvent.VK_O);
					System.out.print("o");
				}
				//if user clicks on previous
				else if(line.equalsIgnoreCase("p")){
					//Simulate press and release of key 'p'
					robot.keyPress(KeyEvent.VK_P);
					robot.keyRelease(KeyEvent.VK_P);
					System.out.print("s");
				}
				else if(line.equalsIgnoreCase(" ")){
					//Simulate press and release of key 'n'
					robot.keyPress(KeyEvent.VK_SPACE);
					robot.keyRelease(KeyEvent.VK_SPACE);
					System.out.print("a");
				}
				else if(line.equalsIgnoreCase("enter")){
					//Simulate press and release of key 'n'
					robot.keyPress(KeyEvent.VK_ENTER);
					robot.keyRelease(KeyEvent.VK_ENTER);
					System.out.println();
				}
				//input will come in x,y format if user moves mouse on mousepad
				else if(line.contains(",")){
					float movex=Float.parseFloat(line.split(",")[0].trim());//extract movement in x direction
					float movey=Float.parseFloat(line.split(",")[1].trim());//extract movement in y direction
					Point point = MouseInfo.getPointerInfo().getLocation(); //Get current mouse position
					float nowx=point.x;
					float nowy=point.y;
					robot.mouseMove((int)(nowx+movex),(int)(nowy+movey));//Move mouse pointer to new location
				}
				//if user taps on mousepad to simulate a left click
				else if(line.contains("left_click")){
					//Simulate press and release of mouse button 1(makes sure correct button is pressed based on user's dexterity)
					robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
					robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				}
				//Exit if user ends the connection
				else if(line.equalsIgnoreCase("exit")){
					isConnected=false;
					//Close server and client socket
					//server.close();
					client.close();
					System.out.println("Connection terminated. Waiting for new connection.");
					client = server.accept();
					isConnected=true;
			}
	        } catch (IOException e) {
				System.out.println("Read failed");
				System.exit(-1);
	        }
      	}
	}
}
