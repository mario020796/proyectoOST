package com.iesaugustobriga.mariosimonrubio.proyecto;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button jugar,configuracion,ayuda,salir;
    Context c=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jugar=(Button)findViewById(R.id.btnJugar);
        configuracion=(Button)findViewById(R.id.btnConfiguracion);
        ayuda=(Button)findViewById(R.id.btnAyuda);
        salir=(Button)findViewById(R.id.btnSalir);



        //Métodos de los botones
        jugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jugar=new Intent(getApplicationContext(),GameActivity.class);
                startActivity(jugar);
            }
        });
        configuracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent config=new Intent(getApplicationContext(),ConfigActivity.class);
                startActivity(config);
            }
        });
        ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ayuda=new Intent(getApplicationContext(),AyudaActivity.class);
                startActivity(ayuda);
            }
        });
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder salir=new AlertDialog.Builder(c);
                salir.setTitle("Salir");
                salir.setMessage("¿ Está seguro de que desea salir de la aplicación?");
                salir.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                salir.setNegativeButton("No",null);
                salir.create();
                salir.show();
            }
        });
    }



}
