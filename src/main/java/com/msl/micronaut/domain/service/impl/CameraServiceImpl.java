package com.msl.micronaut.domain.service.impl;

import java.util.ArrayList;
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

	//@Cacheable(value = "cameras-all", cacheManager = "cacheManager", unless = "#result == null")
	@Cacheable(value = "cameras-all")
	public PageDTO<CameraDTO> findAll(int page, int pageSize) {
		log.info("findAll");
		return findAllNoCache(page, pageSize);
	}

	public PageDTO<CameraDTO> findAllNoCache(int page, int pageSize) {
		log.info("findAllNocache");
		PageDTO<CameraDTO> camerasDtoPage = findAllForSpringAndMicronautData(page, pageSize);
//		PageDTO<CameraDTO> camerasDtoPage = findAllForMicronautJDBC(page, pageSize);
		
		return camerasDtoPage;
	}
	
	private Page<Camera> findPage(int page, int pageSize) {
		Pageable pageable = Pageable.from(page, pageSize);
		return repository.findAll(pageable);
	}
	
	private PageDTO<CameraDTO> findAllForSpringAndMicronautData(int page, int pageSize) {
		Page<Camera> cameraPage = findPage(page, pageSize);
		PageDTO<CameraDTO> camerasDtoPage = cameraConverter.toPageCameraDto(null, cameraPage);
		return camerasDtoPage;
	}
	
//	private PageDTO<CameraDTO> findAllForMicronautJDBC(int page, int pageSize) {
//		SortingAndOrderArguments sortingAndOrderArgs = new SortingAndOrderArguments();		
//		List<Camera> cameraPage = repository.findAll(sortingAndOrderArgs);
//		PageDTO<CameraDTO> camerasDtoPage = cameraConverter.toPageCameraDto(cameraPage, page);
//		return camerasDtoPage;
//	}
	
	

//	@Cacheable(value = "cameras-all-keys", cacheManager = "cacheManager", unless = "#result == null")
	@Cacheable(value = "cameras-all-keys")
	public List<String> findAllKeys(int page, int pageSize) {
		log.info("findAllKeys");
		return findAllKeysForSpringAndMicronautData(page, pageSize);
//		return findAllKeysForMicronautJDBC(page, pageSize);
	}
	
	private List<String> findAllKeysForSpringAndMicronautData(int page, int pageSize) {
		log.info("findAllKeysForSpringAndMicronautData");
		Page<Camera> cameraPage = findPage(page, pageSize);
		//workarounf for micronaut
		List<String> cameraKeysPage = new ArrayList<String>();
		cameraPage.forEach(camera -> cameraKeysPage.add(camera.getSerial()));
		return cameraKeysPage;
	}
	
//	private List<String> findAllKeysForMicronautJDBC(int page, int pageSize) {
//		log.info("findAllKeysForMicronautJDBC");		
//		SortingAndOrderArguments sortingAndOrderArgs = new SortingAndOrderArguments();		
//		return repository.findAllKeys(sortingAndOrderArgs);
//	}
	
	

//	@Cacheable(value = "cameras-by-country-and-installation-and-zone", key = "#country + #installation + #zone", cacheManager = "cacheManager", unless = "#result == null")
	@Cacheable(value = "cameras-by-country-and-installation-and-zone")
	public Optional<CameraDTO> findByCountryAndInstallationAndZone(String country, String installation, String zone) {
		log.info("findBy country {}, installation {}, zone{}:", country, installation, zone);
//		Optional<Camera> camera = repository.findByCountryCodeAndInstallationIdAndZone(country, installation, zone);
		Camera camera = repository.findByCountryCodeAndInstallationIdAndZone(country, installation, zone);
		Optional<Camera> optionalCamera = Optional.of(camera);
		return cameraConverter.toOptionalCameraDto(optionalCamera);
	}

