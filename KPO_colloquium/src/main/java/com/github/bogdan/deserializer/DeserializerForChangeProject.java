package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.model.Project;
import com.github.bogdan.model.User;
import com.j256.ormlite.dao.Dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.github.bogdan.service.DeserializerService.getOldStringFieldValue;
import static com.github.bogdan.service.LocalDateService.checkLocalDateFormat;

public class DeserializerForChangeProject extends StdDeserializer<Project> {
    public DeserializerForChangeProject(int projectId, Dao<User, Integer> userDao, Dao<Project, Integer> projectDao) {
        super(Project.class);
        this.userDao = userDao;
        this.projectId = projectId;
        this.projectDao = projectDao;
    }

    private final int projectId;
    private final Dao<User, Integer> userDao;

    private final Dao<Project, Integer> projectDao;

    @Override
    public Project deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        try {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);

            Project projectBase = projectDao.queryForId(projectId);

            String name = getOldStringFieldValue(node, "name", projectBase.getName());

            String task = getOldStringFieldValue(node, "task",projectBase.getTask());

            String deadline = getOldStringFieldValue(node, "deadline", projectBase.getDeadline());
            checkLocalDateFormat(deadline);

            JsonNode array = node.get("users");

            List<User> users = new ArrayList<>();
            if(array != null) {
                if (array.isArray()) {
                    Iterator<JsonNode> itr = array.iterator();
                    while (itr.hasNext()) {
                        JsonNode item = itr.next();
                        try {
                            users.add(userDao.queryForId(item.asInt()));
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }else {
                users = projectBase.getUsers();
            }

            return new Project(projectId, name, task, deadline, users);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
