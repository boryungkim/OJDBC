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
			System.out.println("DRIVER �ε� ����");
			e.printStackTrace();
		}
	} // �⺻ ������

	public void addMember(MemberDTO memberDTO) throws SQLException {
		String sql = "insert into memberDTO (mno, bwriter, id, pw, regidate, balance, accountNum)"
				+ "values (memberDTO_seq.nextval, ?,?,?, sysdate, ?, memberDTO_seq.nextval)";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, memberDTO.getbwriter());
			pstmt.setString(2, memberDTO.getId());
			pstmt.setString(3, memberDTO.getPw());
			pstmt.setInt(4, memberDTO.getBalance());

			int result = pstmt.executeUpdate();

			if (result > 0) { // ���ప ���� ��
				System.out.println("ȸ������ ����");
				connection.commit();
			} else {
				System.out.println("ȸ������ ����");
				connection.rollback();
			}
		} // try
		catch (SQLException e) { // ���� �߻� -> catch
			System.out.println("addMember() SQL ���ܹ߻�");
			try {
				connection.rollback(); // �۾� ���
			} catch (SQLException e2) { // rollback ���� �� �� �ٸ� ���� ó��
				e2.printStackTrace();
			}
			e.printStackTrace();
		}

	}

	public void ReadAllMember() {
		System.out.println("��ü ȸ�� ��ȸ �޼����Դϴ�");
		String sql = "select mno, bwriter, id, pw, regidate from memberDTO";

		try (PreparedStatement pstmt = connection.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

			System.out.println("ȸ����ȣ\t �̸� \t id \t ��й�ȣ \t ��������");
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
			System.out.println("���� �߻�");
			e.printStackTrace();
		}

	}

	public void readOne(String id) throws SQLException {
		String sql = "select mno, bwriter, id, pw, balance, accountNum, regidate from memberDTO where id = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, id); // ù��° ? �� id �Է°� ����
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					System.out.println("ȸ����ȣ : " + rs.getInt("mno"));
					System.out.println("�̸�: " + rs.getString("bwriter"));
					System.out.println("ID: " + rs.getString("id"));
					System.out.println("��й�ȣ: " + rs.getString("pw"));
					System.out.println("����Ʈ�ܾ�" + rs.getInt("balance"));
					System.out.println("���¹�ȣ:" + rs.getString("accountNum"));
					System.out.println("��������: " + rs.getString("regidate"));
				} else {
					System.out.println("��ġ�ϴ� id�� ã�� �� �����ϴ�");
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
				System.out.println("ȸ�� ���� ���� ����");
				connection.commit();
			} else {
				System.out.println("���� ����: �ش� ID�� �������� ����");
				connection.rollback();
			}

		} catch (SQLException e) {
			System.out.println("updateMember() ���� �� ���� �߻�");
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
			
			//�Խñ� ����
			pstmtBoard.setString(1, id);
			int boardDeleted = pstmtBoard.executeUpdate();
			
			pstmtMember.setInt(1, mno);
			int memberDeleted = pstmtMember.executeUpdate();

			if (memberDeleted > 0) {
				System.out.println("ȸ������ ����");
				if(boardDeleted > 0 ) {
				System.out.println("ȸ���� �ۼ��� �Խñ�" + boardDeleted + "���� �Բ� �����Ǿ����ϴ�");
				} else {
					System.out.println("ȸ���� �ۼ��� �Խñ��� ���� ���� �������� �ʽ��ϴ�");
				}
				connection.commit();
			} else {
				System.out.println("���� ����");
				connection.rollback();
			}
		} catch (SQLException e) {
			System.out.println("deleteOne() ���� �� ���ܹ߻�");
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
				System.out.println("ȸ�� ���� ���� ����");
				connection.commit();
			} else {
				System.out.println("���� ����: �ش� ID�� �������� ����");
				connection.rollback();
			}

		} catch (SQLException e) {
			System.out.println("charge() ���� �� ���� �߻�");
			try {
				connection.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		}
	}
}