//	@Cacheable(value = "cameras-by-country-and-installation", key = "#country + #installation", cacheManager = "cacheManager", unless = "#result == null or #result.size()==0")
	@Cacheable(value = "cameras-by-country-and-installation")
	public List<CameraDTO> findByCountryAndInstallation(String country, String installation) {
		log.info("findBy country {}, installation {}", country, installation);
		List<Camera> cameras = repository.findByCountryCodeAndInstallationId(country, installation);
		return cameraConverter.toListCameraDto(cameras);
	}

//	@Cacheable(value = "cameras-by-serial", key = "#serial", cacheManager = "cacheManager", unless = "#result == null")
	@Cacheable(value = "cameras-by-serial")
	public Optional<CameraDTO> findBySerial(String serial) {
		log.info("findBySerial:" + serial);
		Camera camera = repository.findBySerial(serial);
		log.info("findBySerial, camera found:" + camera);
		return cameraConverter.toOptionalCameraDto(camera);
	}

//	@Cacheable(value = "voss-all", cacheManager = "cacheManager", unless = "#result == null")
	@Cacheable(value = "voss-all")
	public PageDTO<CameraDTO> findAllVoss(int page, int pageSize) {
		log.info("findVossDevices, zone starts with VS");
		return findAllVossForSptringAndMicronautData(page, pageSize);
//		return findAllVossForMicronautJDBC(page, pageSize);
	}
	
//	private PageDTO<CameraDTO> findAllVossForMicronautJDBC(int page, int pageSize) {
//		log.info("findAllVossForMicronautJDBC");
//		SortingAndOrderArguments args = new SortingAndOrderArguments();
//		args.setMax(page);
//		args.setOffset(pageSize);
//		List<Camera> cameraPage = repository.findByZoneStartingWith("VS", args);
//		PageDTO<CameraDTO> camerasDtoPage = cameraConverter.toPageCameraDto(cameraPage, page);
//		return camerasDtoPage;
//	}
	
	private PageDTO<CameraDTO> findAllVossForSptringAndMicronautData(int page, int pageSize) {
		log.info("findAllVossForSptringAndMicronautData");
		Pageable pageable = Pageable.from(page, pageSize);
		Page<Camera> cameraPage = repository.findByZoneStartingWith("VS", pageable);
		PageDTO<CameraDTO> camerasDtoPage = cameraConverter.toPageCameraDto(null, cameraPage);
		return camerasDtoPage;
	}

//	@Cacheable(value = "voss-by-country-and-installation", key = "#country + #installation", cacheManager = "cacheManager", unless = "#result == null or #result.size()==0")
	@Cacheable(value = "voss-by-country-and-installation")
	public List<CameraDTO> findVossDevicesByCountryAndInstallation(String country, String installation) {
		log.info("findVossDevices, zone starts with VS and country is {} and installation is {}", country,
				installation);
		List<Camera> cameras = repository.findByCountryCodeAndInstallationIdAndZoneStartingWith(country, installation, "VS");
		return cameraConverter.toListCameraDto(cameras);
	}

	@PutOperations({
//			@CachePut(value = "cameras-by-country-and-installation-and-zone", key = "#camera.countryCode + #camera.installationId + #camera.zone", cacheManager = "cacheManager", unless = "#result == null"),
//			@CachePut(value = "cameras-by-serial", key = "#camera.serial", cacheManager = "cacheManager", unless = "#result == null") })
			@CachePut(value = "cameras-by-country-and-installation-and-zone"),
			@CachePut(value = "cameras-by-serial") })
	public CameraDTO put(CameraDTO camera) {
		log.info("PUT::This method does not create the object in the database, only has been cached:" + camera);
		return camera;
	}

//	@CachePut(value = "cameras-by-country-and-installation", key = "#camera.countryCode + #camera.installationId", cacheManager = "cacheManager", unless = "#result == null or #result.size()==0") })
	@CachePut(value = "cameras-by-country-and-installation")
	public Iterable<CameraDTO> putByCountryAndInstallation(Iterable<CameraDTO> cameras) {
		log.info("PUT::This method does not create the object in the database, only has been cached:" + cameras);
		return cameras;
	}

