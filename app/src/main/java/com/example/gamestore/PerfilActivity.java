package com.example.gamestore;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PerfilActivity extends AppCompatActivity {

    private EditText edtNombrePerfil;
    private EditText edtApellidoPerfil;
    private TextView txtEmailPerfil;
    private Button btnGuardarPerfil;
    private Button btnVolverCatalogoPerfil;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseUser usuarioActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainPerfil), new androidx.core.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            }
        });

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        usuarioActual = auth.getCurrentUser();

        edtNombrePerfil = findViewById(R.id.edtNombrePerfil);
        edtApellidoPerfil = findViewById(R.id.edtApellidoPerfil);
        txtEmailPerfil = findViewById(R.id.txtEmailPerfil);
        btnGuardarPerfil = findViewById(R.id.btnGuardarPerfil);
        btnVolverCatalogoPerfil = findViewById(R.id.btnVolverCatalogoPerfil);

        if (usuarioActual == null) {
            Toast.makeText(this, getString(R.string.error_usuario_no_logueado), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cargarDatosPerfil();

        btnGuardarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarCambiosPerfil();
            }
        });

        btnVolverCatalogoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void cargarDatosPerfil() {
        txtEmailPerfil.setText(getString(R.string.email) + ": " + usuarioActual.getEmail());

        db.collection("usuarios").document(usuarioActual.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String nombre = documentSnapshot.getString("nombre");
                        String apellido = documentSnapshot.getString("apellido");

                        if (nombre != null) {
                            edtNombrePerfil.setText(nombre);
                        }

                        if (apellido != null) {
                            edtApellidoPerfil.setText(apellido);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(PerfilActivity.this, getString(R.string.error_cargar_usuario), Toast.LENGTH_SHORT).show();
                });
    }

    private void guardarCambiosPerfil() {
        String nombre = edtNombrePerfil.getText().toString().trim();
        String apellido = edtApellidoPerfil.getText().toString().trim();

        if (nombre.isEmpty() || apellido.isEmpty()) {
            Toast.makeText(this, getString(R.string.mensaje_perfil_vacio), Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> datosUsuario = new HashMap<>();
        datosUsuario.put("nombre", nombre);
        datosUsuario.put("apellido", apellido);
        datosUsuario.put("email", usuarioActual.getEmail());
        datosUsuario.put("tipo", "cliente");

        db.collection("usuarios").document(usuarioActual.getUid()).set(datosUsuario)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(PerfilActivity.this, getString(R.string.perfil_actualizado), Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(PerfilActivity.this, getString(R.string.error_actualizar_perfil), Toast.LENGTH_SHORT).show();
                });
    }
}