package com.iesaugustobriga.mariosimonrubio.proyecto;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import javax.sql.DataSource;

public class GameActivity extends AppCompatActivity {
    Context c=this;
    TextView lblPuntosGanados,lbltiempo;
    int tiempo=10;
    Preguntas miHilo = new Preguntas(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        lblPuntosGanados=(TextView)findViewById(R.id.lblPuntosGanados);
        lbltiempo=(TextView)findViewById(R.id.Tiempo);
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Operaciones http
            Toast info = Toast.makeText(getApplicationContext(),"Sí hay internet",Toast.LENGTH_LONG);
            info.show();
            //Recuperamos los nombres de los alumnos llamanado al servicio web
            final Handler handler = new Handler();
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            try {
                                //Ejecuta tu AsyncTask!
                                new Preguntas(GameActivity.this).execute("consulta","1");
                            } catch (Exception e) {
                                Log.e("error", e.getMessage());
                            }
                        }
                    });
                }
            };

            timer.schedule(task, 0, 10000);
        }else {
            Toast info = Toast.makeText(getApplicationContext(),"No hay internet",Toast.LENGTH_LONG);
            info.show();
        }
    }

    public void onBackPressed(){
        AlertDialog.Builder salir=new AlertDialog.Builder(c);
        salir.setTitle("Salir");
        salir.setMessage("¿Está seguro de que desea finalizar el juego con su puntuación actual "+lblPuntosGanados.getText()+" ?");
        salir.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent salir=new Intent(c,MainActivity.class);
                salir.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(salir);
                miHilo.parar();
                finish();
            }
        });
        salir.setNegativeButton("No",null);
        salir.create();
        salir.show();
    }
}
