package com.example.exercise03_listview

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private val okHttpClient = OkHttpClient()
    private val arrayList_details: ArrayList<Model> = ArrayList();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.recipe_list_view)

        val url = "http://www.mocky.io/v2/59a94ceb100000200c3e0a78"
        var message = editText.text.toString()

        editText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                message = s.toString()
            }
        })

        button.setOnClickListener {
            Log.i("MCC",url)
            getRepos(message)

        }
    }

    fun getRepos(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        val call = okHttpClient.newCall(request)

        call.enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                Log.i("MCC", "fail")
            }

            override fun onResponse(call: Call?, response: Response?) {
                var str_response = response?.body()!!.string()
                Log.i("MCC", str_response)
                //creating json array
                val jsonarray = JSONArray(str_response)

                var size: Int = jsonarray.length()
                for (i in 0..size - 1) {
                    var json_objectdetail: JSONObject = jsonarray.getJSONObject(i)
                    var model: Model = Model();
                    model.photo = json_objectdetail.getString("photo")
                    model.author = json_objectdetail.getString("author")
                    arrayList_details.add(model)
                    //Log.i("MCC", arrayList_details[i].photo)
                }
            }

        })

        Thread(Runnable {
            // performing some dummy time taking operation
            var i=0;
            while(i<Int.MAX_VALUE){
                i++
            }

            // try to touch View of UI thread
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                Log.i("MCC","image list ok")
                val myListAdapter = MyListAdapter(arrayList_details, this)
                listView.adapter = myListAdapter

                listView.setOnItemClickListener(){adapterView, view, position, id ->
                    val itemAtPos = adapterView.getItemAtPosition(position)
                    Log.i("MCC",position.toString())

                        val photoInfo = arrayList_details.get(position)
                        Log.i("MCC",photoInfo.photo)
                        val intent = Intent(this, ShowImage::class.java).apply {
                            putExtra(ShowImage.EXTRA_PHOTO,photoInfo.photo)
                            putExtra(ShowImage.EXTRA_AUTHOR,photoInfo.author)
                        }
                        startActivity(intent)
                    }

            })
        }).start()

    }


    class MyListAdapter(val arrayList_details: ArrayList<Model>, val context: Context) : BaseAdapter() {
        override fun getCount(): Int {
            return arrayList_details.size
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {


            var v = View.inflate(context, R.layout.item_view, null)

            var photoImageView: ImageView = v.findViewById(R.id.rv_photo)
            var photoAuthorView: TextView = v.findViewById(R.id.rv_author)

            Picasso.get()
                .load(arrayList_details[position].photo)
                .fit()
                .tag(context)
                .into(photoImageView)

            photoAuthorView.text=arrayList_details[position].author

            return v
        }

        override fun getItem(position: Int): Any? {
            return arrayList_details.get(position)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

    }


}
