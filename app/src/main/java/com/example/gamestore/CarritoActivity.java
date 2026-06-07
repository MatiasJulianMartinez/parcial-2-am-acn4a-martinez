package com.example.gamestore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CarritoActivity extends AppCompatActivity {

    private TextView txtResumenProducto;
    private TextView txtResumenCantidad;
    private TextView txtResumenTotal;
    private TextView txtEstadoCompra;
    private Button btnPagar;
    private Button btnVolverDetalle;
    private Button btnVolverCatalogo;

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

        txtResumenProducto = findViewById(R.id.txtResumenProducto);
        txtResumenCantidad = findViewById(R.id.txtResumenCantidad);
        txtResumenTotal = findViewById(R.id.txtResumenTotal);
        txtEstadoCompra = findViewById(R.id.txtEstadoCompra);
        btnPagar = findViewById(R.id.btnPagar);
        btnVolverDetalle = findViewById(R.id.btnVolverDetalle);
        btnVolverCatalogo = findViewById(R.id.btnVolverCatalogoCarrito);

        actualizarResumen();

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
        txtEstadoCompra.setText(getString(R.string.estado_compra_realizada));
        btnPagar.setEnabled(false);
        btnPagar.setText(getString(R.string.pago_realizado));

        Toast.makeText(this, getString(R.string.mensaje_pago_realizado), Toast.LENGTH_SHORT).show();
    }
}