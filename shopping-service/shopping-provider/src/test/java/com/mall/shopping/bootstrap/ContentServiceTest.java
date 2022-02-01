package com.mall.shopping.bootstrap;

import com.mall.shopping.dto.NavListResponse;
import com.mall.shopping.services.ContentServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Slf4j
public class ContentServiceTest extends ShoppingProviderApplicationTests {

    @Autowired
    ContentServiceImpl contentService;

    /**
     * 导航栏
     * @throws IOException
     */
    @Test
    public void test01() throws IOException {
        NavListResponse response = contentService.queryNavList();
        log.warn("ContentService: {}", response);
        System.in.read();
    }
}
