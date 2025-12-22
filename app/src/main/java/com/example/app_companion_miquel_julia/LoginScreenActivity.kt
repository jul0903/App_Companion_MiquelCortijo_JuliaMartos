package com.example.app_companion_miquel_julia

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginScreenActivity : AppCompatActivity() {

    private lateinit var analytics: FirebaseAnalytics

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText

    private lateinit var playerPreferencesUser: SharedPreferences
    private lateinit var playerPreferencesPassword: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        analytics = FirebaseAnalytics.getInstance(this)

        //Variables que almacenarán valores que se guardarán aunque se cierre la app
        playerPreferencesUser = getSharedPreferences("prefs_user", Context.MODE_PRIVATE)
        playerPreferencesPassword = getSharedPreferences("prefs_password", Context.MODE_PRIVATE)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("843695849584-dghopgbthu58ua4ca5i7mep5keeons6c.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        //INICIAR SESIÓN CON GOOGLE
        //Comprueba si ya hay una cuenta de Google iniciada, si la hay, inicia sesión
        val account = GoogleSignIn.getLastSignedInAccount(this)
        account?.let{
            Log.d("Login Google", "Ya has robado la info de: " + account.displayName + "anteriormente")

            //Guarda el usuario en la base de datos de Fire base
            SaveUserInDataBase()

            analytics.logEvent("googleLogin", null)

            //Inicia sesión con Google
            SignIn()
        }?:run{
            Log.d("Login Google", "No hay sesión iniciada")
        }

        //INICIAR SESIÓN CON FIRE BASE
        //Completa el campo de usuario con la variable que siempre se
        // guarda aunque se cierre la app, si no es nula.
        emailField = findViewById(R.id.user)
        val storedUser = playerPreferencesUser.getString("Resultado", null)
        if (!storedUser.isNullOrEmpty()) {
            emailField.setText(storedUser)
        }

        //Completa el campo de contraseña con la variable que siempre se
        // guarda aunque se cierre la app, si no es nula.
        passwordField = findViewById(R.id.password)
        val storedPassword = playerPreferencesPassword.getString("Resultado", null)
        if (!storedPassword.isNullOrEmpty()) {
            passwordField.setText(storedPassword)

            //Guarda el usuario en la base de datos de Fire base
            SaveUserInDataBase()

            analytics.logEvent("otherLogin", null)

            //Inicia sesión con Fire base
            Login()
        }

        //Iniciar sesión con Google
        findViewById<SignInButton>(R.id.googleLogin).setOnClickListener{ SignIn() }

        //Iniciar sesión en Fire base
        findViewById<View>(R.id.login).setOnClickListener{ Login() }

        //Registrar nueva cuenta en Fire base
        findViewById<View>(R.id.register).setOnClickListener{ Register() }
    }

    //Llamar a función para iniciar sesión en Google
    private fun SignIn(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 9001)
    }

    //Función de inicio de sesión en Google
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 9001){
            //Intentar iniciar sesión
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if(task.isSuccessful){

                //Si lo consigue, mensaje con el nombre de la cuenta
                val account = task.getResult(ApiException::class.java)
                Log.d("Login Google", "Tengo la info de: " + account.displayName)

                //Guarda el usuario en la base de datos de Fire base
                SaveUserInDataBase()

                analytics.logEvent("googleLogin", null)

                //Entrar en la siguiente pantalla de la app
                val intent: Intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            }else{
                //Si no lo consigue, mensaje de error
                Log.w("Login Google", "Error en el login: ", task.exception)
            }
        }
    }

    //Registrar cuenta en Fire base
    private fun Register(){
        val email = emailField.text.toString()
        val password = passwordField.text.toString()

        //Crear usuario + contraseña
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task->

                //Si lo consigue, mensaje
                if(task.isSuccessful){
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()

                }else{//Si no lo consigue, mensaje de error
                    Toast.makeText(this, "Error en el registro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    //Iniciar sesión en Fire base
    private fun Login(){
        val email = emailField.text.toString()
        val password = passwordField.text.toString()

        //Intentar iniciar sesión
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task->
                //Si lo consigue, mensaje con id de usuario
                if(task.isSuccessful){
                    val user = auth.currentUser
                    val userid = user?.uid
                    Toast.makeText(this, "Inicio de sesión exitoso. ID: " + userid, Toast.LENGTH_SHORT).show()

                    //Guarda valores de user y password para la próxima vez que inicie la app
                    playerPreferencesUser.edit().putString("Resultado", email).apply()
                    playerPreferencesPassword.edit().putString("Resultado", password).apply()

                    //Guarda el usuario en la base de datos de Fire base
                    SaveUserInDataBase()

                    analytics.logEvent("otherLogin", null)

                    //Entrar en la siguiente pantalla de la app
                    val intent: Intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                }else{//Si no lo consigue, mensaje de error
                    Toast.makeText(this, "Error en el inicio de sesión: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    //Guarda el usuario en la base de datos de Fire base
    private fun SaveUserInDataBase(){
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid ?: return

        val database = FirebaseDatabase.getInstance().reference
        if (emailField.text.toString() != null)
            database.child("users").child(uid).child("username").setValue(emailField.text.toString())
    }
}