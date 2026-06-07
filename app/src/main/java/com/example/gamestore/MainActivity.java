package com.example.gamestore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button btnAgregarCarrito;
    private Button btnVaciarCarrito;
    private Button btnVolverCatalogo;
    private Button btnIrAlCarrito;
    private TextView txtNombreProducto;
    private TextView txtPrecio;
    private TextView txtDescripcion;
    private TextView txtContadorCarrito;
    private TextView txtCarritoVacio;
    private TextView txtTotalEstimado;
    private ImageView imgProducto;
    private LinearLayout layoutCarrito;

    private String nombreProductoActual = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), new androidx.core.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            }
        });

        btnAgregarCarrito = findViewById(R.id.btnAgregarCarrito);
        btnVaciarCarrito = findViewById(R.id.btnVaciarCarrito);
        btnVolverCatalogo = findViewById(R.id.btnVolverCatalogo);
        btnIrAlCarrito = findViewById(R.id.btnIrAlCarrito);
        txtNombreProducto = findViewById(R.id.txtNombreProducto);
        txtPrecio = findViewById(R.id.txtPrecio);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtContadorCarrito = findViewById(R.id.txtContadorCarrito);
        txtCarritoVacio = findViewById(R.id.txtCarritoVacio);
        txtTotalEstimado = findViewById(R.id.txtTotalEstimado);
        imgProducto = findViewById(R.id.imgProducto);
        layoutCarrito = findViewById(R.id.layoutCarrito);

        recibirDatosDelCatalogo();
        actualizarResumenCarrito();

        btnAgregarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarProductoAlCarrito();
            }
        });

        btnVaciarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaciarCarrito();
            }
        });

        btnVolverCatalogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CatalogoActivity.class);
                startActivity(intent);
            }
        });

        btnIrAlCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CarritoActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarResumenCarrito();
    }

    private void recibirDatosDelCatalogo() {
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("nombre")) {
            nombreProductoActual = intent.getStringExtra("nombre");
            String precioTexto = intent.getStringExtra("precio");
            String descripcion = intent.getStringExtra("descripcion");
            int imagen = intent.getIntExtra("imagen", R.drawable.auricular_redragon);

            txtNombreProducto.setText(nombreProductoActual);
            txtPrecio.setText(precioTexto);
            txtDescripcion.setText(descripcion);
            imgProducto.setImageResource(imagen);
        } else {
            nombreProductoActual = txtNombreProducto.getText().toString();
        }
    }

    private void agregarProductoAlCarrito() {
        CarritoManager.agregarProducto(nombreProductoActual, txtPrecio.getText().toString());
        actualizarResumenCarrito();

        Toast.makeText(this, getString(R.string.mensaje_producto_agregado), Toast.LENGTH_SHORT).show();
    }

    private void vaciarCarrito() {
        CarritoManager.vaciarCarrito();
        actualizarResumenCarrito();

        Toast.makeText(this, getString(R.string.mensaje_carrito_vaciado), Toast.LENGTH_SHORT).show();
    }

    private void actualizarResumenCarrito() {
        layoutCarrito.removeAllViews();

        if (CarritoManager.getCantidadProductos() == 0) {
            layoutCarrito.addView(txtCarritoVacio);
        } else {
            TextView resumenProductos = new TextView(this);
            resumenProductos.setText(CarritoManager.getProductosTexto());
            resumenProductos.setTextSize(16);
            resumenProductos.setTextColor(getResources().getColor(android.R.color.white));
            resumenProductos.setPadding(0, 0, 0, 20);
            layoutCarrito.addView(resumenProductos);
        }

        txtContadorCarrito.setText(getString(R.string.productos_en_carrito_base) + CarritoManager.getCantidadProductos());
        txtTotalEstimado.setText(getString(R.string.total_estimado_base) + CarritoManager.getTotal());
    }
}