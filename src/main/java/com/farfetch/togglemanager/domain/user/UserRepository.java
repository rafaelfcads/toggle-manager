package com.farfetch.togglemanager.domain.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends MongoRepository<User, String> {
	
	public User findByUserName(String userName);
	
	@Override
	@Query("{id: ?#{ hasRole('ROLE_ADMIN') ? {$exists:true} : principal.userId}}")
	Page<User> findAll(Pageable pageable); 
	
	@Override
	@Query("{id: ?#{ hasRole('ROLE_ADMIN') ? {$exists:true} : principal.userId}}")
	User findOne(String arg0);
	
	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	<S extends User> S save(S arg0);
	
	@Override
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	<S extends User> S insert(S entity);
		
}
