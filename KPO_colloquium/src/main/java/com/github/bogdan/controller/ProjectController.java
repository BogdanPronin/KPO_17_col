package com.github.bogdan.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.bogdan.deserializer.DeserializerForAddProject;
import com.github.bogdan.deserializer.DeserializerForChangeProject;
import com.github.bogdan.model.Project;
import com.github.bogdan.model.ProjectUser;
import com.github.bogdan.model.User;
import com.github.bogdan.serializer.ProjectGetSerializer;
import com.github.bogdan.serializer.UserGetSerializer;
import com.j256.ormlite.dao.Dao;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static com.github.bogdan.service.CtxService.*;
import static com.github.bogdan.service.ProjectUserService.deleteProjectUsers;

public class ProjectController {
    static Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);
    public static void add(Context ctx, Dao<User, Integer> userDao, Dao<ProjectUser, Integer> projectUserDao, Dao<Project, Integer> projectDao) throws JsonProcessingException, SQLException {
        ctx.header("content-type:app/json");
        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();

        simpleModule.addDeserializer(Project.class, new DeserializerForAddProject(userDao));

        String body = ctx.body();
        objectMapper.registerModule(simpleModule);
        Project project = objectMapper.readValue(body, Project.class);

        projectDao.create(project);
        for (User user : project.getUsers()) {
            ProjectUser projectUser = new ProjectUser(project, user);
            projectUserDao.create(projectUser);
        }
        created(ctx);

    }

    public static void change(Context ctx, Dao<User, Integer> userDao, Dao<ProjectUser, Integer> projectUserDao, Dao<Project, Integer> projectDao) throws JsonProcessingException, SQLException {
        ctx.header("content-type:app/json");
        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();

        int projectId = Integer.parseInt(ctx.pathParam("id"));
        simpleModule.addDeserializer(Project.class, new DeserializerForChangeProject(projectId, userDao, projectDao));

        String body = ctx.body();
        objectMapper.registerModule(simpleModule);
        Project project = objectMapper.readValue(body, Project.class);


        projectDao.update(project);
        if (project.getUsers() != null) {
            deleteProjectUsers(projectId,projectUserDao);
            for (User user : project.getUsers()) {
                ProjectUser projectUser = new ProjectUser(project, user);
                projectUserDao.create(projectUser);
            }
        }
        updated(ctx);
    }

    public static void get(Context ctx, Dao<Project, Integer> projectDao, Dao<ProjectUser, Integer> projectUserDao) throws JsonProcessingException, SQLException {
        ctx.header("content-type:app/json");
        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();

        simpleModule.addSerializer(Project.class, new ProjectGetSerializer(projectUserDao));
        simpleModule.addSerializer(User.class, new UserGetSerializer());

        objectMapper.registerModule(simpleModule);
        ctx.result(objectMapper.writeValueAsString(projectDao.queryForAll()));
    }

    public static void getById(Context ctx, Dao<Project, Integer> projectDao, Dao<ProjectUser, Integer> projectUserDao) throws JsonProcessingException, SQLException {
        ctx.header("content-type:app/json");
        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();

        simpleModule.addSerializer(Project.class, new ProjectGetSerializer(projectUserDao));
        simpleModule.addSerializer(User.class, new UserGetSerializer());

        objectMapper.registerModule(simpleModule);
        int id = Integer.parseInt(ctx.pathParam("id"));
        ctx.result(objectMapper.writeValueAsString(projectDao.queryForId(id)));
    }

    public static void deleteById(Context ctx, Dao<Project, Integer> projectDao, Dao<ProjectUser, Integer> projectUserDao) throws JsonProcessingException, SQLException {
        ctx.header("content-type:app/json");
        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();



        objectMapper.registerModule(simpleModule);
        int projectId = Integer.parseInt(ctx.pathParam("id"));

        deleteProjectUsers(projectId,projectUserDao);

        projectDao.delete(projectDao.queryForId(projectId));

        deleted(ctx);
    }

}
