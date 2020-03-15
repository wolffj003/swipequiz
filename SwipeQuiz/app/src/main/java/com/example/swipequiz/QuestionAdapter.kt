package com.example.swipequiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.question_item.view.*

class QuestionAdapter(private val questions: ArrayList<Question>) : RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {
    class ViewHolder(questionViewText: View) : RecyclerView.ViewHolder(questionViewText) {
        fun bind(question: Question) {
            itemView.tvQuestion.text = question.questionText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val questionView = LayoutInflater.from(parent.context)
            .inflate(R.layout.question_item, parent, false)
        return ViewHolder(questionView)
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(questions[position])
    }
}
