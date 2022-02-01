package com.mall.user;

import com.mall.user.dto.*;

/**
 * @author Xingchen Li
 * @version 1.0
 * @date 2020/9/17 16:15
 */
public interface ILoginService {
    UserLoginResponse login(UserLoginRequest userLoginRequest);

    LoginVerifyResponse loginVerify(String token);

    CheckAuthResponse validToken(CheckAuthRequest checkAuthRequest);
}
