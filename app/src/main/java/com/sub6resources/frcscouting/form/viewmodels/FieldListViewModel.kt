package com.sub6resources.frcscouting.form.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sub6resources.frcscouting.form.model.Field
import com.sub6resources.frcscouting.form.model.Form

/**
 * Created by Matthew Whitaker on 12/9/17.
 */
class FieldListViewModel: ViewModel() {
    var form: LiveData<Form> = MutableLiveData()
    var fields: LiveData<List<Field>> = MutableLiveData()


    fun saveForm(name: String) {
        form.value?.let {
            it.isDraft = false
            it.name = name

            //db.formDao.updateForm(it)
        }
    }

    fun createQuiz(name: String) {
        val f = Form(name).apply {
            isDraft = true
        }

        //q.id = db.formDao.createForm(f)
        //form = db.formDao.getForm(f.id)
        //questions = db.fieldDao.getFieldsForForm(f.id)
    }

    fun selectQuiz(id: Long) {
        //form = db.formDao.getForm(id)
        //fields = db.fieldDao.getFieldsForForm(id)
    }
}