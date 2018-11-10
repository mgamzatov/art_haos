package com.magomed.gamzatov.ari

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageButton
import kotlinx.android.synthetic.main.more.*

class MoreActivity : AppCompatActivity() {

    private lateinit var mp: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.more)

        mp = MediaPlayer.create (this, R.raw.lebedi)
        var position = 0

        val playButton = findViewById<ImageButton>(R.id.play_button)

        playButton.setOnClickListener {
            if (mp.isPlaying) {
                playButton.setBackgroundResource(R.drawable.ic_play_circle)
                position = mp.currentPosition
                mp.pause()
            } else {
                playButton.setBackgroundResource(R.drawable.ic_pause_circle)
                mp.seekTo(position)
                mp.start()
            }
        }

        conc1.setOnClickListener {
            val intent = Intent(this, ConcertActivity::class.java)
            startActivity(intent, null)
        }

        quizBtn.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent, null)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        mp.release()
    }
}
