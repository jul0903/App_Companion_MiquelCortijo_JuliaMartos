package com.example.app_companion_miquel_julia

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query


class MapsFragment : Fragment() {

    private lateinit var dataBase: DatabaseReference
    private lateinit var comment: EditText
    private lateinit var text: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_maps, container, false)

        val dataBaseUrl = "https://aa3-1-app-companion-default-rtdb.europe-west1.firebasedatabase.app/"
        dataBase = FirebaseDatabase.getInstance(dataBaseUrl).getReference("messages")
        dataBase.addChildEventListener(CreateChildEventListener())

        val query: Query = dataBase.orderByChild("user").equalTo("Everyone")

        query.get()
            .addOnSuccessListener { snapshot ->
                if(snapshot.exists()){
                    for(dataSnapshot in snapshot.children){
                        val message = dataSnapshot.child("message").getValue(String::class.java)
                        Log.d("FireBase test", "Message: $message")
                    }
                }else{
                    Log.d("FireBase test", "No messages found.")
                }
            }
            .addOnFailureListener{ exception ->
                Log.d("FireBase test", "Error: ${exception.message}")
            }


        comment = view.findViewById(R.id.comment)

        text = view.findViewById(R.id.mapsText)

        view.findViewById<View>(R.id.send).setOnClickListener{
            Send()
        }

        return view
    }

    private fun CreateChildEventListener(): ChildEventListener {
        return object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.child("user").getValue(String::class.java)
                val message = snapshot.child("message").getValue(String::class.java)

                if (!message.isNullOrEmpty()) {
                    val currentText = text.text.toString()
                    text.text = "$currentText\n$user: $message"
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

    private fun Send(){
        val text = comment.text.toString()
        val dataId = dataBase.push().key

        val messageData = mapOf(
            "user" to "Everyone",
            "message" to text
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
}