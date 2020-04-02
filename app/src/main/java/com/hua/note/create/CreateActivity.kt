package com.hua.note.create

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
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

class CreateActivity : BaseActivity(), View.OnClickListener {
    private var userDaoManager: UserDaoManager? = null
    private var userName: String = "default"
    private var id: Long? = null
    private var name: String? = null

    companion object {
        fun start(context: Context?, id: Long, name: String) {
            val intent = Intent(context, CreateActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("id", id)
            intent.putExtra("name", name)
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
        name = intent.getStringExtra("name")

        if (id != 0.toLong()) {
            /**
             * 修改便签
             */
            val entity: NoteEntity = userDaoManager!!.findNoteById(id)
            edit_text.setText(entity.text)
            edit_title.setText(entity.title)
            text_time.text =
                DateFormat.yearMonthDayTime(entity.time) + "  |  " + entity.text.toCharArray().size + "字"
        } else {
            /**
             * 新建便签
             */
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

        img_top_left.setOnClickListener(this)
        img_theme.setOnClickListener(this)
    }

    override fun getLayoutResource(): Any {
        return R.layout.activity_create
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val changeTime: Long?
        if (id != 0.toLong()) {
            /**
             * 修改便签
             */
            val noteEntity: NoteEntity = userDaoManager!!.findNoteById(id)
            if (edit_title.text.toString().trim() != "" || edit_text.text.toString().trim() != "") {
                changeTime =
                    if (Tools.isWordChanged(
                            noteEntity.text,
                            edit_text.text.toString().trim()
                        ) || Tools.isWordChanged(
                            noteEntity.title,
                            edit_title.text.toString().trim()
                        )
                    ) {
                        Times.current()
                    } else {
                        noteEntity.time
                    }
                userDaoManager!!.updateNote(
                    id,
                    edit_text.text.toString().trim(),
                    changeTime,
                    edit_title.text.toString().trim()
                )
            } else {
                userDaoManager!!.deleteNote(noteEntity)
                EventBus.getDefault().post(MessageEvent("updateAdapter"))
            }
        } else {
            /**
             * 新建便签
             */
            if (edit_text.text.toString().trim() != "" || edit_title.text.toString().trim() != "") {
                val noteEntity = NoteEntity(
                    Times.current(),
                    Times.current(),
                    edit_text.text.toString().trim(),
                    "default",
                    userName,
                    edit_title.text.toString().trim()
                )
                userDaoManager!!.insertNote(noteEntity)
            }
        }
        EventBus.getDefault().post(MessageEvent("updateAdapter"))
//        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.img_top_left -> onBackPressed()
            R.id.img_theme -> Tools.myToast(applicationContext, "敬请期待")
        }
    }
}
