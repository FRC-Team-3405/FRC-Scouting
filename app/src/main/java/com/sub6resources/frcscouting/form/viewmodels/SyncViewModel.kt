package com.sub6resources.frcscouting.form.viewmodels

import android.arch.lifecycle.LiveData
import com.google.protobuf.Empty
import com.sub6resources.frcscouting.form.SyncRepository
import com.sub6resources.frcscouting.form.model.ChoiceDao
import com.sub6resources.frcscouting.form.model.FieldDao
import com.sub6resources.frcscouting.form.model.FormDao
import com.sub6resources.frcscouting.formresponse.model.FieldResponseDao
import com.sub6resources.frcscouting.formresponse.model.FormResponseDao
import com.sub6resources.frcscouting.login.BasicNetworkState
import com.sub6resources.utilities.BaseViewModel
import org.koin.standalone.inject
import stats.*

/**
 * Created by whitaker on 2/17/18.
 */
class SyncViewModel: BaseViewModel() {
    private val syncRepository by inject<SyncRepository>()
    private val formDao by inject<FormDao>()
    private val fieldDao by inject<FieldDao>()
    private val choiceDao by inject<ChoiceDao>()
    private val formResponseDao by inject<FormResponseDao>()
    private val fieldResponseDao by inject<FieldResponseDao>()

    fun pushData(): LiveData<BasicNetworkState<FormSyncOuterClass.FormSync>> {
        val everything = FormSyncOuterClass.FormSync.newBuilder().apply {
            formDao.getFormsSync().map {
                addForms(FormOuterClass.Form.newBuilder().apply {
                    id = it.id.toString()
                    name = it.name
                    synced = true
                }.build())
            }
            fieldDao.getFieldsSync().map {
                addFields(FieldOuterClass.Field.newBuilder().apply {
                    id = it.id.toString()
                    fieldText = it.fieldText
                    form = it.formId.toString()
                    synced = true
                    type = FieldOuterClass.Field.Type.valueOf(it.type.toString())
                }.build())
            }
            choiceDao.getChoicesSync().map {
                addChoices(ChoiceOuterClass.Choice.newBuilder().apply {
                    id = it.id.toString()
                    fieldId = it.fieldId.toString()
                    choiceText = it.choiceText
                    synced = true
                }.build())
            }
            formResponseDao.getFormResponsesSync().map {
                addFormResponses(FormResponseOuterClass.FormResponse.newBuilder().apply {
                    id = it.id.toString()
                    form = it.formId.toString()
                    synced = true
                }.build())
            }
            fieldResponseDao.getFieldResponsesSync().map {
                addFieldResponses(FieldResponseOuterClass.FieldResponse.newBuilder().apply {
                    id = it.id.toString()
                    formResponse = it.formResponseId.toString()
                    field = it.fieldId.toString()
                    choice = it.choice.toString()
                    user = it.lastEditedBy.toString()
                    synced = true
                }.build())
            }
        }.build()
        return syncRepository.pushEverything(everything)
    }
    fun pullData(): LiveData<BasicNetworkState<FormSyncOuterClass.FormSync>> {
        return syncRepository.pullEverything(Empty.newBuilder().build())
    }
}