package com.sub6resources.frcscouting.form.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.sub6resources.frcscouting.R
import com.sub6resources.frcscouting.form.model.Choice
import com.sub6resources.frcscouting.form.model.FieldType
import com.sub6resources.frcscouting.form.recyclerviews.ChoiceListRecyclerAdapter
import com.sub6resources.frcscouting.form.viewmodels.ChoiceCreateViewModel
import com.sub6resources.utilities.BaseFragment
import com.sub6resources.utilities.bind
import com.sub6resources.utilities.getString
import com.sub6resources.utilities.onClick
import kotlinx.android.synthetic.main.fragment_fieldcreate.*

/**
 * Created by whitaker on 12/2/17.
 */
class FieldCreateFragment : BaseFragment() {
    override val fragLayout: Int = R.layout.fragment_fieldcreate

    val viewModel by getSharedViewModel(ChoiceCreateViewModel::class.java)

    private val questionText by bind<EditText>(R.id.field_text)
    private val fieldType by bind<Spinner>(R.id.field_type_spinner)
    private val choiceRecycler by bind<RecyclerView>(R.id.recycler_answer_list)
    private val saveQuestion by bind<Button>(R.id.save_field)

    val choiceAdapter by lazy {
        ChoiceListRecyclerAdapter(listOf(), fieldType)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        choiceRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        choiceRecycler.adapter = choiceAdapter

        viewModel.field.observe(this, Observer {
            it?.let {
                questionText.setText(it.fieldText)

                fieldType.setSelection(
                        when (viewModel.field.value?.type) {
                            FieldType.MULTICHOICE -> 0
                            FieldType.TRUEFALSE -> 1
                            FieldType.BLANK -> 2
                            FieldType.IMAGE -> 3
                            FieldType.SLIDER -> 4
                            else -> 0
                        }
                )
            }
        })

        viewModel.choices.observe(this, Observer {
            if (it != null && !it.isEmpty()) {
                choiceAdapter.replaceData(it)
            }
        })

        fieldType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, selectedItemIndex: Int, p3: Long) {
                when (selectedItemIndex) {
                    0 -> { //Multiple Choice
                        if (viewModel.field.value?.type != FieldType.MULTICHOICE) {
                            choiceAdapter.removeAll()
                            add_choice_fab.show()
                        }
                        viewModel.setFieldMultipleChoice()
                        if (choiceAdapter.dataSet.size == 0) {
                            choiceAdapter.add(Choice())
                        }
                    }
                    1 -> { //True/False
                        viewModel.setFieldTrueFalse()
                        choiceAdapter.apply {
                            removeAll()
                            add(Choice("True"))
                            add(Choice("False"))
                        }
                        add_choice_fab.hide()
                    }
                    2 -> { //Short answer
                        choiceAdapter.apply {
                            removeAll()
                            add(Choice("User will answer this question."))
                        }
                        add_choice_fab.hide()
                        viewModel.setFieldShortAnswer()
                    }
                    3 -> { //Image
                        choiceAdapter.apply {
                            removeAll()
                            add(Choice("Users can submit multiple images to this field."))
                        }
                        add_choice_fab.hide()
                        viewModel.setFieldImage()
                    }
                    4 -> { //Slider
                        choiceAdapter.apply {
                            removeAll()
                            add(Choice("Users will select a value between one and ten."))
                        }
                        add_choice_fab.hide()
                        viewModel.setFieldSlider()
                    }
                }
            }

        }

        add_choice_fab.onClick {
            choiceAdapter.add(Choice())
        }

        saveQuestion.onClick {
            if (questionText.length() > 0) {
                viewModel.saveField(questionText.getString())
            } else {
                viewModel.saveField("[Question]")
            }
            viewModel.createChoices(choiceAdapter.dataSet)
            popFragment()
        }
    }
}

