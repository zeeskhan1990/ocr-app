package org.ocr.service;

import org.ocr.domain.Document;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

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
    @PreAuthorize("#doc.user.login == principal.username or doc.id==null")
    Document save(@P("doc") Document document);

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
    //@PreAuthorize("#id == principal.username") --- Not being able to retrieve the user Id
    Document findOne(Long id);

    /**
     *  Delete the "id" document.
     *
     *  @param id the id of the entity
     */
    //@PreAuthorize("#id == principal.username") --- Not being able to retrieve the user Id
    void delete(Long id);
}
