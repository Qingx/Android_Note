package com.hua.note.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cn.wl.android.lib.ui.BaseActivity
import cn.wl.android.lib.utils.Times
import com.hua.note.R
import com.hua.note.config.*
import com.hua.note.data.NoteEntity
import com.hua.note.data.UserDaoManager

import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus

class MainActivity : BaseActivity(), View.OnClickListener {
    var list: List<NoteEntity>? = null

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, MainActivity::class.java)
            context!!.startActivity(intent)
        }
    }

    override fun initViewCreated(savedInstanceState: Bundle?) {
        val userDaoManager = UserDaoManager.getInstance(applicationContext)
        val sharedPreferences: SharedPreferences = getSharedPreferences("LoginData", 0)
        val userName: String? = sharedPreferences.getString("loginName", "")
        Log.i("loginName", userName)
        list = userDaoManager!!.findListByName(userName)
        val adapter: NoteAdapter = object : NoteAdapter(applicationContext, list) {}
        recyler_notes.layoutManager = object : LinearLayoutManager(this) {}
        recyler_notes.adapter = adapter

        img_top_create.setOnClickListener {
            val date: String = DateFormat.yearMonthDayTime(Times.current())
            val weekDay: String = Tools.getWeekDays()
            val time: String = date + "" + weekDay
            val noteEntity = NoteEntity(Times.current().toString().trim(), "欢迎", time, userName)
            userDaoManager.insertNote(noteEntity)
            list = userDaoManager.findListByName(userName)
            adapter.updateData(applicationContext, list)
        }
        text_logout.setOnClickListener(this)

    }

    override fun getLayoutResource(): Any {
        return R.layout.activity_main
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.text_logout -> {
                EventBus.getDefault().post(MessageEvent("clearStatus"))
                finish()
            }
        }
    }

}
