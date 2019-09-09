package com.msl.micronaut.domain.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import io.micronaut.data.annotation.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "CAMERAPOC")
public class Camera implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	public String serial;
	
	@GeneratedValue
	@SequenceGenerator(sequenceName = "CAMERAPOC_SEQ", allocationSize = 1, name = "CAMERAPOC_SEQ") 
	@Column(name = "ID")
	public int id;
	
	@Column(name = "COUNTRY_CODE")
	public String countryCode;
	@Column(name = "INSTALLATION_ID")
	public String installationId;
	public String zone;
	public String password;
	public String alias;
	@Column(name = "CREATION_TIME")
	public Date creationTime;
	@Column(name = "LAST_UPDATE_TIME")
	public Date lastUpdateTime;
	@Column(name = "VOSS_SERVICES")
	public String vossServices;

}
