function loginApi(data) {
    return $axios({
        'url': '/user/login',
        'method': 'post',
        data
    })
}

//定义一个登录退出的接口,使用POST方法,返回一个promise对象,该对象的then方法中有一个回调函数,该函数的参数为后台返回的数据
function loginoutApi() {
    return $axios({
        'url': '/user/loginout',
        'method': 'post',
    })
}

//定义一个发送验证码的接口,使用POST方法,将data作为参数传入,返回一个promise对象,该对象的then方法中有一个回调函数,该函数的参数为后台返回的数据
function sendMsgApi(data) {
    return $axios({
        'url': '/user/sendMsg',
        'method': 'post',
        data
    })
}
  