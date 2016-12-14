/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worldofzuulgui;

import java.awt.event.ActionListener;
import java.io.IOException;
import worldofzuul.iGame;
import worldofzuul.Game;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
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
import javafx.scene.control.ListView;
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
    @FXML
    private SplitPane startSP;
    @FXML
    private TextField nameTF;
    @FXML
    private ChoiceBox<CheatList> scenariosCB;
    @FXML
    private Button startButton;
    @FXML
    private TextArea item0TA;
    @FXML
    private TextArea item1TA;
    @FXML
    private TextArea item2TA;
    private ArrayList<TextArea> itemInfo = new ArrayList();
    @FXML
    private ListView<String> hsList;
    @FXML
    private TextArea hsTA;
    @FXML
    private AnchorPane hsAnchor;
    @FXML
    private Button qqButton;
    @FXML
    private AnchorPane gameAnchor;
    @FXML
    private AnchorPane helpAnchor;
    @FXML
    private Button backtoGameButton;
    
    //Defines instance variables
    iGame game;
    TextArea planetTA = new TextArea();
    private ObservableList<CheatList> npcChoices = FXCollections.observableArrayList();
    private ObservableList<CheatList> scenarios = FXCollections.observableArrayList();
    private ObservableList<String> hs = FXCollections.observableArrayList();
    private ObservableList<String> helpTopics = FXCollections.observableArrayList();
    private ArrayList<Button> buttonArray = new ArrayList();
    private ArrayList<ImageView> itemImageViews = new ArrayList();
    private ArrayList<Button> dialogueArray = new ArrayList();
    private ArrayList<Button> dropItemArray = new ArrayList();
    private String availableNpcs;
    private TreeMap<String, String> helps = new TreeMap();
    @FXML
    private ListView<String> helpLV;
    @FXML
    private TextArea helpTA;
    
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
            planetButton.setStyle("-fx-background-image: url("+ this.game.getImgPath(planet, true) +")");
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
                    if (game.isWar(planet)) {
                        planetTA.appendText("\n War is war");
                    }
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
                    npcChoices.clear();
                    dialogueButton2.setText(null);
                    
                    //game.travelToPlanet(game.getMoonId(planet));
                    UUID moonUuid = game.getMoonId(planet);
                    planetHandle(moonUuid, game.getAvailableNpcs(moonUuid));
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
        this.npcChoices.clear();
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
    
    @FXML
    public void handleStart(ActionEvent event) {
        this.game.setScenario(this.scenariosCB.getValue().getNpc());
        mainAnchor.getChildren().remove(startSP);
        mainAnchor.getChildren().remove(helpAnchor);
        mainAnchor.getChildren().remove(hsAnchor);
        this.game.startGame(scenariosCB.getValue().getNpc(), this.nameTF.getText());
        this.timeTimer();
        this.updateStats();
        this.planetHandle(this.game.getPlayerPosition(), this.game.getAvailableNpcs(this.game.getPlayerPosition()));
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
        this.getItemDist();
    }
    
    /**
     * Updates the dialogue text area and possible answers.
     */
    public void updateConversationText() {
        this.npcCB.setDisable(true);
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
                    UUID currentPlanetUuid = game.getPlayerPosition();
                    UUID moonUuid = game.getMoonId(currentPlanetUuid);
                    npcChoices.clear();
                    dialogueButton2.setText(null);
                    
                    //game.travelToPlanet(game.getMoonId(planet));
                    planetHandle(moonUuid, game.getAvailableNpcs(moonUuid));
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
            this.sceneClear();
            this.npcCB.setDisable(false);
            this.npcCB.setValue(null);
            this.npcCB.setItems(npcChoices);
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
                this.itemInfo.get(i).setText(null);
            }
        }
        this.getItemDist();
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
        this.mainAnchor.getChildren().add(helpAnchor);
        this.helpLV.setItems(helpTopics);
        this.helpLV.getSelectionModel().selectedItemProperty().addListener(
        (observableValue, oldValue, newValue) ->
        helpTA.setText(helps.get(newValue)));
        
        this.backtoGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainAnchor.getChildren().remove(helpAnchor);
            }
        });
        
    }
    
    public void timeTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Calendar playedTime = new GregorianCalendar();
                playedTime.setTimeInMillis(game.getPlayedMillis());
                timeTA.setText("" + (playedTime.get(Calendar.HOUR) - 1) + ":" + playedTime.get(Calendar.MINUTE) + ":" + playedTime.get(Calendar.SECOND));
            
            }
        }, 1000, 1000);
    }
    
    public void getItemDist() {
        int i = 0;
        for (UUID item : this.game.getInventory()) {
            this.itemInfo.get(i).setText(this.game.getDeliveryPlanet(item) + "\n" +
                                        this.game.getDeliveryNpc(item));
            i++;
        }
    }
    
    public void clearAll(){
        this.mainAnchor.getChildren().clear();
    }
    
    @FXML
    public void showHighscore() {
        this.mainAnchor.getChildren().remove(gameAnchor);
        this.mainAnchor.getChildren().add(hsAnchor);
        hsAnchor.toFront();
        this.hs.addAll(this.game.quitGame());
        this.hsList.setItems(hs);
        
    }
    
    @FXML
    public void qqButtonAction(ActionEvent event) {
        Stage stage = (Stage) qqButton.getScene().getWindow();
        stage.close();
    }
    
    public void readyHelpText() {
        helps.put("1. Help info", "Hello, and welcome to the help screen. "
                + "Please use the menu to the left in order to recieve help "
                + "on a specific problem. Or use the random help generator "
                + "(also on the left) to recieve some random "
                + "help for those cold days.");
        helps.put("2. How do you travel to a planet?", "Well by using your spaceship "
                + "of course!\n" + "The spaceship in this universe is "
                + "mouse-driven*, which means that you just click on the planet "
                + "you want to travel to after clicking the \"Display Planets\" "
                + "button (cannot be used while in a conversation).\n" +"An "
                + "easy way to travel is by using warp fuel for your ITD. "
                + "When warping, it doesn't matter if you have papers on your "
                + "packages, even if there is a war!\n" +
                "*some in-game fuel may be used.");
        helps.put("3. Conversating with NPCs", "If you are playing this game, "
                + "you might find yourself to be the kind of person who has "
                + "trouble conversating irl. Don't worry. Here you just choose "
                + "an NPC from the list in the top right corner of a planet "
                + "scene and click on them, as they appear on the screen. "
                + "Whatever the NPC has to say, will appear as text in the "
                + "dialogue box below the scene. Afraid you will mess up the "
                + "conversation by being too awkward? Again there is no worry; "
                + "we have limited your answers to no more than three options, "
                + "which will appear on the buttons to the right "
                + "of the dialogue box." );
        helps.put("4. Understanding the stats", "In this game you really only need"
                + " to care about three stats, so forget about your real life "
                + "hunger, financial problems and (non-existing) love life.\n" +
                "\n" +"1: Time. Time progresses as you do stuff; travel around, "
                + "talk to people and so on. Sometimes you don't want to let to "
                + "much time go by before you deliver a package, or else you "
                + "might lose some of your reputation, which is the second stat."
                + "\n" +"\n" +"2: Reputation. This is what you need to impress "
                + "people and the girl of your dreams. As you deliver more and "
                + "more packages on time, your reputation (rep for short) "
                + "increases. This is the key to get to the top of the highscore "
                + "list. But if you ever fall down to 0 rep, you will die of "
                + "discomfort and the game will end.\n" +"\n" +"3: Fuel. Fuel "
                + "is used when traveling between planets. You shipping company "
                + "pays for your full refueling at every planet you visit, so "
                + "it really only limits your route-planning.");
        helps.put("5. Packages and your inventory", "Your inventory is displayed "
                + "to the right. Whenever you pick up a package, you will be "
                + "able to see it over there along with some information about "
                + "where to deliver it.\n" +"Important information: If you "
                + "don't have papers on your packages and you travel to a "
                + "planet (not using warp) where there is a war, you will die. "
                + "And the game will end. You can always get papers on your "
                + "starting planet at your company.");
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
        this.itemInfo.add(this.item0TA);
        this.itemInfo.add(this.item1TA);
        this.itemInfo.add(this.item2TA);
        this.readyHelpText();
        this.helpTopics.addAll(helps.keySet());
        
        System.out.println(this.game.getPossibleScenarios().size());
        for (UUID scenario : this.game.getPossibleScenarios()) {
            this.scenarios.add(new CheatList(scenario, this.game.getName(scenario)));
        }              
        System.out.println(this.scenarios.size());
        this.scenariosCB.setItems(scenarios);
        

    }

    @FXML
    private void titleHandle(MouseEvent event) {
    }

}
