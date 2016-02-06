package com.example.kkhalaf.mpconbot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.net.*;
import android.os.StrictMode;
import android.os.AsyncTask;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button Declaration
        Button ButtonOne = (Button) findViewById(R.id.TestButton);

        // Button Click
        ButtonOne.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        TextView TextOne = (TextView) findViewById(R.id.TestText);
                        TextOne.setText("Hii");

                        final String message = "Android App - Client";
                        Thread networkThread = new Thread() {

                            // No local Host 127.0.0.1 in Android
                            String host = "172.24.212.148"; // localhost
                            int port = 15000;
                            DatagramSocket dsocket = null;

                            public void run() {
                                try {
                                    // Get the Internet address of the specified host
                                    InetAddress address = InetAddress.getByName(host);

                                    // wrap a packet
                                    DatagramPacket packet = new DatagramPacket(
                                            message.getBytes(),
                                            message.length(),
                                            address, port);

                                    // Create a datagram socket, send the packet through it, close it.
                                    dsocket = new DatagramSocket();
                                    dsocket.send(packet);
                                    dsocket.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }//catch
                            }//run
                        };// Networkthread
                        networkThread.start();//networkThread.start()
                    }//onClick
                }//onClickListener
        );//setOnClickListener
    }
}

//This code doesn't run and pops two errors: "Error:(57, 32) error: incompatible types: void cannot be converted to Thread" on the .start() part and then "message is used from within an inner class and should be declared final".
