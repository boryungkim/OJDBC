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
			System.out.println("드라이버 이름이나, ojdbc6.jar 파일이 잘못되었습니다");
			e.printStackTrace();
			System.exit(0);
		} catch (SQLException e) {
			System.out.println("url, id, pw가 잘못되었습니다. BoardDAO에 기본생성자를 확인하세요");
			e.printStackTrace();
			System.exit(0);
		} // catch
	} // 기본 생성자

	public void selectAll() throws SQLException {
		try {
			String sql = "select bno, btitle, bwriter, bdate from board2 order by bdate desc";

			statement = connection.createStatement(); // 객체 생성
			resultset = statement.executeQuery(sql); // 실행 -> 결과 표로 받기
			System.out.println("번호\t 제목\t 작성자\t 작성일 \t");
			while (resultset.next()) {
				System.out.print(resultset.getInt("bno"));
				System.out.print(resultset.getString("btitle") + "\t");
				System.out.print(resultset.getString("bwriter") + "\t");
				System.out.println(resultset.getDate("bdate") + "\t");
			}
			System.out.println("===================끝==================");
		} catch (SQLException e) {
			System.out.println("selectAll()메서드에 쿼리문이 잘못되었습니다");
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
			System.out.println("쿼리 확인 : " + sql); // 쿼리 완성본 확인용

			int result = preparedstatement.executeUpdate();

			if (result > 0) {
				System.out.println(result + "개의 게시물이 등록되었습니다");
				connection.commit();
			} else {
				System.out.println("쿼리 실행 결과 : " + result);
				System.out.println("입력실패");
				connection.rollback();
			}
		} catch (SQLException e) {
			System.out.println("예외발생 : insertBoard()메서드의 쿼리문을 확인하세요");
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

			boolean found = false; // 결과 유무 확인용

			while (resultset.next()) {
				found = true;
				BoardDTO boardDTO = new BoardDTO();
				boardDTO.setBno(resultset.getInt("bno"));
				boardDTO.setBtitle(resultset.getString("btitle"));
				boardDTO.setBcontent(resultset.getString("bcontent"));
				boardDTO.setBwriter(resultset.getString("bwriter"));
				boardDTO.setBdate(resultset.getDate("bdate"));
				// 데이터 베이스에 있는 행을 객체에 넣기 완료

				System.out.println("========================");
				System.out.println("번호 : " + boardDTO.getBno());
				System.out.println("제목 : " + boardDTO.getBtitle());
				System.out.println("내용 : " + boardDTO.getBcontent());
				System.out.println("작성자 : " + boardDTO.getBwriter());
				System.out.println("작성일 : " + boardDTO.getBdate());

			}
			if (!found) {
				System.out.println("게시물이 존재하지 않습니다");
			}
		} catch (SQLException e) {
			System.out.println("예외발생 : readOne() 메서드를 확인하세요");
			e.printStackTrace();
		} finally {
			// 항상 실행문
			resultset.close();
			preparedstatement.close();
		}
	}

	public void modify(int bno, Scanner inputStr, MemberDTO loginMember) throws SQLException {
		BoardDTO boardDTO = new BoardDTO();

		if (loginMember == null) {
			System.out.println("로그인 후 이용할 수 있는 서비스입니다");
			System.out.println("로그인 후 이용해주세요");
			return;
		}
		String checkSql = "select bwriter from board2 where bno =?";
		preparedstatement = connection.prepareStatement(checkSql);
		preparedstatement.setInt(1, bno);
		resultset = preparedstatement.executeQuery();
		if (resultset.next()) {
			String writer = resultset.getString("bwriter");
			if (!writer.equals(loginMember.getId())) {
				System.out.println("수정 권한이 없습니다");
				return;
			}
		} else {
			System.out.println("해당 번호의 게시글이 존재하지 않습니다");
			return;
		}

		System.out.println("[수정할 내용을 입력하세요]");
		System.out.print("새 제목 : ");
		boardDTO.setBtitle(inputStr.next());

		Scanner inputLine = new Scanner(System.in);
		System.out.println("새 내용 : ");
		boardDTO.setBcontent(inputLine.nextLine());

		try {
			String sql = "update board2 set btitle=? , bcontent = ? , bdate = sysdate where bno = ?";
			preparedstatement = connection.prepareStatement(sql);
			preparedstatement.setString(1, boardDTO.getBtitle());
			preparedstatement.setString(2, boardDTO.getBcontent());
			preparedstatement.setInt(3, bno);

			result = preparedstatement.executeUpdate(); // 쿼리문 실행후 결과를 정수로 보냄
			if (result > 0) {
				System.out.println(result + "개의 데이터가 수정되었습니다");
				connection.commit();
			} else {
				System.out.println("수정되지 않았습니다. 작성자만 게시글 삭제 권한이 있습니다!");
				connection.rollback();
			}
		} catch (SQLException e) {
			System.out.println("예외발생 : modify() 메서드와 sql문을 확인하세요!");
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
				System.out.println(result + "게시물이 삭제 되었습니다");
				connection.commit();
			} else {
				System.out.println("게시물이 삭제되지 않았습니다 " + "로그인 한 게시글 작성자만 삭제가 가능합니다!");
				connection.rollback();
			}
			System.out.println("=========================");
			selectAll();
		} catch (SQLException e) {
			System.out.println("예외발생");
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
					System.out.println("번호 : " + rs.getInt("bno"));
					System.out.println("제목 : " + rs.getString("btitle"));
					System.out.println("내용 : " + rs.getString("bcontent"));
					System.out.println("작성자 : " + rs.getString("bwriter"));
					System.out.println("작성일 : " + rs.getDate("bdate"));
					System.out.println("---------------------------");
				}
			}
		}

	}

}// class