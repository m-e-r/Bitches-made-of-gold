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
    @FXML
    private Button dropItem0;
    @FXML
    private Button dropItem1;
    @FXML
    private Button dropItem2;
    
    TextArea planetTA = new TextArea();
    private ObservableList<CheatList> npcChoices = FXCollections.observableArrayList();
    private ArrayList<Button> buttonArray = new ArrayList();
    private ArrayList<ImageView> itemImageViews = new ArrayList();
    private ArrayList<Button> dialogueArray = new ArrayList();
    private ArrayList<Button> dropItemArray = new ArrayList();
    private int[] yCoordinates;
    private int[] xCoordinates;
    
    
    public void setSolarsystem() {
        titleTA.setText("F.U.T.U.R.A.M.A.");
        this.sceneClear();
        Image solarsystem1 = new Image("solarsystemy.png");
        sceneImage.setImage(solarsystem1);
        ArrayList<UUID> listOfPlanets = new ArrayList();
        listOfPlanets = this.game.getListOfPlanets();
        this.updateStats();
        
        for(UUID planet : listOfPlanets){
            Button planetButton = new Button();
            planetButton.setUserData(planet);
            planetButton.setMaxSize(30, 30);          
            planetButton.setStyle("-fx-background-image: url(planet" + this.game.getPid(planet) +  ".png)");
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
            
            buttonArray.add(planetButton);
        }
        
        this.updateInv();
        dialogueButton3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            }
        });
        npcChoices.clear();
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
        dialogueTA.setText(this.game.getDashboardUpdate());

        for (UUID npc : npcs) {
            this.npcChoices.add(new CheatList(npc, this.game.getName(npc)));
        }
        this.npcCB.setItems(npcChoices);
        //game.getImgPath((((ListFoo) npcCB.getValue()).getNpc()))+
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
    
    public void titleHandle() {
        mainAnchor.getChildren().remove(titleTA);
    }
    
    public void handleAnswerButton(String ans) {
        this.game.processAnswer(ans);
        this.updateInv();
        this.updateStats();
        this.updateConversationText();
    }
    
    public void updateStats() {
        fuelTA.setText("Fuel: "+this.game.getFuel());
        if(this.game.canWarp()) {
            warpTA.setText("WarpFuel: "+this.game.getWarpFuel());
        }
        repTA.setText("Rep: " +this.game.getReputation());
        inGameTimeTA.setText("Time: " + this.game.getInGameTime()); //InGameTime
    }
    
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
            dialogueButton3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setSolarsystem();
            }    
        });
        }
    }
    
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
    
    public void dropItems(ActionEvent event) {
        this.game.dropItem((UUID)((Button) event.getSource()).getUserData());
        this.updateInv();
        
    }
    
    public void handleWarp() {
        if (this.game.canWarp()) {   
        }
    }
    
    public void getHelp(ActionEvent event) {
        
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
