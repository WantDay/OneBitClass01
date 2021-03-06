package bitclass;

import java.util.ArrayList;

import home.HomeScreen;
import home.InputReader;
import home.Login;
import member.Member;

public class BitClassInfo {
	private BitClassManager bitClassManager;
	private HomeScreen home;
	private InputReader ir;
	private String mid;

	// 1. 강좌 정보 메뉴 보기
	public void classMenu(Member member) {
		home = HomeScreen.getInstance();
		Login login = new Login();
		ir = new InputReader();

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			System.out.println();
			System.out.println("강좌 정보");
			System.out.println("------------------");
			System.out.println("1. 전체 강좌 보기");
			System.out.println("2. 할인 중인 강좌");
			System.out.println("3. 마감 임박 강좌");
			if (member != null) {
				System.out.println("4. 지역 근처 강좌");
			}
			if (member != null) {
				System.out.println("9. 로그아웃");
			} else {
				System.out.println("9. 로그인");
			}
			System.out.println("0. 홈으로 가기");
			System.out.println("------------------");
			System.out.print("번호 입력 : ");
			int select = ir.readInteger();

			switch (select) {
			case 1:
				showClasses(member, "All");
				break;
			case 2:
				showClasses(member, "Discount");
				break;
			case 3:
				showClasses(member, "DeadLine");
				break;
			case 4:
				showClasses(member, "Local");
				break;
			case 0:
				break;
			case 9:
				if (member != null) {
					home.setLogin(false);
				} else {
					home.setLogin(login.userLogin());
					mid = login.getMid();
				}
				break;
			default:
				System.out.println("올바른 숫자를 입력하세요.");
				break;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void showClasses(Member member, String type) {
		bitClassManager = new BitClassManager(BitClassDAO.getInstance());
		
		ArrayList<BitClass> BitClasses = bitClassManager.getClassesByDAO(member, type);

		for (int i = 0; i < BitClasses.size(); i++) {
			BitClasses.get(i).printClass(i+1);
		}

		bitClassManager.printNavVar();
		
		if (!type.equals("MyEnrollClass")) {
			System.out.println("0. 뒤로가기");
			System.out.println("신청할 강좌의 번호를 입력해주세요");
			int select = ir.readInteger();
			
			if (select == 0) {
				return;
			}
			if((select) > BitClasses.size()) {
				System.out.println("해당 번호에 맞는 강의가 없습니다.");
				return;
			}
			if(member == null) {
				System.out.println("신청을 위해 로그인을 해주세요.");
				return;
			}
			
			payment(BitClasses.get(select - 1), member);
		}
	}
	
	// 강좌 신청하는 클래스
	void payment(BitClass bitClass, Member member) {
		bitClassManager = new BitClassManager(BitClassDAO.getInstance());
		int point = member.getMpoint();
		int fee = bitClass.discountFee();
		int mPoint = point - fee; // 결제 후 남은 잔액

		// 중복된 강좌 신청이 들어오면 메소드 종료
		if (bitClassManager.checkDuplicateClass(member, bitClass.getCno())) {
			return;
		}
		// 포인트가 부족한 경우 메소드 종료
		if (mPoint < 0) {
			System.out.println("포인트가 부족합니다.");
			return;
		}
		// 수강 인원이 초과한 경우 메소드 종료
		if (bitClass.getEnroll() >= bitClass.getNumPeople()) {
			System.out.println("수강 인원이 꽉 찼습니다.");
			return;
		}

		member.setMpoint(mPoint);
		System.out.println("신청이 완료되었습니다.");
		System.out.println("현재 남은 포인트 : " + member.getMpoint());

		bitClass.setEnroll(bitClass.getEnroll() + 1);

		bitClassManager = new BitClassManager(BitClassDAO.getInstance());
		bitClassManager.enrollClass(bitClass, member);
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}
}