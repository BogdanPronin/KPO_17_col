package com.github.bogdan.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.bogdan.controller.ProjectController;
import com.github.bogdan.model.Project;
import com.github.bogdan.model.ProjectUser;
import com.github.bogdan.model.User;
import com.j256.ormlite.dao.Dao;

import java.io.IOException;
import java.sql.SQLException;

import static com.github.bogdan.service.ProjectUserService.getProjectUsers;

public class ProjectGetSerializer  extends StdSerializer<Project> {
    public ProjectGetSerializer(Dao<ProjectUser, Integer> projectUserDao) {
        super(Project.class);
        this.projectUserDao = projectUserDao;
    }

    private final Dao<ProjectUser, Integer> projectUserDao;

    @Override
    public void serialize(Project project, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id",project.getId());
        jsonGenerator.writeObjectField("name",project.getName());
        jsonGenerator.writeObjectField("task",project.getTask());
        jsonGenerator.writeStringField("deadline",project.getDeadline());

        jsonGenerator.writeArrayFieldStart("projectUser");
        try {
            for(User u: getProjectUsers(project.getId(),projectUserDao)){
                jsonGenerator.writeObject(u);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        jsonGenerator.writeEndArray();

        jsonGenerator.writeEndObject();
    }

}
