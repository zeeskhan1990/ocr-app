package org.ocr.service.impl;

import org.ocr.service.MetadataService;
import org.ocr.domain.Metadata;
import org.ocr.repository.MetadataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Metadata.
 */
@Service
@Transactional
public class MetadataServiceImpl implements MetadataService{

    private final Logger log = LoggerFactory.getLogger(MetadataServiceImpl.class);

    private final MetadataRepository metadataRepository;

    public MetadataServiceImpl(MetadataRepository metadataRepository) {
        this.metadataRepository = metadataRepository;
    }

    /**
     * Save a metadata.
     *
     * @param metadata the entity to save
     * @return the persisted entity
     */
    @Override
    public Metadata save(Metadata metadata) {
        log.debug("Request to save Metadata : {}", metadata);
        return metadataRepository.save(metadata);
    }

    /**
     *  Get all the metadata.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Metadata> findAll() {
        log.debug("Request to get all Metadata");
        return metadataRepository.findAll();
    }

    /**
     *  Get one metadata by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Metadata findOne(Long id) {
        log.debug("Request to get Metadata : {}", id);
        return metadataRepository.findOne(id);
    }

    /**
     *  Delete the  metadata by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Metadata : {}", id);
        metadataRepository.delete(id);
    }
}
