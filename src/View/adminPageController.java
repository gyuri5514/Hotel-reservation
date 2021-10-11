package View;

//import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.xssf.XLSBUnsupportedException;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import connection.ConnUtil;
import connection.adminDAO1;
import connection.reservationDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class adminPageController implements Initializable{
	/*-----------------------���̺��----------------------*/
	@FXML private DatePicker DP;
	@FXML private TableView<reservationDTO> tableView1;
	@FXML private TableColumn<reservationDTO, Object> tRname, tName, tChkIn, tChkOut, tPeople, tPhone, tPay;  
	
	/*--------------------��ü����------------------*/
	@FXML private Button allBtn;
	
	/*------------------�ʵ忡 �Ѹ���----------------*/
	@FXML private TextField tfRname , tfName, tfPhone, tfChkIn, tfChkOut, tfPeople, tfPay;
	
	/*------------------����, ���� ,��������------------------*/
	@FXML private Button delBtn, upBtn, excelBtn;
	//private FileInputStream fis;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {  
		DP.setOnAction(e->ClickDP(e));
		allBtn.setOnAction(e-> showAllBtn(e));
		delBtn.setOnAction(e-> deletBtn(e));
		upBtn.setOnAction(e-> updateBtn(e));
		excelBtn.setOnAction(e-> excelSave(e));
	}
	
	
	private void excelSave(ActionEvent e) {//----���������ϱ�  
		String sql = "select * from reservation";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//System.out.println("1" );
		try {
			con = ConnUtil.getConnection();
			pstmt  = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			//System.out.println("2" );
			
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("reservation List");
			XSSFRow header = sheet.createRow(0);
			
			XSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setFillForegroundColor(HSSFColor.LIME.index);
			cellStyle.setFillPattern(CellStyle.BIG_SPOTS);
			
			
			header.createCell(0).setCellValue("�����̸�");
			//header.createCell(0).setCellStyle(cellStyle);
			header.createCell(1).setCellValue("������");
			//header.createCell(1).setCellStyle(cellStyle);
			header.createCell(2).setCellValue("��ȣ");
			//header.createCell(2).setCellStyle(cellStyle);
			header.createCell(3).setCellValue("üũ��");
			//header.createCell(3).setCellStyle(cellStyle);
			header.createCell(4).setCellValue("üũ�ƿ�");
			//header.createCell(4).setCellStyle(cellStyle);
			header.createCell(5).setCellValue("�ο�");
			//header.createCell(5).setCellStyle(cellStyle);
			header.createCell(6).setCellValue("�����ݾ�");
			//header.createCell(6).setCellStyle(cellStyle);
			
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
			FileOutputStream fos = new FileOutputStream("c:/pmProject/������Ȳ.xlsx");
			wb.write(fos);
			fos.close();
			} catch (SQLException e1) {	e1.printStackTrace();
			} catch (FileNotFoundException e2) { e2.printStackTrace();
			} catch (IOException e3) {	e3.printStackTrace();
			} finally {
				try {
					if(rs != null) rs.close();
					if(pstmt != null) pstmt.close();
					if(con != null) con.close();
				} catch (SQLException e4) {e4.printStackTrace();}
			}
			
			Alert alert = new Alert(AlertType.INFORMATION);
		    alert.setHeaderText(null);
		    alert.setContentText("������ �Ϸ�Ǿ����ϴ�.");
			alert.show();
	}


	private void updateBtn(ActionEvent e) { //---����, ��ȭ��ȣ, �ο� �������
		reservationDTO dto = new reservationDTO();

		dto.setRname(tfRname.getText());
		dto.setPhone(tfPhone.getText());
		dto.setPeople(Integer.parseInt(tfPeople.getText()));
		adminDAO1 dao = adminDAO1.getInstance();
		int i = dao.Update(dto, tfName.getText());
		
		Alert alert = new Alert(AlertType.INFORMATION);
	    alert.setHeaderText(null);
	    alert.setContentText("������ �Ϸ�Ǿ����ϴ�.");
		alert.show();
	}


	private void deletBtn(ActionEvent e) { 
		new adminDAO1().deleteReservation(tfName.getText());
		
		Alert alert = new Alert(AlertType.INFORMATION);
	    alert.setHeaderText(null);
	    alert.setContentText("������ �Ϸ�Ǿ����ϴ�.");
		alert.show();
	}


	private void showAllBtn(ActionEvent e) { //--------��ü �����Ϻ����ֱ�
		
		//System.out.println("1. tableView ����");
				adminDAO1 dao = new adminDAO1();
				reservationDTO dto = new reservationDTO();
				
				ObservableList<reservationDTO> arrayList = FXCollections.observableArrayList();
				
				arrayList.addAll(dao.AllshowTV(dto));
				
				TableColumn tRname=tableView1.getColumns().get(0);
				tRname.setCellValueFactory(new PropertyValueFactory("rname"));
				
				TableColumn tName=tableView1.getColumns().get(1);
				tName.setCellValueFactory(new PropertyValueFactory("name"));
				
				TableColumn tChkIn =tableView1.getColumns().get(2);
				tChkIn.setCellValueFactory(new PropertyValueFactory("startday"));
				
				TableColumn tChkOut =tableView1.getColumns().get(3);
				tChkOut.setCellValueFactory(new PropertyValueFactory("endday"));
				
				TableColumn tPeople=tableView1.getColumns().get(4);
				tPeople.setCellValueFactory(new PropertyValueFactory("people"));
				
				TableColumn tPhone=tableView1.getColumns().get(5);
				tPhone.setCellValueFactory(new PropertyValueFactory("phone"));
				
				TableColumn tPay=tableView1.getColumns().get(6);
				tPay.setCellValueFactory(new PropertyValueFactory("pay"));
				
				//System.out.println("sfg" + arrayList);
				//System.out.println("dto" + arrayList.get(0).getRname());
				dao.AllshowTV(dto);
				tableView1.setItems(arrayList);
	}
	
	private void ClickDP(ActionEvent e) { //-----------üũ�γ�¥ �̿��� ���̺�亸���ֱ� 
		String strDP = DP.getValue().toString();
		
		adminDAO1 dao = new adminDAO1();
		
		reservationDTO dto = new reservationDTO(strDP);
		
		ObservableList<reservationDTO> arrayList = FXCollections.observableArrayList();
		arrayList.addAll(dao.showTV(dto));
		
		TableColumn tRname=tableView1.getColumns().get(0);
		tRname.setCellValueFactory(new PropertyValueFactory("rname"));
		
		TableColumn tName=tableView1.getColumns().get(1);
		tName.setCellValueFactory(new PropertyValueFactory("name"));
		
		
		TableColumn tChkIn =tableView1.getColumns().get(2);
		tChkIn.setCellValueFactory(new PropertyValueFactory("startday"));
		TableColumn tChkOut =tableView1.getColumns().get(3);
		tChkOut.setCellValueFactory(new PropertyValueFactory("endday"));
		
		TableColumn tPeople=tableView1.getColumns().get(4);
		tPeople.setCellValueFactory(new PropertyValueFactory("people"));
		
		TableColumn tPhone=tableView1.getColumns().get(5);
		tPhone.setCellValueFactory(new PropertyValueFactory("phone"));
		
		TableColumn tPay=tableView1.getColumns().get(6);
		tPay.setCellValueFactory(new PropertyValueFactory("pay"));
		
		
		dao.showTV(dto);
		tableView1.setItems(arrayList);
	}
	
	@FXML
	void selecttableView(MouseEvent event) { //----�ؽ�Ʈ �ʵ忡 �Ѹ���
		reservationDTO vtd = tableView1.getSelectionModel().getSelectedItem();
		//System.out.println("dfdf");//+vtd.toString());
		
		tfRname.setText(vtd.getRname());
		tfName.setText(vtd.getName());
		tfPhone.setText(vtd.getPhone());
		tfChkIn.setText(vtd.getStartday());
		tfChkOut.setText(vtd.getEndday());
		tfPeople.setText(String.valueOf(vtd.getPeople()));
		tfPay.setText(String.valueOf(vtd.getPay()));
	}
}
