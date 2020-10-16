package controller;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Model;

public class SubmitHandler implements EventHandler<ActionEvent> {
	private TextField vectors;
	private Label label;
	private Model m;
	private TextField dim_input;
	private TextArea output;
	
	public SubmitHandler(TextField input, TextField dim, Label label, TextArea output, Model m) {
		this.vectors = input;
		this.dim_input = dim;
		this.label = label;
		this.m = m;
		this.output = output;
	}
	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub
		String result = vectors.getText();
		output.setText("");
		String dim_string = dim_input.getText();
		float dim = (float) Integer.parseInt(dim_string);
		label.setText(result);
		ArrayList<ArrayList<Float>> list = m.extractNums(result, dim);
		boolean ind = m.independentTest(list);
		if (ind) {
			label.setText("Independent: true");
			ArrayList<ArrayList<Float>> gschmidt = m.GramSchmidt(list);
			int size = gschmidt.get(0).size();
			for (ArrayList<Float> col:gschmidt) {
				output.appendText("(");
				for (int curr =0; curr<size; curr++) {
					if (curr == size-1) {
						output.appendText(col.get(curr) + " ");
					}
					else {
						output.appendText(col.get(curr) + ", ");
					}
				}
				output.appendText(")\n");
			}
		}
		else {
		label.setText("Not independent: orthogonal basis cannot be made");
		
		}
		
		
	}

}
