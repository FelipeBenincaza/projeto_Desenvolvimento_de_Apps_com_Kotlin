package com.benincaza.projetofinalkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class CreateAccount : AppCompatActivity() {

    lateinit var edtEmail: EditText
    lateinit var edtPassword: EditText
    lateinit var edtConfirmPassword: EditText
    lateinit var createAccountInputArray: Array<EditText>

    val Req_Code:Int=123456;
    lateinit var mGoogleSignInClient: GoogleSignInClient;
    private lateinit var firebaseAuth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        supportActionBar?.hide()

        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()

        edtEmail = findViewById<EditText>(R.id.edt_email)
        edtPassword = findViewById<EditText>(R.id.edt_password)
        edtConfirmPassword = findViewById<EditText>(R.id.edt_confirm_password)

        createAccountInputArray = arrayOf(edtEmail, edtPassword, edtConfirmPassword)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        findViewById<View>(R.id.txt_login_view).setOnClickListener{
            val activity = Intent(this, LoginScreen::class.java)
            startActivity(activity)
        }

        findViewById<View>(R.id.btn_signin).setOnClickListener {
            Toast.makeText(this, R.string.registro_com_google, Toast.LENGTH_SHORT).show()
            signInGoogle()
        }

        findViewById<View>(R.id.btn_create_account).setOnClickListener {
            signIn()
        }
    }

    private fun signInGoogle(){
        val signIntent: Intent = mGoogleSignInClient.signInIntent;
        startActivityForResult(signIntent, Req_Code);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == Req_Code){
            val result = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleResult(result);
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try{
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java);
            Toast.makeText(this, "Logado com sucesso", Toast.LENGTH_SHORT).show();
            if(account != null){
                UpdateUser(account)
            }
        }catch (e: ApiException){
            println(e)
            Toast.makeText(this, "Falha ao logar", Toast.LENGTH_SHORT).show();
        }
    }

    private fun UpdateUser(account: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(account.idToken, null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {task ->
            if(task.isSuccessful){
                val intent = Intent(this, MainActivity::class.java);
                startActivity(intent);
                finish()
            }
        }
    }

    private fun notEmpty(): Boolean = edtPassword.text.toString().trim().isNotEmpty() &&
            edtEmail.text.toString().trim().isNotEmpty() &&
            edtConfirmPassword.text.toString().trim().isNotEmpty()

    private fun verifyIdenticalPassword(): Boolean{
        var identical = false;
        if(edtPassword.text.toString().trim() == edtConfirmPassword.text.toString().trim()){
            identical = true;
        }

        return identical;
    }

    private fun verifySizePassword(): Boolean{
        var correct = false
        if(edtPassword.text.toString().trim().length >= 6) {
            correct = true
        }
        return correct;
    }

    private fun signIn(){
        if(notEmpty()){
            if(verifyIdenticalPassword()){
                if(verifySizePassword()){
                    val userEmail = edtEmail.text.toString().trim()
                    val userPassword = edtPassword.text.toString().trim()

                    firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener{task ->
                        if(task.isSuccessful){
                            sendEmailVerification()
                            Toast.makeText(this, "Usuário criado com sucesso. Verifique sua caixa de e-mail", Toast.LENGTH_SHORT).show();
                            finish()
                        }else{
                            val exception = task.exception;
                            if(exception is FirebaseAuthException && exception.errorCode == "ERROR_EMAIL_ALREADY_IN_USE"){
                                Toast.makeText(this, "Email já cadastrado.", Toast.LENGTH_SHORT).show();
                            }else if(exception is FirebaseAuthException && exception.errorCode == "ERROR_WEAK_PASSWORD"){
                                Toast.makeText(this, "Senha fraca.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(this, "Erro ao criar usuário.", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }else{
                    Toast.makeText(this, "As senhas são iguais mas de tamanho inadequado.", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "As senhas não coincidem.", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
        }
    }

    private fun sendEmailVerification(){
        val firebaseUser: FirebaseUser? = firebaseAuth.currentUser

        firebaseUser?.let {
            it.sendEmailVerification().addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "E-mail enviado com sucesso", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}