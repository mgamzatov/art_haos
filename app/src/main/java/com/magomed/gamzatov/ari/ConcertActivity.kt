package com.magomed.gamzatov.ari

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri


class ConcertActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.getIntExtra("index", 1) == 1) {
            setContentView(R.layout.concert)
            val buy = findViewById<Button>(R.id.buy)
            buy.setOnClickListener {
                openWebPage("https://bigbilet.ru/ticket-sale/ticket?id_service=623789EE7586AA92E0504B5E01F57E37")
            }

        } else {
            setContentView(R.layout.concert2)
            val buy = findViewById<Button>(R.id.buy)
            buy.setOnClickListener {
                openWebPage("https://bigbilet.ru/ticket-sale/ticket?id_service=6397C256328FABEBE0504B5E01F56715")
            }
        }
    }

    fun openWebPage(url: String) {
        try {
            val webpage = Uri.parse(url)
            val myIntent = Intent(Intent.ACTION_VIEW, webpage)
            startActivity(myIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this,
                "No application can handle this request. Please install a web browser or check your URL.",
                Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
        }

    }
}
