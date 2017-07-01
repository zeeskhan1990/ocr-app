package org.ocr.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ocr.config.Constants;
import org.ocr.domain.Metadata;
import org.ocr.repository.MetadataTypeRepository;
import org.ocr.service.DocumentService;
import org.ocr.domain.Document;
import org.ocr.repository.DocumentRepository;
import org.ocr.service.MetadataService;
import org.ocr.service.UserService;
import org.ocr.service.dto.MetadataDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Document.
 */
@Service
@Transactional
public class DocumentServiceImpl implements DocumentService{

    private final Logger log = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private final MetadataService metadataService;

    private final UserService userService;

    private final DocumentRepository documentRepository;

    private final MetadataTypeRepository metadataTypeRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository, UserService userService,
                               MetadataService metadataService, MetadataTypeRepository metadataTypeRepository) {
        this.documentRepository = documentRepository;
        this.userService = userService;
        this.metadataService = metadataService;
        this.metadataTypeRepository = metadataTypeRepository;
    }

    /**
     * Save a document.
     *
     * @param document the entity to save
     * @return the persisted entity
     */
    @Override
    public Document save(Document document) {
        log.debug("Request to save Document : {}", document);
        /*
        * Check to see if the request is for updating the document.
        * If it is for updating, then it is being checked if the
        * document belongs to the current user, if it's not then
        * findOne will thrown an AccessDeniedException
        * */
        if(document.getId() != null) {
            findOne(document.getId());
        }
        if(document.getDocumentContentType() == null) {
            document.setDocumentContentType(Constants.DEFAULT_CONTENT_TYPE);
        }
        document.setUser(userService.getUserWithAuthorities());
        Document savedDocument = documentRepository.save(document);

        return savedDocument;
    }

    /**
     *  Get all the documents.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Document> findAll() {
        log.debug("Request to get all Documents");
        return documentRepository.findByUserIsCurrentUser();
    }

    /**
     *  Get one document by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Document findOne(Long id) {
        log.debug("Request to get Document : {}", id);
        return documentRepository.findOne(id);
    }

    /**
     *  Delete the  document by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Document : {}", id);
        documentRepository.delete(id);
    }

    @Override
    public Set<Metadata> createMetadataForDocument(Document document) {
        String extractedMetadataStr = extractMetadataFromSampleService(document.getDocumentBytes(),
            document.getDocumentContentType());
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> extractedMap = new HashMap<String, String>();
        Set<Metadata> metadataSet = new HashSet<>();

        try {
            extractedMap = objectMapper.readValue(extractedMetadataStr, new TypeReference<Map<String, String>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        extractedMap.forEach((metadataTypeId, originalValue) -> {
            Metadata metadata =  new Metadata();
            metadata.setMetadataType(metadataTypeRepository.findOne(Long.parseLong(metadataTypeId)));
            metadata.setOriginalValue(originalValue);
            metadata.setDocument(document);
            metadata = metadataService.save(metadata);
            metadataSet.add(metadata);
        });

        document.setMetadatas(metadataSet);
        Document updatedDocument = save(document);
        return updatedDocument.getMetadatas();
    }

    @Override
    public Set<Metadata> updateMetadataForDocument(Set<Metadata> metadatas) {
        return metadatas.stream()
            .map(metadata -> updateMetadata(metadata))
            .collect(Collectors.toSet());
    }

    private String extractMetadataFromSampleService(byte[] document, String documentContentType) {
        Map<String,String> map = new HashMap<>();
        map.put("1","John");
        map.put("2","6");
        String mapAsJson = "{}";
        try {
            mapAsJson = new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return mapAsJson;
    }

    private Metadata updateMetadata(Metadata metadata) {
        return metadataService.save(metadata);
    }
}
