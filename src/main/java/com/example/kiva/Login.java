package com.example.kiva;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private Button btnMenu;
    private TextView centro, codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnMenu = findViewById(R.id.btnLogin);
        btnMenu.setOnClickListener(this);
        centro = findViewById(R.id.Centro);
        codigo = findViewById(R.id.Contraseña);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("CentroEscolar").document(centro.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                if (codigo.getText().toString().startsWith("P")) {
                                    String valor = document.getString("ClaveProfesor");
                                    if (valor.equals(codigo.getText().toString())) {
                                        Intent intent = new Intent(Login.this, Menu.class);
                                        intent.putExtra("centro", centro.getText().toString());
                                        startActivity(intent);
                                    } else {
                                        Toast toast = Toast.makeText(Login.this, "El codigo es incorrecto", Toast.LENGTH_LONG);
                                        toast.show();
                                        codigo.setText("");
                                    }

                                } else if (codigo.getText().toString().startsWith("a")) {
                                    db.collection("CentroEscolar").document(centro.getText().toString()).collection("alumnos").document(codigo.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    Intent intent = new Intent(Login.this, Menu.class);
                                                    intent.putExtra("centro", centro.getText().toString());
                                                    intent.putExtra("usuario", codigo.getText().toString());
                                                    startActivity(intent);
                                                } else {
                                                    Toast toast = Toast.makeText(Login.this, "El codigo es incorrecto", Toast.LENGTH_LONG);
                                                    toast.show();
                                                    codigo.setText("");
                                                }

                                            }


                                        }
                                    });

                                }

                            } else {
                                Toast toast = Toast.makeText(Login.this, "El centro no existe", Toast.LENGTH_LONG);
                                toast.show();

                            }
                        } else {
                            // Se produjo un error al leer el documento
                        }
                    }
                });


        }


    }
}