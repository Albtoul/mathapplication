package com.example.mathapplication

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.snackbar.Snackbar
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {

    lateinit var highscoretv:TextView
    lateinit var scoretv:TextView
    lateinit var quistion:TextView
    lateinit var edAns:EditText
    lateinit var submit:Button
    lateinit var clroot : ConstraintLayout
    lateinit var recycler: RecyclerView
    private lateinit var qalist: ArrayList<String>
    private lateinit var sharedPreferences: SharedPreferences
    private var highscore = 0
    private var score = 0
    private var numOne = (0..100).random()
    private var numTow= (0..100).random()
    private var operator = "+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startdilog()
        sharedPreferences = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )

        highscore = sharedPreferences.getInt("highscore", 0)
        clroot = findViewById(R.id.clroot)
        highscoretv=findViewById(R.id.Highscore)
        scoretv=findViewById(R.id.Score)
        quistion=findViewById(R.id.Q)
        edAns=findViewById(R.id.edAns)
        submit=findViewById(R.id.button)
        recycler=findViewById(R.id.Rv)
        qalist= ArrayList()
        recycler.adapter= Adapter(qalist)
        recycler.layoutManager = LinearLayoutManager(this)
       res()

        Question()
        updateScore()
        submit.setOnClickListener { AnsFun()    }


    }
    fun res(){
        recycler.adapter?.notifyItemChanged(qalist.size-1)
        recycler.scrollToPosition(qalist.size -1)

    }

    fun AnsFun(){
        try {
            val answer = edAns.text.toString()
            if (operator == "+") {
                if (answer == "${numOne + numTow}") {

                    qalist.add("${numOne} +${numTow}=$answer")
                    score++

                } else if (answer == "${numOne - numTow}") {
                        qalist.add("$numOne - $numTow = $answer")
                        score++
                    }
            }

            Number()
            Question()
            updateScore()
            res()
            edAns.text.clear()
        }catch (e: NumberFormatException){

            Toast.makeText(this," invalid!! please enter a number ", Toast.LENGTH_LONG).show()
        }


    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.substract -> {
                operator = "-"
                Question()
                Number()

                return true
            }
            R.id.Add -> {
                operator = "+"
                Question()
                Number()

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



    private fun Question() {
        val placeholder = "$numOne $operator $numTow"
        quistion.text = placeholder

    }

    fun Number(){
        numOne = (0..100).random()
        numTow = (0..100).random()
    }

    private fun updateScore() {
        val placeholder = "Score: $score"
        val place="Highscore: $highscore"
        scoretv.text = placeholder
        highscoretv.text=place

    }

    private fun saveScore() {
        if (score > highscore) {
            with(sharedPreferences.edit()) {
                putInt("higher", score)
                apply()
            }

        }
    }


    private fun startdilog() {
        val alert = AlertDialog.Builder(this)
        alert.setTitle("MathGame")
        alert.setMessage("welcom!! keep answer operatr Q")
        alert.setPositiveButton("Start",null).show()


    }

    private fun loseDialog() {
        saveScore()

        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("do you want to play Again")
            .setCancelable(false)
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                this.recreate()
            })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle("Game Over")
        alert.show()
    }













}