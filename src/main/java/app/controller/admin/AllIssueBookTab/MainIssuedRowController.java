package app.controller.admin.AllIssueBookTab;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import app.controller.BaseController;
import app.controller.helper.ShowAlert;
import app.domain.BorrowReport;
import app.repository.ReportRepository;
import app.service.mainService.ReportService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

public class MainIssuedRowController implements BaseController {

    @FXML
    Label idLabel, userIdLabel, bookIdLabel;

    @FXML
    ChoiceBox<String> statusChoiceBox;

    @FXML
    Button detailButton, updateButton, deleteButton;

    @FXML
    DatePicker borrowDatePicker, dueDatePicker, returnDatePicker;

    BorrowReport borrowReport;

    ReportService reportService;

    ShowAlert showAlert;

    MainAllIssueController mainAllIssueCtrl;

    void setMainAllIssueController(MainAllIssueController mainAllIssueCtrl) {
        this.mainAllIssueCtrl = mainAllIssueCtrl;
    }

    @Override
    public void initialize() {
        showAlert = new ShowAlert();
        reportService = new ReportService(new ReportRepository(), null, null);
        new AllSetUp().initMainIssueRowCtrl(this);
    }

    public void loadData(BorrowReport data) {
        this.borrowReport = data;
        setUpdata();
    }

    void setUpdata() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (borrowReport.getBorrowDate() != null) {
            LocalDate date = LocalDate.parse(borrowReport.getBorrowDate(), formatter);
            borrowDatePicker.setValue(date);
        }

        if (borrowReport.getExpectedReturnDate() != null) {
            LocalDate date = LocalDate.parse(borrowReport.getExpectedReturnDate(), formatter);
            dueDatePicker.setValue(date);
        }

        if (borrowReport.getReturnDate() != null) {
            LocalDate date = LocalDate.parse(borrowReport.getReturnDate(), formatter);
            returnDatePicker.setValue(date);
        }

        idLabel.setText(String.valueOf(borrowReport.getId()));
        userIdLabel.setText(borrowReport.getUserId());
        bookIdLabel.setText(borrowReport.getBookId());

        statusChoiceBox.getItems().addAll(BorrowReport.PENDING, BorrowReport.BORROWED, BorrowReport.RETURNED);
        statusChoiceBox.setValue(borrowReport.getStatus());
    }

    boolean updateDataBorrowReport() {
        if (!validate()) {
            return false;
        }

        LocalDate borrowDate = borrowDatePicker.getValue();
        if (borrowDate != null) {
            borrowReport.setBorrowDate(borrowDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }

        LocalDate dueDate = dueDatePicker.getValue();
        if (dueDate != null) {
            borrowReport.setExpectedReturnDate(dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }

        LocalDate returnDate = returnDatePicker.getValue();
        if (returnDate != null) {
            borrowReport.setReturnDate(returnDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        } else {
            borrowReport.setReturnDate(null);
        }

        String status = statusChoiceBox.getValue();
        borrowReport.setStatus(status);

        return true;
    }

    boolean validate() {
        String status = statusChoiceBox.getValue();

        if (status.equals(BorrowReport.BORROWED)) {
            if (borrowDatePicker.getValue() == null || dueDatePicker.getValue() == null) {
                showAlert.showAlert("Borrow date and due date cannot be empty!", "error");
                return false;
            }
        }

        if (status.equals(BorrowReport.RETURNED)) {
            if (returnDatePicker.getValue() == null) {
                showAlert.showAlert("Invalid return date for status 'Returned'!", "error");
                return false;
            }
        }

        return true;
    }

}