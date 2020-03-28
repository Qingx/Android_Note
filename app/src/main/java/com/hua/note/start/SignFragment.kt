package com.hua.note.start

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import cn.wl.android.lib.ui.BaseFragment
import cn.wl.android.lib.utils.Times
import com.hua.note.R
import com.hua.note.config.DateFormat
import com.hua.note.config.MessageEvent
import com.hua.note.config.Tools
import com.hua.note.data.NoteEntity
import com.hua.note.data.UserDaoManager
import com.hua.note.data.UserEntity
import kotlinx.android.synthetic.main.fragment_sign.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Iaovy on 2020/3/23 10:42
 *@email Cymbidium@outlook.com
 */
class SignFragment : BaseFragment() {
    private var userManager: UserDaoManager? = null

    companion object {
        fun createFragment(): SignFragment {
            return SignFragment()
        }
    }

    override fun getLayoutResource(): Any {
        return R.layout.fragment_sign
    }

    override fun initViewCreated(view: View?, savedInstanceState: Bundle?) {
        userManager = UserDaoManager.getInstance(context)
        layout_sign.setOnClickListener { sign() }
        text_toLogin.setOnClickListener {
            EventBus.getDefault().post(MessageEvent("toLogin"))
            userVisibleHint = isVisible
        }

        edit_sign_account.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus -> layout_sign_account.isPressed = hasFocus }
        edit_sign_password.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus -> layout_sign_password.isPressed = hasFocus }
        edit_sign_confirm.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus -> layout_sign_confirm.isPressed = hasFocus }
    }

    @SuppressLint("WrongConstant")
    private fun sign() {
        if (isNameAndPwdValid()) {
            val userName = edit_sign_account.text.toString().trim()
            val userPwd = edit_sign_password.text.toString().trim()
            when (userManager!!.findUserByName(userName)) {
                1 -> Tools.myToast(context, "用户名已存在")
                0 -> {
                    val newUser = object : UserEntity(userName, userPwd) {}
                    userManager!!.insert(newUser)
                    Tools.myToast(context, "注册成功")
                    EventBus.getDefault().post(MessageEvent("toLogin"))
                }
            }
        }
    }

    private fun isNameAndPwdValid(): Boolean {
        if (edit_sign_account.text.toString().trim() == "") {
            Tools.myToast(context, "请输入用户名")
            return false
        }
        if (edit_sign_password.text.toString().trim() == "") {
            Tools.myToast(context, "请输入密码")
            return false
        }
        if (edit_sign_password.text.toString().trim() != edit_sign_confirm.text.toString().trim()) {
            Tools.myToast(context, "请重复密码")
            return false
        }
        return true
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (edit_sign_account != null) {
            edit_sign_account.setText("")
            edit_sign_password.setText("")
            edit_sign_confirm.setText("")
        }
        if (edit_sign_password != null) {
            edit_sign_account.setText("")
            edit_sign_password.setText("")
            edit_sign_confirm.setText("")
        }
        if (edit_sign_confirm != null) {
            edit_sign_account.setText("")
            edit_sign_password.setText("")
            edit_sign_confirm.setText("")
        }
    }
}