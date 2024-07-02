package com.example.game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StickmanGame extends Application {

    private static final double PLATFORM_HEIGHT = 150;
    private static double platform_distance;

    AnchorPane root;
    Stickman stickman;
    Platform platform;

    int status=1;
    private Timeline growingTimeline;

    int score = 0;

    private Timeline walkingTimeline;

    Label scorelabel;

    boolean stickSpawned = false;
    boolean isWalking = false;

    boolean growingInProgress;

    Scene scene;

    Scene endscene;

    AnchorPane root2;

    Button revive;
    Button ng;
    Stage stage;
    @Override
    public void start(Stage primaryStage) {
        stage=primaryStage;
        root2=new AnchorPane();
        revive=new Button("Revive");
        ng=new Button("New Game");
        Image backgroundimage=new Image("D:\\ap_project\\ap_project\\demo19\\src\\main\\resources\\com\\example\\demo19\\photu.png");
        BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false, false);
        BackgroundImage bi = new BackgroundImage(backgroundimage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
        Background background = new Background(bi);
        root2.getChildren().add(revive);
        root2.getChildren().add(ng);
        root2.setBackground(background);
        endscene=new Scene(root2,800,600);
        revive.translateXProperty().bind(endscene.widthProperty().divide(2).subtract(revive.widthProperty().divide(2)));
        revive.translateYProperty().bind(endscene.heightProperty().divide(2).subtract(revive.heightProperty().divide(2)));
        ng.translateXProperty().bind(endscene.widthProperty().divide(2).subtract(ng.widthProperty().divide(2)));
        ng.translateYProperty().bind(endscene.heightProperty().divide(2).add(ng.heightProperty()));
        root = new AnchorPane();
        root.setPrefSize(800,600);
        root.setBackground(background);
        scene = new Scene(root, 800, 600);
        Image icon = new Image("D:\\ap_project\\ap_project\\demo19\\src\\main\\resources\\com\\example\\demo19\\561_longer-removebg-preview.png");
        stage.getIcons().add(icon);
        scene.getRoot().requestFocus();
        scorelabel = new Label("Score: 0");
        scorelabel.setStyle(
                "-fx-font-size: 24px; " +
                        "-fx-font-family: 'Arial'; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: white;"
        );
        scorelabel.setLayoutY(20); // Adjust the vertical position
        scorelabel.setLayoutX(root.getWidth() - 150); // Adjust the horizontal position
        root.getChildren().add(scorelabel);

        initializePlatform();
        initializeStickman();
        stickman.setStage(primaryStage);

        Button button = new Button("Start Game");
        button.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #3498db, #2980b9); " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 18px; " +
                        "-fx-font-family: 'Arial'; " +
                        "-fx-padding: 15px 25px; " +
                        "-fx-border-radius: 30px; " +
                        /*"-fx-border-color: derive(#2980b9, -30%); " +*/
                        "-fx-border-width: 3px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 3); " +
                        "-fx-cursor: hand;"
        );


        // Add a scaling animation on hover
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), button);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);

        button.setOnMouseEntered(event -> scaleTransition.playFromStart());
        button.setOnMouseExited(event -> scaleTransition.setToX(1.0));
        Pane root1=new Pane();
        root1.setBackground(background);
        Scene scene1 = new Scene(root1, 800, 600);
        button.translateXProperty().bind(scene.widthProperty().divide(2).subtract(button.widthProperty().divide(2)));
        button.translateYProperty().bind(scene.heightProperty().divide(2).subtract(button.heightProperty().divide(2)));
        root1.getChildren().add(button);

        button.setOnAction(e -> {
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            root.requestFocus();
            startGameLoop();
        });

        primaryStage.setTitle("Stickman Game");
        primaryStage.setScene(scene1);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    void initializePlatform() {
        if (platform==null){
            platform = new Platform(root);
            platform.initialize();
            platform_distance = platform.getDistance();
        }
        else{
            platform.initialize();
            platform_distance = platform.getDistance();
        }
    }

    void initializeStickman() {
        stickman = new Stickman(root);
        stickman.initialize();
    }

    void startGameLoop() {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE && !stickSpawned && !growingInProgress) {
                startGrowingStick();
                stickSpawned = true;
                growingInProgress = false;
            }
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.SPACE && !growingInProgress) {
                growingInProgress=true;
                stopGrowingStick(platform);
            }
            startGameLoop();
        });

    }
    private void startGrowingStick() {
        stickman.startGrowing();
        growingTimeline = new Timeline(
                new KeyFrame(Duration.millis(10), e -> growStick())
        );
        growingTimeline.setCycleCount(Animation.INDEFINITE);
        growingTimeline.play();
    }

    private void growStick() {
        stickman.grow();
    }

    private void stopGrowingStick(Platform platform) {
        growingTimeline.pause();
        stickman.stopGrowing(platform, this);
        stickman.startWalking(platform, this);
        isWalking = true;
    }

    public static void main(String[] args) {
        launch(args);
    }
}