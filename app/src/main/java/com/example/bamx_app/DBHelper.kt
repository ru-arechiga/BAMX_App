package com.example.bamx_app

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.CheckBox
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class DBHelper(context: Context?): SQLiteOpenHelper(context, DB_FILE, null, 1){
    companion object {
        private const val DB_FILE = "bamx.db"
        private const val TABLE_USER = "usuario"
            private const val COLUMN_NAMES = "nombres"
            private const val COLUMN_LNAMES = "apellidos"
            private const val COLUMN_EMAIL = "email"
            private const val COLUMN_CELLPHONE = "telefono"
            private const val COLUMN_ADDRESS = "direccion"
        private const val TABLE_DONATIONS = "donaciones"
            private const val COLUMN_DATE = "fecha"
            private const val COLUMN_AMOUNT = "monto"
        private const val TABLE_STATS = "conteo"
            private const val COLUMN_MONEY = "dinero"
            private const val COLUMN_SPICE = "especie"
            private const val COLUMN_VOLUNTEER = "voluntariado"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val queryUser = "CREATE TABLE $TABLE_USER(" +
                "$COLUMN_NAMES TEXT," +
                "$COLUMN_LNAMES TEXT," +
                "$COLUMN_EMAIL TEXT," +
                "$COLUMN_CELLPHONE TEXT," +
                "$COLUMN_ADDRESS TEXT);"
        db?.execSQL(queryUser)
        val queryDonations = "CREATE TABLE $TABLE_DONATIONS(" +
                "$COLUMN_DATE TEXT," +
                "$COLUMN_AMOUNT INT);"
        db?.execSQL(queryDonations)
        val queryStats = "CREATE TABLE $TABLE_STATS(" +
                "$COLUMN_VOLUNTEER INT," +
                "$COLUMN_SPICE INT," +
                "$COLUMN_MONEY INT);"
        db?.execSQL(queryStats)
        val queryDefaultStats = "INSERT INTO $TABLE_STATS VALUES (0, 0, 0);"
        db?.execSQL(queryDefaultStats)
        val queryDefaultUser = "INSERT INTO $TABLE_USER VALUES ('', '', '', '', '');"
        db?.execSQL(queryDefaultUser)
    }

    override fun onUpgrade(db: SQLiteDatabase?, vAnterior: Int, vActual: Int) {
        val query = "DROP TABLE IF EXISTS ?"
        val args = arrayOf(TABLE_USER, TABLE_DONATIONS, TABLE_STATS)
        db?.execSQL(query, args)
        onCreate(db)
    }

    fun findUserInfo(): Pair<String, String> {
        val cursor = readableDatabase.query(TABLE_STATS, null, null, null, null, null, null)
        cursor.moveToFirst()
        return Pair(cursor.getString(0).toString(), cursor.getString(1).toString())
    }

    fun voluntariado() {
        val cursor = readableDatabase.query(TABLE_STATS, null, null, null, null, null, null)
        cursor.moveToFirst()
        var num = cursor.getInt(0) + 1
        val valores = ContentValues()
        valores.put(COLUMN_VOLUNTEER, num)
        writableDatabase.update(TABLE_STATS, valores, null, null)
    }

    fun donacionEspecie() {
        val cursor = readableDatabase.query(TABLE_STATS, null, null, null, null, null, null)
        cursor.moveToFirst()
        var num = cursor.getInt(1) + 1
        val valores = ContentValues()
        valores.put(COLUMN_SPICE, num)
        writableDatabase.update(TABLE_STATS, valores, null, null)
    }

    fun donacionDinero(monto : String, fecha : String) {
        val cursor = readableDatabase.query(TABLE_STATS, null, null, null, null, null, null)
        cursor.moveToFirst()
        val num = cursor.getInt(2) + 1
        val valoresStat = ContentValues()
        valoresStat.put(COLUMN_MONEY, num)
        writableDatabase.update(TABLE_STATS, valoresStat, null, null)
        val valoresDonation = ContentValues()
        valoresDonation.put(COLUMN_DATE, fecha)
        valoresDonation.put(COLUMN_AMOUNT, monto)
        writableDatabase.insert(TABLE_DONATIONS, null, valoresDonation)

    }

    fun guardarUsuario(nombres : String, apellidos : String, email : String, telefono : String, direccion: String) {
        val valoresUser = ContentValues()
        valoresUser.put(COLUMN_NAMES, nombres)
        valoresUser.put(COLUMN_LNAMES, apellidos)
        valoresUser.put(COLUMN_EMAIL, email)
        valoresUser.put(COLUMN_CELLPHONE, telefono)
        valoresUser.put(COLUMN_ADDRESS, direccion)
        writableDatabase.update(TABLE_USER, valoresUser, null, null)
    }

    fun borrarUsuario() {
        val valoresUser = ContentValues()
        valoresUser.put(COLUMN_NAMES, String())
        valoresUser.put(COLUMN_LNAMES, String())
        valoresUser.put(COLUMN_EMAIL, String())
        valoresUser.put(COLUMN_CELLPHONE, String())
        valoresUser.put(COLUMN_ADDRESS, String())
        writableDatabase.update(TABLE_USER,valoresUser, null, null)
    }

    fun llamarUsuario(): Array<String> {
        val cursor = readableDatabase.query(TABLE_USER, null, null, null, null, null, null)
        cursor.moveToFirst()
        var nombres = cursor.getString(0)
        var apellidos = cursor.getString(1)
        var email = cursor.getString(2)
        var telefono = cursor.getString(3)
        var direccion = cursor.getString(4)
        return arrayOf(nombres, apellidos, email, telefono, direccion)
    }

}