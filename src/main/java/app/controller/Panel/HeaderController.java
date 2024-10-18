package app.controller.Panel;

import app.config.DbConfig;
import app.config.ViewConfig.FXMLResolver;
import app.controller.BaseController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HeaderController implements BaseController {
    @FXML
    private Button closeProgramButton;

    @FXML
    private Button minimizeProgramButton;

    @FXML
    private void handleButtonAction(ActionEvent e) {
        if (e.getSource() == closeProgramButton) {
            System.out.println("click button close program");
            DbConfig.getInstance().close();
            Platform.exit();
        } else if (e.getSource() == minimizeProgramButton) {
            System.out.println("click button minimize program");
        }
    }

    @Override
    public void initialize() {

    }

}
