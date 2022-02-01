package com.mall.order.biz.handler;

import com.alibaba.fastjson.JSON;
import com.mall.commons.tool.exception.BaseBusinessException;
import com.mall.commons.tool.exception.BizException;
import com.mall.order.biz.context.CreateOrderContext;
import com.mall.order.biz.context.TransHandlerContext;
import com.mall.order.dal.entitys.Stock;
import com.mall.order.dal.persistence.OrderItemMapper;
import com.mall.order.dal.persistence.StockMapper;
import com.mall.order.dto.CartProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description: 扣减库存处理器
 * @Author： Xingchen Li
 * @Date: 2019-09-16 00:03
 **/
@Component
@Slf4j
public class SubStockHandler extends AbstractTransHandler {

    @Autowired
    private StockMapper stockMapper;

	@Override
	public boolean isAsync() {
		return false;
	}

	@Override
	@Transactional
	public boolean handle(TransHandlerContext context) {
		log.warn("SubStockHandler");
		//向下转型
		CreateOrderContext createOrderContext = (CreateOrderContext) context;

		//拿到用户的购物车商品的list
		List<CartProductDto> dtoList = createOrderContext.getCartProductDtoList();
		//拿到购买的商品id的list
		List<Long> buyProductIds = createOrderContext.getBuyProductIds();
		//converter中未涉及ProductId的list,做判断是否为空
		if (CollectionUtils.isEmpty(buyProductIds)) {
			//为空时，把购物车商品的list放入购买的商品list
			buyProductIds = dtoList.stream().map(u -> u.getProductId()).collect(Collectors.toList());
		}
		//排个序
		buyProductIds.sort(Long::compareTo);

		//锁定库存
		List<Stock> stockList = stockMapper.findStocksForUpdate(buyProductIds);
		//判断库是否为空
		if (CollectionUtils.isEmpty(stockList)) {
			throw new BaseBusinessException("库存未初始化！");
		}

		if (stockList.size() != buyProductIds.size()) {
			throw new BizException("部分商品库存未初始化");
		}

		//扣减库存
		for (CartProductDto cartProductDto : dtoList) {
			Long productId = cartProductDto.getProductId();
			Long productNum = cartProductDto.getProductNum();

			//productNum 不能超过限购数量
			Long limitNUm = cartProductDto.getLimitNum();
			if (productNum > limitNUm) {
				throw new BaseBusinessException("购买数量超过限购数量");
			}

			Stock stock = new Stock();
			stock.setItemId(productId);
			stock.setLockCount(productNum.intValue());
			stock.setStockCount(-productNum);

			stockMapper.updateStock(stock);
		}

		return true;
	}
}