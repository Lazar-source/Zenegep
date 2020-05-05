package com.example.zenegep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ClientVoteActivity extends AppCompatActivity {
    ListView musicListView;
    static final String serverIp = ClientActivity.serverIp;
    static public ArrayList<String> musicListID = new ArrayList<>();
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_vote);
        myclientelinditas();
        musicListView = findViewById(R.id.musicList);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, musicListID);
        musicListView.setAdapter(adapter);
        musicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mireKattintottal= musicListID.get(position);
                String response="Torles:"+mireKattintottal;
                //Toast.makeText(ClientMusicSelectActivity.this,""+mireKattintottálId,Toast.LENGTH_SHORT).show();
                 MyClientTask myClientTask = new MyClientTask(serverIp, 8080,response);
                myClientTask.execute();
            }
        });


        }

    public void myclientelinditas()
    {
        MyClientTask myClientTask = new MyClientTask(serverIp, 8080, "szavazas");
        myClientTask.execute();

    }






    public class MyClientTask extends AsyncTask<Void, Void, Void> {
        String dstAddress;
        int dstPort;
        String response = "";
        String msgToServer;

        MyClientTask(String addr, int port, String msgTo) {
            dstAddress = addr;
            dstPort = port;
            msgToServer = msgTo;
        }


        public ArrayList getMusicList() {
            Socket socket = null;
            ArrayList<String> musicList = new ArrayList<String>();
            DataInputStream dataInputStream = null;
            boolean End = false;
            try {
                while (!End) {
                    socket = new Socket(dstAddress, dstPort);
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    response = dataInputStream.readUTF();
                    if (response.equals("End")) {
                        End = true;
                    } else {
                        musicList.add(response);
                    }
                }


            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return musicList;
        }


        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                    ClientVoteActivity.musicListID = getMusicList();



            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}




