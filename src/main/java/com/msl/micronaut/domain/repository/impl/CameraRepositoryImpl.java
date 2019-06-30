package com.msl.micronaut.domain.repository.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.msl.micronaut.ApplicationConfiguration;
import com.msl.micronaut.domain.SortingAndOrderArguments;
import com.msl.micronaut.domain.entity.Camera;
import com.msl.micronaut.domain.repository.CameraRepository;

import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.spring.tx.annotation.Transactional;

public class CameraRepositoryImpl implements CameraRepository {

	@PersistenceContext
	private EntityManager entityManager;
	private final ApplicationConfiguration applicationConfiguration;

	public CameraRepositoryImpl(@CurrentSession EntityManager entityManager,
			ApplicationConfiguration applicationConfiguration) {
		this.entityManager = entityManager;
		this.applicationConfiguration = applicationConfiguration;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Camera> findById(@NotNull String serial) {
		return Optional.ofNullable(entityManager.find(Camera.class, serial));
	}
	
    @Override
    @Transactional 
    public Camera save(@NotBlank String serial, @NotBlank String id, @NotBlank String countryCode, @NotBlank String installationId, @NotBlank String zone,
    		@NotBlank String password, @NotBlank String alias, @NotBlank Date creationTime, @NotBlank Date lasUpdateTime, @NotBlank String vossServices) {
        Camera camera = new Camera(serial, id, countryCode, installationId, zone, password, alias, creationTime, lasUpdateTime, vossServices);
        entityManager.persist(camera);
        return camera;
    }
    
    @Override
    @Transactional
    public void deleteById(@NotNull String serial) {
        findById(serial).ifPresent(camera -> entityManager.remove(camera));
    }

    private final static List<String> VALID_PROPERTY_NAMES = Arrays.asList("serial", "id", "countryCode", "installationId", "zone", "password", "alias", 
    		"creationTime", "lasUpdateTime", "vossServices");
    
    @Transactional(readOnly = true)
    public List<Camera> findAll(@NotNull SortingAndOrderArguments args) {
        String qlString = "SELECT c FROM Camera as c";
        if (args.getOrder().isPresent() && args.getSort().isPresent() && VALID_PROPERTY_NAMES.contains(args.getSort().get())) {
                qlString += " ORDER BY c." + args.getSort().get() + " " + args.getOrder().get().toLowerCase();
        }
        TypedQuery<Camera> query = entityManager.createQuery(qlString, Camera.class);
        query.setMaxResults(args.getMax().orElseGet(applicationConfiguration::getMax));
        args.getOffset().ifPresent(query::setFirstResult);

        return query.getResultList();
    }

    @Override
    @Transactional
    public int update(@NotNull String serial, String id, String countryCode, String installationId, String zone,
			String password, String alias, Date creationTime, Date lasUpdateTime, String vossServices) {
        return entityManager.createQuery("UPDATE Camera c SET name = :name where serial = :serial")
                .setParameter("countryCode", countryCode)
                .setParameter("serial", serial)
                .setParameter("id", id)
                .setParameter("installationId", installationId)
                .setParameter("zone", zone)
                .setParameter("password", password)
                .setParameter("creationTime", creationTime)
                .setParameter("lasUpdateTime", lasUpdateTime)
                .setParameter("vossServices", vossServices)

                .executeUpdate();
    }

	public Optional<Camera> findByCountryCodeAndInstallationIdAndZone(String countryCode, String installationId,
			String zone) {
        String qlString = "SELECT c FROM Camera as c where countryCode = :countryCode and installationId = :installationId and zone = :zone";
        TypedQuery<Camera> query = entityManager.createQuery(qlString, Camera.class);
        return query.getResultStream().findFirst();
	}

	public List<Camera> findByCountryCodeAndInstallationId(String countryCode, String installationId) {
        String qlString = "SELECT c FROM Camera as c where countryCode = :countryCode and installationId = :installationId";
        TypedQuery<Camera> query = entityManager.createQuery(qlString, Camera.class);
        return query.getResultList();
	}

	public List<Camera> findByCountryCodeAndInstallationIdAndZoneStartingWith(String countryCode, String installationId,
			String zoneStarting) {
        String qlString = "SELECT c FROM Camera as c where countryCode = :countryCode and installationId = :installationId and zone like :zoneStarting%";
        TypedQuery<Camera> query = entityManager.createQuery(qlString, Camera.class);
        return query.getResultList();
	}

}
