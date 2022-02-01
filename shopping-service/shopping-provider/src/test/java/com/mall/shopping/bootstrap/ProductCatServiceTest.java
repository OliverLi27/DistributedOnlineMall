package com.mall.shopping.bootstrap;

import com.mall.shopping.dto.AllProductCateRequest;
import com.mall.shopping.dto.AllProductCateResponse;
import com.mall.shopping.services.ProductCateServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Slf4j
public class ProductCatServiceTest extends ShoppingProviderApplicationTests {

    @Autowired
    ProductCateServiceImpl productCateService;

    @Test
    public void test01() throws IOException {
        AllProductCateRequest request = new AllProductCateRequest();

        request.setSort("desc");
        AllProductCateResponse allProductCate = productCateService.getAllProductCate(request);

        log.warn("allProductCat= {}", allProductCate);
        System.in.read();
    }

}
