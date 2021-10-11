package View;
 
import java.io.BufferedReader;     
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import connection.reservationDAO;
import connection.reservationDTO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Callback;



public class mainController implements Initializable {
	/*-------------------안내문 ---------------------*/
	@FXML private TextFlow infoText; // room안내문
	/*-------------------관리자열기------------------*/
	@FXML private Button adminBtn;
	/*-------------------예약하기-------------------*/
	@FXML private DatePicker CheckIn, CheckOut;
	@FXML private Button showRoomBtn, insertBtn, deletBtn;
	@FXML private ListView<String> SelectRoom;
	@FXML private TextField nametf, phonetf;
	@FXML private PasswordField pwtf;
	@FXML private ComboBox<String> peoplecb;
	@FXML private Label paylabel;
	/*-------------------확인하기-------------------*/
	@FXML private Button cBtn;
	@FXML private TextField cName1, cName2, cPhone, cRname, cIn, cOut, cPeople, cPay;
	@FXML private PasswordField cPw;


	@Override
	public void initialize(URL location, ResourceBundle resources) {    
		
		/*-------------------안내문 start---------------------*/
		FileInputStream fis = null;
		InputStreamReader is = null;
		BufferedReader br = null;
		
		try {//textflow에 텍스트 넣기 
			fis = new FileInputStream("src/textFile/info.txt");
			is = new InputStreamReader(fis);
			br = new BufferedReader (is);
			String str = null;
			while ((str = br.readLine()) != null) {
				infoText.getChildren().add(new Text(str));
				infoText.getChildren().add(new Text(System.lineSeparator()));
			}
		} catch (FileNotFoundException fnfe) { fnfe.printStackTrace();
		} catch (IOException ioe) { ioe.printStackTrace();
		}finally {
			try {if(br!=null) {br.close();}} catch (IOException e) {}
			try {if(is!=null) {is.close();}} catch (IOException e) {}
			try {if(br!=null) {br.close();}} catch (IOException e) {}
		}
		
		/*-------------------안내문  end---------------------*/
		
		adminBtn.setOnAction(e->adminIn(e)); //관리자로그인창 가기 
		
		/*------------------예약하기 --------------------*/
		showRoomBtn.setOnAction(e->showListBtn(e)); //객실리스트 보여주는 버튼 
		insertBtn.setOnAction(e->insertDBBtn(e)); // 예약하기 버튼 
		deletBtn.setOnAction(e->deletAll(e));
		
		DatePickerCheck();
		
		cBtn.setOnAction(e-> checkRbtn(e)); //예약확인버튼이벤트
	}//-------------------end initialize
 
	private void checkRbtn(ActionEvent e) { //---예약확인버튼이벤트
		reservationDAO rDAO = new reservationDAO();
		ArrayList<reservationDTO> arr = rDAO.showData(cName1.getText(), cPw.getText());
		//System.out.println(cPw.getText());
		//System.out.println(arr.get(0));
		try {
		if (cName1.getText().equals(arr.get(0).getName()) && cPw.getText().equals(arr.get(0).getPw())) {
				cName2.setText(arr.get(0).getName());
				cPhone.setText(arr.get(0).getPhone());
				cRname.setText(arr.get(0).getRname());
				cIn.setText(arr.get(0).getStartday());
				cOut.setText(arr.get(0).getEndday());
				cPeople.setText(String.valueOf(arr.get(0).getPeople()));
				cPay.setText(String.valueOf(arr.get(0).getPay()));
				System.out.println("이름 비번 일치");
				
			} else {	System.out.println("불일치");    }
		}catch (Exception e1) {e1.printStackTrace();}
	}

	private void deletAll(ActionEvent e) { 
		nametf.clear();
		phonetf.clear();
		pwtf.clear();
		CheckIn.setValue(null);
		CheckOut.setValue(null);
		SelectRoom.getItems().clear();
		paylabel.setText(null);
		//peoplecb.getItems().clear(); ---값이 아예 없어짐 
	}

