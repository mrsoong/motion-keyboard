package motionkey.motionmote;
//motion key side app to act as remote control
//derived from tutorial found at http://codesmith.in/control-pc-from-android-app-using-java/
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    Button playPauseButton;
    Button nextButton;
    Button previousButton;
    TextView mousePad;

    private boolean isConnected=false;
    private boolean mouseMoved=false;
    private Socket socket;
    private PrintWriter out = new PrintWriter(System.out);

    private float initX =0;
    private float initY =0;
    private float disX =0;
    private float disY =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this; //save the context to show Toast messages

        //Get references of all buttons
        playPauseButton = (Button)findViewById(R.id.exitButton);
        nextButton = (Button)findViewById(R.id.typeButton);
        previousButton = (Button)findViewById(R.id.centreButton);

        //this activity extends View.OnClickListener, set this as onClickListener
        //for all buttons
        playPauseButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);

        //Get reference to the TextView acting as mousepad
        mousePad = (TextView)findViewById(R.id.mousePad);
        final EditText edittext = (EditText) findViewById(R.id.edittexts);

/*
        edittext.setOnKeyListener(new View.OnKeyListener()  {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                int theaction = event.getAction();
                if (theaction == KeyEvent.ACTION_DOWN) {
                    Toast.makeText(context, "a",Toast.LENGTH_SHORT).show();
                }
                if (theaction == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_B) {
                    Toast.makeText(context, "b",Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(context, Character.toString(Character.toChars(keyCode)[0]),Toast.LENGTH_SHORT).show();
                return true;
                // If the event is a key-down event on the "enter" button
//                if ((event.getAction() == KeyEvent.ACTION_DOWN) && isConnected) {
//                    // Perform action on key press
//                    char c = Character.toChars(keyCode)[0];
//                    out.println(Character.toString(c));
//                    return true;
//                }
//                return false;
            }
        });
*/
        edittext.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0){
                    Toast.makeText(context, Character.toString(s.charAt(s.length() - 1)), Toast.LENGTH_SHORT).show();
                    if (isConnected){
                        out.println(Character.toString(s.charAt(s.length() - 1)));
                    }
                }
                if (s.length() > 5){
                    edittext.setText("");
                }
                //do here your calculation
                //Toast.makeText(context, s ,Toast.LENGTH_SHORT).show();
                //edittext.setText("");
            }
        });


        //capture finger taps and movement on the textview
        mousePad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Toast.makeText(context,"Hey, mousepad selected!",Toast.LENGTH_SHORT).show();
                if(isConnected && out!=null){
                    switch(event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            //save X and Y positions when user touches the TextView
                            initX =event.getX();
                            initY =event.getY();
                            mouseMoved=false;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            disX = event.getX()- initX; //Mouse movement in x direction
                            disY = event.getY()- initY; //Mouse movement in y direction
                            /*set init to new position so that continuous mouse movement
                            is captured*/
                            initX = event.getX();
                            initY = event.getY();
                            if(disX !=0|| disY !=0){
                                out.println(disX +","+ disY); //send mouse movement to server
                            }
                            mouseMoved=true;
                            break;
                        case MotionEvent.ACTION_UP:
                            //consider a tap only if usr did not move mouse after ACTION_DOWN
                            if(!mouseMoved){
                                out.println(Constants.MOUSE_LEFT_CLICK);
                            }
                    }
                }
                return true;
            }
        });
    }









    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //Toast.makeText(context,"Hey, ACTIONBAR selected!",Toast.LENGTH_SHORT).show();
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_connect) {
            //Toast.makeText(context,"Hey, connect selected!",Toast.LENGTH_SHORT).show();
            if (!isConnected){
                ConnectPhoneTask connectPhoneTask = new ConnectPhoneTask();
                Toast.makeText(context, "ATTEMPTING CONNECTION....",Toast.LENGTH_SHORT).show();
                connectPhoneTask.execute(Constants.SERVER_IP, Constants.SERVER_PORT_STRING); //try to connect to server in another thread
                Toast.makeText(context, "Connection Made" ,Toast.LENGTH_SHORT).show();
                //return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    //OnClick method is called when any of the buttons are pressed
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exitButton:
                if (isConnected && out!=null) {
                    out.println(Constants.EXIT);//send "play" to server
                    try {
                        socket.close();
                    } catch (IOException e) {
                    }
                    isConnected = false;
                    out = null;
                }
                break;
            case R.id.typeButton:
                if (isConnected && out!=null) {
                    out.println(Constants.TYPE); //send "next" to server
                }
                break;
            case R.id.centreButton:
                if (isConnected && out!=null) {
                    out.println(Constants.CENTRE); //send "previous" to server
                }
                break;
        }

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(isConnected && out!=null) {
            try {
                out.println("exit"); //tell server to exit
                socket.close(); //close socket
            } catch (IOException e) {
                Log.e("remotedroid", "Error in closing socket", e);
            }
        }
    }

    private class ConnectPhoneTask extends AsyncTask<String,Void,Socket> {

        @Override
        protected Socket doInBackground(String... params) {
            Socket result = null;
            try {
                //Toast.makeText(context,new String(params[0]) ,Toast.LENGTH_SHORT).show();
                InetAddress serverAddr = InetAddress.getByName(params[0]);
                result = new Socket(serverAddr, Integer.parseInt(params[1].trim()));//Open socket on server IP and port
                //Toast.makeText(context, "WE ARE NOW CONNECTED!" ,Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                //Toast.makeText(context,"Error while connecting",Toast.LENGTH_SHORT).show();
                result = null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Socket result)
        {
            socket = result;
            if (result == null){
                isConnected = false;
            } else {
                isConnected = true;
            }

            //Toast.makeText(context,isConnected?"Connected to server!":"Error while connecting",Toast.LENGTH_SHORT).show();
            try {
                if(isConnected) {
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                            .getOutputStream())), true); //create output stream to send data to server
                }
            }catch (IOException e){
                Log.e("remotedroid", "Error while creating OutWriter", e);
                //Toast.makeText(context,"Error while connecting",Toast.LENGTH_LONG).show();
            }
        }
    }
}