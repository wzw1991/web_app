package controllers

import play.api._
import play.api.mvc._
import play.api.templates._
import models._

object Application extends Controller {

  def index = Action {
    // Ok(views.html.index("Your new application is ready."))
    Ok(Html("<p>用户名或密码错误！</p>"))
  }
  
  def findUser(username : String) = TODO
    
  def findWork = TODO
  
  def editUser = TODO
  
  def deleteWork = TODO
  
  //显示个人信息的
  def home = Action {
    Ok(views.html.login(User.list, User.formRegister))
  }
  
  def h = Action {
    Ok(views.html.register(User.list, User.form))
  }

  //登陆
  def login = Action { 
    implicit request =>
      User.formRegister.bindFromRequest.fold(
        //处理错误
//        errors => BadRequest(Html("<p>hello！</p>")),
        errors => BadRequest(views.html.login(User.list, errors)),
        {
          case (username, password) =>
//           case (username, password, null, null) =>
            if(MongoTest.chkPwd(username, password)){
//              MongoTest.add(username, password)
              //发言
//              User.post(username, password)
              Work.post(username, null, null)
              //重新定向到显示留言列表和发言表单页面
              Redirect(routes.Application.info)// 登陆成功后进入勤务信息的登记    
            }
            else
               Ok(Html("<p>用户名或密码错误！</p>"))
        })

  }
  
  //注册
  def register = Action {
    implicit request =>
      User.form.bindFromRequest.fold(
        //处理错误
        errors => BadRequest(views.html.register(User.list, errors)),
        {
          case (username, password, passwordAgain, mail) =>
            if(!MongoTest.chkUser(username)){
              if(password == passwordAgain){
	              MongoTest.add(username, password, passwordAgain, mail)
	              //发言
	              Work.post(username, null, null)
	              //重新定向到显示留言列表和发言表单页面
	              Redirect(routes.Application.info) // 注册成功后进入勤务时间的登记
              }
              else
                Ok(Html("<p>两次密码不一样，请重新输入</p>"))
            }
            else
               Ok(Html("<p>请换另一个用户名，该用户名已有人用了！</p>"))
            
        })

  }
  
  // 个人信息
  def info = Action {
    Ok(views.html.info(Work.list, Work.form))
  }
  
  def work = Action {
     implicit request =>
      Work.form.bindFromRequest.fold(
        //处理错误
        errors => BadRequest(views.html.info(Work.list, errors)),
        {
          case (username, workStart, workEnd) =>
            MongoTest.addWork(username, workStart, workEnd)
            //发言
//            Work.post(username, workStart, workEnd)
            //重新定向到显示留言列表和发言表单页面
            Redirect(routes.Application.info)
        })
  }

}