package com.msl.micronaut.domain.service.impl;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.msl.micronaut.api.converter.CameraConverter;
import com.msl.micronaut.api.dto.CameraDTO;
import com.msl.micronaut.api.dto.PageDTO;
import com.msl.micronaut.domain.entity.Camera;
import com.msl.micronaut.domain.repository.CameraRepositoryData;
import com.msl.micronaut.domain.service.CameraService;

import io.micronaut.cache.annotation.CacheInvalidate;
import io.micronaut.cache.annotation.CachePut;
import io.micronaut.cache.annotation.Cacheable;
import io.micronaut.cache.annotation.InvalidateOperations;
import io.micronaut.cache.annotation.PutOperations;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class CameraServiceImpl implements CameraService {

	@Inject
	CameraRepositoryData repository;

	@Inject
	CameraConverter cameraConverter;
	
	//@Cacheable(value = "cameras/all", cacheManager = "cacheManager", unless = "#result == null")
	@Cacheable(value = "cameras/all")
	public PageDTO<CameraDTO> findAll(int page, int pageSize) {
		log.info("findAll");
		return findAllNoCache(page, pageSize);
	}
	
	public PageDTO<CameraDTO> findAllNoCache(int page, int pageSize) {
		log.info("findAllNocache");
		Pageable pageable = Pageable.from(page, pageSize);
		Page<Camera> cameraPage = repository.findAll(pageable);
		PageDTO<CameraDTO> camerasDtoPage = cameraConverter.toPageCameraDto(null, cameraPage);
		return camerasDtoPage;
	}
	
//	@Cacheable(value = "cameras/allKeys", cacheManager = "cacheManager", unless = "#result == null")
	@Cacheable(value = "cameras/allKeys")
	public List<String> findAllKeys(int page, int pageSize) {
		log.info("findAllKeys");
		Pageable pageable = Pageable.from(page, pageSize);
		List<String> cameraKeysPage = repository.findAllKeysWithPagination(pageable);
		return cameraKeysPage;
	}

//	@Cacheable(value = "cameras/ByCountryAndInstallationAndZone", key = "#country + #installation + #zone", cacheManager = "cacheManager", unless = "#result == null")
	@Cacheable(value = "cameras/ByCountryAndInstallationAndZone")
	public Optional<CameraDTO> findByCountryAndInstallationAndZone(String country, String installation, String zone) {
		log.debug("findBy country {}, installation {}, zone{}:", country, installation, zone);
//		Optional<Camera> camera = repository.findByCountryCodeAndInstallationIdAndZone(country, installation, zone);
		Camera camera = repository.findByCountryCodeAndInstallationIdAndZone(country, installation, zone);
		Optional<Camera> optionalCamera = Optional.of(camera);		
		return cameraConverter.toOptionalCameraDto(optionalCamera);
	}

//	@Cacheable(value = "cameras/ByCountryAndInstallation", key = "#country + #installation", cacheManager = "cacheManager", unless = "#result == null or #result.size()==0")
	@Cacheable(value = "cameras/ByCountryAndInstallation")
	public List<CameraDTO> findByCountryAndInstallation(String country, String installation) {
		log.debug("findBy country {}, installation {}", country, installation);
		List<Camera> cameras = repository.findByCountryCodeAndInstallationId(country, installation);
		return cameraConverter.toListCameraDto(cameras);
	}

//	@Cacheable(value = "cameras/BySerial", key = "#id", cacheManager = "cacheManager", unless = "#result == null")
	@Cacheable(value = "cameras/BySerial")
	public Optional<CameraDTO> findById(String id) {
		log.debug("findById:" + id);
		Optional<Camera> camera = repository.findById(id);
		return cameraConverter.toOptionalCameraDto(camera);
	}

//	@Cacheable(value = "voss/all", cacheManager = "cacheManager", unless = "#result == null")
	@Cacheable(value = "voss/all")
	public PageDTO<CameraDTO> findAllVoss(int page, int pageSize) {
		log.debug("findVossDevices, zone starts with VS");
		Pageable pageable = Pageable.from(page, pageSize);
		Page<Camera> cameraPage = repository.findByZoneStartingWith("VS", pageable);
		PageDTO<CameraDTO> camerasDtoPage = cameraConverter.toPageCameraDto(null, cameraPage);
		return camerasDtoPage;
	}

//	@Cacheable(value = "voss/ByCountryAndInstallation", key = "#country + #installation", cacheManager = "cacheManager", unless = "#result == null or #result.size()==0")
	@Cacheable(value = "voss/ByCountryAndInstallation")
	public List<CameraDTO> findVossDevicesByCountryAndInstallation(String country, String installation) {
		log.debug("findVossDevices, zone starts with VS and country is {} and installation is {}", country,
				installation);
		List<Camera> cameras = repository.findByCountryCodeAndInstallationIdAndZoneStartingWith(country, installation, "VS");
		return cameraConverter.toListCameraDto(cameras);
	}

	@PutOperations({
//			@CachePut(value = "cameras/ByCountryAndInstallationAndZone", key = "#camera.countryCode + #camera.installationId + #camera.zone", cacheManager = "cacheManager", unless = "#result == null"),
//			@CachePut(value = "cameras/BySerial", key = "#camera.serial", cacheManager = "cacheManager", unless = "#result == null") })
			@CachePut(value = "cameras/ByCountryAndInstallationAndZone"),
			@CachePut(value = "cameras/BySerial") })
	public CameraDTO put(CameraDTO camera) {
		log.info("PUT::This method does not create the object in the database, only has been cached:" + camera);
		return camera;
	}

//	@CachePut(value = "cameras/ByCountryAndInstallation", key = "#camera.countryCode + #camera.installationId", cacheManager = "cacheManager", unless = "#result == null or #result.size()==0") })
	@CachePut(value = "cameras/ByCountryAndInstallation")
	public Iterable<CameraDTO> putByCountryAndInstallation(Iterable<CameraDTO> cameras) {
		log.info("PUT::This method does not create the object in the database, only has been cached:" + cameras);
		return cameras;
	}
	
//		@CachePut(value = "voss/ByCountryAndInstallation", key = "#camera.countryCode + #camera.installationId", cacheManager = "cacheManager", condition = "#camera.vossServices != null") })
		@CachePut(value = "voss/ByCountryAndInstallation")
		public Iterable<CameraDTO> putVosses(Iterable<CameraDTO> cameras) {
		log.info("PUT::This method does not create the object in the database, only has been cached:" + cameras);
		return cameras;
	}

//	@CachePut(key = "#id", value = "cameras/BySerial", cacheManager = "cacheManager", unless = "#result == null")
	@CachePut(value = "cameras/BySerial")
	public CameraDTO update(CameraDTO camera, String id) {
		log.debug("This method does not integrate with the database, update camera {} with id {}:", camera, id);
		return repository.findById(id).map(newCamera -> {
			camera.setSerial(camera.getSerial());
			return camera;
		}).orElseGet(() -> {
			camera.setId(id);
			return camera;
		});
	}

	@CacheInvalidate(value = "cameras/BySerial")
	public void deleteById(String id) {
		log.debug("This method does not integrate with the database, deleteById:" + id);
	}

//	@CachePut(key = "#camera.id", value = "cameras/BySerial", cacheManager = "cacheManager")
	@CachePut(value = "cameras/BySerial")
	public Camera createInRepository(Camera camera) {
		log.debug("create:" + camera);
		return repository.save(camera);
	}

//	@CachePut(key = "#id", value = "cameras/BySerial", cacheManager = "cacheManager", unless = "#result == null")
	@CachePut(value = "cameras/BySerial")
	public CameraDTO updateInRepository(CameraDTO camera, String id) {
		log.debug("update camera {} with id {}:", camera, id);
		Camera cameraEntity = cameraConverter.toCameraEntity(camera);
		return repository.findById(id).map(newCamera -> {
			cameraEntity.setPassword(newCamera.getPassword());
			Camera newCameraEntity = repository.save(cameraEntity);
			return cameraConverter.toCameraDto(newCameraEntity);
		}).orElseGet(() -> {
			return null;
		});
	}

	@CacheInvalidate(value = "cameras/BySerial")
	public void deleteByIdInRepository(String id) {
		log.debug("deleteById:" + id);
		repository.deleteById(id);
	}
	
	
	@InvalidateOperations({
//			@CacheInvalidate(value = "cameras/ByCountryAndInstallationAndZone", allEntries = true, cacheManager = "cacheManager"),
//			@CacheInvalidate(value = "cameras/ByCountryAndInstallation", allEntries = true, cacheManager = "cacheManager"),
//			@CacheInvalidate(value = "cameras/BySerial", allEntries = true, cacheManager = "cacheManager"),
//			@CacheInvalidate(value = "cameras/all", allEntries = true, cacheManager = "cacheManager"),
//			@CacheInvalidate(value = "voss/ByCountryAndInstallation", allEntries = true, cacheManager = "cacheManager"),
//			@CacheInvalidate(value = "voss/all", allEntries = true, cacheManager = "cacheManager")
			@CacheInvalidate(value = "cameras/ByCountryAndInstallationAndZone"),
			@CacheInvalidate(value = "cameras/ByCountryAndInstallation"),
			@CacheInvalidate(value = "cameras/BySerial"),
			@CacheInvalidate(value = "cameras/all"),
			@CacheInvalidate(value = "voss/ByCountryAndInstallation"),
			@CacheInvalidate(value = "voss/all")
		})
	@Override
	public void evictAllCacheValues() {}

	@Override
	public long count() {
		log.debug("count");
		return repository.count();
	}
}
