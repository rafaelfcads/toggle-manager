package com.it.togglemanager.domain.toggle;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RepositoryRestResource(collectionResourceRel = "toggles", path = "toggles")
public interface ToggleRepository extends MongoRepository<Toggle, String> {}
