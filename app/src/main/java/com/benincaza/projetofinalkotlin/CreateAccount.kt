package com.benincaza.projetofinalkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast

class CreateAccount : AppCompatActivity() {

    lateinit var edtEmail: EditText
    lateinit var edtPassword: EditText
    lateinit var edtConfirmPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        supportActionBar?.hide()

        edtEmail = findViewById<EditText>(R.id.edt_email)
        edtPassword = findViewById<EditText>(R.id.edt_password)
        edtConfirmPassword = findViewById<EditText>(R.id.edt_confirm_password)

        findViewById<View>(R.id.txt_login_view).setOnClickListener{
            val activity = Intent(this, LoginScreen::class.java);
            startActivity(activity);
        }

        findViewById<View>(R.id.btn_signin).setOnClickListener {
            Toast.makeText(this, R.string.registro_com_google, Toast.LENGTH_SHORT).show()
        }

        findViewById<View>(R.id.btn_create_account).setOnClickListener {
            Toast.makeText(this, "Registrando normal", Toast.LENGTH_SHORT).show()
        }
    }
}