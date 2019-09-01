package com.msl.micronaut.domain.repository;

import java.util.List;

import com.msl.micronaut.domain.entity.Camera;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.PageableRepository;

/**
 * Camera DAO/Repository
 *
 * @since 1.0.0
 * @author Miguel Salas
 */
@Repository
public interface CameraRepositoryData extends PageableRepository<Camera, String>{
	public Camera findBySerial(String serial);
	public Camera findByCountryCodeAndInstallationIdAndZone(String countryCode, String installationId, String zone);
	public List<Camera> findByCountryCodeAndInstallationId(String countryCode, String installationId);
	public Page<Camera> findByZoneStartingWith(String zoneStarting, Pageable pageable);
	public List<Camera> findByCountryCodeAndInstallationIdAndZoneStartingWith(String countryCode, String installationId, String zoneStarting);
//	@Query(value = "SELECT c.serial FROM Camera c")
//	public List<String> findAllKeysWithPagination(Pageable pageable);
}
