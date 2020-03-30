package com.hua.note.create

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
    var userDaoManager: UserDaoManager? = null
    private var userName: String? = ""
    private var flag: String? = ""

    companion object {
        fun start(context: Context?, flag: String) {
            val intent = Intent(context, CreateActivity::class.java)
            intent.putExtra("flag", flag)
            context!!.startActivity(intent)
        }

        fun start(context: Context?) {
            val intent = Intent(context, CreateActivity::class.java)
            context!!.startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initViewCreated(savedInstanceState: Bundle?) {
        userDaoManager = UserDaoManager.getInstance(applicationContext)
        val sharedPreferences: SharedPreferences? = getSharedPreferences("LoginData", 0)
        userName = sharedPreferences?.getString("loginName", "default")
        flag = intent?.getStringExtra("flag")
        if (flag != null) {
            val entity: NoteEntity = userDaoManager!!.findNoteByFlag(flag)
            edit_text.setText(entity.text)
            text_time.text = entity.time + "  |  " + entity.text.toCharArray().size + "字"
        } else {
            val date: String = DateFormat.yearMonthDayTime(Times.current())
            val weekDay: String = Tools.getWeekDays()
            text_time.text = "$date $weekDay"
        }

        edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val date: String = DateFormat.yearMonthDayTime(Times.current())
                val weekDay: String = Tools.getWeekDays()
                text_time.text = "$date $weekDay  |  " + edit_text.text.toString().trim()
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
        val changeTime: String?
        if (flag != null) {
            val noteEntity: NoteEntity = userDaoManager!!.findNoteByFlag(flag)
            changeTime =
                if (Tools.isWordChanged(noteEntity.text, edit_text.text.toString().trim())) {
                    val date: String = DateFormat.yearMonthDayTime(Times.current())
                    val weekDay: String = Tools.getWeekDays()
                    "$date $weekDay"
                } else {
                    noteEntity.time
                }
            userDaoManager!!.updateNote(flag, edit_text.text.toString().trim(), changeTime)
        } else {
            if (edit_text.text.toString().trim() != "") {
                val date: String = DateFormat.yearMonthDayTime(Times.current())
                val weekDay: String = Tools.getWeekDays()
                val time = "$date $weekDay"
                val noteEntity = NoteEntity(
                    Times.current().toString().trim(),
                    edit_text.text.toString().trim(),
                    time,
                    userName
                )
                userDaoManager!!.insertNote(noteEntity)
            }
        }
        EventBus.getDefault().post(MessageEvent("updateAdapter"))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
