package com.benincaza.projetofinalkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast

class CreateAccount : AppCompatActivity() {

    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var etConfirmPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        supportActionBar?.hide()

        etEmail = findViewById<EditText>(R.id.etEmail)
        etPassword = findViewById<EditText>(R.id.etPassword)
        etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)

        findViewById<View>(R.id.loginView).setOnClickListener{
            val activity = Intent(this, LoginScreen::class.java);
            startActivity(activity);
        }

        findViewById<View>(R.id.Signin).setOnClickListener {
            Toast.makeText(this, R.string.registro_com_google, Toast.LENGTH_SHORT).show()
        }

        findViewById<View>(R.id.btnCreateAccount).setOnClickListener {
            println(etEmail.text.toString().trim())
            println(etPassword.text.toString().trim())
            println(etConfirmPassword.text.toString().trim())
            Toast.makeText(this, "Registrando normal", Toast.LENGTH_SHORT).show()
        }
    }
}