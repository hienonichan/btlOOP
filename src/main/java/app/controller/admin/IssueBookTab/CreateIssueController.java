package app.controller.admin.IssueBookTab;

import java.io.File;

import app.config.ViewConfig.FXMLResolver;
import app.controller.admin.BookLoanTab.MainBookLoanController;
import app.domain.BorrowReport;
import app.service.subService.GMailer;
import app.service.subService.PdfExportService;

public class CreateIssueController {
    final MainIssueController mainIssueCtrl;

    public CreateIssueController(MainIssueController mainIssueCtrl) {
        this.mainIssueCtrl = mainIssueCtrl;
    }

    void init() {
        mainIssueCtrl.issueBookButton.setOnAction(e -> createIssue());
    }

    private void createIssue() {
        BorrowReport data = mainIssueCtrl.getData();

        if (data == null) {
            return;
        }

        if (mainIssueCtrl.bookInfo.getBookRemaining() <= 0) {
            mainIssueCtrl.showAlert.showAlert("The number of books left is not enough!", "error");
            return;
        }

        if (mainIssueCtrl.reportService.handleSave(data)) {
            String currentPath = "admin/issueBookTab/issuebook_tab";
            FXMLResolver resolver = FXMLResolver.getInstance();
            resolver.renderScene("admin/bookLoanTab/bookloan_tab");

            mainIssueCtrl.showAlert.showAlert("Create success new borrow report!", "success");

            MainBookLoanController bookLoanController = resolver.getLoader().getController();
            bookLoanController.renderData(data, currentPath);

            sendMail(bookLoanController);
        } else {
            mainIssueCtrl.showAlert.showAlert("Create fail new borrow report !", "error");
        }

    }

    void sendMail(MainBookLoanController bookLoanController) {
        File tempPdfFile = PdfExportService.exportPaneToPdf(bookLoanController.getPaneData());

        String email = mainIssueCtrl.userInfo.getEmail();

        if (tempPdfFile != null) {
            try {
                new GMailer(email).sendMail("Bill mượn sách", "Xin chào quý khách hàng\n" +
                        "\n" +
                        "Cảm ơn bạn đã sử dụng dịch vụ thư viện online của 3HTeam chúng tôi\n" +
                        "\n" +
                        "Khi đến trả sách, vui lòng mang bill đến để scan thông tin\n" +
                        "\n" +
                        "Trân trọng cảm ơn! ",
                        tempPdfFile);
                System.out.println("ok");
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("loi");
        }

    }
}
