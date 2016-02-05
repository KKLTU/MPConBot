package com.example.kkhalaf.mpconbot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.net.DatagramSocket;


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
                                
                            }
                        }
                );
    }

}

