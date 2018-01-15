package com.sub6resources.frcscouting.login.fragments

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.sub6resources.frcscouting.MainActivity
import com.sub6resources.frcscouting.R
import com.sub6resources.frcscouting.login.LoginFailure
import com.sub6resources.frcscouting.login.LoginSuccess
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

        observeNotNull(viewModel.getOfflineUsers()) {
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
            loadingDialog.show()

            viewModel.signIn(username, password).observe(this, Observer { loginResponse ->
                when(loginResponse) {
                    is LoginSuccess -> {
                        loadingDialog.dismiss()
                        baseActivity.sharedPreferences.edit {
                            //Set this user as the current user
                            putString("currentUser", loginResponse.user.username)
                        }.apply()
                        startActivity(Intent(baseActivity, MainActivity::class.java))
                    }
                    is LoginFailure -> {
                        //Display error message
                        when {
                            loginResponse.error == "HTTP 400 Bad Request" -> edittext_password.error = "Username or password is incorrect."
                            loginResponse.error.contains("Unable to resolve host") -> edittext_password.error = "Connection is unavailable. Please use an existing user."
                            loginResponse.error == "UserDoesNotExistInLocalDatabase" -> return@Observer //Ignore this error.
                            else -> edittext_password.error = loginResponse.error
                        }
                        loadingDialog.dismiss()
                    }
                }
            })
        }
    }
}