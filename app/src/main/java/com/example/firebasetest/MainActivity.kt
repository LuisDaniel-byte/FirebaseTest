package com.example.firebasetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar FirebaseAuth
        auth = FirebaseAuth.getInstance()
        //loginAnonymous()

        // Inicializar FirebaseDatabase
        database = FirebaseDatabase.getInstance()

        //getSeasons()

        setSeason("8")

        // Usuario
        //val email = "5cvargas.luis@gmail.com"
        //val password = "181196"
    }

    override fun onStart() {
        super.onStart()

        // Verificar si el usuario ya esta autenticado
        val  currentUser : FirebaseUser? = auth.currentUser

        if (currentUser !=null) {
            // El usuario ya esta autenticado
            if (currentUser.email != "") {
                Toast.makeText(this, "Bienvenido" + currentUser.email, Toast.LENGTH_LONG).show()
            } else {
                // El usuario ya esta autenticado como anonimo, realizar alguna accion
                Toast.makeText(this, "Bienvenido" + currentUser.email, Toast.LENGTH_LONG).show()
            }
        } else {
            // El usuario no esta autenticado
            loginAnonymous()

            //no esta login. Usuario
            login("5cvargas.luis@gmail.com","181196")
        }
    }


    fun login(email : String, password : String)  {
        // Iniciar sesión con correo electrónico y contraseña
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso
                    val user = auth.currentUser
                    // Hacer algo con el usuario autenticado
                    Toast.makeText(  this,"Inicio de sesión exitoso", Toast.LENGTH_LONG).show()
                } else {
                    // Error en el inicio de sesión
                    Toast.makeText( this,"Error en el inicio de sesion: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }

    }
    fun loginAnonymous() {
        //Iniciar de forma anonima
        auth.signInAnonymously()
            .addOnCompleteListener{task ->
            if (task.isSuccessful){
                // Inicio se sesión anónimo exitoso
                val user = auth.currentUser
                // Acción con el usuario autenticado
                Toast.makeText(this, "Inicio de sesión anónimo exitoso",Toast.LENGTH_LONG).show()
            }else{
                // Error en el inicio de sesión anónimo
                Toast.makeText(this, "Error en el inicio de sesión anónimo: ${task.exception?.message}",Toast.LENGTH_LONG).show()
            }
        }
    }
    fun getSeasons(){
        val reference = database.getReference("seasons")

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (seasonSnapshot in dataSnapshot.children){
                    val season = seasonSnapshot.getValue(Season::class.java)

                    val message = "{ name : ${season?.name} , description : ${season?.description}, status : ${season?.status}"

                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(databaseError : DatabaseError){
                println("Error al leer los datos ${databaseError.message}")
            }
        })
    }

    fun setSeason(seasonID : String) {
        val reference = database.getReference("seasons")

        val season = Season("Sesión prueba8","Descripción prueba",false)

        reference.child(seasonID).setValue(season).addOnCompleteListener {
            Toast.makeText(this@MainActivity, "Se guardo la info", Toast.LENGTH_LONG).show()
        }
    }
}

