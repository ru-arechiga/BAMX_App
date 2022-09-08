package com.example.bamx_app

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.RadioButton
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.bamx_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var fragmentoSobreBamx: SobreBamx

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomNavigationView = binding.include5.navigationView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)

        //setContentView(R.layout.activity_main)
        //fragmentoSobreBamx = SobreBamx()
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