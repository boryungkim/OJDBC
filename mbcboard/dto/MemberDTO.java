package mbcboard.dto;

import java.sql.Date;

public class MemberDTO {

	private int mno;
	private String bwriter;
	private String id;
	private String pw;
	private Date regidate;
	private int balance;
	private String accountNum;
	
	//기본 생성자
	
	public MemberDTO() {
		
	}

	//GETTER SETTER
	
	public int getMno() {
		return mno;
	}

	public void setMno(int mno) {
		this.mno = mno;
	}

	public String getbwriter() {
		return bwriter;
	}

	public void setbwriter(String bwriter) {
		this.bwriter = bwriter;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public Date getRegidate() {
		return regidate;
	}

	public void setRegidate(Date regidate) {
		this.regidate = regidate;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}
	
	
	
}