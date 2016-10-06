package com.example.a1144097187.semana_nueve;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import datos.Bola;

public class MainActivity extends AppCompatActivity implements Observer {

    private int r = 0, g = 0, b = 0;

    private EditText tam;
    private EditText posX;
    private EditText posY;

    private SeekBar rojo;
    private SeekBar verde;
    private SeekBar azul;

    private ImageView cuadro;
    private Button enviar;

    private Comunicacion com;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tam = (EditText) findViewById(R.id.tam);
        posX = (EditText) findViewById(R.id.x);
        posY = (EditText) findViewById(R.id.y);

        rojo = (SeekBar) findViewById(R.id.red);
        verde = (SeekBar) findViewById(R.id.green);
        azul = (SeekBar) findViewById(R.id.blue);

        cuadro = (ImageView) findViewById(R.id.color);

        enviar = (Button) findViewById(R.id.envio);

        com.getInstance().addObserver(this);

        rojo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                r = progress;
                cuadro.setBackgroundColor(Color.argb(255, r, g, b));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        verde.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                g = progress;
                cuadro.setBackgroundColor(Color.argb(255, r, g, b));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        azul.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                b = progress;
                cuadro.setBackgroundColor(Color.argb(255, r, g, b));


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    public void enviar(View v) {

        String posXD = posX.getText().toString();
        String posYD = posY.getText().toString();
        String tamm = tam.getText().toString();


        int x = Integer.parseInt(posXD);
        int y = Integer.parseInt(posYD);
        int tam = Integer.parseInt(tamm);
        int rr = r;
        int gg = g;
        int bb = b;

        Bola bolas = new Bola(x, y, tam, rr, gg, bb);


        com.getInstance().sendMessage(bolas, com.getInstance().MULTI_GROUP_ADDRESS, com.getInstance().DEFAULT_PORT);

        System.out.println();

        Toast.makeText(getBaseContext(),posXD, Toast.LENGTH_SHORT).show();





    }

    @Override
    public void update(Observable observable, Object data) {

    }
}
