package com.example.zenegep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ClientMusicSelectActivity extends AppCompatActivity {
    ListView musicListView;
    ArrayList<String> musicList;
    ArrayList<String> musicIdList;
    ArrayAdapter adapter;
    static final  String serverIp= ClientActivity.serverIp;
    private final static String TABLE_NAME = DatabaseHelper.TABLE_CLIENT;
    DatabaseHelper dh;
    String musicToSuggest = ClientActivity.suggestedmusic;
    TextView suggestedMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_music_select);
        dh = new DatabaseHelper(this);
        musicList = dh.getMusicList(TABLE_NAME);
        musicIdList = dh.getVideoIDList(TABLE_NAME);
        musicListView = findViewById(R.id.musicList);
        suggestedMusic = findViewById(R.id.suggestedMusic);
        adapter= new ArrayAdapter(this,android.R.layout.simple_list_item_1,musicList);
        musicListView.setAdapter(adapter);
        suggestedMusic.setText(dh.getMusicNameByID(musicToSuggest,TABLE_NAME));

        musicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mireKattintottalId= musicIdList.get(position);
                MyClientTask myClientTask = new MyClientTask(serverIp, 8080,mireKattintottalId);
                myClientTask.execute();
            }
        });

        suggestedMusic.setOnClickListener(new View.OnClickListener() {
            int pos;
            @Override
            public void onClick(View v) {
                for (int i =0;i<musicList.size();i++)
                    if(musicList.get(i).equals(suggestedMusic.getText().toString()))
                        pos=i;

                String item = musicIdList.get(pos);
                MyClientTask myClientTask = new MyClientTask(serverIp,8080,item);
                myClientTask.execute();
            }
        });
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

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;

            try {
                socket = new Socket(dstAddress, dstPort);
                dataOutputStream = new DataOutputStream(
                        socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());

                if(msgToServer != null){
                    dataOutputStream.writeUTF(msgToServer);
                }
                response = dataInputStream.readUTF();



            } catch (UnknownHostException e) {
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {

                e.printStackTrace();
                response = "IOException: " + e.toString();
            }finally {
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
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (response.equals("added")){
                dh.updateSql(TABLE_NAME, msgToServer);
                Toast.makeText(ClientMusicSelectActivity.this, "A zene hozzáadva a lejátszási listához!", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
        }

    }

}
