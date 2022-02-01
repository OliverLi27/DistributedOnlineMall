package com.mall.user.services;

import com.alibaba.fastjson.JSON;
import com.mall.user.ILoginService;
import com.mall.user.constants.SysRetCodeConstants;
import com.mall.user.dal.entitys.Member;
import com.mall.user.dal.entitys.UserVerify;
import com.mall.user.dal.persistence.MemberMapper;
import com.mall.user.dal.persistence.UserVerifyMapper;
import com.mall.user.dto.*;
import com.mall.user.utils.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author Xingchen Li
 * @version 1.0
 * @date 2020/9/17 16:18
 */
@Slf4j
@Component
@Service
public class LoginServiceImpl implements ILoginService {
    @Autowired
    UserVerifyMapper userVerifyMapper;
    @Autowired
    MemberMapper memberMapper;
    @Override
    public UserLoginResponse login(UserLoginRequest userLoginRequest) {
        UserLoginResponse response = new UserLoginResponse();

        String username = userLoginRequest.getUserName();
        byte[] bytes = userLoginRequest.getUserPwd().getBytes();
        String password = DigestUtils.md5DigestAsHex(bytes);
        //判断用户是否存在
        Example example = new Example(Member.class);
        Example.Criteria exampleCriteria = example.createCriteria();
        exampleCriteria.andEqualTo("username", username);
        int i = memberMapper.selectCountByExample(example);
        if (i==0){
            response.setCode(SysRetCodeConstants.USERORPASSWORD_ERRROR.getCode());
            response.setMsg(SysRetCodeConstants.USERORPASSWORD_ERRROR.getMessage());
            return response;
        }
        //判断用户名或者密码是否正确

        exampleCriteria.andEqualTo("password", password);
        List<Member> members = memberMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(members)){
            response.setCode(SysRetCodeConstants.USERORPASSWORD_ERRROR.getCode());
            response.setMsg(SysRetCodeConstants.USERORPASSWORD_ERRROR.getMessage());
            return response;
        }
        //判断用户是否激活
        Example userVerifyExample = new Example(UserVerify.class);
        userVerifyExample.createCriteria().andEqualTo("username", username);
        List<UserVerify> userVerifies = userVerifyMapper.selectByExample(userVerifyExample);
        UserVerify userVerify = userVerifies.get(0);
        if (!"Y".equals(userVerify.getIsVerify())){
            response.setCode(SysRetCodeConstants.USER_ISVERFIED_ERROR.getCode());
            response.setMsg(SysRetCodeConstants.USER_ISVERFIED_ERROR.getMessage());
            return response;
        }

        //产生一个JWT(msg必须为json格式)
        Long id=Long.parseLong("0");
        String file=null;
        for (Member member : members) {
            id = member.getId();
            file = member.getFile();
        }
        String msg = JSON.toJSONString(id.toString());
        JwtTokenUtils tokenUtils = JwtTokenUtils.builder().msg(msg).build();
        String token = tokenUtils.creatJwtToken();
        response.setId(id);
        response.setUsername(userLoginRequest.getUserName());
        response.setSex(null);
        response.setBalance(null);
        response.setFile(file);
        response.setState(1);
        response.setToken(token);
        response.setCode(SysRetCodeConstants.SUCCESS.getCode());
        response.setMsg(SysRetCodeConstants.SUCCESS.getMessage());

        return response;
    }

    @Override
    public LoginVerifyResponse loginVerify(String token) {
        LoginVerifyResponse response = new LoginVerifyResponse();

        JwtTokenUtils tokenUtils = JwtTokenUtils.builder().token(token).build();
        String key = tokenUtils.freeJwt();
        Long uid = JSON.parseObject(key,Long.class);
        Example example = new Example(Member.class);
        example.createCriteria().andEqualTo("id", uid);
        List<Member> members = memberMapper.selectByExample(example);
        for (Member member : members) {
            response.setFile(member.getFile());
        }
        response.setUid(uid);
        response.setCode(SysRetCodeConstants.SUCCESS.getCode());
        response.setMsg(SysRetCodeConstants.SUCCESS.getMessage());
        return response;
    }

    /**
     *
     * @param checkAuthRequest
     * @return
     */
    @Override
    public CheckAuthResponse validToken(CheckAuthRequest checkAuthRequest) {
        CheckAuthResponse checkAuthResponse = new CheckAuthResponse();

        String token = checkAuthRequest.getToken();
        JwtTokenUtils tokenUtils = JwtTokenUtils.builder().token(token).build();
        String key = tokenUtils.freeJwt();
        Long uid = JSON.parseObject(key,Long.class);


        checkAuthResponse.setUid(uid);
        checkAuthResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        checkAuthResponse.setMsg(SysRetCodeConstants.SUCCESS.getMessage());
        return checkAuthResponse;
    }
}
