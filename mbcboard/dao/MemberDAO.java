package mbcboard.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import mbcboard.dto.MemberDTO;

public class MemberDAO {
	public Connection connection = null;

	public MemberDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.156:1521:xe", "boardtest2",
					"boardtest2");
		} catch (Exception e) {
			System.out.println("DRIVER 로딩 실패");
			e.printStackTrace();
		}
	} // 기본 생성자

	public void addMember(MemberDTO memberDTO) throws SQLException {
		String sql = "insert into memberDTO (mno, bwriter, id, pw, regidate, balance, accountNum)"
				+ "values (memberDTO_seq.nextval, ?,?,?, sysdate, ?, memberDTO_seq.nextval)";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, memberDTO.getbwriter());
			pstmt.setString(2, memberDTO.getId());
			pstmt.setString(3, memberDTO.getPw());
			pstmt.setInt(4, memberDTO.getBalance());

			int result = pstmt.executeUpdate();

			if (result > 0) { // 실행값 있을 때
				System.out.println("회원가입 성공");
				connection.commit();
			} else {
				System.out.println("회원가입 실패");
				connection.rollback();
			}
		} // try
		catch (SQLException e) { // 예외 발생 -> catch
			System.out.println("addMember() SQL 예외발생");
			try {
				connection.rollback(); // 작업 취소
			} catch (SQLException e2) { // rollback 실패 시 또 다른 예외 처리
				e2.printStackTrace();
			}
			e.printStackTrace();
		}

	}

	public void ReadAllMember() {
		System.out.println("전체 회원 조회 메서드입니다");
		String sql = "select mno, bwriter, id, pw, regidate from memberDTO";

		try (PreparedStatement pstmt = connection.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

			System.out.println("회원번호\t 이름 \t id \t 비밀번호 \t 가입일자");
			System.out.println("================================");

			while (rs.next()) {
				int mno = rs.getInt("mno");
				String bwriter = rs.getString("bwriter");
				String id = rs.getString("id");
				String pw = rs.getString("pw");
				String regidate = rs.getString("regidate");

				System.out.println(mno + "\t" + bwriter + "\t" + id + "\t" + pw + "\t" + regidate);
			} // while
		} // try
		catch (SQLException e) {
			System.out.println("예외 발생");
			e.printStackTrace();
		}

	}

	public void readOne(String id) throws SQLException {
		String sql = "select mno, bwriter, id, pw, balance, accountNum, regidate from memberDTO where id = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, id); // 첫번째 ? 에 id 입력값 넣음
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					System.out.println("회원번호 : " + rs.getInt("mno"));
					System.out.println("이름: " + rs.getString("bwriter"));
					System.out.println("ID: " + rs.getString("id"));
					System.out.println("비밀번호: " + rs.getString("pw"));
					System.out.println("포인트잔액" + rs.getInt("balance"));
					System.out.println("계좌번호:" + rs.getString("accountNum"));
					System.out.println("가입일자: " + rs.getString("regidate"));
				} else {
					System.out.println("일치하는 id를 찾을 수 없습니다");
				} // else
			} // try
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void modifyMember(MemberDTO memberDTO, String newId, String newpw) {

		String sql = "UPDATE memberDTO SET id = ?, pw = ?, regidate = SYSDATE WHERE id = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) { 
			pstmt.setString(1, newId);
			pstmt.setString(2, newpw);
			pstmt.setString(3, memberDTO.getId());

			int result = pstmt.executeUpdate();

			if (result > 0) {
				System.out.println("회원 정보 수정 성공");
				connection.commit();
			} else {
				System.out.println("수정 실패: 해당 ID가 존재하지 않음");
				connection.rollback();
			}

		} catch (SQLException e) {
			System.out.println("updateMember() 실행 중 예외 발생");
			try {
				connection.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	public void deleteOne(int mno, String id) throws SQLException {
		
		String deleteBoardSql = "delete from board2 where bwriter = ?";
		String deleteMemberSql = "delete from memberDTO where mno = ?";

		try (PreparedStatement pstmtBoard = connection.prepareStatement(deleteBoardSql); 
			PreparedStatement pstmtMember = connection.prepareStatement(deleteMemberSql)){
			
			//게시글 삭제
			pstmtBoard.setString(1, id);
			int boardDeleted = pstmtBoard.executeUpdate();
			
			pstmtMember.setInt(1, mno);
			int memberDeleted = pstmtMember.executeUpdate();

			if (memberDeleted > 0) {
				System.out.println("회원삭제 성공");
				if(boardDeleted > 0 ) {
				System.out.println("회원이 작성한 게시글" + boardDeleted + "개가 함께 삭제되었습니다");
				} else {
					System.out.println("회원이 작성한 게시글이 없어 따로 삭제되지 않습니다");
				}
				connection.commit();
			} else {
				System.out.println("삭제 실패");
				connection.rollback();
			}
		} catch (SQLException e) {
			System.out.println("deleteOne() 실행 중 예외발생");
			try {
				connection.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	public MemberDTO LoginMember(String id, String pw) throws SQLException {
		String sql = "select * from memberDTO where id = ? and pw =?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					MemberDTO memberDTO = new MemberDTO();
					memberDTO.setMno(rs.getInt("mno"));
					memberDTO.setbwriter(rs.getString("bwriter"));
					memberDTO.setId(rs.getString("id"));
					memberDTO.setPw(rs.getString("pw"));
					memberDTO.setRegidate(rs.getDate("regidate"));
					return memberDTO;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	public void charge(MemberDTO memberDTO, Scanner inputStr) {
		String sql = "UPDATE memberDTO SET balance = ? WHERE id = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, memberDTO.getId());
			pstmt.setInt(2, memberDTO.getBalance());

			int result = pstmt.executeUpdate();

			if (result > 0) {
				System.out.println("회원 정보 수정 성공");
				connection.commit();
			} else {
				System.out.println("수정 실패: 해당 ID가 존재하지 않음");
				connection.rollback();
			}

		} catch (SQLException e) {
			System.out.println("charge() 실행 중 예외 발생");
			try {
				connection.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		}
	}
}