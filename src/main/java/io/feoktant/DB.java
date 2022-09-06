package io.feoktant;

import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DB {

    private DB() {}

    private static final DataSource dataSource;

    static {
        var h2DataSource = new JdbcDataSource();
        var url = "jdbc:h2:mem:txscript;INIT=runscript from 'classpath:txscript.sql'";
        h2DataSource.setUrl(url);

        dataSource = h2DataSource;
    }

    public static PreparedStatement prepare(String sql) throws SQLException {
        return dataSource.getConnection().prepareStatement(sql);
    }
}
