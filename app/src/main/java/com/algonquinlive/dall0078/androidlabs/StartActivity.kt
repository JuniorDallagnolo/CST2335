package com.algonquinlive.dall0078.androidlabs

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        Log.i(this.localClassName,"In " + object{}.javaClass.enclosingMethod.name)
        val btn_ref = findViewById(btn_button.id)
        btn_ref.setOnClickListener{v ->
            val intent = Intent(this, ListItemActivity::class.java)
            startActivityForResult(intent,50) }
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

    override fun onActivityResult(requestCode:Int , responseCode:Int, data:Intent) {
    if (requestCode == 50) {
        Log.i(this.localClassName,"Returned to StartActivity.onActivityResult")
        if (responseCode == Activity.RESULT_OK){
            val messagePassed = data.getStringExtra("Response")
            val toast = Toast.makeText(this, messagePassed, Toast.LENGTH_SHORT)
            toast.show()
        }
    }
    }
}
