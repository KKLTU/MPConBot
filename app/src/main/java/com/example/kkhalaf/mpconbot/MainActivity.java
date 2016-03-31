package com.example.kkhalaf.mpconbot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.MotionEvent;
import java.net.*;
import java.lang.Object;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class MainActivity extends AppCompatActivity {

    String message;
    String Response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button StrtBtn = (Button) findViewById(R.id.StartButton);
        Button StpBtn = (Button) findViewById(R.id.StopButton);
        //final ImageView imageView = (ImageView)findViewById(R.id.imageView);

        // Start Button Click
        StrtBtn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        message = "StartVideo";
                        SendUdpMsg();
                    }//onClick
                }//onClickListener
        );//setOnClickListener

        // Stop Button Click
        StpBtn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        message = "StopVideo";
                        //SendUdpMsg();
                    }//onClick
                }//onClickListener
        );//setOnClickListener

//        // Button Declaration
//        final TextView TextOne = (TextView) findViewById(R.id.StatusText);
//        Button FrwrdBtn = (Button) findViewById(R.id.ForwardButton);
//        Button BkwrdBtn = (Button) findViewById(R.id.BackwardButton);
//        Button RgtBtn = (Button) findViewById(R.id.RightButton);
//        Button LftBtn = (Button) findViewById(R.id.LeftButton);
//
//        // Forward Button Click
//        FrwrdBtn.setOnTouchListener(
//                new Button.OnTouchListener() {
//                    public boolean onTouch(View v, MotionEvent event) {
//                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                            //Button pressed
//                            TextOne.setText("Forward");
//                            message = "Forward";
//                            SendUdpMsg(message);
//                            return true;
//                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                            //Button released
//                            TextOne.setText("Stop");
//                            message = "Stop";
//                            SendUdpMsg(message);
//                            return true;
//                        }
//                        return false;
//                    }//onTouch
//                });//setOnTouchListener
//
//        // Backward Button Click
//        BkwrdBtn.setOnTouchListener(
//                new Button.OnTouchListener() {
//                    public boolean onTouch(View v, MotionEvent event) {
//                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                            //Button pressed
//                            TextOne.setText("Backward");
//                            message = "Backward";
//                            SendUdpMsg(message);
//                            return true;
//                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                            //Button released
//                            TextOne.setText("Stop");
//                            message = "Stop";
//                            SendUdpMsg(message);
//                            return true;
//                        }
//                        return false;
//                    }//onTouch
//                });//setOnTouchListener
//
//        // Right Button Click
//        RgtBtn.setOnTouchListener(
//                new Button.OnTouchListener() {
//                    public boolean onTouch(View v, MotionEvent event) {
//                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                            //Button pressed
//                            TextOne.setText("Right");
//                            message = "Right";
//                            SendUdpMsg(message);
//                            return true;
//                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                            //Button released
//                            TextOne.setText("Stop");
//                            message = "Stop";
//                            SendUdpMsg(message);
//                            return true;
//                        }
//                        return false;
//                    }//onTouch
//                });//setOnTouchListener
//
//        // Left Button Click
//        LftBtn.setOnTouchListener(
//                new Button.OnTouchListener() {
//                    public boolean onTouch(View v, MotionEvent event) {
//                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                            //Button pressed
//                            TextOne.setText("Left");
//                            message = "Left";
//                            SendUdpMsg(message);
//                            return true;
//                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                            //Button released do nothing
//                            return true;
//                        }
//                        return false;
//                    }//onTouch
//                });//setOnTouchListener
    }

    // This function is responsible for sending a udp packet to a hardCoded IP below. Returns nothing and takes a string(the message) as a parameter.
    public void SendUdpMsg()
    {
        Thread networkThread = new Thread() {

            // No local Host 127.0.0.1 in Android
            String host = "192.168.200.3"; // Server's IP
            int port = 15000;
            DatagramSocket dsocket = null;

            public void run() {
                try {
                    // Get the Internet address of the specified host
                    InetAddress address = InetAddress.getByName(host);

                    // wrap a packet
                    DatagramPacket packetToSend = new DatagramPacket(
                            message.getBytes(),
                            message.length(),
                            address, port);

                    // Create a datagram socket, send the packet through it.
                    dsocket = new DatagramSocket();
                    dsocket.send(packetToSend);

                    // Here, I am receiving the response
                    byte[] buffer = new byte[65535]; // prepare
                    DatagramPacket packetReceived = new DatagramPacket(buffer, buffer.length); // prepare

                    while (true)
                    {
                        dsocket.receive(packetReceived); // receive packet
                        byte[] buff = packetReceived.getData(); // convert packet to byte[]
                        final Bitmap ReceivedImage = BitmapFactory.decodeByteArray(buff, 0, buff.length); // convert byte[] to image
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                // this is executed on the main (UI) thread
                                final ImageView imageView = (ImageView) findViewById(R.id.imageView);
                                imageView.setImageBitmap(ReceivedImage);
                            }
                        });
                    }//
                    //dsocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }//catch
            }//run
        };// Networkthread
        networkThread.start();//networkThread.start()
    }

}
