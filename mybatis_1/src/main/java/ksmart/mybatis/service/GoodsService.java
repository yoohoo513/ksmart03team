package ksmart.mybatis.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ksmart.mybatis.dto.Goods;
import ksmart.mybatis.dto.Member;
import ksmart.mybatis.mapper.GoodsMapper;
import ksmart.mybatis.mapper.MemberMapper;

@Service
@Transactional
public class GoodsService {
	private final GoodsMapper goodsMapper;
	private final MemberMapper memberMapper;
	
	public GoodsService(GoodsMapper goodsMapper, MemberMapper memberMapper) {
		this.goodsMapper = goodsMapper;
		this.memberMapper = memberMapper;
	}
	
	/**
	 * 상품 목록 조회
	 * @return List<Goods>
	 */
	public List<Goods> getGoodsList(){
		
		List<Goods> goodsList = goodsMapper.getGoodsList();
		
		return goodsList;
		
	}
	
	public void addGoods(Goods goods) {
		goodsMapper.addGoods(goods);
	}
	
	public List<Member> getSellerId(){
		
		List<Member> sellerIdList = goodsMapper.getSellerId();
		
		return sellerIdList;
		
	}
	
	public Goods getGoodsInfoByGoodsCode(String goodsCode) {
		
		Goods goodsInfoByGoodsCode = goodsMapper.getGoodsInfoByGoodsCode(goodsCode);
		
		return goodsInfoByGoodsCode;
	}
	
	public int modifyGoods(Goods goods) {
		
		int modifyGoodsResult = goodsMapper.modifyGoods(goods);
		
		return modifyGoodsResult;
	}
	
	public boolean checkPwSellerId(String goodsSellerId, String memberPw) {
		
		boolean result = false;
		
		Member member = memberMapper.getMemberInfoById(goodsSellerId);
		
		if(member != null) {
			String checkPw = member.getMemberPw();
			
			if(memberPw.equals(checkPw)) {
				result = true;
			}
		}
		System.out.println(result);
		
		return result;
	}
	
	public int removeGoods(Goods goods) {
		
		int result = 0;
		
		String goodsCode = goods.getGoodsCode();
		
		goodsMapper.removeOrderHistoryByGoodsCode(goodsCode);
		goodsMapper.removeGoodsByGoodsCode(goodsCode);
		
		return result;
	};
	
	/**
	 * 판매자 현황 조회
	 * @return List<Member>
	 */
	public List<Member> getSellerList(){
		
		List<Member> sellerList = memberMapper.getSellerList();
		
		return sellerList;
	}

}
