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

    val field: MutableLiveData<Field> = MutableLiveData()
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
            fieldId = field.value?.id as Long
        }
        c.id = choiceDao.create(c)
        choiceDao.update(c)
    }

    fun createChoices(choices: List<Choice>) {
        choiceDao.createAll(choices.map { choice ->
            choice.apply {
                fieldId = field.value?.id as Long
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
        val q = Field().apply {
            type = FieldType.MUILTICHOICE
            formId = _formId
        }

        q.id = fieldDao.create(q)
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
            fieldDao.update(it)
        }

    }

}