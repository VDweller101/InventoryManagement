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
        Part inHouse1 = new InHouse(0, "a", 1.0, 1, 1, 1, 1);
        Part inHouse2 = new InHouse(1, "as", 1.0, 1, 1, 1, 1);
        Part outsourced1 = new Outsourced(2, "asd", 1.0, 1, 1, 1, "Company1");
        Part outsourced2 = new Outsourced(3, "ads", 1.0, 1, 1, 1, "Company2");

        Inventory.addPart(inHouse1);
        Inventory.addPart(inHouse2);
        Inventory.addPart(outsourced1);
        Inventory.addPart(outsourced2);

        Product product1 = new Product(0, "a", 1.0, 1, 1, 1);
        product1.addAssociatedPart(inHouse1);
        product1.addAssociatedPart(inHouse2);
        product1.addAssociatedPart(outsourced1);


        Product product2 = new Product(1, "ad", 1.0, 1, 1, 1);
        product2.addAssociatedPart(inHouse1);
        product2.addAssociatedPart(outsourced2);

        Inventory.addProduct(product1);
        Inventory.addProduct(product2);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
