package app.repository;

import app.config.DbConfig;
import app.domain.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * {@link BookRepository} for doing all accessData logic in table Book mapping
 * to
 * {@link Book} class in Java application
 *
 */
public class BookRepository implements CrudRepository<Book, String> {
    /**
     * this method will get all {@link Book} in database.
     *
     * @return return list of all {@link Book} in database
     */
    @Override
    public List<Book> findAll() {
        List<Book> list = new ArrayList<>();
        String query = "SELECT * FROM book";
        try (Connection connection = DbConfig.getInstance().getConnection();
                Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                list.add(new Book(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("author"),
                        resultSet.getString("description"),
                        resultSet.getString("category"),
                        resultSet.getString("bookPublisher"),
                        resultSet.getInt("bookQuantity"),
                        resultSet.getInt("bookRemaining"),
                        resultSet.getString("imagePath")));
            }
            resultSet.close();
        } catch (SQLException e) {
            System.out.println("error in findAll function in Book repo");
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Find book by id.
     *
     * @param Id book's id want to query(primary key in database)
     * @return return {@code Optional} wrapper of Book
     */
    @Override
    public Optional<Book> findById(String Id) {
        String query = "SELECT * FROM book WHERE id = ?";
        try (Connection connection = DbConfig.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, Id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Book book = new Book(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("author"),
                        resultSet.getString("description"),
                        resultSet.getString("category"),
                        resultSet.getString("bookPublisher"),
                        resultSet.getInt("bookQuantity"),
                        resultSet.getInt("bookRemaining"),
                        resultSet.getString("imagePath"));
                resultSet.close();
                return Optional.of(book);
            }
        } catch (SQLException e) {
            System.out.println("error in findById function in Book repo");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * This function is used to delete one {@link Book} by id in database.
     *
     * @param Id Book's id want to delete from database
     */
    @Override
    public boolean deleteById(String Id) {
        String query = "DELETE FROM book WHERE id = ?";
        try (Connection connection = DbConfig.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, Id);
            int count = statement.executeUpdate();
            System.out.println(count);
            return true;
        } catch (SQLException e) {
            System.out.println("error in deleteById function in Book repo");
            return false;
        }
    }

    /**
     * This function is used to save {@link Book} object in database.
     *
     * @param entity {@link Book} object you want to save in Book table in database
     */
    @Override
    public boolean save(Book entity) {
        String query = "INSERT INTO book(id,name,author,description,category,bookPublisher,bookQuantity,bookRemaining,imagePath) "
                + "VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection connection = DbConfig.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getId());
            statement.setString(2, entity.getName());
            statement.setString(3, entity.getAuthor());
            statement.setString(4, entity.getDescription());
            statement.setString(5, entity.getCategory());
            statement.setString(6, entity.getBookPublisher());
            statement.setInt(7, entity.getBookQuantity());
            statement.setInt(8, entity.getBookRemaining());
            statement.setString(9, entity.getImagePath());
            int count = statement.executeUpdate();
            System.out.println(count);
            return true;
        } catch (SQLException e) {
            System.out.println("error in save function in Book repo");
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * This function is used to count the num of {@link Book} in database.
     *
     * @return the num of document {@link Book} database
     */
    @Override
    public int count() {
        String query = "SELECT COUNT(*) FROM book";
        try (Connection connection = DbConfig.getInstance().getConnection();
                Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                int result = rs.getInt(1);
                rs.close();
                return result;
            }
        } catch (SQLException e) {
            System.out.println("error in count function in Book repo");
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Searching {@link Book} by category method.
     *
     * @param keywordCategory category keyword for searching
     * @return List of {@link Book} which have category include input keyword
     */
    public List<Book> findByCategory(String keywordCategory) {
        List<Book> list = new ArrayList<>();
        String query = "SELECT * FROM book WHERE category LIKE ?";
        try (Connection connection = DbConfig.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + keywordCategory + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                list.add(new Book(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("author"),
                        resultSet.getString("description"),
                        resultSet.getString("category"),
                        resultSet.getString("bookPublisher"),
                        resultSet.getInt("bookQuantity"),
                        resultSet.getInt("bookRemaining"),
                        resultSet.getString("imagePath")));
            }
            resultSet.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Update One {@link Book}.
     * <p>
     * This method only update one book because of unique id
     * </p>
     *
     * @param book the new {@link Book} entity want to replace the current
     *             {@link Book}
     */
    public boolean updateOne(Book book) {
        String query = "UPDATE book SET name=?,author=?,description=?,category=?,bookPublisher=?" +
                ",bookQuantity=?,bookRemaining=?,imagePath=? WHERE id=?";
        try (Connection connection = DbConfig.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setString(1, book.getName());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getDescription());
            preparedStatement.setString(4, book.getCategory());
            preparedStatement.setString(5, book.getBookPublisher());
            preparedStatement.setInt(6, book.getBookQuantity());
            preparedStatement.setInt(7, book.getBookRemaining());
            preparedStatement.setString(8, book.getImagePath());
            preparedStatement.setString(9, book.getId());

            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Book> findByInput(String col, String value) {
        List<Book> list = new ArrayList<>();
        String query = "SELECT * FROM book WHERE " + col + " LIKE ?";
        try (Connection connection = DbConfig.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + value + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new Book(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("author"),
                        resultSet.getString("description"),
                        resultSet.getString("category"),
                        resultSet.getString("bookPublisher"),
                        resultSet.getInt("bookQuantity"),
                        resultSet.getInt("bookRemaining"),
                        resultSet.getString("imagePath")));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
