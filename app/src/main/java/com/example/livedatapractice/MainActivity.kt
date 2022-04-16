package com.example.livedatapractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.livedatapractice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val vmodel : MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.lifecycleOwner = this

        binding.viewModel = vmodel

        initViews()
    }



    fun initViews(){
        val numberTxv = findViewById<TextView>(R.id.txvNumber)
        val nextBtn = findViewById<Button>(R.id.btn_next)
        val backBtn = findViewById<Button>(R.id.btn_back)
        val answer1Txv = findViewById<TextView>(R.id.txv_answer1)
        val answer2Txv = findViewById<TextView>(R.id.txv_answer2)
        val answer3Txv = findViewById<TextView>(R.id.txv_answer3)
        val answer4Txv = findViewById<TextView>(R.id.txv_answer4)
        val scoreTxv = findViewById<TextView>(R.id.txv_score)
        val totalQuestion = findViewById<TextView>(R.id.count_questions_txv)
        val addQuestionBtn = findViewById<Button>(R.id.addQuestionRandom_btn)

        val answersTextViews = arrayListOf<TextView>(
            answer1Txv, answer2Txv, answer3Txv, answer4Txv
        )
        answerClick(answersTextViews)



        nextBtn.setOnClickListener {
            vmodel.nextClicked()
        }

        backBtn.setOnClickListener {
            vmodel.backClicked()
        }

        addQuestionBtn.setOnClickListener {
            vmodel.addRandomQuestion()
        }

        val numberObserver = Observer<Int> { number ->
            numberTxv.text = number.toString()
        }

        val buttonEnabledObserver = Observer<Boolean>{  enabled ->
            nextBtn.isEnabled = enabled
        }

        val backBtnEnabledObserver = Observer<Boolean>{  enabled ->
            backBtn.isEnabled = enabled
        }



        val answerObserver = Observer<ArrayList<Int>> {
           for (i in 0 until answersTextViews.size) {
               answersTextViews[i].text = it[i].toString()
           }
        }

        vmodel.nextEnabledLiveData.observe(this , buttonEnabledObserver)
        vmodel.backEnabledLiveData.observe(this,backBtnEnabledObserver)
        vmodel.questionNumber.observe(this , numberObserver)
        vmodel.answerList.observe(this, answerObserver)
        vmodel.answerEnabledLiveData.observe(this) {
            for (answer in answersTextViews) {
                answer.isEnabled = it
            }
        }
        vmodel.score.observe(this){
            scoreTxv.text = it.toString()
        }
        vmodel.scoreColor.observe(this){
            scoreTxv.setTextColor(ContextCompat.getColor(
                this,
                chooseColor(it))
            )
        }
        vmodel.questionCount.observe(this){
            totalQuestion.text = it.toString()
        }



    }

    private fun chooseColor(it: Color?): Int {

        return  when (it) {
            Color.Green -> R.color.green
            Color.Yellow -> R.color.yellow
            else -> R.color.red
        }
    }


    private fun answerClick(answers: ArrayList<TextView>) {
        for (i in 0 until answers.size){
            answers[i].setOnClickListener {
                vmodel.checkAnswer(answers[i].text.toString().toInt())
            }
        }

    }

}