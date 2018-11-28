package com.algonquinlive.dall0078.androidlabs

import android.app.AlertDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast


class TestToolbar : AppCompatActivity() {

    var currentMessage: String = "You selected item 1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_toolbar)
        val lab8_toolbar = findViewById(R.id.lab8_toolbar) as android.support.v7.widget.Toolbar
        setSupportActionBar(lab8_toolbar)
        val btn_snack = findViewById(R.id.snackBar)
        btn_snack.setOnClickListener {
            Snackbar.make(btn_snack, "Hello snackbar", Snackbar.LENGTH_SHORT)
                    .setAction("Undo") { _ ->
                        Toast.makeText(this@TestToolbar, "Undone", Toast.LENGTH_LONG).show()
                    }
                    .show()
        }
    }

    override fun onCreateOptionsMenu(m: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, m)
        return true
    }

    override fun onOptionsItemSelected(mi: MenuItem): Boolean {
        when (mi.itemId) {
            R.id.action_add -> {
                Log.d("Toolbar", "Option 1 selected")
                Snackbar.make(findViewById(R.id.action_add), currentMessage, Snackbar.LENGTH_SHORT).show()
            }
            R.id.action_cancel -> {
                Log.d("Toolbar", "Option 2 selected")
                val builder: AlertDialog.Builder = this.let {
                    AlertDialog.Builder(it).setTitle(R.string.dia_title).setPositiveButton(R.string.dia_pos) { _, _ ->
                        finish()
                    }.setNegativeButton(R.string.dia_neg) { _, _ -> }
                }
                builder.create().show()
            }
            R.id.action_edit -> {
                Log.d("Toolbar", "Option 3 selected")
                val dialogStuff = layoutInflater.inflate(R.layout.dialog_stuff, null)
                var editText = dialogStuff.findViewById(R.id.newMessageField) as EditText
                val builder: AlertDialog.Builder = this.let {
                    AlertDialog.Builder(it).setTitle("Question")
                            .setView(dialogStuff)
                            .setPositiveButton(R.string.dia_pos) { _, _ ->
                                currentMessage = editText.text.toString()
                            }.setNegativeButton(R.string.dia_neg) { _, _ -> }
                }
                builder.create().show()
            }
            R.id.action_about -> Toast.makeText(this, "Version 1.0, by Ernilo Dallagnolo Junior", Toast.LENGTH_LONG).show()
        }
        return true
    }

}
