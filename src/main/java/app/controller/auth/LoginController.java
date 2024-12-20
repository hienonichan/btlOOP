package app.controller.auth;

import app.config.SetUpApp;
import app.config.ViewConfig.FXMLResolver;
import app.controller.helper.ShowAlert;
import app.exception.auth.DuplicateException;
import app.exception.auth.PasswordException;
import app.repository.UserRepository;
import app.service.authService.AuthenticationService;
import app.service.authService.SessionService;
import app.service.mainService.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    Button signupRedirectButton;

    @FXML
    Button loginRedirectButton;

    @FXML
    Button loginSubmitButton;

    @FXML
    TextField usernameField;

    @FXML
    PasswordField passwordField;

    @FXML
    Button forgetPasswordButton;

    ShowAlert showAlert;

    AuthenticationService authService;

    SessionService sessionService;

    public void initialize() {
        showAlert = new ShowAlert();
        authService = new AuthenticationService(new SessionService(), new UserService(new UserRepository()));
        sessionService = new SessionService();
    }

    @FXML
    private void handleButtonAction(ActionEvent e) {
        if (e.getSource() == loginSubmitButton) {
            submitLogin();
        } else if (e.getSource() == signupRedirectButton) {
            FXMLResolver.getInstance().renderScene("auth/register");
        } else if (e.getSource() == loginRedirectButton) {
            FXMLResolver.getInstance().renderScene("auth/login");
        }
    }

    @FXML
    private void handleForgetPasswordButton() {
        FXMLResolver.getInstance().renderScene("auth/forgetPassword");
    }

    private void submitLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert.showAlert("Username and password cannot be empty!", "error");
            return;
        }
        // Kiểm tra tính hợp lệ của tài khoản
        try {
            authService.verifyLogin(username, password);
            SetUpApp.setUpApp();
        } catch (DuplicateException | PasswordException e) {
            showAlert.showAlert(e.getMessage(), "error");
        }
    }
}
