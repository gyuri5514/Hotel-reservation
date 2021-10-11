package View;

import java.net.URL;   
import java.util.ResourceBundle;

import connection.adminDAO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class adminLogController implements Initializable {
	@FXML private TextField adminID;
	@FXML private PasswordField adminPW;
	@FXML private Button logBtn;
	@FXML private CheckBox checkID;
	
	final int FIELDLENGTH = 9;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logBtn.setDisable(true);
		logBtn.setOnAction(e -> adminAction(e));
		adminID.textProperty().addListener((obs, oldTxt, newTxt)->{	checkFieldLength();	});
		//adminID.setEditable(false); --- 텍스트필드 안써지는거 
	}
	public void adminAction(ActionEvent event) {
	      adminDAO db =new adminDAO();
	      if(!adminField(this.adminID, this.adminPW))
				return;
	     if((db.table.containsKey(adminID.getText()) && db.table.get(adminID.getText()).equals(adminPW.getText()))) {
	            System.out.println("로그인 성공!");
	            try {
	    			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("adminPage.fxml"));
	    			Parent root1 = (Parent)fxmlLoader.load();
	    			Stage stage = new Stage();
	    			stage.setScene(new Scene(root1));
	    			stage.setTitle("관리자페이지 입니다.");
	    			stage.show();
	    		} catch (Exception ee) {ee.printStackTrace();}
	            
	           Stage stage = (Stage)logBtn.getScene().getWindow();
	           Platform.runLater(() -> stage.close()); //----성공시 로그인 창 사라짐 
	         }else {
	            Msg("로그인 실패");
	            System.out.println("로그인 실패!");
	            this.adminPW.clear();
	            if(this.checkID.isSelected() == false) {
	            	this.adminID.clear();
	            	this.adminID.requestFocus();
	            }else {this.adminPW.requestFocus();}
	         }
	      
	   }
	public void checkFieldLength() {
		logBtn.setDisable(false);
		if(adminID.getLength() >= FIELDLENGTH) {
			adminID.setText(adminID.getAccessibleText().substring(0, FIELDLENGTH));
		}
	}

	public boolean adminField(TextField adminID, PasswordField adminPW) {
		if(adminID.getText().isEmpty()) {
			System.out.println("관리자 아이디입력 안함");
			Msg("ID를 입력해 주세요.");
			
			adminID.requestFocus();
			adminPW.clear();
			return false;
		}
		if(adminPW.getText().isEmpty()) {
			System.out.println("관리자 비번입력 안함");
			Msg("비밀번호를 입력해주세요.");
			adminPW.requestFocus();
			return false;
		}
		return true;
	}
	public void Msg(String msgIn) {
		Alert alert1 = new Alert(AlertType.INFORMATION);
		alert1.setTitle("안내메세지");
		alert1.setHeaderText("로그인");
		alert1.setContentText(msgIn);
		alert1.show();
	}
}
