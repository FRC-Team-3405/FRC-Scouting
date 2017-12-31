package com.sub6resources.frcscouting

import com.sub6resources.frcscouting.login.fragments.LoginFragment
import com.sub6resources.utilities.BaseActivity

class MainActivity : BaseActivity(R.layout.activity_main) {

    override val fragmentTargets = R.id.main_activity_fragment_target
    override val landingFragment = LoginFragment()
}