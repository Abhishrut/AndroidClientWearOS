package com.example.wearosapp;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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

public class MainActivity extends Activity implements SensorEventListener{

    private PrintWriter output;
    private BufferedReader input;

    private Button mConnectButton;
    private TextView mIPTextView;
    private Socket socket;
    private String TAG="ABHI";
    private String SERVER_IP;
    private int SERVER_PORT=4321;
    private boolean connected=false;

    private SensorManager sensorManager;
    private Sensor stepsSensor;
    private Sensor heartbeatSensor;

    private TextView textViewHB;
    private TextView textViewSteps;
    private Sensor sensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepsSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        heartbeatSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
//        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        textViewHB = findViewById(R.id.textHB);
        textViewSteps = findViewById(R.id.stepcount);

        mIPTextView= findViewById(R.id.IPTextView);
        mConnectButton= findViewById(R.id.connectButton);

        mConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sensorManager.registerListener((SensorEventListener) view.getContext(), stepsSensor, SensorManager.SENSOR_DELAY_NORMAL);
                sensorManager.registerListener( (SensorEventListener) view.getContext(), heartbeatSensor, SensorManager.SENSOR_DELAY_NORMAL);

/*
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
*/
            }
        });


        //mTextView = binding.text;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //sensorManager.unregisterListener(this);
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        Log.i(TAG, "Hello");
        if(event.sensor.getType()==Sensor.TYPE_HEART_RATE)
        {
            Log.i(TAG, "onSensorChanged: Heartbeat");
            textViewHB.setText((int) event.values[0]+"");
        }
        else if(event.sensor.getType()==Sensor.TYPE_STEP_COUNTER)
        {
            Log.i(TAG, "onSensorChanged: stepcount");
            textViewHB.setText((int) event.values[0]+"");
        }
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

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