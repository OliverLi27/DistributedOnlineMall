package com.cskaoyan.gateway.controller.user;


import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.commons.tool.utils.CookieUtil;
import com.mall.user.IKaptchaService;
import com.mall.user.ILoginService;
import com.mall.user.annotation.Anoymous;
import com.mall.user.constants.SysRetCodeConstants;
import com.mall.user.dto.*;


import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Xingchen Li
 * @version 1.0
 * @date 2020/9/17 16:14
 */
@RestController
@RequestMapping("user")
public class LoginController {
    @Reference(check = false)
    IKaptchaService iKaptchaService;
    @Reference(check = false)
    ILoginService loginService;
    @PostMapping("login")
    @Anoymous
    public ResponseData login(@RequestBody UserLoginRequest userLoginRequest,
                              HttpServletRequest request, HttpServletResponse response) {

        //验证验证码
        KaptchaCodeRequest kaptchaCodeRequest = new KaptchaCodeRequest();
        kaptchaCodeRequest.setCode(userLoginRequest.getCaptcha());
        String kaptcha_uuid = CookieUtil.getCookieValue(request, "kaptcha_uuid");
        kaptchaCodeRequest.setUuid(kaptcha_uuid);
        KaptchaCodeResponse kaptchaCodeResponse = iKaptchaService.validateKaptchaCode(kaptchaCodeRequest);
        if (!kaptchaCodeResponse.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())){
            return new ResponseUtil<>().setErrorMsg(SysRetCodeConstants.KAPTCHA_CODE_ERROR.getMessage());
        }
        //验证用户名和密码
        UserLoginResponse loginResponse = loginService.login(userLoginRequest);
        if (!loginResponse.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())) {
            String msg = loginResponse.getMsg();
            return new ResponseUtil<>().setErrorMsg(msg);
        }
        //将token放入cookie中?
        String requestURL = request.getRequestURL().toString();
        String requestURI = request.getRequestURI();
        String domainUrl=requestURL.replace(requestURI,"");

        String token = loginResponse.getToken();
        Cookie cookie = new Cookie("access_token", token);
        //为啥setCookie是的第一个参数response ? ? ? 通过返回体，把这个cookie返回给浏览器携带，这个cookie里面
        //存的是认证信息
//        CookieUtil.setCookie(response, cookie);
        //CookieUtil.setCookie(response, cookie);
        //cookie.setDomain(domainUrl);
        cookie.setMaxAge(-1);
        cookie.setPath("/");
        response.addCookie(cookie);
        if (loginResponse.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())) {
            return new ResponseUtil<>().setData(loginResponse);
        } else {
            return new ResponseUtil<>().setErrorMsg(loginResponse.getMsg());
        }
    }
    @GetMapping("login")
    public ResponseData loginVerify(LoginVerifyRequest verifyRequest, HttpServletRequest request) {
        String token = CookieUtil.getCookieValue(request, "access_token");
        if ("".equals(token)) {
            return new ResponseUtil<>().setErrorMsg(SysRetCodeConstants.PERMISSION_DENIED.getMessage());
        }
        LoginVerifyResponse response = loginService.loginVerify(token);
        if (response.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())){
            return new ResponseUtil<>().setData(response);
        }else {
            return new ResponseUtil<>().setErrorMsg(SysRetCodeConstants.PERMISSION_DENIED.getMessage());
        }
    }
}
