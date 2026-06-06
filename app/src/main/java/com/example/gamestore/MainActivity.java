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
    private TextView txtNombreProducto;
    private TextView txtPrecio;
    private TextView txtDescripcion;
    private TextView txtContadorCarrito;
    private TextView txtCarritoVacio;
    private TextView txtTotalEstimado;
    private ImageView imgProducto;
    private LinearLayout layoutCarrito;

    private int cantidadProductos = 0;
    private int precioProducto = 0;
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
        txtNombreProducto = findViewById(R.id.txtNombreProducto);
        txtPrecio = findViewById(R.id.txtPrecio);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtContadorCarrito = findViewById(R.id.txtContadorCarrito);
        txtCarritoVacio = findViewById(R.id.txtCarritoVacio);
        txtTotalEstimado = findViewById(R.id.txtTotalEstimado);
        imgProducto = findViewById(R.id.imgProducto);
        layoutCarrito = findViewById(R.id.layoutCarrito);

        recibirDatosDelCatalogo();

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

            precioProducto = convertirPrecioANumero(precioTexto);
        } else {
            nombreProductoActual = txtNombreProducto.getText().toString();
            precioProducto = convertirPrecioANumero(txtPrecio.getText().toString());
        }
    }

    private int convertirPrecioANumero(String precioTexto) {
        String precioLimpio = precioTexto.replace("$", "").replace(".", "").trim();
        return Integer.parseInt(precioLimpio);
    }

    private void agregarProductoAlCarrito() {
        if (txtCarritoVacio.getParent() != null) {
            layoutCarrito.removeView(txtCarritoVacio);
        }

        TextView nuevoProducto = new TextView(this);
        nuevoProducto.setText("• " + nombreProductoActual + " - " + txtPrecio.getText().toString());
        nuevoProducto.setTextSize(16);
        nuevoProducto.setTextColor(getResources().getColor(android.R.color.white));
        nuevoProducto.setPadding(0, 0, 0, 20);

        layoutCarrito.addView(nuevoProducto);

        cantidadProductos++;
        txtContadorCarrito.setText("Productos en carrito: " + cantidadProductos);

        int total = cantidadProductos * precioProducto;
        txtTotalEstimado.setText("Total estimado: $" + total);

        Toast.makeText(this, "Producto agregado al carrito", Toast.LENGTH_SHORT).show();
    }

    private void vaciarCarrito() {
        layoutCarrito.removeAllViews();
        layoutCarrito.addView(txtCarritoVacio);

        cantidadProductos = 0;
        txtContadorCarrito.setText("Productos en carrito: 0");
        txtTotalEstimado.setText("Total estimado: $0");

        Toast.makeText(this, "Carrito vaciado", Toast.LENGTH_SHORT).show();
    }
}