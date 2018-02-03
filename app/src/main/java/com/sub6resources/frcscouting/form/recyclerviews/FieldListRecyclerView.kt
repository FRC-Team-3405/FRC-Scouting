package com.sub6resources.frcscouting.form.recyclerviews

import android.view.View
import android.widget.TextView
import com.sub6resources.frcscouting.R
import com.sub6resources.frcscouting.form.model.Field
import com.sub6resources.utilities.BaseRecyclerViewAdapter
import com.sub6resources.utilities.BaseRecyclerViewHolder
import com.sub6resources.utilities.bind
import com.sub6resources.utilities.onClick

/**
 * Created by whitaker on 12/2/17.
 */
class FieldListRecyclerViewHolder(v: View, val onClick: (field: Field) -> Unit): BaseRecyclerViewHolder<Field>(v) {

    val question_text by bind<TextView>(R.id.recycler_field_text)

    override fun onBind(data: Field) {
        question_text.text = data.fieldText

        question_text.onClick {
            onClick(data)
        }
    }
}

class FieldListRecyclerAdapter(questions: List<Field>,  val onClick: (field: Field) -> Unit):
        BaseRecyclerViewAdapter<Field>(questions.toMutableList(), R.layout.item_field, {
            FieldListRecyclerViewHolder(it, onClick)
        })
