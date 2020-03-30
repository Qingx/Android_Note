package com.hua.note.create

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
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
        val userName: String? = sharedPreferences?.getString("loginName", "default")
        val flag: String? = intent?.getStringExtra("flag")
        if (flag != null) {
            val entity: NoteEntity = userDaoManager!!.findNoteByFlag(flag)
            text_text.setText(entity.text)
        }

        text_exit.setOnClickListener {
            val changeTime: String?
            if (flag != null) {
                val noteEntity: NoteEntity = userDaoManager!!.findNoteByFlag(flag)
                changeTime =
                    if (Tools.isWordChanged(noteEntity.text, text_text.text.toString().trim())) {
                        val date: String = DateFormat.yearMonthDayTime(Times.current())
                        val weekDay: String = Tools.getWeekDays()
                        date + "" + weekDay
                    } else {
                        noteEntity.time
                    }
                userDaoManager!!.updateNote(flag, text_text.text.toString().trim(), changeTime)
            } else {
                val date: String = DateFormat.yearMonthDayTime(Times.current())
                val weekDay: String = Tools.getWeekDays()
                val time: String = date + "" + weekDay
                val noteEntity = NoteEntity(
                    Times.current().toString().trim(),
                    text_text.text.toString().trim(),
                    time,
                    userName
                )
                userDaoManager!!.insertNote(noteEntity)
            }
            EventBus.getDefault().post(MessageEvent("updateAdapter",""))
            onBackPressed()
        }
    }

    override fun getLayoutResource(): Any {
        return R.layout.activity_create
    }

}
