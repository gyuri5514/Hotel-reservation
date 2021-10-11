package connection;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



public class adminDAO1 {
	private static adminDAO1 instance;
	private Connection con;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	//DB singleton
		public static adminDAO1 getInstance() {
			if(instance == null) {
				synchronized (adminDAO1.class) {
					instance = new adminDAO1();
				}
			}
			return instance;
		}
		public void getConnection() {
			 try {
		         con = ConnUtil.getConnection();
		         System.out.println("관리자DB연동 성공!!");
		         } catch(SQLException e) { e.printStackTrace(); }
		}
		//체크인날짜로 테이블뷰 보여주기
		public ArrayList<reservationDTO> showTV(reservationDTO dto){
			ArrayList<reservationDTO> arrayList = new ArrayList<reservationDTO>();
			
			getConnection();
			reservationDTO dto2 = null;
			String sql = "select * from reservation where startday = ?";
			try {
				pstmt = con.prepareStatement(sql.toString());
				pstmt.setString(1, dto.getStartday());
				//System.out.println("!!11" + dto.getStartday());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					dto2 = new reservationDTO();
					dto2.setRname(rs.getString("rname"));
					dto2.setName(rs.getString("name"));
					dto2.setPhone(rs.getString("phone"));
					dto2.setStartday(rs.getString("startday"));
					dto2.setEndday(rs.getString("endday"));
					dto2.setPeople(Integer.parseInt(rs.getString("people")));
					dto2.setPay(Integer.parseInt(rs.getString("pay")));
					arrayList.add(dto2);
					//System.out.println("showTv" + arrayList);
				}
			} catch (SQLException e) {e.printStackTrace();
			} finally {
				try {
					if(rs != null) rs.close();
					if(pstmt != null) pstmt.close();
					if(con != null) con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return arrayList;
	}
		//전체 다 테이블뷰에 보여주기
		public ArrayList<reservationDTO> AllshowTV(reservationDTO dto){
			ArrayList<reservationDTO> arrayList = new ArrayList<reservationDTO>();
			getConnection();
			reservationDTO dto2 = null;
			String sql = "select * from reservation";
			try {
				pstmt = con.prepareStatement(sql.toString());
				
				rs = pstmt.executeQuery();
				while (rs.next()) {
					dto2 = new reservationDTO();
					dto2.setRname(rs.getString("rname"));
					dto2.setName(rs.getString("name"));
					dto2.setPw(rs.getString("pw"));
					dto2.setPhone(rs.getString("phone"));
					dto2.setStartday(rs.getString("startday"));
					dto2.setEndday(rs.getString("endday"));
					dto2.setPeople(Integer.parseInt(rs.getString("people")));
					dto2.setPay(Integer.parseInt(rs.getString("pay")));
					arrayList.add(dto2);
					
				}
			} catch (SQLException e) {e.printStackTrace();
			} finally {
				try {
					if(rs != null) rs.close();
					if(pstmt != null) pstmt.close();
					if(con != null) con.close();
				} catch (SQLException e) {e.printStackTrace();}
			}
			return arrayList;
	}
		public int Update(reservationDTO dto, String name){ //---수정하기
			//ArrayList<reservationDTO> arrayList = new ArrayList<reservationDTO>();
			getConnection();
			int i =0;
			String sql ="update reservation set rname=?, phone=?, people=? where name= ?";
			try {
				pstmt= con.prepareStatement(sql);
				pstmt.setString(1, dto.getRname());
				pstmt.setString(2, dto.getPhone());
				pstmt.setInt(3, dto.getPeople());
				pstmt.setString(4, name);
				
				i = pstmt.executeUpdate();
				System.out.println(i+ "개의 데이터가 수정되었습니다.");
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if(rs != null) rs.close();
					if(pstmt != null) pstmt.close();
					if(con != null) con.close();
				} catch (SQLException e) {e.printStackTrace();}
			}
			return i;
		}
		
		public void deleteReservation(String name) { //----삭제하기
			int i = 0;
			getConnection();
			String sql  = "delete reservation where name=?";
			try {
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, name);
				
				i = pstmt.executeUpdate();
				System.out.println(i + "개가 삭제되었습니다.");
				
			} catch (SQLException e) { e.printStackTrace();}
			 finally {
					try {
						if(rs != null) rs.close();
						if(pstmt != null) pstmt.close();
						if(con != null) con.close();
					} catch (SQLException e) {e.printStackTrace();}
				}
		}/*
		public void ExcelSave(reservationDTO dto) {
			try {
				String sql = "select * from reservation";
				pstmt  = con.prepareStatement(sql);
				rs = pstmt.executeQuery();
				
				XSSFWorkbook wb = new XSSFWorkbook();
				XSSFSheet sheet = wb.createSheet("reservation List");
				XSSFRow header = sheet.createRow(0);
				header.createCell(0).setCellValue("rname");
				header.createCell(1).setCellValue("name");
				header.createCell(2).setCellValue("phone");
				header.createCell(3).setCellValue("startday");
				header.createCell(4).setCellValue("endday");
				header.createCell(5).setCellValue("people");
				header.createCell(6).setCellValue("pay");
				
				int index = 1 ;
				while (rs.next()) {
					XSSFRow row = sheet.createRow(index);
					row.createCell(0).setCellValue(rs.getString("rname"));
					row.createCell(1).setCellValue(rs.getString("name"));
					row.createCell(2).setCellValue(rs.getString("phone"));
					row.createCell(3).setCellValue(rs.getString("startday"));
					row.createCell(4).setCellValue(rs.getString("endday"));
					row.createCell(5).setCellValue(rs.getString("people"));
					row.createCell(6).setCellValue(rs.getString("pay"));
					index++;
				}
				FileOutputStream fos = new FileOutputStream("c:/pmProject/예약.xlsx");
				wb.write(fos);
				fos.close();
				} catch (SQLException e) {	e.printStackTrace();
				}catch (FileNotFoundException e) {	e.printStackTrace();
				}catch (IOException e) {	e.printStackTrace();
				}finally {
					try {
						if(rs != null) rs.close();
						if(pstmt != null) pstmt.close();
						if(con != null) con.close();
					} catch (SQLException e) {e.printStackTrace();}
				}
		}*/
		
}
