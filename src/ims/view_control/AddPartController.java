package ims.view_control;

import ims.model.InHouse;
import ims.model.Inventory;
import ims.model.Outsourced;
import ims.model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

import static java.lang.Integer.*;

public class AddPartController {

    /*
    ////    Radio Controls
     */
    @FXML
    private RadioButton addPartInHouseRadio;

    @FXML
    private RadioButton addPartOutsourcedRadio;

    @FXML
    private ToggleGroup addPartToggleGroup;

    /*
    ////    Attribute Fields
     */
    @FXML
    private TextField addPartIDTextField;

    @FXML
    private TextField addPartNameTextField;

    @FXML
    private TextField addPartPriceTextField;

    @FXML
    private TextField addPartStockTextField;

    @FXML
    private TextField addPartMinTextField;

    @FXML
    private TextField addPartMaxTextField;

    @FXML
    private Label addPartMachineCompanyLabel;

    @FXML
    private TextField addPartMachineCompanyTextField;

    /*
    ////    Radio Group
     */

    @FXML
    private void addPartToggleChanged(ActionEvent event)
    {
        RadioButton button = (RadioButton)event.getSource();
        char firstLetter = button.getText().charAt(0);
        if (firstLetter == 'O') {
            addPartMachineCompanyLabel.setText("Company Name");
        } else {
            addPartMachineCompanyLabel.setText("Machine ID");
        }
    }

    /*
    ////    Save/Cancel/Exit Buttons
    */
    @FXML
    private Button addPartSaveButton;

    @FXML
    private Button addPartCancelButton;

    @FXML
    private Button exitButton;

    /*
    ////    Save/Cancel/Exit Button Actions
     */

    @FXML
    private void addPartSaveButton (ActionEvent event)
    {
        Boolean type;
        if (addPartInHouseRadio.isSelected()) {
            type = true;
        } else {
            type = false;
        }
        String message = Utilities.ArePartFieldsValid(
                type,
                addPartIDTextField.getText(),
                addPartNameTextField.getText(),
                addPartPriceTextField.getText(),
                addPartStockTextField.getText(),
                addPartMinTextField.getText(),
                addPartMaxTextField.getText(),
                addPartMachineCompanyTextField.getText());
        if (message.isBlank()) {
            Part part = createPartFromFields();
            if (part != null) {
                Inventory.addPart(part);
                System.out.println("AddPartController: Created new part named: " + part.getName());
                returnToMainMenu(event);
            } else {
                Utilities.DisplayErrorMessage("Error", "Something went wrong, returning to main menu.");
                returnToMainMenu(event);
            }
        } else {
            Utilities.DisplayErrorMessage("Error with Part Creation", message);
        }
    }

    @FXML
    private void addPartCancelButton(ActionEvent event) throws IOException
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "All unsaved changes will be lost.");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setHeaderText("Cancel new part?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            returnToMainMenu(event);
        }
    }

    @FXML
    private void addPartExitButton (ActionEvent event)
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

    @FXML
    private Part createPartFromFields()
    {
        //int id, String name, double price, int stock, int min, int max, String companyName
        String errorMessage = "";
        Integer id = Utilities.TryParseInt(addPartIDTextField.getText());
        if (id == null) {
            errorMessage += "ID must be valid number and can not be blank.\n";
        }
        String name = addPartNameTextField.getText();
        Double price = Utilities.TryParseDouble(addPartPriceTextField.getText());
        if (price == null) {
            errorMessage += "Price must be valid decimal value and can not be blank.\n";
        }
        Integer stock = Utilities.TryParseInt(addPartStockTextField.getText());
        if (stock == null) {
            errorMessage += "Stock level must be a valid numerical value and can not be blank.\n";
        }
        Integer min = Utilities.TryParseInt(addPartMinTextField.getText());
        if (min == null) {
            errorMessage += "Minimun Inventory level must be a valid number and must not be blank.\n";
        }
        Integer max = Utilities.TryParseInt(addPartMaxTextField.getText());
        if (max == null) {
            errorMessage += "Maximum Inventory level must be a valid number and must not be blank.\n";
        }
        if(addPartInHouseRadio.isSelected()) {
            Integer machineID = Utilities.TryParseInt(addPartMachineCompanyTextField.getText());
            if (machineID == null) {
                errorMessage += "Machine ID must be a valid number and must not be blank.\n";
            }
            if (errorMessage.isBlank()) {
                Part part = new InHouse(id, name, price, stock, min, max, machineID);
                String message = Utilities.IsPartValid ((InHouse)part);
                if (message.isBlank()) {
                    return part;
                } else {
                    Utilities.DisplayErrorMessage("Part Creation Error", message);
                }
            } else {
                Utilities.DisplayErrorMessage("Part Creation Error", errorMessage);
            }
        } else {
            String companyName = addPartMachineCompanyTextField.getText();
            if (errorMessage.isBlank()) {
                Part part = new Outsourced(id, name, price, stock, min, max, companyName);
                String message = Utilities.IsPartValid ((Outsourced) part);
                if (message.isBlank()) {
                    return part;
                } else {
                    Utilities.DisplayErrorMessage("Part Creation Error", message);
                }
            } else {
                Utilities.DisplayErrorMessage("Part Creation Error", errorMessage);
            }
        }
        return null;
    }
}

