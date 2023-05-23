package com.yjm.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yjm.reggie.common.R;
import com.yjm.reggie.entity.User;
import com.yjm.reggie.service.UserService;
import com.yjm.utils.MailUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 游锦民
 * @version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    //注入UserService对象
    @Autowired
    private UserService userService;

    //注入RedisTemplate对象
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 发送邮箱验证码
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) throws MessagingException {
        //获取邮箱
        String phone = user.getPhone();
        //生成验证码
        if (!phone.isEmpty()) {

            //调用工具类生成验证码
            String code = MailUtils.achieveCode();

            //打印日志
            log.info(code);

            //发送验证码
          //  MailUtils.sendMail(phone, code);

            //将验证码保存到Session中,验证没有保存到session中,导致验证码错误
           // session.setAttribute(phone, code);

            //将验证码保存到redis中并设置过期时间
            redisTemplate.opsForValue().set(phone, code,5, TimeUnit.MINUTES);

            // 启动多线程来限定验证码的时效性
            //lambda表达式实现多线程
            new Thread(() -> {
                try {
                    // 验证码的有效时长
                    Thread.sleep(500000L);
                    // 更换新验证码
                    session.setAttribute(phone, MailUtils.achieveCode());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            return R.success("发送成功");
        }
        return R.error("发送失败");
    }


    /**
     * 移动端用户登录
     *
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {

        log.info(map.toString());
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();

        //获取session中的验证码,验证码的key为手机号
       // String codeInSession = (String) session.getAttribute(phone);

        //获取redis中的验证码
        String codeInRedis = redisTemplate.opsForValue().get(phone);

        //看看接收到用户输入的验证码是否和session中的验证码相同
        if (code != null && code.equals(codeInRedis)) {
            //根据手机号查询用户
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.eq(User::getPhone, phone);

            //根据手机号查询用户
            User user = userService.getOne(queryWrapper);

            //如果用户不存在,则创建用户
            if (user == null) {

                user = new User();
                //设置用户手机号
                user.setPhone(phone);
                //保存用户
                userService.save(user);
            }

            //将用户id保存到session中
            session.setAttribute("user", user.getId());

            //登录成功之后删除redis中的验证码
            redisTemplate.delete(phone);

            //返回用户信息
            return R.success(user);
        }
        return R.error("登录失败");
    }

    /**
     * 移动端用户退出
     *
     * @param request
     * @return
     */
    @PostMapping("/loginout")
    public R<String> logout(HttpServletRequest request) {   //HttpServletRequest request的作用是获取session,因为session是在服务器端的,所以需要通过request来获取
        //移除session中的用户id
        request.getSession().removeAttribute("user");


        return R.success("退出成功");
    }


}
