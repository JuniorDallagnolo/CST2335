package com.algonquinlive.dall0078.androidlabs

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_list_item.*
import android.provider.MediaStore
import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast


class ListItemActivity : Activity() {

    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_item)
        Log.i(this.localClassName, "In " + object {}.javaClass.enclosingMethod.name)
        btn_image.setOnClickListener { dispatchTakePictureIntent() }
        btn_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val toast = Toast.makeText(this, "Switch is On", Toast.LENGTH_SHORT)
                toast.show()
            } else {
                val toast = Toast.makeText(this, "Switch is Off", Toast.LENGTH_LONG)
                toast.show()
            }
        }
        btn_check.setOnCheckedChangeListener { buttonView, isChecked ->
            val builder = AlertDialog.Builder(this)
            builder.setMessage(R.string.modal_dialog_message)
                    .setTitle(R.string.modal_dialog_title)
                    .setPositiveButton(R.string.modal_ok) { dialog, id ->
                        val resultIntent = Intent()
                        resultIntent.putExtra("Response", "Here is my response")
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
                    .setNegativeButton(R.string.modal_no) { dialog, id ->
                    }
                    .show()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i(this.localClassName, "In " + object {}.javaClass.enclosingMethod.name)
    }

    override fun onStart() {
        super.onStart()
        Log.i(this.localClassName, "In " + object {}.javaClass.enclosingMethod.name)
    }

    override fun onPause() {
        super.onPause()
        Log.i(this.localClassName, "In " + object {}.javaClass.enclosingMethod.name)
    }

    override fun onStop() {
        super.onStop()
        Log.i(this.localClassName, "In " + object {}.javaClass.enclosingMethod.name)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(this.localClassName, "In " + object {}.javaClass.enclosingMethod.name)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val extras = data.extras
            val imageBitmap = extras!!.get("data") as Bitmap
            btn_image.setImageBitmap(imageBitmap)
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }
}
