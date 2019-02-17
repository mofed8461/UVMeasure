package com.dronecontrol.mofed.dronecontrol;

import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {


    TextView connectionStatus;

    EditText ipText;

    TextView uvText;

    EditText uvLimit;


    String getIP()
    {
        return ipText.getText().toString();
    }

    public String getContents(String myurl) {
        try {
            String sss = new RetrieveHTMLTask().execute(myurl).get().HTML;

            return sss;
        }
        catch (Exception ex)
        {
        }

        return "";

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipText = (EditText)findViewById(R.id.ipText);
        connectionStatus = (TextView)findViewById(R.id.status);

        uvText = (TextView) findViewById(R.id.uvVal);
        uvLimit = (EditText)findViewById(R.id.uvLimit);


        final Handler handler = new Handler();
        final int delay = 1000; //milliseconds

        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);


        handler.postDelayed(new Runnable(){
            public void run(){

                String web = getIP();

                try {
                    String asText = getContents(web);
//                    String asText = "1000";
                    float uv = Float.parseFloat(asText);
                    float uvLim = Float.parseFloat(uvLimit.getText().toString());
                    if (uv > uvLim)
                    {
                        uvText.setText(asText);
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this, "1234")
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("UV")
                                .setContentText("Exceeded")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        notificationManager.notify(12322, mBuilder.build());

                    }
                    else
                    {
                        uvText.setText(asText);

                    }
                    connectionStatus.setText("Connected");
                }
                catch (Exception ex)
                {
                    connectionStatus.setText("Failed to connect");
                }

                handler.postDelayed(this, delay);

            }
        }, delay);
    }
}


