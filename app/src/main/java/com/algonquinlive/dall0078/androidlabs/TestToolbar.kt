package com.algonquinlive.dall0078.androidlabs

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.NotificationCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast



class TestToolbar : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var currentMessage: String = "You selected item 1"
    lateinit var btn_snack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_toolbar)
        val lab8_toolbar = findViewById(R.id.lab8_toolbar) as Toolbar
        setSupportActionBar(lab8_toolbar)

        //add navigation to tool bar
        var drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        var toggle = ActionBarDrawerToggle(this, drawer, lab8_toolbar, R.string.open, R.string.close)

        drawer.setDrawerListener(toggle)
        toggle.syncState()

        var navView = findViewById(R.id.navigation_view) as NavigationView
        navView.setNavigationItemSelectedListener(this)

        btn_snack = findViewById(R.id.snackBar) as Button
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.chat_item -> {
                //system notification
                var mBuilder = NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.account)
                        .setAutoCancel(true)
                        .setContentTitle("Let's chat")
                        .setContentText("Chat with me");

                var resultIntent = Intent(this, ChatWindow::class.java)

                var resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                mBuilder.setContentIntent(resultPendingIntent)
                var mNotificationId = 1
                var mNotifyMgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                mNotifyMgr.notify(mNotificationId, mBuilder.build())
            }
            R.id.list_item -> {
                //system notification
                var mBuilder = NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.renew)
                        .setAutoCancel(true)
                        .setContentTitle("List Items")
                        .setContentText("List");

                var resultIntent = Intent(this, ListItemActivity::class.java)

                var resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                mBuilder.setContentIntent(resultPendingIntent)
                var mNotificationId = 2
                var mNotifyMgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                mNotifyMgr.notify(mNotificationId, mBuilder.build())
            }
            R.id.contact_item -> {
                //system notification
                var mBuilder = NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.android)
                        .setAutoCancel(true)
                        .setContentTitle("How to contact")
                        .setContentText("choose one");

//                var resultIntent = Intent(this, ListItemsActivity::class.java)
                var emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
                var resultPendingIntent = PendingIntent.getActivity(this, 0, emailIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                mBuilder.addAction(R.drawable.android, "Send email", resultPendingIntent)

                var smsIntent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:"))
                var resultSMSIntent = PendingIntent.getActivity(this, 0, smsIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                mBuilder.addAction(R.drawable.android, "Send sms", resultSMSIntent)

                //mBuilder.setContentIntent(resultPendingIntent)
                var mNotificationId = 3
                var mNotifyMgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                mNotifyMgr.notify(mNotificationId, mBuilder.build())
            }
        }

        var drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
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
