package org.ocr.repository;

import org.ocr.domain.MetadataType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MetadataType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetadataTypeRepository extends JpaRepository<MetadataType,Long> {
    
}
