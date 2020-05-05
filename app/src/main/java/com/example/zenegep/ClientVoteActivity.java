package com.example.zenegep;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;

public class ClientVoteActivity extends AppCompatActivity {
    ListView musicListView;
    static final String serverIp = ClientActivity.serverIp;
    public ArrayList<String> musicListID = new ArrayList<>();
    ArrayAdapter adapter;
    DatabaseHelper dh;
    TextView musicInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_vote);
        myclientelinditas();
        for (String s : musicListID)
            Log.d("sdf",s);
        musicInfo = findViewById(R.id.musInfo);
        musicListView = findViewById(R.id.musicList);
        musicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mireKattintottal= musicListID.get(position);
                String response="Torles:"+mireKattintottal;
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

    public void frissit(){

        dh = new DatabaseHelper(this);
        ArrayList<String> musicList = new ArrayList<>();
        for (String s :musicListID)
            musicList.add(dh.getMusicNameByID(s,DatabaseHelper.TABLE_CLIENT));

        if(!musicList.isEmpty())
            musicInfo.setText("");
        else
            musicInfo.setText("Nincs megjelenítendő zene!");
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, musicList);
        musicListView.setAdapter(adapter);
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


        public ArrayList<String> getMusicList() {
            Socket socket = null;
            ArrayList<String> musicList = new ArrayList<String>();
            DataInputStream dataInputStream = null;
            DataOutputStream dataOutputStream=null;
            try {

                socket = new Socket(dstAddress, dstPort);
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream=new DataOutputStream((socket.getOutputStream()));
                dataOutputStream.writeUTF("szavazas");
                final ObjectInputStream ArrayInputStream = new ObjectInputStream(dataInputStream);
                @SuppressWarnings("unchecked")
                final ArrayList<String> yourList=  (ArrayList<String>)ArrayInputStream.readObject();
                response="feltoltve";
                musicList=yourList;

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return musicList;
        }
        public void  SendTorles()
        {
            Socket socket = null;
            DataInputStream dataInputStream = null;
            DataOutputStream dataOutputStream;
            try {
                socket = new Socket(dstAddress, dstPort);
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream((socket.getOutputStream()));
                dataOutputStream.writeUTF(msgToServer);
                response=dataInputStream.readUTF();

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                if(msgToServer.equals("szavazas"))
                    musicListID = getMusicList();

                else if(msgToServer.contains("Torles:"))
                    SendTorles();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            if (response.equals("added")){

                Toast.makeText(ClientVoteActivity.this, "A zene törlési igénye hozzáadva!", Toast.LENGTH_LONG).show();
            }
            else if(response.equals("deleted"))
            {
                int index=msgToServer.indexOf(":");
                String msg=msgToServer.substring((index+1));
                musicListID.remove(msg);
                frissit();
                Toast.makeText(ClientVoteActivity.this, "A zene törölve!", Toast.LENGTH_LONG).show();
            }
            else if(response.equals("feltoltve")){
                frissit();
            }
            super.onPostExecute(result);
        }
    }
}




