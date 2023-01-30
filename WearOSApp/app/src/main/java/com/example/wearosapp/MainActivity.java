package com.example.wearosapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wearosapp.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends Activity {

    private PrintWriter output;
    private BufferedReader input;

    private Button mConnectButton;
    private TextView mIPTextView;
    private Socket socket;
    private String TAG="ABHI";
    private String SERVER_IP;
    private int SERVER_PORT=4321;
    private boolean connected=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mIPTextView= findViewById(R.id.IPTextView);
        mConnectButton= findViewById(R.id.connectButton);

        mConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connected)
                {
                    try {
                        if(socket!=null)
                        {
                            socket.close();
                            connected=false;
                            Log.i(TAG, "Closed");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    SERVER_IP=mIPTextView.getText().toString();
                    connected=true;
                    new ConnectToServer().execute("");
                }

            }
        });


        //mTextView = binding.text;
    }

    class ConnectToServer extends AsyncTask {
        @Override
        protected Object doInBackground(Object... arg0) {
            try {
                //new socket created
                Log.i(TAG, "Connecting");
                socket = new Socket(SERVER_IP, SERVER_PORT);
                output = new PrintWriter(socket.getOutputStream());
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Log.i(TAG, "Connected"+socket.isInputShutdown());
                Log.i(TAG, "Connected"+socket.isOutputShutdown());
                Log.i(TAG, "Connected"+socket.isConnected());
                //start a new thread for additional connections
                //new Thread(new Thread1()).start();

                //receive message from server
                /*
                String messageFromClient;
                messageFromClient = input.readLine();
                Log.i(TAG, "From Server: " + messageFromClient);
                */
                //send message to server

                String message = "Hello from client\n";
                while(!socket.isClosed())
                {
                    output.write(message);
                    output.flush();
                    Log.i(TAG, "Sending message");
                    Thread.sleep(1000);
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}