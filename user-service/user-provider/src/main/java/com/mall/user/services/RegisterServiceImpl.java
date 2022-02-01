package com.mall.user.services;

import com.mall.user.IRegisterService;
import com.mall.user.constants.SysRetCodeConstants;
import com.mall.user.dal.entitys.Member;
import com.mall.user.dal.entitys.UserVerify;
import com.mall.user.dal.persistence.MemberMapper;
import com.mall.user.dal.persistence.UserVerifyMapper;
import com.mall.user.dto.RegisterVerifyResponse;
import com.mall.user.dto.UserRegisterRequest;
import com.mall.user.dto.UserRegisterResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Xingchen Li
 * @version 1.0
 * @date 2020/9/16 19:42
 */
@Slf4j
@Component
@Service
public class RegisterServiceImpl implements IRegisterService {
    @Autowired
    MemberMapper memberMapper;
    @Autowired
    UserVerifyMapper userVerifyMapper;
    @Autowired
    JavaMailSender javaMailSender;

    @Override
    public UserRegisterResponse register(UserRegisterRequest userRegisterRequest) {
        UserRegisterResponse response = new UserRegisterResponse();
        //验证用户名是否重复
        Example example = new Example(Member.class);
        example.createCriteria().andEqualTo("username",  userRegisterRequest.getUserName());
        List<Member> members = memberMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(members)){
            response.setCode(SysRetCodeConstants.USERNAME_ALREADY_EXISTS.getCode());
            response.setMsg(SysRetCodeConstants.USERNAME_ALREADY_EXISTS.getMessage());
            return response;
        }

        //向用户表中插入一条记录
        Member member = new Member();
        member.setUsername(userRegisterRequest.getUserName());
        member.setEmail(userRegisterRequest.getEmail());
        //密码要做加密处理(MD5)
        byte[] bytes = userRegisterRequest.getUserPwd().getBytes();
        String password = DigestUtils.md5DigestAsHex(bytes);
        member.setPassword(password);
        member.setCreated(new Date());
        member.setUpdated(new Date());
        member.setState(1);
        int effectRow = memberMapper.insertSelective(member);
        if (effectRow!=1){
            response.setCode(SysRetCodeConstants.USER_REGISTER_FAILED.getCode());
            response.setMsg(SysRetCodeConstants.USER_REGISTER_FAILED.getMessage());
            return response;
        }
        //向用户验证表中插入一条记录
        UserVerify userVerify = new UserVerify();
        userVerify.setUsername(userRegisterRequest.getUserName());
        userVerify.setIsVerify("N");
        userVerify.setIsExpire("N");
        userVerify.setRegisterDate(new Date());
        String key = userRegisterRequest.getUserName()+userRegisterRequest.getUserPwd()+UUID.randomUUID();
        String uuid = DigestUtils.md5DigestAsHex(key.getBytes());
        userVerify.setUuid(uuid);
        userVerifyMapper.insertSelective(userVerify);

        //发送用户激活邮件
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("csmall激活--你也想起舞吗?");
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder http = stringBuilder.append("http://localhost:8080/user/verify?uid=").append(uuid).append("&username=").append(userRegisterRequest.getUserName());
        mailMessage.setText(http.toString());
        mailMessage.setTo(userRegisterRequest.getEmail());
        mailMessage.setFrom("3529400282@qq.com");

        //异步发送解决email发送缓慢
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                javaMailSender.send(mailMessage);

            }
        });

        thread.start();
        ////线程池
        //ExecutorService executorService = Executors.newFixedThreadPool(2);
        //executorService.submit(new Runnable() {
        //    @Override
        //    public void run() {
        //        javaMailSender.send(mailMessage);
        //    }
        //});

        response.setCode(SysRetCodeConstants.SUCCESS.getCode());
        response.setMsg(SysRetCodeConstants.SUCCESS.getMessage());
        return response;
    }

    /**
     * 用户注册激活
     * @param uid
     * @param username
     * @return
     */
    @Override
    public RegisterVerifyResponse registerVerify(String uid, String username) {
        UserVerify userVerify = new UserVerify();
        userVerify.setIsVerify("Y");

        Example example = new Example(UserVerify.class);
        example.createCriteria().andEqualTo("uuid", uid).andEqualTo("username",username);
        userVerifyMapper.updateByExampleSelective(userVerify,example);
        return null;
    }
}
