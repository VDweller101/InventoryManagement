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
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class ModifyPartController {

    Part currentPart;
    Part modifiedPart;

    @FXML
    private RadioButton modifyPartInHouseRadio;

    @FXML
    private RadioButton modifyPartOutsourcedRadio;

    @FXML
    private ToggleGroup modifyPartToggleGroup;

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

    public void initialize()
    {
        //Check to make sure there is a part to modify before continuing.
        if (Utilities.CurrentSelectedPart == null) {
            System.out.println("Modify Part: No current part in Utilities.");
            Utilities.DisplayErrorMessage("No Part Selected", "There is no part selected from the Inventory Main Screen to modify.");
        } else {
            System.out.println("Modify Part: Initiliaizing.");
            currentPart = Utilities.CurrentSelectedPart;
            populateFields(currentPart);
        }
    }

    @FXML
    private void modifyPartToggleChanged(ActionEvent event)
    {
        RadioButton button = (RadioButton)event.getSource();
        char firstLetter = button.getText().charAt(0);
        if (firstLetter == 'O') {
            modifyPartMachineCompanyLabel.setText("Company Name");
        } else {
            modifyPartMachineCompanyLabel.setText("Machine ID");
        }
    }
    @FXML
    private void modifyPartSaveButton (ActionEvent event)throws IOException
    {
        String partCreateError = areFieldsValid();
        if (partCreateError.isBlank()) {
            modifiedPart = createPartFromFields();
            if (modifiedPart == null) {
                Utilities.DisplayErrorMessage("Part Modification Failed", "An Unknown Error Occured, Returning to Main Menu.");
                returnToMainMenu(event);
            } else {
                String errorMessage = Utilities.IsPartValid(modifiedPart);
                if (errorMessage.isBlank()) {
                    System.out.println("MParts: Updating Part#" + String.valueOf(currentPart.getId()) + " to Part#: " + String.valueOf(modifiedPart.getId()));
                    Inventory.updatePart(Inventory.getAllParts().indexOf(currentPart), modifiedPart);
                    currentPart = null;
                    modifiedPart = null;
                    returnToMainMenu(event);
                } else {
                    Utilities.DisplayErrorMessage("Part Modification Failed", errorMessage);
                }
            }
        } else {
            Utilities.DisplayErrorMessage("Part Modification Failed", partCreateError);
        }
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

    private void populateFields (Part part)
    {
        modifyPartIDTextField.setText(String.valueOf(part.getId()));
        modifyPartNameTextField.setText(part.getName());
        modifyPartPriceTextField.setText(String.valueOf(part.getPrice()));
        modifyPartStockTextField.setText(String.valueOf(part.getStock()));
        modifyPartMinTextField.setText(String.valueOf(part.getMin()));
        modifyPartMaxTextField.setText(String.valueOf(part.getMax()));
        if (part.getClass() == InHouse.class) {
            modifyPartMachineCompanyTextField.setText(String.valueOf(((InHouse) part).getMachineId()));
            modifyPartInHouseRadio.setSelected(true);
            modifyPartOutsourcedRadio.setSelected(false);
            modifyPartMachineCompanyLabel.setText("Machine ID:");
        } else {
            modifyPartMachineCompanyTextField.setText(((Outsourced)part).getCompanyName());
            modifyPartInHouseRadio.setSelected(false);
            modifyPartOutsourcedRadio.setSelected(true);
            modifyPartMachineCompanyLabel.setText("Company Name:");
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
        Utilities.CurrentSelectedPart = null;
        Utilities.CurrentSelectedProduct = null;
    }

    private Part createPartFromFields()
    {
        if (modifyPartInHouseRadio.isSelected()) {
            //int id, String name, double price, int stock, int min, int max, int machineId
            return new InHouse (
                    (int)Utilities.TryParseInt(modifyPartIDTextField.getText()),
                    modifyPartNameTextField.getText(),
                    (double)Utilities.TryParseDouble(modifyPartPriceTextField.getText()),
                    (int)Utilities.TryParseInt(modifyPartStockTextField.getText()),
                    (int)Utilities.TryParseInt(modifyPartMinTextField.getText()),
                    (int)Utilities.TryParseInt(modifyPartMaxTextField.getText()),
                    (int)Utilities.TryParseInt(modifyPartMachineCompanyTextField.getText()));
        } else {
            return new Outsourced (
                    (int)Utilities.TryParseInt(modifyPartIDTextField.getText()),
                    modifyPartNameTextField.getText(),
                    (double)Utilities.TryParseDouble(modifyPartPriceTextField.getText()),
                    (int)Utilities.TryParseInt(modifyPartStockTextField.getText()),
                    (int)Utilities.TryParseInt(modifyPartMinTextField.getText()),
                    (int)Utilities.TryParseInt(modifyPartMaxTextField.getText()),
                    modifyPartMachineCompanyTextField.getText());
        }
    }


    private String areFieldsValid()
    {
        String message = "";

        //int id, String name, double price, int stock, int min, int max, String companyName
        Integer idVal = Utilities.TryParseInt(modifyPartIDTextField.getText());
        if (idVal == null) {
            message += "ID must be a valid Integer.\n";
        }

        if (modifyPartNameTextField.getText().isBlank()) {
            message += "Name must not be blank.\n";
        }

        Double priceVal = Utilities.TryParseDouble(modifyPartPriceTextField.getText());
        if (priceVal == null) {
            message += "Price must be a valid decimal number.\n";
        }

        Integer stockVal = Utilities.TryParseInt(modifyPartStockTextField.getText());
        if (stockVal == null) {
            message += "Stock level must be a valid Integer.\n";
        }

        Integer minVal = Utilities.TryParseInt(modifyPartMinTextField.getText());
        if (minVal == null) {
            message += "Minimum Stock level must  be a valid integer.\n";
        }

        Integer maxVal = Utilities.TryParseInt(modifyPartMaxTextField.getText());
        if (maxVal == null) {
            message += "Maximum Stock level must be a valid Integer.\n";
        }
        String machineCompany = modifyPartMachineCompanyTextField.getText();
        if (modifyPartInHouseRadio.isSelected()) {
            Integer machineVal = Utilities.TryParseInt(machineCompany);
            if (machineVal == null) {
                message += "MachineID must be a valid Integer.\n";
            }
        } else {
            if (machineCompany.isBlank()) {
                message += "Company Name must not be blank.\n";
            }
        }
        if (minVal != null && maxVal != null && stockVal != null) {
            if (stockVal < minVal) {
                message += "Stock level must be higher than minimum stock level.\n";
            }
            if (stockVal > maxVal) {
                message += "Stock level must be lower than maximum stock level.\n";
            }
        }

        return message;
    }
}

