package ksmart.mybatis.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import ksmart.mybatis.dto.LoginHistory;
import ksmart.mybatis.dto.Member;
import ksmart.mybatis.dto.MemberLevel;

@Mapper
public interface MemberMapper {
	//회원목록 조회
	public List<Member> getMemberList(String searchKey, String searchValue);
	
	//회원등급 조회
	public List<MemberLevel> getMemberLevelList();
	
	//회원 가입
	public int addMember(Member member);
	
	//회원 중복 체크
	public boolean checkMemberId(String memberId);
	
	//특정 회원 정보 조회
	public Member getMemberInfoById(String memberId);
	
	//특정 회원 정보 수정
	public int modifyMember(Member member);
	
	//판매자가 등록한 상품의 주문 삭제
	public int removeOrderByGoodsCode(String memberId);
	
	//판매자가 등록한 상품 삭제
	public int removerGoodsById(String memberId);
	
	//구매자의 구매 이력 삭제
	public int removeOrderById(String memberId);
	
	//로그인 이력 삭제
	public int removeLoginHistoryById(String memberId);
	
	//회원 삭제
	public int removeMemberById(String memberId);
	
	//판매자 현황 조회
	public List<Member> getSellerList();
	
	//로그인 이력 조회
	public List<LoginHistory> getLoginHistory(Map<String, Object> paramMap);
	
	public int getLoginHistoryCnt();
}
