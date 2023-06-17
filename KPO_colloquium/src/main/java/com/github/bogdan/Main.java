package com.github.bogdan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.bogdan.controller.ProjectController;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.Project;
import com.github.bogdan.model.ProjectUser;
import com.github.bogdan.model.User;
import com.github.bogdan.serializer.WebExceptionSerializer;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.Javalin;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.enableDevLogging();
            javalinConfig.enableCorsForAllOrigins();
            javalinConfig.defaultContentType = "application/json";
        }).start(22867);

        String jdbcConnectionString = "jdbc:sqlite:/Users/bogdan/Desktop/KPO.sqlite";
        DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration(jdbcConnectionString);

        Dao<User, Integer> userDao = DaoManager.createDao(databaseConfiguration.getConnectionSource(), User.class);
        Dao<Project, Integer> projectDao = DaoManager.createDao(databaseConfiguration.getConnectionSource(), Project.class);
        Dao<ProjectUser, Integer> projectUserDao = DaoManager.createDao(databaseConfiguration.getConnectionSource(), ProjectUser.class);


        app.post("/projects", ctx -> ProjectController.add(ctx, userDao, projectUserDao, projectDao));

        app.get("/projects", ctx -> ProjectController.get(ctx, projectDao, projectUserDao));

        app.get("/projects/:id", ctx -> ProjectController.getById(ctx, projectDao, projectUserDao));

        app.patch("/projects/:id", ctx -> ProjectController.change(ctx, userDao, projectUserDao, projectDao));

        app.delete("/projects/:id", ctx -> ProjectController.deleteById(ctx, projectDao, projectUserDao));

        app.exception(WebException.class, (e, ctx) -> {
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addSerializer(WebException.class, new WebExceptionSerializer());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(simpleModule);
            try {
                ctx.result(objectMapper.writeValueAsString(e));
                ctx.status(e.getStatus());
            } catch (JsonProcessingException jsonProcessingException) {
                jsonProcessingException.printStackTrace();
            }
        });

    }
}
