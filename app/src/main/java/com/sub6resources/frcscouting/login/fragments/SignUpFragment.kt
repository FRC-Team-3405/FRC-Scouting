package com.sub6resources.frcscouting.login.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import com.sub6resources.frcscouting.R
import com.sub6resources.frcscouting.login.BasicNetworkState
import com.sub6resources.frcscouting.login.UserMessage
import com.sub6resources.frcscouting.login.viewmodels.SignUpViewModel
import com.sub6resources.utilities.BaseFragment
import com.sub6resources.utilities.dialog
import com.sub6resources.utilities.onClick
import kotlinx.android.synthetic.main.fragment_signup.*

/**
 * Created by 59485 on 2/15/18.
 */
class SignUpFragment() : BaseFragment() {
    override val fragLayout = R.layout.fragment_signup
    override val toolbar = R.id.signup_toolbar

    val viewModel by getViewModel(SignUpViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edittext_username.requestFocus()

        button_signup.onClick {
            val username = edittext_username.text.toString()
            val password = edittext_password.text.toString()
            val firstName = edittext_firstname.text.toString()
            val lastName = edittext_lastname.text.toString()
            val emailAddress = edittext_email.text.toString()

            val loadingDialog = baseActivity.dialog {
                title("Signing Up...")
                content("This should only take a moment.")
                progress(true, 0)
            }

            if(viewModel.signUp(username, password, firstName, lastName, emailAddress)) {
                viewModel.createdUser.observe(this, Observer { signUpResponse: BasicNetworkState<UserMessage>? ->
                    when(signUpResponse) {
                        is BasicNetworkState.Success<UserMessage> -> {
                            loadingDialog.dismiss()
                            popFragment()
                        }
                        is BasicNetworkState.Error -> {
                            edittext_email.error = signUpResponse.message
                            loadingDialog.dismiss()
                        }
                        is BasicNetworkState.Loading -> {
                            loadingDialog.show()
                        }
                    }
                })
            } else {
                edittext_email.error = "All fields are required"
            }
        }
    }
}