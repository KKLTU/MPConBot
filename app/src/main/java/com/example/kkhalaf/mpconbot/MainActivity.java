
/* This application was Designed, Implemented, Tested and Documented by Khalil Khalaf Lawrence Technological Univeristy Southfield, MI USA in Spring 2016
This is the Client part of the Multi Purpose Connected Robot Senior Project for my Masters of Computer Science in Intelligent Systems 2015 - 2017
For any additional Information or questions: KhalafKhalil@gmail.com
See it working: https://www.youtube.com/watch?v=XvDxw0TYJYU */


package com.example.kkhalaf.mpconbot;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.MotionEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.content.ContextWrapper;
//import org.jcodec.api.awt.SequenceEncoder;


public class MainActivity extends AppCompatActivity {

    Thread VideoThread = new Thread();
    Thread L2BotThread = new Thread();
    ArrayList<String> ImagePathes = new ArrayList<String>();;
    boolean SaveImage = false;

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
        final ImageButton StrtVBtn = (ImageButton) findViewById(R.id.StartVButton);
        final ImageButton StpVBtn = (ImageButton) findViewById(R.id.StopVButton);

        // Bot Movement Control
        final ImageButton ForwdBtn = (ImageButton) findViewById(R.id.ForwardButton);
        final ImageButton BackwdBtn = (ImageButton) findViewById(R.id.BackwardButton);
        final ImageButton RightBtn = (ImageButton) findViewById(R.id.RightButton);
        final ImageButton LeftBtn = (ImageButton) findViewById(R.id.LeftButton);

        // Other Buttons
        final ImageButton TakeSnapshotBtn = (ImageButton) findViewById(R.id.Snapshot);
        final ImageButton GoToGalleryBtn = (ImageButton) findViewById(R.id.Gallery);



        // Start Video Steam Button Click
        StrtVBtn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        StartVideoStreaming(FrameRequest); // Start video streaming
                        //StpVBtn.setVisibility(View.VISIBLE);
                        TakeSnapshotBtn.setVisibility(View.VISIBLE);
                        StrtVBtn.setVisibility(View.INVISIBLE);

                        ForwdBtn.setVisibility(View.VISIBLE);
                        BackwdBtn.setVisibility(View.VISIBLE);
                        LeftBtn.setVisibility(View.VISIBLE);
                        RightBtn.setVisibility(View.VISIBLE);
                        GoToGalleryBtn.setVisibility(View.VISIBLE);
                    }
                }
        );

        // Stop Video Stream Button Click
        StpVBtn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        VideoThread.interrupt(); // Stop Video Streaming
                        StrtVBtn.setVisibility(View.VISIBLE);
                        StpVBtn.setVisibility(View.INVISIBLE);
                        TakeSnapshotBtn.setVisibility(View.INVISIBLE);
                    }
                }
        );

        // Snapshot Button Click
        TakeSnapshotBtn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        SaveImage = true;
                    }
                }
        );

        // Snapshot Button Click
        GoToGalleryBtn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        GoToGallery();
                    }
                }
        );

        // Move Forward Button Click
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

        // Move Backward Button Click
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

        // Move Right Button Click
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

        // Move Left Button Click
        LeftBtn.setOnTouchListener(
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

    public void StartVideoStreaming(final String FrameRequest)
    {
        // This function is responsible of the video stream
        // It creates an infinite loop to keep sending "FrameRequests" to the server where each FrameRequest is a single image

        final ImageView imageView = (ImageView) findViewById(R.id.GalleryImageView);

        // prepare a network thread
        VideoThread = new Thread(new Runnable()
        {
            public void run()
            {
            String host = "192.168.1.100"; // Server's IP
            int port = 15000;
            DatagramSocket dsocket;
                int ImageNumber = 0; // for recording

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
                    final Bitmap ReceivedImage = BitmapFactory.decodeByteArray(buff, 0, buff.length); // convert byte[] to bitmap image

                    if(SaveImage) {
                        saveToInternalStorage(ReceivedImage, ImageNumber);
                        ImageNumber++;
                        SaveImage = false;
                    }

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            imageView.setImageBitmap(ReceivedImage); // this is executed on the main (UI) thread
                        }
                    });
                    //Thread.sleep(50);
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
        L2BotThread = new Thread()
        {

        // No local Host 127.0.0.1 in Android
        String host = "192.168.1.100"; // Server's IP
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


    private void saveToInternalStorage(Bitmap bitmapImage, int ImageNumber)
    {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());

        // path to /data/data/MyApp/app_data/imageDir
        File MyDirectory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        // Create imageDir
        File MyPath = new File(MyDirectory,"Image" + ImageNumber + ".jpg");

        // Add the path to the container
        ImagePathes.add("Image" + ImageNumber + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(MyPath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //return MyDirectory.getAbsolutePath();
    }

    private void DisplaySavedImage()
    {

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File MyDirectory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        String path = MyDirectory.getAbsolutePath();

        try {
            File f=new File(path, "Image0.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img=(ImageView)findViewById(R.id.GalleryImageView);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    public void GoToGallery()
    {
        Intent GalleryIntenet = new Intent(this, GalleryActivity.class);

        GalleryIntenet.putExtra("ImagePathes", ImagePathes);
        startActivity(GalleryIntenet);
    }


}

