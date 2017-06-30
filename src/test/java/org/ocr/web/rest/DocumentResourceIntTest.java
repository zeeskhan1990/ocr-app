package org.ocr.web.rest;

import org.modelmapper.ModelMapper;
import org.ocr.OcrApp;

import org.ocr.domain.Document;
import org.ocr.repository.DocumentRepository;
import org.ocr.service.DocumentService;
import org.ocr.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.ocr.domain.enumeration.ExtractionStatus;
/**
 * Test class for the DocumentResource REST controller.
 *
 * @see DocumentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OcrApp.class)
public class DocumentResourceIntTest {

    private static final byte[] DEFAULT_DOCUMENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DOCUMENT = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_DOCUMENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DOCUMENT_CONTENT_TYPE = "image/png";

    private static final ExtractionStatus DEFAULT_EXTRACTION_STATUS = ExtractionStatus.PENDING;
    private static final ExtractionStatus UPDATED_EXTRACTION_STATUS = ExtractionStatus.VERIFIED;

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private ModelMapper modelMapper;

    private MockMvc restDocumentMockMvc;

    private Document document;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DocumentResource documentResource = new DocumentResource(documentService, modelMapper);
        this.restDocumentMockMvc = MockMvcBuilders.standaloneSetup(documentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Document createEntity(EntityManager em) {
        Document document = new Document()
            .document(DEFAULT_DOCUMENT)
            .documentContentType(DEFAULT_DOCUMENT_CONTENT_TYPE)
            .extractionStatus(DEFAULT_EXTRACTION_STATUS);
        return document;
    }

    @Before
    public void initTest() {
        document = createEntity(em);
    }

    @Test
    @Transactional
    public void createDocument() throws Exception {
        int databaseSizeBeforeCreate = documentRepository.findAll().size();

        // Create the Document
        restDocumentMockMvc.perform(post("/api/documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(document)))
            .andExpect(status().isCreated());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeCreate + 1);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getDocumentBytes()).isEqualTo(DEFAULT_DOCUMENT);
        assertThat(testDocument.getDocumentContentType()).isEqualTo(DEFAULT_DOCUMENT_CONTENT_TYPE);
        assertThat(testDocument.getExtractionStatus()).isEqualTo(DEFAULT_EXTRACTION_STATUS);
    }

    @Test
    @Transactional
    public void createDocumentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = documentRepository.findAll().size();

        // Create the Document with an existing ID
        document.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentMockMvc.perform(post("/api/documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(document)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDocumentIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentRepository.findAll().size();
        // set the field null
        document.setDocumentBytes(null);

        // Create the Document, which fails.

        restDocumentMockMvc.perform(post("/api/documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(document)))
            .andExpect(status().isBadRequest());

        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDocuments() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList
        restDocumentMockMvc.perform(get("/api/documents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(document.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentContentType").value(hasItem(DEFAULT_DOCUMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].document").value(hasItem(Base64Utils.encodeToString(DEFAULT_DOCUMENT))))
            .andExpect(jsonPath("$.[*].extractionStatus").value(hasItem(DEFAULT_EXTRACTION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].updationDate").value(hasItem(DEFAULT_UPDATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())));
    }

    @Test
    @Transactional
    public void getDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get the document
        restDocumentMockMvc.perform(get("/api/documents/{id}", document.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(document.getId().intValue()))
            .andExpect(jsonPath("$.documentContentType").value(DEFAULT_DOCUMENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.document").value(Base64Utils.encodeToString(DEFAULT_DOCUMENT)))
            .andExpect(jsonPath("$.extractionStatus").value(DEFAULT_EXTRACTION_STATUS.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.updationDate").value(DEFAULT_UPDATION_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDocument() throws Exception {
        // Get the document
        restDocumentMockMvc.perform(get("/api/documents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocument() throws Exception {
        // Initialize the database
        documentService.save(document);

        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // Update the document
        Document updatedDocument = documentRepository.findOne(document.getId());
        updatedDocument
            .document(UPDATED_DOCUMENT)
            .documentContentType(UPDATED_DOCUMENT_CONTENT_TYPE)
            .extractionStatus(UPDATED_EXTRACTION_STATUS);

        restDocumentMockMvc.perform(put("/api/documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDocument)))
            .andExpect(status().isOk());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getDocumentBytes()).isEqualTo(UPDATED_DOCUMENT);
        assertThat(testDocument.getDocumentContentType()).isEqualTo(UPDATED_DOCUMENT_CONTENT_TYPE);
        assertThat(testDocument.getExtractionStatus()).isEqualTo(UPDATED_EXTRACTION_STATUS);
        assertThat(testDocument.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // Create the Document

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDocumentMockMvc.perform(put("/api/documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(document)))
            .andExpect(status().isCreated());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDocument() throws Exception {
        // Initialize the database
        documentService.save(document);

        int databaseSizeBeforeDelete = documentRepository.findAll().size();

        // Get the document
        restDocumentMockMvc.perform(delete("/api/documents/{id}", document.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Document.class);
        Document document1 = new Document();
        document1.setId(1L);
        Document document2 = new Document();
        document2.setId(document1.getId());
        assertThat(document1).isEqualTo(document2);
        document2.setId(2L);
        assertThat(document1).isNotEqualTo(document2);
        document1.setId(null);
        assertThat(document1).isNotEqualTo(document2);
    }
}
