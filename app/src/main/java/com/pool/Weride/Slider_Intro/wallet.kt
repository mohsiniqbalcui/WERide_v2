package com.pool.Weride.Slider_Intro;

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pool.Weride.R

class wallet : AppCompatActivity() {
    private var recyclerView4: RecyclerView? = null
    private var array = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(1024, 1024)
        setContentView(R.layout.activity_wallet)

        array.add("https://drive.google.com/uc?id=1mr9M0BMdMO83JfkmH4mIuNwK_G_C8m5P")
        array.add("https://drive.google.com/uc?id=15Geyuwc_PUTYRfvYVh9yewyp-bPE-aRl")
        array.add("https://drive.google.com/uc?id=1GtcJkJCQK2aYs0LZ78rZzaSMdtitFns_")
        array.add("https://drive.google.com/uc?id=1BZQDPndC-tnF543zxm_zHq0tTN9XhqYO")
        array.add("https://drive.google.com/uc?id=1giidrbLMlP-v-LW6W_eiiObKJxG_kOGh")

        recyclerView4 = findViewById(R.id.recyclerview1)
        val mLayoutManager4 = LinearLayoutManager(this@wallet, LinearLayoutManager.HORIZONTAL,
                false)
        recyclerView4!!.layoutManager = mLayoutManager4
        recyclerView4!!.setHasFixedSize(true)
        recyclerView4!!.isNestedScrollingEnabled = false
        recyclerView4!!.adapter = SliderAdapter(this, array)

    }
}
