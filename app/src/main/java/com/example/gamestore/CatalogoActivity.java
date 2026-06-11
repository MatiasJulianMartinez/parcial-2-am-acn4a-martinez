package com.example.gamestore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CatalogoActivity extends AppCompatActivity {

    private Button btnVerProducto1;
    private Button btnVerProducto2;
    private Button btnVerProducto3;
    private Button btnVerProducto4;
    private ImageView imgProducto4;

    private ImageView imgProductoOnline;
    private TextView txtProductoOnlineNombre;
    private TextView txtProductoOnlinePrecio;
    private TextView txtProductoOnlineDescripcion;

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
        btnVerProducto4 = findViewById(R.id.btnVerProducto4);
        imgProducto4 = findViewById(R.id.imgProducto4);

        imgProductoOnline = findViewById(R.id.imgProductoOnline);
        txtProductoOnlineNombre = findViewById(R.id.txtProductoOnlineNombre);
        txtProductoOnlinePrecio = findViewById(R.id.txtProductoOnlinePrecio);
        txtProductoOnlineDescripcion = findViewById(R.id.txtProductoOnlineDescripcion);

        Glide.with(this)
                .load("https://redragon.es/content/uploads/2023/10/harrow-pro-660x520-1.png")
                .into(imgProducto4);

        cargarProductoOnline();

        btnVerProducto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDetalleLocal(
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
                abrirDetalleLocal(
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
                abrirDetalleLocal(
                        getString(R.string.producto_teclado_nombre),
                        getString(R.string.producto_teclado_precio),
                        getString(R.string.producto_teclado_descripcion),
                        R.drawable.teclado_redragon_kumara
                );
            }
        });

        btnVerProducto4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDetalleUrl(
                        getString(R.string.producto_harrow_nombre),
                        getString(R.string.producto_harrow_precio),
                        getString(R.string.producto_harrow_descripcion),
                        "https://redragon.es/content/uploads/2023/10/harrow-pro-660x520-1.png"
                );
            }
        });
    }

    private void cargarProductoOnline() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://mocki.io/v1/aa5cef7f-92d0-4459-a58f-a684159c1d34")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtProductoOnlineNombre.setText(getString(R.string.error_producto_online));
                        txtProductoOnlinePrecio.setText("");
                        txtProductoOnlineDescripcion.setText("");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtProductoOnlineNombre.setText(getString(R.string.error_producto_online));
                            txtProductoOnlinePrecio.setText("");
                            txtProductoOnlineDescripcion.setText("");
                        }
                    });
                    return;
                }

                String json = response.body().string();
                Gson gson = new Gson();
                ProductoOnline productoOnline = gson.fromJson(json, ProductoOnline.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (productoOnline != null) {
                            txtProductoOnlineNombre.setText(productoOnline.nombre);
                            txtProductoOnlinePrecio.setText(productoOnline.precio);
                            txtProductoOnlineDescripcion.setText(productoOnline.descripcion);

                            if (productoOnline.imagenUrl != null && !productoOnline.imagenUrl.isEmpty()) {
                                Glide.with(CatalogoActivity.this)
                                        .load(productoOnline.imagenUrl)
                                        .into(imgProductoOnline);
                            }
                        } else {
                            txtProductoOnlineNombre.setText(getString(R.string.error_producto_online));
                            txtProductoOnlinePrecio.setText("");
                            txtProductoOnlineDescripcion.setText("");
                        }
                    }
                });
            }
        });
    }

    private void abrirDetalleLocal(String nombre, String precio, String descripcion, int imagen) {
        Intent intent = new Intent(CatalogoActivity.this, MainActivity.class);
        intent.putExtra("nombre", nombre);
        intent.putExtra("precio", precio);
        intent.putExtra("descripcion", descripcion);
        intent.putExtra("imagen", imagen);
        startActivity(intent);
    }

    private void abrirDetalleUrl(String nombre, String precio, String descripcion, String imagenUrl) {
        Intent intent = new Intent(CatalogoActivity.this, MainActivity.class);
        intent.putExtra("nombre", nombre);
        intent.putExtra("precio", precio);
        intent.putExtra("descripcion", descripcion);
        intent.putExtra("imagenUrl", imagenUrl);
        startActivity(intent);
    }

    private static class ProductoOnline {
        String nombre;
        String precio;
        String descripcion;
        String imagenUrl;
    }
}