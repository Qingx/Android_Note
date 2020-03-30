package com.hua.note.create

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import cn.wl.android.lib.ui.BaseActivity
import cn.wl.android.lib.utils.Times
import com.hua.note.R
import com.hua.note.config.DateFormat
import com.hua.note.config.MessageEvent
import com.hua.note.config.Tools
import com.hua.note.data.NoteEntity
import com.hua.note.data.UserDaoManager
import kotlinx.android.synthetic.main.activity_create.*
import org.greenrobot.eventbus.EventBus

class CreateActivity : BaseActivity() {
    private var userDaoManager: UserDaoManager? = null
    private var userName: String = "default"
    private var id: Long? = null

    companion object {
        fun start(context: Context?, id: Long) {
            val intent = Intent(context, CreateActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("id", id)
            context!!.startActivity(intent)
        }

        fun start(context: Context?) {
            val intent = Intent(context, CreateActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context!!.startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initViewCreated(savedInstanceState: Bundle?) {
        userDaoManager = UserDaoManager.getInstance(applicationContext)
        id = intent.getLongExtra("id", 0)
        if (id != 0.toLong()) {
            val entity: NoteEntity = userDaoManager!!.findNoteById(id)
            edit_text.setText(entity.text)
            text_time.text =
                DateFormat.yearMonthDayTime(entity.time) + "  |  " + entity.text.toCharArray().size + "字"
        } else {
            text_time.text = Tools.getYMDTW()
        }

        edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                text_time.text = Tools.getYMDTW() + "  |  " + edit_text.text.toString().trim()
                    .toCharArray().size + "字"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        img_top_left.setOnClickListener {
            onBackPressed()
        }
    }

    override fun getLayoutResource(): Any {
        return R.layout.activity_create
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val changeTime: Long?
        if (id != 0.toLong()) {
            val noteEntity: NoteEntity = userDaoManager!!.findNoteById(id)
            changeTime =
                if (Tools.isWordChanged(noteEntity.text, edit_text.text.toString().trim())) {
                    Times.current()
                } else {
                    noteEntity.time
                }
            userDaoManager!!.updateNote(id, edit_text.text.toString().trim(), changeTime)
        } else {
            if (edit_text.text.toString().trim() != "") {
                val noteEntity = NoteEntity(
                    Times.current(),
                    Times.current(),
                    edit_text.text.toString().trim(),
                    "default",
                    userName
                )
                userDaoManager!!.insertNote(noteEntity)
            }
        }
        EventBus.getDefault().post(MessageEvent("updateAdapter"))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
