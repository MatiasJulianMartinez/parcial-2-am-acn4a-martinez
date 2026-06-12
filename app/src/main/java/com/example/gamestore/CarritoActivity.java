package com.example.gamestore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CarritoActivity extends AppCompatActivity {

    private TextView txtResumenProducto;
    private TextView txtResumenCantidad;
    private TextView txtResumenTotal;
    private TextView txtEstadoCompra;
    private TextView txtHistorialVacio;
    private Button btnPagar;
    private Button btnVolverDetalle;
    private Button btnVolverCatalogo;
    private Button btnBorrarHistorial;
    private LinearLayout layoutHistorialCompras;

    private FirebaseFirestore db;
    private int numeroCompraActual = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carrito);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainCarrito), new androidx.core.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            }
        });

        db = FirebaseFirestore.getInstance();

        txtResumenProducto = findViewById(R.id.txtResumenProducto);
        txtResumenCantidad = findViewById(R.id.txtResumenCantidad);
        txtResumenTotal = findViewById(R.id.txtResumenTotal);
        txtEstadoCompra = findViewById(R.id.txtEstadoCompra);
        txtHistorialVacio = findViewById(R.id.txtHistorialVacio);
        btnPagar = findViewById(R.id.btnPagar);
        btnVolverDetalle = findViewById(R.id.btnVolverDetalle);
        btnVolverCatalogo = findViewById(R.id.btnVolverCatalogoCarrito);
        btnBorrarHistorial = findViewById(R.id.btnBorrarHistorial);
        layoutHistorialCompras = findViewById(R.id.layoutHistorialCompras);

        actualizarResumen();
        cargarHistorialCompras();

        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simularPago();
            }
        });

        btnVolverDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnVolverCatalogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CarritoActivity.this, CatalogoActivity.class);
                startActivity(intent);
            }
        });

        btnBorrarHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                borrarHistorialCompras();
            }
        });
    }

    private void actualizarResumen() {
        txtResumenProducto.setText(CarritoManager.getProductosTexto());
        txtResumenCantidad.setText(getString(R.string.cantidad_base) + CarritoManager.getCantidadProductos());
        txtResumenTotal.setText(getString(R.string.total_estimado_base) + CarritoManager.getTotal());

        if (CarritoManager.getCantidadProductos() == 0) {
            btnPagar.setEnabled(false);
            txtEstadoCompra.setText(getString(R.string.estado_carrito_vacio));
        } else {
            btnPagar.setEnabled(true);
            txtEstadoCompra.setText(getString(R.string.estado_pendiente));
        }
    }

    private void simularPago() {
        final String productos = CarritoManager.getProductosTexto();
        final int cantidad = CarritoManager.getCantidadProductos();
        final int total = CarritoManager.getTotal();

        if (cantidad == 0) {
            Toast.makeText(this, getString(R.string.estado_carrito_vacio), Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> compra = new HashMap<>();
        compra.put("numeroCompra", numeroCompraActual);
        compra.put("productos", productos);
        compra.put("cantidad", cantidad);
        compra.put("total", total);
        compra.put("estado", getString(R.string.estado_compra_realizada));

        db.collection("compras")
                .add(compra)
                .addOnSuccessListener(documentReference -> {
                    txtEstadoCompra.setText(getString(R.string.estado_compra_realizada));
                    btnPagar.setEnabled(false);
                    btnPagar.setText(getString(R.string.pago_realizado));

                    agregarCompraAlHistorial(
                            numeroCompraActual,
                            productos,
                            cantidad,
                            total,
                            getString(R.string.estado_compra_realizada)
                    );

                    numeroCompraActual++;

                    Toast.makeText(this, getString(R.string.mensaje_compra_guardada), Toast.LENGTH_LONG).show();

                    CarritoManager.vaciarCarrito();

                    txtResumenProducto.setText(getString(R.string.productos_agregados));
                    txtResumenCantidad.setText(getString(R.string.cantidad_base) + "0");
                    txtResumenTotal.setText(getString(R.string.total_estimado));
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, getString(R.string.error_guardar_compra), Toast.LENGTH_LONG).show()
                );
    }

    private void cargarHistorialCompras() {
        db.collection("compras")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    layoutHistorialCompras.removeAllViews();

                    if (queryDocumentSnapshots.isEmpty()) {
                        txtHistorialVacio.setVisibility(View.VISIBLE);
                        layoutHistorialCompras.addView(txtHistorialVacio);
                        numeroCompraActual = 1;
                    } else {
                        txtHistorialVacio.setVisibility(View.GONE);

                        int numero = 1;
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            String productos = document.getString("productos");
                            Long cantidad = document.getLong("cantidad");
                            Long total = document.getLong("total");
                            String estado = document.getString("estado");

                            agregarCompraAlHistorial(
                                    numero,
                                    productos != null ? productos : "",
                                    cantidad != null ? cantidad.intValue() : 0,
                                    total != null ? total.intValue() : 0,
                                    estado != null ? estado : ""
                            );
                            numero++;
                        }
                        numeroCompraActual = numero;
                    }
                })
                .addOnFailureListener(e -> {
                    txtHistorialVacio.setText(getString(R.string.error_cargar_historial));
                    layoutHistorialCompras.removeAllViews();
                    layoutHistorialCompras.addView(txtHistorialVacio);
                });
    }

    private void borrarHistorialCompras() {
        db.collection("compras")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        db.collection("compras").document(document.getId()).delete();
                    }

                    layoutHistorialCompras.removeAllViews();
                    txtHistorialVacio.setVisibility(View.VISIBLE);
                    txtHistorialVacio.setText(getString(R.string.historial_vacio));
                    layoutHistorialCompras.addView(txtHistorialVacio);
                    numeroCompraActual = 1;

                    Toast.makeText(this, getString(R.string.historial_borrado), Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, getString(R.string.error_borrar_historial), Toast.LENGTH_LONG).show()
                );
    }

    private void agregarCompraAlHistorial(int numeroCompra, String productos, int cantidad, int total, String estado) {
        txtHistorialVacio.setVisibility(View.GONE);

        TextView txtCompra = new TextView(this);
        txtCompra.setText(
                getString(R.string.compra_numero_base) + numeroCompra +
                        "\n" + getString(R.string.resumen_producto) + " " + productos +
                        "\n" + getString(R.string.cantidad_base) + cantidad +
                        "\n" + getString(R.string.total_estimado_base) + total +
                        "\n" + estado
        );
        txtCompra.setTextSize(16);
        txtCompra.setTextColor(getResources().getColor(android.R.color.white));
        txtCompra.setBackgroundColor(getResources().getColor(R.color.fondo_contador));
        txtCompra.setPadding(20, 20, 20, 20);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 20);
        txtCompra.setLayoutParams(params);

        layoutHistorialCompras.addView(txtCompra);
    }
}