package com.example.app_companion_miquel_julia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth

class LoginScreenActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("843695849584-dghopgbthu58ua4ca5i7mep5keeons6c.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        emailField = findViewById(R.id.user)
        passwordField = findViewById(R.id.password)

        auth = FirebaseAuth.getInstance()

        findViewById<SignInButton>(R.id.googleLogin).setOnClickListener{ SignIn() }
        findViewById<View>(R.id.logout).setOnClickListener{ SignOut() }

        findViewById<View>(R.id.login).setOnClickListener{ Login() }
        findViewById<View>(R.id.register).setOnClickListener{ Register() }
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
        auth.signOut()
    }

    private fun Register(){
        val email = emailField.text.toString()
        val password = passwordField.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task->
                if(task.isSuccessful){
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Error en el registro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun Login(){
        val email = emailField.text.toString()
        val password = passwordField.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task->
                if(task.isSuccessful){
                    val user = auth.currentUser
                    val userid = user?.uid
                    Toast.makeText(this, "Inicio de sesión exitoso. ID: " + userid, Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Error en el inicio de sesión: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
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