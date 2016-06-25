
/* This application was Designed, Implemented, Tested and Documented by Khalil Khalaf Lawrence Technological Univeristy Southfield, MI USA in Spring 2016
This is the Client part of the Multi Purpose Connected Robot Senior Project for my Masters of Computer Science in Intelligent Systems 2015 - 2017
For any additional Information or questions: KhalafKhalil@gmail.com
See it working: https://www.youtube.com/watch?v=XvDxw0TYJYU */


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

    boolean VFlag = true;
    Thread VideoThread = new Thread();
    Thread L2BotThread = new Thread();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String FrameRequest = "Picture";
        final String MoveLeft = "Left";
        final String MoveRight = "Right";
        final String MoveBackwards = "Backward";
        final String MoveForwards = "Forward";

        // video stream control
        Button StrtVBtn = (Button) findViewById(R.id.StartVButton);
        Button StpVBtn = (Button) findViewById(R.id.StopVButton);

        // Bot Movement Control
        Button ForwdBtn = (Button) findViewById(R.id.ForwardButton);
        Button BackwdBtn = (Button) findViewById(R.id.BackwardButton);
        Button RightBtn = (Button) findViewById(R.id.RightButton);
        Button LeftVBtn = (Button) findViewById(R.id.LeftButton);

        // Start Button Click
        StrtVBtn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        SendRequestAndReceiveAndDisplaySinglePicture(FrameRequest);
                    }
                }
        );

        // Stop Button Click
        StpVBtn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        VideoThread.interrupt();
                    }
                }
        );

        // Forward Button Click
        ForwdBtn.setOnTouchListener(
                new Button.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            //Button pressed
                            ControlL2Bot(MoveForwards);

                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            //Button released
                            ControlL2Bot("Stop");
                            return true;
                        }
                        return false;
                    }//onTouch
                });//setOnTouchListener

        // Backward Button Click
        BackwdBtn.setOnTouchListener(
                new Button.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            //Button pressed
                            ControlL2Bot(MoveBackwards);
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            //Button released
                            ControlL2Bot("Stop");
                            return true;
                        }
                        return false;
                    }//onTouch
                });//setOnTouchListener

        // Right Button Click
        RightBtn.setOnTouchListener(
                new Button.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            //Button pressed
                            ControlL2Bot(MoveRight);
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            //Button released
                            ControlL2Bot("Stop");
                            return true;
                        }
                        return false;
                    }//onTouch
                });//setOnTouchListener

        // Left Button Click
        LeftVBtn.setOnTouchListener(
                new Button.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            //Button pressed
                            ControlL2Bot(MoveLeft);
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            //Button released do nothing
                            ControlL2Bot("Stop");
                            return true;
                        }
                        return false;
                    }//onTouch
                });//setOnTouchListener
    }

    @Override
    protected void onStart()
    {
        // This Function was not used in the current project
        super.onStart();
    }

    public void SendRequestAndReceiveAndDisplaySinglePicture(final String FrameRequest)
    {
        // This function is responsible of the video stream
        // It creates an infinite loop to keep sending "FrameRequests" to the server where each FrameRequest is a single image

        // prepare a network thread
        VideoThread = new Thread(new Runnable()
        {
            public void run()
            {
            String host = "192.168.200.3"; // Server's IP
            int port = 15000;
            DatagramSocket dsocket;
            while(!VideoThread.isInterrupted())
            {
                try {
                    // Get the Internet address of the specified host
                    InetAddress address = InetAddress.getByName(host);

                    // wrap a packet
                    DatagramPacket packetToSend = new DatagramPacket(
                            FrameRequest.getBytes(),
                            FrameRequest.length(),
                            address, port);

                    // Create a datagram socket and send the packet through it.
                    dsocket = new DatagramSocket();
                    dsocket.send(packetToSend);

                    // receive the response
                    byte[] buffer = new byte[65535]; // prepare
                    DatagramPacket packetReceived = new DatagramPacket(buffer, buffer.length); // prepare

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

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } });

        VideoThread.start(); // execute the network thread
    }

    public void ControlL2Bot(final String ControlRequest)
    {
        // This function is responsible of sending a string message to the server.
        // The string message "ControlRequest" controls the bot movement on the serve side

        // prepare a network thread
        L2BotThread = new Thread() {

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
                        ControlRequest.getBytes(),
                        ControlRequest.length(),
                        address, port);

                // Create a datagram socket and send the packet through it.
                dsocket = new DatagramSocket();
                dsocket.send(packetToSend);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };
        L2BotThread.start(); // execute the network thread
    }
}
