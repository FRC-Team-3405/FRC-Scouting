package com.sub6resources.frcscouting

import android.content.Intent
import com.sub6resources.frcscouting.form.FormCreateActivity
import com.sub6resources.utilities.BaseActivity

class MainActivity : BaseActivity(R.layout.activity_main) {

    override fun setUp() {
        startActivity(Intent(this, FormCreateActivity::class.java))
    }
}
