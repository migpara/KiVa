package com.example.kiva;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class TipoReporte extends AppCompatActivity implements View.OnClickListener {

    TextView fila1, fila2, fila3, fila4, fila5, fila6, fila7, fila8;
    private Bundle extras;
    private String centro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_reporte);

        ImageButton btnAtras = findViewById(R.id.imgAtras);
        extras = getIntent().getExtras();
        centro = extras.getString("centro");

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        fila1 = findViewById(R.id.fila1);
        fila2 = findViewById(R.id.fila2);
        fila3 = findViewById(R.id.fila3);
        fila4 = findViewById(R.id.fila4);
        fila5 = findViewById(R.id.fila5);
        fila6 = findViewById(R.id.fila6);
        fila7 = findViewById(R.id.fila7);
        fila8 = findViewById(R.id.fila8);
        fila1.setOnClickListener(this);
        fila2.setOnClickListener(this);
        fila3.setOnClickListener(this);
        fila4.setOnClickListener(this);
        fila5.setOnClickListener(this);
        fila6.setOnClickListener(this);
        fila7.setOnClickListener(this);
        fila8.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fila1:
                Intent intent1 = new Intent(this, FormularioReporte.class);
                intent1.putExtra("tipo", fila1.getText().toString());
                intent1.putExtra("centro", centro);
                startActivity(intent1);
                break;
            case R.id.fila2:
                Intent intent2 = new Intent(this, FormularioReporte.class);
                intent2.putExtra("tipo", fila2.getText().toString());
                intent2.putExtra("centro", centro);
                startActivity(intent2);
                break;
            case R.id.fila3:
                Intent intent3 = new Intent(this, FormularioReporte.class);
                intent3.putExtra("tipo", fila3.getText().toString());
                intent3.putExtra("centro", centro);
                startActivity(intent3);
                break;
            case R.id.fila4:
                Intent intent4 = new Intent(this, FormularioReporte.class);
                intent4.putExtra("tipo", fila4.getText().toString());
                intent4.putExtra("centro", centro);
                startActivity(intent4);
                break;
            case R.id.fila5:
                Intent intent5 = new Intent(this, FormularioReporte.class);
                intent5.putExtra("tipo", fila5.getText().toString());
                intent5.putExtra("centro", centro);
                startActivity(intent5);
                break;
            case R.id.fila6:
                Intent intent6 = new Intent(this, FormularioReporte.class);
                intent6.putExtra("tipo", fila6.getText().toString());
                intent6.putExtra("centro", centro);
                startActivity(intent6);
                break;
            case R.id.fila7:
                Intent intent7 = new Intent(this, FormularioReporte.class);
                intent7.putExtra("tipo", fila7.getText().toString());
                intent7.putExtra("centro", centro);
                startActivity(intent7);
                break;
            case R.id.fila8:
                Intent intent8 = new Intent(this, FormularioReporte.class);
                intent8.putExtra("tipo", fila8.getText().toString());
                intent8.putExtra("centro", centro);
                startActivity(intent8);
                break;
        }
    }
}