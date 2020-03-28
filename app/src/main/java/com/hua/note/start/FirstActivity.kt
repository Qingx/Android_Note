package com.hua.note.start

import android.content.SharedPreferences
import android.os.Bundle
import cn.wl.android.lib.ui.BaseActivity
import com.hua.note.R
import com.hua.note.config.MessageEvent
import com.hua.note.home.MainActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class FirstActivity : BaseActivity() {
    var sharedPreferences: SharedPreferences? = null
    override fun initViewCreated(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)

        sharedPreferences = getSharedPreferences("LoginData", 0)
        val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
        val status: String? = sharedPreferences?.getString("loginStatus", "")
        if (status == "1") {
            MainActivity.start(applicationContext)
            finish()
        } else {
            editor.putString("loginStatus", "1")
            editor.apply()
            StartActivity.start(applicationContext)
            finish()
        }
    }

    override fun getLayoutResource(): Any {
        return R.layout.activity_first
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun clearStatus(messageEvent: MessageEvent) {
        when (messageEvent.message) {
            "clearStatus" -> {
                val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
                editor.putString("loginStatus", "0")
                editor.apply()
            }
        }
    }
}
