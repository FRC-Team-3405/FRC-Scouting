package com.sub6resources.frcscouting.login.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.sub6resources.frcscouting.R
import com.sub6resources.frcscouting.form.fragments.FormListFragment
import com.sub6resources.frcscouting.login.LoginFailure
import com.sub6resources.frcscouting.login.LoginSuccess
import com.sub6resources.frcscouting.login.User
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

                switchFragment(FormListFragment())
            }.apply()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_existing_users.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler_existing_users.adapter = userAdapter

        //Get existing offline users
        val users = ArrayList<User>()
        activity!!.sharedPreferences.getString("users", "").split(",").forEach { username ->
            val token = activity!!.sharedPreferences.getString(username, "")
            if(username.isNotEmpty() && token.isNotEmpty()) {
                users.add(User(username, token))
            }
        }
        userAdapter.replaceData(users)


        button_signin.onClick {

            val username = edittext_username.text.toString()
            val password = edittext_password.text.toString()

            if(username.isBlank()) {
                edittext_username.error = "Please enter your username"
                return@onClick
            }

            //If user is already signed in, don't try and sign in again.
            if(baseActivity.sharedPreferences.getString("users", "").contains(username)) {
                baseActivity.sharedPreferences.edit {
                    putString("currentUser", username)
                }.apply()
                switchFragment(FormListFragment())
                return@onClick
            }

            if(password.isBlank()) {
                edittext_password.error = "Please enter your password"
                return@onClick
            }

            val loadingDialog = activity!!.dialog {
                title("Signing In...")
                content("This should only take a moment.")
                progress(true, 0)
            }
            loadingDialog.show()

            viewModel.signIn(username, password) { result ->
                loadingDialog.dismiss()
                when(result) {
                    is LoginSuccess -> {
                        val listOfUsers = activity!!.sharedPreferences.getString("users", "")
                        //Save user to list of signed in users
                        activity!!.sharedPreferences.edit {
                            if(listOfUsers.isEmpty()) {
                                putString("users", username)
                            } else if(!listOfUsers.contains(username)) {
                                putString("users", listOfUsers+","+username)
                            }

                            //Save this user's token
                            putString(result.username, result.token)

                            //Set this user as the current user
                            putString("currentUser", result.username)
                        }.apply()

                        switchFragment(FormListFragment())
                    }
                    is LoginFailure -> {
                        //Display error message
                        edittext_password.error = result.error
                    }
                }
            }
        }
    }
}