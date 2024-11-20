package app.controller.user.HomePage;

import java.io.IOException;
import java.io.InputStream;

import app.config.ViewConfig.FXMLResolver;
import app.controller.BaseController;
import app.controller.user.BookDetail.BookDetailController;
import app.domain.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;



public class Card implements BaseController{
    
    @FXML
    private Button cardButton;

    @FXML  
    private ImageView imageURL;

    @FXML
    private Label bookName;

    @FXML
    private Label authorName;

    @FXML
    private VBox cardVBox;

    private Book book;

    private String status;

    private String previousPath;

    public void loadBook(Book book) {
        this.book = book;
        this.status = "returned";
        bookName.setText(book.getName());
        authorName.setText(book.getAuthor());
        loadImage(book);              
    }

    public void loadBookWithStatus(Book book, String status) {
        this.book = book;
        this.status = status;
        bookName.setText(book.getName());
        authorName.setText(book.getAuthor());
        loadImage(book);  
        try {
            FXMLLoader statusloader = new FXMLLoader(getClass().getResource("/view/user/status/" + status + ".fxml"));
            Label statusLabel = statusloader.load();
            cardVBox.getChildren().add(statusLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }
                
    }

     private void loadImage(Book book) {
        InputStream inputStream = getClass().getResourceAsStream("/image/book/" + book.getImagePath());
        if (inputStream == null) {
            imageURL.setImage(new Image(getClass().getResourceAsStream("/image/book/book-default-cover.jpg")));
        } else {
            imageURL.setImage(new Image(inputStream));

        }
    }

    public void handleCardEvent(ActionEvent e) {
        if (e.getSource() == cardButton) {
            loadBookDetail(book);
        }
    }

    private void loadBookDetail(Book book) {
        FXMLResolver.getInstance().renderScene("user/bookdetail/bookdetail");
        BookDetailController controller = FXMLResolver.getInstance().getLoader().getController();
        controller.loadBookWithStatus(book, status);
    }

    @Override
    public void initialize() {
        // TODO Auto-generated method stub
    }

    
}
