package org.boomgames.boomclicker;

import java.beans.PropertyVetoException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.cuba.joker.Joker;
import org.joker.dialect.Dialect;
import org.joker.dialect.postgresql.PostgreDialect;
import org.joker.session.Session;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class Database {
    private static Database instance;
    
    public static Database getInstance() {
        return instance;
    }
    
    public static Database newInstance(String url, String user, String password) {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass("org.postgresql.Driver");
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }
        dataSource.setJdbcUrl("jdbc:postgresql://" + url + "?ssl=true&sslmode=require");
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setInitialPoolSize(2);
        dataSource.setMaxPoolSize(8);
        return instance = new Database(dataSource);
    }

    private Dialect dialect;
    private Joker joker;
    
    private Database(DataSource dataSource) {
        this.dialect = new PostgreDialect();
        this.joker = new Joker(dataSource, dialect);
    }
    
    public Session openSession() throws SQLException {
        return joker.openSession();
    }
    
    public Dialect dialect() {
        return dialect;
    }
}
