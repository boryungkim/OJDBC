package mbcboard.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import mbcboard.dto.BoardDTO;
import mbcboard.dto.MemberDTO;
import mbcboard.service.MemberService;

public class BoardDAO {

	public BoardDTO boardDTO = new BoardDTO();
	public Connection connection = null;
	public Statement statement = null;
	public PreparedStatement preparedstatement = null;
	public ResultSet resultset = null;
	public int result = 0;

	public BoardDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.156:1521:xe", "boardtest2",
					"boardtest2");
		} catch (ClassNotFoundException e) {
			System.out.println("����̹� �̸��̳�, ojdbc6.jar ������ �߸��Ǿ����ϴ�");
			e.printStackTrace();
			System.exit(0);
		} catch (SQLException e) {
			System.out.println("url, id, pw�� �߸��Ǿ����ϴ�. BoardDAO�� �⺻�����ڸ� Ȯ���ϼ���");
			e.printStackTrace();
			System.exit(0);
		} // catch
	} // �⺻ ������

	public void selectAll() throws SQLException {
		try {
			String sql = "select bno, btitle, bwriter, bdate from board2 order by bdate desc";

			statement = connection.createStatement(); // ��ü ����
			resultset = statement.executeQuery(sql); // ���� -> ��� ǥ�� �ޱ�
			System.out.println("��ȣ\t ����\t �ۼ���\t �ۼ��� \t");
			while (resultset.next()) {
				System.out.print(resultset.getInt("bno"));
				System.out.print(resultset.getString("btitle") + "\t");
				System.out.print(resultset.getString("bwriter") + "\t");
				System.out.println(resultset.getDate("bdate") + "\t");
			}
			System.out.println("===================��==================");
		} catch (SQLException e) {
			System.out.println("selectAll()�޼��忡 �������� �߸��Ǿ����ϴ�");
			e.printStackTrace();
		} finally {
			resultset.close();
			statement.close();
			// connection.close();
		}

	}

	public void insertBoard(BoardDTO boardDTO, MemberDTO loginMember) throws SQLException {

		try {
			String sql = "insert into board2(bno,btitle, bcontent, bwriter,bdate)"
					+ "values(board2_seq.nextval, ?,?,?, sysdate)";
			preparedstatement = connection.prepareStatement(sql);
			preparedstatement.setString(1, boardDTO.getBtitle());
			preparedstatement.setString(2, boardDTO.getBcontent());
			preparedstatement.setString(3, boardDTO.getBwriter());
			System.out.println("���� Ȯ�� : " + sql); // ���� �ϼ��� Ȯ�ο�

			int result = preparedstatement.executeUpdate();

			if (result > 0) {
				System.out.println(result + "���� �Խù��� ��ϵǾ����ϴ�");
				connection.commit();
			} else {
				System.out.println("���� ���� ��� : " + result);
				System.out.println("�Է½���");
				connection.rollback();
			}
		} catch (SQLException e) {
			System.out.println("���ܹ߻� : insertBoard()�޼����� �������� Ȯ���ϼ���");
			e.printStackTrace();
		} finally {
			if (preparedstatement != null)
				preparedstatement.close();
		}

	}// insertBoard

	public void readPost(String btitle) throws SQLException {
		try {
			String sql = "select bno, btitle, bcontent, bwriter, bdate from board2 where btitle = ?";
			preparedstatement = connection.prepareStatement(sql);
			preparedstatement.setString(1, btitle);
			resultset = preparedstatement.executeQuery();

			boolean found = false; // ��� ���� Ȯ�ο�

			while (resultset.next()) {
				found = true;
				BoardDTO boardDTO = new BoardDTO();
				boardDTO.setBno(resultset.getInt("bno"));
				boardDTO.setBtitle(resultset.getString("btitle"));
				boardDTO.setBcontent(resultset.getString("bcontent"));
				boardDTO.setBwriter(resultset.getString("bwriter"));
				boardDTO.setBdate(resultset.getDate("bdate"));
				// ������ ���̽��� �ִ� ���� ��ü�� �ֱ� �Ϸ�

				System.out.println("========================");
				System.out.println("��ȣ : " + boardDTO.getBno());
				System.out.println("���� : " + boardDTO.getBtitle());
				System.out.println("���� : " + boardDTO.getBcontent());
				System.out.println("�ۼ��� : " + boardDTO.getBwriter());
				System.out.println("�ۼ��� : " + boardDTO.getBdate());

			}
			if (!found) {
				System.out.println("�Խù��� �������� �ʽ��ϴ�");
			}
		} catch (SQLException e) {
			System.out.println("���ܹ߻� : readOne() �޼��带 Ȯ���ϼ���");
			e.printStackTrace();
		} finally {
			// �׻� ���๮
			resultset.close();
			preparedstatement.close();
		}
	}

	public void modify(int bno, Scanner inputStr, MemberDTO loginMember) throws SQLException {
		BoardDTO boardDTO = new BoardDTO();

		if (loginMember == null) {
			System.out.println("�α��� �� �̿��� �� �ִ� �����Դϴ�");
			System.out.println("�α��� �� �̿����ּ���");
			return;
		}
		String checkSql = "select bwriter from board2 where bno =?";
		preparedstatement = connection.prepareStatement(checkSql);
		preparedstatement.setInt(1, bno);
		resultset = preparedstatement.executeQuery();
		if (resultset.next()) {
			String writer = resultset.getString("bwriter");
			if (!writer.equals(loginMember.getId())) {
				System.out.println("���� ������ �����ϴ�");
				return;
			}
		} else {
			System.out.println("�ش� ��ȣ�� �Խñ��� �������� �ʽ��ϴ�");
			return;
		}

		System.out.println("[������ ������ �Է��ϼ���]");
		System.out.print("�� ���� : ");
		boardDTO.setBtitle(inputStr.next());

		Scanner inputLine = new Scanner(System.in);
		System.out.println("�� ���� : ");
		boardDTO.setBcontent(inputLine.nextLine());

		try {
			String sql = "update board2 set btitle=? , bcontent = ? , bdate = sysdate where bno = ?";
			preparedstatement = connection.prepareStatement(sql);
			preparedstatement.setString(1, boardDTO.getBtitle());
			preparedstatement.setString(2, boardDTO.getBcontent());
			preparedstatement.setInt(3, bno);

			result = preparedstatement.executeUpdate(); // ������ ������ ����� ������ ����
			if (result > 0) {
				System.out.println(result + "���� �����Ͱ� �����Ǿ����ϴ�");
				connection.commit();
			} else {
				System.out.println("�������� �ʾҽ��ϴ�. �ۼ��ڸ� �Խñ� ���� ������ �ֽ��ϴ�!");
				connection.rollback();
			}
		} catch (SQLException e) {
			System.out.println("���ܹ߻� : modify() �޼���� sql���� Ȯ���ϼ���!");
			e.printStackTrace();
		} finally {
			preparedstatement.close();
		}
		}

	

	public void deleteOne(int selectBno) throws SQLException {
		try {
			String sql = "delete from board2 where bno = ?";
			preparedstatement = connection.prepareStatement(sql);
			preparedstatement.setInt(1, selectBno);

			result = preparedstatement.executeUpdate();
			if (result > 0) {
				System.out.println(result + "�Խù��� ���� �Ǿ����ϴ�");
				connection.commit();
			} else {
				System.out.println("�Խù��� �������� �ʾҽ��ϴ� " + "�α��� �� �Խñ� �ۼ��ڸ� ������ �����մϴ�!");
				connection.rollback();
			}
			System.out.println("=========================");
			selectAll();
		} catch (SQLException e) {
			System.out.println("���ܹ߻�");
			e.printStackTrace();
		} finally {
			preparedstatement.close();
		}

	}

	public void readByWriter(String id) throws SQLException {
		String sql = "select * from board2 where bwriter =?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, id);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					System.out.println("��ȣ : " + rs.getInt("bno"));
					System.out.println("���� : " + rs.getString("btitle"));
					System.out.println("���� : " + rs.getString("bcontent"));
					System.out.println("�ۼ��� : " + rs.getString("bwriter"));
					System.out.println("�ۼ��� : " + rs.getDate("bdate"));
					System.out.println("---------------------------");
				}
			}
		}

	}

}// class