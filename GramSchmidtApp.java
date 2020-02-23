package model;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class GramSchmidtApp extends Application {
	Model model;
	
	public void start(Stage stage) throws Exception {
		
		this.model = new Model();
		
		BorderPane root = new BorderPane();
		HBox top = new HBox();
		Label title = new Label("Gram-Schmidt");
		Button button = new Button("random text");
		top.getChildren().addAll(title);
		root.setTop(top);
		root.setCenter(button);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Gram-Schmidt Orthogonalisation App");
		stage.show();
	}
	
	public static void main(String[] args) {
		new GramSchmidtApp();
		launch();
	}
}
