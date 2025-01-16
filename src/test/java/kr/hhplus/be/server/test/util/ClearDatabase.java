package kr.hhplus.be.server.test.util;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
@ActiveProfiles("test")
public class ClearDatabase implements InitializingBean {
    private final List<String> tableNames = new ArrayList<String>();

    private final DataSource dataSource;

    @PersistenceContext
    private EntityManager entityManager;

    public ClearDatabase(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void findTableList() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            var resultSet = statement.executeQuery("SHOW TABLES");
            while (resultSet.next()) {
                tableNames.add(resultSet.getString(1));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch table names", e);
        }
    }

    public void clear() {
        entityManager.clear(); //영속성 컨텍스트 CLEAR
        truncate();
    }

    private void truncate() { // 실제 DB CLEAR
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("SET FOREIGN_KEY_CHECKS = 0");
            for (String tableName : tableNames) {
                statement.execute(String.format("TRUNCATE TABLE %s", tableName));
            }
            statement.execute("SET FOREIGN_KEY_CHECKS = 1");
        } catch (Exception e) {
            throw new RuntimeException("Failed to clear database", e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}