package com.sub6resources.frcscouting.form.viewmodels

import android.arch.lifecycle.LiveData
import com.sub6resources.frcscouting.form.model.Form
import com.sub6resources.frcscouting.form.model.FormDao
import com.sub6resources.utilities.BaseViewModel
import org.koin.standalone.inject

/**
 * Created by whitaker on 12/26/17.
 */
class FormListViewModel : BaseViewModel() {
    val formDao by inject<FormDao>()

    val forms: LiveData<List<Form>> by lazy { formDao.getForms() }
    val drafts: LiveData<List<Form>> by lazy { formDao.getFormDrafts() }

    fun syncData() {
        //TODO Sync Data With Backend Database
    }
}
