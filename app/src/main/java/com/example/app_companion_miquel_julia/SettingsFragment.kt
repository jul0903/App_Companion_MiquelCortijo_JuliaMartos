package com.example.app_companion_miquel_julia

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SettingsFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var playerPreferencesUser: SharedPreferences
    private lateinit var playerPreferencesPassword: SharedPreferences

    private lateinit var userName: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        //Antes:
        //playerPreferencesUser =
        //    requireContext().getSharedPreferences("prefs_user", Context.MODE_PRIVATE)
        //playerPreferencesPassword =
        //    requireContext().getSharedPreferences("prefs_password", Context.MODE_PRIVATE)
        //Ahora:
        playerPreferencesUser =
            requireContext().getSharedPreferences(Constants.PREFS_USER_FILE, Context.MODE_PRIVATE)
        playerPreferencesPassword =
            requireContext().getSharedPreferences(Constants.PREFS_PASSWORD_FILE, Context.MODE_PRIVATE)

        userName = view.findViewById(R.id.accountName)

        //Read the username from fire base
        ReadUsername()

        //Al pulsar logout:
        view.findViewById<View>(R.id.logout).setOnClickListener{
            SignOutUserAndPassword()
        }

        return view
    }

    //Sign out y eliminar los valores almacenados en user y password para
    // no hacer auto login al volver a la pantalla anterior
    private fun SignOutUserAndPassword() {
        googleSignInClient.signOut()
        auth.signOut()

        //Eliminar valores almacenados en user y password

        //Antes:
        //playerPreferencesUser.edit().remove("Resultado").apply()
        //playerPreferencesPassword.edit().remove("Resultado").apply()
        //Ahora:
        playerPreferencesUser.edit().remove(Constants.PREFS_RESULT_KEY).apply()
        playerPreferencesPassword.edit().remove(Constants.PREFS_RESULT_KEY).apply()

        val intent = Intent(requireContext(), LoginScreenActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    //Recoge el nombre de usuario de la base de datos de Fire base
    private fun ReadUsername(){
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid ?: return

        val database = FirebaseDatabase.getInstance().reference

        //Antes:
        //database.child("users").child(uid).child("username")
        //Ahora:
        database.child(Constants.NODE_USERS).child(uid).child(Constants.FIELD_USERNAME)
            .get()
            .addOnSuccessListener { snapshot ->
                val username = snapshot.value as? String
                userName.text = username ?: "No username"
            }
    }
}