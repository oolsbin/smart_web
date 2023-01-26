package board;

import java.sql.Date;
import java.util.List;

public class BoardVO {
	private int id, readcnt, no, filecnt;
	private String title, content, writer, name;
	private Date writedate;
	private List<BoardFileVO> fileList;
	
	public int getFilecnt() {
		return filecnt;
	}
	public void setFilecnt(int filecnt) {
		this.filecnt = filecnt;
	}
	public List<BoardFileVO> getFileList() {
		return fileList;
	}
	public void setFileList(List<BoardFileVO> fileList) {
		this.fileList = fileList;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getReadcnt() {
		return readcnt;
	}
	public void setReadcnt(int readcnt) {
		this.readcnt = readcnt;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getWritedate() {
		return writedate;
	}
	public void setWritedate(Date writedate) {
		this.writedate = writedate;
	}
	
}
