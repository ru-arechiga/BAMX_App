package com.example.bamx_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class sobre_bamx1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sobre_bamx1)
    }

    fun cambiarActividad(view: View?){

        // el que cambia la actividad es el sistema operativo
        // nosotros le pedimos atentamente, no le ordenamos

        // creamos una solicitud que se llama intent
        // 2 maneras de abrir una actividad
        // 1 - con tipo explícito
        // 2 - con una acción
        val intent = Intent(this, sobre_BAMX2::class.java)

        startActivity(intent)
    }
}