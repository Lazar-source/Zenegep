package com.example.zenegep;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Pair;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.zenegep.ServerActivity.*;

public class YoutubeActivity extends YouTubeBaseActivity
implements YouTubePlayer.OnInitializedListener {
    private static String GOOGLE_API_KEY = "AIzaSyBNk8C_vUyaMjIvPb6RnekVZ2i6p0xEz7c";
    private static String YOUTUBE_VIDEO_ID = "EmFED7vdk7Y";
    public static TextView info, infoip, msg;
    private YouTubePlayer youTubePlayer;
    private YouTubePlayerView youTubePlayerView;
    public static String[] Music = new String[255];
    public static int count = 0;
    public static int[] Prio = new int[255];
    public static Set<String> ipAddresses = new HashSet<String>();
    private static final String TAG = "MyActivity";
    private static final String TABLE_NAME = DatabaseHelper.TABLE_SERVER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        youTubePlayerView = findViewById(R.id.youtube_player);
        youTubePlayerView.initialize(GOOGLE_API_KEY, this);
        info = findViewById(R.id.info);
        infoip = findViewById(R.id.infoip);
        msg = findViewById(R.id.msg);
        infoip.setText(getIpAddress());
        StartServerThread(this);
        for(int i=0;i<255;i++)
        {
            Music[i]=" ";
            Prio[i]=0;
        }
    }

    public void StartServerThread(Context context){
        ServerinBackground sb = new ServerinBackground();
        ServerinBackground.SocketServerThread st = sb.new SocketServerThread(context);
        st.start();
    }

    public String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "SiteLocalAddress: "
                                + inetAddress.getHostAddress() + "\n";
                    }
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }
        return ip;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean b) {
        Toast.makeText(this, "Initialized Youtube Player successfully", Toast.LENGTH_LONG).show();
        youTubePlayer = player;
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);
        youTubePlayer.cueVideo(YOUTUBE_VIDEO_ID);
    }

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {
            Toast.makeText(YoutubeActivity.this, "Good, video is playing ok", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onPaused() {
            Toast.makeText(YoutubeActivity.this, "Video has paused", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onStopped() {}

        @Override
        public void onBuffering(boolean b) {}

        @Override
        public void onSeekTo(int i) {}
    };

    YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {}

        @Override
        public void onLoaded(String s) {}

        @Override
        public void onAdStarted() {
            Toast.makeText(YoutubeActivity.this, "Click Ad now, make the video creator rich!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onVideoStarted() {
            Toast.makeText(YoutubeActivity.this, "Video has started!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onVideoEnded() {
            Toast.makeText(YoutubeActivity.this, "Thanks for watching!", Toast.LENGTH_LONG).show();
            //TODO inicializálni ezt a fosást, hogy egy if-el tudjuk ellenőrizni van-e beküldött zene
            YOUTUBE_VIDEO_ID = Music[count];
            count--;
            youTubePlayer.cueVideo(YOUTUBE_VIDEO_ID);
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {}
    };

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "Failed to Initialize Youtube Player", Toast.LENGTH_LONG).show();
    }

    static class ServerinBackground extends Activity {
        String message = "";
        String reply;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        class SocketServerThread extends Thread {
            Context context;
            public SocketServerThread(Context context) {
                this.context=context;
            }

            static final int SocketServerPORT = 8080;
            private ServerSocket serverSocket;

            @Override
            public void run() {
                DatabaseHelper dh = new DatabaseHelper(context);
                Socket socket = null;
                DataInputStream dataInputStream = null;
                DataOutputStream dataOutputStream = null;
                try {
                    serverSocket = new ServerSocket(SocketServerPORT);
                    ServerinBackground.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            info.setText("I'm waiting here: "
                                    + serverSocket.getLocalPort());
                        }
                    });

                    while (true) {
                        socket = serverSocket.accept();
                        dataInputStream = new DataInputStream(
                                socket.getInputStream());
                        dataOutputStream = new DataOutputStream(
                                socket.getOutputStream());

                        String messageFromClient = "";

                        //If no message sent from client, this code will block the program
                        messageFromClient = dataInputStream.readUTF();
                        message=messageFromClient;

                        if (dh.isInDatabase(message,DatabaseHelper.TABLE_SERVER)) {
                            boolean tartalmaz = false;
                            int index = 0;
                            for (int i = 0; i < count && !tartalmaz; i++) {
                                if (message.equals(Music[i])) {
                                    tartalmaz = true;
                                    index = i;
                                }
                            }

                            if (!tartalmaz) {

                                Music[count] = message;
                                Prio[count]++;
                                dh.updateSql(TABLE_NAME, Music[count]);
                                count++;
                            } else {
                                Prio[index]++;
                                for (int i = 0; i < count; i++) {
                                    if (Prio[i] < Prio[i + 1]) {
                                        int temp;
                                        String stemp;
                                        temp = Prio[i];
                                        stemp = Music[i];
                                        Prio[i] = Prio[i + 1];
                                        Music[i] = Music[i + 1];
                                        Music[i + 1] = stemp;
                                        Prio[i + 1] = temp;
                                    }
                                }
                            }
                            reply="added";
                            dataOutputStream.writeUTF(reply);
                        }
                        else if (message.equals("connect")){
                            ipAddresses.add(socket.getInetAddress().toString());
                            //Log.d("Teszt",ipAddresses.toString());
                            reply = "connected";
                            dataOutputStream.writeUTF(reply);
                        }

                        ServerinBackground.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                msg.setText(message);
                            }
                        });


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    final String errMsg = e.toString();
                    ServerinBackground.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            msg.setText(errMsg);
                        }
                    });
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
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
                    if (dataOutputStream != null) {
                        try {
                            dataOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}





