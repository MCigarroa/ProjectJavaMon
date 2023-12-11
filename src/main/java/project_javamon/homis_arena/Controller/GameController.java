package project_javamon.homis_arena.Controller;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import project_javamon.homis_arena.Game.Game;
import project_javamon.homis_arena.Game.GameState;
import project_javamon.homis_arena.Game.Player;
import project_javamon.homis_arena.Game.Pokemon.Card;
import project_javamon.homis_arena.Util.CardPosition;
import project_javamon.homis_arena.Util.FlagEvent;

import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class GameController implements Initializable {
    public AnchorPane gameMat;
    public HBox northBench;
    public HBox northActive;
    public HBox northPrize;
    public HBox southBench;
    public HBox southActive;
    public HBox southPrize;
    public VBox northDiscard;
    public VBox northDeck;
    public VBox southDiscard;
    public VBox southDeck;

    public Button showHandBtn;
    public Button endTurnBtn;

    Image backCard = new Image(getClass().getResourceAsStream("/images/cardback.jpg"));
    HBox southCardHand;
    HBox northCardHand;

    ArrayList<Node> northMat;
    ArrayList<Node> southMat;

    private List<Player> playerList;

    static int uniqueNumber = 1;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Controller initialized");
        initMat();
        System.out.println("Mat initialized");


    }
    public void initMat(){
        // All do the same just needed to fill up the boxes for alignment checks
        // TODO Logic to determine what cards go in needed


        initDeck(northDeck, CardPosition.DECK);
        initDeck(southDeck,CardPosition.DECK);
        fillPrize(northPrize, CardPosition.PRIZE);
        fillPrize(southPrize,CardPosition.PRIZE);
        southPrize.setSpacing(-100);
        fillActive(northActive,CardPosition.ACTIVE);
        fillActive(southActive,CardPosition.ACTIVE);
        fillDiscard(northDiscard,CardPosition.DISCARD);
        fillDiscard(southDiscard,CardPosition.DISCARD);
        fillBench(northBench,CardPosition.BENCH);
        fillBench(southBench,CardPosition.BENCH);

        generateSouthCardHand();
        generateNorthCardHand();

        northMat = new ArrayList<>(Arrays.asList(northBench, northActive, northPrize, northDiscard, northDeck, northCardHand));
        southMat = new ArrayList<>(Arrays.asList(southBench, southActive, southPrize, southDiscard, southDeck, southCardHand));

        Collections.shuffle(Game.getPlayerList().get(0).getDeck());
    }

    private void fillDiscard(VBox northDiscard, CardPosition cardPosition) {
    }

    // TODO move card to hand, send to discard, faced down prize, restrictionNotification, players not able to interact with other-side card,
    // TODO coin flip ui, UI damage tracker/ status,

    // Button Actions ===============================
    @FXML
    public void onSouthDeckClicked() {
        // Should auto-draw
        if (!GameState.getFlagEventList().contains(FlagEvent.PLAYER_CAN_DRAW_FROM_DECK)){
            drawCard(southCardHand);
        }
        Game.getPlayerList().get(0).printPlayerCards();
    }
    @FXML
    public void showHand() {
        if (southCardHand.getLayoutY() == gameMat.getPrefHeight() - 150){
            southCardHand.setLayoutY(gameMat.getPrefHeight()- 300);
        } else {
            southCardHand.setLayoutY(gameMat.getPrefHeight()- 150);
        }
    }
    @FXML
    public void endTurn() {
        showCoverScreen(gameMat);
    }
    private void showCoverScreen(AnchorPane gameMat) {
        // Create the overlay pane that will cover the game area
        StackPane coverPane = new StackPane();
        coverPane.setStyle("-fx-background-color: rgba(0, 0, 0, 1);");
        coverPane.setPrefSize(gameMat.getWidth(), gameMat.getHeight());

        Button switchPlayersBtn = new Button("Switch Players");
        switchPlayersBtn.setOnAction(e -> {
            switchPlayers();
            gameMat.getChildren().remove(coverPane);

        });

        coverPane.getChildren().add(switchPlayersBtn);
        StackPane.setAlignment(switchPlayersBtn, Pos.CENTER);

        // Add the coverPane to the gameMat
        gameMat.getChildren().add(coverPane);
        StackPane.setAlignment(coverPane, Pos.CENTER);
    }

    private void switchPlayers() {
        printMatContents("Before switch - NorthMat: ", northMat);
        printMatContents("Before switch - SouthMat: ", southMat);
        ArrayList<Node> tempSouthContent = new ArrayList<>();
        for (Node node : southMat) {
            tempSouthContent.add(node);
        }
        southMat.clear();
        for (Node node : northMat) {
            southMat.add(node);
        }
        northMat.clear();
        for (Node node : tempSouthContent) {
            northMat.add(node);
        }
        updateUIForMats();
        printMatContents("After switch - NorthMat: ", northMat);
        printMatContents("After switch - SouthMat: ", southMat);
    }
    private void updateUIForMats() {
        for (Node node : northMat) {
            System.out.println("Updating northMat: " + node);
            if (node instanceof Pane) {
                Pane pane = (Pane) node;
                pane.getChildren().clear();
                int index = northMat.indexOf(node);
                repopulatePane(pane, northMat.get(index));
            }
        }
        for (Node node : southMat) {
            System.out.println("Updating southMat: " + node);
            if (node instanceof Pane) {
                Pane pane = (Pane) node;
                pane.getChildren().clear();
                // Similar repopulation for the southMat
                int index = southMat.indexOf(node);
                repopulatePane(pane, southMat.get(index));
            }
        }
    }

    private void repopulatePane(Pane pane, Node content) {
        if (content instanceof Pane) {
            Pane contentPane = (Pane) content;
            for (Node child : contentPane.getChildren()) {
                System.out.println("Adding child to pane: " + child);
                pane.getChildren().add(child);
            }
        }
    }

    private void swapMatContents() {
        // Debug print before swap
        System.out.println("Before swap - NorthMat: " + northMat);
        System.out.println("Before swap - SouthMat: " + southMat);

        // Swap logic ...

        // Debug print after swap
        System.out.println("After swap - NorthMat: " + northMat);
        System.out.println("After swap - SouthMat: " + southMat);
    }

    // Button Actions END ===========================
    // Listeners ====================================
    private void enableDragOverAndDragDrop(HBox Hbox, CardPosition cardPosition) {
        Hbox.setOnDragOver(event -> {
            if (event.getGestureSource() != Hbox && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }

            event.consume();
        });
        Hbox.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                // Gets ID
                String cardId = db.getString();
                ImageView cardView = findCardViewById(cardId);
                if (cardView != null) {
                    // Will remove card from previous location at GUI
                    Node parent = cardView.getParent();
                    if (parent instanceof Pane) {
                        ((Pane) parent).getChildren().remove(cardView);
                    }
                    Hbox.getChildren().add(0,cardView);

                    // Will remove card from previous and add new location at Player
                    Card card = Game.getPlayerList().get(0).findCardById(cardId);
                    Game.getPlayerList().get(0).RemoveFromPrevious(card);
                    Game.getPlayerList().get(0).addToActive(card);

                    // Hack to get active to rotate properly
                    if (Hbox.getLayoutY() < 695) {
                        updateActiveSpacing(Hbox);
                    }
                    success = true;
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }
    private void enableDragAndDrop(ImageView cardView) {
        cardView.setOnDragDetected(mouseEvent -> {
            Dragboard dragboard = cardView.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent clipboardContent = new ClipboardContent();

            // This is only for the image being dragged
            SnapshotParameters snapshotParams = new SnapshotParameters();
            snapshotParams.setFill(Color.TRANSPARENT);
            Image dragImage = cardView.snapshot(snapshotParams, null);

            dragboard.setDragView(dragImage, mouseEvent.getX(), mouseEvent.getY());
            // ===============================

            String cardId = (String) cardView.getProperties().get("cardId");
            clipboardContent.putString(cardId);

            dragboard.setContent(clipboardContent);
            mouseEvent.consume();
        });

        cardView.setOnDragDone(event -> {

            cardView.setOpacity(1);
            setUpContextMenuWithPopUp(cardView);
            event.consume();
        });
    }
    // Listeners End ================================

    private void drawCard(HBox hBox) {
        // Will need to know which player is playing to get proper deck
        Card drawnCard = Game.getPlayerList().get(0).drawCard();

        if (drawnCard != null) {
            String imagePath = "/images/" + drawnCard.getImage();
            InputStream is = getClass().getResourceAsStream(imagePath);

            if (is != null) {
                Image cardImage = new Image(is);
                ImageView cardView = new ImageView(cardImage);
                setCardHeightAndWidth(cardView);
                addDragAndDropCapability(cardView, drawnCard);
                setUpContextMenuWithPopUp(cardView);

                hBox.getChildren().add(cardView);
                updateDeckUI(southDeck);
            } else {
                System.out.println("Image not found: " + imagePath);
            }
        }

        if (!gameMat.getChildren().contains(hBox)) {
            gameMat.getChildren().add(hBox);
        }
    }

    private void generateSouthCardHand() {
        southCardHand = new HBox();
        southCardHand.setLayoutX(gameMat.getPrefWidth() /  2);
        southCardHand.setLayoutY(gameMat.getPrefHeight() - 150);
        gameMat.getChildren().add(southCardHand);
    }
    private void generateNorthCardHand() {
        northCardHand = new HBox();
        northCardHand.setLayoutX(gameMat.getPrefWidth() /  2);
        northCardHand.setLayoutY(gameMat.getPrefHeight() - 150);
        gameMat.getChildren().add(northCardHand);
    }

    private void updateDeckUI(VBox southDeck) {
        if (!southDeck.getChildren().isEmpty()) {
            southDeck.getChildren().remove(southDeck.getChildren().getLast());
            updateCardHandSpacing(southCardHand);
        }
    }

    public void initDeck(VBox deck, CardPosition cardPosition) {
        // TODO Will need to get players custom deck
        Player player = Game.getPlayerList().get(0);

        if (player.getDeck() == null) {
            System.out.println("Deck is null.");
            return;
        }

        deck.getChildren().clear();
        int deckSize = player.getDeck().size();

        for (int index = 0; index < deckSize; index++) {
            ImageView cardView = new ImageView(backCard);
            setCardHeightAndWidth(cardView);
            deck.getChildren().add(cardView);
        }

        if (deck.getLayoutY() < gameMat.getPrefHeight() / 2) flipCard(deck);
        deck.setSpacing(-200);
        deck.setBackground(Background.fill(Color.BLACK));
    }
    private void fillPrize(HBox prize, CardPosition cardPosition) {
        initBox(prize);
        enableDragOverAndDragDrop(prize, cardPosition);
    }

    private void enableDragOverAndDragDropPrize(HBox prize) {
    }

    private void fillActive(HBox active, CardPosition cardPosition) {
        initBox(active);
        enableDragOverAndDragDrop(active, cardPosition);
    }

    private void initBox(HBox active) {
        active.getChildren().clear();
        if (active.getLayoutY() < gameMat.getPrefHeight() / 2) flipCard(active);
        //active.setBackground(Background.fill(Color.BLACK));
    }
    private void fillBench(HBox bench, CardPosition cardPosition){
        bench.getChildren().clear();
        if (bench.getLayoutY() < gameMat.getPrefHeight() / 2) flipCard(bench);
        bench.setSpacing(5);
        enableDragOverAndDragDrop(bench, cardPosition);
    }



    private void updateActiveSpacing(HBox hbox) {
        double overlap = 100;
        // For wanted behavior I needed to rotate southActive 180, resulting in images being rotated and mirrored
        for (int i = 0; i < hbox.getChildren().size(); i++) {
            Node node = hbox.getChildren().get(i);
            node.setRotate(180);
            node.setTranslateX((hbox.getChildren().size() - 1 - i) * overlap);
        }
    }
    public void updateCardHandSpacing(HBox cardHand){
        int numberOfCards = cardHand.getChildren().size();
        double cardSpacing = 5;
        double cardWidth = 144;

        double totalCardWidth = (cardWidth * numberOfCards) + (cardSpacing * (numberOfCards - 1));

        if (totalCardWidth > 1400) {
            double excessWidth = totalCardWidth - 1400;
            // This controls how much it compresses: 100 ATM it is too jaring
            double reductionPerCard = Math.max(excessWidth / numberOfCards, 100);
            cardSpacing -= reductionPerCard;
            totalCardWidth = (cardWidth * numberOfCards) + (cardSpacing * (numberOfCards - 1));
        }

        cardHand.setSpacing(cardSpacing);

        cardHand.setLayoutX((gameMat.getPrefWidth() - totalCardWidth) / 2);
        cardHand.setLayoutY(gameMat.getPrefHeight() - 150);
    }

    private void setCardHeightAndWidth(ImageView cardView){
        cardView.setPreserveRatio(true);
        cardView.setFitHeight(200);
        cardView.setFitWidth(144);
    }

    private void addDragAndDropCapability(ImageView cardView, Card card) {
        String cardId = card.getCardID();
        cardView.getProperties().put("cardId", cardId);
        System.out.println("Assigned cardId: " + cardId + " to card");

        enableDragAndDrop(cardView);
    }

    private void setUpContextMenuWithPopUp(ImageView cardView) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem viewLargeItem = new MenuItem("View Large");
        contextMenu.getItems().add(viewLargeItem);

        viewLargeItem.setOnAction(event -> setCardPopupEffects(cardView));

        cardView.setOnContextMenuRequested(event -> {
            contextMenu.show(cardView, event.getScreenX(), event.getScreenY());
        });
    }

    private void setCardPopupEffects(ImageView cardView) {
        Popup popup = new Popup();
        ImageView enlargedCardView = new ImageView(cardView.getImage());
        // Size of Popup
        enlargedCardView.setFitWidth(cardView.getFitWidth() * 2);
        enlargedCardView.setFitHeight(cardView.getFitHeight() * 2);
        enlargedCardView.setPreserveRatio(true);

        popup.getContent().add(enlargedCardView);
        popup.setAutoHide(true);

        // Location of Popup
        popup.setX(gameMat.getPrefWidth() / 2);
        popup.setY(gameMat.getPrefHeight() / 2);

        popup.show(cardView.getScene().getWindow());
    }



    // Utility =========================================================================================
    private ImageView findCardViewById(String cardId) {
        for (Node container : southMat) {
            if (container instanceof Pane) {
                ImageView found = searchContainerForCard((Pane) container, cardId);
                if (found != null) return found;
            }
        }

        return null;
    }
    private ImageView searchContainerForCard(Pane container, String cardId) {
        for (Node node : container.getChildren()) {
            if (node instanceof ImageView) {
                ImageView imageView = (ImageView) node;
                if (cardId.equals(imageView.getProperties().get("cardId"))) {
                    return imageView;
                }
            }
        }
        return null;
    }

    private void flipCard(Pane pane) {
        for (Node node : pane.getChildren()) {
            if (node instanceof ImageView) {
                ((ImageView) node).setScaleY(-1);
            }
        }
    }

    private void printMatContents(String message, ArrayList<Node> mat) {
        System.out.println(message);
        for (Node node : mat) {
            if (node instanceof Pane) {
                Pane pane = (Pane) node;
                System.out.println("Pane: " + pane + ", Children: " + pane.getChildren());
            } else {
                System.out.println("Node: " + node);
            }
        }
    }


    // Utility END =====================================================================================


}