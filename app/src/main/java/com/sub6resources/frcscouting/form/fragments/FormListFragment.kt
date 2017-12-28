package com.sub6resources.frcscouting.form.fragments

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import com.sub6resources.frcscouting.R
import com.sub6resources.frcscouting.form.FormCreateActivity
import com.sub6resources.frcscouting.form.viewmodels.FieldListViewModel
import com.sub6resources.frcscouting.form.viewmodels.FormListViewModel
import com.sub6resources.frcscouting.form.views.FormListPagerAdapter
import com.sub6resources.frcscouting.form.views.FormListView
import com.sub6resources.utilities.BaseFragment
import com.sub6resources.utilities.bind
import com.sub6resources.utilities.onClick
import kotlinx.android.synthetic.main.fragment_formlist.*

/**
 * Created by whitaker on 12/26/17.
 */
class FormListFragment : BaseFragment() {
    override val fragLayout: Int = R.layout.fragment_formlist

    val viewModel by getViewModel(FormListViewModel::class.java)
    //val formViewModel by getSharedViewModel(FormViewModel::class.java)
    val fieldListViewModel by getSharedViewModel(FieldListViewModel::class.java)

    val viewPager by bind<ViewPager>(R.id.formlist_view_pager)
    val tabs by bind<TabLayout>(R.id.formlist_tab_layout)

    val formDrafts by lazy {
        FormListView (
                context = context!!,
                attributeSet = null,
                buttonClick = {
                    startActivity(Intent(activity, FormCreateActivity::class.java).apply {
                        putExtra("formId", it)
                    })
                },

                onClick = {

                }
        )
    }

    val forms by lazy {
        FormListView (
                context = context!!,
                attributeSet = null,
                buttonClick = {
                    //formViewModel.selectQuiz(it)
                    //addFragment(FormFragment())
                },

                onClick = {

                }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager.offscreenPageLimit = 2
        viewPager.adapter = FormListPagerAdapter(context!!, listOf(
                "Forms" to forms,
                "Drafts" to formDrafts
        ))

        viewPager.setCurrentItem(0, true)
        tabs.setupWithViewPager(viewPager)

        viewModel.forms.observe(this, Observer {
            it?.let {
                forms.bind(it)
            }
        })

        viewModel.drafts.observe(this, Observer {
            it?.let {
                formDrafts.bind(it)
            }
        })

        create_form_fab.onClick {
            val id = fieldListViewModel.createForm("[Draft]")
            startActivity(Intent(activity, FormCreateActivity::class.java).apply {
                putExtra("formId", id)
            })
        }

        swipe_container.setOnRefreshListener {
            //This is called when user swipes down on Form list
            swipe_container.isRefreshing = true
            //Sync Data
            viewModel.syncData()
            swipe_container.isRefreshing = false
        }
    }
}
