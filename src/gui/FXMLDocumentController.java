/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author MER
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private AnchorPane mainAnchor;
    @FXML
    private GridPane backGrid;
    @FXML
    private AnchorPane sceneAnchor;
    @FXML
    private ImageView sceneImage;
    @FXML
    private GridPane sceneGrid;
    @FXML
    private ChoiceBox<?> npcCB;
    @FXML
    private AnchorPane dialogueAnchor;
    @FXML
    private TextArea dialogueTA;
    @FXML
    private AnchorPane ansButtonAnchor;
    @FXML
    private GridPane buttonGrid;
    @FXML
    private Button dialogueButton1;
    @FXML
    private Button dialogueButton2;
    @FXML
    private Button dialogueButton3;
    @FXML
    private AnchorPane fuelAnchor;
    @FXML
    private TextArea fuelTA;
    @FXML
    private AnchorPane warpFuelAnchor;
    @FXML
    private TextArea warpTA;
    @FXML
    private AnchorPane repAnchor;
    @FXML
    private TextArea repTA;
    @FXML
    private AnchorPane timeAnchor;
    @FXML
    private TextArea timeTA;
    @FXML
    private AnchorPane quitAnchor;
    @FXML
    private AnchorPane itemAnchor;
    @FXML
    private GridPane itemGrid;
    @FXML
    private ImageView itemIV0;
    @FXML
    private Button dropItem0;
    @FXML
    private ImageView itemIV1;
    @FXML
    private ImageView itemIV2;
    @FXML
    private Button dropItem1;
    @FXML
    private Button dropItem2;
    @FXML
    private AnchorPane warpAnchor;
    @FXML
    private TextArea inGameTimeTA;
    @FXML
    private Button helpButton;
    @FXML
    private Button quitButton;
    @FXML
    private TextArea titleTA;
    @FXML
    private GridPane titleGrid;
    @FXML
    private Button scenario1Button;
    @FXML
    private Button scenario2Button;
    @FXML
    private TextField nameText;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void setSolarsystem(ActionEvent event) {
    }

    @FXML
    private void dropItems(ActionEvent event) {
    }

    @FXML
    private void getHelp(ActionEvent event) {
    }

    @FXML
    private void titleHandle(MouseEvent event) {
    }
    
}
