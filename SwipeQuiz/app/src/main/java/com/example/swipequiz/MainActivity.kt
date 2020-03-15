package com.example.swipequiz

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.reflect.typeOf


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    val questions = arrayListOf<Question>() // Make new arraylist of object Question. Still needs to be filled up

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createItemTouchHelper().attachToRecyclerView(rvQuestions)

        viewManager = LinearLayoutManager(this)
        viewAdapter = QuestionAdapter(questions)

        recyclerView = findViewById<RecyclerView>(R.id.rvQuestions).apply {
            setHasFixedSize(true)   // Performance tweak
            layoutManager = viewManager
            adapter = viewAdapter
        }

        initViews()
    }

    fun initViews() {
        rvQuestions.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        rvQuestions.adapter = viewAdapter

        val questionsStrings  = resources.getStringArray(R.array.questions) // Array of questions from strings.xml
        fillQuestions(questionsStrings) // Fill questions with array retrieved earlier

        viewAdapter.notifyDataSetChanged() // miss bij elke loop
    }

   ///

    private fun createItemTouchHelper(): ItemTouchHelper {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                var answer = false

                if (direction == ItemTouchHelper.RIGHT) {
                    Log.i("OnSwiped", "Right swipe detected")
                    answer = true
                } else if (direction == ItemTouchHelper.LEFT) {
                    Log.i("OnSwiped", "Left swipe detected")
                    answer = false // Code verbosity
                }

                var questionsMap = linkedMapOf( // linked map for retaining order. Dit is echt dubbelop. strings.xml heeft zowel een stringarray als aparte strings.
                    getString(R.string.question0) to true,
                    getString(R.string.question1) to false,
                    getString(R.string.question2) to true,
                    getString(R.string.question3) to true
                )

                val questionAtPos = questions[position].questionText
                Log.d("onSwiped", "Question at pos : " + questionAtPos)
                val correctAnswer = questionsMap.get(questionAtPos)
                Log.d("onSwiped", "Question that should be correct: " + correctAnswer)

                val correct = checkAnswer(correctAnswer, answer)

                if (!correct) {viewAdapter.notifyItemChanged(position)}
            }
        }
        return ItemTouchHelper(callback)
    }

    ///

    fun fillQuestions(questionList: Array<String>) {
        Log.i("fillQuestions", "Arrived at function with data: " + questionList)
        for (question in questionList) {
            Log.i("fillQuestions", "currentquestion: " + question)
            questions.add(Question(question))
            viewAdapter.notifyDataSetChanged()
        }
    }

    fun checkAnswer(correctAnswer: Boolean?, answer: Boolean?): Boolean{
        var correct: Boolean
        Log.d("checkAnswer", "Correct answer: " + correctAnswer)
        Log.d("checkAnswer", "Provided answer: " + answer)

        if (answer == correctAnswer) {
            correct = true
            Toast.makeText(applicationContext, R.string.fbCorrect, Toast.LENGTH_SHORT).show()
        } else {
            correct = false
            Toast.makeText(applicationContext, R.string.fbIncorrect, Toast.LENGTH_SHORT).show()
        }

        return correct
    }
}
