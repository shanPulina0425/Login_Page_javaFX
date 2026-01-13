package Controller;

public class test {

    @FXML
    void handleSignUpBtn(ActionEvent event) {

        String firstName = firstNameTxt.getText().trim();
        String lastName = lastNameTxt.getText().trim();
        String email = emailTxt.getText().trim();
        String password = passwordTxt.getText();
        String rePassword = reEnterPasswordTxt.getText();


        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
                password.isEmpty() || rePassword.isEmpty()) {
            showAlert("Warning", "Please fill all fields.");
            return;
        }


        if (!password.equals(rePassword)) {
            showAlert("Error", "Passwords do not match!");
            reEnterPasswordTxt.clear();
            return;
        }


        if (password.length() < 6) {
            showAlert("Warning", "Password must be at least 6 characters.");
            return;
        }

        Connection conn = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;

        try {
            conn = DBConnection.getInstance().getConnection();


            conn.setAutoCommit(false);

            // First check if email already exists
            String checkSql = "SELECT email FROM signup_details WHERE email = ?";
            try (PreparedStatement checkPst = conn.prepareStatement(checkSql)) {
                checkPst.setString(1, email);
                try (ResultSet rs = checkPst.executeQuery()) {
                    if (rs.next()) {
                        showAlert("Warning", "Email already registered.");
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

            showAlert("Success", "Registration successful!");


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
            showAlert("Database Error", "Registration failed: " + e.getMessage());
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
}
