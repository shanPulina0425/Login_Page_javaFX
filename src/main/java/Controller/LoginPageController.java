package Controller;

import DB_Connection.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
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






    }

    @FXML
    void handleLoginPageSignUp(ActionEvent event) {

        loginPane.setVisible(false);
        signUpPane.setVisible(true);

    }

    @FXML
    void handleSignUpBtn(ActionEvent event) {

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

            System.out.println("");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
