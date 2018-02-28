package com.sub6resources.frcscouting.form.fragments

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.sub6resources.frcscouting.R
import com.sub6resources.frcscouting.form.FormActivity
import com.sub6resources.frcscouting.form.FormCreateActivity
import com.sub6resources.frcscouting.form.viewmodels.FieldListViewModel
import com.sub6resources.frcscouting.form.viewmodels.FormListViewModel
import com.sub6resources.frcscouting.form.viewmodels.FormViewModel
import com.sub6resources.frcscouting.form.viewmodels.SyncViewModel
import com.sub6resources.frcscouting.form.views.FormListPagerAdapter
import com.sub6resources.frcscouting.form.views.FormListView
import com.sub6resources.frcscouting.formresponse.ResponseActivity
import com.sub6resources.frcscouting.login.BasicNetworkState
import com.sub6resources.frcscouting.login.LoginActivity
import com.sub6resources.frcscouting.login.fragments.LoginFragment
import com.sub6resources.utilities.BaseFragment
import com.sub6resources.utilities.bind
import com.sub6resources.utilities.onClick
import kotlinx.android.synthetic.main.fragment_formlist.*
import kotlinx.coroutines.experimental.async

/**
 * Created by whitaker on 12/26/17.
 */
class FormListFragment : BaseFragment() {
    override val fragLayout = R.layout.fragment_formlist
    override val toolbar = R.id.landing_toolbar
    override val menu = R.menu.menu_formlist

    val viewModel by getViewModel(FormListViewModel::class.java)
    val formViewModel by getSharedViewModel(FormViewModel::class.java)
    val fieldListViewModel by getSharedViewModel(FieldListViewModel::class.java)
    val syncViewModel by getViewModel(SyncViewModel::class.java)

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
                responseClick = {},
                onClick = {}
        )
    }

    val forms by lazy {
        FormListView (
                context = context!!,
                attributeSet = null,
                buttonClick = {
                    startActivity(Intent(activity, FormActivity::class.java).apply {
                        putExtra("formId", it)
                        putExtra("formResponseId", formViewModel.createFormResponse(it))
                    })
                },
                responseClick = {
                    startActivity(Intent(baseActivity, ResponseActivity::class.java).apply {
                        putExtra("formId", it)
                    })
                },
                onClick = {
                    startActivity(Intent(baseActivity, FormCreateActivity::class.java).apply {
                        putExtra("formId", it)
                    })
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
            async {
                //Sync Data
                syncViewModel.pushData().observe(this@FormListFragment, Observer {
                    when (it) {
                        is BasicNetworkState.Success -> {
                            syncViewModel.pullData().observe(this@FormListFragment, Observer {
                                when(it) {
                                    is BasicNetworkState.Success -> {
                                        swipe_container.isRefreshing = false
                                        Toast.makeText(baseActivity, "Success!!", Toast.LENGTH_LONG).show()
                                    }
                                    is BasicNetworkState.Error -> {
                                        swipe_container.isRefreshing = false
                                        Toast.makeText(baseActivity, "Error Pulling: " + it.message, Toast.LENGTH_LONG).show()
                                        Log.e("GRPC Sync", "Error Pulling: " + it.message)
                                    }
                                    is BasicNetworkState.Loading -> {

                                    }
                                }
                            })

                        }
                        is BasicNetworkState.Error -> {
                            swipe_container.isRefreshing = false
                            Toast.makeText(baseActivity, "Error Pushing: " + it.message, Toast.LENGTH_LONG).show()
                            Log.e("GRPC Sync", "Error Pushing: " + it.message)
                        }
                        is BasicNetworkState.Loading -> {

                        }
                    }
                })
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.switch_user -> {
                startActivity(Intent(baseActivity, LoginActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
