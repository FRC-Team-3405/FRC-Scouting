package com.sub6resources.frcscouting.login.fragments

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.sub6resources.frcscouting.MainActivity
import com.sub6resources.frcscouting.R
import com.sub6resources.frcscouting.login.BasicNetworkState
import com.sub6resources.frcscouting.login.TokenMessage
import com.sub6resources.frcscouting.login.model.User
import com.sub6resources.frcscouting.login.recyclerviews.UserRecyclerAdapter
import com.sub6resources.frcscouting.login.viewmodels.LoginViewModel
import com.sub6resources.utilities.*
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * Created by whitaker on 12/30/17.
 */
class LoginFragment: BaseFragment() {
    override val fragLayout = R.layout.fragment_login
    override val toolbar = R.id.login_toolbar

    val viewModel by getViewModel(LoginViewModel::class.java)

    val userAdapter by lazy {
        UserRecyclerAdapter(users = listOf(), selectUser = {
            activity!!.sharedPreferences.edit {
                putString("currentUser", it.username)

                startActivity(Intent(baseActivity, MainActivity::class.java))
            }.apply()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_existing_users.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler_existing_users.adapter = userAdapter

        observeNotNull(viewModel.offlineUsers) {
            userAdapter.replaceData(it)
        }

        button_signin.onClick {

            val username = edittext_username.text.toString()
            val password = edittext_password.text.toString()

            val loadingDialog = activity!!.dialog {
                title("Signing In...")
                content("This should only take a moment.")
                progress(true, 0)
            }


            if(viewModel.signIn(username, password)) {
                viewModel.user.observe(this, Observer<BasicNetworkState<TokenMessage>> { loginResponse: BasicNetworkState<TokenMessage>? ->
                    when(loginResponse) {
                        is BasicNetworkState.Success<TokenMessage> -> {
                            loadingDialog.dismiss()
                            baseActivity.sharedPreferences.edit {
                                putString("currentUser", username)
                            }.apply()
                            startActivity(Intent(baseActivity, MainActivity::class.java))
                        }
                        is BasicNetworkState.Error -> {
                            edittext_password.error = loginResponse.message
                            loadingDialog.dismiss()
                        }
                        is BasicNetworkState.Loading -> {
                            loadingDialog.show()
                        }
                    }
                })
            } else {
                edittext_password.error = "Username and password are required"
            }

        }

        text_signup.onClick {
            addFragment(SignUpFragment())
        }
    }
}