package com.example.repository.GsonBean

/**
 * User的GsonBean类
 * 接口地址：baseUrl/login/cellphone?phone=XXX&password=XXX
 * */
class UserJson {
    var account : Account? = null
    var profile : Profile? = null
    
    class Account{
        var id : Long? = null
    }
    
    class Profile{
        var avatarUrl : String? = null
        var nickname : String? = null
    }
}