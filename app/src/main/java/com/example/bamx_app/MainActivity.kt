package com.example.bamx_app

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.bamx_app.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.UserAction
import java.util.Calendar
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var binding :ActivityMainBinding
    private lateinit var db : DBHelper
    private lateinit var database : DatabaseReference
    private var count: Int? = 0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_BAMX_App)
        db = DBHelper(this)
        val (userName, userSchool) = db.findUserInfo()
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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()
        val bottomNavigationView = binding.include5.navigationView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notif Channel"
            val desc = "A Description of the Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelID, name, importance)
            channel.description = desc
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
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
                val mediaPlayer: MediaPlayer = MediaPlayer.create(this, resID)
                mediaPlayer.start()
                count = 0
            }


        }
}