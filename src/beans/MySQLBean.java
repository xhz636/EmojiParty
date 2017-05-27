package beans;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLBean {

    private Connection connection = null;

    public MySQLBean() {
        final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://localhost:3306/EmojiParty?useUnicode=true&characterEncoding=utf-8";
        final String USER = "root";
        final String PASSWORD = "xhz636";
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isIDExist(String ID) {
        boolean result = true;
        try {
            String sql = "select count(*) from imageinfo where ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            if (count == 0)
                result = false;
            resultSet.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean addImage(Image image) {
        boolean result = false;
        try {
            String sql = "insert into imageinfo (ID, format, size, height, width, uploadtime) values (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, image.getID());
            preparedStatement.setString(2, image.getFormat());
            preparedStatement.setInt(3, image.getSize());
            preparedStatement.setInt(4, image.getHeight());
            preparedStatement.setInt(5, image.getWidth());
            preparedStatement.setTimestamp(6, new Timestamp(image.getUploadtime().getTime()));
            int row = preparedStatement.executeUpdate();
            if (row == 1)
                result = true;
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean addKeyWord(Keyword keyword) {
        boolean result = false;
        try {
            String sql = "insert into keywords (keyword, ID, weight) values (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, keyword.getKeyword());
            preparedStatement.setString(2, keyword.getID());
            preparedStatement.setInt(3, 0);
            int row = preparedStatement.executeUpdate();
            if (row == 1)
                result = true;
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean addWeight(String ID) {
        return false;
    }

    public List<Image> getImageByKeywords(List<String> keywords) {
        List<Image> result = new ArrayList<>();
        if (keywords.size() <= 0)
            return result;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("select ID from keywords where keyword like ?");
            for (int i = 1; i < keywords.size(); i++)
                sql.append(" or keyword like ?");
            sql.append(" group by ID order by count(ID) desc");
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
            for (int i = 0; i < keywords.size(); i++)
                preparedStatement.setString(i + 1, "%" + keywords.get(i) + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String sqlID = "select * from imageinfo where ID = ?";
                PreparedStatement preparedStatementID = connection.prepareStatement(sqlID);
                preparedStatementID.setString(1, resultSet.getString("ID"));
                ResultSet resultSetID = preparedStatementID.executeQuery();
                resultSetID.next();
                Image temp = new Image(resultSetID.getString("ID"),
                                       resultSetID.getString("format"),
                                       resultSetID.getInt("size"),
                                       resultSetID.getInt("height"),
                                       resultSetID.getInt("width"),
                                       resultSetID.getDate("uploadtime"));
                result.add(temp);
                resultSetID.close();
                preparedStatementID.clearParameters();
            }
            resultSet.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
