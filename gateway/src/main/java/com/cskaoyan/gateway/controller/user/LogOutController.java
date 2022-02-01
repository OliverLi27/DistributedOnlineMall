package com.cskaoyan.gateway.controller.user;

import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Xingchen Li
 * @version 1.0
 * @date 2020/9/17 16:39
 */
@RestController
@RequestMapping("user")
public class LogOutController {
    @GetMapping("loginOut")
    public ResponseData logout(HttpServletRequest request, HttpServletResponse response){
        Cookie cookie = new Cookie("access_token",null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return new ResponseUtil<>().setData(null);
    }
}
