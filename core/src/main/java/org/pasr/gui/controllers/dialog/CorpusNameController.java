package org.pasr.gui.controllers.dialog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.pasr.gui.dialog.CorpusNameDialog;


public class CorpusNameController extends Controller<String> {
    public CorpusNameController (CorpusNameDialog dialog){
        super(dialog);
    }

    @FXML
    public void initialize(){
        button.setOnAction(this :: buttonOnAction);
    }

    private void buttonOnAction(ActionEvent actionEvent){
        dialog_.setValue(textField.getText());
    }

    @FXML
    TextField textField;

    @FXML
    Button button;

}
