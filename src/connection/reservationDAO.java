package connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class reservationDAO {
	private static reservationDAO instance;
	private Connection con;
	private PreparedStatement pstmt;
	private ResultSet rs;

	String name;

	// DB singleton
	public static reservationDAO getInstance() {
		if (instance == null) {
			synchronized (reservationDAO.class) {
				instance = new reservationDAO();
			}
		}
		return instance;
	}

	public void getConnection() {
		try {
			con = ConnUtil.getConnection();
			System.out.println("예약DB연동 성공!!");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public int insertreservation(reservationDTO dto) {
		int i = 0;
		getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("insert into reservation values(?,?,?,?,?,?,?,?)");
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, dto.getRname());
			pstmt.setString(2, dto.getName());
			pstmt.setString(3, dto.getPw());
			pstmt.setString(4, dto.getPhone());
			pstmt.setString(5, dto.getStartday());
			pstmt.setString(6, dto.getEndday());
			pstmt.setInt(7, dto.getPeople());
			pstmt.setInt(8, dto.getPay());

			i = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i;
	}

	public ArrayList<reservationDTO> daycheak(reservationDTO dto) {
		ArrayList<reservationDTO> daylist = new ArrayList<reservationDTO>();
		getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("select startday,endday from reservation where rname=?");
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, dto.getRname());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				reservationDTO rDTO = new reservationDTO();
				rDTO.setStartday(rs.getString("startday"));
				rDTO.setEndday(rs.getString("endday"));
				daylist.add(rDTO);
				System.out.println(rDTO.getStartday());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return daylist;
	}

	public ArrayList<reservationDTO> daycheak(String rname) {
		ArrayList<reservationDTO> daylist = new ArrayList<reservationDTO>();
		getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("select rname, startday, endday from reservation where rname=?");
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, rname);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				reservationDTO rDTO = new reservationDTO();
				rDTO.setRname(rs.getString("rname"));
				rDTO.setStartday(rs.getString("startday"));
				rDTO.setEndday(rs.getString("endday"));
				daylist.add(rDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return daylist;
	}

	public ArrayList<reservationDTO> getRoomData(reservationDTO dto1) {
		ArrayList<reservationDTO> arrayList = new ArrayList<reservationDTO>();
		System.out.println("rservationDto" + dto1.getRname());
		getConnection();
		String sql = "select * from reservation where rname = ?";
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, dto1.getRname());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reservationDTO dto2 = new reservationDTO();
				dto2.setName(rs.getString("name"));
				dto2.setPw(rs.getString("pw"));
				dto2.setPhone(rs.getString("phone"));
				dto2.setStartday(rs.getString("startday"));
				dto2.setEndday(rs.getString("endday"));
				dto2.setPeople(Integer.parseInt(rs.getString("people")));
				dto2.setPay(Integer.parseInt(rs.getString("pay")));
				arrayList.add(dto2);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		return arrayList;
	}

	public ArrayList<reservationDTO> showData(String name, String pw) {
		ArrayList<reservationDTO> arrayList = new ArrayList<reservationDTO>();

		StringBuffer sql = new StringBuffer();
		getConnection();
		reservationDTO rDTO = null;
		sql.append("select * from reservation where name = ? and pw = ?");
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, name);
			pstmt.setString(2, pw);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				rDTO = new reservationDTO();
				rDTO.setName(rs.getString("name"));
				rDTO.setPw(rs.getString("pw"));
				rDTO.setPhone(rs.getString("phone"));
				rDTO.setRname(rs.getString("rname"));
				rDTO.setStartday(rs.getString("startday"));
				rDTO.setEndday(rs.getString("endday"));
				rDTO.setPeople(rs.getInt("people"));
				rDTO.setPay(rs.getInt("pay"));

				arrayList.add(rDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return arrayList;
	}

	public ArrayList<reservationDTO> roomList() {
		ArrayList<reservationDTO> arrayList = new ArrayList<reservationDTO>();

		getConnection();
		reservationDTO dto1 = null;
		String sql = "select * from room";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				dto1 = new reservationDTO();
				dto1.setRname(rs.getString("rname"));
				arrayList.add(dto1);
			}
		} catch (SQLException e) {

		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return arrayList;
	}

}// end메소드
