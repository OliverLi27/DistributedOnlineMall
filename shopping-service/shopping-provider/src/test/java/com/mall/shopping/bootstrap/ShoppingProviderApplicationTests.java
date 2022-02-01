package com.mall.shopping.bootstrap;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.shopping.dto.*;
import com.mall.shopping.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShoppingProviderApplication.class)
public class 1ShoppingProviderApplicationTests {

//    @Autowired
//    private ICartService cartService;
//
//
//    @Test
//    public void testCartService() throws IOException {
//        AddCartRequest request = new AddCartRequest();
//        request.setUserId(100023501L);
//        request.setUserId(123L);
//        cartService.addToCart(request);
//        System.in.read();
//    }
//
//    @Autowired
//    private IContentService contentService;
//
//    @Test
//    public void testContentService() throws IOException {
//        contentService.queryNavList();
//        System.in.read();
//    }
//
//    @Autowired
//    private IHomeService homeService;
//    @Test
//    public void testHomeService() throws IOException {
//        homeService.homepage();
//        System.in.read();
//    }
//
//    @Autowired
//    private IProductCateService productCateService;
//
//    @Test
//    public void testProductCateService() throws IOException {
//        AllProductCateRequest request = new AllProductCateRequest();
//        request.setSort("1");
//        productCateService.getAllProductCate(request);
//        System.in.read();
//    }
//
//    @Autowired
//    private IProductService productService;
//
//    @Test
//    public void testProductService() throws IOException {
//        ProductDetailRequest productDetailRequest = new ProductDetailRequest();
//        productDetailRequest.setId(100023501L);
//        productService.getProductDetail(productDetailRequest);
//
//        // ----------------------------------------------------------------------------
//
////        productService.getRecommendGoods();
//
//        System.in.read();
//    }
//    @Autowired
//    private ICartService cartService;
//
//
//    @Test
//    public void testCartService() throws IOException {
//        AddCartRequest request = new AddCartRequest();
//        request.setItemId(100023501L);
//        request.setUserId(123L);
//        cartService.addToCart(request);
//        System.in.read();
//    }
//
//    @Autowired
//    private IContentService contentService;
//
//    @Test
//    public void testContentService() throws IOException {
//        contentService.queryNavList();
//        System.in.read();
//    }
//
//    @Autowired
//    private IHomeService homeService;
//    @Test
//    public void testHomeService() throws IOException {
//        homeService.homepage();
//        System.in.read();
//    }
//
//    @Autowired
//    private IProductCateService productCateService;
//
//    @Test
//    public void testProductCateService() throws IOException {
//        AllProductCateRequest request = new AllProductCateRequest();
//        request.setSort("1");
//        productCateService.getAllProductCate(request);
//        System.in.read();
//    }
//
//    @Autowired
//    private IProductService productService;
//
//    @Test
//    public void testProductService() throws IOException {
////        ProductDetailRequest productDetailRequest = new ProductDetailRequest();
////        productDetailRequest.setId(100023501L);
////        productService.getProductDetail(productDetailRequest);
//
//        // ----------------------------------------------------------------------------
//
////        productService.getRecommendGoods();
//
//        AllProductRequest allProductRequest = new AllProductRequest();
//        allProductRequest.setPage(1);
//        allProductRequest.setSize(8);
//        allProductRequest.setSort("1");
//        allProductRequest.setPriceGt(100);
//        allProductRequest.setPriceLte(1000);
//        allProductRequest.setCid((long)231);
//        AllProductResponse allProduct = productService.getAllProduct(allProductRequest);
//        System.out.println(allProduct);
//        System.in.read();
//    }
//
//    @Test
//    public void testProductDetail() throws IOException {
//        ProductDetailRequest request = new ProductDetailRequest();
//        request.setId((long)100057401);
//        ProductDetailResponse productDetail = productService.getProductDetail(request);
//        System.out.println(productDetail);
//        System.in.read();
//    }
//
//    @Test
//    public void test11() throws IOException {
//        RecommendResponse recommendGoods = productService.getRecommendGoods();
//        System.out.println(recommendGoods);
//        System.in.read();
//    }
//
//    @Test
//    public void testPageHelper() throws IOException {
//        AllProductRequest allProductRequest = new AllProductRequest();
//        allProductRequest.setPage(1);
//        allProductRequest.setSize(1);
//        allProductRequest.setSort("1");
//        allProductRequest.setPriceGt(100);
//        allProductRequest.setPriceLte(1000);
//        allProductRequest.setCid((long)231);
//        AllProductResponse allProduct = productService.getAllProduct(allProductRequest);
//
//        System.out.println(allProduct);
//        System.in.read();
//
//    }
}
