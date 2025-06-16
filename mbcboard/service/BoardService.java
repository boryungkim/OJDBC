package mbcboard.service;

import java.sql.SQLException;
import java.util.Scanner;

import mbcboard.dao.BoardDAO;
import mbcboard.dao.MemberDAO;
import mbcboard.dto.BoardDTO;
import mbcboard.dto.MemberDTO;

public class BoardService {

	// �ʵ�
	public BoardDAO boardDAO = new BoardDAO();

	// ������

	// �޼���
	public void subMenu(Scanner inputStr, MemberDTO loginMember) throws SQLException {
		boolean subRun = true;
		while (subRun) {
			System.out.println("MBC ��ī���� �Խ��� �����Դϴ�");
			System.out.println("1. ��κ���");
			System.out.println("2. �Խñ� �ۼ�");
			System.out.println("3. �Խñ� �ڼ��� ����");
			System.out.println("4. �Խñ� ����");
			System.out.println("5. �Խñ� ����");
			System.out.println("6. ������");
			System.out.print(">>>");

			String subSelect = inputStr.next();

			switch (subSelect) {
			case "1":
				System.out.println("��� �Խñ� ����");
				selectAll(boardDAO);
				break;
			case "2":
				System.out.println("�Խñ� �ۼ��޼���� �����մϴ�");
				insertBoard(boardDAO, inputStr, loginMember);
				break;
			case "3":
				System.out.println("�Խñ� �ڼ��� ����");
				readPost(inputStr);
				break;
			case "4":
				System.out.println("�Խñ� ���� �޼���� �����մϴ�");
				modify(inputStr, loginMember);
				break;
			case "5":
				System.out.println("�Խñ� ���� �޼���� �����մϴ�");
				deleteOne();
				break;
			case "6":
				System.out.println("�Խñ� ���� ����");
				boardDAO.connection.close(); // �Խ��� ���� �� connection ����
				subRun = false;
				break;
			default:
				System.out.println("1~7 ������ �Է¹ٶ��ϴ�");
				break;
			}// switch�� ����(�θ޴�)
		} // while�� ���� (�θ޴�)
	}

	private void deleteOne() throws SQLException {
		System.out.println("�����Ϸ��� �Խñ��� ��ȣ�� �Է��ϼ���");
		Scanner inputInt = new Scanner(System.in);
		System.out.println();
		System.out.println(">>>");
		int selectBno = inputInt.nextInt();
		boardDAO.deleteOne(selectBno);
	}

	private void modify(Scanner inputStr, MemberDTO loginMember) throws SQLException {

		System.out.println("�����Ϸ��� �Խñ� ��ȣ�� �Է��ϼ���");
		System.out.print(">>>");
		int bno = inputStr.nextInt();

		boardDAO.modify(bno, inputStr, loginMember);
		System.out.println("==============��==============");
	}

	private void readPost(Scanner inputStr) throws SQLException {
		System.out.println("�Խñ� ��ȸ ������ �����ϼ���");
		System.out.print("1. �������� ��ȸ�ϱ� \t 2.�ۼ��� ID�� ��ȸ�ϱ�");
		System.out.println(">>>");
		String select = inputStr.next();

		switch (select) {
		case "1":
			System.out.println("��ȸ�� �Խñ� ���� >>>");
			String btitle = inputStr.next();
			boardDAO.readPost(btitle);
			break;

		case "2":
			System.out.println("��ȸ�� �ۼ��� id �Է� >>>");
			String id = inputStr.next();
			boardDAO.readByWriter(id);
			break;
		default:
			System.out.println("1 �Ǵ� 2�� �Է� �����մϴ�");
		}

	}

	private void insertBoard(BoardDAO boardDAO, Scanner inputStr, MemberDTO loginMember) throws SQLException {
		BoardDTO boardDTO = new BoardDTO();
		MemberService memberService = new MemberService();
		MemberDTO memberDTO = new MemberDTO();
		MemberDAO memberDAO = new MemberDAO();
		// �� ��ü ����

		if (loginMember == null) {
			System.out.println("�α��� �� �̿����ּ���");
			memberService.LoginMember(memberDTO, memberDAO, inputStr);

			System.out.println("�ۼ��� ID : ");
			boardDTO.setBwriter(inputStr.next());

			System.out.print("���� : ");
			boardDTO.setBtitle(inputStr.next());

			Scanner inputLine = new Scanner(System.in);
			System.out.println("���� : "); // ���Ⱑ �ִ� ���� -> nextLine()
			boardDTO.setBcontent(inputLine.nextLine());

			boardDAO.insertBoard(boardDTO, loginMember);
		}

		if (loginMember != null) {
			System.out.println("�ۼ��� : ");
			boardDTO.setBwriter(inputStr.next());

			System.out.print("���� : ");
			boardDTO.setBtitle(inputStr.next());

			Scanner inputLine = new Scanner(System.in);
			System.out.println("���� : "); // ���Ⱑ �ִ� ���� -> nextLine()
			boardDTO.setBcontent(inputLine.nextLine());

			boardDAO.insertBoard(boardDTO, loginMember);// ������ ���� ��ü�� DAO���� ����

			System.out.println("=============insertBoard �޼��� ����=============");
		}

	}

	private void selectAll(BoardDAO boardDAO) throws SQLException {
		System.out.println("=====================");
		boardDAO.selectAll();
		System.out.println("=====MBC�Խ��Ǹ���Դϴ�=====");
		System.out.println("=====================");
	}
}