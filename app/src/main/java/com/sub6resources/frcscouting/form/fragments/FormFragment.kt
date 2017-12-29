package com.sub6resources.frcscouting.form.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.support.v7.widget.Toolbar
import com.sub6resources.frcscouting.R
import com.sub6resources.frcscouting.form.recyclerviews.FormRecyclerAdapter
import com.sub6resources.frcscouting.form.viewmodels.FormViewModel
import com.sub6resources.utilities.*
import kotlinx.android.synthetic.main.fragment_form.*

/**
 * Created by whitaker on 12/28/17.
 */
class FormFragment: BaseFragment() {
    override val fragLayout = R.layout.fragment_form

    val viewModel by getSharedViewModel(FormViewModel::class.java)
    val toolbar by bind<Toolbar>(R.id.quiz_toolbar)
    val formRecycler by bind<RecyclerView>(R.id.form_recycler)

    override fun onDestroy() {
        super.onDestroy()
    }

    val formAdapter by lazy {
        FormRecyclerAdapter(
                fields = listOf(),
                getChoicesForField = { field ->
                    viewModel.getChoicesForField(field.id)
                },
                setAnswer = { field, answer ->
                    viewModel.setAnswer(field, answer)
                },
                selectImages = { field ->
                    //This should return some base64 encoded images in the future.
                    "IMGS:"
                }
        )
    }

    override fun onBackPressed() {
        activity!!.dialog {
            title("Exit without submitting?")
            content("This form submission will be lost if you exit. Are you sure you want to exit?")
            positiveText("Okay")
            negativeText("Cancel")
            onPositive { _,_ ->
                viewModel.deleteFormResponse()
                activity!!.finish()
            }
        }.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        formRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        formRecycler.adapter = formAdapter

        activity?.let {
            it.intent?.let {
                viewModel.selectForm(it.getLongExtra("formId", 0))
                viewModel.selectFormResponse(it.getLongExtra("formResponseId", 0))
            }
        }

        observeNotNull(viewModel.form) {
            toolbar.title = it.name
        }

        observeNotNull(viewModel.fields) {
            formAdapter.replaceData(it)
        }

        //Just happily observing so values aren't null.
        observeNotNull(viewModel.fieldResponses) {}
        observeNotNull(viewModel.formResponse) {}

        button_submit.onClick {
            if(viewModel.validateFormCompleted()) {
                activity!!.finish()
            }
        }
    }

}