/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers;


import com.google.inject.Injector;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import dao.UserDao;
import models.User;
import ninja.NinjaTest;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import utils.UnirestObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ApplicationControllerIntegrationTest extends NinjaTest {

    UserDao userDao;
    ObjectMapper objectMapper;
    String apiUrl;
    String usersUrl;

    @Before
    public void beforeEach() {
        Injector injector = getInjector();
        userDao = injector.getInstance(UserDao.class);
        objectMapper = new ObjectMapper();
        apiUrl = getServerAddress() + "api/v1";
        usersUrl = apiUrl + "/users";

        Unirest.setObjectMapper(new UnirestObjectMapper());
    }

    @Test
    public void testRoot() {
        try {
            HttpResponse<JsonNode> response = Unirest.get(getServerAddress())
                    .asJson();

            assertEquals(200, response.getStatus());
            assertEquals("user-microservice-ninja", response.getBody().getObject().get("name"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testHealthcheck() {
        try {
            HttpResponse<JsonNode> response = Unirest.get(getServerAddress() + "/healthcheck")
                    .asJson();

            assertEquals(200, response.getStatus());
            assertEquals("running", response.getBody().getObject().get("status"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testListUsers() {
        String response = ninjaTestBrowser.makeJsonRequest(usersUrl);
        try {
            List<User> users = objectMapper.readValue(response, new TypeReference<List<User>>(){});

            assertEquals(0, users.size());
        } catch (IOException ioe) {
            fail(ioe.getMessage());
        }
    }

    @Test
    public void testListUsersParamUsernameExists() {
        User user = new User("bob", "bob@gmail.com");
        userDao.create(user);
        String response = ninjaTestBrowser.makeJsonRequest(usersUrl + "?username=" + user.getUsername());
        try {
            List<User> usersWithSameUsername = objectMapper.readValue(response, new TypeReference<List<User>>(){});

            assertEquals(1, usersWithSameUsername.size());
            assertEquals(user.getEmail(), usersWithSameUsername.get(0).getEmail());
        } catch (IOException ioe) {
            fail(ioe.getMessage());
        }
    }

    @Test
    public void testListUsersParamUsernameDoesNotExist() {
        String username = "stanley";
        String response = ninjaTestBrowser.makeJsonRequest(usersUrl + "?username=" + username);
        try {
            List<User> usersWithSameUsername = objectMapper.readValue(response, new TypeReference<List<User>>(){});

            assertEquals(0, usersWithSameUsername.size());
        } catch (IOException ioe) {
            fail(ioe.getMessage());
        }
    }

    @Test
    public void testCreateUserValid() {
        User user = new User("bob", "bob@bob.com");
        String response = ninjaTestBrowser.postJson(usersUrl, user);
        try {
            User createdUser = objectMapper.readValue(response, User.class);

            assertEquals(user.getUsername(), createdUser.getUsername());
        } catch (IOException ioe) {
            fail(ioe.getMessage());
        }
    }

    @Test
    public void testRetrieveUserDoesNotExist() {
        int id = 42;
        try {
            HttpResponse<JsonNode> response = Unirest.get(usersUrl + "/" + id)
                    .asJson();
            Map<String, Object> responseBody = objectMapper.readValue(response.getBody().toString(), new TypeReference<Map>(){});

            assertEquals(404, response.getStatus());
            assertTrue(responseBody.isEmpty());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testRetrieveUserExists() {
        User user = userDao.create(new User("bob", "bob@gmail.com"));
        try {
            HttpResponse<JsonNode> response = Unirest.get(usersUrl + "/" + user.getId())
                    .asJson();
            User responseUser = objectMapper.readValue(response.getBody().toString(), User.class);

            assertEquals(200, response.getStatus());
            assertEquals(user.getUsername(), responseUser.getUsername());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testUpdateUserExists() {
        User user = userDao.create(new User("bob", "bob@gmail.com"));
        String newEmail = "bob@yahoo.com";
        user.setEmail(newEmail);
        try {
            HttpResponse<JsonNode> response = Unirest.put(usersUrl + "/" + user.getId())
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .body(user)
                    .asJson();
            User responseUser = objectMapper.readValue(response.getBody().toString(), User.class);

            assertEquals(200, response.getStatus());
            assertEquals(user.getId(), responseUser.getId());
            assertEquals(newEmail, responseUser.getEmail());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDestroyUserDoesNotExist() {
        Long id = new Long(43);
        try {
            HttpResponse<JsonNode> response = Unirest.delete(usersUrl + "/" + id)
                    .asJson();

            assertEquals(404, response.getStatus());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDestroyUserExists() {
        User user = userDao.create(new User("bob", "bob@gmail.com"));
        try {
            HttpResponse<JsonNode> response = Unirest.delete(usersUrl + "/" + user.getId())
                    .asJson();

            assertEquals(204, response.getStatus());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
