package com.yds.login

import android.content.Context
import android.os.IBinder
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.crystallake.base.activity.DataBindingActivity
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.resources.RouterPath
import com.yds.login.databinding.ActivityLoginBinding
import com.yds.login.vm.LoginViewModel


@Route(path = RouterPath.LOGIN_ACTIVITY)
class LoginActivity : DataBindingActivity<ActivityLoginBinding, LoginViewModel>() {

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_login)
    }

    override fun initData() {
        super.initData()
        mBinding?.seeImg?.setOnClickListener {
            mViewModel.eyeCanSeeLiveModel.value = !(mViewModel.eyeCanSeeLiveModel.value ?: false)
            if (mViewModel.eyeCanSeeLiveModel.value == true) {
                Glide.with(this@LoginActivity).load(R.drawable.ic_password_can_see)
                    .into(it as ImageView)
                mBinding?.password?.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                Glide.with(this@LoginActivity).load(R.drawable.ic_password_not_can_see)
                    .into(it as ImageView)
                mBinding?.password?.inputType =
                    (InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT)
            }
            mBinding?.password?.setSelection(mBinding?.password?.text?.toString()?.length ?: 0)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v?.windowToken)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun isShouldHideKeyboard(v: View?, event: MotionEvent?): Boolean {
        if (v != null && v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val right = left + v.width
            val bottom = top + v.height
            return !((event?.x ?: 0f) > left && (event?.x ?: 0f) < right
                    && (event?.y ?: 0f) > top && (event?.y ?: 0f) < bottom)
        }
        return false
    }

    private fun hideKeyboard(token: IBinder?) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        token?.let {
            imm?.hideSoftInputFromWindow(it, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}