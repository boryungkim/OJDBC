package mbcboard.service;

import java.sql.SQLException;
import java.util.Scanner;

import mbcboard.dao.BoardDAO;
import mbcboard.dao.MemberDAO;
import mbcboard.dto.BoardDTO;
import mbcboard.dto.MemberDTO;

public class BoardService {

	// 필드
	public BoardDAO boardDAO = new BoardDAO();

	// 생성자

	// 메서드
	public void subMenu(Scanner inputStr, MemberDTO loginMember) throws SQLException {
		boolean subRun = true;
		while (subRun) {
			System.out.println("MBC 아카데미 게시판 서비스입니다");
			System.out.println("1. 모두보기");
			System.out.println("2. 게시글 작성");
			System.out.println("3. 게시글 자세히 보기");
			System.out.println("4. 게시글 수정");
			System.out.println("5. 게시글 삭제");
			System.out.println("6. 나가기");
			System.out.print(">>>");

			String subSelect = inputStr.next();

			switch (subSelect) {
			case "1":
				System.out.println("모든 게시글 보기");
				selectAll(boardDAO);
				break;
			case "2":
				System.out.println("게시글 작성메서드로 진입합니다");
				insertBoard(boardDAO, inputStr, loginMember);
				break;
			case "3":
				System.out.println("게시글 자세히 보기");
				readPost(inputStr);
				break;
			case "4":
				System.out.println("게시글 수정 메서드로 진입합니다");
				modify(inputStr, loginMember);
				break;
			case "5":
				System.out.println("게시글 삭제 메서드로 진입합니다");
				deleteOne();
				break;
			case "6":
				System.out.println("게시글 보기 종료");
				boardDAO.connection.close(); // 게시판 종료 시 connection 종료
				subRun = false;
				break;
			default:
				System.out.println("1~7 까지만 입력바랍니다");
				break;
			}// switch문 종료(부메뉴)
		} // while문 종료 (부메뉴)
	}

	private void deleteOne() throws SQLException {
		System.out.println("삭제하려는 게시글의 번호를 입력하세요");
		Scanner inputInt = new Scanner(System.in);
		System.out.println();
		System.out.println(">>>");
		int selectBno = inputInt.nextInt();
		boardDAO.deleteOne(selectBno);
	}

	private void modify(Scanner inputStr, MemberDTO loginMember) throws SQLException {

		System.out.println("수정하려는 게시글 번호를 입력하세요");
		System.out.print(">>>");
		int bno = inputStr.nextInt();

		boardDAO.modify(bno, inputStr, loginMember);
		System.out.println("==============끝==============");
	}

	private void readPost(Scanner inputStr) throws SQLException {
		System.out.println("게시글 조회 기준을 선택하세요");
		System.out.print("1. 제목으로 조회하기 \t 2.작성자 ID로 조회하기");
		System.out.println(">>>");
		String select = inputStr.next();

		switch (select) {
		case "1":
			System.out.println("조회할 게시글 제목 >>>");
			String btitle = inputStr.next();
			boardDAO.readPost(btitle);
			break;

		case "2":
			System.out.println("조회할 작성자 id 입력 >>>");
			String id = inputStr.next();
			boardDAO.readByWriter(id);
			break;
		default:
			System.out.println("1 또는 2만 입력 가능합니다");
		}

	}

	private void insertBoard(BoardDAO boardDAO, Scanner inputStr, MemberDTO loginMember) throws SQLException {
		BoardDTO boardDTO = new BoardDTO();
		MemberService memberService = new MemberService();
		MemberDTO memberDTO = new MemberDTO();
		MemberDAO memberDAO = new MemberDAO();
		// 빈 객체 생성

		if (loginMember == null) {
			System.out.println("로그인 후 이용해주세요");
			memberService.LoginMember(memberDTO, memberDAO, inputStr);

			System.out.println("작성자 ID : ");
			boardDTO.setBwriter(inputStr.next());

			System.out.print("제목 : ");
			boardDTO.setBtitle(inputStr.next());

			Scanner inputLine = new Scanner(System.in);
			System.out.println("내용 : "); // 띄어쓰기가 있는 문장 -> nextLine()
			boardDTO.setBcontent(inputLine.nextLine());

			boardDAO.insertBoard(boardDTO, loginMember);
		}

		if (loginMember != null) {
			System.out.println("작성자 : ");
			boardDTO.setBwriter(inputStr.next());

			System.out.print("제목 : ");
			boardDTO.setBtitle(inputStr.next());

			Scanner inputLine = new Scanner(System.in);
			System.out.println("내용 : "); // 띄어쓰기가 있는 문장 -> nextLine()
			boardDTO.setBcontent(inputLine.nextLine());

			boardDAO.insertBoard(boardDTO, loginMember);// 위에서 만든 객체를 DAO에게 전달

			System.out.println("=============insertBoard 메서드 종료=============");
		}

	}

	private void selectAll(BoardDAO boardDAO) throws SQLException {
		System.out.println("=====================");
		boardDAO.selectAll();
		System.out.println("=====MBC게시판목록입니다=====");
		System.out.println("=====================");
	}
}