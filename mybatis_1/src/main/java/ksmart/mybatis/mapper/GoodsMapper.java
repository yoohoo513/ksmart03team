package ksmart.mybatis.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import ksmart.mybatis.dto.Goods;
import ksmart.mybatis.dto.Member;

@Mapper
public interface GoodsMapper {

	public List<Goods> getGoodsList();
	
	public void addGoods(Goods goods);
	
	public List<Member> getSellerId();
	
	public Goods getGoodsInfoByGoodsCode(String goodsCode);
	
	public List<Map<String, Object>> getSellerIdList();
	
	public int modifyGoods(Goods goods);
	
	//등록한 상품의 주문 이력 삭제
	public int removeOrderHistoryByGoodsCode(String goodsCode);
	//등록한 상품 삭제
	public int removeGoodsByGoodsCode(String goodsCode);
}
