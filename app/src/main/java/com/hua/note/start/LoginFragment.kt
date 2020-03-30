package com.hua.note.start

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import cn.wl.android.lib.ui.BaseFragment
import com.hua.note.R
import com.hua.note.config.App
import com.hua.note.config.DataStorage
import com.hua.note.config.MessageEvent
import com.hua.note.config.Tools
import com.hua.note.data.UserDaoManager
import com.hua.note.home.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by Iaovy on 2020/3/23 10:42
 *@email Cymbidium@outlook.com
 */
class LoginFragment : BaseFragment() {
    var userManager: UserDaoManager? = null
    var sharedPreferences: SharedPreferences? = null

    companion object {
        fun createFragment(): LoginFragment {
            return LoginFragment()
        }
    }

    override fun getLayoutResource(): Any {
        return R.layout.fragment_login
    }

    override fun initViewCreated(view: View?, savedInstanceState: Bundle?) {

        userManager = UserDaoManager.getInstance(context)
        sharedPreferences = activity!!.getSharedPreferences("LoginData", 0)
        checkbox_login.isChecked = true
        val name: String? = sharedPreferences?.getString("loginName", "")
        val pwd: String? = sharedPreferences?.getString("loginPwd", "")
        val choseRemember: Boolean = sharedPreferences!!.getBoolean("rememberCheck", false)
        if (choseRemember) {
            edit_login_account.setText(name)
            edit_login_password.setText(pwd)
            checkbox_login.isChecked = true
        }

        layout_login.setOnClickListener {
            login()
        }
        text_create.setOnClickListener {
            userVisibleHint = isVisible
            EventBus.getDefault().post(MessageEvent("toSign"))
        }
        edit_login_account.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus -> layout_login_account.isPressed = hasFocus }
        edit_login_password.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus -> layout_login_password.isPressed = hasFocus }

    }

    private fun login() {
        if (isNameAndPwdValid()) {
            val userName = edit_login_account.text.toString().trim()
            val userPwd = edit_login_password.text.toString().trim()
            val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
            when (userManager!!.findUserByName(userName)) {
                0 -> Tools.myToast(context, "用户名不存在")
                1 -> when (userManager!!.isPwdRightByName(userName, userPwd)) {
                    0 -> Tools.myToast(context, "密码不正确")
                    1 -> {
                        editor.putString("loginName", userName)
                        editor.putString("loginPwd", userPwd)
                        editor.putString("loginStatus", "1")
                        if (checkbox_login.isChecked) {
                            editor.putBoolean("rememberCheck", true)
                        } else editor.putBoolean("rememberCheck", false)
                        editor.apply()
                        MainActivity.start(context)
                        activity!!.finish()
                    }
                }
            }
        }
    }

    private fun isNameAndPwdValid(): Boolean {
        if (edit_login_account.text.toString().trim() == "") {
            Tools.myToast(context, "请输入用户名")
            return false
        }
        if (edit_login_password.text.toString().trim() == "") {
            Tools.myToast(context, "请输入密码")
            return false
        }
        return true
    }
}