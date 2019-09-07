package com.msl.micronaut.api.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Sample DTO object. <b>Please remove for actual project implementation.</b>
 *
 * @since 1.0.0
 * @author FaaS [faas@securitasdirect.es]
 *
 */
public class CameraDTO extends BaseDTO implements Serializable {
	
	public CameraDTO() {
		
	}
	
	public CameraDTO(String serial, int id, String countryCode, String installationId, String zone, String alias, Date creationTime, Date lasUpdateTime, String vossServices, String password) {
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

    @Schema(description = "serial, the identifier", required = true)
	public String serial;

    @Schema(description = "id autoincremental", required = true)
	public int id;

    @Schema(description = "Country code", required = true)
	public String countryCode;

    @Schema(description = "Installation id", required = true)
	public String installationId;

    @Schema(description = "Zone", required = true)
	public String zone;

    @Schema(description = "Alias", required = true)
    public String alias;

    @Schema(description = "Creation time", required = true)
	public Date creationTime;

    @Schema(description = "Last update time", required = true)
	public Date lastUpdateTime;

    @Schema(description = "Number of VOSS devices", required = true)
	public String vossServices;
    
    @Schema(description = "password", required = false)
	public String password;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
