package com.example.aplicatie

import android.content.Intent
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import kotlin.system.*
import android.graphics.Color

class QuizActivity : AppCompatActivity() {

    private val allQuestions = listOf(
        Question("Care este dimensiunea tipică a unui `int` în C pe sistemele moderne (64-bit)?",
            listOf("2 bytes", "4 bytes", "8 bytes", "Depinde de compilator"), 1),

        Question("Ce valoare returnează funcția `main()` în C/C++ dacă totul a decurs cu succes?",
            listOf("0", "1", "-1", "void"), 0),

        Question("Ce structură de date folosește stiva funcțiilor în execuție?",
            listOf("Queue", "Heap", "Stack", "Tree"), 2),

        Question("Ce operator în C/C++ este folosit pentru a accesa membrii unui pointer către structură?",
            listOf(".", "->", "::", "#"), 1),

        Question("Cum se numește zona de memorie unde sunt alocate variabilele locale?",
            listOf("Heap", "Stack", "Data segment", "Text segment"), 1),

        Question("Care este complexitatea medie a căutării într-un `hash map` (C++/Java)?",
            listOf("O(n)", "O(1)", "O(log n)", "O(n log n)"), 1),

        Question("Ce instrucțiune oprește complet execuția unui ciclu `for`?",
            listOf("continue", "break", "return", "exit"), 1),

        Question("Care este standardul cel mai recent pentru C++ (în 2023)?",
            listOf("C++11", "C++17", "C++20", "C++23"), 3),

        Question("Ce comandă folosești în terminal pentru a compila un fișier `main.c` cu gcc?",
            listOf("gcc main.c", "g++ main.c", "make main", "compile main.c"), 0),

        Question("Cum este reprezentat caracterul NULL în ASCII?",
            listOf("'\\0'", "'NULL'", "'\\n'", "'0'"), 0)
    )

    private val questions = allQuestions.shuffled().take(5)


    private var currentQuestionIndex = 0
    private var score = 0
    private lateinit var timer: CountDownTimer
    private var startTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        showQuestion()
    }

    private fun showQuestion() {
        if (currentQuestionIndex >= questions.size) {
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("score", score)
            startActivity(intent)
            finish()
            return
        }

        val question = questions[currentQuestionIndex]

        val questionText = findViewById<TextView>(R.id.question_text)
        val answers = listOf<Button>(
            findViewById(R.id.answer1),
            findViewById(R.id.answer2),
            findViewById(R.id.answer3),
            findViewById(R.id.answer4)
        )

        questionText.text = question.text
        answers.forEachIndexed { index, button ->
            // Resetăm fundalul la cel frumos cu colțuri rotunjite
            button.setBackgroundResource(R.drawable.rounded_answer_button)
            button.setTextColor(resources.getColor(android.R.color.black))
            button.isClickable = true
            button.isFocusable = true
            button.text = question.options[index]

            button.setOnClickListener {
                timer.cancel()
                val timeTaken = SystemClock.elapsedRealtime() - startTime

                // Colorăm corect/greșit
                if (index == question.correctAnswerIndex) {
                    score += calculateScore(timeTaken)
                    button.setBackgroundColor(Color.GREEN)
                } else {
                    button.setBackgroundResource(R.drawable.answer_wrong)
                }

                // Dezactivăm butoanele
                answers.forEach {
                    it.isClickable = false
                    it.isFocusable = false
                }

                // Trecem la următoarea întrebare
                Handler(Looper.getMainLooper()).postDelayed({
                    currentQuestionIndex++
                    showQuestion()
                }, 1000)
            }
        }


        startTime = SystemClock.elapsedRealtime()
        startTimer()
    }

    private fun startTimer() {
        val timerText = findViewById<TextView>(R.id.timer_text)
        timer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerText.text = "Timp rămas: ${millisUntilFinished / 1000}s"
            }

            override fun onFinish() {
                currentQuestionIndex++
                showQuestion()
            }
        }.start()
    }

    private fun calculateScore(timeTaken: Long): Int {
        return (10000 - timeTaken).toInt() / 1000
    }

    data class Question(val text: String, val options: List<String>, val correctAnswerIndex: Int)
}
