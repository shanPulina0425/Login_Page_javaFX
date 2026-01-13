package Controller;

import DB_Connection.DB_Connection;
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
        String email = signInEmailTxt.getText().trim();
        String password = signInPasswordTxt.getText();


        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Incomplete", "Please fill all fields.");
            return;
        }


        String sql = "SELECT email FROM login_details WHERE email = ? AND password = ?";

        try (Connection conn = DB_Connection.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, email);
            pst.setString(2, password);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {

                    showAlert(Alert.AlertType.INFORMATION,"", "Login successful!");
                    loginPane.setVisible(false);
                    dashBoardPane.setVisible(true);


                    signInEmailTxt.clear();
                    signInPasswordTxt.clear();
                } else {
                    showAlert(Alert.AlertType.ERROR,"", "Invalid email or password.");
                }
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR,"", "Cannot connect to database.");
            e.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }











    @FXML
    void handleLoginPageSignUp(ActionEvent event) {

        loginPane.setVisible(false);
        signUpPane.setVisible(true);

    }

    @FXML
    void handleSignUpBtn(ActionEvent event) {

        String firstName = firstNameTxt.getText().trim();
        String lastName = lastNameTxt.getText().trim();
        String email = emailTxt.getText().trim();
        String password = passwordTxt.getText();
        String rePassword = reEnterPasswordTxt.getText();


        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
                password.isEmpty() || rePassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "","Please fill all fields.");
            return;
        }


        if (!password.equals(rePassword)) {
            showAlert(Alert.AlertType.ERROR,"", "Passwords do not match!");
            reEnterPasswordTxt.clear();
            return;
        }


        if (password.length() < 6) {
            showAlert(Alert.AlertType.WARNING,"", "Password must be at least 6 characters.");
            return;
        }

        Connection conn = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;

        try {
            conn = DB_Connection.getInstance().getConnection();


            conn.setAutoCommit(false);


            String checkSql = "SELECT email FROM signup_details WHERE email = ?";
            try (PreparedStatement checkPst = conn.prepareStatement(checkSql)) {
                checkPst.setString(1, email);
                try (ResultSet rs = checkPst.executeQuery()) {
                    if (rs.next()) {
                        showAlert(Alert.AlertType.WARNING,"", "Email already registered.");
                        return;
                    }
                }
            }


            String sql1 = "INSERT INTO signup_details(first_name, last_name, email, password) VALUES(?, ?, ?, ?)";
            pst1 = conn.prepareStatement(sql1);
            pst1.setString(1, firstName);
            pst1.setString(2, lastName);
            pst1.setString(3, email);
            pst1.setString(4, password);
            pst1.executeUpdate();


            String sql2 = "INSERT INTO login_details(email, password) VALUES(?, ?)";
            pst2 = conn.prepareStatement(sql2);
            pst2.setString(1, email);
            pst2.setString(2, password);
            pst2.executeUpdate();


            conn.commit();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Cashier registered successfully!");



            firstNameTxt.clear();
            lastNameTxt.clear();
            emailTxt.clear();
            passwordTxt.clear();
            reEnterPasswordTxt.clear();


            signUpPane.setVisible(false);
            loginPane.setVisible(true);

        } catch (SQLException e) {

            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            showAlert(Alert.AlertType.ERROR,"", "Registration failed: " + e.getMessage());
            e.printStackTrace();
        } finally {

            try {
                if (pst1 != null) pst1.close();
                if (pst2 != null) pst2.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }



}
