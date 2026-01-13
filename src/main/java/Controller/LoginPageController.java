package Controller;

import DB_Connection.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;



public class LoginPageController implements Initializable {

    @FXML
    private Button backBtn;

    @FXML
    private StackPane dashBoardPane;

    @FXML
    private TextField emailTxt;

    @FXML
    private TextField firstNameTxt;

    @FXML
    private TextField lastNameTxt;

    @FXML
    private Button loginPageSignUpBtn;

    @FXML
    private StackPane loginPane;

    @FXML
    private TextField passwordTxt;

    @FXML
    private TextField reEnterPasswordTxt;

    @FXML
    private Button signInBtn;

    @FXML
    private TextField signInEmailTxt;

    @FXML
    private TextField signInPasswordTxt;

    @FXML
    private Button signUpBtn;

    @FXML
    private StackPane signUpPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loginPane.setVisible(true);
        signUpPane.setVisible(false);
        dashBoardPane.setVisible(false);

    }

    @FXML
    void handleBackBtn(ActionEvent event) {

        signUpPane.setVisible(false);
        loginPane.setVisible(true);

    }


    @FXML
    void handleSignIn(ActionEvent event) {

        if (signInEmailTxt == null || signInPasswordTxt == null) {
            showAlert(Alert.AlertType.ERROR, "Initialization Error", "Sign-in controls are not initialized.");
            return;
        }

        String email = signInEmailTxt.getText() == null ? "" : signInEmailTxt.getText().trim();
        String password = signInPasswordTxt.getText() == null ? "" : signInPasswordTxt.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Incomplete", "Please fill all fields.");
            return;
        }

        String sql = "SELECT password FROM login_details WHERE email = ?";

        try (Connection connection = DBConnection.getInstance().getConnection()) {
            if (connection == null) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to obtain database connection.");
                return;
            }

            try (PreparedStatement pst = connection.prepareStatement(sql)) {
                pst.setString(1, email);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        String dbPassword = rs.getString("password");
                        if (dbPassword != null && dbPassword.equals(password)) {
                            showAlert(Alert.AlertType.INFORMATION, "Success", "Sign in successful!");
                            loginPane.setVisible(false);
                            dashBoardPane.setVisible(true);
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password.");
                        }
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Not Found", "User not found.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred: " + e.getMessage());
        }
    }








    @FXML
    void handleLoginPageSignUp(ActionEvent event) {

        loginPane.setVisible(false);
        signUpPane.setVisible(true);

    }

    @FXML
    void handleSignUpBtn(ActionEvent event) {


            if (firstNameTxt.getText().isEmpty() || emailTxt.getText().isEmpty()||passwordTxt.getText().isEmpty()||reEnterPasswordTxt.getText().isEmpty()||lastNameTxt.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Incomplete", "Please fill all fields.");
                return;
            }




        try {
            Connection con = DBConnection.getInstance().getConnection();

            String sql1 = "INSERT INTO signup_details(name, price) VALUES(?, ?, ?, ?)";
            String sql2 ="INSERT INTO login_details VALUES(?,?)";

            PreparedStatement pst1 = con.prepareStatement(sql1);
            PreparedStatement pst2 = con.prepareStatement(sql2);

            pst1.setString(1,firstNameTxt.getText());
            pst1.setString(2,lastNameTxt.getText());
            pst1.setString(3,emailTxt.getText());
            pst1.setString(4,passwordTxt.getText());

            pst2.setString(1,emailTxt.getText());
            pst2.setString(2,passwordTxt.getText());

            pst1.executeUpdate();
            pst2.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Sign up successfully!");



        } catch (Exception e) {
            e.printStackTrace();
        }




        firstNameTxt.clear();
        lastNameTxt.clear();
        emailTxt.clear();
        passwordTxt.clear();
        reEnterPasswordTxt.clear();


    }



    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }



}
