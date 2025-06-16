package mbcboard.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import mbcboard.BoardExam;
import mbcboard.dao.BoardDAO;
import mbcboard.dao.MemberDAO;
import mbcboard.dto.BoardDTO;
import mbcboard.dto.MemberDTO;

public class MemberService {

	private MemberDAO memberDAO = new MemberDAO();
	private MemberDTO memberDTO = new MemberDTO();

	public void subMenu(Scanner inputStr) throws SQLException {

		boolean subRun = true;
		while (subRun) {
			System.out.println("MBC ��ī���� ȸ�� �����Դϴ�");
			System.out.println("0. ȸ�� �α���");
			System.out.println("1. ȸ������");
			System.out.println("2. ��üȸ�������ȸ");
			System.out.println("3. ����ȸ������ȸ");
			System.out.println("4. ȸ�� ���� ����");
			System.out.println("5. ȸ�� ����");
			System.out.println("6. ȸ������ ����Ʈ ����");
			System.out.println("7. ������");
			System.out.print(">>>");

			String subSelect = inputStr.next();

			switch (subSelect) {
			case "0":
				System.out.println("ȸ�� �α��� �޴��Դϴ�");
				LoginMember(memberDTO, memberDAO, inputStr);
				break;

			case "1":
				System.out.println("ȸ�������ϱ� �޴��Դϴ�");
				AddMember(inputStr);
				break;
			case "2":
				System.out.println("��üȸ����� �޴��Դϴ�");
				ReadALLMember(memberDTO, memberDAO, inputStr);
				break;
			case "3":
				if (!isLogin())
					break;
				System.out.println("����ȸ�� ����ȸ �޴��Դϴ�");
				readOne(memberDTO, memberDAO, inputStr);
				break;
			case "4":
				if(!isLogin())
					break;
				System.out.println("ȸ������ ���� �޴��Դϴ�");
				modifyMember(memberDTO, memberDAO, inputStr);
				break;
			case "5":
				if(!isLogin())
					break;
				System.out.println("ȸ������ �޴��Դϴ�");
				deleteOne(memberDTO, memberDAO, inputStr);
				break;
			case "6":
				if(!isLogin())
					break;
				System.out.println("ȸ���� ����Ʈ ���� �޴��Դϴ�");
				charge(memberDTO, memberDAO, inputStr);
				break;

			case "7":
				System.out.println("ȸ�� �޴� ����");
				memberDAO.connection.close(); // �Խ��� ���� �� connection ����
				subRun = false;
				break;

			default:
				System.out.println("1~7������ �Է¹ٶ��ϴ�");
				break;
			}// switch�� ����(�θ޴�)
		} // while�� ���� (�θ޴�)
	}

	private boolean isLogin() { //�α��� Ȯ�� �޼��� �����
		if(BoardExam.loginMember == null) {
			System.out.println("�α��� �� �̿��� �� �ִ� �����Դϴ�");
			return false;
		}
		return true;
	}

	private void charge(MemberDTO memberDTO, MemberDAO memberDAO, Scanner inputStr) throws SQLException {
		Scanner inputInt = new Scanner(System.in);

		if (BoardExam.loginMember != null) {
			System.out.println("�����Ͻ� ���� id�� �Է��ϼ���");
			String id = inputStr.next();
			System.out.println("������ ����Ʈ�� �Է��ϼ���>>>");
			int point = inputInt.nextInt();

			memberDTO.setBalance(memberDTO.getBalance() + point);
			memberDAO.charge(memberDTO, inputStr);

		} else {
			System.out.println("���� ���� : �α��ε��� �ʾҽ��ϴ�");
		}

	}

	private void deleteOne(MemberDTO memberDTO, MemberDAO memberDAO, Scanner inputStr) throws SQLException {

		if (BoardExam.loginMember != null) {
			memberDAO.deleteOne(BoardExam.loginMember.getMno(), BoardExam.loginMember.getId());
		} else {
			System.out.println("���� ���� : �α��ε��� �ʾҽ��ϴ�");
		}
	}

	public static void LoginMember(MemberDTO memberDTO, MemberDAO memberDAO, Scanner inputStr) throws SQLException {
		System.out.print("ID �Է� >>> ");
		String id = inputStr.next();
		System.out.print("��й�ȣ �Է� >>> ");
		String pw = inputStr.next();

		MemberDTO member = memberDAO.LoginMember(id, pw);
		if (member != null) {
			BoardExam.loginMember = member; // �α��� �����ϸ� boardexam�ʵ忡 �����
			BoardExam.loginMember = member;
			System.out.println("�α��� ����!");
		} else {
			System.out.println("�α��� ����!");
		}
	}

	private void modifyMember(MemberDTO memberDTO, MemberDAO memberDAO, Scanner inputStr) throws SQLException { // �α��� ��
																												// �̿�
																												// ����?
		System.out.print("�� ID >>> ");
		String newId = inputStr.next();

		System.out.print("�� ��й�ȣ >>> ");
		String newpw = inputStr.next();

		memberDTO.setId(BoardExam.loginMember.getId());

		memberDAO.modifyMember(memberDTO, newId, newpw);

		BoardExam.loginMember.setId(newId);
		BoardExam.loginMember.setPw(newpw);
	}

	private void readOne(MemberDTO memberDTO, MemberDAO memberDAO, Scanner inputStr) throws SQLException {

		System.out.println("ã�� ȸ���� id�� �Է��ϼ���");
		System.out.println("id >>> ");
		String inputID = inputStr.next();

		memberDAO.readOne(inputID);

	}

	private void ReadALLMember(MemberDTO memberDTO, MemberDAO memberDAO, Scanner inputStr) {

		memberDAO.ReadAllMember();

	}

	private void AddMember(Scanner inputStr) throws SQLException {

		Scanner inputInt = new Scanner(System.in);

		MemberDAO memberDAO = new MemberDAO();
		MemberDTO memberDTO = new MemberDTO();

		System.out.println("�̸� >>> ");
		memberDTO.setbwriter(inputStr.next());

		System.out.println("id >>> ");
		memberDTO.setId(inputStr.next());

		System.out.println("��й�ȣ >>> ");
		memberDTO.setPw(inputStr.next());

		System.out.println("�ʱ� ���� ����Ʈ >>> ");
		memberDTO.setBalance(inputInt.nextInt());

		memberDAO.addMember(memberDTO);
	}

}