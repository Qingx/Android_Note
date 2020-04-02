package com.hua.note.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cn.wl.android.lib.ui.BaseActivity
import com.hua.note.R
import com.hua.note.data.NoteEntity
import com.hua.note.data.UserDaoManager
import com.hua.note.home.NoteAdapter
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity(), View.OnClickListener {
    private var userDaoManager: UserDaoManager? = null
    private var adapter: NoteAdapter? = null

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, SearchActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context!!.startActivity(intent)
        }
    }

    override fun initViewCreated(savedInstanceState: Bundle?) {
        img_top_left_search.setOnClickListener(this)
        userDaoManager = UserDaoManager.getInstance(applicationContext)

        edit_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (edit_search.text.toString().trim().toCharArray().isNotEmpty()) {
                    val noteList =
                        userDaoManager!!.findListLikeText(edit_search.text.toString().trim())
                    val noteTitleList =
                        userDaoManager!!.findListLikeTitle(edit_search.text.toString().trim())
                    /**
                     * size==0表示没有找到
                     */
                    if (noteTitleList.size != 0 || noteList.size != 0) {
                        val mergeList: MutableList<NoteEntity>? = noteTitleList
                        mergeList!!.removeAll(noteList)
                        mergeList.addAll(noteList)
                        mergeList.sort()
                        adapter = NoteAdapter(applicationContext, mergeList)
                        recyler_search.visibility = View.VISIBLE
                        recyler_search.adapter = adapter
                        recyler_search.layoutManager =
                            object : LinearLayoutManager(applicationContext) {}
                        text_result.visibility = View.INVISIBLE
                    } else {
                        /**
                         * 未找到
                         */
                        recyler_search.visibility = View.INVISIBLE
                        text_result.visibility = View.VISIBLE
                    }
                } else {
                    /**
                     * 未输入关键词
                     */
                    recyler_search.visibility = View.INVISIBLE
                    text_result.visibility = View.INVISIBLE
                }
            }
        })
    }

    override fun getLayoutResource(): Any {
        return R.layout.activity_search
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.img_top_left_search -> {
                onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
