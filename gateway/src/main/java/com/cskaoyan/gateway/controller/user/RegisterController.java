package com.cskaoyan.gateway.controller.user;

import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.commons.tool.utils.CookieUtil;
import com.mall.user.IKaptchaService;
import com.mall.user.IRegisterService;
import com.mall.user.annotation.Anoymous;
import com.mall.user.constants.SysRetCodeConstants;
import com.mall.user.dto.*;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Xingchen Li
 * @version 1.0
 * @date 2020/9/16 19:33
 */
@RestController
@RequestMapping("/user")
public class RegisterController {
    @Reference(check = false)
    IKaptchaService iKaptchaService;
    @Reference(check = false)
    IRegisterService iRegisterService;

    @PostMapping("/register")
    @Anoymous
    public ResponseData register(@RequestBody UserRegisterRequest registerRequest, HttpServletRequest request){
        //验证验证码是否正确
       KaptchaCodeRequest kaptchaCodeRequest = new KaptchaCodeRequest();
        kaptchaCodeRequest.setCode(registerRequest.getCaptcha());
        String kaptcha_uuid = CookieUtil.getCookieValue(request, "kaptcha_uuid");
        kaptchaCodeRequest.setUuid(kaptcha_uuid);
        KaptchaCodeResponse kaptchaCodeResponse = iKaptchaService.validateKaptchaCode(kaptchaCodeRequest);
        if (!kaptchaCodeResponse.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())){
            return new ResponseUtil<>().setErrorMsg(SysRetCodeConstants.KAPTCHA_CODE_ERROR.getMessage());
        }

        //插入数据库
        UserRegisterResponse response = iRegisterService.register(registerRequest);
        if (response.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())){
            ResponseData responseData = new ResponseUtil().setData(null);
            return responseData;
        }else {
            return new ResponseUtil().setErrorMsg(response.getMsg());
        }
    }
    @GetMapping("verify")
    @Anoymous
    public ResponseData registerVerify(String uid,String username){
        RegisterVerifyResponse response=iRegisterService.registerVerify(uid, username);
        return new ResponseUtil<>().setData(response);
    }
}
