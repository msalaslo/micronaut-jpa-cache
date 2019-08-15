package com.msl.micronaut.domain.service;

import java.util.Optional;

import com.msl.micronaut.api.dto.CameraDTO;
import com.msl.micronaut.api.dto.PageDTO;

public interface CameraService {
	public CameraDTO put(CameraDTO camera);
	public Optional<CameraDTO> findById(String id);
	public Iterable<CameraDTO> findByCountryAndInstallation(String country, String installation);
	public Optional<CameraDTO> findByCountryAndInstallationAndZone(String country, String installation, String zone);	
	public CameraDTO update(CameraDTO camera, String id);
	public CameraDTO updateInRepository(CameraDTO camera, String id);
	public void deleteById(String id);
	public void evictAllCacheValues();
	public PageDTO<CameraDTO> findAll(int page, int pageSize);
	public PageDTO<CameraDTO> findAllNoCache(int page, int pageSize);
//	public List<String> findAllKeys(int page, int pageSize);
	public PageDTO<CameraDTO> findAllVoss(int page, int pageSize);
	public Iterable<CameraDTO> findVossDevicesByCountryAndInstallation(String country, String installation);
	public long count();
}
