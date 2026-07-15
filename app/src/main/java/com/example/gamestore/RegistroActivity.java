package com.example.gamestore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    private EditText edtEmailRegistro;
    private EditText edtContrasenaRegistro;
    private Button btnCrearCuenta;
    private Button btnVolverLogin;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainRegistro), new androidx.core.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            }
        });

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        edtEmailRegistro = findViewById(R.id.edtEmailRegistro);
        edtContrasenaRegistro = findViewById(R.id.edtContrasenaRegistro);
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);
        btnVolverLogin = findViewById(R.id.btnVolverLogin);

        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearCuenta();
            }
        });

        btnVolverLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void crearCuenta() {
        String email = edtEmailRegistro.getText().toString().trim();
        String contrasena = edtContrasenaRegistro.getText().toString().trim();

        if (email.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, getString(R.string.mensaje_campos_vacios), Toast.LENGTH_SHORT).show();
            return;
        }

        if (contrasena.length() < 6) {
            Toast.makeText(this, getString(R.string.mensaje_contrasena_corta), Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() && auth.getCurrentUser() != null) {
                    guardarDatosUsuario(auth.getCurrentUser().getUid(), email);
                } else {
                    Toast.makeText(RegistroActivity.this, getString(R.string.error_registro), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void guardarDatosUsuario(String uid, String email) {
        Map<String, Object> datosUsuario = new HashMap<>();
        datosUsuario.put("email", email);
        datosUsuario.put("tipo", "cliente");

        db.collection("usuarios").document(uid).set(datosUsuario).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegistroActivity.this, getString(R.string.mensaje_registro_correcto), Toast.LENGTH_SHORT).show();
                    abrirCatalogo();
                } else {
                    Toast.makeText(RegistroActivity.this, getString(R.string.error_guardar_usuario), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void abrirCatalogo() {
        Intent intent = new Intent(RegistroActivity.this, CatalogoActivity.class);
        startActivity(intent);
        finish();
    }
}