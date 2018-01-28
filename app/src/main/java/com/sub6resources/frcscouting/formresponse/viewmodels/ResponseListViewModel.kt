package com.sub6resources.frcscouting.formresponse.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.sub6resources.frcscouting.formresponse.model.FormResponseDao
import com.sub6resources.utilities.BaseViewModel
import org.koin.standalone.inject
import java.util.*

/**
 * Created by whitaker on 1/13/18.
 */
class ResponseListViewModel: BaseViewModel() {
    val formResponseDao by inject<FormResponseDao>()

    val selectedFormId = MutableLiveData<UUID>()
    val formResponses = Transformations.switchMap(selectedFormId, { formResponseDao.getFormResponses(it) })


    fun selectForm(id: UUID) {
        selectedFormId.value = id
    }
}