package org.ocr.service;

import org.ocr.domain.Metadata;
import java.util.List;

/**
 * Service Interface for managing Metadata.
 */
public interface MetadataService {

    /**
     * Save a metadata.
     *
     * @param metadata the entity to save
     * @return the persisted entity
     */
    Metadata save(Metadata metadata);

    /**
     *  Get all the metadata.
     *
     *  @return the list of entities
     */
    List<Metadata> findAll();

    /**
     *  Get the "id" metadata.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Metadata findOne(Long id);

    /**
     *  Delete the "id" metadata.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
