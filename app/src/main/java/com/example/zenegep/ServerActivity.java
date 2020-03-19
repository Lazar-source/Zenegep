package com.example.zenegep;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerActivity extends AppCompatActivity{
    Connection conn;
    private static final String TAG = ServerActivity.class.getSimpleName();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        Button button = (Button) findViewById(R.id.serverbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conn = new Connection();
                conn.execute();
            }
        });
    }

    class Connection extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                server_elinditas();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }



    }



    private static ServerSocket server;
    private static int port = 9876;

    public void server_elinditas() throws IOException, ClassNotFoundException {
        server = new ServerSocket(port);
        while (true) {
            //final TextView simpleTextView=(TextView) findViewById(R.id.servertextview);
            //simpleTextView.setText("Waiting for the client request");
            Log.d(TAG,"Waiting for the client request");
           // System.out.println("Waiting for the client request");
            Socket socket = server.accept();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String message = (String) ois.readObject();
            //simpleTextView.setText("Message Received: "+message);
            Log.d(TAG,"Message received: "+message);
           // System.out.println("Message Received: " + message);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
           // simpleTextView.setText("Hi Client " + message);
            Log.d(TAG,"Hi Client"+message);
            oos.writeObject("Hi Client " + message);
            ois.close();
            oos.close();
            socket.close();
            if (message.equalsIgnoreCase("exit")) break;
        }
        //final TextView simpleTextView=(TextView) findViewById(R.id.servertextview);
        //simpleTextView.setText("Shutting down Socket server!!");
        Log.d(TAG,"Shutting down Socket server!!");
       // System.out.println("Shutting down Socket server!!");
        server.close();
    }
}
