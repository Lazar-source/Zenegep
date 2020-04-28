package com.example.zenegep;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
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
    DatabaseHelper myDb;
    TextView textResponse;
    EditText editTextAddress, editTextPort;
    Button buttonConnect, buttonClear;
    ListView listView;
    EditText welcomeMsg;
    ArrayAdapter adapter;
    ArrayList<String> musicList;
    ArrayList<String> musicIdList;
    private final static String TABLE_NAME = "Kliens";
    DatabaseHelper dh;

    public static String serverIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        dh = new DatabaseHelper(this);
        dh.initDatabase(TABLE_NAME);
        editTextAddress =  findViewById(R.id.address);
        editTextPort =  findViewById(R.id.port);
        buttonConnect =  findViewById(R.id.connect);
        buttonClear = findViewById(R.id.clear);
        textResponse =  findViewById(R.id.response);
        welcomeMsg = findViewById(R.id.welcomemsg);
        buttonConnect.setOnClickListener(buttonConnectOnClickListener);
        buttonClear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                textResponse.setText("");
            }
        });
        final MyClientTask myclienttask=new MyClientTask("",8080,"");

        //ez van a lista megjelenítéshez, törölendő lesz majd egyszer
        listView=findViewById(R.id.listview);
        musicList = dh.getMusicList(TABLE_NAME);
        musicIdList = dh.getVideoIDList(TABLE_NAME);
        adapter= new ArrayAdapter(this,android.R.layout.simple_list_item_1,musicList);
        listView.setAdapter(adapter);


        //kattintós cucc
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String mireKattintottál = listView.getItemAtPosition(position).toString();
                String mireKattintottálId= musicIdList.get(position);
               // Log.d("Teszt",mireKattintottál);
               Toast.makeText(ClientActivity.this,""+mireKattintottálId,Toast.LENGTH_SHORT).show();
                MyClientTask myClientTask = new MyClientTask(editTextAddress
                        .getText().toString(), Integer.parseInt(editTextPort
                        .getText().toString()),
                        mireKattintottálId);
                myClientTask.execute();
                dh.updateSql(TABLE_NAME, mireKattintottálId);    // a tábla frissítése
            }
        });

        }



    OnClickListener buttonConnectOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            serverIp = editTextAddress.getText().toString();
            String tMsg = welcomeMsg.getText().toString();
            if(tMsg.equals("")){
                tMsg = null;
                Toast.makeText(ClientActivity.this, "No Welcome Msg sent", Toast.LENGTH_SHORT).show();
            }

            MyClientTask myClientTask = new MyClientTask(editTextAddress
                    .getText().toString(), Integer.parseInt(editTextPort
                    .getText().toString()),
                    tMsg);
            myClientTask.execute();
        }
    };

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
                // TODO Auto-generated catch block
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
            textResponse.setText(response);
            super.onPostExecute(result);
        }

    }

}
