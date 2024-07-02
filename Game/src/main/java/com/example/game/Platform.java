package com.example.game;

import javafx.animation.TranslateTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public class Platform {

    public static final double PLATFORM_HEIGHT = 150;
    public static double platform_distance;
    public static ArrayList<Double> newrect = new ArrayList<Double>();

    private Pane root;
    Rectangle newRectangle;
    Rectangle oldRectangle;

    Rectangle redbox;

    static int count;

    public Platform(Pane root) {
        this.root = root;
    }

    public void initialize() {
        Random rand = new Random();
        int random1 = -1;
        int random2 = -2;
        while (random1 > random2) {
            random1 = rand.nextInt(11, 30);
            random2 = rand.nextInt(random1 + 10, random1 + 30);
        }
        newrect.add((double) random1);
        newrect.add((double) random2);
        oldRectangle = createPlatformRectangle(0, root.getHeight() - PLATFORM_HEIGHT, root.getWidth() * (0.1), PLATFORM_HEIGHT, Color.web("#cbd1d7"));
        root.getChildren().add(oldRectangle);
        newRectangle = createPlatformRectangle(root.getWidth() * ((double) random1 / 100), root.getHeight() - PLATFORM_HEIGHT, root.getWidth() * (((double) random2 - (double) random1) / 100), PLATFORM_HEIGHT, Color.web("#cbd1d7"));
        root.getChildren().add(newRectangle);
        platform_distance = root.getWidth() * ((newrect.get(0) / 100) - 0.1);
        System.out.println(newrect);
    }

    public void nextPlatform(StickmanGame stickmangame) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(3));
        transition.setByX(-1 * (platform_distance + oldRectangle.getWidth()));
        transition.setNode(oldRectangle);

        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(3));
        transition2.setByX(-1 * (platform_distance + newRectangle.getWidth()));
        transition2.setNode(newRectangle);

        TranslateTransition transition3 = new TranslateTransition(Duration.seconds(3));
        transition3.setByX(-1 * (platform_distance + newRectangle.getWidth()));
        transition3.setNode(stickmangame.stickman.stick);

        TranslateTransition transition4 = new TranslateTransition(Duration.seconds(3));
        transition4.setByX(-1 * (stickmangame.stickman.stickmanX - root.getWidth() * 0.1) - 60);
        transition4.setNode(stickmangame.stickman.stickman);

        transition.play();
        transition2.play();
        transition3.play();
        transition4.play();
        transition2.setOnFinished(e -> insertNew(stickmangame));
    }

    private void insertNew(StickmanGame stickmangame) {
        if (count % 2 == 0) {
            root.getChildren().remove(oldRectangle);
            Random rand = new Random();
            int rand1 = -1;
            int rand2 = -2;
            while (rand1 > rand2) {
                rand1 = rand.nextInt(11, 30);
                rand2 = rand.nextInt(rand1 + 10, rand1 + 30);
            }
            newrect.set(0, (double) rand1);
            newrect.set(1, (double) rand2);
            oldRectangle = createPlatformRectangle(
                    root.getWidth() * newrect.get(0) / 100,
                    oldRectangle.getY(),
                    root.getWidth() * (newrect.get(1) - newrect.get(0)) / 100,
                    oldRectangle.getHeight(),
                    Color.web("#cbd1d7"));
            root.getChildren().add(oldRectangle);
        } else {
            root.getChildren().remove(newRectangle);
            Random rand = new Random();
            int rand1 = -1;
            int rand2 = -2;
            while (rand1 > rand2) {
                rand1 = rand.nextInt(11, 100);
                rand2 = rand.nextInt(11, rand1 + 20);
            }
            newrect.set(0, (double) rand1);
            newrect.set(1, (double) rand2);
            newRectangle = createPlatformRectangle(
                    root.getWidth() * newrect.get(0) / 100,
                    oldRectangle.getY(),
                    root.getWidth() * (newrect.get(1) - newrect.get(0)) / 100,
                    oldRectangle.getHeight(),
                    Color.web("#cbd1d7"));
            root.getChildren().add(newRectangle);
        }

        this.count += 1;
        stickmangame.stickSpawned = false;
        stickmangame.isWalking = false;
        platform_distance = root.getWidth() * ((newrect.get(0) / 100) - 0.1);
        stickmangame.stickman.resetPosition(stickmangame.platform, stickmangame);
    }

    public double getDistance() {
        return platform_distance;
    }

    private Rectangle createPlatformRectangle(double x, double y, double width, double height, Color color) {
        Rectangle rectangle = new Rectangle(x, y, width, height);
        rectangle.setFill(color);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(5);
        return rectangle;
    }
}
