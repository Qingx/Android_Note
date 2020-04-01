package com.hua.note.home

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.wl.android.lib.utils.Times
import com.hua.note.R
import com.hua.note.data.NoteEntity
import com.hua.note.data.UserDaoManager

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val sharedPreferences: SharedPreferences? = getSharedPreferences("LoginStatus", 0)
        when (sharedPreferences?.getString("loginStatus", "0")) {
            "0" -> {
                val userDaoManager: UserDaoManager = UserDaoManager.getInstance(applicationContext)
                val defaultEntity =
                    NoteEntity(Times.current(), Times.current(), "欢迎使用便签", "default", "default")
                userDaoManager.insertNote(defaultEntity)
                val editor: SharedPreferences.Editor? = sharedPreferences.edit()
                editor!!.putString("loginStatus", "1")
                editor.apply()
                MainActivity.start(applicationContext)
            }
            "1" -> {
                MainActivity.start(applicationContext)
            }
        }
    }
}
