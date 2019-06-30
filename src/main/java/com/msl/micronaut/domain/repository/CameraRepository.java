package com.msl.micronaut.domain.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
public interface CameraRepository {
	Optional<Camera> findById(@NotNull String serial);

	Camera save(@NotBlank String serial, @NotBlank String id, @NotBlank String countryCode,
			@NotBlank String installationId, @NotBlank String zone, @NotBlank String password, @NotBlank String alias,
			@NotBlank Date creationTime, @NotBlank Date lasUpdateTime, @NotBlank String vossServices);

	void deleteById(@NotNull String serial);

	List<Camera> findAll(@NotNull SortingAndOrderArguments args);

	int update(@NotNull String serial, String id, String countryCode, String installationId, String zone,
			String password, String alias, Date creationTime, Date lasUpdateTime, String vossServices);

	public Optional<Camera> findByCountryCodeAndInstallationIdAndZone(String countrCode, String installationId,
			String zone);

	public List<Camera> findByCountryCodeAndInstallationId(String countrCode, String installationId);

	public List<Camera> findByCountryCodeAndInstallationIdAndZoneStartingWith(String country, String installation,
			String zoneStarting);
}
