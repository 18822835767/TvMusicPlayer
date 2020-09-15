package com.example.tvmusicplayer.login

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.bean.User
import com.example.tvmusicplayer.util.LoginStatusManager

class LoginActivity : AppCompatActivity(),LoginContract.OnView, View.OnClickListener{

    private lateinit var presenter : LoginContract.Presenter
    private lateinit var userName : EditText
    private lateinit var passWord : EditText
    private lateinit var loginButton : Button
    private lateinit var progressBar : ProgressBar
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    
        initView()
        initData()
        initEvent()
    }
    
    fun initView(){
        userName = findViewById(R.id.username)
        passWord = findViewById(R.id.password)
        loginButton = findViewById(R.id.login)
        progressBar = findViewById(R.id.progress_bar)
    }
    
    fun initData(){
        LoginPresenter(this)
    }
    
    fun initEvent(){
        loginButton.setOnClickListener(this)
    }

    override fun showSuccess(user: User) {
        LoginStatusManager.alreadyLogin = true
        LoginStatusManager.user = user
    }

    override fun setPresenter(presenter: LoginContract.Presenter) {
        this.presenter = presenter
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun showError(errorMessage: String) {
        
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.login -> presenter.login(userName.text.toString(),passWord.text.toString())
        }
    }
}
