package com.sub6resources.frcscouting.form.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.sub6resources.frcscouting.form.model.Field
import com.sub6resources.frcscouting.form.model.FieldDao
import com.sub6resources.frcscouting.form.model.Form
import com.sub6resources.frcscouting.form.model.FormDao
import com.sub6resources.utilities.BaseViewModel
import org.koin.standalone.inject

/**
 * Created by Matthew Whitaker on 12/9/17.
 */
class FieldListViewModel: BaseViewModel() {
    val formDao by inject<FormDao>()
    val fieldDao by inject<FieldDao>()

    var form: LiveData<Form> = MutableLiveData()
    var fields: LiveData<List<Field>> = MutableLiveData()


    fun saveForm(name: String) {
        form.value?.let {
            it.isDraft = false
            it.name = name

            formDao.update(it)
        }
    }

    fun createForm(name: String) {
        val f = Form(name).apply {
            isDraft = true
        }

        f.id = formDao.create(f)
        form = formDao.get(f.id)
        fields = fieldDao.getFieldsForForm(f.id)
    }

    fun selectForm(id: Long) {
        form = formDao.get(id)
        fields = fieldDao.getFieldsForForm(id)
    }
}