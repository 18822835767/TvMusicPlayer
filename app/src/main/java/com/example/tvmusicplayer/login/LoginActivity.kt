package com.example.tvmusicplayer.login

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.bean.User

class LoginActivity : AppCompatActivity(),LoginContract.OnView{

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

    override fun showSuccess(user: User) {
        
    }

    override fun setPresenter(presenter: LoginContract.Presenter) {
        
    }

    override fun showLoading() {
        
    }

    override fun hideLoading() {
        
    }

    override fun showError(errorMessage: String) {
        
    }
}
