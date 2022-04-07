package com.example.currency

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

import com.example.currency.adapter.MyCurrencyAdapter

import com.example.currency.databinding.ActivityMainBinding
import com.example.currency.model.Money
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_currency_info.view.*
import org.json.JSONArray
import java.net.URL

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var myCurrencyAdapter: MyCurrencyAdapter
    lateinit var requestQueue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestQueue = Volley.newRequestQueue(this)
        loadArray("http://cbu.uz/uzc/arkhiv-kursov-valyut/json/")

    }

    private fun loadArray(url: String) {
        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET,url,null,object : Response.Listener<JSONArray>{
            override fun onResponse(response: JSONArray?) {
                val str = response.toString()
                val type = object : TypeToken<ArrayList<Money>>(){}.type
                val list = Gson().fromJson<ArrayList<Money>>(str,type)

                Log.d(TAG, "onResponse: $list")

                 myCurrencyAdapter = MyCurrencyAdapter(list,object:MyCurrencyAdapter.RvClick{
                    @SuppressLint("SetTextI18n")
                    override fun onItemClick(money: Money) {
                        val sheetDialog = BottomSheetDialog(this@MainActivity)
                         val info =    LayoutInflater.from(this@MainActivity).inflate(R.layout.item_currency_info,null,false)
                        info.tv_1.text ="Eng: "+ money.CcyNm_EN
                        info.tv_2.text ="Ru: "+ money.CcyNm_RU
                        info.tv_3.text ="Uz: "+ money.CcyNm_UZ
                        info.tv_4.text ="Rating: "+ money.Rate
                        info.tv_5.text ="Last update: " + money.Date


                        sheetDialog.setContentView(info)
                        sheetDialog.show()
                    }
                })

                val manager = LinearLayoutManager(this@MainActivity)
                rv_currency.layoutManager = manager
                rv_currency.adapter = myCurrencyAdapter
            }
        },object :Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError?) {
                Toast.makeText(this@MainActivity, "Internet error", Toast.LENGTH_SHORT).show()
            }
        })
        requestQueue.add(jsonArrayRequest)
    }
}