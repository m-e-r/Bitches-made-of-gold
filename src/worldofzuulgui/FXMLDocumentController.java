/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worldofzuulgui;

import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author MER
 */
public class FXMLDocumentController implements Initializable {
    
    //Defines controllers
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
    @FXML
    private Button dropItem0;
    @FXML
    private Button dropItem1;
    @FXML
    private Button dropItem2;
    @FXML
    private Button helpButton;
    @FXML
    private Button quitButton;
    @FXML
    private RadioButton warpRB;
    
    
    //Defines instance variables
    iGame game;
    TextArea planetTA = new TextArea();
    private ObservableList<CheatList> npcChoices = FXCollections.observableArrayList();
    private ArrayList<Button> buttonArray = new ArrayList();
    private ArrayList<ImageView> itemImageViews = new ArrayList();
    private ArrayList<Button> dialogueArray = new ArrayList();
    private ArrayList<Button> dropItemArray = new ArrayList();
    private String availableNpcs;
    @FXML
    private SplitPane startSP;
    @FXML
    private Button scenarioButton;
    @FXML
    private TextField nameTF;
    
    /**
     * Sets the scene as the solar system. 
     * This is the default scene.
     */
    @FXML
    public void setSolarsystem() {
        titleTA.setText("F.U.T.U.R.A.M.A.");
        this.sceneClear();
        Image solarsystem1 = new Image("solarsystemy.png");
        sceneImage.setImage(solarsystem1);
        ArrayList<UUID> listOfPlanets = new ArrayList();
        listOfPlanets = this.game.getListOfPlanets();
        this.updateStats();
        this.dialogueButton2.setText(null);
        this.dialogueButton2.setOnAction(null);
        
        for(UUID planet : listOfPlanets){
            Button planetButton = new Button();
            planetButton.setUserData(planet);
            planetButton.setMaxSize(30, 30);          
            planetButton.setStyle("-fx-background-image: url(planet" + this.game.getPid(planet) +  ".png)");
            if (planet == this.game.getPlayerPosition()) {
                planetButton.setDisable(true);
            }
            planetButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    planetHandle(planet, game.getAvailableNpcs(planet));
                }
            });

            
            this.sceneGrid.add(planetButton, (int) ((this.game.getPositionCoordinates(planet)[0]/167)+0.5), (int) ((this.game.getPositionCoordinates(planet)[1]/167)+0.5));
            planetButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {   
                    availableNpcs = "";
                    for (UUID npc : game.getAvailableNpcs(planet)) {
                        availableNpcs += game.getName(npc) + ", ";
                    }
                    planetTA.setText(game.getName(planet)+ "\n" + game.getDescription(planet) + "\nNpcs on this mofo: " + availableNpcs );
                    itemGrid.add(planetTA, 0, 0);
                    planetTA.setWrapText(true);
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
            
            buttonArray.add(planetButton);
        }
        
        this.updateInv();
        dialogueButton3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            }
        });
        npcChoices.clear();
        
        this.canWarp();
    }
    
    /**
     * Clears the scene grid from all nodes.
     * This is called before every new scene is loaded.
     */
    public void sceneClear() {
        sceneGrid.getChildren().clear();
    }
    
    /**
     * Not finished?.
     */
    public void itemClear() {
        itemGrid.getChildren().clear();
    }
    
    /**
     * Loads the scene of the new planet and moves the player to it.
     * @param planet UUID of the planet which is travlled to
     * @param npcs UUIDs of the npcs on that planet
     */
    public void planetHandle(UUID planet, ArrayList<UUID> npcs) {
        Image planetImg = new Image(this.game.getImgPath(planet));
        sceneImage.setImage(planetImg);
        sceneClear();
        if (this.game.getMoonId(planet) != null) {
            dialogueButton2.setText("To the moon!");
            dialogueButton2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    dialogueButton2.setText(null);
                    game.travelToPlanet(game.getMoonId(planet));
                }            
            });
        }
        dialogueButton3.setText("Display Planets");
        dialogueButton3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setSolarsystem();
            }    
        });
        dialogueTA.setText(this.game.getDashboardUpdate());
        
            
        if (warpRB.isSelected()) {
            this.game.processWarp(planet);
        } else {
            this.game.travelToPlanet(planet);
        }   
        

        for (UUID npc : npcs) {
            this.npcChoices.add(new CheatList(npc, this.game.getName(npc)));
        }
        this.npcCB.setItems(npcChoices);
        Button npcButton = new Button();
        this.npcCB.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (npcCB.getValue() == null) {
                    return;
                }
                sceneClear();
                npcButton.setMaxSize(100, 50);
                sceneGrid.add(npcButton, 1, 3, 1, 3);
                npcButton.setStyle("-fx-background-color: transparent;");
                Image npcImage = new Image(game.getImgPath((((CheatList) npcCB.getValue()).getNpc())));
                npcButton.setGraphic( new ImageView(npcImage));
                npcButton.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        game.startConversation(((CheatList) npcCB.getValue()).getNpc());
                        updateConversationText();
                    }
                });
            }
        });
    }
    
    /**
     * Malte?.
     */
    @FXML
    public void titleHandle() {
        mainAnchor.getChildren().remove(titleTA);
    }
    
    @FXML
    public void handleStart(ActionEvent event) {
        mainAnchor.getChildren().remove(startSP);
    }
    
    /**
     * Sends the selected answer to Game and updates stuff.
     * @param ans The selected answer from user
     */
    public void handleAnswerButton(String ans) {
        this.game.processAnswer(ans);
        this.updateInv();
        this.updateStats();
        this.updateConversationText();
    }
    
    /**
     * Updates stats.
     */
    public void updateStats() {
        fuelTA.setText("Fuel: "+this.game.getFuel());
        if(this.game.canWarp()) {
            warpTA.setText("WarpFuel: "+this.game.getWarpFuel());
        }
        repTA.setText("Rep: " +this.game.getReputation());
        inGameTimeTA.setText("Time: " + this.game.getInGameTime()); //InGameTime
    }
    
    /**
     * Updates the dialogue text area and possible answers.
     */
    public void updateConversationText() {
        dialogueTA.setText(this.game.getDashboardUpdate());
        for (Button dialogueButton : dialogueArray) {
            dialogueButton.setText("");
            dialogueButton.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event) {
                }
            });
        }
        
        if(this.game.getAnswers() != null) {
            for (int i = 0; i < this.game.getAnswers().length; i++) {
            int n= i+1;
            
            dialogueArray.get(i).setUserData(this.game.getAnswers()[i]);
            dialogueArray.get(i).setText(this.game.getAnswers()[i]);
            dialogueArray.get(i).setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event) {
                    handleAnswerButton((String) ((Button) event.getSource()).getUserData());
                }
            });
            }
        } else {
            if (this.game.getMoonId(this.game.getPlayerPosition()) != null) {
                this.dialogueButton2.setText("To the moon!");
                dialogueButton2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    dialogueButton2.setText(null);
                    game.travelToPlanet(game.getMoonId(game.getPlayerPosition()));
                }            
            });
            }
            dialogueButton3.setText("Display Planets");
            dialogueButton3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialogueButton3.setText(null);
                setSolarsystem();
            }    
        });
        }
    }
    
    /**
     * Updates the inventory.
     */
    public void updateInv() {
        for (int i = 0; i < this.itemImageViews.size(); i++) {
            if (i < this.game.getInventory().size()) {
                Image itemImg = new Image(this.game.getImgPath(this.game.getInventory().get(i)));
                this.itemImageViews.get(i).setImage(itemImg);
                this.itemImageViews.get(i).setUserData(this.game.getInventory().get(i));
                this.dropItemArray.get(i).setUserData(this.game.getInventory().get(i));
            } else {
                this.itemImageViews.get(i).setImage(null);
                this.itemImageViews.get(i).setUserData(null);
                this.dropItemArray.get(i).setUserData(null);
            }            
        }                
    }
    
    /**
     * Checks which button is pressed and drops the corresponding item.
     * @param ansButton One of the three drop buttons
     */
    @FXML
    public void dropItems(ActionEvent ansButton) {
        this.game.dropItem((UUID)((Button) ansButton.getSource()).getUserData());
        this.updateInv();        
    }
    
    /**
     * Checks if warp is available.
     */
    public void canWarp() {
        if (this.game.canWarp()) {
            warpRB.setDisable(false);
        }
    }
    
    /**
     *
     * @param event
     */
    @FXML
    public void handleHelp(ActionEvent event) {

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.game = new Game();
        this.itemImageViews.add(this.itemIV0);
        this.itemImageViews.add(this.itemIV1);
        this.itemImageViews.add(this.itemIV2);
        this.dialogueArray.add(this.dialogueButton1);
        this.dialogueArray.add(this.dialogueButton2);
        this.dialogueArray.add(this.dialogueButton3);
        this.dropItemArray.add(this.dropItem0);
        this.dropItemArray.add(this.dropItem1);
        this.dropItemArray.add(this.dropItem2);
        this.npcCB.setItems(npcChoices);
        
        setSolarsystem();
    }

}
