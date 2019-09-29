package com.example.exercise01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var message = editText.text.toString()
        var n = 0
        editText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                message = s.toString()
                n=0

            }
        })



        upperLowerButton.setOnClickListener {

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            var result : String
            if(n%2==0){
                result =  message.map {
                    it.toUpperCase()
                    //Log.i("MainActivity",it.toUpperCase().toString())
                }.joinToString("")
                //Log.i("MainActivity",message.map { it.toUpperCase() }.joinToString(""))
            }else{
                result = message.map {
                    it.toLowerCase()
                    //Log.i("MainActivity",it.toLowerCase().toString())
                }.joinToString("")
                //Log.i("MainActivity",message.map { it.toLowerCase() }.joinToString(""))
            }
            Log.i("MCC",result)

            textUpperLower.setText("Uppercase/Lowercase: " + result)
            n++
        }

        charCount.setOnClickListener {

            val count = message.replace("\\s".toRegex(),"")

            Log.i("MCC",count.count().toString())
            countChar.setText("Count: "+count.count().toString())
        }

    }
}
