package com.example.exercise03

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException




class MainActivity : AppCompatActivity() {

    val okHttpClient = OkHttpClient()
    var arrayList_details:ArrayList<Model> = ArrayList();


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val url="http://www.mocky.io/v2/59a94ceb100000200c3e0a78"
        var message = editText.text.toString()
        button.setOnClickListener {
            Log.i("MCC", "fail")
            getRepos(url)
        }

    }

    fun getRepos(url:String) {
        val request = Request.Builder()
            .url(url)
            .build()
        // 建立Call
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                Log.i("MCC", "fail")
            }
            override fun onResponse(call: Call?, response: Response?) {
                var str_response = response?.body()!!.string()
                Log.i("MCC",str_response)
                //creating json array
                val jsonarray = JSONArray(str_response)

                var size:Int = jsonarray.length()
                for (i in 0.. size-1) {
                    var json_objectdetail:JSONObject=jsonarray.getJSONObject(i)
                    var model:Model= Model();
                    model.photo=json_objectdetail.getString("photo")
                    model.author=json_objectdetail.getString("author")
                    arrayList_details.add(model)
                    Log.i("MCC",model.author)
                }
            }
        })
    }
}


