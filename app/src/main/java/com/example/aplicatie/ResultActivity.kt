package com.example.aplicatie

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val score = intent.getIntExtra("score", 0)

        val prefs = getSharedPreferences("quiz_prefs", MODE_PRIVATE)
        val highScore = prefs.getInt("high_score", 0)

        // actualizare scor maxim dacă scorul curent e mai mare
        if (score > highScore) {
            prefs.edit().putInt("high_score", score).apply()
        }

        val resultText = findViewById<TextView>(R.id.result_text)
        resultText.text = "Scor final: $score"

        val highScoreText = findViewById<TextView>(R.id.high_score_text)
        highScoreText.text = "Scor maxim: ${maxOf(score, highScore)}"

        val playAgainButton = findViewById<Button>(R.id.play_again_button)
        playAgainButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}
