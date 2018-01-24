package com.iesaugustobriga.mariosimonrubio.proyecto;

import android.app.Activity;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by mariosimonrubio on 13/5/17.
 */

public class Preguntas extends AsyncTask<String,Void,String>{
    private Activity padre;
    private MediaPlayer mp=new MediaPlayer();
    int puntos=0;
    Random aleatorios=new Random();
    public Preguntas(Activity padre){
        this.padre=padre;
    }
    @Override
    protected String doInBackground(String... parametros) {
        String cadena=parametros[0];
        URL url=null;
        String retorno="";
        int numeros=(int)(Math.random()*3)+1;
            switch (parametros[1]){
                case "1":
                    try{
                        url=new URL("http://mariosimorubio.hol.es/ServicioWebAndroid/obtener_Preguntas.php"+"?id="+numeros);
                        HttpURLConnection conexionURL=(HttpURLConnection) url.openConnection();
                        int respuesta=conexionURL.getResponseCode();
                        StringBuilder result=new StringBuilder();
                        if(respuesta==HttpURLConnection.HTTP_OK){
                            InputStream in=new BufferedInputStream(conexionURL.getInputStream());

                            BufferedReader lectura=new BufferedReader(new InputStreamReader(in));

                            String linea;
                            while((linea=lectura.readLine())!=null){
                                result.append(linea);

                            }
                            JSONObject respuestaJSON=new JSONObject(result.toString());

                            JSONArray preguntasJSON=respuestaJSON.getJSONArray("preguntas");
                            for(int i=0;i<preguntasJSON.length();i++){
                                retorno+=preguntasJSON.getJSONObject(i).getString("id")
                                        +"#"+preguntasJSON.getJSONObject(i).getString("pregunta")+
                                        "#"+preguntasJSON.getJSONObject(i).getString("id_resp")+";";
                            }
                        }
                        try{
                            url=new URL("http://mariosimorubio.hol.es/ServicioWebAndroid/obtener_Canciones.php"+"?id="+numeros);
                            conexionURL=(HttpURLConnection) url.openConnection();
                            respuesta=conexionURL.getResponseCode();
                            result=new StringBuilder();
                            if(respuesta==HttpURLConnection.HTTP_OK){
                                InputStream in=new BufferedInputStream(conexionURL.getInputStream());

                                BufferedReader lectura=new BufferedReader(new InputStreamReader(in));

                                String linea;
                                while((linea=lectura.readLine())!=null){
                                    result.append(linea);

                                }
                                JSONObject respuestaJSON=new JSONObject(result.toString());

                                JSONArray preguntasJSON=respuestaJSON.getJSONArray("url");

                                for(int i=0;i<preguntasJSON.length();i++){
                                    retorno+=preguntasJSON.getJSONObject(i).getString("id")
                                            +"#"+preguntasJSON.getJSONObject(i).getString("url")+";";
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        Log.w("Numeros",""+numeros);
                        url=new URL("http://mariosimorubio.hol.es/ServicioWebAndroid/obtener_Respuestas.php"+"?id="+numeros);
                        conexionURL=(HttpURLConnection) url.openConnection();
                        respuesta=conexionURL.getResponseCode();
                        result=new StringBuilder();
                        if(respuesta==HttpURLConnection.HTTP_OK){
                            InputStream in=new BufferedInputStream(conexionURL.getInputStream());

                            BufferedReader lectura=new BufferedReader(new InputStreamReader(in));

                            String linea;
                            while((linea=lectura.readLine())!=null){
                                result.append(linea);

                            }
                            JSONObject respuestaJSON=new JSONObject(result.toString());

                            JSONArray preguntasJSON=respuestaJSON.getJSONArray("respuestas");

                            for(int i=0;i<preguntasJSON.length();i++){
                                retorno+=preguntasJSON.getJSONObject(i).getString("id")
                                        +"#"+preguntasJSON.getJSONObject(i).getString("respuesta")+";";
                            }
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
            }

        return retorno;
    }

    @Override
    protected void onPostExecute(String s) {
            String[] preguntas=s.split(";");
            final TextView t=(TextView)padre.findViewById(R.id.pregunta);
            final String[] pregunta=preguntas[0].split("#");
            t.setText(pregunta[1]);
            final TextView puntosGanados=(TextView)padre.findViewById(R.id.lblPuntosGanados);
            final String[] cancion=preguntas[1].split("#");
            final Button reproducir=(Button)padre.findViewById(R.id.repro);
            reproducir.setEnabled(true);
            try{
                String[] respuestas=s.split(";");
                final LinearLayout Layout=(LinearLayout)padre.findViewById(R.id.vertical);
                for(int i=2;i<respuestas.length;i++){
                    final String[] respuesta=respuestas[i].split("#");
                    final Button b1=new Button(padre.getApplicationContext());
                    b1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    b1.setText(respuesta[1]);
                    b1.setTag(respuesta[0]);
                    b1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(pregunta[2].contentEquals(b1.getTag().toString())) {
                                puntos++;
                                Log.w("Puntos",String.valueOf(puntos));
                                reproducir.setEnabled(false);
                                b1.setEnabled(false);
                                b1.setBackgroundColor(Color.GREEN);
                                b1.setTextColor(Color.BLACK);
                                if(mp.isPlaying()) {
                                    mp.stop();
                                }
                                puntosGanados.setText(String.valueOf(puntos));
                            }else{
                                b1.setEnabled(false);
                                b1.setBackgroundColor(Color.RED);
                                b1.setTextColor(Color.BLACK);
                            }
                        }
                    });
                    Layout.addView(b1);
                    new CountDownTimer(10000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            if(reproducir.isEnabled()==false && mp.isPlaying()){
                                reproducir.setEnabled(true);
                                mp.stop();
                            }
                            Layout.removeView(b1);
                            t.setText("");
                        }
                    }.start();
                }
            }catch(Exception e){
                e.getMessage();
            }
            reproducir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mp.setDataSource(cancion[1]);
                        mp.prepare();
                        mp.start();
                        reproducir.setEnabled(false);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    public void parar(){
        mp.stop();
    }

}
