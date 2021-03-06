package member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MemberDAO {

	// 외부 클래스 또는 인스턴스에서 해당 클래스로 인스턴스를 생성하지 못하도록 처리

	// 2. 클래스 내부에서 인스턴스를 만들고 메소드를 통해서 반환하도록 처리
	static private MemberDAO dao = new MemberDAO();

	// 3. 메소드를 통해서 반환 하도록 처리
	public static MemberDAO getInstance() {
		return dao;
	}

	// 1. 회원 가입 기능
	public int createId(Connection conn, Member member) { // 회원 가입
		int result = 0;

		String sql = "insert into classmember (mno, mid, mpw, mname, mphone, mloc) values (classmember_mno_seq.nextval, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)){

			pstmt.setString(1, member.getMid());
			pstmt.setString(2, member.getMpw());
			pstmt.setString(3, member.getMname());
			pstmt.setString(4, member.getMphone());
			pstmt.setString(5, member.getMloc());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	// 2. 회원 정보 수정
	public int editInfo(Connection conn, Member member) {
		int result = 0;
		
		String sql = "update classmember set mpw=?, mloc =?, mphone =? where mid=?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, member.getMpw());
			pstmt.setString(2, member.getMloc());
			pstmt.setString(3, member.getMphone());
			pstmt.setString(4, member.getMid());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 3. 회원 정보 보기
	public Member getMemberByMid(Connection conn, String mid) {
		Member member = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from classmember where mid = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, mid);

			// 결과 받아오기
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				member = new Member(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return member;
	}

	// 4. 회원 탈퇴 기능
	public int deleteId(Connection conn, Member member) {
		int result = 0;

		String sql = "delete from classmember where mid = ? ";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, member.getMid());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 5. 포인트 수정
	public int editPoint(Connection conn, Member member) {
		int result = 0;

		String sql = "update classmember set mpoint=? where mid=?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setInt(1, member.getMpoint());
			pstmt.setString(2, member.getMid());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	// 6. 아이디 조회
	public ArrayList<String> getMemberIds(Connection conn) {
		ArrayList<String> memberIds = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select mid from classmember";
			pstmt = conn.prepareStatement(sql);

			// 결과 받아오기
			rs = pstmt.executeQuery();

			memberIds = new ArrayList<String>();

			while (rs.next()) {
				memberIds.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return memberIds;
	}
}