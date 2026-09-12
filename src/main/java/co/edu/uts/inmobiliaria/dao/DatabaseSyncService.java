package co.edu.uts.inmobiliaria.dao;

import co.edu.uts.inmobiliaria.config.Database;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Replica la base remota en la base local sin doble escritura ni conflictos de IDs. */
public final class DatabaseSyncService {
    private static final DatabaseSyncService INSTANCE = new DatabaseSyncService();

    private DatabaseSyncService() {
    }

    public static DatabaseSyncService getInstance() {
        return INSTANCE;
    }

    public synchronized int synchronize() throws SQLException {
        if (!Database.isRemoteConfigured()) {
            throw new SQLException("Configura la conexión remota antes de sincronizar.");
        }
        if (!Database.isLocalSyncEnabled()) {
            throw new SQLException("La sincronización local está desactivada. Usa INMOBILIARIA_SYNC_LOCAL=true.");
        }
        try (Connection remote = Database.getRemoteConnection(); Connection local = Database.getLocalConnection()) {
            List<String> tables = tables(remote);
            verifyLocalTables(local, tables);
            local.setAutoCommit(false);
            try {
                setForeignKeys(local, false);
                clearTables(local, tables);
                for (String table : tables) copyTable(remote, local, table);
                setForeignKeys(local, true);
                local.commit();
                return tables.size();
            } catch (SQLException exception) {
                local.rollback();
                try {
                    setForeignKeys(local, true);
                } catch (SQLException ignored) {
                    // Se conserva la excepción original para el diagnóstico.
                }
                throw exception;
            }
        }
    }

    private List<String> tables(Connection connection) throws SQLException {
        List<String> tables = new ArrayList<>();
        DatabaseMetaData metadata = connection.getMetaData();
        try (ResultSet result = metadata.getTables(connection.getCatalog(), null, "%", new String[]{"TABLE"})) {
            while (result.next()) tables.add(result.getString("TABLE_NAME"));
        }
        return tables;
    }

    private void verifyLocalTables(Connection local, List<String> tables) throws SQLException {
        DatabaseMetaData metadata = local.getMetaData();
        for (String table : tables) {
            try (ResultSet result = metadata.getTables(local.getCatalog(), null, table, new String[]{"TABLE"})) {
                if (!result.next()) {
                    throw new SQLException("La tabla local falta para sincronizar: " + table
                            + ". Ejecuta database/01_schema.sql y database/04_chat.sql.");
                }
            }
        }
    }

    private void clearTables(Connection local, List<String> tables) throws SQLException {
        try (Statement statement = local.createStatement()) {
            // DELETE mantiene la transacción; TRUNCATE haría commit implícito en MySQL.
            for (String table : tables) statement.executeUpdate("DELETE FROM " + quote(table));
        }
    }

    private void copyTable(Connection remote, Connection local, String table) throws SQLException {
        String selectSql = "SELECT * FROM " + quote(table);
        try (Statement select = remote.createStatement(); ResultSet rows = select.executeQuery(selectSql)) {
            ResultSetMetaData metadata = rows.getMetaData();
            int columns = metadata.getColumnCount();
            StringBuilder columnsSql = new StringBuilder();
            StringBuilder valuesSql = new StringBuilder();
            for (int index = 1; index <= columns; index++) {
                if (index > 1) {
                    columnsSql.append(", ");
                    valuesSql.append(", ");
                }
                columnsSql.append(quote(metadata.getColumnName(index)));
                valuesSql.append('?');
            }
            String insertSql = "INSERT INTO " + quote(table) + " (" + columnsSql + ") VALUES (" + valuesSql + ")";
            try (PreparedStatement insert = local.prepareStatement(insertSql)) {
                int batchSize = 0;
                while (rows.next()) {
                    for (int index = 1; index <= columns; index++) insert.setObject(index, rows.getObject(index));
                    insert.addBatch();
                    if (++batchSize == 500) {
                        insert.executeBatch();
                        batchSize = 0;
                    }
                }
                if (batchSize > 0) insert.executeBatch();
            }
        }
    }

    private void setForeignKeys(Connection connection, boolean enabled) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("SET FOREIGN_KEY_CHECKS = " + (enabled ? "1" : "0"));
        }
    }

    private static String quote(String identifier) {
        return "`" + identifier.replace("`", "``") + "`";
    }
}
