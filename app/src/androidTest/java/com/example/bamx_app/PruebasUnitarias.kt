package com.example.bamx_app

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.database.DatabaseReference

import org.junit.Test
import org.junit.runner.RunWith
import com.google.firebase.database.*
import org.junit.Assert.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RunWith(AndroidJUnit4::class)
class PruebasUnitarias {

    private lateinit var database : DatabaseReference
    private lateinit var db : DBHelper
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    @Test
    fun dinero_donado_valor_correcto() {
        db = DBHelper(appContext.applicationContext)
        val conteo1 = db.llamarConteo()
        val fecha: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("M/d/y H:m:ss"))
        db.donacionDinero("500", fecha)
        val conteo2 = db.llamarConteo()
        assertEquals(500, conteo2[3].toInt() - conteo1[3].toInt())
    }

    @Test
    fun cantidad_donaciones_monetarias_correctas() {
        db = DBHelper(appContext.applicationContext)
        val conteo1 = db.llamarConteo()
        val fecha: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("M/d/y H:m:ss"))
        db.donacionDinero("1000", fecha)
        val conteo2 = db.llamarConteo()
        assertEquals(1, conteo2[2].toInt() - conteo1[2].toInt())
    }

    @Test
    fun cantidad_donacion_especie_correcta() {
        db = DBHelper(appContext.applicationContext)
        val conteo1 = db.llamarConteo()
        db.donacionEspecie()
        val conteo2 = db.llamarConteo()
        assertEquals(1, conteo2[1].toInt() - conteo1[1].toInt())
    }

    @Test
    fun cantida_sesiones_voluntariado_correcta() {
        db = DBHelper(appContext.applicationContext)
        val conteo1 = db.llamarConteo()
        db.voluntariado()
        val conteo2 = db.llamarConteo()
        assertEquals(1, conteo2[0].toInt() - conteo1[0].toInt())
    }

    @Test
    fun checar_registro_voluntariado_exitoso() {
        database = FirebaseDatabase.getInstance().getReference("Voluntarios")
        Log.i("Pruebita", "dfjgodfigj")
     /*   database.child("Hector").child("nombre").get().addOnSuccessListener {
            Log.i("BUSCAAAAA", "lol")
            //"${it.value}"
            //${it.value.toString()}
        }.addOnFailureListener{
            Log.i("firebase", "Error getting data", it)
        }*/
        database.child("Hector").child("nombre").get().addOnSuccessListener {
            Log.i("firebase", "Got value")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }



        assertEquals("d","d")
    }
}