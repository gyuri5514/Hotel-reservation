package connection;

import java.sql.Connection;   
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class adminDAO {
	
	public HashMap<String, String> table;
	   /*-------------------login------------------------------------*/
	   public adminDAO() {
	      StringBuffer sql = new StringBuffer();
	      sql.append("select * from admin");
	      
	      Connection con = null;
	      PreparedStatement pstmt = null;
	      ResultSet rs = null;
	      try {
	         con = ConnUtil.getConnection();
	         pstmt = con.prepareStatement(sql.toString());
	         rs = pstmt.executeQuery();
	         
	         table = new HashMap<>();
	         while(rs.next()) {
	            table.put(rs.getString("adminid"), rs.getString("adminpw"));
	     
	        	 System.out.println("adminDB연동 성공!!");
	         }
	      }catch(SQLException e) {
	         e.printStackTrace();
	      }finally {
	         try{if(rs != null) rs.close();}catch(SQLException e) {}
	         try{if(pstmt != null) pstmt.close();}catch(SQLException e) {}
	         try{if(con != null) con.close();}catch(SQLException e) {}
	      }
	   }

}