//		@CachePut(value = "voss-by-country-and-installation", key = "#camera.countryCode + #camera.installationId", cacheManager = "cacheManager", condition = "#camera.vossServices != null") })
		@CachePut(value = "voss-by-country-and-installation")
	public Iterable<CameraDTO> putVosses(Iterable<CameraDTO> cameras) {
		log.info("PUT::This method does not create the object in the database, only has been cached:" + cameras);
		return cameras;
	}

//	@CachePut(key = "#id", value = "cameras-by-serial", cacheManager = "cacheManager", unless = "#result == null")
	@CachePut(value = "cameras-by-serial")
	public CameraDTO update(CameraDTO camera, String serial) {
		log.info("This method does not integrate with the database, update camera {} with id {}:", camera, serial);
		return camera;
	}

	@CacheInvalidate(value = "cameras-by-serial")
	public void deleteById(String id) {
		log.info("This method does not integrate with the database, deleteById:" + id);
	}

//	@CachePut(key = "#id", value = "cameras-by-serial", cacheManager = "cacheManager", unless = "#result == null")
	@CachePut(value = "cameras-by-serial")
	public CameraDTO updateInRepository(CameraDTO camera, String serial) {
		log.info("update camera {} with serial {}:", camera, serial);
		Camera cameraEntity = cameraConverter.toCameraEntity(camera);
		Camera newCamera = repository.findBySerial(serial);
		if(newCamera != null){
			//Setting the values that are not exposed to the external APIs
			cameraEntity.setPassword(newCamera.getPassword());
//			cameraEntity.setIdentificador(newCamera.getIdentificador());
			log.info("updateInRepository, camera to insert {}:", cameraEntity );
			Camera newCameraEntity = repository.save(cameraEntity);
			return cameraConverter.toCameraDto(newCameraEntity);
		}else{
			return null;
		}
	}
	
	@CachePut(value = "cameras-by-serial")
	public CameraDTO insertInRepository(CameraDTO camera, String serial) {
		log.info("update camera {} with serial {}:", camera, serial);
		Camera cameraEntity = cameraConverter.toCameraEntity(camera);
		Camera newCameraEntity = repository.save(cameraEntity);
		return cameraConverter.toCameraDto(newCameraEntity);
	}

	@CacheInvalidate(value = "cameras-by-serial")
	public void deleteByIdInRepository(String id) {
		log.info("deleteById:" + id);
		repository.deleteById(id);
//		repository.deleteBySerial(id);
	}


	@InvalidateOperations({
//			@CacheInvalidate(value = "cameras-by-country-and-installation-and-zone", allEntries = true, cacheManager = "cacheManager"),
//			@CacheInvalidate(value = "cameras-by-country-and-installation", allEntries = true, cacheManager = "cacheManager"),
//			@CacheInvalidate(value = "cameras-by-serial", allEntries = true, cacheManager = "cacheManager"),
//			@CacheInvalidate(value = "cameras-all", allEntries = true, cacheManager = "cacheManager"),
//			@CacheInvalidate(value = "voss-by-country-and-installation", allEntries = true, cacheManager = "cacheManager"),
//			@CacheInvalidate(value = "voss-all", allEntries = true, cacheManager = "cacheManager")
			@CacheInvalidate(value = "cameras-by-country-and-installation-and-zone"),
			@CacheInvalidate(value = "cameras-by-country-and-installation"),
			@CacheInvalidate(value = "cameras-by-serial"),
			@CacheInvalidate(value = "cameras-all"),
			@CacheInvalidate(value = "voss-by-country-and-installation"),
			@CacheInvalidate(value = "voss-all")
		})
	@Override
	public void evictAllCacheValues() {}

	@Override
	public long count() {
		log.info("count");
		return repository.count();
	}
}
