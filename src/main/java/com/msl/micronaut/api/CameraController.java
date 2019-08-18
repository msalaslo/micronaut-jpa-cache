package com.msl.micronaut.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.validation.Valid;

import com.msl.micronaut.api.dto.CameraDTO;
import com.msl.micronaut.domain.service.CameraService;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.validation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Validated 
@Slf4j
@Controller("/correlation/v1.0/cameras")
public class CameraController {
	
	@Inject
    protected CameraService cameraService;

    @Get("/{serial}") 
    public CameraDTO findById(String serial) {
		log.info("Finding cameras by id (serial): {}", serial);

        return cameraService
                .findById(serial)
                .orElse(null); 
    }
    
	@Get("/")
	public HttpResponse<Iterable<CameraDTO>> findBy(String country, String installation, @Nullable String zone) {
		if (zone != null) {
			log.info("Finding cameras by country: {}, installation: {}, zone: {}", country, installation, zone);
			Optional<CameraDTO> camera = cameraService.findByCountryAndInstallationAndZone(country, installation, zone);
			if (camera.isPresent()) {
				List<CameraDTO> cameras = new ArrayList<CameraDTO>();
				cameras.add(camera.get());
				return  HttpResponse.ok(cameras);
			} else {
				return HttpResponse.notFound();
			}
		} else if (country != null && installation != null) {
			log.info("Finding cameras by country: {}, installation: {}", country, installation);
			return HttpResponse.ok(cameraService.findByCountryAndInstallation(country, installation));
		} else {
			return HttpResponse.badRequest();
		}
	}

    @Put("/") 
    public HttpResponse update(@Body @Valid CameraDTO camera, String serial) { 
        cameraService.update(camera, serial);
        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, location(camera.getSerial()).getPath()); 
    }
        
    @Get(value = "/page", produces = "application/json")
	@Operation(description = "Returns paged Cameras caching the camera keys (serial) and then retrieving the content from the individual cache")
	public HttpResponse<List<CameraDTO>> findAllCachedKeys(final Integer page, final Integer size, final String sort) {
		log.info("Finding all cameras page: {} and size {}", page, size);
		List<String> keys = cameraService.findAllKeys(page, size);
		List<CameraDTO> cameras = keys.stream().map(cameraService::findById).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
		return HttpResponse.ok(cameras);
	}

    @Post("/") 
    public HttpResponse<CameraDTO> save(@Body @Valid CameraDTO camera) {
        cameraService.put(camera);
        return HttpResponse
                .created(camera)
                .headers(headers -> headers.location(location(camera.getSerial())));
    }

    @Delete("/{serial}") 
    public HttpResponse delete(String serial) {
        cameraService.deleteById(serial);
        return HttpResponse.noContent();
    }

    protected URI location(String serial) {
        return URI.create("/" + serial);
    }

    protected URI location(CameraDTO camera) {
        return location(camera.getSerial());
    }
}
