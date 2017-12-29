package com.sub6resources.frcscouting.form.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.sub6resources.frcscouting.form.model.*
import com.sub6resources.frcscouting.formresponse.model.FieldResponse
import com.sub6resources.frcscouting.formresponse.model.FieldResponseDao
import com.sub6resources.frcscouting.formresponse.model.FormResponse
import com.sub6resources.frcscouting.formresponse.model.FormResponseDao
import com.sub6resources.utilities.BaseViewModel
import org.koin.standalone.inject

/**
 * Created by whitaker on 12/28/17.
 */
class FormViewModel : BaseViewModel() {

    val formDao by inject<FormDao>()
    val fieldDao by inject<FieldDao>()
    val choiceDao by inject<ChoiceDao>()
    val formResponseDao by inject<FormResponseDao>()
    val fieldResponseDao by inject<FieldResponseDao>()

    val selectedFormId = MutableLiveData<Long>()
    val formResponseId = MutableLiveData<Long>()

    val form: LiveData<Form> = Transformations.switchMap(selectedFormId) { id -> formDao.get(id) }
    val fields: LiveData<List<Field>> = Transformations.switchMap(selectedFormId) { id -> fieldDao.getFieldsForForm(id) }
    val formResponse: LiveData<FormResponse> = Transformations.switchMap(formResponseId) { id -> formResponseDao.get(id) }
    val fieldResponses: LiveData<List<FieldResponse>> = Transformations.switchMap(formResponseId) { id -> fieldResponseDao.getFieldResponses(id) }

    fun selectForm(id: Long) {
        selectedFormId.value = id
    }

    fun selectFormResponse(id: Long) {
        formResponseId.value = id
    }

    fun getChoicesForField(id: Long): List<Choice> {
        return choiceDao.getChoicesForField(id)
    }

    fun setAnswer(field: Field, answer: String) {
        val correspondingFieldResponse = getFieldResponseOfField(field)
        if(correspondingFieldResponse != null) {
            correspondingFieldResponse.apply {
                response = answer
            }
            fieldResponseDao.update(correspondingFieldResponse)
        } else {
            formResponseId.value?.let { formRId ->
                fieldResponseDao.create(FieldResponse().apply {
                    fieldId = field.id
                    formResponseId = formRId
                    response = answer
                })
            }
        }

    }

    fun appendToAnswer(field: Field, additionalAnswer: String) {
        val correspondingFieldResponse = getFieldResponseOfField(field)
        if(correspondingFieldResponse != null) {
            correspondingFieldResponse.apply {
                response += ","+additionalAnswer
            }
            fieldResponseDao.update(correspondingFieldResponse)
        } else {
            formResponseId.value?.let { formRId ->
                fieldResponseDao.create(FieldResponse().apply {
                    fieldId = field.id
                    formResponseId = formRId
                    response = additionalAnswer
                })
            }
        }
    }

    private fun getFieldResponseOfField(field: Field): FieldResponse? {
        fieldResponses.value?.let {
            for(fieldResponse in it) {
                if(fieldResponse.fieldId == field.id) {
                    return fieldResponse
                }
            }
        }
        return null
    }

    fun createFormResponse(_formId: Long): Long {
        val formRes = FormResponse().apply {
            formId = _formId
        }
        val id = formResponseDao.create(formRes)
        formResponseId.value = id
        return id
    }

    fun deleteFormResponse() {
        fieldResponses.value?.let {
            for(fieldResponse in it) {
                fieldResponseDao.delete(fieldResponse)
            }
        }
        formResponse.value?.let {
            formResponseDao.delete(it)
        }
    }

    fun validateFormCompleted(): Boolean {
        fieldResponses.value?.let {
            //Check to ensure that no Fields are blank.
            for(fieldResponse in it) {
                if(fieldResponse.response.isEmpty()) {
                    return false
                }
            }
            //Check that all Fields have a corresponding FieldResponse
            return it.size == fields.value?.size
        }
        return false
    }

}
