package com.sub6resources.frcscouting.formresponse.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.sub6resources.frcscouting.R
import com.sub6resources.frcscouting.form.FormActivity
import com.sub6resources.frcscouting.formresponse.recyclerviews.ResponseListRecyclerAdapter
import com.sub6resources.frcscouting.formresponse.viewmodels.ResponseListViewModel
import com.sub6resources.utilities.BaseFragment
import com.sub6resources.utilities.observeNotNull
import kotlinx.android.synthetic.main.fragment_responselist.*
import java.util.*

/**
 * Created by whitaker on 1/13/18.
 */
class ResponseListFragment: BaseFragment() {
    val viewModel by getSharedViewModel(ResponseListViewModel::class.java)

    override val fragLayout = R.layout.fragment_responselist
    override val toolbar = R.id.toolbar_responselist

    val responseListAdapter by lazy {
        ResponseListRecyclerAdapter(
                formResponses = listOf(),
                onButtonClick = { id ->
                    startActivity(Intent(baseActivity, FormActivity::class.java).apply {
                        putExtra("formId", viewModel.selectedFormId.value)
                        putExtra("formResponseId", id)
                        putExtra("editing", true)
                    })
                }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.selectForm(baseActivity.intent.extras["formId"] as UUID)

        baseActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recycler_responselist.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler_responselist.adapter = responseListAdapter

        observeNotNull(viewModel.formResponses) {
            responseListAdapter.replaceData(it)
        }
    }

    override fun onBackPressed() {
        baseActivity.finish()
    }
}