package mbcboard;

import java.sql.SQLException;
import java.util.Scanner;

import mbcboard.dto.MemberDTO;
import mbcboard.service.BoardService;
import mbcboard.service.MemberService;

public class BoardExam {

	public static Scanner inputStr = new Scanner(System.in);
	
	public static MemberDTO loginMember = null;
	
	public static void main(String[] args) throws SQLException {

		boolean run = true;
		while (run) {
			System.out.println("MBC 자유 게시판입니다");
			System.out.println("1. 회원");
			System.out.println("2. 게시판");
			System.out.println("3. 종료");
			System.out.print(">>>");
			String select = inputStr.next();

			switch (select) {
			case "1":
				System.out.println("회원용 서비스로 진입합니다");
				MemberService memberService = new MemberService();
				memberService.subMenu(inputStr);
				break;
			case "2":
				System.out.println("게시판 서비스로 진입합니다");
				BoardService boardService = new BoardService();
				boardService.subMenu(inputStr, loginMember);
				break;
			case "3":
				System.out.println("자유게시판 프로그램을 종료합니다");
				run = false;
				break;
				
			default:
				System.out.println("1~3 까지만 입력 바랍니다");

			}// switch 문 종료
		} // while문 종료

	}

}
