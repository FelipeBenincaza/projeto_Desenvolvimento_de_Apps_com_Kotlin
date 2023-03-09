package com.benincaza.projetofinalkotlin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private var gson = Gson()
    private val NAME_LIST = "lista_de_compras"
    lateinit var mGoogleSignClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();
        mGoogleSignClient = GoogleSignIn.getClient(this, gso)

        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

        val firebaseUser: FirebaseUser? = firebaseAuth.currentUser

        if(firebaseUser == null){
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
        }

        sharedPreferences = getSharedPreferences(NAME_LIST, Context.MODE_PRIVATE)

        val listViewTasks = findViewById<ListView>(R.id.listViewTasks)
        val createTask = findViewById<EditText>(R.id.createTask)

        val itemList = getData()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, itemList)

        findViewById<View>(R.id.btn_add).setOnClickListener {
            itemList.add(createTask.text.toString());
            listViewTasks.adapter = adapter;
            adapter.notifyDataSetChanged()

            createTask.text.clear()
        }

        findViewById<View>(R.id.btn_delete).setOnClickListener {
            val position: SparseBooleanArray = listViewTasks.checkedItemPositions;
            val count = listViewTasks.count;
            var item = count - 1;
            while(item >= 0){
                if(position.get(item)){
                    adapter.remove(itemList.get(item))
                }
                item--;
            }

            saveData(itemList)
            position.clear();
            adapter.notifyDataSetChanged()
        }

        findViewById<View>(R.id.btn_clear).setOnClickListener {
            itemList.clear()
            saveData(itemList)
            adapter.notifyDataSetChanged()
        }

        findViewById<View>(R.id.logout).setOnClickListener{
            firebaseAuth.signOut()
            mGoogleSignClient.signOut()

            startActivity(Intent(this, LoginScreen::class.java))
        }
    }

    private fun getData(): ArrayList<String> {
        val arrayJson = sharedPreferences.getString("lista", null)
        return if(arrayJson.isNullOrEmpty()){
            arrayListOf()
        }else{
            gson.fromJson(arrayJson, object: TypeToken<ArrayList<String>>(){}.type)
        }
    }

    private fun saveData(array: ArrayList<String>){
        val arrayJson = gson.toJson(array)
        val editor = sharedPreferences.edit()
        editor.putString("lista", arrayJson)
        editor.apply();
    }
}