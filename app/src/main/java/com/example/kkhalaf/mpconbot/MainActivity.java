package com.example.kkhalaf.mpconbot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.MotionEvent;
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
        Button BkwrdBtn = (Button) findViewById(R.id.BackwardButton);
        Button RgtBtn = (Button) findViewById(R.id.RightButton);
        Button LftBtn = (Button) findViewById(R.id.LeftButton);

        // Forward Button Click
        FrwrdBtn.setOnTouchListener(
                new Button.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            //Button pressed
                            TextOne.setText("Forward");
                            message = "Forward";
                            SendUdpMsg(message);
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            //Button released
                            TextOne.setText("Stop");
                            message = "Stop";
                            SendUdpMsg(message);
                            return true;
                        }
                        return false;
                    }//onTouch
                });//setOnTouchListener

        // Backward Button Click
        BkwrdBtn.setOnTouchListener(
                new Button.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            //Button pressed
                            TextOne.setText("Backward");
                            message = "Backward";
                            SendUdpMsg(message);
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            //Button released
                            TextOne.setText("Stop");
                            message = "Stop";
                            SendUdpMsg(message);
                            return true;
                        }
                        return false;
                    }//onTouch
                });//setOnTouchListener

        // Right Button Click
        RgtBtn.setOnTouchListener(
                new Button.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            //Button pressed
                            TextOne.setText("Right");
                            message = "Right";
                            SendUdpMsg(message);
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            //Button released
                            TextOne.setText("Stop");
                            message = "Stop";
                            SendUdpMsg(message);
                            return true;
                        }
                        return false;
                    }//onTouch
                });//setOnTouchListener

        // Left Button Click
        LftBtn.setOnTouchListener(
                new Button.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            //Button pressed
                            TextOne.setText("Left");
                            message = "Left";
                            SendUdpMsg(message);
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            //Button released
                            TextOne.setText("Stop");
                            message = "Stop";
                            SendUdpMsg(message);
                            return true;
                        }
                        return false;
                    }//onTouch
                });//setOnTouchListener
    }


    // This function is responsible for sending a udp packet to a hardCoded IP below. Returns nothing and takes a string(the message) as a parameter.
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
