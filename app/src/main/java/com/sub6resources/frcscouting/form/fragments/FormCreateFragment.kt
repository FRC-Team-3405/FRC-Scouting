package com.sub6resources.frcscouting.form.fragments

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.sub6resources.frcscouting.R
import com.sub6resources.frcscouting.form.recyclerviews.FieldListRecyclerAdapter
import com.sub6resources.frcscouting.form.viewmodels.ChoiceCreateViewModel
import com.sub6resources.frcscouting.form.viewmodels.FieldListViewModel
import com.sub6resources.utilities.*

/*
 * Created by Matthew on 12/2/17.
 */
class FormCreateFragment : BaseFragment() {
    override val fragLayout: Int = R.layout.fragment_formcreate

    val viewModel by getSharedViewModel(FieldListViewModel::class.java)
    val choiceCreateViewModel by getSharedViewModel(ChoiceCreateViewModel::class.java)

    val fieldRecycler by bind<RecyclerView>(R.id.recycler_field_list)
    val saveForm by bind<Button>(R.id.submit_form_button)
    val formTitle by bind<EditText>(R.id.add_form_title)
    val addField by bind<FloatingActionButton>(R.id.add_field)

    val fieldAdapter by lazy {
        FieldListRecyclerAdapter(listOf(),
                onClick = { field ->
                    choiceCreateViewModel.fieldId.value = field.id
                    addFragment(FieldCreateFragment())
                }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            it.intent?.let {
                viewModel.selectForm(it.getLongExtra("formId", 0))
            }
        }

        fieldRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        fieldRecycler.adapter = fieldAdapter

        observeNotNull(viewModel.fields) {
            fieldAdapter.replaceData(it)
        }

        observeNotNull(viewModel.form) { form ->
            if(form.isDraft)
                formTitle.setText(form.name)
        }

        addField.onClick {
            //Create a new field in the view model.
            viewModel.form.value?.let {
                choiceCreateViewModel.createField(it.id)
                addFragment(FieldCreateFragment())
            }
        }

        saveForm.onClick {
            //Save the form
            viewModel.saveForm(formTitle.getString())
            activity?.finish()
        }
    }
}


