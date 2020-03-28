package com.hua.note.create

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import cn.wl.android.lib.ui.BaseActivity
import com.hua.note.R
import com.hua.note.data.NoteEntity
import com.hua.note.data.UserDaoManager
import kotlinx.android.synthetic.main.activity_create.*

class CreateActivity : BaseActivity() {
    var userDaoManager: UserDaoManager? = null

    companion object {
        fun start(context: Context?, flag: String) {
            val intent = Intent(context, CreateActivity::class.java)
            intent.putExtra("flag", flag)
            context!!.startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initViewCreated(savedInstanceState: Bundle?) {
        userDaoManager = UserDaoManager.getInstance(applicationContext)
        val flag: String = intent.getStringExtra("flag")
        val entity: NoteEntity = userDaoManager!!.findNoteByFlag(flag)
        text_text.text = entity.text + entity.time
        text_exit.setOnClickListener { onBackPressed() }
    }

    override fun getLayoutResource(): Any {
        return R.layout.activity_create
    }

}
