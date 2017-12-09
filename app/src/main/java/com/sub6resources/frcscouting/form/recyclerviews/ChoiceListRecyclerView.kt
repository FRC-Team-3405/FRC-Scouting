package com.sub6resources.frcscouting.form.recyclerviews

import android.view.View
import android.widget.EditText
import android.widget.Spinner
import com.jakewharton.rxbinding2.widget.RxTextView
import com.sub6resources.frcscouting.R
import com.sub6resources.frcscouting.form.model.Choice
import com.sub6resources.utilities.BaseRecyclerViewAdapter
import com.sub6resources.utilities.BaseRecyclerViewHolder
import com.sub6resources.utilities.bind

/**
 * Created by whitaker on 12/2/17.
 */
class ChoiceListRecyclerViewHolder(v: View, val spinner: Spinner): BaseRecyclerViewHolder<Choice>(v) {

    val choiceText by bind<EditText>(R.id.item_choice_text)
    override fun onBind(data: Choice) {
        choiceText.isEnabled = (spinner.selectedItemPosition == 0) //If answer is multiple choice, allow custom answers, otherwise disable edittext.
        choiceText.setText(data.choiceText)
        RxTextView.afterTextChangeEvents(choiceText).subscribe {
            data.choiceText = it.editable().toString()
        }
    }
}

class ChoiceListRecyclerAdapter(choices: List<Choice>, spinner: Spinner):
        BaseRecyclerViewAdapter<Choice>(choices.toMutableList(), R.layout.item_choice, {
            ChoiceListRecyclerViewHolder(it, spinner)
        })
