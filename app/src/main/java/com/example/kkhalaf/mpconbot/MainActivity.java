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
                                TextOne.setText("Hi");

                                String host = "127.0.0.1"; // localhost
                                int port = 15000;
                                String message = "Test";
                                DatagramSocket dsocket = null;

                                if (android.os.Build.VERSION.SDK_INT > 9)
                                {
                                    StrictMode.ThreadPolicy policy =
                                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                    StrictMode.setThreadPolicy(policy);
                                }

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
                                }
                            }
                        }
                );
    }

}

