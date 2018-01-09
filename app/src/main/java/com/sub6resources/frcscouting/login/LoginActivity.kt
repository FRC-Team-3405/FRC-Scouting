package com.sub6resources.frcscouting.login

import com.sub6resources.utilities.BaseActivity
import com.sub6resources.frcscouting.R
import com.sub6resources.frcscouting.login.fragments.LoginFragment

/**
 * Created by whitaker on 1/1/18.
 */
class LoginActivity: BaseActivity(R.layout.activity_login) {
    override val landingFragment = LoginFragment()
    override val fragmentTargets = R.id.login_activity_fragment_target
}