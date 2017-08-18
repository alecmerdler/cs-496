/**
 * Copyright (C) 2012 the original author or authors.
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

package conf;


import controllers.ApplicationController;
import ninja.Results;
import ninja.Router;
import ninja.application.ApplicationRoutes;

public class Routes implements ApplicationRoutes {

    private String apiUrl = "/api/v1";
    private String usersUrl = apiUrl + "/users";

    @Override
    public void init(Router router) {
        router.GET().route("/").with(Results.json().render("name", "user-microservice-ninja"));
        router.GET().route("/healthcheck").with(Results.json().render("status", "running"));
        router.POST().route("/initialize").with(ApplicationController.class, "initialize");

        router.GET().route("/messages").with(ApplicationController.class, "listMessages");

        router.GET().route(usersUrl).with(ApplicationController.class, "listUsers");
        router.POST().route(usersUrl).with(ApplicationController.class, "createUser");
        router.GET().route(usersUrl + "/{id}").with(ApplicationController.class, "retrieveUser");
        router.PUT().route(usersUrl + "/{id}").with(ApplicationController.class, "updateUser");
        router.DELETE().route(usersUrl + "/{id}").with(ApplicationController.class, "destroyUser");
    }

}
