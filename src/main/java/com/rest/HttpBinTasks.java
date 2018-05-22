package com.rest;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.env.Environment;
import com.exceptions.TestException;

import com.models.HttpBinModel;

/**
 * This method configures the ReSTEasy client for a target- in this case HttpBin.
 * Do not instantiate this class- inject it.
 * Use this class for reference when creating new ReSTEasy client for new services.
 */

//TODO It will be nice to add a custom response class in the future
public class HttpBinTasks {

    private Environment env;
    private HttpBinApi httpBinApi;

    /**
     * Constructor. It injects the environment file and assigns the env property to the one in this class- this.env.
     * getTarget().proxy(HttpBinApi.class) - loads the HttpBinApi as a target for the client
     */
    @com.google.inject.Inject
    public HttpBinTasks(final Environment env) {
        this.env = env;
        this.httpBinApi = getTarget().proxy(HttpBinApi.class);
    }

    /**
     * getMethodResponse and postMethodResponse execute the call the corresponding methods from HttpBinApi.
     * This classes should be public and with meaningful names (not like these).
     * They are the ones being used in the step definitions.
     * Everything else should be private.
     */
    public Response getMethodResponse() {
        /*
          This has to be an array. Trust me! Do not try to change it.
         */
        final Response[] response = new Response[1];
        final int pollingIntervalInSeconds = 5;
        final int pollTimeoutInSeconds = 60;
        /*
          This is Awaitility. This block polls the API endpoint until the response code is 200.
          The lambda expression is executed every "pollingIntervalInSeconds" for max period of "pollTimeoutInSeconds".
          If the expression returns True, the execution continues.
          If it does not before "pollTimeoutInSeconds" is reached, a ConditionTimeoutException is thrown.
         */
        try {
            Awaitility.await().with().pollInterval(pollingIntervalInSeconds, TimeUnit.SECONDS)
                    .atMost(pollTimeoutInSeconds, TimeUnit.SECONDS).await("Polling for feature update")
                    .until(() -> {
                response[0] = httpBinApi.getMethod();
                        return response[0].getStatus() == 200;
                    });
        } catch (ConditionTimeoutException e) {
            throw new TestException(e.getMessage() + " status code is not 200");
        }
        return response[0];
    }

    public Response postMethodResponse(final HttpBinModel model) {
        return httpBinApi.postMethod(model.getUserName(), model.getLength(),
                model.getCupSize(), model.getMeasures());
    }

    /**
     * Private method returning the web target for this service.
     * It builds a client and add a target to it, using the apiUrl from the yml file.
     */
    private ResteasyWebTarget getTarget() {
        final ResteasyClient client = new ResteasyClientBuilder().build();
        return client.target(env.getProperty("apiUrl"));
    }
}
