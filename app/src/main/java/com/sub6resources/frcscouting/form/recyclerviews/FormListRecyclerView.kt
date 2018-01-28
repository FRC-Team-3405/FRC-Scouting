package com.sub6resources.frcscouting.form.recyclerviews

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.sub6resources.frcscouting.R
import com.sub6resources.frcscouting.form.model.Form
import com.sub6resources.utilities.*
import java.util.*

/**
 * Created by whitaker on 12/26/17.
 */
class FormListRecyclerViewHolder(v: View, val takeForm: (id: UUID) -> Unit, val responseClick: (id: UUID) -> Unit, val onClick: (id: UUID) -> Unit): BaseRecyclerViewHolder<Form>(v) {

    val name by bind<TextView>(R.id.recycler_form_name)
    val take_form by bind<Button>(R.id.recycler_take_form)
    val viewResponses by bind<Button>(R.id.recycler_form_responses)

    override fun onBind(data: Form) {
        name.text = data.name

        if (data.isDraft) {
            take_form.text = "Edit"
            viewResponses.hide()
        }

        take_form.onClick {
            takeForm(data.id)
        }

        viewResponses.onClick {
            responseClick(data.id)
        }




    }
}

class FormListRecyclerAdapter(forms: List<Form>, val takeForm: (id: UUID) -> Unit, val responseClick: (id: UUID) -> Unit, val onClick: (id: UUID) -> Unit):
        BaseRecyclerViewAdapter<Form>(forms.toMutableList(), R.layout.item_form, {
            FormListRecyclerViewHolder(it, takeForm, responseClick, onClick)
        })
