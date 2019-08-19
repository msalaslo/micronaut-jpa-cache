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
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	@Transactional(readOnly = true)
	public Camera findBySerial(String serial) {
		return entityManager.find(Camera.class, serial);
	}
	
    @Override
    @Transactional 
    public Camera save(@NotBlank String serial, @NotBlank int id, @NotBlank String countryCode, @NotBlank String installationId, @NotBlank String zone,
    		@NotBlank String password, @NotBlank String alias, @NotBlank Date creationTime, @NotBlank Date lasUpdateTime, @NotBlank String vossServices) {
        Camera camera = new Camera(serial, id, countryCode, installationId, zone, password, alias, creationTime, lasUpdateTime, vossServices);
        entityManager.persist(camera);
        return camera;
    }
    
    @Override
    @Transactional 
    public Camera save(@NotBlank Camera camera) {
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
    
    @Transactional(readOnly = true)
    public List<String> findAllKeys(@NotNull SortingAndOrderArguments args) {
        String qlString = "SELECT c.serial FROM Camera as c";
        if (args.getOrder().isPresent() && args.getSort().isPresent() && VALID_PROPERTY_NAMES.contains(args.getSort().get())) {
                qlString += " ORDER BY c." + args.getSort().get() + " " + args.getOrder().get().toLowerCase();
        }
        TypedQuery<String> query = entityManager.createQuery(qlString, String.class);
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

    @Override
    @Transactional(readOnly = true)
	public Camera findByCountryCodeAndInstallationIdAndZone(String countryCode, String installationId,
			String zone) {
		log.info("Finding cameras findByCountryCodeAndInstallationIdAndZone::countryCode: {}, installationId: {}, zone: {}", countryCode, installationId, zone);

        String qlString = "SELECT c FROM Camera as c where countryCode = :countryCode and installationId = :installationId and zone = :zone";
        TypedQuery<Camera> query = entityManager.createQuery(qlString, Camera.class)
                .setParameter("countryCode", countryCode)
                .setParameter("installationId", installationId)
                .setParameter("zone", zone);
        return query.getResultStream().findFirst().get();
	}

    @Override
    @Transactional(readOnly = true)
	public List<Camera> findByCountryCodeAndInstallationId(String countryCode, String installationId) {
		log.info("Finding cameras findByCountryCodeAndInstallationId::countryCode: {}, installationId: {}: {}", countryCode, installationId);

        String qlString = "SELECT c FROM Camera as c where countryCode = :countryCode and installationId = :installationId";
        TypedQuery<Camera> query = entityManager.createQuery(qlString, Camera.class)
                .setParameter("countryCode", countryCode)
                .setParameter("installationId", installationId);
        return query.getResultList();
	}

    @Override
    @Transactional(readOnly = true)
	public List<Camera> findByCountryCodeAndInstallationIdAndZoneStartingWith(String countryCode, String installationId,
			String zoneStarting) {
        String qlString = "SELECT c FROM Camera as c where countryCode = :countryCode and installationId = :installationId and zone like :zoneStarting%";
        TypedQuery<Camera> query = entityManager.createQuery(qlString, Camera.class)
                .setParameter("countryCode", countryCode)
                .setParameter("installationId", installationId)
                .setParameter("zoneStarting", zoneStarting);
        return query.getResultList();
	}
    
    @Override
    @Transactional(readOnly = true)
	public List<Camera> findByZoneStartingWith(String zoneStarting, @NotNull SortingAndOrderArguments args) {
        String qlString = "SELECT c FROM Camera as c where zone like :zoneStarting%";
        if (args.getOrder().isPresent() && args.getSort().isPresent() && VALID_PROPERTY_NAMES.contains(args.getSort().get())) {
            qlString += " ORDER BY c." + args.getSort().get() + " " + args.getOrder().get().toLowerCase();
    }
        TypedQuery<Camera> query = entityManager.createQuery(qlString, Camera.class)
                .setParameter("zoneStarting", zoneStarting);
        
        query.setMaxResults(args.getMax().orElseGet(applicationConfiguration::getMax));
        args.getOffset().ifPresent(query::setFirstResult);
        
        return query.getResultList();
	}    
    
    @Override
    @Transactional(readOnly = true)
	public long count() {
        String qlString = "SELECT count(c) FROM Camera c";
        TypedQuery<Long> query = entityManager.createQuery(qlString, Long.class);
        Long count = (long)query.getSingleResult();
        return count;
	}
}
