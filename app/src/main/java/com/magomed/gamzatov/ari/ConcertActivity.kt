package com.magomed.gamzatov.ari

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class ConcertActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.getIntExtra("index", 1) == 1) {
            setContentView(R.layout.concert)
        } else {
            setContentView(R.layout.concert2)
        }
    }
}
