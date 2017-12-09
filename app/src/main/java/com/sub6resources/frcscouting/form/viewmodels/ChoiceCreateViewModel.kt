package com.sub6resources.frcscouting.form.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sub6resources.frcscouting.form.model.Choice
import com.sub6resources.frcscouting.form.model.Field
import com.sub6resources.frcscouting.form.model.FieldType

/**
 * Created by ryanberger on 12/3/17.
 */

class ChoiceCreateViewModel : ViewModel() {
    //TODO: Create db stuff.

    val field: MutableLiveData<Field> = MutableLiveData()
    val choices: MutableLiveData<List<Choice>> = MutableLiveData()

    fun saveChoices() {
        choices.value?.let {
            //db.choiceDao.createAllChoices(it)
        }
    }

    fun getChoices() {
        field.value?.let {
            //choices = db.choiceDao.getChoiceForQuestion(it.id)
        }
    }


    fun createChoice() {
        val c = Choice().apply {
            choiceText = ""
            fieldId = field.value?.id as Long
        }
        //c.id = db.choiceDao.createChoice(c)
        //db.choiceDao.updateChoice(c)
    }

    fun createChoices(choices: List<Choice>) {
        /*db.choiceDao.createAllChoices(choices.map { choice ->
            choice.apply {
                fieldId = field.value?.id as Long
            }
        })*/
    }

    fun updateChoiceText(choice: Choice, text: String) {
        choice.apply {
            choiceText = text
        }
        //db.choiceDao.updateChoice(choice)
    }

    fun createField(_formId: Long) {
        val q = Field().apply {
            type = FieldType.MUILTICHOICE
            formId = _formId
        }

        //q.id = db.questionDao.createQuestion(q)
        field.value = q
    }

    fun setFieldMultipleChoice() {
        field.value?.apply {
            type = FieldType.MUILTICHOICE
        }
    }

    fun setFieldTrueFalse() {
        field.value?.apply {
            type = FieldType.TRUEFALSE
        }
    }

    fun setFieldShortAnswer() {
        field.value?.apply {
            type = FieldType.BLANK
        }
    }

    fun setFieldImage() {
        field.value?.apply {
            type = FieldType.IMAGE
        }
    }

    fun saveQuestion(_fieldText: String) {
        field.value?.let {
            if(_fieldText != "") {
                it.fieldText = _fieldText
            } else {
                it.fieldText = "[blank]"
            }
            //db.questionDao.updateQuestion(it)
        }

    }

}