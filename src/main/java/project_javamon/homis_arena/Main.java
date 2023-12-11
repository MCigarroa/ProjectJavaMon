package project_javamon.homis_arena;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import project_javamon.homis_arena.Game.GameState;
import project_javamon.homis_arena.Game.Game;
import java.io.File;

public class Main extends Application {
    public static GameState gameState;
    public static Game game;


    public static void main(String[] args) { launch(); }

    final int X_RES = 1600;
    final int Y_RES = 900;
    final String TITLE = "Project JavaMon: Homi’s Arena";

    @Override
    public void start(Stage stage) {
        gameState = new GameState();
        game = new Game();

        Image image = new Image(getClass().getResourceAsStream("/images/homi-poki.jpg"));

        String musicFile = "src\\main\\resources\\music\\1-01. Opening.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0.2);
        mediaPlayer.play();

        stage.getIcons().add(image);
        Scene startMenuScene = createMainScene(stage);
        stage.setScene(startMenuScene);
        stage.setTitle(TITLE);
        stage.show();
    }

    // Scene creation ==============================================
    private Scene createStartScene(Stage stage) {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/project_javamon/homis_arena/GameMat.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), X_RES, Y_RES);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return scene;
    }
    private Scene createMainScene(Stage stage){
        Pane root = new Pane();
        Image image = new Image(getClass().getResourceAsStream("/images/homi-poki.jpg"));
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        root.setBackground(new Background(backgroundImage));

        Label titleLabel = new Label(TITLE);
        titleLabel.setStyle("-fx-font-size: 40; -fx-font-weight: bold;");

        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> {
            Scene scene = createStartScene(stage);
            stage.setScene(scene);
        });
        Button importDecksButton = new Button("Import Decks");
        importDecksButton.setOnAction(e -> {
            stage.setScene(createImportDeckScene(stage));
        });

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> System.exit(0));

        VBox menuLayout = new VBox(20);
        menuLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        menuLayout.getChildren().addAll(titleLabel, startButton, importDecksButton, exitButton);
        // TODO Will need to bind properties to auto align #source Chpt 14ish?
        menuLayout.setAlignment(Pos.CENTER_LEFT);

        root.getChildren().add(menuLayout);
        return new Scene(root, X_RES, Y_RES);
    }
    private Scene createImportDeckScene(Stage stage){
        StackPane root = new StackPane();

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            stage.setScene(createMainScene(stage));
        });

        root.getChildren().add(backButton);

        return new Scene(root, X_RES,Y_RES);
    }
    // Scene creation END =========================================

}