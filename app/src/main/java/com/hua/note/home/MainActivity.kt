package com.hua.note.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cn.wl.android.lib.ui.BaseActivity
import com.hua.note.R
import com.hua.note.config.MessageEvent
import com.hua.note.create.CreateActivity
import com.hua.note.data.NoteEntity
import com.hua.note.data.StickyEntity
import com.hua.note.data.UserDaoManager
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : BaseActivity(), View.OnClickListener {
    private var userDaoManager: UserDaoManager? = null
    private var defaultList: List<NoteEntity>? = null
    private var stickyList: List<StickyEntity>? = null
    private var defaultAdapter: NoteAdapter? = null
    private var stickyAdapter: StickyAdapter? = null
    private val default: String = "default"
    private val sticky: String = "sticky"

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context!!.startActivity(intent)
        }
    }

    override fun initViewCreated(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)

        userDaoManager = UserDaoManager.getInstance(applicationContext)

        /**
         * 置顶列表
         */
        stickyList = userDaoManager?.findStickyListByName(sticky)
        stickyAdapter = object : StickyAdapter(applicationContext, stickyList) {}
        recyler_sticky.adapter = stickyAdapter
        recyler_sticky.layoutManager = object : LinearLayoutManager(this) {}

        /**
         * 默认列表
         */
        defaultList = userDaoManager?.findListByName(default)
        defaultAdapter = object : NoteAdapter(applicationContext, defaultList) {}
        recyler_notes.adapter = defaultAdapter
        recyler_notes.layoutManager = object : LinearLayoutManager(this) {}

        img_top_create.setOnClickListener(this)
    }

    override fun getLayoutResource(): Any {
        return R.layout.activity_main
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateAdapter(messageEvent: MessageEvent) {
        when (messageEvent.message) {
            "updateAdapter" -> {
                /**
                 * 默认便签列表
                 */
                defaultList = userDaoManager?.findListByName(default)
                defaultAdapter?.updateData(applicationContext, defaultList)
                /**
                 * 置顶便签列表
                 */
                stickyList = userDaoManager?.findStickyListByName(sticky)
                stickyAdapter?.updateData(applicationContext, stickyList)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.img_top_create -> {
                CreateActivity.start(applicationContext)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
    }
}
