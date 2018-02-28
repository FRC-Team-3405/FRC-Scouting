package com.sub6resources.frcscouting.form.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.graphics.Bitmap
import com.sub6resources.frcscouting.form.model.*
import com.sub6resources.frcscouting.formresponse.model.FieldResponse
import com.sub6resources.frcscouting.formresponse.model.FieldResponseDao
import com.sub6resources.frcscouting.formresponse.model.FormResponse
import com.sub6resources.frcscouting.formresponse.model.FormResponseDao
import com.sub6resources.utilities.BaseViewModel
import org.koin.standalone.inject
import java.util.*

/**
 * Created by whitaker on 12/28/17.
 */
class FormViewModel : BaseViewModel() {

    val formDao by inject<FormDao>()
    val fieldDao by inject<FieldDao>()
    val choiceDao by inject<ChoiceDao>()
    val formResponseDao by inject<FormResponseDao>()
    val fieldResponseDao by inject<FieldResponseDao>()
    val imageDao by inject<ImageDao>()

    val selectedFormId = MutableLiveData<UUID>()
    val formResponseId = MutableLiveData<UUID>()

    val form: LiveData<Form> = Transformations.switchMap(selectedFormId) { id -> formDao.get(id) }
    val fields: LiveData<List<Field>> = Transformations.switchMap(selectedFormId) { id -> fieldDao.getFieldsForForm(id) }
    val formResponse: LiveData<FormResponse> = Transformations.switchMap(formResponseId) { id -> formResponseDao.get(id) }
    val fieldResponses: LiveData<List<FieldResponse>> = Transformations.switchMap(formResponseId) { id -> fieldResponseDao.getFieldResponses(id) }

    fun selectForm(id: UUID) {
        selectedFormId.value = id
    }

    fun selectFormResponse(id: UUID) {
        formResponseId.value = id
    }

    fun getChoicesForField(id: UUID): List<Choice> {
        return choiceDao.getChoicesForField(id)
    }

    fun getAnswer(field: Field): Choice? {
        getFieldResponseOfField(field)?.let {
            return choiceDao.get(it.choice)
        }
        return null
    }

    fun setAnswer(field: Field, answer: String, userId: UUID) {
        val correspondingFieldResponse = getFieldResponseOfField(field)
        if(correspondingFieldResponse != null) {
            correspondingFieldResponse.apply {
                choice = choiceDao.getChoicesForField(field.id).find { it.choiceText == answer }?.id ?: generateNewChoice(field, answer)
                lastEditedBy = userId
            }
            fieldResponseDao.update(correspondingFieldResponse)
        } else {
            formResponseId.value?.let { formRId ->
                fieldResponseDao.create(FieldResponse().apply {
                    id = UUID.randomUUID()
                    fieldId = field.id
                    formResponseId = formRId
                    choice = choiceDao.getChoicesForField(field.id).find { it.choiceText == answer }?.id ?: generateNewChoice(field, answer)
                    lastEditedBy = userId
                })
            }
        }

    }

    fun appendToAnswer(field: Field, additionalAnswer: String, userId: UUID): Int {
        val correspondingFieldResponse = getFieldResponseOfField(field)
        if(correspondingFieldResponse != null) {
            imageDao.create(Image(UUID.randomUUID(), additionalAnswer, correspondingFieldResponse.id))
            return imageDao.getNumberOfImages(getFieldResponseOfField(field)?.id!!)
        } else {
            formResponseId.value?.let { formRId ->
                val newFieldResponse = FieldResponse().apply {
                    id = UUID.randomUUID()
                    fieldId = field.id
                    formResponseId = formRId
                    choice = generateNewChoice(field, "0")
                    lastEditedBy = userId
                }
                fieldResponseDao.create(newFieldResponse)
                imageDao.create(Image(UUID.randomUUID(), additionalAnswer, newFieldResponse.id))
            }
            return 1
        }
    }

    fun generateNewChoice(field: Field, answer: String): UUID {
        val choice = Choice(answer).apply {
            fieldId = field.id
            id = UUID.randomUUID()
        }
        choiceDao.create(choice)
        return choice.id
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

    fun createFormResponse(_formId: UUID): UUID {
        val formRes = FormResponse().apply {
            formId = _formId
            id = UUID.randomUUID()
        }
        formResponseDao.create(formRes)
        formResponseId.value = formRes.id
        return formRes.id
    }

    fun deleteFormResponse() {
        formResponse.value?.let {
            formResponseDao.delete(it)
        }
    }

    fun validateFormCompleted(): Boolean {
        fieldResponses.value?.let {
            //Check to ensure that no Fields are blank.
            for(fieldResponse in it) {
                val choice = choiceDao.get(fieldResponse.choice)
                if (choice.choiceText == "") {
                    return false
                }
                if (choice.choiceText.length > 140) {
                    return false
                }
            }

            //Check that all Fields have a corresponding FieldResponse
            return it.size == fields.value?.size
        }
        return false
    }

    fun getImages(field: Field): List<Image> {
        val id = getFieldResponseOfField(field)?.id
        return imageDao.getByFieldResponse(id!!)
    }

    fun getNumberOfImages(field: Field): Int {
        return imageDao.getNumberOfImages(getFieldResponseOfField(field)?.id!!)
    }

}
