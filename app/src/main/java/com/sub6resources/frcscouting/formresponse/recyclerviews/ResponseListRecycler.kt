package com.sub6resources.frcscouting.formresponse.recyclerviews

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.sub6resources.frcscouting.R
import com.sub6resources.frcscouting.formresponse.model.FormResponse
import com.sub6resources.utilities.BaseRecyclerViewAdapter
import com.sub6resources.utilities.BaseRecyclerViewHolder
import com.sub6resources.utilities.bind
import com.sub6resources.utilities.onClick
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by whitaker on 1/13/18.
 */
class ResponseListRecyclerViewHolder(v: View, val onButtonClick: (id: UUID) -> Unit): BaseRecyclerViewHolder<FormResponse>(v) {

    val responseName by bind<TextView>(R.id.response_form_name)
    val responseDate by bind<TextView>(R.id.response_date)
    val editResponseButton by bind<Button>(R.id.response_edit_form)

    override fun onBind(data: FormResponse) {
        responseName.text = data.id.toString()
        responseDate.text = data.id.toString()
        editResponseButton.onClick {
            onButtonClick(data.id)
        }
    }
}

class ResponseListRecyclerAdapter(formResponses: List<FormResponse>, onButtonClick: (id: UUID) -> Unit):
        BaseRecyclerViewAdapter<FormResponse>(formResponses.toMutableList(), R.layout.item_response, {
            ResponseListRecyclerViewHolder(it, onButtonClick)
        })