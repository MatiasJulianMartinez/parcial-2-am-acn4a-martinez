package com.example.gamestore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CatalogoActivity extends AppCompatActivity {

    private Button btnVerProducto1;
    private Button btnVerProducto2;
    private Button btnVerProducto3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_catalogo);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainCatalogo), new androidx.core.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            }
        });

        btnVerProducto1 = findViewById(R.id.btnVerProducto1);
        btnVerProducto2 = findViewById(R.id.btnVerProducto2);
        btnVerProducto3 = findViewById(R.id.btnVerProducto3);

        btnVerProducto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDetalle(
                        getString(R.string.producto_auricular_nombre),
                        getString(R.string.producto_auricular_precio),
                        getString(R.string.producto_auricular_descripcion),
                        R.drawable.auricular_redragon
                );
            }
        });

        btnVerProducto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDetalle(
                        getString(R.string.producto_mouse_nombre),
                        getString(R.string.producto_mouse_precio),
                        getString(R.string.producto_mouse_descripcion),
                        R.drawable.mouse_logitech_g305
                );
            }
        });

        btnVerProducto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDetalle(
                        getString(R.string.producto_teclado_nombre),
                        getString(R.string.producto_teclado_precio),
                        getString(R.string.producto_teclado_descripcion),
                        R.drawable.teclado_redragon_kumara
                );
            }
        });
    }

    private void abrirDetalle(String nombre, String precio, String descripcion, int imagen) {
        Intent intent = new Intent(CatalogoActivity.this, MainActivity.class);
        intent.putExtra("nombre", nombre);
        intent.putExtra("precio", precio);
        intent.putExtra("descripcion", descripcion);
        intent.putExtra("imagen", imagen);
        startActivity(intent);
    }
}