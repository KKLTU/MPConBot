package com.example.kkhalaf.mpconbot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.net.*;


public class MainActivity extends AppCompatActivity {

    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button Declaration
        final TextView TextOne = (TextView) findViewById(R.id.StatusText);
        Button FrwrdBtn = (Button) findViewById(R.id.ForwardButton);
        Button StpBtn = (Button) findViewById(R.id.StopButton);
        Button BkwrdBtn = (Button) findViewById(R.id.BackwardButton);
        Button RgtBtn = (Button) findViewById(R.id.RightButton);
        Button LftBtn = (Button) findViewById(R.id.LeftButton);

        // Forward Button Click
        FrwrdBtn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        TextOne.setText("Forward");

                        message = "Forward";
                        SendUdpMsg(message);
                    }//onClick
                }//onClickListener
        );//setOnClickListener

        // Backward Button Click
        BkwrdBtn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        TextOne.setText("Backward");

                        message = "Backward";
                        SendUdpMsg(message);
                    }//onClick
                }//onClickListener
        );//setOnClickListener

        // Stop Button Click
        StpBtn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        TextOne.setText("Stop");

                        message = "Stop";
                        SendUdpMsg(message);
                    }//onClick
                }//onClickListener
        );//setOnClickListener

        // Right Button Click
        RgtBtn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        TextOne.setText("Right");

                        message = "Right";
                        SendUdpMsg(message);
                    }//onClick
                }//onClickListener
        );//setOnClickListener

        // Left Button Click
        LftBtn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        TextOne.setText("Left");

                        message = "Left";
                        SendUdpMsg(message);
                    }//onClick
                }//onClickListener
        );//setOnClickListener
    }


    // This function is responsible for sending a udp packet to a hardCoded IP below
    public void SendUdpMsg(final String msg)
    {
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
                            msg.getBytes(),
                            msg.length(),
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
    }
}
