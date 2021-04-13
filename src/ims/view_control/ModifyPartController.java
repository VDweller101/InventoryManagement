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

public class ModifyPartController {

    @FXML
    private RadioButton modifyPartInHouseRadio;

    @FXML
    private RadioButton modifyPartOutsourcedRadio;

    @FXML
    private TextField modifyPartIDTextField;

    @FXML
    private TextField modifyPartNameTextField;

    @FXML
    private TextField modifyPartPriceTextField;

    @FXML
    private TextField modifyPartStockTextField;

    @FXML
    private TextField modifyPartMinTextField;

    @FXML
    private TextField modifyPartMaxTextField;

    @FXML
    private Label modifyPartMachineCompanyLabel;

    @FXML
    private TextField modifyPartMachineCompanyTextField;

    @FXML
    private Button modifyPartSaveButton;

    @FXML
    private Button modifyPartCancelButton;

    @FXML
    private Button modifyPartExitButton;

    @FXML
    private void modifyPartSaveButton (ActionEvent event)throws IOException
    {
        //TODO 1. Check if the modifications are valid
        //     2. Save the modified part
        returnToMainMenu(event);
    }
    @FXML
    private void modifyPartCancelButton (ActionEvent event)throws IOException
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "All unsaved changes will be lost.");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setHeaderText("Cancel part modification?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            returnToMainMenu(event);
        }
    }

    @FXML
    private void modifyPartExitButton (ActionEvent event)
    {
        Utilities.ExitApplication(event);
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

