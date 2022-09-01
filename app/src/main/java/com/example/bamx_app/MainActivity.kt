package com.example.bamx_app

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    lateinit var fragmentoSobreBamx: SobreBamx

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentoSobreBamx = SobreBamx()
    }

    fun facebook(view: View?) {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse("https://www.facebook.com/BDAGDL/")
        startActivity(openURL)
    }

    fun twitter(view: View?) {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse("https://twitter.com/BDAGDL")
        startActivity(openURL)
    }

    fun instagram(view: View?) {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse("https://www.instagram.com/bda_guadalajara/")
        startActivity(openURL)
    }
}