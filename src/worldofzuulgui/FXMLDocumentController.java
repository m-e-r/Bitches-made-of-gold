/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worldofzuulgui;

import worldofzuul.iGame;
import worldofzuul.Game;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 *
 * @author MER
 */
public class FXMLDocumentController implements Initializable {
    
    iGame game;
    
    @FXML
    private AnchorPane sceneAnchor;
    @FXML
    private AnchorPane dialogueAnchor;
    @FXML
    private AnchorPane ansButtonAnchor;
    @FXML
    private AnchorPane fuelAnchor;
    @FXML
    private AnchorPane warpFuelAnchor;
    @FXML
    private AnchorPane repAnchor;
    @FXML
    private AnchorPane timeAnchor;
    @FXML
    private AnchorPane quitAnchor;
    @FXML
    private AnchorPane mainAnchor;
    @FXML
    private AnchorPane itemAnchor;
    @FXML
    private AnchorPane warpAnchor;
    @FXML
    private GridPane itemGrid;
    @FXML
    private GridPane backGrid;
    @FXML
    private GridPane sceneGrid;
    @FXML
    private GridPane buttonGrid;
    @FXML
    private TextArea fuelTA;
    @FXML
    private TextArea titleTA;
    @FXML
    private TextArea warpTA;
    @FXML
    private TextArea repTA;
    @FXML
    private TextArea timeTA;
    @FXML
    private TextArea dialogueTA;
    @FXML
    private ImageView sceneImage;
    @FXML
    private ImageView itemIV0;
    @FXML
    private ImageView itemIV1;
    @FXML
    private ImageView itemIV2;
    @FXML
    private Button dialogueButton1;
    @FXML
    private Button dialogueButton2;
    @FXML
    private Button dialogueButton3;
    @FXML
    private TextArea inGameTimeTA;
    @FXML
    private ChoiceBox npcCB;
    
    TextArea planetTA = new TextArea();
    
    private ObservableList<ListFoo> npcChoices = FXCollections.observableArrayList();
    private ArrayList<Button> buttonArray = new ArrayList();
    private ArrayList<ImageView> itemImageViews = new ArrayList();
    private ArrayList<Button> dialogueArray = new ArrayList();
    private int[] yCoordinates;
    private int[] xCoordinates;
    
    public void setSolarsystem() {
        titleTA.setText("F.U.T.U.R.A.M.A.");
        Image solarsystem1 = new Image("solarsystemy.png");
        sceneImage.setImage(solarsystem1);
        ArrayList<UUID> listOfPlanets = new ArrayList();
        listOfPlanets = this.game.getListOfPlanets();            
        
        
        for(UUID planet : listOfPlanets){
            Button planetButton = new Button();
            planetButton.setUserData(planet);
            planetButton.setMaxSize(58, 58);          
            planetButton.setStyle("-fx-background-image: url(planet" + this.game.getPid(planet) +  ".png)");
            planetButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    planetHandle(planet, game.getAvailableNpcs(planet));
                }
            });
            
            planetButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {                    
                    planetTA.setText(game.getName(planet)+game.getDescription(planet));
                    itemGrid.add(planetTA, 0, 0);
                    if(game.getPossiblePlanets().contains(planet)) {
                       planetTA.appendText("\n\n\tThis planet is reachable");
                    } else {
                        planetTA.appendText("\n\n\tThis planet is out of reach");
                    }
                    
                }
            });
            
            planetButton.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    itemGrid.getChildren().remove(planetTA);
                }
            });
            System.out.println("buttonArray");
            buttonArray.add(planetButton);
        }
        
        sceneGrid.add(buttonArray.get(0), 2, 2);
        sceneGrid.add(buttonArray.get(1), 3, 0);
        sceneGrid.add(buttonArray.get(2), 5, 3);
        sceneGrid.add(buttonArray.get(3), 6, 1);
        sceneGrid.add(buttonArray.get(4), 1, 3);
        sceneGrid.add(buttonArray.get(5), 0, 0);
        sceneGrid.add(buttonArray.get(6), 3, 4);
        
        dialogueButton3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            }
        });
    }
    
    public void sceneClear() {
        sceneGrid.getChildren().clear();
    }
    
    public void itemClear() {
        itemGrid.getChildren().clear();
    }
    
    public void planetHandle(UUID planet, ArrayList<UUID> npcs) {
        Image planetImg = new Image(this.game.getImgPath(planet));
        sceneImage.setImage(planetImg);
        sceneClear();
        dialogueButton3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setSolarsystem();
            }    
        });
        this.game.travelToPlanet(planet);
        this.game.getDashboardUpdate();
        
        for (UUID npc : npcs) {
            this.npcChoices.add(new ListFoo(npc, this.game.getName(npc)));
        }
        this.npcCB.setItems(npcChoices);
        
        Button npcButton = new Button();
        sceneGrid.add(npcButton, 1, 4);
        npcButton.setStyle("-fx-background-image: url(data/"+this.game.getImgPath
        ((((ListFoo) npcCB.getValue()).getNpc()))+"/images/");
        npcButton.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event) {
                    game.startConversation(((ListFoo) npcCB.getValue()).getNpc());
                }
            });
    }
    
    public void titleHandle() {
        mainAnchor.getChildren().remove(titleTA);
    }
    
    public void handleAnswerButton(String ans) {
        this.game.processAnswer(ans);
        updateInv();
        updateStats();
        updateConversationText();
    }
    
    public void updateStats() {
        fuelTA.setText(""+this.game.getFuel());
        if(this.game.canWarp()) {
            warpTA.setText(""+this.game.getWarpFuel());
        }
        repTA.setText("" +this.game.getReputation());
        inGameTimeTA.setText("" + this.game.getInGameTime()); //InGameTime
    }
    
    public void updateConversationText() {
        this.game.getDashboardUpdate();
        for (Button dialogueButton : dialogueArray) {
            dialogueButton.setUserData(null);
            dialogueButton.setText("");
        }
        
        for (int i = 0; i == this.game.getAnswers().length; i++) {
            int n = i;
            dialogueArray.get(i).setUserData(this.game.getAnswers()[i]);
            dialogueArray.get(i).setText(this.game.getAnswers()[i]);
            dialogueArray.get(i).setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event) {
                    handleAnswerButton((String) dialogueArray.get(n).getUserData());
                }
            });
        }
    }
    
    public void updateInv() {
        for ( int i = 0; i == this.game.getInventory().size(); i++) {
            Image itemImg = new Image(this.game.getImgPath(this.game.getInventory().get(i)));
            this.itemImageViews.get(i).setImage(itemImg);
        }
    }
    
    public void handleWarp() {
        if (this.game.canWarp()) {   
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.game = new Game();
        this.itemImageViews.add(this.itemIV0);
        this.itemImageViews.add(this.itemIV1);
        this.itemImageViews.add(this.itemIV2);
        this.npcCB.setItems(npcChoices);
        
        setSolarsystem();
    }
}
