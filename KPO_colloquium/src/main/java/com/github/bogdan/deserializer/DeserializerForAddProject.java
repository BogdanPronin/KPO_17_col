package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.model.Project;
import com.github.bogdan.model.ProjectUser;
import com.github.bogdan.model.User;
import com.j256.ormlite.dao.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.github.bogdan.service.DeserializerService.getJsonNodeFieldValue;
import static com.github.bogdan.service.DeserializerService.getStringFieldValue;
import static com.github.bogdan.service.LocalDateService.checkLocalDateFormat;


public class DeserializerForAddProject extends StdDeserializer<Project> {
    public DeserializerForAddProject(Dao<User, Integer> userDao) {
        super(Project.class);
        this.userDao = userDao;
    }

    private final Dao<User, Integer> userDao;

    @Override
    public Project deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

        Logger LOGGER = LoggerFactory.getLogger(DeserializerForAddProject.class);

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String name = getStringFieldValue(node, "name");

        String task = getStringFieldValue(node, "task");

        String deadline = getStringFieldValue(node, "deadline");
        checkLocalDateFormat(deadline);


        JsonNode array = node.get("users");

        List<User> users = new ArrayList<>();
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


        LOGGER.info(users.toString());
        return new Project(name, task, deadline, users);
    }
}
