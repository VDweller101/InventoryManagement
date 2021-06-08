package ims;

import ims.model.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.text.TableView;

public class Main extends Application {

    @FXML
    public static TableView PartTable;
    @FXML
    public static TableView ProductTable;

    @Override
    public void start(Stage primaryStage) throws Exception{
        createDummyData();

        Parent root = FXMLLoader.load(getClass().getResource("view_control/Inventory.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root, 800, 400));
        primaryStage.show();
    }

    private void createDummyData()
    {
        Part screw = new InHouse(0, "Screw", 1.0, 1, 1, 1, 1);
        Part nut = new InHouse(1, "Nut", 1.0, 1, 1, 1, 2);
        Part washer = new InHouse(2, "Washer", 1.0, 1, 1, 1, 3);
        Part rod = new InHouse(3, "Rod", 1.0, 1, 1, 1, 4);
        Part rubberWheel = new Outsourced(4, "Rubber Wheel", 1.0, 1, 1, 1, "Wheels R Us");
        Part paint = new Outsourced (5, "Bicycle Paint", 1.0, 1, 1, 1, "PaintWorld");

        Inventory.addPart(screw);
        Inventory.addPart(nut);
        Inventory.addPart(washer);
        Inventory.addPart(rod);
        Inventory.addPart(rubberWheel);
        Inventory.addPart(paint);

        Product bikeWheel = new Product(0, "Bicycle Wheel", 1.0, 1, 1, 1);
        bikeWheel.addAssociatedPart(screw);
        bikeWheel.addAssociatedPart(nut);
        bikeWheel.addAssociatedPart(washer);
        bikeWheel.addAssociatedPart(rod);
        bikeWheel.addAssociatedPart(rubberWheel);


        Product bikeFrame = new Product(1, "Bicycle Frame", 1.0, 1, 1, 1);
        bikeFrame.addAssociatedPart(screw);
        bikeFrame.addAssociatedPart(nut);
        bikeFrame.addAssociatedPart(washer);
        bikeFrame.addAssociatedPart(rod);
        bikeFrame.addAssociatedPart(rubberWheel);

        Inventory.addProduct(bikeWheel);
        Inventory.addProduct(bikeFrame);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
