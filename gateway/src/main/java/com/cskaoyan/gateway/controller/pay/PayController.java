package com.cskaoyan.gateway.controller.pay;

import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.pay.IPayService;
import com.mall.pay.dto.PayQueryStatusResponse;
import com.mall.pay.dto.PayRequest;
import com.mall.pay.dto.PayResponse;
import com.mall.user.constants.SysRetCodeConstants;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.mall.pay.config.Constants.SUCCESS;

/**
 * @author Xingchen Li
 * @version 1.0
 * @date 2020/9/19 15:10
 */
@RestController
@RequestMapping("cashier")
public class PayController {
    @Reference(check = false)
    IPayService payService;

    @PostMapping("pay")
    public ResponseData payorder(@RequestBody PayRequest payRequest, HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
        String requestURI = request.getRequestURI();
        String result = requestURL.replace(requestURI, "");
        PayResponse response = payService.createPicToPay(payRequest,result);

        if (response.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())){
            return new ResponseUtil<>().setData(response.getResult());
        }else {
            return new ResponseUtil<>().setErrorMsg(response.getMsg());
        }
    }

    @GetMapping("queryStatus")
    public ResponseData queryStatus(String orderId){
        ResponseData<Object> responseData = new ResponseData<>();
        PayQueryStatusResponse response = payService.queryStatus(orderId);
        responseData.setCode(Integer.valueOf(response.getCode()));
        responseData.setMessage(response.getMsg());
        responseData.setTimestamp(System.currentTimeMillis());
        if (SUCCESS.equalsIgnoreCase(response.getCode())){
            return new ResponseUtil<>().setData(null);
        }else {
            responseData.setSuccess(false);
        }

        responseData.setResult(null);
        return responseData;
    }
}
