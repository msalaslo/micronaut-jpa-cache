package com.msl.micronaut.api;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

import com.msl.micronaut.domain.entity.Camera;

import io.micronaut.context.ApplicationContext;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;

public class CameraControllerTest {

    private static EmbeddedServer server; 
    private static HttpClient client; 

    @BeforeClass
    public static void setupServer() {
        server = ApplicationContext
                .build()
                .run(EmbeddedServer.class); 
        client = server.getApplicationContext().createBean(HttpClient.class, server.getURL()); 
    }

    @AfterClass
    public static void stopServer() {
        if (server != null) {
            server.stop();
        }
        if (client != null) {
            client.stop();
        }
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void supplyAnInvalidOrderTriggersValidationFailure() {
        thrown.expect(HttpClientResponseException.class);
        thrown.expect(hasProperty("response", hasProperty("status", is(HttpStatus.BAD_REQUEST))));
        client.toBlocking().exchange(HttpRequest.GET("/cameras/list?order=foo"));
    }

    @Test
    public void testFindNonExistingCameraReturns404() {
        thrown.expect(HttpClientResponseException.class);
        thrown.expect(hasProperty("response", hasProperty("status", is(HttpStatus.NOT_FOUND))));
        HttpResponse response = client.toBlocking().exchange(HttpRequest.GET("/cameras/99"));
    }

    @Test
    public void testCameraCrudOperations() {

        List<Long> cameraIds = new ArrayList<>();

        HttpRequest request = HttpRequest.POST("/cameras", new CameraSaveCommand("DevOps")); 
        HttpResponse response = client.toBlocking().exchange(request);
        cameraIds.add(entityId(response));

        assertEquals(HttpStatus.CREATED, response.getStatus());

        request = HttpRequest.POST("/cameras", new CameraSaveCommand("Microservices")); 
        response = client.toBlocking().exchange(request);

        assertEquals(HttpStatus.CREATED, response.getStatus());

        Long id = entityId(response);
        cameraIds.add(id);
        request = HttpRequest.GET("/cameras/"+id);

        Camera camera = client.toBlocking().retrieve(request, Camera.class); 

        assertEquals("Microservices", camera.getName());

        request = HttpRequest.PUT("/cameras", new CameraUpdateCommand(id, "Micro-services"));
        response = client.toBlocking().exchange(request);  

        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());

        request = HttpRequest.GET("/cameras/" + id);
        camera = client.toBlocking().retrieve(request, Camera.class);
        assertEquals("Micro-services", camera.getName());

        request = HttpRequest.GET("/cameras/list");
        List<Camera> cameras = client.toBlocking().retrieve(request, Argument.of(List.class, Camera.class));

        assertEquals(2, cameras.size());

        request = HttpRequest.GET("/cameras/list?max=1");
        cameras = client.toBlocking().retrieve(request, Argument.of(List.class, Camera.class));

        assertEquals(1, cameras.size());
        assertEquals("DevOps", cameras.get(0).getName());

        request = HttpRequest.GET("/cameras/list?max=1&order=desc&sort=name");
        cameras = client.toBlocking().retrieve(request, Argument.of(List.class, Camera.class));

        assertEquals(1, cameras.size());
        assertEquals("Micro-services", cameras.get(0).getName());

        request = HttpRequest.GET("/cameras/list?max=1&offset=10");
        cameras = client.toBlocking().retrieve(request, Argument.of(List.class, Camera.class));

        assertEquals(0, cameras.size());

        // cleanup:
        for (Long cameraId : cameraIds) {
            request = HttpRequest.DELETE("/cameras/"+cameraId);
            response = client.toBlocking().exchange(request);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
        }
    }

    protected Long entityId(HttpResponse response) {
        String path = "/cameras/";
        String value = response.header(HttpHeaders.LOCATION);
        if ( value == null) {
            return null;
        }
        int index = value.indexOf(path);
        if ( index != -1) {
            return Long.valueOf(value.substring(index + path.length()));
        }
        return null;
    }
}
