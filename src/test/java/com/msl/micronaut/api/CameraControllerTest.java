package com.msl.micronaut.api;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.msl.micronaut.api.dto.CameraDTO;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CameraControllerTest {

    private static EmbeddedServer server; 
    private static HttpClient client; 
    
    private static final String BASE_PATH = "/correlation/v1.0";
    
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
        client.toBlocking().exchange(HttpRequest.GET(BASE_PATH + "/cameras/page?order=foo"));
    }

    @Test
    public void testFindNonExistingCameraReturns404() {
    	log.info("testFindNonExistingCameraReturns404");
        thrown.expect(HttpClientResponseException.class);
        thrown.expect(hasProperty("response", hasProperty("status", is(HttpStatus.NOT_FOUND))));
        HttpResponse response = client.toBlocking().exchange(HttpRequest.GET(BASE_PATH + "/cameras/99"));
    }
    
    @Test
    public void testCameraInsertOperation() {
    	log.info("testCameraInsertOperation");
    	String serial = "123456789";
    	deleteBySerial(serial);
        CameraDTO command1 = new CameraDTO(serial, 123456789,"ESP", "123456789", "01", "alias", new Date(), new Date(), "0", "11111");
        HttpRequest request = HttpRequest.POST(BASE_PATH + "/cameras", command1); 
        HttpResponse response = client.toBlocking().exchange(request);
        String serialRetrieved = entityId(response);
        assertEquals(HttpStatus.CREATED, response.getStatus());
        
        deleteBySerial(serial);
    }
    
    @Test
    public void testCameraInsertAndRetrieveOperation() {
    	log.info("testCameraInsertAndRetrieveOperation");
    	String serial = "123456789";
    	deleteBySerial(serial);
    	CameraDTO command2 = new CameraDTO(serial, 123456789 ,"ESP", "123456789", "01", "alias", new Date(), new Date(), "0", "11111");
        HttpRequest request = HttpRequest.POST(BASE_PATH + "/cameras", command2); 
        HttpResponse response = client.toBlocking().exchange(request);

        assertEquals(HttpStatus.CREATED, response.getStatus());

        String serialRetrieved = entityId(response);
        log.info("testCameraInsertAndRetrieveOperation serial:" + serialRetrieved);
        request = HttpRequest.GET(BASE_PATH + "/cameras/" + serialRetrieved);

        CameraDTO camera = client.toBlocking().retrieve(request, CameraDTO.class);
        
        log.info("testCameraInsertAndRetrieveOperation, retrieved camera:{} for serial:{}", camera, serialRetrieved);

        assertEquals("alias", camera.getAlias());
        
        deleteBySerial(serial);
    }
    
    @Test
    public void testCameraUpdateOperations() {
    	log.info("testCameraUpdateOperations");

        HttpRequest request = null;
        HttpResponse response = null;
        
    	String serial = "ZB3E6V2J2000RAL";
        log.info("testCameraUpdateOperations serial:" + serial);
        request = HttpRequest.GET(BASE_PATH + "/cameras/" + serial);

        
        CameraDTO camera = client.toBlocking().retrieve(request, CameraDTO.class);    
        
        String alias = "alias1";
        camera.setAlias(alias);
        
        request = HttpRequest.PUT(BASE_PATH + "/cameras/" + serial, camera);
        response = client.toBlocking().exchange(request);  

        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());

        request = HttpRequest.GET(BASE_PATH + "/cameras/" + serial);
        CameraDTO cameraAfterUpdate = client.toBlocking().retrieve(request, CameraDTO.class);
        
        log.info("testCameraUpdateOperations, retrieved camera:{} for serial:{}", cameraAfterUpdate, serial);  
        //desactivado porque no actualizamos en el repositorio
//        assertEquals(alias, cameraAfterUpdate.getAlias());
        
//        deleteBySerial(serial);
    }

    @Test
    public void testCameraPaginationOperations() {
    	log.info("testCameraPaginationOperations");

        List<Long> cameraIds = new ArrayList<>();
        HttpRequest request = null;
        HttpResponse response = null;
        CameraDTO camera = null;

//        request = HttpRequest.GET(BASE_PATH + "/cameras/list");
//        List<Camera> cameras = client.toBlocking().retrieve(request, Argument.of(List.class, Camera.class));
//
//        assertEquals(2, cameras.size());
//
//        request = HttpRequest.GET(BASE_PATH + "/cameras/list?max=1");
//        cameras = client.toBlocking().retrieve(request, Argument.of(List.class, Camera.class));
//
//        assertEquals(1, cameras.size());
//        assertEquals("123456789", cameras.get(0).getSerial());
//
//        request = HttpRequest.GET(BASE_PATH + "/cameras/list?max=1&order=desc&sort=name");
//        cameras = client.toBlocking().retrieve(request, Argument.of(List.class, Camera.class));
//
//        assertEquals(1, cameras.size());
//        assertEquals("alias1", cameras.get(0).getAlias());
//
//        request = HttpRequest.GET(BASE_PATH + "/cameras/list?max=1&offset=10");
//        cameras = client.toBlocking().retrieve(request, Argument.of(List.class, Camera.class));
//
//        assertEquals(0, cameras.size());

    }
    
    protected void deleteBySerial(String serial) {
        HttpRequest request = null;
        HttpResponse response = null;
    	request = HttpRequest.DELETE(BASE_PATH + "/cameras/"+serial);
        response = client.toBlocking().exchange(request);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    }

    protected String entityId(HttpResponse response) {
        String path = "/";
        String value = response.header(HttpHeaders.LOCATION);
        log.info("entityId, location:" + value);
        if ( value == null) {
            return null;
        }
        int index = value.indexOf(path);
        if ( index != -1) {
            return value.substring(index + path.length());
        }
        return null;
    }
}
