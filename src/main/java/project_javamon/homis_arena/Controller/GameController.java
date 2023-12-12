package project_javamon.homis_arena.Controller;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
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
import project_javamon.homis_arena.Main;
import project_javamon.homis_arena.Util.CardPosition;
import project_javamon.homis_arena.Util.FlagEvent;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;

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

    Image backCard = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/cardback.jpg")));
    HBox southHand;
    HBox northHand;

    ArrayList<Node> northMat;
    ArrayList<Node> southMat;

    Game game = Main.getGame();
    GameState gameState = Main.getGameState();

    private List<Player> playerList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Controller initialized");
        initMat();
        System.out.println("Mat initialized");

        Game game = Main.getGame();
        GameState gameState = Main.getGameState();

        Player player1 = game.getPlayerList().get(0);
        bindPlayerToUI(player1);
        Player player2 = game.getPlayerList().get(1);
        bindPlayerToUI(player2);
    }

    private void bindPlayerToUI(Player player) {
        player.getHand().addListener((ListChangeListener.Change<? extends Card> change) -> {
            updateHandUI(player.getHand());
        });
        player.getActive().addListener((ListChangeListener.Change<? extends Card> change) -> {
            updateActiveUI(player.getActive());
        });
//        player.getPrize().addListener((ListChangeListener.Change<? extends Card> change) -> {
//            updatePrizeUI(player.getHand());
//        });
//        player.getDiscard().addListener((ListChangeListener.Change<? extends Card> change) -> {
//            updateDiscardUI(player.getHand());
//        });
//        player.getDiscard().addListener((ListChangeListener.Change<? extends Card> change) -> {
//            updateDiscardUI(player.getHand());
//        });
    }


    private void updateActiveUI(ObservableList<Card> hand) {
        updateActiveSpacing(southActive);
    }

    private void updateHandUI(ObservableList<Card> hand) {
        updateSouthCardHand();
        updateCardHandSpacing(southHand);
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

        northMat = new ArrayList<>(Arrays.asList(northBench, northActive, northPrize, northDiscard, northDeck, northHand));
        southMat = new ArrayList<>(Arrays.asList(southBench, southActive, southPrize, southDiscard, southDeck, southHand));

        Collections.shuffle(game.getPlayerList().get(0).getDeck());
    }

    private void fillDiscard(VBox northDiscard, CardPosition cardPosition) {
    }
    // TODO move card to hand, send to discard, faced down prize, restrictionNotification, players not able to interact with other-side card,
    // TODO coin flip ui, UI damage tracker / status,

    // Button Actions ===============================
    @FXML
    public void onSouthDeckClicked() {
        Player currentPlayer = game.getPlayerList().get(0);
        if (!GameState.getFlagEventList().contains(FlagEvent.PLAYER_CAN_DRAW_FROM_DECK)){
            currentPlayer.drawCard();
        }
        currentPlayer.printPlayerCards();
    }
    @FXML
    public void showHand() {
        if (southHand.getLayoutY() == gameMat.getPrefHeight() - 150){
            southHand.setLayoutY(gameMat.getPrefHeight()- 300);
        } else {
            southHand.setLayoutY(gameMat.getPrefHeight()- 150);
        }
    }
    @FXML
    public void endTurn() {
        showCoverScreen(gameMat);
    }
    private void showCoverScreen(AnchorPane gameMat) {
        // This creates a pane that covers the screen for player switch
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

        gameMat.getChildren().add(coverPane);
        StackPane.setAlignment(coverPane, Pos.CENTER);
    }

    private void switchPlayers() {
        printMatContents("Before switch - NorthMat: ", northMat);
        printMatContents("Before switch - SouthMat: ", southMat);
        Collections.reverse(game.getPlayerList());

        ArrayList<Node> tempSouthContent = new ArrayList<>();
        tempSouthContent.addAll(southMat);
        southMat.clear();
        southMat.addAll(northMat);
        northMat.clear();
        northMat.addAll(tempSouthContent);
        updateUI();
        printMatContents("After switch - NorthMat: ", northMat);
        printMatContents("After switch - SouthMat: ", southMat);
    }

    // Button Actions END ===========================
    // Listeners ====================================
    private void setupDropZone(Pane pane, CardPosition cardPosition) {
        pane.setOnDragOver(event -> {
            if (event.getGestureSource() != pane && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
        pane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                // Gets ID
                String cardId = db.getString();
                moveCardToLocation(cardId,pane);

                // Hack to get active to rotate properly
                if (pane.equals(southActive)) {
                    updateActiveSpacing((HBox) pane);
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void moveCardToLocation(String cardId, Pane newLocation) {
        System.out.println("Moving card with ID: " + cardId + " to new location: " + newLocation);

        Card card = findCardById(cardId);
        Player currentPlayer = game.getPlayerList().get(0);

        currentPlayer.RemoveFromPrevious(card, determineCardPosition(newLocation));
        updatePlayerListWithNewCardLocation(currentPlayer, card, newLocation);

        ImageView cardView = findCardViewById(cardId);
        if (cardView != null) {
            Pane currentParent = (Pane) cardView.getParent();
            if (currentParent != null) {
                currentParent.getChildren().remove(cardView);
            }
            newLocation.getChildren().add(cardView);
        } else {
            System.out.println("Error: CardView not found for card ID: " + cardId);
        }

        updateUI();
    }

    private CardPosition determineCardPosition(Pane newLocation) {
        // Determine the card position based on the new location
        if (newLocation.equals(southHand)) return CardPosition.HAND;
        if (newLocation.equals(southBench)) return CardPosition.BENCH;
        if (newLocation.equals(southActive)) return CardPosition.ACTIVE;
        if (newLocation.equals(southDiscard)) return CardPosition.DISCARD;
        if (newLocation.equals(southPrize)) return CardPosition.PRIZE;
        return CardPosition.UNKNOWN;
    }

    private void updatePlayerListWithNewCardLocation(Player player, Card card, Pane newLocation) {
        // Add card to the new list based on new location
        if (newLocation.equals(southHand)) player.getHand().add(card);
        else if (newLocation.equals(southBench)) player.getBench().add(card);
        else if (newLocation.equals(southActive)) player.getActive().add(card);
        else if (newLocation.equals(southDiscard)) player.getDiscard().add(card);
        else if (newLocation.equals(southPrize)) player.getPrize().add(card);
        else System.out.println("Error: Unknown new location for the card");
    }

    public void updateUI() {
        southHand.getChildren().clear();
        southBench.getChildren().clear();
        southActive.getChildren().clear();
        southPrize.getChildren().clear();
        southDiscard.getChildren().clear();
        northHand.getChildren().clear();
        northBench.getChildren().clear();
        northActive.getChildren().clear();
        northPrize.getChildren().clear();
        northDiscard.getChildren().clear();

        populateContainerFromList(southHand, game.getPlayerList().get(0).getHand());
        populateContainerFromList(southBench, game.getPlayerList().get(0).getBench());
        populateContainerFromList(southActive, game.getPlayerList().get(0).getActive());
        populateContainerFromList(southPrize, game.getPlayerList().get(0).getPrize());
        populateContainerFromList(southDiscard, game.getPlayerList().get(0).getDiscard());
        populateContainerFromList(northHand, game.getPlayerList().get(1).getHand());
        populateContainerFromList(northBench, game.getPlayerList().get(1).getBench());
        populateContainerFromList(northActive, game.getPlayerList().get(1).getActive());
        populateContainerFromList(northPrize, game.getPlayerList().get(1).getPrize());
        populateContainerFromList(northDiscard, game.getPlayerList().get(1).getDiscard());

        updateActiveSpacing(southActive);
        updateActiveSpacing(northActive);
        updateHandUI();
    }

    private void populateContainerFromList(Pane container, ObservableList<Card> cardList) {
        for (Card card : cardList) {
            ImageView cardView;
            if (container.equals(southPrize) || container.equals(southDiscard) || container.equals(northPrize) ||
                    container.equals(northHand) || container.equals(northDiscard)){
                cardView = new ImageView(backCard);
                setCardHeightAndWidth(cardView);
                addDragAndDropCapability(cardView, card);
                setUpContextMenuWithPopUp(cardView);
            } else {
                cardView = createCardImageView(card);
            }

            container.getChildren().add(cardView);
        }
    }

    public Card findCardById(String cardId) {
        Player player = game.getPlayerList().get(0);
        return player.findCardById(cardId);
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
    private void generateSouthCardHand() {
        southHand = new HBox();
        southHand.setLayoutX(gameMat.getPrefWidth() /  2);
        southHand.setLayoutY(gameMat.getPrefHeight() - 150);
        gameMat.getChildren().add(southHand);
    }
    private void generateNorthCardHand() {
        northHand = new HBox();
        northHand.setLayoutX(gameMat.getPrefWidth() /  2);
        northHand.setLayoutY(0);
        gameMat.getChildren().add(northHand);
    }

    private void updateDeckUI(VBox southDeck) {
        if (!southDeck.getChildren().isEmpty()) {
            updateCardHandSpacing(southHand);
        }
    }

    public void initDeck(VBox deck, CardPosition cardPosition) {
        // TODO Will need to get players custom deck
        Player player = game.getPlayerList().get(0);

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
        setupDropZone(prize, cardPosition);
    }

    private void fillActive(HBox active, CardPosition cardPosition) {
        initBox(active);
        setupDropZone(active, cardPosition);
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
        setupDropZone(bench, cardPosition);
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
            double reductionPerCard = Math.max(excessWidth / numberOfCards, 30);
            cardSpacing -= reductionPerCard;
            totalCardWidth = (cardWidth * numberOfCards) + (cardSpacing * (numberOfCards - 1));
        }

        cardHand.setSpacing(cardSpacing);

        cardHand.setLayoutX((gameMat.getPrefWidth() - totalCardWidth) / 2);
        // Check if it is northhand to make sure Y is not dragged down to southhand position
        cardHand.setLayoutY(cardHand.equals(northHand)? 0 :gameMat.getPrefHeight() - 150);
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

        cardView.setOnContextMenuRequested(event ->
                contextMenu.show(cardView, event.getScreenX(), event.getScreenY()));
    }

    private void setCardPopupEffects(ImageView cardView) {
        Popup popup = new Popup();
        ImageView enlargedCardView;

        // If prize or discard, we need to get the card image instead of the face down image
        if (cardView.getParent().equals(southPrize) || cardView.getParent().equals(southPrize)) {
            enlargedCardView = createCardImageView(findCardById((String) cardView.getProperties().get("cardId")));
        } else {
            enlargedCardView = new ImageView(cardView.getImage());
        }

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
            if (node instanceof ImageView imageView) {
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
                (node).setScaleY(-1);
            }
        }
    }

    private void printMatContents(String message, ArrayList<Node> mat) {
        System.out.println(message);
        for (Node node : mat) {
            if (node instanceof Pane pane) {
                System.out.println("Pane: " + pane + ", Children: " + pane.getChildren());
            } else {
                System.out.println("Node: " + node);
            }
        }
    }

    public void updateHandUI() {
        southHand.getChildren().clear();
        for (Card card : game.getPlayerList().get(0).getHand()) {
            ImageView cardView = createCardImageView(card);
            southHand.getChildren().add(cardView);
            updateCardHandSpacing(southHand);
        }
    }
    private ImageView createCardImageView(Card card) {
        String imagePath = "/images/" + card.getImage();
        InputStream is = getClass().getResourceAsStream(imagePath);

        Image cardImage;
        if (is != null) {
            cardImage = new Image(is);
        } else {
            throw new RuntimeException("Card Null: " + card);
        }
        ImageView imageView = new ImageView(cardImage);
        addDragAndDropCapability(imageView, card);
        setCardHeightAndWidth(imageView);
        setUpContextMenuWithPopUp(imageView);
        return imageView;
    }

    private void updateSouthCardHand() {
        southHand.getChildren().clear();
        for (Card card : game.getPlayerList().get(0).getHand()) {
            ImageView cardView = createCardImageView(card);
            southHand.getChildren().add(cardView);
            updateCardHandSpacing(southHand);
        }
    }

    // Utility END =====================================================================================


}