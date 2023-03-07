package com.benincaza.projetofinalkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast

class LoginScreen : AppCompatActivity() {

    lateinit var etEmail: EditText
    lateinit var etPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)
        supportActionBar?.hide()

        etEmail = findViewById<EditText>(R.id.etEmail)
        etPassword = findViewById<EditText>(R.id.etPassword)

        findViewById<View>(R.id.registerView).setOnClickListener{
            startActivity(Intent(this, CreateAccount::class.java));
        }

        findViewById<View>(R.id.Signin).setOnClickListener {
            Toast.makeText(this, R.string.login_com_google, Toast.LENGTH_SHORT).show()
        }

        findViewById<View>(R.id.btnEnter).setOnClickListener {
            Toast.makeText(this, "Entrando com login normal", Toast.LENGTH_SHORT).show()
        }
    }
}