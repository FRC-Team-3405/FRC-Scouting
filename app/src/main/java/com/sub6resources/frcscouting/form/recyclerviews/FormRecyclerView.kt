package com.sub6resources.frcscouting.form.recyclerviews

import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.*
import com.jakewharton.rxbinding2.widget.RxTextView
import com.sub6resources.frcscouting.R
import com.sub6resources.frcscouting.form.model.Field
import com.sub6resources.frcscouting.form.model.FieldType
import com.sub6resources.frcscouting.form.model.Choice
import com.sub6resources.utilities.*

/**
 * Created by whitaker on 12/28/17.
 */
class FormRecyclerViewHolder(v: View, val getChoicesForField: (field: Field) -> List<Choice>, val setAnswer: (field: Field, answer: String) -> Unit, val selectImages: (field: Field) -> String): BaseRecyclerViewHolder<Field>(v) {

    val questionText by bind<TextView>(R.id.field_take_text)
    val answerEditText by bind<EditText>(R.id.field_take_answer)
    val answerChoices by bind<RadioGroup>(R.id.field_take_choices)
    val answerImageContainer by bind<ConstraintLayout>(R.id.field_take_image_container)
    val answerImageSelectButton by bind<Button>(R.id.field_take_image_add_button)
    val answerImageText by bind<TextView>(R.id.field_take_image_text)

    override fun onBind(data: Field) {
        questionText.text = data.fieldText

        answerEditText.hide()
        answerChoices.hide()
        answerImageContainer.hide()

        when(data.type) {
            FieldType.BLANK -> {
                answerEditText.show()
                RxTextView.afterTextChangeEvents(answerEditText).subscribe {
                    setAnswer(data, it.editable().toString())
                }
            }
            FieldType.TRUEFALSE, FieldType.MULTICHOICE -> {
                answerChoices.show()
                val choices = getChoicesForField(data)
                choices.forEach {
                    answerChoices.addView(RadioButton(answerChoices.context).apply {
                        text = it.choiceText
                        onClick {
                            setAnswer(data, (it as RadioButton).text.toString())
                        }
                    })
                }
            }
            FieldType.IMAGE -> {
                answerImageContainer.show()
                answerImageText.text = "0 images selected"
                answerImageSelectButton.onClick {
                    val newAnswer = selectImages(data)
                    setAnswer(data, "IMGS:")
                }
            }
        }
    }
}

class FormRecyclerAdapter(fields: List<Field>, getChoicesForField: (field: Field) -> List<Choice>, setAnswer: (field: Field, answer: String) -> Unit, selectImages: (field: Field) -> String):
        BaseRecyclerViewAdapter<Field>(fields.toMutableList(), R.layout.item_field_take, {
            FormRecyclerViewHolder(it, getChoicesForField, setAnswer, selectImages)
        })
