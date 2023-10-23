package com.yossefjm.dam2_3

import DBHelper
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.yossefjm.dam2_3.db.UserClass

class SignUpActivity : AppCompatActivity() {

    lateinit var nomInput : EditText
    lateinit var apellidoInput : EditText
    lateinit var userInput : EditText
    lateinit var passInput : EditText
    lateinit var naciInput : EditText
    lateinit var dniInput : EditText
    lateinit var btnCreateUpdate : Button
    lateinit var btnDelete : Button
    lateinit var titulo : TextView

    val dbHelper = DBHelper(this)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup) // Asocia el diseño XML con esta actividad

        nomInput = findViewById(R.id.nom_input)
        apellidoInput = findViewById(R.id.apellido_input)
        userInput = findViewById(R.id.user_input)
        passInput = findViewById(R.id.pass_input)
        naciInput = findViewById(R.id.naci_input)
        dniInput = findViewById(R.id.dni_input)
        btnCreateUpdate = findViewById(R.id.btnSign)
        btnDelete = findViewById(R.id.btnDelete)
        titulo = findViewById(R.id.titulo)

        val param = intent.getStringExtra("accio")

        if (param == "update") {
            titulo.setText(R.string.update_titulo)

            btnCreateUpdate.text = "UPDATE"
            rellenarCampos()
            dniInput.isEnabled = false
            btnCreateUpdate.setOnClickListener {
                uploadUser()
            }
            btnDelete.setOnClickListener {
                elimateUser()
            }


        } else if (param == "create"){
            titulo.setText(R.string.create_titulo)
            btnCreateUpdate.text = "CREATE"
            btnCreateUpdate.setOnClickListener {
                createUser()
            }
            btnDelete.visibility = View.GONE
        }




        var returnLogIn = findViewById<ImageView>(R.id.return_log_in)

        returnLogIn.setOnClickListener {
            // Cierra la actividad actual y regresa a la actividad anterior
            acabarIntent()
        }
    }

    private fun rellenarCampos() {
        val user: UserClass? = intent.getSerializableExtra("user") as UserClass?
        if (user != null) {
            nomInput.setText(user.firstName)
            apellidoInput.setText(user.lastName)
            userInput.setText(user.username)
            passInput.setText(user.password)
            naciInput.setText(user.birthdate)
            dniInput.setText(user.dni)
        }
    }

    private fun createUser() {
        // Obtiene los valores ingresados por el usuario
        val nombre = nomInput.text.toString()
        val apellido = apellidoInput.text.toString()
        val usuario = userInput.text.toString()
        val contraseña = passInput.text.toString()
        val fechaNacimiento = naciInput.text.toString()
        val dni = dniInput.text.toString()

        // Realiza la verificación de los campos
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido) || TextUtils.isEmpty(usuario) || TextUtils.isEmpty(contraseña) || TextUtils.isEmpty(fechaNacimiento) || TextUtils.isEmpty(dni)) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
        } else {
            var usuari = UserClass(
                firstName = nombre,
                lastName = apellido,
                username = usuario,
                password = contraseña,
                birthdate = fechaNacimiento,
                dni = dni
            )

            dbHelper.addUser(usuari)
            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
            acabarIntent()
        }

    }

    private fun uploadUser() {
        // Obtiene los valores ingresados por el usuario
        val nombre = nomInput.text.toString()
        val apellido = apellidoInput.text.toString()
        val usuario = userInput.text.toString()
        val contraseña = passInput.text.toString()
        val fechaNacimiento = naciInput.text.toString()
        val dni = dniInput.text.toString()

        // Realiza la verificación de los campos
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido) || TextUtils.isEmpty(usuario) || TextUtils.isEmpty(contraseña) || TextUtils.isEmpty(fechaNacimiento) || TextUtils.isEmpty(dni)) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
        } else {
            var usuari = UserClass(
                firstName = nombre,
                lastName = apellido,
                username = usuario,
                password = contraseña,
                birthdate = fechaNacimiento,
                dni = dni
            )

            dbHelper.updateUser(usuari)
            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
            acabarIntent()
        }
    }

    private fun elimateUser(){
        val dni = dniInput.text.toString()
        dbHelper.deleteUser(dni)
        Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show()
        acabarIntent()
    }

    private fun acabarIntent(){
        finish()
    }

}
