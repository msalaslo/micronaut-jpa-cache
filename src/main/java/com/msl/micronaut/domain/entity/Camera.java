package com.msl.micronaut.domain.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import io.micronaut.data.model.naming.NamingStrategies;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "CAMERAPOC1")
@MappedEntity(namingStrategy = NamingStrategies.UnderScoreSeparatedUpperCase.class)
public class Camera implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	public String serial;
	
	@GeneratedValue
	@SequenceGenerator(sequenceName = "CAMERAPOC1_SEQ", allocationSize = 1, name = "CAMERAPOC1_SEQ") 
	@MappedProperty("ID")
	public int identificador;
	
//	@MappedProperty(value = "COUNTRY_CODE", definition = "COUNTRY_CODE")
	public String countryCode;
//	@MappedProperty("INSTALATION_ID")
	public String installationId;
	public String zone;
	public String password;
	public String alias;
//	@MappedProperty("CREATION_TIME")
	public Date creationTime;
//	@MappedProperty("LAST_UPDATE_TIME")
	public Date lastUpdateTime;
//	@MappedProperty("VOSS_SERVICES")
	public String vossServices;

}
