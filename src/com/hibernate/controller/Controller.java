package com.hibernate.controller;

import animatefx.animation.AnimationFX;
import animatefx.animation.ZoomOut;
import com.hibernate.model.Person;
import com.hibernate.repository.PersonRepository;
import com.hibernate.service.PersonService;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;

import java.util.List;
import java.util.Optional;

public class Controller {
    @FXML
    private AnchorPane anchorpane;

    @FXML
    private TableView<Person> table;

    @FXML
    private TableColumn<Person, Integer> id;

    @FXML
    private TableColumn<Person, String> firstName;

    @FXML
    private TableColumn<Person, String> lastName;

    private Stage primaryStage;
    private ChangeListener<? super Boolean> minimizeAnimation;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public void setMinimizeAnimation(javafx.beans.value.ChangeListener<? super Boolean> minimizeAnimation) {
        this.minimizeAnimation = minimizeAnimation;
    }

    private PersonService personService;
    public Controller(){
        this.personService = new PersonRepository();
    }

    public void Onload(){
        //Auto load data
        getAllData();
    }
    private ObservableList<Person> person = FXCollections.observableArrayList();

    public void getAllData(){
        List<Person>personList = personService.getPerson();

        person.addAll(personList);
        id.setCellValueFactory(new PropertyValueFactory <> ("id"));
        firstName.setCellValueFactory(new PropertyValueFactory <> ("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory <> ("lastName"));
        table.setItems(person);

    }


    public void exit(MouseEvent mouseEvent) {
        Platform.exit();
    }

    public void minimize(MouseEvent mouseEvent) {
        AnimationFX fx = new ZoomOut(anchorpane);
        fx.setSpeed(1.75D);
        fx.setResetOnFinished(true);
        primaryStage = (Stage) anchorpane.getScene().getWindow();
        fx.setOnFinished(actionEvent -> primaryStage.setIconified(true));
        fx.play();
    }

    public void saveDialog(){
        primaryStage.focusedProperty().removeListener(minimizeAnimation);
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Save Dialog");
        dialog.setHeaderText("Please Save your details");
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(primaryStage);

        // Set the button types.
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        //panel
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        //textfield
        TextField firstName = new TextField();
        TextField lastName = new TextField();

        //for width and height childrens
        grid.add(new Label("First Name:"), 0, 1);
        grid.add(firstName, 1, 1);

        grid.add(new Label("Last Name:"), 0, 2);
        grid.add(lastName, 1, 2);

        // Enable/Disable save button
        Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(true);

        firstName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!firstName.getText().equals("")&&!lastName.getText().equals("") ) {
                saveButton.setDisable(false);
            }
        });
        firstName.setOnKeyTyped(event -> {
            if (firstName.getText().isEmpty()) {
                saveButton.setDisable(true);
            }
        });
        lastName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!firstName.getText().equals("")&&!lastName.getText().equals("") ) {
                saveButton.setDisable(false);
            }
            else if (lastName.getText().isEmpty()){
                saveButton.setDisable(false);
            }
        });
        lastName.setOnKeyTyped(event -> {
            if (lastName.getText().isEmpty()) {
                saveButton.setDisable(true);
            }
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> firstName.requestFocus());

        // Convert the result to a buttons-pair when the save button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Pair<>(firstName.getText(), lastName.getText());

            }
            return null;
        });
        //if click result saveButton
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(updateProducts -> {

           //method here

            System.out.println("save working");
            Person person = new Person();
            person.setFirstName(firstName.getText() );
            person.setLastName(lastName.getText() );

            personService.savePerson(person);

            //refreshTable
            refreshTable();
        });
        primaryStage.focusedProperty().addListener(minimizeAnimation);
    } //End of Statement

    public void editButton() {
        TableColumn<Person, Void> colBtn = new TableColumn("Update");
        colBtn.setStyle("-fx-background-color:#585858;-fx-text-background-color:white;");

        Callback<TableColumn<Person, Void>, TableCell<Person, Void>>
                cellFactory = new Callback<TableColumn<Person, Void>, TableCell<Person, Void>>() {
            @Override
            public TableCell<Person, Void> call(final TableColumn<Person, Void> param) {
                final TableCell<Person, Void> cell = new TableCell<Person, Void>() {
                    private void handle(ActionEvent event) {
                        //get value index if clicked button update
                        Person personModel = getTableView().getItems().get(getIndex());
                        updateDialog(personModel);
                    }
                    private Button edit= new Button("Edit");
                    {
                        //stylesheet
                        edit.setStyle("-fx-background-color:#212F3D;-fx-background-radius:1em;" +
                                "-fx-text-fill: white;" );
                        edit.setCursor(Cursor.HAND);



                        //ACTION EVENT
                        edit.setOnAction(this::handle);
                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(edit);
                        }
                    }
                };
                return cell;
            }
        };
        //Executed
        colBtn.setCellFactory(cellFactory);
        table.getColumns().add(colBtn);
    }
    public void updateDialog(Person personModel){
        primaryStage.focusedProperty().removeListener(minimizeAnimation);
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Update Dialog");
        dialog.setHeaderText("Please Update your details");
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(primaryStage);

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        //panel
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        //textfield
        TextField ID = new TextField();
        ID.setText(Integer.toString(personModel.getId()) );
        ID.setDisable(true);

        TextField firstName = new TextField();
        firstName.setText(personModel.getFirstName());

        TextField lastName = new TextField();
        lastName.setText(personModel.getLastName());

        //for width and height childrens
        grid.add(new Label("ID:"), 0, 0);
        grid.add(ID, 1, 0);

        grid.add(new Label("First Name:"), 0, 1);
        grid.add(firstName, 1, 1);

        grid.add(new Label("Last Name:"), 0, 2);
        grid.add(lastName, 1, 2);


        // Enable/Disable update button
        Node updateButton = dialog.getDialogPane().lookupButton(updateButtonType);
        updateButton.setDisable(true);

        firstName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!firstName.getText().equals("")&&!lastName.getText().equals("") ) {
                updateButton.setDisable(false);
            }
        });
        firstName.setOnKeyTyped(event -> {
            if (firstName.getText().isEmpty()) {
                updateButton.setDisable(true);
            }
        }); //if empty
        lastName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!firstName.getText().equals("")&&!lastName.getText().equals("") ) {
                updateButton.setDisable(false);
            }
            else if (lastName.getText().isEmpty()){
                updateButton.setDisable(false);
            }
        });
        lastName.setOnKeyTyped(event -> {
            if (lastName.getText().isEmpty()) {
                updateButton.setDisable(true);
            }
        });


        dialog.getDialogPane().setContent(grid);


        Platform.runLater(() -> firstName.requestFocus());


        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                return new Pair<>(firstName.getText(), lastName.getText());

            }
            return null;
        });
        //if click result updateButton
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(updateProducts -> {

            Person person = new Person();

            person.setId(Integer.parseInt(ID.getText()) );
            person.setFirstName(firstName.getText());
            person.setLastName(lastName.getText());

            personService.updatePerson(person);

            //Refresh Table

            refreshTable();

        });
        primaryStage.focusedProperty().addListener(minimizeAnimation);
    } //End of statement

    public void add(ActionEvent actionEvent) {
        saveDialog();
    }
    public void rightClickDelete(){
        table.setOnMouseClicked(event -> {

            primaryStage.focusedProperty().removeListener(minimizeAnimation);
            if(event.getButton()== MouseButton.SECONDARY){

                System.out.println("secondary working");
                Integer getID = table.getSelectionModel().getSelectedItem().getId();
                String getFirstName = table.getSelectionModel().getSelectedItem().getFirstName();
                String getLastName = table.getSelectionModel().getSelectedItem().getLastName();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Are you sure you want to delete selected?: ");
                alert.setContentText("ID: "+getID+"\nFirst Name: "+getFirstName+"\nLast Name: "+getLastName );
                alert.initModality(Modality.WINDOW_MODAL);
                alert.initOwner(primaryStage);
                //variable if ok and cancel
                Optional<ButtonType> action = alert.showAndWait();

                if(action.get() == ButtonType.OK)
                {

                Person person = new Person();
                person.setId(getID);
                person.setFirstName(getFirstName);
                person.setLastName(getLastName);


                personService.deletePerson(person);

                System.out.println("okay sucecss");

                    //onload
                    refreshTable();

                }
                else if(action.get() == ButtonType.CANCEL)
                {
                    System.out.println("cancel");
                }
            }
            primaryStage.focusedProperty().addListener(minimizeAnimation);
        });

    } //End of Statement

    public void refreshTable(){
        List<Person>personList = personService.getPerson();


        id.setCellValueFactory(new PropertyValueFactory <> ("id"));
        firstName.setCellValueFactory(new PropertyValueFactory <> ("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory <> ("lastName"));
        person.setAll(personList);

    }
}
