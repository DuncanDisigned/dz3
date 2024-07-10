package tables;


import data.AnimalData;
import db.MySQLConnector;
import exceptions.AnimalNotSupported;
import factory.AnimalFactory;
import main.java.Animal;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class AnimalTable extends AbsTable {

    public AnimalTable() throws SQLException {
        super("animals");
        columns = new HashMap<>();
        columns.put("id", "int(254)");
        columns.put("type", "varchar(15)");
        columns.put("name", "varchar(15)");
        columns.put("age", "int");
        columns.put("weight", "FLOAT");
        columns.put("color", "varchar(15)");
        create();
    }

    public ArrayList<Animal> selectAll() throws AnimalNotSupported, SQLException {
        String sqlQuery = String.format("SELECT * FROM %s", tableName);
        return selectByQuery(sqlQuery);
    }

    public ArrayList<Animal> selectByType(String type) throws AnimalNotSupported, SQLException {
        String sqlQuery = String.format("SELECT * FROM %s WHERE type = '%s'",
                tableName, type);
        return selectByQuery(sqlQuery);
    }

    private ArrayList<Animal> selectByQuery(String sqlQuery) throws AnimalNotSupported, SQLException {
        ArrayList<Animal> animals = new ArrayList<>();
        db = new MySQLConnector();
        ResultSet rs = db.executeRequestWithAnswer(sqlQuery);
        try {
            // Перебор строк с данными
            while (rs.next()) {
                //Создать объект устройство и добавление его в результирующий массив
                AnimalData type = AnimalData.valueOf(rs.getString("type").toUpperCase());
                Animal animal = new AnimalFactory().create(type);
                animal.setId(rs.getLong("id"));
                animal.setAnimalType(type);
                animal.setName(rs.getString("name"));
                animal.setAge(rs.getInt("age"));
                animal.setWeight(rs.getFloat("weight"));
                animal.setColor(rs.getString("color"));
                animals.add(animal);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (AnimalNotSupported e) {
            throw new RuntimeException(e);
        } finally {
            db.close();
        }
        return animals;
    }


    public void insert(Animal animal) throws SQLException {
        MySQLConnector db = new MySQLConnector();
        String sqlQuery = String.format("INSERT INTO %s (type, name, age, weight, color) VALUES (?, ?, ?, ?, ?)", tableName);
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sqlQuery)) {
            pstmt.setString(1, String.valueOf(animal.getAnimalType()));
            pstmt.setString(2, animal.getName());
            pstmt.setInt(3, animal.getAge());
            pstmt.setFloat(4, animal.getWeight());
            pstmt.setString(5, animal.getColor());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public void update(Animal animal) throws SQLException {
        MySQLConnector db = new MySQLConnector();
        StringBuilder sqlQuery = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
        boolean isFirst = true;

        if (animal.getName() != null && !animal.getName().isEmpty()) {
            if (!isFirst) {
                sqlQuery.append(", ");
            }
            sqlQuery.append("name='").append(animal.getName()).append("'");
            isFirst = false;
        }

        if (animal.getAge() > 0) {
            if (!isFirst) {
                sqlQuery.append(", ");
            }
            sqlQuery.append("age=").append(animal.getAge());
            isFirst = false;
        }

        if (animal.getWeight() > 0) {
            if (!isFirst) {
                sqlQuery.append(", ");
            }
            sqlQuery.append("weight=").append(animal.getWeight());
            isFirst = false;
        }

        if (animal.getColor() != null && !animal.getColor().isEmpty()) {
            if (!isFirst) {
                sqlQuery.append(", ");
            }
            sqlQuery.append("color='").append(animal.getColor()).append("'");
        }

        sqlQuery.append(" WHERE id=").append(animal.getId());

        db.executeRequest(sqlQuery.toString());
        db.close();
    }

    public Animal selectById(int id) throws SQLException {
        db = new MySQLConnector();
        String sqlQuery = String.format("SELECT * FROM %s WHERE id = %d", tableName, id);
        ResultSet rs = db.executeRequestWithAnswer(sqlQuery);

        Animal animal = null;
        try {
            if (rs.next()) {
                AnimalData type = AnimalData.valueOf(rs.getString("type"));
                animal = new AnimalFactory().create(type);
                animal.setId(rs.getLong("id"));
                animal.setAnimalType(type);
                animal.setName(rs.getString("name"));
                animal.setAge(rs.getInt("age"));
                animal.setWeight(rs.getFloat("weight"));
                animal.setColor(rs.getString("color"));
            }
        } catch (SQLException | AnimalNotSupported e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return animal;
    }

    public void delete(long id) throws SQLException {
        db = new MySQLConnector();
        String sqlQuery = String.format("DELETE FROM %s WHERE id=?",
                tableName);
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sqlQuery)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }
}