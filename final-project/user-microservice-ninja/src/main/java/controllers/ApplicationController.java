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
import models.Message;
import models.User;
import ninja.Context;
import ninja.Result;
import ninja.exceptions.BadRequestException;
import ninja.params.Param;
import ninja.params.PathParam;
import org.hibernate.service.spi.ServiceException;
import rx.schedulers.Schedulers;
import services.MessageService;
import services.UserService;

import java.util.*;

import static ninja.Results.json;


@Singleton
public class ApplicationController {

    private final UserService userService;
    private final MessageService messageService;

    @Inject
    public ApplicationController(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    public Result initialize(Context context, Map<String, Object> options) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "initialized");

        return json()
                .status(200)
                .render(response);
    }

    public Result listMessages() {
        final List<Message> messages = new ArrayList<>();
        try {
            messageService.getMessages()
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(messages::addAll);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }

        return json()
                .status(200)
                .render(messages);
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
                messageService.publish(new Message("users", createdUser.getId(), "create", user.mapProperties(), user.mapProperties()));
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
                messageService.publish(new Message("users", id, "update", user.mapProperties(), user.mapProperties()));
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
                messageService.publish(new Message("users", user.getId(), "destroy"));
                response = json()
                        .status(204)
                        .render(new HashMap<>());
            }
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }

        return response;
    }
}
