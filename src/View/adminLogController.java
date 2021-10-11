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
		//adminID.setEditable(false); --- �ؽ�Ʈ�ʵ� �Ƚ����°� 
	}
	public void adminAction(ActionEvent event) {
	      adminDAO db =new adminDAO();
	      if(!adminField(this.adminID, this.adminPW))
				return;
	     if((db.table.containsKey(adminID.getText()) && db.table.get(adminID.getText()).equals(adminPW.getText()))) {
	            System.out.println("�α��� ����!");
	            try {
	    			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("adminPage.fxml"));
	    			Parent root1 = (Parent)fxmlLoader.load();
	    			Stage stage = new Stage();
	    			stage.setScene(new Scene(root1));
	    			stage.setTitle("������������ �Դϴ�.");
	    			stage.show();
	    		} catch (Exception ee) {ee.printStackTrace();}
	            
	           Stage stage = (Stage)logBtn.getScene().getWindow();
	           Platform.runLater(() -> stage.close()); //----������ �α��� â ����� 
	         }else {
	            Msg("�α��� ����");
	            System.out.println("�α��� ����!");
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
			System.out.println("������ ���̵��Է� ����");
			Msg("ID�� �Է��� �ּ���.");
			
			adminID.requestFocus();
			adminPW.clear();
			return false;
		}
		if(adminPW.getText().isEmpty()) {
			System.out.println("������ ����Է� ����");
			Msg("��й�ȣ�� �Է����ּ���.");
			adminPW.requestFocus();
			return false;
		}
		return true;
	}
	public void Msg(String msgIn) {
		Alert alert1 = new Alert(AlertType.INFORMATION);
		alert1.setTitle("�ȳ��޼���");
		alert1.setHeaderText("�α���");
		alert1.setContentText(msgIn);
		alert1.show();
	}
}
