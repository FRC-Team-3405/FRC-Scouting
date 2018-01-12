package com.sub6resources.frcscouting.form.views

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.sub6resources.frcscouting.R
import com.sub6resources.frcscouting.form.model.Form
import com.sub6resources.frcscouting.form.recyclerviews.FormListRecyclerAdapter
import com.sub6resources.utilities.bind
import java.util.UUID

/**
 * Created by whitaker on 12/26/17.
 */
class FormListView @JvmOverloads constructor(context: Context,
                                             attributeSet: AttributeSet? = null,
                                             buttonClick: (id: UUID) -> Unit,
                                             onClick: (id: Long) -> Unit) : LinearLayout(context, attributeSet) {

    val formAdapter by lazy {
        FormListRecyclerAdapter(
                forms = listOf(),
                takeForm = buttonClick,
                onClick = onClick
        )
    }

    val formRecycler by bind<RecyclerView>(R.id.formlist_recycler)



    init {
        LayoutInflater.from(context).inflate(R.layout.view_formlist, this)
        formRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        formRecycler.adapter = formAdapter

    }

    fun bind(list: List<Form>) {
        formAdapter.replaceData(list)
    }


}
