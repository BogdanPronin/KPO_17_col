package com.github.bogdan.service;

import com.github.bogdan.model.ProjectUser;
import com.github.bogdan.model.User;
import com.j256.ormlite.dao.Dao;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProjectUserService {
    public static void deleteProjectUsers(int projId, Dao<ProjectUser, Integer> projectUserDao) throws SQLException {
        for(ProjectUser pu: projectUserDao.queryForAll()){
            if(pu.getProject().getId() == projId){
                projectUserDao.delete(pu);
            }
        }
    }
    public static ArrayList<User> getProjectUsers(int projId, Dao<ProjectUser, Integer> projectUserDao) throws SQLException {
        ArrayList<User> users = new ArrayList<User>();
        for(ProjectUser pu: projectUserDao.queryForAll()){
            if(pu.getProject().getId() == projId){
                users.add(pu.getUser());
            }
        }
        return users;
    }
}
