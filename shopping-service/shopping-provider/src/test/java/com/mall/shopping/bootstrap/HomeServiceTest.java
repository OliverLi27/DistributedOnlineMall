package com.mall.shopping.bootstrap;

import com.mall.shopping.dal.persistence.PanelMapper;
import com.mall.shopping.dto.HomePageResponse;
import com.mall.shopping.services.HomeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Slf4j
public class HomeServiceTest extends ShoppingProviderApplicationTests {


    @Autowired
    PanelMapper panelMapper;

    @Autowired
    HomeServiceImpl homeService;

    @Test
    public void test01() throws IOException {
        HomePageResponse homepage = homeService.homepage();
        log.warn("hompage = {}", homepage);
        System.in.read();
    }
}
