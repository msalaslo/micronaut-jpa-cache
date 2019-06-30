package com.msl.micronaut.domain.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
	public String id;
	public String countryCode;
	public String installationId;
	public String zone;
	public String password;
	public String alias;
	public Date creationTime;
	public Date lastUpdateTime;
	public String vossServices;

}
