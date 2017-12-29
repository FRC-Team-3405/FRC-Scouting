package com.sub6resources.frcscouting.form

import com.sub6resources.frcscouting.R
import com.sub6resources.frcscouting.form.fragments.FormFragment
import com.sub6resources.utilities.BaseActivity
import com.sub6resources.utilities.BaseFragment

/**
 * Created by ryanberger on 11/22/17.
 */
class FormActivity: BaseActivity(R.layout.activity_form) {

    override val landingFragment = FormFragment()
    override val fragmentTargets = R.id.form_activity_fragment_target
}
