package app;

import app.config.ViewConfig.FXMLResolver;
import app.service.authService.SessionService;
import app.service.subService.QRCodeService;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * JavaFX App
 */
public class AppAdmin extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The starter method in JavaFx application
     *
     * @param stage the primary stage for this application, onto which
     *              the application scene can be set.
     *              Applications may create other stages, if needed, but they will
     *              not be
     *              primary stages.
     */
    @Override
    public void start(Stage stage) {
        stage.initStyle(StageStyle.UNDECORATED);
//        FXMLResolver.getInstance().setUp("/view/admin", stage, 1100, 650);
//        FXMLResolver.getInstance().renderScene("homeTab/home_tab");

        QRCodeService qrCodeService = new QRCodeService();
        qrCodeService.createQRImage("https://hentaivn.mom/the-loai/housewife/page/2/?m_orderby=alphabet", "QRcode");
    }
}
