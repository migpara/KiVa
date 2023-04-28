package com.example.kiva;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Menu extends AppCompatActivity implements View.OnClickListener {
    ImageButton btnReporte, btnBlog, btnChat, btnInfo;
    private Bundle extras;
    private String centro;
    private String codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        extras = getIntent().getExtras();
        centro = extras.getString("centro");
        codigo = extras.getString("usuario");

        btnReporte = findViewById(R.id.btnReporte);
        btnReporte.setOnClickListener(this);
        btnBlog = findViewById(R.id.btnBlog);
        btnBlog.setOnClickListener(this);
        btnChat = findViewById(R.id.btnChat);
        btnChat.setOnClickListener(this);
        btnInfo = findViewById(R.id.btnInformacion);
        btnInfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReporte:
                Intent intent = new Intent(this, TipoReporte.class);
                intent.putExtra("centro", centro);
                startActivity(intent);
                break;
            case R.id.btnBlog:
                Intent intent1 = new Intent(this, Blog.class);
                intent1.putExtra("centro", centro);
                intent1.putExtra("usuario", codigo);
                startActivity(intent1);
                break;
            case R.id.btnChat:
                Intent intent2 = new Intent(this, Chat.class);
                intent2.putExtra("centro", centro);
                intent2.putExtra("usuario", codigo);
                startActivity(intent2);
                break;
            case R.id.btnInformacion:
                Intent intent3 = new Intent(this, Infomacion.class);
                startActivity(intent3);
                break;


        }


    }
}