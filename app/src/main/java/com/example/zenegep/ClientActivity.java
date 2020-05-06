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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ClientActivity extends Activity {
    private static final String TABLE_NAME = DatabaseHelper.TABLE_CLIENT;
    TextView textResponse;
    EditText editTextAddress;
    Button buttonConnect;
    DatabaseHelper dh;
    public static String serverIp;
    ArrayList<String> musicIdList;
    ArrayList<Integer> musicSentList;
    public static String suggestedmusic;
    public static Map<String,Integer> musicIdMap=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        dh = new DatabaseHelper(this);
        editTextAddress =  findViewById(R.id.address);
        buttonConnect =  findViewById(R.id.connect);
        textResponse =  findViewById(R.id.response);
        musicIdList = dh.getVideoIDList(TABLE_NAME);
        musicSentList=dh.getVideoSentCount(TABLE_NAME);
        buttonConnect.setOnClickListener(buttonConnectOnClickListener);
        for(int i=0;i<musicIdList.size();i++)
        {
            musicIdMap.put(musicIdList.get(i),musicSentList.get(i));
        }

    }

    OnClickListener buttonConnectOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            MyClientTask myClientTask = new MyClientTask(editTextAddress.getText().toString(), 8080, "connect",musicIdMap);
            myClientTask.execute();
            MyClientTask myClientTask2= new MyClientTask(editTextAddress.getText().toString(), 8080,null, musicIdMap);
            myClientTask2.execute();
        }
    };

    public class MyClientTask extends AsyncTask<Void, Void, Void> {
        String dstAddress;
        int dstPort;
        String response = "";
        String msgToServer;
        Map<String, Integer> playList;

        public MyClientTask(String addr, int port, String msgTo, Map<String, Integer> map) {
            dstAddress = addr;
            dstPort = port;
            msgToServer = msgTo;
            playList=map;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;
            if(msgToServer != null) {
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
                    socket = new Socket(dstAddress, dstPort);
                    dataOutputStream =new DataOutputStream(
                            socket.getOutputStream());
                    dataOutputStream.writeUTF("object");
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    final Map<String, Integer> yourMap =playList;
                    final ObjectOutputStream mapOutputStream = new ObjectOutputStream(dataOutputStream);
                    mapOutputStream.writeObject(yourMap);

                    response = dataInputStream.readUTF();
                    suggestedmusic=response;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
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
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (response.equals("connected")){
                serverIp=dstAddress;
            }

            else if(dh.isInDatabase(response,TABLE_NAME)) {
                Intent intent = new Intent(getApplicationContext(), ClientMenuActivity.class);
                startActivity(intent);
            }

            else {
                super.onPostExecute(result);
                response = "Sikertelen csatlakozás!";
            }
            super.onPostExecute(result);
        }

    }

}