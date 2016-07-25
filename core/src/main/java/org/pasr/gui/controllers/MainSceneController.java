package org.pasr.gui.controllers;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

import java.io.File;
import java.io.IOException;


public class MainSceneController {
    public MainSceneController(){}

    @FXML
    public void initialize() throws IOException {
        // At the moment initialize is called,
        // the views are not yet ready to handle
        // focus so runLater is used.
        Platform.runLater(() -> emailAddress.requestFocus());

        File corpusDirectory = new File("corpora");
        File acousticModelDirectory = new File("acoustic_models");
        if(!corpusDirectory.exists()){
            if(!corpusDirectory.mkdir()){
                throw new IOException("Failed to create corpus directory");
            }
        }

        if(!acousticModelDirectory.exists()){
            if(!acousticModelDirectory.mkdir()){
                throw new IOException("Failed to create acoustic model directory");
            }
        }

        if(corpusDirectory.list().length == 0 || acousticModelDirectory.list().length == 0){
            dictateButton.setDisable(true);
            dictateButtonLabel.setVisible(true);
            dictateButtonLabel.setText(DictateButtonMessages.DISABLED.getMessage());
        }
        else{
            dictateButton.setTooltip(
                new Tooltip(DictateButtonMessages.ENABLED.getMessage())
            );
        }

        newCorpusButton.setTooltip(new Tooltip(NewCorpusButtonMessages.TOOLTIP.getMessage()));
    }

    @FXML
    public void newCorpusButtonClicked(){
        String emailAddressText = emailAddress.getText();
        String passwordText = password.getText();

        if(emailAddressText.isEmpty() && passwordText.isEmpty()){
            newCorpusButtonLabel.setVisible(true);
            newCorpusButtonLabel.setText(NewCorpusButtonMessages.NO_INPUT.getMessage());
        }
        else if(emailAddressText.isEmpty()){
            newCorpusButtonLabel.setVisible(true);
            newCorpusButtonLabel.setText(NewCorpusButtonMessages.NO_EMAIL_ADDRESS.getMessage());
        }
        else if(passwordText.isEmpty()){
            newCorpusButtonLabel.setVisible(true);
            newCorpusButtonLabel.setText(NewCorpusButtonMessages.NO_PASSWORD.getMessage());
        }
        else{
            if(newCorpusButtonLabel.isVisible()){
                newCorpusButtonLabel.setVisible(false);
            }

            // TODO move on with e-mail listing
        }
    }

    @FXML
    public void dictateButtonClicked(){
        // TODO move on with dictation
    }

    @FXML
    private ListView<String> corpusList;

    @FXML
    private ListView<String> acousticModelList;

    @FXML
    private TextField emailAddress;

    @FXML
    private PasswordField password;

    @FXML
    private Button newCorpusButton;

    @FXML
    private Label newCorpusButtonLabel;

    private enum NewCorpusButtonMessages{
        TOOLTIP("Enter the address and the password of your e-mail to fetch your e-mails" +
            "and create a corpus"),
        NO_INPUT("Please enter the address and the password\nof your e-mail"),
        NO_EMAIL_ADDRESS("Please enter the address of your e-mail"),
        NO_PASSWORD("Please enter the password of your e-mail");

        NewCorpusButtonMessages(String message){
            message_ = message;
        }

        public String getMessage(){
            return message_;
        }

        private String message_;
    }

    @FXML
    private Button dictateButton;

    @FXML
    private Label dictateButtonLabel;

    private enum DictateButtonMessages{
        DISABLED("Note: You should create at least one corpus\n" +
            "and one acoustic model in order to dictate"),
        ENABLED("Use the chosen corpus and acoustic model during speech recognition");

        DictateButtonMessages(String message){
            message_ = message;
        }

        public String getMessage(){
            return message_;
        }

        private String message_;
    }

}
