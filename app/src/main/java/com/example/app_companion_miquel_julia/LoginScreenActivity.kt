package com.example.app_companion_miquel_julia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException

class LoginScreenActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("843695849584-dghopgbthu58ua4ca5i7mep5keeons6c.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        findViewById<SignInButton>(R.id.login).setOnClickListener{ SignIn() }
        findViewById<View>(R.id.logout).setOnClickListener{ SignOut() }
    }

    private fun SignIn(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 9001)

        //val intent: Intent = Intent(this, MainActivity::class.java)
        //startActivity(intent)
        //finish()
    }

    private fun SignOut(){
        googleSignInClient.signOut()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 9001){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if(task.isSuccessful){
                val account = task.getResult(ApiException::class.java)
                Log.d("Login Google", "Tengo la info de: " + account.displayName)
                val intent: Intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Log.w("Login Google", "Error en el login: ", task.exception)
            }
        }
    }
}