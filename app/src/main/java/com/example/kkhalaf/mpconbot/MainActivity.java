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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        message = "StartV";
                        SendUdpMsg();
                    }
                }
        );

        // Stop Button Click
        StpVBtn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        message = "StopV";

                    }
                }
        );

        // Forward Button Click
        ForwdBtn.setOnTouchListener(
                new Button.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            //Button pressed
                            message = "Forward";

                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            //Button released
                            message = "Stop";
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
                            message = "Backward";
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            //Button released
                            message = "Stop";
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
                            message = "Right";
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            //Button released
                            message = "Stop";
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
                            message = "Left";
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            //Button released do nothing
                            message = "Stop";
                            return true;
                        }
                        return false;
                    }//onTouch
                });//setOnTouchListener
    }

    @Override
    protected void onStart()
    {
        super.onStart();

    }

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
                        if(message.equals("StartV"))
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

                            ////////////////////////////////////////////////

//                            // wrap a packet
//                            packetToSend = new DatagramPacket(
//                                    "StartV".getBytes(),
//                                    "StartV".length(),
//                                    address, port);
//
//                            // Create a datagram socket, send the packet through it.
//                            dsocket = new DatagramSocket();
//                            dsocket.send(packetToSend);

                            ///////////////////////////////////////////////

                        }
                        else
                        {
                            if (message.equals("Forward") || message.equals("Backward") || message.equals("Left") || message.equals("Stop") || message.equals("Right"))
                            {
                                // wrap a packet to send the Stop
                                packetToSend = new DatagramPacket(
                                        message.getBytes(),
                                        message.length(),
                                        address, port);

                                // Create a datagram socket, send the packet through it.
                                dsocket = new DatagramSocket();
                                dsocket.send(packetToSend);
                                message = "StartV";
                            }
                            else
                            {
                                packetToSend = new DatagramPacket(
                                message.getBytes(),
                                message.length(),
                                address, port);

                                // Create a datagram socket, send the packet through it.
                                dsocket = new DatagramSocket();
                                dsocket.send(packetToSend);
                                break; // break the whole thread
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        networkThread.start();
    }
}




//package com.example.kkhalaf.mpconbot;
//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.view.MotionEvent;
//import java.net.*;
//import java.lang.Object;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//
//
//public class MainActivity extends AppCompatActivity {
//
//    String message;
//    String Response;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        Button StrtBtn = (Button) findViewById(R.id.StartVButton);
//        Button StpBtn = (Button) findViewById(R.id.StopVButton);
//        final ImageView imageView = (ImageView)findViewById(R.id.imageView);
//
//        // Start Button Click
//        StrtBtn.setOnClickListener(
//                new Button.OnClickListener() {
//                    public void onClick(View v) {
//                        message = "StartVideo";
//                        SendUdpMsg();
//                    }//onClick
//                }//onClickListener
//        );//setOnClickListener
//
//        // Stop Button Click
//        StpBtn.setOnClickListener(
//                new Button.OnClickListener() {
//                    public void onClick(View v) {
//                        message = "StopVideo";
//                        //SendUdpMsg();
//                    }//onClick
//                }//onClickListener
//        );//setOnClickListener
//    }
//
//    // This function is responsible for sending a udp packet to a hardCoded IP below. Returns nothing and takes a string(the message) as a parameter.
//    public void SendUdpMsg()
//    {
//        Thread networkThread = new Thread() {
//
//            // No local Host 127.0.0.1 in Android
//            String host = "192.168.200.3"; // Server's IP
//            int port = 15000;
//            DatagramSocket dsocket = null;
//
//            public void run() {
//                try {
//                    // Get the Internet address of the specified host
//                    InetAddress address = InetAddress.getByName(host);
//
//                    // wrap a packet
//                    DatagramPacket packetToSend = new DatagramPacket(
//                            message.getBytes(),
//                            message.length(),
//                            address, port);
//
//                    // Create a datagram socket, send the packet through it.
//                    dsocket = new DatagramSocket();
//                    dsocket.send(packetToSend);
//
//                    // Here, I am receiving the response
//                    byte[] buffer = new byte[65535]; // prepare
//                    DatagramPacket packetReceived = new DatagramPacket(buffer, buffer.length); // prepare
//
//                    while (true)
//                    {
//                        if(message == "StartVideo")
//                        {
//                            dsocket.receive(packetReceived); // receive packet
//                            byte[] buff = packetReceived.getData(); // convert packet to byte[]
//                            final Bitmap ReceivedImage = BitmapFactory.decodeByteArray(buff, 0, buff.length); // convert byte[] to image
//                            runOnUiThread(new Runnable()
//                            {
//                                @Override
//                                public void run()
//                                {
//                                    // this is executed on the main (UI) thread
//                                    final ImageView imageView = (ImageView) findViewById(R.id.imageView);
//                                    imageView.setImageBitmap(ReceivedImage);
//                                }
//                            });
//                        }
//                        else
//                        {
//                            // wrap a packet
//                            packetToSend = new DatagramPacket(
//                                    message.getBytes(),
//                                    message.length(),
//                                    address, port);
//
//                            // Create a datagram socket, send the packet through it.
//                            dsocket = new DatagramSocket();
//                            dsocket.send(packetToSend);
//                            break;
//                        }
//                    }
//                    //dsocket.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }//catch
//            }//run
//        };// Networkthread
//        networkThread.start();//networkThread.start()
//    }
//}





