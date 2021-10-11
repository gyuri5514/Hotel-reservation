package View;
/*
   ------- 메인 화면 띄우기  
 */
import javafx.application.Application;  
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class miniMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root= FXMLLoader.load(getClass().getResource("root.fxml"));
		Scene scene = new Scene(root);
		
		primaryStage.setTitle("MiniProject");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
