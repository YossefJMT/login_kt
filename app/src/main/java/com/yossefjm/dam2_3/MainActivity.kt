package com.yossefjm.dam2_3

import DBHelper
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    private lateinit var dbHelper: DBHelper // Declarar la variable dbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbHelper = DBHelper(this)

        // Configura el click listener para el botón de registro
        val btnOpenSignUp = findViewById<Button>(R.id.btnOpenSignUp)
        // Crea un Intent para abrir el SignUpActivity
        btnOpenSignUp.setOnClickListener {
            // Crea un Intent para abrir la otra actividad
            try {
                val intent = Intent(this, SignUpActivity::class.java)
                intent.putExtra("accio", "create")
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error al abrir SignUpActivity", Toast.LENGTH_SHORT).show()
            }
        }

        // Configura el click listener para el botón de log in
        val btnLogIn = findViewById<Button>(R.id.btnLogIn)

        // Crea un Intent para abrir el LogInActivity
        btnLogIn.setOnClickListener() {
            logIn()
        }
    }

    fun logIn() {
        val usuari = findViewById<EditText>(R.id.user_name)
        val contrasenya = findViewById<EditText>(R.id.user_password)

        val user = dbHelper.checkLogin(usuari.text.toString(), contrasenya.text.toString())

        // Comprova les credencials si són correctes obre la següent activitat
        if (credencialsNotEmpty(usuari, contrasenya) && credencialsIsCorrect(usuari, contrasenya)) {
            // Crea un Intent para abrir la otra actividad
            val intent = Intent(this, SignUpActivity::class.java)
            // Configura el objeto UserClass como un extra en el Intent
            intent.putExtra("accio", "update")
            intent.putExtra("user", user)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
        }
    }

    fun credencialsNotEmpty(usuari: EditText, contrasenya: EditText): Boolean {
        // TODO: Comprovar les credencials no son vacias ni tienen un sql injection

        if (usuari.text.toString().isEmpty() || usuari.text.toString().contains(" ") || usuari.text.toString().contains("'")) {
            Toast.makeText(this, "Introdueix un usuari valid", Toast.LENGTH_SHORT).show()
            return false;
        } else if (contrasenya.text.toString().isEmpty() || contrasenya.text.toString().contains(" ")) {
            Toast.makeText(this, "Introdueix una contrasenya VALIDA", Toast.LENGTH_SHORT).show()
            return false;
        }

        return true;
    }

    fun credencialsIsCorrect(usuari: EditText, contrasenya: EditText): Boolean {
        // TODO: Comprobar las credenciales con la base de datos
        val user = dbHelper.checkLogin(usuari.text.toString(), contrasenya.text.toString())
        if (user == null) {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            return false;
        } else {
            Toast.makeText(this, "Credenciales correctas", Toast.LENGTH_SHORT).show()
            return true;
        }
    }
}