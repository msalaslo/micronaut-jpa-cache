package com.msl.micronaut.domain.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.inject.Singleton;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.msl.micronaut.domain.SortingAndOrderArguments;
import com.msl.micronaut.domain.entity.Camera;

/**
 * Camera DAO/Repository
 * 
 * @since 1.0.0
 * @author Miguel Salas
 */
@Singleton
public interface CameraRepository {
	public Optional<Camera> findById(@NotNull String serial);

	public Camera save(@NotBlank String serial, @NotBlank int id, @NotBlank String countryCode,
			@NotBlank String installationId, @NotBlank String zone, @NotBlank String password, @NotBlank String alias,
			@NotBlank Date creationTime, @NotBlank Date lasUpdateTime, @NotBlank String vossServices);

	public Camera save(@NotBlank Camera camera);

	public void deleteById(@NotNull String serial);

	public List<Camera> findAll(@NotNull SortingAndOrderArguments args);
	
	public List<String> findAllKeys(@NotNull SortingAndOrderArguments args);

	public int update(@NotNull String serial, String id, String countryCode, String installationId, String zone,
			String password, String alias, Date creationTime, Date lasUpdateTime, String vossServices);

	public Camera findBySerial(String serial);

	public Camera findByCountryCodeAndInstallationIdAndZone(String countryCode, String installationId,
			String zone);

	public List<Camera> findByCountryCodeAndInstallationId(String countryCode, String installationId);

	public List<Camera> findByCountryCodeAndInstallationIdAndZoneStartingWith(String countryCode, String installationId,
			String zoneStarting);
	
	public List<Camera> findByZoneStartingWith(String zoneStarting, @NotNull SortingAndOrderArguments args);
	
	public long count();
}
