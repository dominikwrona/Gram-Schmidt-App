package view;

import controller.SubmitHandler;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Model;

public class Main extends Application {
	Model model;
	
	public void start(Stage stage) throws Exception {
		
		this.model = new Model();
		
		BorderPane root = new BorderPane();
		Insets def_pad = new Insets(4);
		Insets text_pad = new Insets(1);
		Label title = new Label("Independence test and orthogonalisation");
		title.setPadding(def_pad);
		HBox top = new HBox(title);
		TextField dim_input = new TextField();
		TextField vector_input = new TextField();
		TextArea gschmidt_output = new TextArea();
		dim_input.setPrefColumnCount(2);
		dim_input.setPadding(text_pad);
		vector_input.setPrefColumnCount(20);
		vector_input.setPadding(text_pad);
		gschmidt_output.setEditable(false);
		gschmidt_output.setPrefRowCount(4);
		gschmidt_output.setPadding(def_pad);
		Button button = new Button("Submit");
		Label dim_label = new Label("n ( \u211D\u207F ):");
		Label ind = new Label("Enter vectors below in the format (x,y,...)(a,b,...)");
		Label ans = new Label();
		ind.setPadding(new Insets(5));
		dim_label.setPadding(new Insets(0,5,0,10));
		BorderPane.setMargin(ans, def_pad);
		HBox instruction = new HBox(ind);
		HBox dimension = new HBox(dim_label, dim_input);
		HBox input2 = new HBox(vector_input, button);
		HBox answer = new HBox(ans);
		VBox main = new VBox(instruction, dimension, input2, answer);
		main.setSpacing(8);
		root.setTop(top);
		root.setCenter(main); 
		root.setBottom(gschmidt_output); BorderPane.setMargin(root.getBottom(), def_pad);
		//Setting Event Handlers
		SubmitHandler submit = new SubmitHandler(vector_input,dim_input, ans,gschmidt_output, this.model);
		button.setOnAction(submit);
		
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Gram-Schmidt Orthogonalisation App");
		stage.show();
	}
	
	public static void main(String[] args) {
		new Main();
		launch();
	}
}
