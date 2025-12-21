package com.example.app_companion_miquel_julia

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val splashLayout: View = findViewById(R.id.splash_screen_layout)

        // Listener: detecta cualquier toque en la pantalla
        splashLayout.setOnClickListener {
            val intent: Intent = Intent(this, LoginScreenActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}