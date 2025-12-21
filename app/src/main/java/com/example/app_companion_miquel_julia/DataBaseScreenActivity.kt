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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class DataBaseScreenActivity : AppCompatActivity() {

    private lateinit var dataBase: DatabaseReference

    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_base_screen)

        val dataBaseUrl = "https://aa3-1-app-companion-default-rtdb.europe-west1.firebasedatabase.app/"
        dataBase = FirebaseDatabase.getInstance(dataBaseUrl).getReference("messages")
        dataBase.addChildEventListener(CreateChildEventListener())

        val query: Query = dataBase.orderByChild("user").equalTo("Jose")

        query.get()
            .addOnSuccessListener { snapshot ->
                if(snapshot.exists()){
                    for(dataSnapshot in snapshot.children){
                        val message = dataSnapshot.child("message").getValue(String::class.java)
                        Log.d("FireBase test", "Message: $message")
                    }
                }else{
                    Log.d("FireBase test", "No messages found for user Jose.")
                }
            }
            .addOnFailureListener{ exception ->
                Log.d("FireBase test", "Error: ${exception.message}")
            }

        emailField = findViewById(R.id.user)
        passwordField = findViewById(R.id.password)

        findViewById<SignInButton>(R.id.googleLogin).setOnClickListener{ SignIn() }
        findViewById<View>(R.id.logout).setOnClickListener{ SignOut() }

        findViewById<View>(R.id.login).setOnClickListener{ Login() }
        findViewById<View>(R.id.register).setOnClickListener{ Register() }
    }

    private fun CreateChildEventListener(): ChildEventListener{
        return object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                dataBase.ref.get().addOnSuccessListener { fullSnapshot ->
                    for(child in fullSnapshot.children){
                        val u = child.child("user").getValue((String::class.java))
                        val m = child.child("message").getValue((String::class.java))
                        Log.d("FireBase test", "User: $u, Message: $m")
                    }
                }.addOnFailureListener{ e ->
                    Log.e("FireBase test", "Error fetching fullcollection: ${e.message}")
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val newUser = snapshot.child("user").getValue(String::class.java)
                val newMessage = snapshot.child("message").getValue(String::class.java)

                val oldSnapshot = previousChildName?.let {dataBase.child(it).get().result}
                val oldUser = oldSnapshot?.child("user")?.getValue(String::class.java)
                val oldMessage = oldSnapshot?.child("message")?.getValue(String::class.java)

                Log.d("FireBase test", "Changed - Old User: $oldUser, Old Message: $oldMessage")
                Log.d("FireBase test", "Changed - New User: $newUser, New Message: $newUser")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val user = snapshot.child("user").getValue(String::class.java)
                val message = snapshot.child("message").getValue(String::class.java)

                Log.d("FireBase test", "Removed - User: $user, Message: $message")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                val movedKey = snapshot.key

                Log.d("FireBase test", "Moved - From: $previousChildName, To: $movedKey")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("FireBase test", "Cancelled - Error: ${error.message}")
            }
        }
    }

    private fun SignIn(){
        val dataId = dataBase.push().key

        val messageData = mapOf(
            "user" to "Jose",
            "message" to "Hello World"
        )

        if(dataId != null){
            dataBase.child(dataId).setValue(messageData)
                .addOnSuccessListener { result ->
                    Log.d("FireBase test", "Insert correcto")
                }
                .addOnFailureListener{ exception ->
                    Log.d("FireBase test", "Error: ${exception.message}")
                }
        }
    }

    private fun SignOut(){
    }

    private fun Register(){
    }

    private fun Login(){
    }
}