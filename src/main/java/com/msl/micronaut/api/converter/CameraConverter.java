package com.msl.micronaut.api.converter;

import java.util.List;
import java.util.Optional;

import org.mapstruct.Mapper;

import com.msl.micronaut.api.dto.CameraDTO;
import com.msl.micronaut.api.dto.PageDTO;
import com.msl.micronaut.domain.entity.Camera;

import io.micronaut.data.model.Page;

/**
 * Camera converter
 *
 * @since 1.0.0
 * @author FaaS [faas@securitasdirect.es]
 */
@Mapper(componentModel = "jsr330")
public abstract class CameraConverter {
	public abstract CameraDTO toCameraDto(Camera camera);
	
	//Implementamos este mapeo ya que de momento mapstruct no soporta Optional (busca un contructor sin parametros de Optional)
	public Optional<CameraDTO> toOptionalCameraDto(Optional<Camera> camera){
		if(camera.isPresent()) {
			return Optional.of(toCameraDto(camera.get()));
		}else {
			return Optional.empty();
		}
	}
	
	public abstract List<CameraDTO> toListCameraDto(List<Camera> cameras);
	
    //Void workaround: https://github.com/mapstruct/mapstruct/issues/661
	public abstract PageDTO<CameraDTO> toPageCameraDto(Void workaround, Page<Camera> cameras);
    
	public abstract Camera toCameraEntity(CameraDTO camera);
}