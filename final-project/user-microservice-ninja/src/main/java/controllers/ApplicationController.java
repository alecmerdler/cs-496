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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import models.User;
import ninja.Context;
import ninja.Result;
import ninja.exceptions.BadRequestException;
import ninja.params.Header;
import ninja.params.Param;
import ninja.params.PathParam;
import org.hibernate.service.spi.ServiceException;
import services.UserService;

import java.util.*;

import static ninja.Results.json;


@Singleton
public class ApplicationController {

    private final UserService userService;

    @Inject
    public ApplicationController(UserService userService) {
        this.userService = userService;
    }

    public Result initialize(Context context, Map<String, Object> options) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "initialized");

        return json()
                .status(200)
                .render(response);
    }

    public Result listUsers(@Param("username") String username) {
        List<User> users = new ArrayList<>();

        if (username != null) {
            Optional<User> userOptional = userService.retrieveUserByUsername(username);
            if (userOptional.isPresent()) {
                users.add(userOptional.get());
            }
        }
        else {
            users = userService.listAllUsers();
        }

        return json()
                .status(200)
                .render(users);
    }

    public Result createUser(Context context, User user) {
        User createdUser = null;
        try {
            Optional<User> userOptional = userService.createUser(user);
            if (userOptional.isPresent()) {
                createdUser = userOptional.get();
            }
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }

        return json()
                .status(201)
                .render(createdUser);
    }

    public Result retrieveUser(@PathParam("id") Long id) {
        Result response = json()
                .status(404)
                .render(new HashMap<>());
        try {
            Optional<User> userOptional = userService.retrieveUserById(id);
            if (userOptional.isPresent()) {
                response = json()
                        .status(200)
                        .render(userOptional.get());
            }
        } catch (ServiceException se) {
            throw new BadRequestException(se.getMessage());
        }

        return response;
    }

    public Result updateUser(@PathParam("id") Long id, Context context, User user) {
        User updatedUser = null;
        try {
            Optional<User> userOptional = userService.updateUser(user);
            if (userOptional.isPresent()) {
                updatedUser = userOptional.get();
            }
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }

        return json()
                .status(200)
                .render(updatedUser);
    }

    public Result destroyUser(@PathParam("id") Long id) {
        Result response = json()
                .status(404)
                .render(new HashMap<>());
        try {
            Optional<User> userOptional = userService.retrieveUserById(id);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                userService.destroyUser(user);
                response = json()
                        .status(204)
                        .render(new HashMap<>());
            }
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }

        return response;
    }

    public Result authenticate(@Header("authorization") String authToken) {
        Map<String, Object> response = new HashMap<>();
        int statusCode = 200;

        if (authToken == null) {
            response.put("error", "missing 'Authorization' header");
            statusCode = 401;
        }
        else if (authToken.indexOf("Bearer") != 0) {
            response.put("error", "missing 'Bearer' in 'Authorization' header");
            statusCode = 401;
        }
        else {
            String accessToken = authToken.replace("Bearer ", "");
            response.put("token", accessToken);
//            GoogleCredential credential = new GoogleCredential().setAccessToken()
        }

        return json()
                .status(statusCode)
                .render(response);
    }
}
