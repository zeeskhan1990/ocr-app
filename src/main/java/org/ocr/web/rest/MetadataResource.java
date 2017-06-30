package org.ocr.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.modelmapper.ModelMapper;
import org.ocr.domain.Metadata;
import org.ocr.service.MetadataService;
import org.ocr.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Metadata.
 */
@RestController
@RequestMapping("/api")
public class MetadataResource {

    private final Logger log = LoggerFactory.getLogger(MetadataResource.class);

    private static final String ENTITY_NAME = "metadata";

    private final MetadataService metadataService;

    private ModelMapper modelMapper;

    public MetadataResource(MetadataService metadataService, ModelMapper modelMapper) {
        this.metadataService = metadataService;
        this.modelMapper = modelMapper;
    }

    /**
     * POST  /metadata : Create a new metadata.
     *
     * @param metadata the metadata to create
     * @return the ResponseEntity with status 201 (Created) and with body the new metadata, or with status 400 (Bad Request) if the metadata has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/metadata")
    @Timed
    public ResponseEntity<Metadata> createMetadata(@Valid @RequestBody Metadata metadata) throws URISyntaxException {
        log.debug("REST request to save Metadata : {}", metadata);
        if (metadata.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new metadata cannot already have an ID")).body(null);
        }
        Metadata result = metadataService.save(metadata);
        return ResponseEntity.created(new URI("/api/metadata/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /metadata : Updates an existing metadata.
     *
     * @param metadata the metadata to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated metadata,
     * or with status 400 (Bad Request) if the metadata is not valid,
     * or with status 500 (Internal Server Error) if the metadata couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/metadata")
    @Timed
    public ResponseEntity<Metadata> updateMetadata(@Valid @RequestBody Metadata metadata) throws URISyntaxException {
        log.debug("REST request to update Metadata : {}", metadata);
        if (metadata.getId() == null) {
            return createMetadata(metadata);
        }
        Metadata result = metadataService.save(metadata);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, metadata.getId().toString()))
            .body(result);
    }

    /**
     * GET  /metadata : get all the metadata.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of metadata in body
     */
    @GetMapping("/metadata")
    @Timed
    public List<Metadata> getAllMetadata() {
        log.debug("REST request to get all Metadata");
        return metadataService.findAll();
    }

    /**
     * GET  /metadata/:id : get the "id" metadata.
     *
     * @param id the id of the metadata to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the metadata, or with status 404 (Not Found)
     */
    @GetMapping("/metadata/{id}")
    @Timed
    public ResponseEntity<Metadata> getMetadata(@PathVariable Long id) {
        log.debug("REST request to get Metadata : {}", id);
        Metadata metadata = metadataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(metadata));
    }

    /**
     * DELETE  /metadata/:id : delete the "id" metadata.
     *
     * @param id the id of the metadata to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/metadata/{id}")
    @Timed
    public ResponseEntity<Void> deleteMetadata(@PathVariable Long id) {
        log.debug("REST request to delete Metadata : {}", id);
        metadataService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
