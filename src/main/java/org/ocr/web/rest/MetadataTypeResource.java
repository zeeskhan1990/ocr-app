package org.ocr.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.ocr.domain.MetadataType;

import org.ocr.repository.MetadataTypeRepository;
import org.ocr.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing MetadataType.
 */
@RestController
@RequestMapping("/api")
public class MetadataTypeResource {

    private final Logger log = LoggerFactory.getLogger(MetadataTypeResource.class);

    private static final String ENTITY_NAME = "metadataType";

    private final MetadataTypeRepository metadataTypeRepository;

    public MetadataTypeResource(MetadataTypeRepository metadataTypeRepository) {
        this.metadataTypeRepository = metadataTypeRepository;
    }

    /**
     * POST  /metadata-types : Create a new metadataType.
     *
     * @param metadataType the metadataType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new metadataType, or with status 400 (Bad Request) if the metadataType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/metadata-types")
    @Timed
    public ResponseEntity<MetadataType> createMetadataType(@RequestBody MetadataType metadataType) throws URISyntaxException {
        log.debug("REST request to save MetadataType : {}", metadataType);
        if (metadataType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new metadataType cannot already have an ID")).body(null);
        }
        MetadataType result = metadataTypeRepository.save(metadataType);
        return ResponseEntity.created(new URI("/api/metadata-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /metadata-types : Updates an existing metadataType.
     *
     * @param metadataType the metadataType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated metadataType,
     * or with status 400 (Bad Request) if the metadataType is not valid,
     * or with status 500 (Internal Server Error) if the metadataType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/metadata-types")
    @Timed
    public ResponseEntity<MetadataType> updateMetadataType(@RequestBody MetadataType metadataType) throws URISyntaxException {
        log.debug("REST request to update MetadataType : {}", metadataType);
        if (metadataType.getId() == null) {
            return createMetadataType(metadataType);
        }
        MetadataType result = metadataTypeRepository.save(metadataType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, metadataType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /metadata-types : get all the metadataTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of metadataTypes in body
     */
    @GetMapping("/metadata-types")
    @Timed
    public List<MetadataType> getAllMetadataTypes() {
        log.debug("REST request to get all MetadataTypes");
        return metadataTypeRepository.findAll();
    }

    /**
     * GET  /metadata-types/:id : get the "id" metadataType.
     *
     * @param id the id of the metadataType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the metadataType, or with status 404 (Not Found)
     */
    @GetMapping("/metadata-types/{id}")
    @Timed
    public ResponseEntity<MetadataType> getMetadataType(@PathVariable Long id) {
        log.debug("REST request to get MetadataType : {}", id);
        MetadataType metadataType = metadataTypeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(metadataType));
    }

    /**
     * DELETE  /metadata-types/:id : delete the "id" metadataType.
     *
     * @param id the id of the metadataType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/metadata-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteMetadataType(@PathVariable Long id) {
        log.debug("REST request to delete MetadataType : {}", id);
        metadataTypeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
