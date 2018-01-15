package com.sub6resources.frcscouting.formresponse

import com.sub6resources.frcscouting.R
import com.sub6resources.frcscouting.formresponse.fragments.ResponseListFragment
import com.sub6resources.utilities.BaseActivity

/**
 * Created by whitaker on 1/13/18.
 */
class ResponseActivity: BaseActivity(R.layout.activity_response) {
    override val fragmentTargets = R.id.response_activity_fragment_target
    override val landingFragment = ResponseListFragment()
}