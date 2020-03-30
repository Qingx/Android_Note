package com.hua.note.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import cn.wl.android.lib.ui.BaseActivity
import com.hua.note.R
import com.hua.note.config.MessageEvent
import com.hua.note.create.CreateActivity
import com.hua.note.data.NoteEntity
import com.hua.note.data.UserDaoManager
import com.hua.note.start.StartActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : BaseActivity() {
    private var userDaoManager: UserDaoManager? = null
    private var list: List<NoteEntity>? = null
    private var adapter: NoteAdapter? = null
    private var userName: String? = null

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context!!.startActivity(intent)
        }
    }

    override fun initViewCreated(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)

        val sharedPreferences: SharedPreferences? = getSharedPreferences("LoginData", 0)
        userName = sharedPreferences?.getString("loginName", "default")
        userDaoManager = UserDaoManager.getInstance(applicationContext)
        list = userDaoManager?.findListByName(userName)
        adapter = object : NoteAdapter(applicationContext, list) {}
        recyler_notes.adapter = adapter
        recyler_notes.layoutManager = object : LinearLayoutManager(this) {}

        img_top_create.setOnClickListener {
            CreateActivity.start(applicationContext)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        text_login.setOnClickListener {
            StartActivity.start(applicationContext)
            finish()
        }

        text_logout.setOnClickListener {
            finish()
        }
    }

    override fun getLayoutResource(): Any {
        return R.layout.activity_main
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateAdapter(messageEvent: MessageEvent) {
        when (messageEvent.message) {
            "updateAdapter" -> {
                list = userDaoManager?.findListByName(userName)
                adapter!!.notifyItemInserted(0)
                adapter?.updateData(applicationContext, list)
                recyler_notes!!.layoutManager!!.scrollToPosition(0)
            }
        }
    }
}
