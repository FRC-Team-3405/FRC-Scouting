package com.sub6resources.frcscouting.form.recyclerviews

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.sub6resources.frcscouting.R
import com.sub6resources.frcscouting.form.model.Form
import com.sub6resources.utilities.BaseRecyclerViewAdapter
import com.sub6resources.utilities.BaseRecyclerViewHolder
import com.sub6resources.utilities.bind
import com.sub6resources.utilities.onClick

/**
 * Created by whitaker on 12/26/17.
 */
class FormListRecyclerViewHolder(v: View, val takeForm: (id: Long) -> Unit, val onClick: (id: Long) -> Unit): BaseRecyclerViewHolder<Form>(v) {

    val name by bind<TextView>(R.id.recycler_form_name)
    val take_form by bind<Button>(R.id.recycler_take_form)

    override fun onBind(data: Form) {
        name.text = data.name

        if (data.isDraft) {
            take_form.text = "Edit"
        }

        take_form.onClick {
            takeForm(data.id)
        }
    }
}

class FormListRecyclerAdapter(forms: List<Form>, val takeForm: (id: Long) -> Unit, val onClick: (id: Long) -> Unit):
        BaseRecyclerViewAdapter<Form>(forms.toMutableList(), R.layout.item_form, {
            FormListRecyclerViewHolder(it, takeForm, onClick)
        })
