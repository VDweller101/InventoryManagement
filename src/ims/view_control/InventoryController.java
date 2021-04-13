package ims.view_control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class InventoryController {

    /*
        ====Parts Pane====
     */
    @FXML
    private Pane partsPane;

    @FXML
    private TableView<?> partsTableView;

    @FXML
    private TableColumn<?, ?> partsIDColumn;

    @FXML
    private TableColumn<?, ?> partsNameColumn;

    @FXML
    private TableColumn<?, ?> partsInventoryColumn;

    @FXML
    private TableColumn<?, ?> partsPriceColumn;

    @FXML
    private Button partsDeleteButton;

    @FXML
    private Button partsModifyButton;

    @FXML
    private Button partsAddButton;

    @FXML
    private Label partsLabel;

    @FXML
    private TextField partsSearchField;


    /*
        ====Products Pane====
     */

    @FXML
    private Pane productsPane;

    @FXML
    private TableView<?> productsTableView;

    @FXML
    private TableColumn<?, ?> productsIdColumn;

    @FXML
    private TableColumn<?, ?> productsNameColumn;

    @FXML
    private TableColumn<?, ?> productsInventoryColumn;

    @FXML
    private TableColumn<?, ?> productsPriceColumn;

    @FXML
    private Button productsDeleteButton;

    @FXML
    private Button productsModifyButton;

    @FXML
    private Button productsAddButton;

    @FXML
    private TextField productsSearchField;

    @FXML
    private Button exitButton;



    /*
    ////    Parts Pane Actions
    */
    @FXML
    private void partsAddButton(ActionEvent event) throws IOException
    {
        switchScene(0, event);
    }

    @FXML
    private void partsModifyButton(ActionEvent event) throws IOException {
        switchScene(1, event);
        //TODO Get currently selected part to modify
    }

    // Check if user actually wants to quit.
    @FXML
    private void inventoryExitButton(ActionEvent event)
    {
        Utilities.ExitApplication(event);
    }

    @FXML
    public void switchScene(int sceneIndex, ActionEvent event) throws IOException {
        //Scene Index: 0 = Add part | 1 = Modify part | 2 = Add product | 3 = Modify Product | Default = This (Inventory)

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent root = null;
        switch (sceneIndex)
        {
            case 0:
                try {
                    root = FXMLLoader.load(getClass().getResource("AddPart.fxml"));
                } catch (IOException e){
                    e.printStackTrace();
                }
                stage.setTitle("Add Part");
                stage.setScene(new Scene(root, 500, 600));
                break;
            case 1:
                try {
                    root = FXMLLoader.load(getClass().getResource("ModifyPart.fxml"));
                } catch (IOException e){
                    e.printStackTrace();
                }
                stage.setTitle("Modify Part");
                stage.setScene(new Scene(root, 500, 600));
                break;
            case 2:
                try {
                    root = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
                } catch (IOException e){
                    e.printStackTrace();
                }
                stage.setTitle("Add Product");
                stage.setScene(new Scene(root, 500, 600));
                break;
            case 3:
                try {
                    root = FXMLLoader.load(getClass().getResource("ModifyProduct.fxml"));
                } catch (IOException e){
                    e.printStackTrace();
                }
                stage.setTitle("Modify Product");
                stage.setScene(new Scene(root, 500, 600));
                break;
            default:
                System.out.println("Call for unknown scene index" + sceneIndex);
                break;
        }
        stage.show();
    }

}
