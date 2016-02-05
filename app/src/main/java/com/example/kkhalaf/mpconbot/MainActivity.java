package com.example.kkhalaf.mpconbot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.*;
import java.net.*;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button Declaration
        Button ButtonOne = (Button) findViewById(R.id.Test);

        // Test Button
        ButtonOne.setOnClickListener
                (
                        new Button.OnClickListener()
                        {
                            public void onClick(View v)
                            {
//                                String host = "127.0.0.1"; // localhost
//                                // String host = "198.111.37.84";
//                                int port = 15000;
//                                String message = "Test";
//                                DatagramSocket dsocket = null;
//
//                                try {
//                                    // Get the Internet address of the specified host
//                                    InetAddress address = InetAddress.getByName(host);
//
//                                    // Initialize a datagram packet with data and address
//                                    DatagramPacket packet = new DatagramPacket(
//                                            message.getBytes(),
//                                            message.length(),
//                                            address, port);
//
//                                    // Create a datagram socket, send the packet through it, close it.
//                                    dsocket = new DatagramSocket();
//                                    dsocket.send(packet);
//                                    dsocket.close();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
                                String udpMsg = "hello world from UDP client";
                                DatagramSocket ds = null;
                                try {
                                    ds = new DatagramSocket();
                                    InetAddress serverAddr = InetAddress.getByName("127.0.0.1");
                                    DatagramPacket dp;
                                    dp = new DatagramPacket(udpMsg.getBytes(), udpMsg.length(), serverAddr, 15000);
                                    ds.send(dp);
                                } catch (Exception e) {
                                    e.printStackTrace();
//                                } finally {
//                                    if (ds != null) {
//                                        ds.close();
//                                    }
                                }
                            }
                        }
                );

    }
}
