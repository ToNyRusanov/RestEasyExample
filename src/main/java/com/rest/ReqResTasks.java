package com.rest;

import com.env.Environment;
import com.google.gson.Gson;
import com.models.ReqResUserModel;
import com.models.ReqResUsersModel;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

/**
 * Created by Petar Petrov on 07-Nov-17.
 */
public class ReqResTasks {

    private Environment env;
    private ReqResApi reqResApi;

    /**
     * Constructor. It injects the environment file and assigns the env property to the one in this class- this.env.
     * getTarget().proxy(HttpBinApi.class) - loads the HttpBinApi as a target for the client
     */
    @com.google.inject.Inject
    public ReqResTasks(final Environment env) {
        this.env = env;
        this.reqResApi = getTarget().proxy(ReqResApi.class);
    }

    public ReqResUsersModel listUsers() {
        final Gson gson = new Gson();
        final String responseJson = reqResApi.listUsers(1000).readEntity(String.class);
        ReqResUsersModel model = gson.fromJson(responseJson, ReqResUsersModel.class);
        return model;
    }

    public Response createUser(final ReqResUserModel userModel) {
        return reqResApi.createUser(getJsonString(userModel));
    }

    public Response updateUser(final String id, final ReqResUserModel userModel) {
        return reqResApi.updateUser(id, getJsonString(userModel));
    }

    private ResteasyWebTarget getTarget() {
        final ResteasyClient client = new ResteasyClientBuilder().build();
        return client.target(env.getProperty("reqresUrl"));
    }

    private String getJsonString(final Object object) {
        return new Gson().toJson(object);
    }
    public Response deleteUser(final String id) {
    	return reqResApi.deleteUser(id);
    }
}
