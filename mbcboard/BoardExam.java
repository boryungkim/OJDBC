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
			System.out.println("MBC ���� �Խ����Դϴ�");
			System.out.println("1. ȸ��");
			System.out.println("2. �Խ���");
			System.out.println("3. ����");
			System.out.print(">>>");
			String select = inputStr.next();

			switch (select) {
			case "1":
				System.out.println("ȸ���� ���񽺷� �����մϴ�");
				MemberService memberService = new MemberService();
				memberService.subMenu(inputStr);
				break;
			case "2":
				System.out.println("�Խ��� ���񽺷� �����մϴ�");
				BoardService boardService = new BoardService();
				boardService.subMenu(inputStr, loginMember);
				break;
			case "3":
				System.out.println("�����Խ��� ���α׷��� �����մϴ�");
				run = false;
				break;
				
			default:
				System.out.println("1~3 ������ �Է� �ٶ��ϴ�");

			}// switch �� ����
		} // while�� ����

	}

}
