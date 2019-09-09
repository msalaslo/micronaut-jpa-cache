package com.msl.micronaut.domain.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import io.micronaut.data.annotation.GeneratedValue;

@Entity
@Table(name = "CAMERAPOC")
//@MappedEntity(namingStrategy = NamingStrategies.UnderScoreSeparatedUpperCase.class)
public class Camera implements Serializable{
	
	public Camera() {
		
	}
	
	public Camera(String serial, int id, String countryCode, String installationId, String zone, String password, String alias, Date creationTime, Date lasUpdateTime, String vossServices) {
		this.setSerial(serial);
		this.setId(id);
		this.setCountryCode(countryCode);
		this.setInstallationId(installationId);
		this.setZone(zone);
		this.setPassword(getPassword());
		this.setAlias(alias);
		this.setCreationTime(creationTime);
		this.setLastUpdateTime(lastUpdateTime);
		this.setVossServices(vossServices);
	}

	private static final long serialVersionUID = 1L;

	public String serial;
	
	@GeneratedValue
	@SequenceGenerator(sequenceName = "CAMERAPOC1_SEQ", allocationSize = 1, name = "CAMERAPOC1_SEQ") 
	@Column(name = "ID")
	@Id
	public int id;
	
//	@MappedProperty(value = "COUNTRY_CODE", definition = "COUNTRY_CODE")
	@Column(name = "COUNTRY_CODE")
	public String countryCode;
//	@MappedProperty("INSTALATION_ID")
	@Column(name = "INSTALATION_ID")
	public String installationId;
	public String zone;
	public String password;
	public String alias;
//	@MappedProperty("CREATION_TIME")
	@Column(name = "CREATION_TIME")
	public Date creationTime;
//	@MappedProperty("LAST_UPDATE_TIME")
	@Column(name = "LAST_UPDATE_TIME")
	public Date lastUpdateTime;
//	@MappedProperty("VOSS_SERVICES")
	@Column(name = "VOSS_SERVICES")
	public String vossServices;
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getInstallationId() {
		return installationId;
	}
	public void setInstallationId(String installationId) {
		this.installationId = installationId;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getVossServices() {
		return vossServices;
	}
	public void setVossServices(String vossServices) {
		this.vossServices = vossServices;
	}

}
