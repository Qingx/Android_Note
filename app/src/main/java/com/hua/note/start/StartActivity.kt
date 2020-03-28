package com.hua.note.start

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import cn.wl.android.lib.ui.BaseActivity
import com.hua.note.R
import com.hua.note.config.MessageEvent
import kotlinx.android.synthetic.main.activity_start.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class StartActivity : BaseActivity() {
    var fragments: ArrayList<Fragment> = ArrayList()

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, StartActivity::class.java)
            context?.startActivity(intent)
        }

        fun isShouldHideInput(view: View?, motionEvent: MotionEvent): Boolean {
            if (view != null && (view is EditText)) {
                val leftTop: IntArray = intArrayOf(0, 0)
                view.getLocationInWindow(leftTop)
                val left = leftTop[0]
                val top = leftTop[1]
                val bottom = top + view.height
                val right = left + view.width
                return !(motionEvent.x > left && motionEvent.x < right && motionEvent.y > top && motionEvent.y < bottom)
            }
            return false
        }

        fun hideInputMethod(context: Context, view: View?): Boolean =
                when (val imm: InputMethodManager? =
                        context.getSystemService(Context.INPUT_METHOD_SERVICE)
                                as? InputMethodManager) {
                    null -> false
                    else -> {
                        view?.clearFocus()
                        imm.hideSoftInputFromWindow(view?.windowToken, 0)
                    }
                }
    }

    override fun getLayoutResource(): Any {
        return R.layout.activity_start
    }

    override fun initViewCreated(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)

        fragments.add(LoginFragment.createFragment())
        fragments.add(SignFragment.createFragment())

        viewpager_start.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return fragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }
        }
        viewpager_start.offscreenPageLimit = 2
        viewpager_start.currentItem = 0

        viewpager_start.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0) {
                    text_title.setText(R.string.title_login)
                } else text_title.setText(R.string.title_sign)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun changePage(messageEvent: MessageEvent) {
        when (messageEvent.message) {
            "toLogin" -> {
                viewpager_start.currentItem = 0
            }
            "toSign" -> {
                viewpager_start.currentItem = 1
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (isShouldHideInput(view, ev)) {
                if (hideInputMethod(this, view)) {
                    return true
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}
