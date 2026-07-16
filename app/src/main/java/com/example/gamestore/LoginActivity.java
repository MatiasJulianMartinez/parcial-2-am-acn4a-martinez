package com.example.gamestore;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmailLogin;
    private EditText edtContrasenaLogin;
    private Button btnIniciarSesion;
    private Button btnIrRegistro;
    private ImageButton btnMostrarContrasenaLogin;
    private FirebaseAuth auth;
    private boolean contrasenaVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLogin), new androidx.core.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            }
        });

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            abrirCatalogo();
            return;
        }

        edtEmailLogin = findViewById(R.id.edtEmailLogin);
        edtContrasenaLogin = findViewById(R.id.edtContrasenaLogin);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnIrRegistro = findViewById(R.id.btnIrRegistro);
        btnMostrarContrasenaLogin = findViewById(R.id.btnMostrarContrasenaLogin);

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarSesion();
            }
        });

        btnIrRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

        btnMostrarContrasenaLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarVisibilidadContrasena();
            }
        });
    }

    private void iniciarSesion() {
        String email = edtEmailLogin.getText().toString().trim();
        String contrasena = edtContrasenaLogin.getText().toString().trim();

        if (email.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, getString(R.string.mensaje_campos_vacios), Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, getString(R.string.mensaje_login_correcto), Toast.LENGTH_SHORT).show();
                    abrirCatalogo();
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void cambiarVisibilidadContrasena() {
        contrasenaVisible = !contrasenaVisible;

        if (contrasenaVisible) {
            edtContrasenaLogin.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            btnMostrarContrasenaLogin.setImageResource(R.drawable.ic_visibility);
            btnMostrarContrasenaLogin.setContentDescription(getString(R.string.ocultar_contrasena));
        } else {
            edtContrasenaLogin.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            btnMostrarContrasenaLogin.setImageResource(R.drawable.ic_visibility_off);
            btnMostrarContrasenaLogin.setContentDescription(getString(R.string.mostrar_contrasena));
        }

        edtContrasenaLogin.setSelection(edtContrasenaLogin.getText().length());
    }

    private void abrirCatalogo() {
        Intent intent = new Intent(LoginActivity.this, CatalogoActivity.class);
        startActivity(intent);
        finish();
    }
}