package com.example.proyecto_gio;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    List<RadioGroup> radioGroups = new ArrayList<>();
    List<Respuesta> listRes = new ArrayList<>();
    Map<String, ImageView> imageMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InterfaceAPI apiService = Retrofit.getApiService();

        LinearLayout layout1 = findViewById(R.id.layout1);

        Call<List<Pregunta>> call = apiService.getPreguntas();

        call.enqueue(new Callback<List<Pregunta>>() {
            @Override
            public void onResponse(Call<List<Pregunta>> call, Response<List<Pregunta>> response) {
                if(response.isSuccessful()){
                    Log.d("Respuesta", "GET CORRECTO");
                    List<Pregunta> preguntas = response.body();
                    if(preguntas != null){
                        int i = 0;
                        for (Pregunta pregunta : preguntas) {
                            Log.d("DATA PREGUNTA", pregunta.getPregunta());

                            int id = i +1000;

                            TextView t = new TextView(MainActivity.this);
                            t.setTextAppearance(android.R.style.TextAppearance_Large);
                            t.setText(pregunta.getPregunta());
                            t.setBackgroundColor(Color.MAGENTA);
                            t.setGravity(Gravity.CENTER);
                            t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                            // Configura el TextView para que esté centrado en su contenedor
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    150//LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            layoutParams.gravity = Gravity.CENTER_VERTICAL;

                            // Aplica las layoutParams al TextView
                            t.setLayoutParams(layoutParams);
                            layout1.addView(t);


                            // Crear un RadioGroup
                            RadioGroup radioGroup = new RadioGroup(MainActivity.this);
                            //radioGroup.setId(pregunta.getId()+10);
                            radioGroups.add(radioGroup);
                            List<String> respostes = pregunta.getRespuestas();

                            // Configura el GridLayout con 2 columnas
                            GridLayout gridLayout = new GridLayout(MainActivity.this);
                            gridLayout.setLayoutParams(new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            ));
                            gridLayout.setColumnCount(2);


                            for (String r : respostes) {
                                ImageView imgButton = new ImageView(MainActivity.this);
                                RadioButton button = new RadioButton(MainActivity.this);
                                imageMap.put(r, imgButton);
                                button.setTag(imgButton);
                                // Establecer el ancho y alto fijo para todos los ImageButton
                                int buttonWidth = 500;  // Ancho deseado en píxeles
                                int buttonHeight = 500; // Alto deseado en píxeles
                                imgButton.setLayoutParams(new ViewGroup.LayoutParams(buttonWidth, buttonHeight));

                                // Cargar la imagen usando Picasso
                                Picasso.get().load(r).resize(buttonWidth, buttonHeight).centerCrop().into(imgButton);

                                radioGroup.addView(button);
                                radioGroup.addView(imgButton);

                            }
                            gridLayout.addView(radioGroup);
                            layout1.addView(gridLayout);

                        }
                    }

                    Button enviar = new Button(MainActivity.this);
                    enviar.setText("Enviar");
                    layout1.addView(enviar);
                    enviar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            boolean respondido = true;
                            for (RadioGroup radioGroup : radioGroups) {
                                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

                                try {
                                    RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                                    if (selectedRadioButtonId == -1) {
                                        respondido = false;
                                    } else {
                                        Log.d("SELEC", "ENCONTADO");

                                    }
                                } catch (Exception e) {
                                    Log.d("error", "ERROR NO ENCONTRADO");
                                }
                            }
                            if(respondido){
                                int i = 0;
                                for (RadioGroup radioGroup : radioGroups) {
                                    int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

                                    try {
                                        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                                        if (selectedRadioButtonId != -1) {
                                            String url = "";
                                            //BUSCAR LA URL DE LA RESPUESTA SELECCIONADA
                                            for (Map.Entry<String, ImageView> entry:imageMap.entrySet()) {
                                                if(entry.getValue() == selectedRadioButton.getTag()){
                                                    url = entry.getKey();
                                                }
                                            }
                                            //RECOGO TODAS LAS RESPUESTAS DE LA PREGUNTA
                                            List<String> respuestas = preguntas.get(i).getRespuestas();
                                            int indiceRespuestaAPI = 0;
                                            int aux = 0;
                                            //BUSCO EL INDICE DE LA RESPUESTA SELECCIONADA
                                            for (String r: respuestas) {
                                                if(r==url){
                                                    indiceRespuestaAPI = aux;
                                                }
                                                aux++;
                                            }
                                            //VERIFICO SI ES CORRECTO CON EL INDICE DE LA RESPUESTA CORRECTA
                                            boolean correcto = false;
                                            if(indiceRespuestaAPI == preguntas.get(i).getIndiceCorrecto()){
                                                correcto = true;
                                            }
                                            Respuesta respuesta = new Respuesta(i, correcto);
                                            listRes.add(respuesta);
                                        } else {
                                            Log.d("SELEC", "NO ENCONTRADO");

                                        }
                                    } catch (Exception e) {
                                        Log.d("error", "ERROR NO ENCONTRADO");
                                    }
                                i++;
                                }

                                for (Respuesta r:listRes) {
                                    Log.d("dato: " , r.getId() + " " + r.isCorrecto());
                                }
                                InterfaceAPI apiService = Retrofit.getApiService();
                                Call<List<Respuesta>> callPost = apiService.createRespuestas(listRes);
                                callPost.enqueue(new Callback<List<Respuesta>>() {
                                    @Override
                                    public void onResponse(Call<List<Respuesta>> call, Response<List<Respuesta>> response) {
                                        if(response.isSuccessful()){
                                            Log.d("RESULTADO", "HECHO EL ESCRITO");
                                            listRes.clear();
                                        } else {
                                            Log.d("RESULTADO", "NO SUCCESSFUL");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<Respuesta>> call, Throwable t) {
                                        Log.d("enviado", "fallo: " + t);
                                    }
                                });
                            }else{
                                CharSequence text = "Responde todas las preguntas!";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(MainActivity.this, text, duration);
                                toast.show();
                            }
                        }
                    });
                }else{
                    Log.d("Respuesta", "GET INCORRECTO");
                }
            }

            @Override
            public void onFailure(Call<List<Pregunta>> call, Throwable t) {
                Log.d("conexio", "error");
            }
        });
    }
}