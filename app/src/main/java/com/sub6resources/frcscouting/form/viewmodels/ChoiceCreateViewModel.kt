package com.sub6resources.frcscouting.form.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.sub6resources.frcscouting.form.model.*
import com.sub6resources.utilities.BaseViewModel
import org.koin.standalone.inject

/**
 * Created by ryanberger on 12/3/17.
 */

class ChoiceCreateViewModel : BaseViewModel() {

    val choiceDao by inject<ChoiceDao>()
    val fieldDao by inject<FieldDao>()

    val fieldId = MutableLiveData<Long>()
    val field = Transformations.switchMap(fieldId) { id -> fieldDao.get(id) }
    val choices: LiveData<List<Choice>> = Transformations.switchMap(field, {
        MutableLiveData<List<Choice>>()
    })

    fun saveChoices() {
        choices.value?.let {
            choiceDao.createAll(it)
        }
    }

    fun getChoices() {
        field.value?.let {
            //choices = choiceDao.getChoiceForField(it.id)
        }
    }


    fun createChoice() {
        val c = Choice().apply {
            choiceText = ""
            field.value?.let {
                fieldId = field.value?.id as Long
            }
        }
        c.id = choiceDao.create(c)
        choiceDao.update(c)
    }

    fun createChoices(choices: List<Choice>) {
        choiceDao.createAll(choices.map { choice ->
            choice.apply {
                field.value?.let {
                    fieldId = field.value?.id as Long
                }
            }
        })
    }

    fun updateChoiceText(choice: Choice, text: String) {
        choice.apply {
            choiceText = text
        }
        choiceDao.update(choice)
    }

    fun createField(_formId: Long) {
        val f = Field().apply {
            type = FieldType.MULTICHOICE
            formId = _formId
        }

        fieldId.value = fieldDao.create(f)
    }

    fun setFieldMultipleChoice() {
        field.value?.apply {
            type = FieldType.MULTICHOICE
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

    fun saveField(_fieldText: String) {
        field.value?.let {
            if(_fieldText != "") {
                it.fieldText = _fieldText
            } else {
                it.fieldText = "[blank]"
            }
            fieldDao.update(it)
        }

    }

}