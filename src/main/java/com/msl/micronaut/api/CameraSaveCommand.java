package com.msl.micronaut.api;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CameraSaveCommand {
	@NotNull
	public String serial;
	public String id;
	@NotNull
	public String countryCode;
	@NotNull
	public String installationId;
	@NotNull
	public String zone;
	public String password;
	public String alias;
	public Date creationTime;
	public Date lastUpdateTime;
	public String vossServices;
}
