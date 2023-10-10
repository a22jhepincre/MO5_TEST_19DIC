package com.example.proyecto_gio;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface InterfaceAPI {
    @GET("http://192.168.206.249:3000/getPreguntas/")
    Call<List<Pregunta>> getPreguntas();

    @POST("http://192.168.206.249:3000/almacenar/")
    Call<List<Respuesta>>  createRespuestas(@Body List<Respuesta> respuestas);
}
