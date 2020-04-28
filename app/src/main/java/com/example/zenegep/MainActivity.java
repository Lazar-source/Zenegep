package com.example.zenegep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
   /* public void openclient(){
        Intent intent= new Intent(getApplicationContext(),ClientActivity.class);
        startActivity(intent);
    }
    public void openszerver() {
        Intent intent = new Intent(getApplicationContext(), ServerActivity.class);
        startActivity(intent);
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button =  findViewById(R.id.button_kliens);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),ClientActivity.class);
                startActivity(intent);
            }
        });
        button =  findViewById(R.id.button_szerver);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), YoutubeActivity.class);
                startActivity(intent);
            }
        });
        DatabaseHelper dh = new DatabaseHelper(this);
    }

}
