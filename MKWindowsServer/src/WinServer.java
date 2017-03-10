import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.MouseInfo;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
 
public class WinServer {
	
	private static ServerSocket server = null;
	private static Socket client = null;
	private static BufferedReader packetAsString = null;
	private static String receivedPacket;
	private static boolean connectionStatus=true;
	private static Robot robot;
	private static final int SERVER_PORT = 1667;
 
	public static void main(String[] args) {
		try{
	    	robot = new Robot();
			server = new ServerSocket(SERVER_PORT);
			client = server.accept(); 
			packetAsString = new BufferedReader(new InputStreamReader(client.getInputStream()));
		}catch (IOException e) {
			System.out.println("Could not open socket");
			System.exit(-1);
		}catch (AWTException e) {
			System.out.println("Could not create the ghost ui manipulator");
			System.exit(-1);
		}
			
		
	    while(connectionStatus){
	        try{
		        receivedPacket = packetAsString.readLine();
				System.out.println(receivedPacket);

				if(receivedPacket.contains(" ")){
					float deltaX=Float.parseFloat(receivedPacket.split(" ")[0]);
					float deltaY=Float.parseFloat(receivedPacket.split(" ")[1]);
					Point point = MouseInfo.getPointerInfo().getLocation();
					float nowx=point.x;
					float nowy=point.y;
					robot.mouseMove((int)(nowx+deltaX),(int)(nowy+deltaY));
				}
				else if(receivedPacket.contains("left_click")){
					robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
					robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				}
				else if(receivedPacket.contains("right_click")){
					robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
					robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
				}
				else if(receivedPacket.equalsIgnoreCase("close")){
					connectionStatus=false;
					server.close();
					client.close();
				}
	        } catch (IOException e) {
				System.out.println("An error occured. Connection possibly closed.");
				System.exit(-1);
	        }
      	}
	}
}
