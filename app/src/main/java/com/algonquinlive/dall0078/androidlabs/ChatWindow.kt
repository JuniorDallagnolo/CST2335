package com.algonquinlive.dall0078.androidlabs

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import kotlinx.android.synthetic.main.activity_start.*

class ChatWindow : Activity() {

    var chatMessages = ArrayList<String>() //automatically grows

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_window)

        //LAB 5 STUFF
        val dbHelper = ChatDatabaseHelper() //get helper object
         val db = dbHelper.writableDatabase //open your database
        val results = db.query(TABLE_NAME, arrayOf("_id", KEY_MESSAGES), null, null, null, null, null, null)
        val numRows = results.count
        val idIndex = results.getColumnIndex("_id")
        val idMessages = results.getColumnIndex("Messages")
        results.moveToFirst() //point to first row
        while (!results.isAfterLast) {
            var thisID = results.getInt(idIndex)
            var thisMessage = results.getString(idMessages)
            chatMessages.add(thisMessage)
            results.moveToNext() // go to next
        }

        //get id for list view, edit text and send button
        var editText = findViewById(R.id.edit) as EditText
        var listItem = findViewById(R.id.listItems) as ListView
        var sendBtn = findViewById(R.id.btnSend1) as Button

        var messageAdapter = ChatAdapter(this)

        sendBtn.setOnClickListener {
            var context = this
            var typedString = editText.text.toString()
            chatMessages.add(typedString)
            //LAB 5 STUFF writes to database
            val newRow = ContentValues()
            newRow.put(KEY_MESSAGES,typedString)
            db.insert(TABLE_NAME,"",newRow)

            messageAdapter.notifyDataSetChanged()
            editText.setText("")
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(sendBtn.windowToken, 0)
        }
        listItem.adapter = messageAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    inner class ChatAdapter(ctx: Context) : ArrayAdapter<String>(ctx, 0) {

        override fun getCount(): Int {
            return chatMessages.size
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {

            var inflater = LayoutInflater.from(parent.getContext())

            var result: View

            if (position % 2 == 0) {

                result = inflater.inflate(R.layout.chat_row_incoming, null)

            } else {

                result = inflater.inflate(R.layout.chat_row_outgoing, null)
            }

            val message = result.findViewById(R.id.message_text) as TextView

            message.setText(getItem(position)) // get the string at position

            return result

        }

        override fun getItem(position: Int): String? {
            return chatMessages.get(position)
        }

        override fun getItemId(position: Int): Long {
            return 0
        }
    }

    val DATABASE_NAME = "Messages.db"
    val VERSION_NUM = 2
    val TABLE_NAME = "Messages"
    val KEY_MESSAGES = "Messages"

    inner class ChatDatabaseHelper : SQLiteOpenHelper(this@ChatWindow, DATABASE_NAME, null, VERSION_NUM) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL("CREATE TABLE " + TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_MESSAGES + " TEXT)") //create the table
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME") //deletes your old data
            //create new table
            onCreate(db)
        }

    }

}


