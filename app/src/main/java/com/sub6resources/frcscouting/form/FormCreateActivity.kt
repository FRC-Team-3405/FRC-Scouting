package com.sub6resources.frcscouting.form

import com.sub6resources.frcscouting.R
import com.sub6resources.frcscouting.form.fragments.FormCreateFragment
import com.sub6resources.utilities.BaseActivity
import com.sub6resources.utilities.BaseFragment

/*
 * Created by Matthew on 12/2/17.
 */
class FormCreateActivity : BaseActivity(R.layout.activity_formcreate) {

    override val landingFragment: BaseFragment = FormCreateFragment()
    override val fragmentTargets: Int = R.id.main_fragment_target
}