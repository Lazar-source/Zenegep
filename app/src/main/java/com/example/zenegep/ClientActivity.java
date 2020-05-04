package com.example.zenegep;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;

public class ClientActivity extends Activity {
    private static final String TABLE_NAME = DatabaseHelper.TABLE_CLIENT;
    TextView textResponse;
    EditText editTextAddress;
    Button buttonConnect;
    DatabaseHelper dh;
    public static String serverIp;
    ArrayList<String> musicIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        dh = new DatabaseHelper(this);
        editTextAddress =  findViewById(R.id.address);
        buttonConnect =  findViewById(R.id.connect);
        textResponse =  findViewById(R.id.response);
        musicIdList = dh.getVideoIDList(TABLE_NAME);
        buttonConnect.setOnClickListener(buttonConnectOnClickListener);

        }

    OnClickListener buttonConnectOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            //String tMsg = welcomeMsg.getText().toString();
            /*if(tMsg.equals("")){
                tMsg = null;
                Toast.makeText(ClientActivity.this, "No Welcome Msg sent", Toast.LENGTH_SHORT).show();
            }
*/
            MyClientTask myClientTask = new MyClientTask(editTextAddress.getText().toString(), 8080, "connect");
            myClientTask.execute();
        }
    };

    public class MyClientTask extends AsyncTask<Void, Void, Void> {
        String dstAddress;
        int dstPort;
        String response = "";
        String msgToServer;
        Map<String, Integer> playList = new HashMap<>();

        MyClientTask(String addr, int port, String msgTo) {
            dstAddress = addr;
            dstPort = port;
            msgToServer = msgTo;
        }
        MyClientTask(String addr, int port, Map playL) {
            dstAddress = addr;
            dstPort = port;
            playList=playL;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;
            if(msgToServer!=null) {
                try {
                    socket = new Socket(dstAddress, dstPort);
                    dataOutputStream = new DataOutputStream(
                            socket.getOutputStream());
                    dataInputStream = new DataInputStream(socket.getInputStream());

                    if (msgToServer != null) {
                        dataOutputStream.writeUTF(msgToServer);
                    }

                    response = dataInputStream.readUTF();


                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    response = "UnknownHostException: " + e.toString();
                } catch (IOException e) {

                    e.printStackTrace();
                    response = "IOException: " + e.toString();
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {

                            e.printStackTrace();
                        }
                    }

                    if (dataOutputStream != null) {
                        try {
                            dataOutputStream.close();
                        } catch (IOException e) {

                            e.printStackTrace();
                        }
                    }

                    if (dataInputStream != null) {
                        try {
                            dataInputStream.close();
                        } catch (IOException e) {

                            e.printStackTrace();
                        }
                    }
                }
            }
            else
            {

                try {
                    dataOutputStream =new DataOutputStream(
                            socket.getOutputStream());


                final ObjectOutputStream mapOutputStream = new ObjectOutputStream(dataOutputStream);
                mapOutputStream.writeObject(musicIdList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (response.equals("connected")){
                serverIp = dstAddress;
                MyClientTask myClientTask = new MyClientTask(editTextAddress.getText().toString(), 8080, playList);
                myClientTask.execute();

                Intent intent = new Intent(getApplicationContext(), ClientMenuActivity.class);
                startActivity(intent);
            }
            else {
                response = "Sikertelen csatlakozás!";
                textResponse.setText(response);
            }
            super.onPostExecute(result);
        }

    }

}
