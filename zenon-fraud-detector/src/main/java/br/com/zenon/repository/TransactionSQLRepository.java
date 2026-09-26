package br.com.zenon.repository;

import br.com.zenon.model.Transaction;
import br.com.zenon.model.TransactionCustomer;
import br.com.zenon.model.TransactionType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TransactionSQLRepository implements TransactionRepository {

    private static final String DELE_ALL_SQL = "TRUNCATE TABLE TRANSACTIONS";

    private static final String FIND_BY_ORIGIN_NAME_SQL = """
            SELECT step, type, amount,
                   name_orig, old_balance_orig, new_balance_orig,
                   name_dest, old_balance_dest, new_balance_dest,
                   is_fraud, is_flagged_fraud
            FROM TRANSACTIONS
            WHERE name_orig = ?
            ORDER BY id
            LIMIT 1
            """;

    private static final String INSERT_SQL = """
            INSERT INTO TRANSACTIONS (
                step, type, amount,
                name_orig, old_balance_orig, new_balance_orig,
                name_dest, old_balance_dest, new_balance_dest,
                is_fraud, is_flagged_fraud
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private final String url;
    private final String user;
    private final String password;

    public TransactionSQLRepository(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Optional<Transaction> findByOriginName(String nameOrig) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ORIGIN_NAME_SQL)) {
            statement.setString(1, nameOrig);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? Optional.of(toTransaction(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar transação de " + nameOrig, e);
        }
    }

    public void deleteAll() {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELE_ALL_SQL)) {
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao limpar a tabela", e);
        }
    }

    @Override
    public void saveAll(List<Transaction> transactions) {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
                for (Transaction transaction : transactions) {
                    bindInsertParameters(statement, transaction);
                    statement.addBatch();
                }

                statement.executeBatch();
                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar lote de transação", e);
        }
    }

    @Override
    public void save(Transaction transaction) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
            bindInsertParameters(statement, transaction);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar transação", e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    private void bindInsertParameters(PreparedStatement statement, Transaction transaction) throws SQLException {
        TransactionCustomer origin = transaction.origin();
        TransactionCustomer recipient = transaction.recipient();

        statement.setInt(1, transaction.step());
        statement.setString(2, transaction.type().name());
        statement.setBigDecimal(3, transaction.amount());
        statement.setString(4, origin.name());
        statement.setBigDecimal(5, origin.oldBalance());
        statement.setBigDecimal(6, origin.newBalance());
        statement.setString(7, recipient.name());
        statement.setBigDecimal(8, recipient.oldBalance());
        statement.setBigDecimal(9, recipient.newBalance());
        statement.setBoolean(10, transaction.isFraud());
        statement.setBoolean(11, transaction.isFlaggedFraud());
    }

    private Transaction toTransaction(ResultSet rs) throws SQLException {
        TransactionCustomer origin = new TransactionCustomer(
                rs.getString("name_orig"),
                rs.getBigDecimal("old_balance_orig"),
                rs.getBigDecimal("new_balance_orig")
        );
        TransactionCustomer recipient = new TransactionCustomer(
                rs.getString("name_dest"),
                rs.getBigDecimal("old_balance_dest"),
                rs.getBigDecimal("new_balance_dest")
        );
        return new Transaction(
                rs.getInt("step"),
                TransactionType.valueOf(rs.getString("type")),
                rs.getBigDecimal("amount"),
                origin,
                recipient,
                rs.getBoolean("is_fraud"),
                rs.getBoolean("is_flagged_fraud")
        );
    }
}
