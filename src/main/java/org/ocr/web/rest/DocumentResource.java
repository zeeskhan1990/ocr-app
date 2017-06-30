package org.ocr.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.ocr.domain.Document;
import org.ocr.domain.Metadata;
import org.ocr.service.DocumentService;
import org.ocr.service.dto.DocumentDTO;
import org.ocr.service.dto.MetadataDTO;
import org.ocr.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.ocr.web.rest.validation.Create;
import org.ocr.web.rest.validation.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Document.
 */
@RestController
@RequestMapping("/api")
public class DocumentResource {

    private final Logger log = LoggerFactory.getLogger(DocumentResource.class);

    private static final String ENTITY_NAME = "document";

    private final DocumentService documentService;

    private ModelMapper modelMapper;

    public DocumentResource(DocumentService documentService, ModelMapper modelMapper) {
        this.documentService = documentService;
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    }

    /**
     * POST  /documents : Create a new document.
     *
     * @param documentDTO the document to create
     * @return the ResponseEntity with status 201 (Created) and with body the new document, or with status 400 (Bad Request) if the document has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/documents")
    @Timed
    public ResponseEntity<DocumentDTO> createDocuments(@RequestBody DocumentDTO documentDTO) throws URISyntaxException {
        log.debug("REST request to save Document : {}", documentDTO);
        if (documentDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new documents cannot already have an ID")).body(null);
        }
        Document savedDocument = documentService.save(convertDocumentDTOToEntity(documentDTO));

        //savedDocument
        DocumentDTO result = convertDocumentToDTO(savedDocument);
        return ResponseEntity.created(new URI("/api/documents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /documents : Updates an existing document.
     *
     * @param documentDTO the document to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated document,
     * or with status 400 (Bad Request) if the document is not valid,
     * or with status 500 (Internal Server Error) if the document couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/documents")
    @Timed
    public ResponseEntity<DocumentDTO> updateDocuments(@Validated(Update.class) @RequestBody DocumentDTO documentDTO) throws URISyntaxException {
        log.debug("REST request to update Document : {}", documentDTO);
        if (documentDTO.getId() == null) {
            return createDocuments(documentDTO);
        }
        Document updatedDocument = documentService.save(convertDocumentDTOToEntity(documentDTO));
        DocumentDTO result = convertDocumentToDTO(updatedDocument);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, documentDTO.getId().toString()))
            .body(result);
    }



    /**
     * GET  /documents : get all the documents.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of documents in body
     */
    @GetMapping("/documents")
    @Timed
    public List<Document> getAllDocuments() {
        log.debug("REST request to get all Documents");
        return documentService.findAll();
    }

    /**
     * GET  /documents/:id : get the "id" document.
     *
     * @param id the id of the document to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the document, or with status 404 (Not Found)
     */
    @GetMapping("/documents/{id}")
    @Timed
    public ResponseEntity<Document> getDocument(@PathVariable Long id) {
        log.debug("REST request to get Document : {}", id);
        Document document = documentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(document));
    }

    /**
     * DELETE  /documents/:id : delete the "id" document.
     *
     * @param id the id of the document to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/documents/{id}")
    @Timed
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        log.debug("REST request to delete Document : {}", id);
        documentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    private DocumentDTO convertDocumentToDTO(Document document) {
        DocumentDTO documentDTO = modelMapper.map(document, DocumentDTO.class);
//        documentDTO.formatCreationDate(document.getCreationDate());
//        documentDTO.formatUpdationDate(document.getUpdationDate());
        return documentDTO;
    }

    private Document convertDocumentDTOToEntity(DocumentDTO documentDTO) {
        return modelMapper.map(documentDTO, Document.class);
    }

    private MetadataDTO convertMetadataToDTO(Metadata metadata) {
        return modelMapper.map(metadata, MetadataDTO.class);
    }

    private Metadata convertMetadataDTOToEntity(MetadataDTO metadataDTO) {
        return modelMapper.map(metadataDTO, Metadata.class);
    }
}
