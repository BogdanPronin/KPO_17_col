package com.github.bogdan.databaseConfiguration;

import com.github.bogdan.model.Project;
import com.github.bogdan.model.ProjectUser;
import com.github.bogdan.model.User;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseConfiguration {
    private ConnectionSource connectionSource;

    public DatabaseConfiguration(String jdbcConnectionString) {
        try{
            connectionSource = new JdbcConnectionSource(jdbcConnectionString);
            TableUtils.createTableIfNotExists(connectionSource, User.class);
            TableUtils.createTableIfNotExists(connectionSource, Project.class);
            TableUtils.createTableIfNotExists(connectionSource, ProjectUser.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }
}
