package com.example.kiva;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Blog extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private EditText mEditText;
    private Button mButton;
    private Bundle extras;
    private String centro;
    private String codigo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        extras = getIntent().getExtras();
        centro = extras.getString("centro");
        codigo= extras.getString("usuario");

        // Obtener referencias a los elementos de la vista
        mRecyclerView = findViewById(R.id.recycler_view);
        mEditText = findViewById(R.id.edit_text);
        mButton = findViewById(R.id.button_send);
        ImageView buttonBack = findViewById(R.id.button_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        // Configurar el RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        // Crear un adaptador vacío para el RecyclerView
        BlogAdapter adapter = new BlogAdapter(new ArrayList<Message>(),codigo);
        mRecyclerView.setAdapter(adapter);

        // Obtener una referencia a la colección de mensajes
        CollectionReference messagesRef = FirebaseFirestore.getInstance()
                .collection("CentroEscolar")
                .document(centro)
                .collection("mensajes");

// Agregar un listener para obtener los mensajes existentes en la base de datos
       messagesRef.orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
           @Override
           public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error);
                    return;
                }

                // Crear una lista de mensajes a partir de los documentos obtenidos de la base de datos
                ArrayList<Message> messages = new ArrayList<>();
                for (QueryDocumentSnapshot document : querySnapshot) {
                    String content = document.getString("content");
                    Date timestamp = document.getDate("timestamp");
                    String codigoUser=document.getString("codigo");
                    Message message = new Message(content, timestamp,codigoUser);
                    messages.add(message);
                    Log.i("Comprobar",message.toString());

                }

                // Crear un adaptador con los mensajes obtenidos y configurar el RecyclerView
                BlogAdapter adapter = new BlogAdapter(messages,codigo);
                mRecyclerView.setAdapter(adapter);
                Log.i("Comprobar","se ha creado el adaptador");
               layoutManager.scrollToPosition(adapter.getItemCount() - 1);
            }
        });




        // Configurar el botón para agregar nuevos mensajes
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el contenido del mensaje del EditText
                String messageText = mEditText.getText().toString().trim();

                // Crear un nuevo objeto Message con el contenido del mensaje y la fecha actual
                Message message = new Message(messageText, new Date(),codigo.trim());

                // Agregar el nuevo mensaje al RecyclerView y a la base de datos
                adapter.addMessage(message);
                addMessageToFirestore(message);

                // Borrar el contenido del EditText
                mEditText.setText("");
                // Desplazar el RecyclerView al último mensaje
                layoutManager.scrollToPosition(adapter.getItemCount() - 1);
            }
        });
    }

    private void addMessageToFirestore(Message message) {
        // Crear una instancia de FirebaseFirestore y obtener una referencia a la colección "messages"
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference messagesRef = db.collection("CentroEscolar").document(centro)
                .collection("mensajes");

        // Crear un nuevo documento con los datos del mensaje
        Map<String, Object> data = new HashMap<>();
        data.put("content", message.getContent());
        data.put("timestamp", message.getTimestamp());
        data.put("codigo",message.getUserId());

        // Agregar el nuevo documento a la colección "messages"
        messagesRef.add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Document added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}
