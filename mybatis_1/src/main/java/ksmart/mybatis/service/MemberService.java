package ksmart.mybatis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ksmart.mybatis.dto.LoginHistory;
import ksmart.mybatis.dto.Member;
import ksmart.mybatis.dto.MemberLevel;
import ksmart.mybatis.mapper.MemberMapper;

@Service
@Transactional
public class MemberService {
	private final MemberMapper memberMapper;
	
	public MemberService(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
	}
	
	/**
	 * 회원 목록 조회
	 * @return List<Member>
	 */
	public List<Member> getMemberList(String searchKey, String searchValue){

		if(searchKey != null) {
			switch (searchKey){
				case "memberId": 
					searchKey ="m.m_id";
					break;
				case "memberName": 
					searchKey ="m.m_name";
					break;
				case "memberLevelName": 
					searchKey ="ml.level_name";
					break;
				case "memberEmail": 
					searchKey ="m.m_email";
					break;
			}
		}
		
		List<Member> memberList = memberMapper.getMemberList(searchKey, searchValue);
		
		for(Member member : memberList) {
			int memberLevel = member.getMemberLevel();
			
			if(memberLevel == 1 ) {
				member.setMemberLevelName("관리자");
			}else if(memberLevel == 2) {
				member.setMemberLevelName("판매자");
			}else if(memberLevel == 3) {
				member.setMemberLevelName("구매자");
			}
		}
		
		return memberList;
	}
		
	/**
	 * 회원 등급 조회
	 * @return List<MemberLevel>
	 */
	public List<MemberLevel> getMemberLevelList(){
		
		List<MemberLevel> memberLevelList = memberMapper.getMemberLevelList();
		
		
		
		return memberLevelList;
	}
	
	/**
	 * 회원 가입
	 * @param member
	 */
	public void addMember(Member member) {
		memberMapper.addMember(member);
		
	}
	
	/**
	 * 특정 회원 조회
	 * @param memberId
	 * @return Member
	 */
	public Member getMemberInfoById(String memberId) {
		
		Member memberInfo = memberMapper.getMemberInfoById(memberId);
		
		return memberInfo;
	}
	/**
	 * 특정 회원 수정
	 * @param member
	 * @return int (update 쿼리 실행 결과)
	 */
	public int modifyMember(Member member) {
		int result = memberMapper.modifyMember(member);
		return result;
	}
	
	/**
	 * 회원정보(비밀번호) 확인
	 * @param memberId, memberPw
	 * @return
	 */
	public Map<String, Object> checkPwMymemberId(String memberId, String memberPw) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		boolean result = false;
		
		Member member = memberMapper.getMemberInfoById(memberId);
		
		if(member != null) {
			String checkPw = member.getMemberPw();
			
			if(memberPw.equals(checkPw)) {
				result = true;
			}
		}
		
		resultMap.put("result", result);
		resultMap.put("memberInfo", member);
		
		return resultMap;
	}
	
	/**
	 * 권한별 회원 탈퇴
	 * @param member
	 * @return int
	 */
	public int removeMember(Member member) {
		int result = 0;
		
		String memberLevelName = member.getMemberLevelName();
		String memberId = member.getMemberId();
		
		if("판매자".equals(memberLevelName)) {
			result += memberMapper.removeOrderByGoodsCode(memberId);
			result += memberMapper.removerGoodsById(memberId);
		}
		if("구매자".equals(memberLevelName)) {
			result += memberMapper.removeOrderById(memberId);
		}
		result += memberMapper.removeLoginHistoryById(memberId);
		result += memberMapper.removeMemberById(memberId);
		
		return result;
	}
	
	public List<LoginHistory> getLoginHistory(){
		
		
		return null;
	}
	
	public Map<String, Object> getLoginHistory(int currentPage){
		//보여질 행의 갯수
		int rowPerPage = 10;
		
		//로그인이력 테이블의 보여질 행의 시작점
		int startRowNum = (currentPage-1)*10;
		
		//전체 행의 갯수
		double rowCnt = memberMapper.getLoginHistoryCnt();
		
		//마지막 페이지
		int lastPage = (int)Math.ceil(rowCnt/rowPerPage);
		
		//보여질 페이지 번호 구현
		//보여질 페이지 번호 초기화
		int startPageNum = 1;
		int endPageNum =10;
		
		//동적 페이지 번호 구현
		//페이지 번호 (동적) 10페이지 미만일 경우 처리
		if(currentPage > 6 && lastPage > 9) {
			startPageNum = currentPage - 5;
			endPageNum = currentPage + 4;
			if(endPageNum >= lastPage) {
				startPageNum = lastPage - 9;
				endPageNum = lastPage;
			}
		}
		
		//로그인이력 조회 시 limit 인수 파라미터 셋팅
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("startRowNum", startRowNum);
		paramMap.put("rowPerPage", rowPerPage);
		
		//로그인이력 data
		List<LoginHistory> loginHistory = memberMapper.getLoginHistory(paramMap);
		
		//controller에 전달하기 위한 파라미터 셋팅
		paramMap.clear();
		paramMap.put("loginHistory", loginHistory);
		paramMap.put("startPageNum", startPageNum);
		paramMap.put("endPageNum", endPageNum);
		paramMap.put("lastPage", lastPage);
		
		return paramMap;
	}
	
}
