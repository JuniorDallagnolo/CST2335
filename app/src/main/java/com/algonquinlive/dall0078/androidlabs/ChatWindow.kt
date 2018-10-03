package com.algonquinlive.dall0078.androidlabs

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class ChatWindow : Activity() {

    var chatMessages = ArrayList<String>() //automatically grows

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_window)

        //get id for list view, edit text and send button

        var editText = findViewById(R.id.edit) as EditText
        var listItem = findViewById(R.id.listItems)  as ListView
        var sendBtn = findViewById(R.id.btnSend1) as Button

        var messageAdapter = ChatAdapter( this )

        sendBtn.setOnClickListener{

            var typedString = editText.text.toString()
            chatMessages.add(typedString)

            messageAdapter.notifyDataSetChanged()

            editText.setText("")

        }
        listItem.setAdapter(messageAdapter)
    }

    inner class ChatAdapter( ctx: Context): ArrayAdapter<String>(ctx, 0) {

        override fun getCount(): Int {
            return chatMessages.size
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {

            var inflater = LayoutInflater.from(parent.getContext())

            var result : View

            if(position % 2 == 0){

                result = inflater.inflate(R.layout.chat_row_incoming, null)

            }else{

                result = inflater.inflate(R.layout.chat_row_outgoing, null)
            }

            val message = result.findViewById(R.id.message_text) as TextView

            message.setText( getItem(position) ) // get the string at position

            return result

        }

        override fun getItem(position: Int): String? {
            return chatMessages.get(position)
        }

        override fun getItemId(position: Int): Long {
            return 0
        }
    }
}

