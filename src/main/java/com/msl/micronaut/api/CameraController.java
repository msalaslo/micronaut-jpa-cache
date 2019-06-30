package com.msl.micronaut.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;
import javax.validation.Valid;

import com.msl.micronaut.domain.SortingAndOrderArguments;
import com.msl.micronaut.domain.entity.Camera;
import com.msl.micronaut.domain.repository.CameraRepository;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.validation.Validated;
import lombok.extern.slf4j.Slf4j;

@Validated 
@Slf4j
@Controller("/cameras")
public class CameraController {
    protected final CameraRepository cameraRepository;

    public CameraController(CameraRepository cameraRepository) { 
        this.cameraRepository = cameraRepository;
    }

    @Get("/{serial}") 
    public Camera findById(String serial) {
		log.info("Finding cameras by id (serial): {}", serial);

        return cameraRepository
                .findById(serial)
                .orElse(null); 
    }
    
	@Get("/")
	public HttpResponse<Iterable<Camera>> findBy(String country, String installation, @Nullable String zone) {
		if (zone != null) {
			log.info("Finding cameras by country: {}, installation: {}, zone: {}", country, installation, zone);
			Optional<Camera> camera = cameraRepository.findByCountryCodeAndInstallationIdAndZone(country, installation, zone);
			if (camera.isPresent()) {
				List<Camera> cameras = new ArrayList<Camera>();
				cameras.add(camera.get());
				return  HttpResponse.ok(cameras);
			} else {
				return HttpResponse.notFound();
			}
		} else if (country != null && installation != null) {
			log.info("Finding cameras by country: {}, installation: {}", country, installation);
			return HttpResponse.ok(cameraRepository.findByCountryCodeAndInstallationId(country, installation));
		} else {
			return HttpResponse.badRequest();
		}
	}

    @Put("/") 
    public HttpResponse update(@Body @Valid CameraUpdateCommand cmd) { 
        int numberOfEntitiesUpdated = cameraRepository.update(cmd.getSerial(), cmd.getId(), cmd.getCountryCode(), cmd.getInstallationId(), 
        		cmd.getZone(), cmd.getPassword(), cmd.getAlias(),
        		cmd.getCreationTime(), cmd.getLastUpdateTime(), cmd.getVossServices());

        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, location(cmd.getSerial()).getPath()); 
    }

    @Get(value = "/list{?args*}") 
    public List<Camera> list(@Valid SortingAndOrderArguments args) {
        return cameraRepository.findAll(args);
    }

    @Post("/") 
    public HttpResponse<Camera> save(@Body @Valid CameraSaveCommand cmd) {
        Camera camera = cameraRepository.save(cmd.getSerial(), cmd.getId(), cmd.getCountryCode(), cmd.getInstallationId(), 
        		cmd.getZone(), cmd.getPassword(), cmd.getAlias(),
        		cmd.getCreationTime(), cmd.getLastUpdateTime(), cmd.getVossServices());

        return HttpResponse
                .created(camera)
                .headers(headers -> headers.location(location(camera.getSerial())));
    }

    @Delete("/{serial}") 
    public HttpResponse delete(String serial) {
        cameraRepository.deleteById(serial);
        return HttpResponse.noContent();
    }

    protected URI location(String serial) {
        return URI.create("/cameras/" + serial);
    }

    protected URI location(Camera camera) {
        return location(camera.getSerial());
    }
}
