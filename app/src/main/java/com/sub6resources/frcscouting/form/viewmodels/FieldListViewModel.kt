package com.sub6resources.frcscouting.form.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.sub6resources.frcscouting.form.model.Field
import com.sub6resources.frcscouting.form.model.FieldDao
import com.sub6resources.frcscouting.form.model.Form
import com.sub6resources.frcscouting.form.model.FormDao
import com.sub6resources.utilities.BaseViewModel
import org.koin.standalone.inject
import java.util.*

/**
 * Created by Matthew Whitaker on 12/9/17.
 */
class FieldListViewModel: BaseViewModel() {
    val formDao by inject<FormDao>()
    val fieldDao by inject<FieldDao>()

    val formId = MutableLiveData<UUID>()
    val form: LiveData<Form> = Transformations.switchMap(formId) { formId -> formDao.get(formId) }
    var fields: LiveData<List<Field>> = Transformations.switchMap(formId) { formId -> fieldDao.getFieldsForForm(formId) }


    fun saveForm(name: String) {
        form.value?.let {
            it.isDraft = false
            it.name = name

            formDao.update(it)
        }
    }

    fun createForm(name: String): UUID {
        val f = Form(name).apply {
            isDraft = true
            id = UUID.randomUUID()
        }
        formDao.create(f)
        formId.value = f.id
        return f.id
    }

    fun selectForm(id: UUID) {
        formId.value = id
    }
}