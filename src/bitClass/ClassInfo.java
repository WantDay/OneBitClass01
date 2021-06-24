package bitClass;

import java.util.ArrayList;

import member.ClassMember;
import onebitclass.BitClass;
import onebitclass.ClassDAO;
import onebitclass.ClassManager;

public class ClassInfo {
	private ClassManager classManager;
	private InputReader ir;
	private String mid;

	public void classMenu(ClassMember member) {
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
				showTakeClass(member);
				break;
			case 2:
				showDiscountClasses(member);
				break;
			case 3:
				showDeadlineClasses(member);
				break;
			case 4:
				showLocalClasses(member);
				break;
			case 0:
				break;
			case 9:
				if (member != null) {
					HomeScreen.isLogin = false;
				} else {
					HomeScreen.isLogin = login.userLogin();
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

	// 할인 강좌 보기
	void showDiscountClasses(ClassMember member) {
		classManager = new ClassManager(ClassDAO.getInstance());
		ArrayList<BitClass> list = classManager.getDiscountClass();

		for (int i = 0; i < list.size(); i++) {
			System.out.println(i+1 + ". " +list.get(i));

		}
		System.out.println("-----------------------------------");
		System.out.println("신청할 강좌의 번호를 입력해주세요");
		int select = ir.readInteger();	
		
		payment(list.get(select-1), member);
	}

	// 마감임박 강좌 보기
	void showDeadlineClasses(ClassMember member) {
		classManager = new ClassManager(ClassDAO.getInstance());
		ArrayList<BitClass> list = classManager.getDeadLineClass();

		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}	
		System.out.println("-----------------------------------");
		System.out.println("신청할 강좌의 번호를 입력해주세요");
		int select = ir.readInteger();	
		
		payment(list.get(select-1), member);
		
	}

	// 내 관심지역 강좌 보기
	void showLocalClasses(ClassMember member) {
		classManager = new ClassManager(ClassDAO.getInstance());
		ArrayList<BitClass> list = classManager.getLocClass(member.getMloc());

		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}	
		System.out.println("-----------------------------------");
		System.out.println("신청할 강좌의 번호를 입력해주세요");
		int select = ir.readInteger();	
		
		payment(list.get(select-1), member);
		
	}

	// 전체 강좌 보기
	void showTakeClass(ClassMember member) {
		classManager = new ClassManager(ClassDAO.getInstance());
		ArrayList<BitClass> list = classManager.takeClass();

		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
				
		}
		System.out.println("-----------------------------------");
		System.out.println("신청할 강좌의 번호를 입력해주세요");
		int select = ir.readInteger();	
		
		payment(list.get(select-1), member);
	}
	
	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}
	
	// 강좌 신청하는 클래스
	void payment(BitClass bitClass, ClassMember member) {
		int point = member.getMpoint(); 
		int fee = bitClass.discountFee();
		int mPoint = point - fee; // 결제 후 남은 잔액
		member.setMpoint(mPoint);
		System.out.println("신청이 완료되었습니다.");
		System.out.println("현재 남은 포인트 : " + member.getMpoint());

		bitClass.setEnroll(bitClass.getEnroll()+1);

		classManager = new ClassManager(ClassDAO.getInstance());
		classManager.enrollClass(bitClass, member);
	
	} 
}
