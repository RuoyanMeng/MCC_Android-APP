package com.example.exercise02

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity() {
//    val positiveButtonClick = { dialog: DialogInterface, which: Int ->
//        Toast.makeText(applicationContext,
//            android.R.string.yes, Toast.LENGTH_SHORT).show()

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val upload=     
    }

    fun withItems(view: View) {

        val items = arrayOf("Red", "Orange", "Yellow", "Blue")
        val builder = AlertDialog.Builder(this)
        with(builder)
        {
            setTitle("List of Items")
            setItems(items) { dialog, which ->
                Toast.makeText(applicationContext, items[which] + " is clicked", Toast.LENGTH_SHORT).show()
            }

//            setPositiveButton("OK", positiveButtonClick)
            show()
        }
    }
}
