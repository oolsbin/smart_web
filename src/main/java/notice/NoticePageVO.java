package notice;

import java.util.List;

import common.PageVO;

public class NoticePageVO extends PageVO{
	private List<NoticeVO> list; //공지글 10건만 담을 필드

	public List<NoticeVO> getList() {
		return list;
	}

	public void setList(List<NoticeVO> list) {
		this.list = list;
	}
}
