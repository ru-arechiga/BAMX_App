package com.example.bamx_app

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.bamx_app.databinding.ActivityMainBinding
import com.example.bamx_app.databinding.FragmentVoluntariadoBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.UserAction

class MainActivity : AppCompatActivity() {

    private lateinit var database : DatabaseReference
    private var count: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_BAMX_App)
        super.onCreate(savedInstanceState)
        val config = CheckoutConfig(
            application = application,
            clientId = "AfXbZq9DheJ1DhUUyahG-vKtPXszfS5yscJ4aDvhCbeNWe16Lx6aIVzsDV9iaqfy8Jxsvt6OWLWZky5h",
            environment = Environment.SANDBOX,
            currencyCode = CurrencyCode.MXN,
            userAction = UserAction.PAY_NOW,
            settingsConfig = SettingsConfig(
                loggingEnabled = true
            )
        )
        PayPalCheckout.setConfig(config)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomNavigationView = binding.include5.navigationView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)
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

    fun easterEgg(view: View?) {
        count = count?.plus(1)
        if (count == 20) {
            val resID = resources.getIdentifier(
                "spinningseal", "raw",
                packageName
            )
            Toast.makeText(this, "IN MEMORIAM: Spinning Seal FM", Toast.LENGTH_SHORT).show()
            val mediaPlayer: MediaPlayer = MediaPlayer.create(this, resID)
            mediaPlayer.start()
            count = 0
        }
    }

}