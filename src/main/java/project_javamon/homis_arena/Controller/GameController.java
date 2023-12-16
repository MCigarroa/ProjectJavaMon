package project_javamon.homis_arena.Controller;

import javafx.animation.RotateTransition;
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
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.util.Duration;
import project_javamon.homis_arena.Game.Game;
import project_javamon.homis_arena.Game.GameState;
import project_javamon.homis_arena.Game.Player;
import project_javamon.homis_arena.Game.Pokemon.Card;
import project_javamon.homis_arena.Game.Pokemon.EnergyCard;
import project_javamon.homis_arena.Game.Pokemon.PokemonCard;
import project_javamon.homis_arena.Util.AbilityBinder;
import project_javamon.homis_arena.Util.CardPosition;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;

import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;


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
    public Button coinFlipBtn;

    Image backCard = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/cardback.jpg")));
    Image homi = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/homi-poki.jpg")));
    HBox southHand;
    HBox northHand;

    ArrayList<Node> northMat;
    ArrayList<Node> southMat;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Controller initialized");
        initMat();
        System.out.println("Mat initialized");

        bindPlayerToUI(Game.getActivePlayer());
        bindPlayerToUI(Game.getWaitingPlayer());
    }

    // Updates ==========================================================================================
    public void updateUI() {
        System.out.println("Updating UI");
        // Updates every location with cards from player
        // Very brute, much performance eater
        // Simplifies updates tho
        southHand.getChildren().clear();
        southBench.getChildren().clear();
        southActive.getChildren().clear();
        southPrize.getChildren().clear();
        southDiscard.getChildren().clear();
        //southDeck.getChildren().clear();
        northHand.getChildren().clear();
        northBench.getChildren().clear();
        northActive.getChildren().clear();
        northPrize.getChildren().clear();
        northDiscard.getChildren().clear();
        //northDeck.getChildren().clear();

        Player player1 = Game.getActivePlayer();
        Player player2 = Game.getWaitingPlayer();

        populateContainerFromList(southHand, player1.getHand());
        populateContainerFromList(southBench, player1.getBench());
        populateContainerFromList(southActive, player1.getActive());
        populateContainerFromList(southPrize, player1.getPrize());
        populateContainerFromList(southDiscard, player1.getDiscard());
        //populateContainerFromList(southDeck, player1.getDeck());
        populateContainerFromList(northHand, player2.getHand());
        populateContainerFromList(northBench, player2.getBench());
        populateContainerFromList(northActive, player2.getActive());
        populateContainerFromList(northPrize, player2.getPrize());
        populateContainerFromList(northDiscard, player2.getDiscard());
        //populateContainerFromList(northDeck, player1.getDeck());

        updateActiveSpacing(southActive);
        updateCardHandSpacing(southHand);
        updateCardHandSpacing(northHand);
        updateActiveSpacing(northActive);
        //updateHandUI(player1.getHand());
    }

    private void populateContainerFromList(Pane container, ObservableList<Card> cardList) {
        // Expensive operation but simplifies updates
        for (Card card : cardList) {
            ImageView imageView;
            // Cards that should face down get backCard assignment
            if (container.equals(southPrize) || container.equals(southDiscard) ||
                    container.equals(northPrize) || container.equals(northHand)    || container.equals(northDiscard)) {
                imageView = new ImageView(backCard);
                setCardHeightAndWidth(imageView);
                addDragAndDropCapability(imageView, card);
            } else {
                imageView = createCardImageView(card);
                setCardHeightAndWidth(imageView);
                addDragAndDropCapability(imageView, card);
            }

            if (card instanceof PokemonCard && (container.equals(southActive) || container.equals(northActive))) {
                Pane cardView = createEnergyIcons(imageView, (PokemonCard) card);
                container.getChildren().add(cardView);
            } else {
                container.getChildren().add(imageView);
            }

            setUpContextMenuWithPopUp(imageView, card);
        }
    }


    private void bindPlayerToUI(Player player) {
        // Applies listeners to any changes to the arrays, calling their respective UI updates
        player.getHand().addListener((ListChangeListener.Change<? extends Card> change) -> {
            updateHandUI(player.getHand());
        });
        player.getActive().addListener((ListChangeListener.Change<? extends Card> change) -> {
            updateActiveUI(player.getActive());
        });
        player.getPrize().addListener((ListChangeListener.Change<? extends Card> change) -> {
            updatePrizeUI(player.getPrize());
        });
        player.getDiscard().addListener((ListChangeListener.Change<? extends Card> change) -> {
            updateDiscardUI(player.getDiscard());
        });
        player.getBench().addListener((ListChangeListener.Change<? extends Card> change) -> {
            updateBenchUI(player.getBench());
        });
    }

    private void updateBenchUI(ObservableList<Card> bench) {
        southBench.getChildren().clear();
        populateContainerFromList(southBench, bench);
    }

    private void updateDiscardUI(ObservableList<Card> discard) {
        updateActiveUI(Game.getActivePlayer().getActive());
        southDiscard.getChildren().clear();
        populateContainerFromList(southDiscard, discard);
        // We don't really have a special UI effect for this
    }

    private void updatePrizeUI(ObservableList<Card> prize) {
        southPrize.getChildren().clear();
        populateContainerFromList(southPrize, prize);
        // We don't really have a special UI effect for this
    }


    private void updateActiveUI(ObservableList<Card> hand) {
        southActive.getChildren().clear();
        northActive.getChildren().clear();
        populateContainerFromList(southActive, hand);
        populateContainerFromList(northActive, Game.getWaitingPlayer().getActive());
        updateActiveSpacing(southActive);
        updateActiveSpacing(northActive);
    }

    private void updateHandUI(ObservableList<Card> hand) {
        // extremely expensive once hand has 30+ cards // fixed: still lags at 50, but much better
        southHand.getChildren().clear();
        populateContainerFromList(southHand, hand);
        updateCardHandSpacing(southHand);
        //populateContainerFromList(southActive, Game.getActivePlayer().getActive());
        //updateUI();
    }
    // Updates END=======================================================================================

    public void initMat(){
        initDeck(northDeck, CardPosition.DECK);
        initDeck(southDeck,CardPosition.DECK);
        initPrize(northPrize);
        initPrize(southPrize);
        initActive(northActive);
        initActive(southActive);
        initDiscard(northDiscard);
        initDiscard(southDiscard);
        initBench(northBench);
        initBench(southBench);

        initSouthCardHand();
        initNorthCardHand();

        northMat = new ArrayList<>(Arrays.asList(northBench, northActive, northPrize, northDiscard, northDeck, northHand));
        southMat = new ArrayList<>(Arrays.asList(southBench, southActive, southPrize, southDiscard, southDeck, southHand));

        Collections.shuffle(Game.getActivePlayer().getDeck());
        Collections.shuffle(Game.getWaitingPlayer().getDeck());
    }


    // TODO move card to hand, send to discard, , restrictionNotification,
    // TODO UI damage tracker / status,

    // Button Actions ================================================================
    @FXML
    public void onSouthDeckClicked() {
        Player currentPlayer = Game.getActivePlayer();
        currentPlayer.drawCard();
        updateHandUI(currentPlayer.getHand());
        //currentPlayer.printPlayerCards(); // debug
    }
    @FXML
    public void showHand() {
        // Just up and down on button click
        if (southHand.getLayoutY() == gameMat.getPrefHeight() - 100){
            southHand.setLayoutY(gameMat.getPrefHeight()- 300);
        } else {
            southHand.setLayoutY(gameMat.getPrefHeight()- 100);
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
        Game.changeActivePlayer();
        updateUI();
    }

    // Button Actions END ============================================================
    // Listeners =====================================================================
    private void setupDropZone(Pane pane) {
        if (pane.equals(northHand) || pane.equals(northDiscard) || pane.equals(northPrize) || pane.equals(northBench) ||
            pane.equals(northActive) || pane.equals(northDeck)) {
            return;
        }
        pane.setOnDragOver(event -> {
            if (event.getGestureSource() != pane && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
        pane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            // This should prevent card from moving to the north mat,
            // but it is just causing them to disappear
//            if (pane.getLayoutY() < gameMat.getLayoutY() / 2) {
//                return;
//            }
            if (db.hasString()) {
                // Gets ID
                String cardId = db.getString();
                moveCardToLocation(cardId, pane);

                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void moveCardToLocation(String cardId, Pane newLocation) {
        System.out.println("Moving card with ID: " + cardId + " to new location: " + newLocation);

        Card card = findCardById(cardId);
        Player currentPlayer = Game.getActivePlayer();

        // Expensive operation ===============================================================
        // I don't want to mess with it atm, it calls 5 UI updates
        // Fixed to call only 2 times :)
        if (!GameState.isMoveLegal()){

            return;
        }
        if (card instanceof EnergyCard && (newLocation.equals(southActive) || newLocation.equals(southBench))) {
            currentPlayer.getDiscard().add(card); //updateCall
            currentPlayer.getHand().remove(card); //updateCall
            // If cards are energy will send to discard and add energy to card
            PokemonCard pokemonCard = (PokemonCard) currentPlayer.getActive().get(0);
            pokemonCard.addEnergy((EnergyCard) card);
            System.out.println(pokemonCard.getEnergyBanked());
            card.setCardPositions(CardPosition.DISCARD);
        } else {
            currentPlayer.removeFromPrevious(card, determineCardPosition(newLocation)); //updateCall
            updatePlayerListWithNewCardLocation(currentPlayer, card, newLocation); //updateCall
        }
        // Expensive operation END============================================================
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


    public Card findCardById(String cardId) {
        return Game.getActivePlayer().findCardById(cardId);
    }

    private void enableDragAndDrop(ImageView cardView, Card card) {
        cardView.setOnDragDetected(mouseEvent -> {
            Dragboard dragboard = cardView.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent clipboardContent = new ClipboardContent();

            // This is only for the image being dragged
            SnapshotParameters snapshotParams = new SnapshotParameters();
            snapshotParams.setFill(Color.TRANSPARENT);
            Image dragImage = cardView.snapshot(snapshotParams, null);

            dragboard.setDragView(dragImage, mouseEvent.getX(), mouseEvent.getY());
            // ===============================

            String cardId = card.getCardID();
            clipboardContent.putString(cardId);

            dragboard.setContent(clipboardContent);
            mouseEvent.consume();
        });

        cardView.setOnDragDone(event -> {
            cardView.setOpacity(1);

            setUpContextMenuWithPopUp(cardView, card);
            updateUI();
            event.consume();
        });
    }
    // Listeners End =================================================================
    // Init Zone =====================================================================
    private void initSouthCardHand() {
        southHand = new HBox();
        southHand.setLayoutX(gameMat.getPrefWidth() /  2);
        southHand.setLayoutY(gameMat.getPrefHeight() - 100);
        gameMat.getChildren().add(southHand);
    }
    private void initNorthCardHand() {
        northHand = new HBox();
        northHand.setLayoutX(gameMat.getPrefWidth() /  3);
        northHand.setLayoutY(-100);
        gameMat.getChildren().add(northHand);
    }

    public void initDeck(VBox deck, CardPosition cardPosition) {
        Player player = Game.getActivePlayer();

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

        if (deck.getLayoutY() < gameMat.getPrefHeight() / 2) flipCard(deck, -1);
        deck.setSpacing(-200);
        deck.setBackground(Background.fill(Color.BLACK));
    }
    private void initPrize(HBox prize) {
        initBox(prize);
        prize.setSpacing(-100);
        setupDropZone(prize);
    }

    private void initActive(HBox active) {
        initBox(active);
        setupDropZone(active);
    }

    private void initDiscard(VBox discard) {
        initBox(discard);
        discard.setSpacing(-200);
        setupDropZone(discard);
    }

    private void initBox(Pane active) {
        active.getChildren().clear();
        if (active.getLayoutY() < gameMat.getPrefHeight() / 2) flipCard(active, -1);
    }

    private void initBench(HBox bench){
        bench.getChildren().clear();
        if (bench.getLayoutY() < gameMat.getPrefHeight() / 2) flipCard(bench, -1);
        bench.setSpacing(5);
        setupDropZone(bench);
    }
// Init Zone END==================================================================

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
            double reductionPerCard = Math.max(excessWidth / numberOfCards, 25);
            cardSpacing -= reductionPerCard;
            totalCardWidth = (cardWidth * numberOfCards) + (cardSpacing * (numberOfCards - 1));
        }

        cardHand.setSpacing(cardSpacing);

        cardHand.setLayoutX((gameMat.getPrefWidth() - totalCardWidth) / 2);
        // Check if it is northHand to make sure Y is not dragged down to southHand position
        cardHand.setLayoutY(cardHand.equals(northHand)? -100 :gameMat.getPrefHeight() - 150);
    }

    private void setCardHeightAndWidth(ImageView cardView){
        cardView.setPreserveRatio(true);
        cardView.setFitHeight(200);
        cardView.setFitWidth(144);
    }

    private void addDragAndDropCapability(ImageView cardView, Card card) {
        String cardId = card.getCardID();
        cardView.getProperties().put(cardId, card);
        System.out.println("Assigned cardId: " + cardId + " to card");

        enableDragAndDrop(cardView, card);
    }

    private void setUpContextMenuWithPopUp(ImageView cardView, Card card) {
        ContextMenu contextMenu = new ContextMenu();

        menuActionsGenerator(contextMenu, cardView, card);

        MenuItem viewLargeItem = new MenuItem("View Large");
        contextMenu.getItems().add(viewLargeItem);

        viewLargeItem.setOnAction(event -> setCardPopupEffects(cardView));

        cardView.setOnContextMenuRequested(event ->
                contextMenu.show(cardView, event.getScreenX(), event.getScreenY()));
    }

    private void menuActionsGenerator(ContextMenu contextMenu, ImageView cardView, Card card) {

         if ( card.getCardPosition() != CardPosition.ACTIVE && !(card instanceof PokemonCard)) {
             return;
         }
         PokemonCard pokemonCard = (PokemonCard) card;
         AbilityBinder.attackGenerator(pokemonCard);
         for (int index = 0; index < pokemonCard.getiAction().size(); index++) {
             String result = formatActionName(pokemonCard, index);
             MenuItem iAction = new MenuItem(result);
             contextMenu.getItems().add(iAction);
             int finalIndex = index;
             iAction.setOnAction(event -> {
                 pokemonCard.getiAction().get(finalIndex).TakeAction((PokemonCard) card);
                 updateUI();
             });
         }
    }


    private void setCardPopupEffects(ImageView cardView) {
        Popup popup = new Popup();
        ImageView enlargedCardView;

        // If prize or discard, we need to get the card image instead of the face-down image
        if (cardView.getParent().equals(southPrize) || cardView.getParent().equals(southPrize)) {
            enlargedCardView = createCardImageView(findCardById((String) cardView.getProperties().get("cardId")));
        } else {
            enlargedCardView = new ImageView(cardView.getImage());
        }

        // Size of Popup
        enlargedCardView.setFitWidth(cardView.getFitWidth() * 2.5);
        enlargedCardView.setFitHeight(cardView.getFitHeight() * 2.5);
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
        // Not having north caused issues in card / ability binding
        for (Node container : northMat) {
            if (container instanceof Pane) {
                ImageView found = searchContainerForCard((Pane) container, cardId);
                if (found != null) return found;
            }
        }

        return null;
    }
    private ImageView searchContainerForCard(Pane container, String cardId) {
        // helper to find card
        for (Node node : container.getChildren()) {
            if (node instanceof ImageView imageView) {
                if (cardId.equals(imageView.getProperties().get("cardId"))) {
                    return imageView;
                }
            }
        }
        return null;
    }

    private void flipCard(Pane pane, double angle) {
        // Flips cards: -1 for upside down, 0.5 for right, -0.5 for left
        for (Node node : pane.getChildren()) {
            if (node instanceof ImageView) {
                (node).setScaleY(angle);
            }
        }
    }


    private void printMatContents(String message, ArrayList<Node> mat) {
        // Used for debug
        System.out.println(message);
        for (Node node : mat) {
            if (node instanceof Pane pane) {
                System.out.println("Pane: " + pane + ", Children: " + pane.getChildren());
            } else {
                System.out.println("Node: " + node);
            }
        }
    }

    private ImageView createCardImageView(Card card) {
        InputStream is = getClass().getResourceAsStream("/images/" + card.getImage());

        Image cardImage;
        if (is != null) {
            cardImage = new Image(is);
        } else {
            throw new RuntimeException("Card Null: " + card);
        }
        ImageView imageView = new ImageView(cardImage);
        addDragAndDropCapability(imageView, card);
        setCardHeightAndWidth(imageView);
        setUpContextMenuWithPopUp(imageView, card);
        return imageView;
    }
    private static String formatActionName(PokemonCard pokemonCard, int index) {
        String str = pokemonCard.getiAction().get(index).getActionName();
        String[] words = str.split(" ");
        StringBuilder capitalizedString = new StringBuilder();
        for (String word : words) {
            String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
            capitalizedString.append(capitalizedWord).append(" ");
        }
        return capitalizedString.toString().trim();
    }

    // Utility END =====================================================================================
    // Coin Flip END====================================================================================
    @FXML
    private void coinFlip() {
        updateUI();
        // This creates a pane that allows for coin flips
        StackPane coinPane = new StackPane();
        coinPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0);-fx-alignment: center;");
        coinPane.setPrefSize(gameMat.getWidth() / 3, gameMat.getHeight() / 3);
        coinPane.setLayoutX(gameMat.getPrefWidth() / 2);
        coinPane.setLayoutY(gameMat.getPrefHeight() / 2);

        ImageView coinView = new ImageView(homi);
        setCardHeightAndWidth(coinView);
        Button flipButton = new Button("Flip Coin");
        flipButton.setTranslateY(80);
        flipButton.setOnAction(e -> {
             flipCoin(coinView, homi, backCard);
        });

        Button exitFlipButton = new Button("Exit");
        exitFlipButton.setTranslateY(100);
        exitFlipButton.setOnAction(e-> gameMat.getChildren().remove(coinPane));

        coinPane.getChildren().add(exitFlipButton);
        StackPane.setAlignment(coinPane, Pos.CENTER);

        coinPane.getChildren().add(coinView);
        StackPane.setAlignment(coinView, Pos.CENTER);

        coinPane.getChildren().add(flipButton);
        StackPane.setAlignment(flipButton, Pos.CENTER);

        gameMat.getChildren().add(coinPane);
        StackPane.setAlignment(coinPane, Pos.CENTER);
    }

    private void flipCoin(ImageView coinView, Image homi, Image backCard) {
        Random random = new Random();
        RotateTransition rt = new RotateTransition(Duration.seconds(1), coinView);
        rt.setByAngle(360 * 3); // Rotate three times
        rt.setOnFinished(e -> {
            // Randomly choose heads or tails at the end of the animation
            // Need way to send bool for game logic
            boolean result = random.nextBoolean();
            coinView.setImage(result ? homi : backCard);
        });
        rt.play();
    }
    public static void flipCoin(ImageView coinView, Image homi, Image backCard, Consumer<Boolean> callback) {
        Random random = new Random();
        RotateTransition rt = new RotateTransition(Duration.seconds(1), coinView);
        rt.setByAngle(360 * 3); // Rotate three times
        rt.setOnFinished(e -> {
            // Randomly choose heads or tails at the end of the animation
            // Need way to send bool for game logic
            boolean result = random.nextBoolean();
            coinView.setImage(result ? homi : backCard);
            callback.accept(result);
        });
        rt.play();
    }

    // Coin Flip END ====================================================================================
    // Energy Icon ======================================================================================

    public Pane createEnergyIcons(ImageView imageView, PokemonCard card) {
        // This breaks
        double padding = 10;
        //setCardHeightAndWidth(imageView);

        Pane cardView = new Pane();
        cardView.getChildren().add(imageView);

        // Add HP
        Text healthText = new Text(String.valueOf(card.getHp()));
        healthText.setStyle("-fx-font-size: 20;");
        healthText.setFill(Color.BLACK);
        // Positions the HP top left corner
        healthText.relocate(padding, padding);
        cardView.getChildren().add(healthText);


        double radius = 10;
        double initialY = padding;
        // Creates energyIcons
        for (Map.Entry<String, Integer> entry : card.getEnergyBanked().entrySet()) {
            String type = entry.getKey();
            Integer amount = entry.getValue();

            if (amount > 0) {
                StackPane energyIndicator = new StackPane();
                // Circle
                Circle energyCircle = new Circle(radius, getColorForType(type));
                energyCircle.setStroke(Color.BLACK);
                energyCircle.setStrokeWidth(2);
                // Text
                Text energyAmountText = new Text(String.valueOf(amount));
                energyAmountText.setFill(Color.BLACK);
                energyAmountText.setStyle("-fx-font-weight: bold; -fx-font-size: " + (radius * 1.5) + ";");
                    
                energyIndicator.getChildren().addAll(energyCircle, energyAmountText);

                // location
                double xPosition = imageView.getFitWidth() - (radius / 2) - (padding / 2);
                energyIndicator.relocate(xPosition, initialY);

                cardView.getChildren().add(energyIndicator);
                // creates space between energyicons
                initialY += 1 * radius + padding;
            }
        }
        return cardView;
    }

    private Color getColorForType(String type) {
        return switch (type) {
            case "fire" -> Color.RED;
            case "water" -> Color.DEEPSKYBLUE;
            case "grass" -> Color.LIGHTGREEN;
            case "colorless" -> Color.GRAY;
            case "psychic" -> Color.PURPLE;
            case "fighting" -> Color.ORANGE;
            case "darkness" -> Color.BLACK;
            case "metal" -> Color.SILVER;
            case "fairy" -> Color.PINK;
            default -> Color.YELLOW;
        };
    }
    // Energy Icon END===================================================================================
}