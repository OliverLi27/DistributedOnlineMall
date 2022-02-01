package com.mall.user;

import com.mall.user.dto.RegisterVerifyResponse;
import com.mall.user.dto.UserRegisterRequest;
import com.mall.user.dto.UserRegisterResponse;

/**
 * @author Xingchen Li
 * @version 1.0
 * @date 2020/9/16 19:43
 */
public interface IRegisterService {
    UserRegisterResponse register(UserRegisterRequest request);
    RegisterVerifyResponse registerVerify(String uid, String username);
}
