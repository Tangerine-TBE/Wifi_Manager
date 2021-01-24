package com.example.module_user.ui.activity
import com.example.module_base.base.BaseVmViewActivity
import com.example.module_base.utils.setToolBar
import com.example.module_base.utils.showToast
import com.example.module_base.utils.toolbarEvent
import com.example.module_user.R
import com.example.module_user.databinding.ActivityLoginBinding
import com.example.module_user.viewmodel.LoginViewModel

class LoginActivity : BaseVmViewActivity<ActivityLoginBinding,LoginViewModel>() {
    override fun getViewModelClass(): Class<LoginViewModel> {
        return LoginViewModel::class.java
    }
    override fun getLayoutView(): Int = R.layout.activity_login


    override fun initView() {
        setToolBar(this,"登录",binding.loginToolbar)
    }

    override fun initEvent() {
        binding.apply {
            loginToolbar.toolbarEvent(this@LoginActivity){}
            toLogin.setOnClickListener {
                val number = numberInclude.number.text.trim().toString()
                val pwd = pwdInInclude.pwd.text.trim().toString()
                viewModel.toLocalLogin(number,pwd)
            }
        }
    }

    override fun observerData() {
        viewModel.apply {
            loginState.observe(this@LoginActivity,{
                showToast(if (it) "登录成功" else "登录失败")
            })

        }
    }
}