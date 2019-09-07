package com.msl.micronaut.api.dto;

import java.io.Serializable;
import java.util.Date;

import io.micronaut.core.annotation.Introspected;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Sample DTO object. <b>Please remove for actual project implementation.</b>
 *
 * @since 1.0.0
 * @author FaaS [faas@securitasdirect.es]
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CameraDTOLombok extends BaseDTO implements Serializable {

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
}
