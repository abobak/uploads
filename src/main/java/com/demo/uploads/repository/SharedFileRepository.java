package com.demo.uploads.repository;

import com.demo.uploads.model.SharedFile;
import com.demo.uploads.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SharedFileRepository extends CrudRepository<SharedFile, Long>, JpaRepository<SharedFile, Long> {

    Optional<SharedFile> findByIdentifier(String identifier);

    List<SharedFile> findAllByOwner(User owner);

    List<SharedFile> findAllBySharedWithIsContaining(User user);
}
