package org.ocr.service;

import org.ocr.domain.Document;
import org.ocr.domain.Metadata;
import org.ocr.service.dto.MetadataDTO;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Set;

/**
 * Service Interface for managing Document.
 */
public interface DocumentService {

    /**
     * Save a document.
     *
     * @param document the entity to save
     * @return the persisted entity
     */
    Document save(Document document);

    /**
     *  Get all the documents.
     *
     *  @return the list of entities
     */
    List<Document> findAll();

    /**
     *  Get the "id" document.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @PostAuthorize("returnObject.user.login == principal.username")
    Document findOne(Long id);

    /**
     *  Delete the "id" document.
     *
     *  @param id the id of the entity
     */
    @PostAuthorize("returnObject.user.login == principal.username")
    void delete(Long id);

    Set<Metadata> createMetadataForDocument(Document document);

    Set<Metadata> updateMetadataForDocument(Set<Metadata> metadatas);
}
