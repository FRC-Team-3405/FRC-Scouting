package com.sub6resources.frcscouting.form.recyclerviews

import android.graphics.Bitmap
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.*
import com.jakewharton.rxbinding2.widget.RxSeekBar
import com.jakewharton.rxbinding2.widget.RxTextView
import com.sub6resources.frcscouting.R
import com.sub6resources.frcscouting.form.model.Choice
import com.sub6resources.frcscouting.form.model.Field
import com.sub6resources.frcscouting.form.model.FieldType
import com.sub6resources.utilities.*

/**
 * Created by whitaker on 12/28/17.
 */
class FormRecyclerViewHolder(v: View, val getChoicesForField: (field: Field) -> List<Choice>, val setAnswer: (field: Field, answer: String) -> Unit, val getAnswer: (field: Field) -> Choice?, val getExistingImages: (field: Field, callback: (bitmaps: List<Bitmap>) -> Unit) -> Unit, val selectImages: (field: Field, callback: (data: Bitmap, count: Int) -> Unit) -> Unit): BaseRecyclerViewHolder<Field>(v) {

    val questionText by bind<TextView>(R.id.field_take_text)
    val answerEditText by bind<EditText>(R.id.field_take_answer)
    val answerChoices by bind<RadioGroup>(R.id.field_take_choices)
    val answerImageContainer by bind<ConstraintLayout>(R.id.field_take_image_container)
    val answerImageSelectButton by bind<Button>(R.id.field_take_image_add_button)
    val answerImageText by bind<TextView>(R.id.field_take_image_text)
    val answerImages by bind<LinearLayout>(R.id.field_take_images)
    val answerSliderContainer by bind<ConstraintLayout>(R.id.field_take_slider_container)
    val answerSlider by bind<SeekBar>(R.id.field_take_slider_slider)
    val answerSliderText by bind<TextView>(R.id.field_take_slider_text)

    override fun onBind(data: Field) {
        questionText.text = data.fieldText

        answerEditText.hide()
        answerChoices.hide()
        answerImageContainer.hide()
        answerSliderContainer.hide()

        when(data.type) {
            FieldType.BLANK -> {
                answerEditText.show()
                getAnswer(data)?.let {
                    answerEditText.setText(it.choiceText)
                    setAnswer(data, it.choiceText)
                }
                RxTextView.afterTextChangeEvents(answerEditText).subscribe {
                    setAnswer(data, it.editable().toString())
                }
            }
            FieldType.TRUEFALSE, FieldType.MULTICHOICE -> {
                answerChoices.show()
                val choices = getChoicesForField(data)
                choices.forEach { choice ->
                    answerChoices.addView(RadioButton(answerChoices.context).apply {
                        text = choice.choiceText
                        onClick {
                            answerChoices.clearCheck()
                            this.isChecked = true
                            setAnswer(data, (it as RadioButton).text.toString())
                        }
                    })
                }
                choices.forEachIndexed { i, choice ->
                    if(getAnswer(data)?.choiceText == choice.choiceText) {
                        answerChoices.check(i)
                        (answerChoices.getChildAt(i) as RadioButton).isChecked = true
                        setAnswer(data, choice.choiceText)
                    }
                }
            }
            FieldType.IMAGE -> {
                answerImageContainer.show()
                answerImageText.text = "0 images selected"
                getAnswer(data)?.let {
                    answerImageText.text = answerImageText.context.getPlural(R.plurals.images_selected, it.choiceText.split(",").size)
                    getExistingImages(data) {
                        it.forEach { bitmap ->
                            addImage(bitmap)
                        }
                    }
                }
                answerImageSelectButton.onClick {
                    selectImages(data) { bitmap, count ->
                        addImage(bitmap)
                        answerImageText.text = answerImageText.context.getPlural(R.plurals.images_selected, count)
                    }
                }
            }
            FieldType.SLIDER -> {
                answerSliderContainer.show()
                answerSliderText.text = "1"
                getAnswer(data)?.let {
                    answerSlider.progress = it.choiceText.toInt()
                    answerSliderText.text = it.choiceText
                }
                RxSeekBar.userChanges(answerSlider).subscribe {
                    answerSliderText.text = it.toString()
                    setAnswer(data, it.toString())
                }
            }
        }
    }

    private fun addImage(bitmap: Bitmap) {
        answerImages.addView(ImageView(answerImages.context).apply {
            setImageBitmap(bitmap)
            setPadding(8, 8, 8, 8)
        })
    }
}

class FormRecyclerAdapter(fields: List<Field>, getChoicesForField: (field: Field) -> List<Choice>, setAnswer: (field: Field, answer: String) -> Unit, getAnswer: (field: Field) -> Choice?, getExistingImages: (field: Field, callback: (bitmaps: List<Bitmap>) -> Unit) -> Unit, selectImages: (field: Field, callback: (data: Bitmap, count: Int) -> Unit) -> Unit):
        BaseRecyclerViewAdapter<Field>(fields.toMutableList(), R.layout.item_field_take, {
            FormRecyclerViewHolder(it, getChoicesForField, setAnswer, getAnswer, getExistingImages, selectImages)
        })
