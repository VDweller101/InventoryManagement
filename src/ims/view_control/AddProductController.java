package ims.view_control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class AddProductController {


    @FXML
    private TextField addProductIDTextField;

    @FXML
    private TextField addProductNameTextField;

    @FXML
    private TextField addProductInventoryTextField;

    @FXML
    private TextField addProductPriceTextField;

    @FXML
    private TextField addProductMinTextField;

    @FXML
    private TextField addProductMaxTextField;

    @FXML
    private Button addProductSaveButton;

    @FXML
    private Button addProductCancelButton;

    @FXML
    private Button addProductExitButton;

    @FXML
    private TextField addProductAddPartTextField;

    @FXML
    private TableView<?> addProductAddPartTableView;

    @FXML
    private TableColumn<?, ?> addProductAddPartIDColumn;

    @FXML
    private TableColumn<?, ?> addProductAddPartNameColumn;

    @FXML
    private TableColumn<?, ?> addProductAddPartInventoryColumn;

    @FXML
    private TableColumn<?, ?> addProductAddPartPriceColumn;

    @FXML
    private Button addProductAddPartAddButton;

    @FXML
    private TableView<?> addProductAssociatedPartTableView;

    @FXML
    private TableColumn<?, ?> addProductAssociatedPartIDColumn;

    @FXML
    private TableColumn<?, ?> addProductAssociatedPartNameColumn;

    @FXML
    private TableColumn<?, ?> addProductAssociatedPartInventoryColumn;

    @FXML
    private TableColumn<?, ?> addProductAssociatedPartPriceColumn;

    @FXML
    private Button addProductAssociatedPartRemoveButton;

    @FXML
    private void addProductExitButton(ActionEvent event)
    {
        Utilities.ExitApplication(event);
    }

    @FXML
    private void addProductCancelButton(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "All unsaved changes will be lost.");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setHeaderText("Cancel new product creation?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            returnToMainMenu(event);
        }
    }

    private void returnToMainMenu(ActionEvent event)
    {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("Inventory.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Inventory");
        stage.setScene(new Scene(root, 800, 400));
        stage.show();
    }
}
