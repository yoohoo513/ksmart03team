package ksmart.mybatis.controller;


import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ksmart.mybatis.dto.Goods;
import ksmart.mybatis.dto.Member;
import ksmart.mybatis.mapper.GoodsMapper;
import ksmart.mybatis.mapper.MemberMapper;
import ksmart.mybatis.service.GoodsService;
import ksmart.mybatis.service.MemberService;


@Controller
@RequestMapping("/goods")
public class GoodsController {
	
	private static final Logger log = LoggerFactory.getLogger(GoodsController.class);
	
	private final GoodsService goodsService;
	private final GoodsMapper goodsMapper;
	private final MemberService memberService;
	
	public GoodsController(GoodsService goodsService, GoodsMapper goodsMapper, MemberService memberService) {
		this.goodsService = goodsService;
		this.goodsMapper = goodsMapper;
		this.memberService = memberService;
	}
	

	
	@GetMapping("/goodsList")
	public String getGoodsList(Model model) {
		
		List<Goods> goodsList = goodsService.getGoodsList();
		
		model.addAttribute("goodsList",goodsList);
		model.addAttribute("title", "상품목록조회");
		
		log.info("상품 목록 조회: {}",goodsList);
		
		return "goods/goodsList";
	}
	
	/* 내가 만든 상품 추가
	 @GetMapping("/addGoods") public String addGoods(Model model) {
	 
	 List<Member> sellerIdList = goodsService.getSellerId();
	 
	 model.addAttribute("sellerIdList",sellerIdList); model.addAttribute("title",
	 "상품추가");
	 
	 return "goods/addGoods"; }
	 */
	
	@PostMapping("/addGoods")
	public String addGoods(Goods goods) {
		
		log.info("상품추가 쿼리파라미터: {}", goods);
		goodsService.addGoods(goods);
		
		return "redirect:/goods/goodsList";
	}
	
	//선생님이 만든 상품 추가
	@GetMapping("/addGoods")
    public String getGoods(Model model) {
        List<Map<String, Object>> goodsSellerIdList = goodsMapper.getSellerIdList();
        //List<Goods> goodsList = goodsService.getGoodsList();


        model.addAttribute("goodsSellerIdList", goodsSellerIdList);
        model.addAttribute("title", "상품등록");

         return "goods/addGoods";
    }
	
	
	@GetMapping("/modifyGoods")
	public String modifyGoods(@RequestParam(value = "goodsCode", required = false) String goodsCode, Model model) {
		
		List<Map<String, Object>> goodsSellerIdList = goodsMapper.getSellerIdList();
		Goods goodsInfo = goodsService.getGoodsInfoByGoodsCode(goodsCode);
		log.info("상품추가 쿼리파라미터: {}", goodsInfo);
		
		model.addAttribute("goodsSellerIdList", goodsSellerIdList);
		model.addAttribute("goodsInfo",goodsInfo);
		model.addAttribute("title", "상품수정");
		
		return "goods/modifyGoods";
	}
	
	@PostMapping("/modifyGoodsAction")
	public String modifyGoods(Goods goods) {
		
		goodsService.modifyGoods(goods);
		
		return "redirect:/goods/goodsList";
	}
	
	@GetMapping("/removeGoods/{goodsCode}")
	public String removeGoods(@PathVariable(value = "goodsCode", required = false) String goodsCode, Model model) {
		
		Goods goodsInfo = goodsService.getGoodsInfoByGoodsCode(goodsCode);
		log.info("상품삭제 화면 쿼리 파라미터: {}", goodsInfo);
		model.addAttribute("goodsInfo",goodsInfo);
		model.addAttribute("title", "상품삭제");
		
		return "goods/removeGoods";
	}
	
	@PostMapping("/removeGoods")
	public String removeGoods(@RequestParam(value = "memberPw") String memberPw
							 ,@RequestParam(value = "goodsSellerId") String goodsSellerId
							 ,@RequestParam(value = "goodsCode") String goodsCode
							 ,Goods goods) {
		
		boolean isChecked = goodsService.checkPwSellerId(goodsSellerId, memberPw);
		
		String redirectURI = "redirect:/goods/goodsList";
		
		if(!isChecked) {
			redirectURI="redirect:/goods/removeGoods/"+goodsCode;
			//reAttr.addAttribute("memberId",memberId);
			//reAttr.addAttribute("msg","입력하신 회원의 정보가 일치하지 않습니다.");
		}else {
			// 2. 비밀번호가 일치 시 관계 테이블을 고려하여 탈퇴 진행 (Service계층에서 탈퇴 프로세스 완성(because 트랜잭션))
			goodsService.removeGoods(goods);
		}
		
		return redirectURI;
	}
	
	@GetMapping("/sellerList")
	public String getSellerList(Model model) {
		
		List<Member> sellerList = goodsService.getSellerList();
		
		log.info("판매자 현황: {}", sellerList);
//		
//		return "redirect:/";
		
		model.addAttribute("sellerList", sellerList);
		model.addAttribute("title", "판매자 현황");
		
		
		return "goods/sellerList";
		
	}

}
