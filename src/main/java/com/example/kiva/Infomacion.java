package com.example.kiva;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.text.util.Linkify;
import android.text.method.LinkMovementMethod;
import android.os.Bundle;

public class Infomacion extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infomacion);
        TextView txtWebsite1 = findViewById(R.id.txt_website1);
        TextView txtWebsite2 = findViewById(R.id.txt_website2);
        ImageButton btnAtras= findViewById(R.id.btnAtras);
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Linkify.addLinks(txtWebsite1, Linkify.WEB_URLS);
        Linkify.addLinks(txtWebsite2, Linkify.WEB_URLS);

        txtWebsite1.setMovementMethod(LinkMovementMethod.getInstance());
        txtWebsite2.setMovementMethod(LinkMovementMethod.getInstance());
    }

}