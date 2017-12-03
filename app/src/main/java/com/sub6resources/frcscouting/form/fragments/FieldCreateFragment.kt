package com.sub6resources.frcscouting.form.fragments

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
import com.sub6resources.utilities.BaseFragment
import com.sub6resources.utilities.bind
import com.sub6resources.utilities.getString
import com.sub6resources.utilities.onClick
import kotlinx.android.synthetic.main.fragment_fieldcreate.*

/**
 * Created by whitaker on 12/2/17.
 */
class QuestionCreateFragment : BaseFragment() {
    override val fragLayout: Int = R.layout.fragment_fieldcreate

    //val viewModel by getSharedViewModel(ChoiceCreateViewModel::class.java)

    val questionText by bind<EditText>(R.id.field_text)
    val questionType by bind<Spinner>(R.id.field_type_spinner)
    val choiceRecycler by bind<RecyclerView>(R.id.recycler_answer_list)
    val saveQuestion by bind<Button>(R.id.save_field)

    val choiceAdapter by lazy {
        ChoiceListRecyclerAdapter(listOf(), questionType)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        choiceRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        choiceRecycler.adapter = choiceAdapter

        /*viewModel.field.observe(this, Observer {
            it?.let {
                fieldText.setText(it.questionText)

                fieldType.setSelection(
                        when (viewModel.question.value?.type) {
                            FieldType.MUILTICHOICE -> 0
                            FieldType.TRUEFALSE -> 1
                            FieldType.BLANK -> 2
                            else -> 0
                        }
                )
                viewModel.choices.removeObservers(this)
                viewModel.getChoices()

                viewModel.choices.observe(this, Observer {
                    if (it != null && !it.isEmpty()) {
                        choiceAdapter.replaceData(it)
                    }
                })

            }
        })*/




        questionType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, selectedItemIndex: Int, p3: Long) {
                when (selectedItemIndex) {
                    0 -> { //Multiple Choice
                        if (viewModel.field.value.type == FieldType.TRUEFALSE) {
                            choiceAdapter.removeAll()
                            add_choice_fab.show()
                        }
                        viewModel.setQuestionMultipleChoice()
                        if (choiceAdapter.dataSet.size == 0) {
                            choiceAdapter.add(Choice())
                        }
                    }
                    1 -> { //True/False
                        if(viewModel.field.value.type != FieldType.TRUEFALSE) {
                            viewModel.setQuestionTrueFalse()
                            choiceAdapter.apply {
                                removeAll()
                                add(Choice("True"))
                                add(Choice("False"))
                            }
                            add_choice_fab.hide()
                        }
                    }
                    2 -> { //Short answer
                        if (viewModel.field.value.type == FieldType.TRUEFALSE) {
                            choiceAdapter.removeAll()
                            add_choice_fab.show()
                        }
                        viewModel.setQuestionShortAnswer()
                        if (choiceAdapter.dataSet.size == 0) {
                            choiceAdapter.add(Choice())
                        }
                    }
                    3 -> { //Image

                    }
                }
            }

        }

        add_choice_fab.onClick {
            choiceAdapter.add(Choice())
        }

        saveQuestion.onClick {
            if (questionText.length() > 0) {
                viewModel.saveQuestion(questionText.getString())
            } else {
                viewModel.saveQuestion("[Question]")
            }
            viewModel.createChoices(choiceAdapter.dataSet)
            popFragment()
        }
    }
}

