package common;

public class PageVO {
	private int totalList; //총 목록수 : DB에서 조회
	private int pageList = 10;//페이지당 보여질 목록 수 : 4
	private int blockPage = 10; //블럭당 보여질 페이지의 수 : 3
	 
	private int totalPage;  //총 페이지수 : 8 페이지 = 30 / 4 = 7 ... 2
	private int totalBlock; //총 블록수 : 3 블록 = 8 / 3 = 2 ... 2

	private int curPage = 1;   //현재 페이지번호
	private int endList;   //각 페이지의 끝 목록번호 :  총 목록수 - (페이지번호-1) * 페이지당 보여질 목록수  
	private int beginList; //각 페이지의 시작 목록번호 :  끝 목록번호 - (페이지당 보여질 목록수-1)
	 
	private int curBlock; //현재 블록번호 : 페이지번호 / 블록당 보여질 페이지수
	private int endPage; //각 블럭의 끝 페이지번호 : 블록번호 * 블록당 보여질 페이지수
	private int beginPage; //각 블럭의 시작 페이지번호 : 끝 페이지번호 - (블럭당 보여질 페이지수-1)

	private String search, keyword, viewType="list"; //검색조건/검색어/보기형태
	
	public String getViewType() {
		return viewType;
	}
	public void setViewType(String viewType) {
		this.viewType = viewType;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public int getTotalList() {
		return totalList;
	}
	public void setTotalList(int totalList) {
		this.totalList = totalList;
		//DB에서 조회해온 총 목록수에 따라 총 페이지수가 결정된다
		totalPage = totalList / pageList; //  442 / 10 = 44..2 -> 45 
		if( totalList % pageList > 0 ) ++totalPage;
		
		//총 페이지수에 따라 총 블럭수가 결정된다.
		totalBlock = totalPage / blockPage; // 45 / 10 = 4 ...5
		if( totalPage % blockPage > 0 ) ++totalBlock;
		
		//각 페이지의 끝 목록번호 :  총 목록수 - (페이지번호-1) * 페이지당 보여질 목록수  
		//각 페이지의 시작 목록번호 :  끝 목록번호 - (페이지당 보여질 목록수-1)
		endList = totalList - (curPage-1) * pageList;
		beginList = endList - (pageList-1);
		
		//현재 블록번호 : 페이지번호 / 블록당 보여질 페이지수
		//각 블럭의 끝 페이지번호 : 블록번호 * 블록당 보여질 페이지수
		//각 블럭의 시작 페이지번호 : 끝 페이지번호 - (블럭당 보여질 페이지수-1)
		curBlock = curPage / blockPage;
		if( curPage % blockPage > 0 ) ++curBlock;
		endPage = curBlock * blockPage;
		beginPage = endPage - (blockPage-1);
		
		//끝 페이지번호가 총 페이지수보다 클 수 없으므로
		//총 페이지수를 끝 페이지번호로 한다.
		if( endPage > totalPage ) endPage = totalPage;		
	}
	
	public int getPageList() {
		return pageList;
	}
	public void setPageList(int pageList) {
		this.pageList = pageList;
	}
	public int getBlockPage() {
		return blockPage;
	}
	public void setBlockPage(int blockPage) {
		this.blockPage = blockPage;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getTotalBlock() {
		return totalBlock;
	}
	public void setTotalBlock(int totalBlock) {
		this.totalBlock = totalBlock;
	}
	public int getCurPage() {
		return curPage;
	}
	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}
	public int getEndList() {
		return endList;
	}
	public void setEndList(int endList) {
		this.endList = endList;
	}
	public int getBeginList() {
		return beginList;
	}
	public void setBeginList(int beginList) {
		this.beginList = beginList;
	}
	public int getCurBlock() {
		return curBlock;
	}
	public void setCurBlock(int curBlock) {
		this.curBlock = curBlock;
	}
	public int getEndPage() {
		return endPage;
	}
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
	public int getBeginPage() {
		return beginPage;
	}
	public void setBeginPage(int beginPage) {
		this.beginPage = beginPage;
	}
	 
	 
}
