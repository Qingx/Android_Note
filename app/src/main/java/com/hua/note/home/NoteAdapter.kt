package com.hua.note.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import cn.wl.android.lib.ui.BaseActivity
import cn.wl.android.lib.utils.Times
import com.chad.library.adapter.base.BaseViewHolder
import com.hua.note.R
import com.hua.note.config.DateFormat
import com.hua.note.config.MessageEvent
import com.hua.note.config.Tools.Companion.cutStr
import com.hua.note.config.Tools.Companion.getWeekDays
import com.hua.note.create.CreateActivity.Companion.start
import com.hua.note.data.NoteEntity
import com.hua.note.data.UserDaoManager
import org.greenrobot.eventbus.EventBus

/**
 * Created by Iaovy on 2020/4/1 11:31
 *@email Cymbidium@outlook.com
 */
class NoteAdapter(context: Context, entities: MutableList<NoteEntity>) :
    RecyclerView.Adapter<NoteAdapter.ItemViewHolder?>() {
    private var entities: MutableList<NoteEntity>
    private var context: Context
    private var userDaoManager: UserDaoManager? = null

    init {
        this.context = context
        this.entities = entities
    }

    fun updateData(context: Context, entities: MutableList<NoteEntity>) {
        this.context = context
        this.entities = entities
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        userDaoManager = UserDaoManager.getInstance(context)
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.item_recyler_note, parent, false
        )
        return ItemViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val noteEntity = entities[position]
        val name = noteEntity.name

        if (name == "default") {
            holder.stickyView.visibility = View.INVISIBLE
        } else holder.stickyView.visibility = View.VISIBLE

        if (noteEntity.title == "") {
            holder.text.text = cutStr(noteEntity.text.replace("", ""), 18)
        } else {
            holder.text.text = noteEntity.title.replace("", "")
        }

        holder.time.text =
            DateFormat.yearMonthDayTime(noteEntity.time) + " " + getWeekDays()

        holder.itemView.setOnClickListener {
            start(context, noteEntity.id, "default")
//            (v.context as BaseActivity).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        holder.itemView.setOnLongClickListener { v: View ->
            /**
             * 创建dialog
             */
            val builder = AlertDialog.Builder(v.context)
            val view = View.inflate(v.context, R.layout.dialog_note, null)
            builder.setView(view)
            builder.setCancelable(true)
            val dialog = builder.create()
            dialog.show()

            val stickyLayout: ConstraintLayout = view.findViewById(R.id.layout_sticky)
            val stickyText = view.findViewById<TextView>(R.id.text_sticky)
            if (name == "default") {
                stickyText.text = "置顶这条便签"
            } else {
                stickyText.text = "取消置顶"
            }
            /**
             * 置顶按钮
             */
            stickyLayout.setOnClickListener {
                if (name == "default") {
                    userDaoManager!!.stickyNote(noteEntity.id, Times.current())
                    EventBus.getDefault().post(MessageEvent("updateAdapter"))
                } else {
                    userDaoManager!!.deleteSticky(noteEntity.id, Times.current())
                    EventBus.getDefault().post(MessageEvent("updateAdapter"))
                }
                dialog.dismiss()
            }
            /**
             * 删除按钮
             */
            val removeLayout: ConstraintLayout = view.findViewById(R.id.layout_remove)
            removeLayout.setOnClickListener {
                userDaoManager!!.deleteNote(noteEntity)
                entities.remove(noteEntity)
                notifyItemRemoved(position)
                notifyDataSetChanged()
                dialog.dismiss()
            }
            false
        }
    }

    override fun getItemCount(): Int {
        return entities.size
    }

    inner class ItemViewHolder(view: View) :
        BaseViewHolder(view) {
        val text: TextView = view.findViewById(R.id.text_item_name)
        val time: TextView = view.findViewById(R.id.text_item_time)
        val stickyView: View = view.findViewById(R.id.view_sticky)
    }
}