	private void DatePickerCheck() {//----------데이트피커 디자인   
		CheckIn.setValue(LocalDate.now());
		
		final Callback<DatePicker, DateCell> dayCellFactory=
				new Callback<DatePicker, DateCell>() { 
					@Override
					public DateCell call(final DatePicker datePicker) {
						return new DateCell() {
							@Override
							public void updateItem(LocalDate item, boolean empty) {
								super.updateItem(item, empty);
								if(item.isBefore(CheckIn.getValue().plusDays(1))) {
									setDisable(true);
									setStyle("-fx-background-color: #ffc0cb;");
								}
								long p = ChronoUnit.DAYS.between(CheckIn.getValue(), item);
								setTooltip(new Tooltip("숙박일수 : "+ p +"일"));
							}
						};
					}
				};
				CheckOut.setDayCellFactory(dayCellFactory);
	}

	private void insertDBBtn(ActionEvent e) {//------- 예약하기버튼   
		try {
		reservationDAO rDAO = new reservationDAO();
		
		String name = nametf.getText();
		String pw = pwtf.getText();
		String phone = phonetf.getText();
		String chkIn = String.valueOf(CheckIn.getValue());
		String chkOut = String.valueOf(CheckOut.getValue());
		String rname = SelectRoom.getSelectionModel().getSelectedItem();
		int people = Integer.parseInt(peoplecb.getSelectionModel().getSelectedItem());
		int pay = Integer.parseInt(paylabel.getText());
		
		reservationDTO rDTO = new reservationDTO(rname,name,pw,phone,chkIn,chkOut,people,pay);
		rDAO.insertreservation(rDTO);
		
		
		
		}catch (NullPointerException e1) {		
			//e1.printStackTrace();	
			if (nametf.getText() == null || pwtf.getText() == null || phonetf.getText() == null || CheckIn.getValue() == null || CheckOut.getValue() == null || SelectRoom.getSelectionModel().getSelectedItem() == null || peoplecb.getValue()== null || paylabel == null){
				Alert alert = new Alert(AlertType.WARNING);
			    alert.setHeaderText(null);
			    alert.setContentText("빈 곳이 있으면 안됩니다!");
				alert.show();
				System.out.println("ffff");
				return;
			}else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("알림창");
				alert.setContentText("예약이 성공적으로 되었습니다. 감사합니다.");
				alert.setHeaderText("예약확인");
				alert.show();
				System.out.println("rrrr");
			}
		}
		/*
		if (nametf.getText() == null || pwtf.getText() == null || phonetf.getText() == null || CheckIn.getValue() == null || CheckOut.getValue() == null || SelectRoom.getSelectionModel().getSelectedItem() == null || peoplecb.getValue()== null || paylabel == null){
			Alert alert = new Alert(AlertType.WARNING);
		    alert.setHeaderText(null);
		    alert.setContentText("빈 곳이 있으면 안됩니다!");
			alert.show();
			System.out.println("ffff");
			return;
		}else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("알림창");
			alert.setContentText("예약이 성공적으로 되었습니다. 감사합니다.");
			alert.setHeaderText("예약확인");
			alert.show();
			System.out.println("rrrr");
		}*/
		//System.out.println("이름:"+nametf.getText()+"비번:"+pwtf.getText()+"번호:"+phonetf.getText()+"쳌인:"+CheckIn.getValue().toString()+"쳌앗:"+CheckOut.getValue().toString()+"객실:"+SelectRoom.getSelectionModel().getSelectedItems()+"인원:"+peoplecb.getSelectionModel().getSelectedItem()+"금액:"+paylabel.getText());
	}

	private void showListBtn(ActionEvent e) { //확인버튼을 누르면 리스트가 보여짐    
		SelectRoom.getItems().clear();
//		SelectRoom.setItems(FXCollections.observableArrayList());
//		SelectRoom.getItems().add("객실1");
//		SelectRoom.getItems().add("객실2");
//		SelectRoom.getItems().add("객실3");
//		Object obj = SelectRoom.getSelectionModel().getSelectedItem();
		
		try { //----결제금액 계산해주는거 
			SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
			java.util.Date FirstDate = format.parse(CheckIn.getValue().toString());
			java.util.Date secondDate = format.parse(CheckOut.getValue().toString());
			long calDate = FirstDate.getTime() - secondDate.getTime();
			
			long calDateDays = calDate/(24*60*60*1000);
			calDateDays = Math.abs(calDateDays)*Integer.parseInt("50000");
			
			paylabel.setText(Long.toString(calDateDays));
			
			} catch (ParseException e1) {	e1.printStackTrace();	}
		
		if (e.getSource() == showRoomBtn) { //-----빈방보여주기
			
			//System.out.println("1");
			String startday = String.valueOf(CheckIn.getValue());
			String endday = String.valueOf(CheckOut.getValue());
			SimpleDateFormat input1 = new SimpleDateFormat("yyyy-MM-dd");
			//SimpleDateFormat input2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s");
			//System.out.println("2" + startday);
			Date start = null;
			Date end = null;
			try {
				start = input1.parse(startday);
				end = input1.parse(endday);
				//System.out.println("3"+start);
				//System.out.println("4"+end);

			} catch (ParseException e1) {e1.printStackTrace();}

			ArrayList<reservationDTO> rnameList = new ArrayList<reservationDTO>();
			ArrayList<ArrayList<reservationDTO>> dayList = new ArrayList<ArrayList<reservationDTO>>();
			//dto = new reservationDTO();
			
			reservationDAO dao = reservationDAO.getInstance();
			rnameList = dao.roomList();
			
			ArrayList<String> impossible = new ArrayList();
			
			int count = 0;
			boolean add = false;
			
			for(int i=0; i<rnameList.size(); i++) {
				dayList.add(dao.daycheak(rnameList.get(i).getRname()));
			}
			if(dayList.get(0) == null && dayList.get(1) == null && dayList.get(2) == null) { //선택한 날의 예약이 없을 때
				SelectRoom.setItems(FXCollections.observableArrayList());
				for(int i=0; i<3; i++) {
					SelectRoom.getItems().add(rnameList.get(i).getRname());
				}
				//SelectRoom.getItems().add("rname");
			} else {
				for(int index=0; index<dayList.size(); index++) {
					for(reservationDTO cheakdto : dayList.get(index)) {
						String sd = cheakdto.getStartday();
						String ed = cheakdto.getEndday();
						try {
							Date dbstart = input1.parse(sd);
							Date dbend  = input1.parse(ed);
							
							int ck = end.compareTo(dbstart);
							int ck3 = dbend.compareTo(end);
							int ck2 = start.compareTo(dbstart);
							int ck4 = dbend.compareTo(start);
							//System.out.println("1818//"+cheakdto.getRname());
							if ((ck == 1 && ck3 == 1) || (ck2 == 1 && ck4 == 1)||ck2 ==0 || ck3==0 ){//|| (ck ==1 && ck3 == -1)||(ck2 ==-1 && ck4 == 1)) {
								impossible.add(cheakdto.getRname());
								//System.out.println("2828//"+cheakdto.getRname());
							}else {
								
							}
						} catch (ParseException e2) {	e2.printStackTrace();	}
					}
					for(reservationDTO cheakdto : dayList.get(index)) {
						for(int i=0; i<impossible.size(); i++) {
							if(cheakdto.getRname().equals(impossible.get(i))){
								//System.out.println(cheakdto.getRname());
								count++;
							}
						}
						if(count == 0 && add == false) {
							SelectRoom.getItems().addAll(cheakdto.getRname());
							add = true;
						}
						count = 0;
					}
					add = false;
				}
			}
		}// end button
	}//end showListBtn
	

	public void adminIn(ActionEvent e) { //관리자로그인 가기  메소드  
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("adminlogin.fxml"));
			Parent root1 = (Parent)fxmlLoader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.show();
		} catch (Exception ee) { ee.printStackTrace(); }
	}//-----------------------------------관리자로그인창 열기

}// end class
