package com.algonquinlive.dall0078.androidlabs

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import kotlinx.android.synthetic.main.activity_start.*

class ChatWindow : Activity() {

    var chatMessages = ArrayList<String>() //automatically grows
    private val ACTIVITY_NAME = "ChatWindow"
    lateinit var db: SQLiteDatabase
    lateinit var results: Cursor
    lateinit var dbHelper: ChatDatabaseHelper
    lateinit var messageAdapter: ChatAdapter
    var messagePosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_window)

        //LAB 7 STUFF
        var fragmentLocation = findViewById(R.id.fragment_location)
        var iAmTablet = fragmentLocation != null //Very important

        //LAB 5 STUFF
        dbHelper = ChatDatabaseHelper() //get helper object
        db = dbHelper.writableDatabase //open your database
        results = db.query(TABLE_NAME, arrayOf("_id", KEY_MESSAGES), null, null, null, null, null, null)
        //we added this log and for loop today
        Log.i(ACTIVITY_NAME, "Cursor's column count = " + results.columnCount)
        for (i in 0 until results.columnCount) {
            Log.i(ACTIVITY_NAME, results.getColumnName(i))
        }
        results.count
        val idIndex = results.getColumnIndex("_id")
        val idMessages = results.getColumnIndex("Messages")
        results.moveToFirst() //point to first row
        while (!results.isAfterLast) {
            results.getInt(idIndex)
            val thisMessage = results.getString(idMessages)
            chatMessages.add(thisMessage)
            results.moveToNext() // go to next
        }
//        results.close();

        //get id for list view, edit text and send button
        val editText = findViewById(R.id.edit) as EditText
        val listItem = findViewById(R.id.listItems) as ListView
        val sendBtn = findViewById(R.id.btnSend1) as Button

        //LAB 7 Stuff
        listItem.setOnItemClickListener { parent, view, position, id ->
            messagePosition = position
            val string = chatMessages[position] //messages is your ArrayList<string>
            val dataToPass = Bundle()
            dataToPass.putString("Message", string)
            dataToPass.putLong("ID", id)
            if (iAmTablet) {
                //Tablet
                val newFragment = MessageFragment()
                newFragment.arguments = dataToPass //bundle goes to the Fragment
                newFragment.amITablet = iAmTablet
                val transition = fragmentManager.beginTransaction() // how to load fragment ADD REMOVE OR REPLACE
                transition.replace(R.id.fragment_location, newFragment) // where to load, what to load
                transition.commit() //make it run
            } else {
                //Phone
                var detailsActivity = Intent(this, MessageDetails::class.java)
                detailsActivity.putExtras(dataToPass) //send data bro
                startActivityForResult(detailsActivity,35)
            }
        }

        messageAdapter = ChatAdapter(this)

        sendBtn.setOnClickListener {
            val context = this
            val typedString = editText.text.toString()
            chatMessages.add(typedString)
            //LAB 5 STUFF writes to database
            val newRow = ContentValues()
            newRow.put(KEY_MESSAGES, typedString)
            db.insert(TABLE_NAME, "", newRow)
            results = db.query(TABLE_NAME, arrayOf("_id", KEY_MESSAGES), null, null, null, null, null, null)


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

    fun deleteMessage(id: Long) {
        db.delete(TABLE_NAME, "_id=$id", null)
        results = db.query(TABLE_NAME, arrayOf("_id", KEY_MESSAGES), null, null, null, null, null, null)
        chatMessages.removeAt(messagePosition)
        messageAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 35 && resultCode == RESULT_OK){
            deleteMessage(data?.getLongExtra("ID", 0 )!!)
        }
    }

    inner class ChatAdapter(ctx: Context) : ArrayAdapter<String>(ctx, 0) {

        override fun getCount(): Int {
            return chatMessages.size
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {

            val inflater = LayoutInflater.from(parent.getContext())

            val result: View

            result = if (position % 2 == 0) {

                inflater.inflate(R.layout.chat_row_incoming, parent, false)

            } else {

                inflater.inflate(R.layout.chat_row_outgoing, parent, false)
            }

            val message = result.findViewById(R.id.message_text) as TextView

            message.setText(getItem(position)) // get the string at position

            return result

        }

        override fun getItem(position: Int): String? {
            return chatMessages.get(position)
        }

        override fun getItemId(position: Int): Long {
            results.moveToPosition(position)
            val index = results.getColumnIndex("_id")
            return results.getInt(index).toLong()
        }
    }

    val DATABASE_NAME = "Messages.db"
    val VERSION_NUM = 2
    val TABLE_NAME = "Messages"
    val KEY_MESSAGES = "Messages"

    inner class ChatDatabaseHelper : SQLiteOpenHelper(this@ChatWindow, DATABASE_NAME, null, VERSION_NUM) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL("CREATE TABLE $TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT, $KEY_MESSAGES TEXT)") //create the table
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME") //deletes your old data
            //create new table
            onCreate(db)
        }

    }

}


