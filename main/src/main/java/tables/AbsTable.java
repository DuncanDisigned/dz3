package tables;



import db.MySQLConnector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbsTable {
    protected String tableName;
    protected Map<String, String> columns;
    MySQLConnector db;

    public AbsTable(String tableName) {
        this.tableName = tableName;
    }

    public void create() throws SQLException {
        String sqlRequest = String.format("CREATE TABLE IF NOT EXISTS %s (%s)", this.tableName, convertMapColumnsToString());
        db = new MySQLConnector();
        db.executeRequest(sqlRequest);
        db.close();
    }

    private String convertMapColumnsToString() {
        final String result = columns.entrySet().stream()
                .map((Map.Entry entry) -> String.format("%s %s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(", "));
        return result;
    }

    public void writeAll() throws SQLException {
        db = new MySQLConnector();
        final String sqlRequest = String.format("SELECT * FROM %s", tableName);
        ResultSet rs = db.executeRequestWithAnswer(sqlRequest);
        // Количество колонок в результирующем запросе
        try {
            int columns = rs.getMetaData().getColumnCount();
            // Перебор строк с данными
            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    System.out.print(rs.getString(i) + "\t");
                }
                System.out.println();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            db.close();
        }
    }
}
