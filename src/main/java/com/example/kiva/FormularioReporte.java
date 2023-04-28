package com.example.kiva;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.util.UUID;

public class FormularioReporte extends AppCompatActivity {
    private TextView problema;
    private EditText editTextQueHaPasado, editTextQuienNecesitaAyuda, editTextDondeCuando, editTextQuienLoHaHecho;
    private Bundle extras;
    private String centro;
    private FirebaseFirestore firestore;
    private FirebaseStorage firebaseStorage;
    private Bitmap selectedImageBitmap;
    private static final int SELECT_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_reporte);
        ImageButton btnAtras = findViewById(R.id.btnAtras2);
        editTextQueHaPasado = findViewById(R.id.editTextQueHaPasado);
        editTextDondeCuando = findViewById(R.id.editTextDondeCuando);
        editTextQuienLoHaHecho = findViewById(R.id.editTextQuienLoHaHecho);
        editTextQuienNecesitaAyuda = findViewById(R.id.editTextQuienNecesitaAyuda);
        extras = getIntent().getExtras();
        centro = extras.getString("centro");
        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        problema = findViewById(R.id.txtProblema);
        Bundle extras = getIntent().getExtras();
        String s = extras.getString("tipo");
        problema.setText(s);

        Button buttonSaveReport = findViewById(R.id.button_submit_form);
        buttonSaveReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String queHaPasado = editTextQueHaPasado.getText().toString();
                String quienNecesitaAyuda = editTextQuienNecesitaAyuda.getText().toString();
                String dondeCuando = editTextDondeCuando.getText().toString();
                String quienLoHaHecho = editTextQuienLoHaHecho.getText().toString();
                String tipoProblema = problema.getText().toString();
                String colegio = centro;

                if (selectedImageBitmap != null && !queHaPasado.isEmpty() && !quienNecesitaAyuda.isEmpty() && !dondeCuando.isEmpty() && !quienLoHaHecho.isEmpty()) {
                    saveReport(selectedImageBitmap, queHaPasado, quienNecesitaAyuda, dondeCuando, quienLoHaHecho, tipoProblema, colegio);
                } else {
                    Toast.makeText(FormularioReporte.this, "Por favor, completa todos los campos y selecciona una imagen", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Verifica permisos en tiempo de ejecución
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        // Configura el botón para seleccionar la imagen
        Button buttonSelectImage = findViewById(R.id.button_select_image);
        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromGallery();
            }
        });
    }

    private void saveReport(Bitmap imageBitmap, String queHaPasado, String quienNecesitaAyuda, String dondeCuando, String quienLoHaHecho, String tipoProblema, String colegio) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("report_images/" + System.currentTimeMillis() + ".jpg");

        UploadTask uploadTask = imageRef.putBytes(imageData);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return imageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> report = new HashMap<>();
                    report.put("queHaPasado", queHaPasado);
                    report.put("quienNecesitaAyuda", quienNecesitaAyuda);
                    report.put("dondeCuando", dondeCuando);
                    report.put("quienLoHaHecho", quienLoHaHecho);
                    report.put("tipoProblema", tipoProblema);
                    report.put("imageUrl", downloadUri.toString());

                    db.collection("CentroEscolar").document(colegio).collection("Formularios")
                            .add(report)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(FormularioReporte.this, "Reporte guardado con éxito", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(FormularioReporte.this, "Error al guardar el reporte", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(FormularioReporte.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void selectImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), SELECT_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            try {
                // Mostrar la imagen en un ImageView
                ImageView imageView = findViewById(R.id.imageView);
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                imageView.setImageBitmap(selectedImageBitmap);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }


}

