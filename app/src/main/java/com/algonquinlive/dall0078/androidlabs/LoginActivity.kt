package com.algonquinlive.dall0078.androidlabs

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : Activity() {

    val PREFS_KEY = "com.dall0078.androidlabs"
    var prefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Log.i(this.localClassName,"In " + object{}.javaClass.enclosingMethod.name)
        prefs = this.getSharedPreferences(PREFS_KEY,0)
        var loginEmail = prefs!!.getString("loginEmail", "email@domain.com")
        et_username.setText(loginEmail)
        btn_login.setOnClickListener {loginClick() }
    }

    fun loginClick(){
        val editor = prefs!!.edit()
        editor.putString("loginEmail", et_username.text.toString())
        editor.apply()
        var intent = Intent(this,StartActivity::class.java)
        startActivity(intent)
    }


    override fun onResume() {
        super.onResume()
        Log.i(this.localClassName,"In " + object{}.javaClass.enclosingMethod.name)
    }

    override fun onStart() {
        super.onStart()
        Log.i(this.localClassName,"In " + object{}.javaClass.enclosingMethod.name)
    }

    override fun onPause() {
        super.onPause()
        Log.i(this.localClassName,"In " + object{}.javaClass.enclosingMethod.name)
    }

    override fun onStop() {
        super.onStop()
        Log.i(this.localClassName,"In " + object{}.javaClass.enclosingMethod.name)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(this.localClassName,"In " + object{}.javaClass.enclosingMethod.name)
    }
}