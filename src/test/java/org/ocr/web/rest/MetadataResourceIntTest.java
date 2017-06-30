package org.ocr.web.rest;

import org.modelmapper.ModelMapper;
import org.ocr.OcrApp;

import org.ocr.domain.Metadata;
import org.ocr.repository.MetadataRepository;
import org.ocr.service.MetadataService;
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

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MetadataResource REST controller.
 *
 * @see MetadataResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OcrApp.class)
public class MetadataResourceIntTest {

    private static final String DEFAULT_ORIGINAL_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINAL_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_VERIFIED_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VERIFIED_VALUE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_UPDATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    @Autowired
    private MetadataRepository metadataRepository;

    @Autowired
    private MetadataService metadataService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private ModelMapper modelMapper;

    private MockMvc restMetadataMockMvc;

    private Metadata metadata;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MetadataResource metadataResource = new MetadataResource(metadataService, modelMapper);
        this.restMetadataMockMvc = MockMvcBuilders.standaloneSetup(metadataResource)
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
    public static Metadata createEntity(EntityManager em) {
        Metadata metadata = new Metadata()
            .originalValue(DEFAULT_ORIGINAL_VALUE)
            .verifiedValue(DEFAULT_VERIFIED_VALUE);
        return metadata;
    }

    @Before
    public void initTest() {
        metadata = createEntity(em);
    }

    @Test
    @Transactional
    public void createMetadata() throws Exception {
        int databaseSizeBeforeCreate = metadataRepository.findAll().size();

        // Create the Metadata
        restMetadataMockMvc.perform(post("/api/metadata")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metadata)))
            .andExpect(status().isCreated());

        // Validate the Metadata in the database
        List<Metadata> metadataList = metadataRepository.findAll();
        assertThat(metadataList).hasSize(databaseSizeBeforeCreate + 1);
        Metadata testMetadata = metadataList.get(metadataList.size() - 1);
        assertThat(testMetadata.getOriginalValue()).isEqualTo(DEFAULT_ORIGINAL_VALUE);
        assertThat(testMetadata.getVerifiedValue()).isEqualTo(DEFAULT_VERIFIED_VALUE);
        assertThat(testMetadata.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    public void createMetadataWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = metadataRepository.findAll().size();

        // Create the Metadata with an existing ID
        metadata.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetadataMockMvc.perform(post("/api/metadata")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metadata)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Metadata> metadataList = metadataRepository.findAll();
        assertThat(metadataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkOriginalValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = metadataRepository.findAll().size();
        // set the field null
        metadata.setOriginalValue(null);

        // Create the Metadata, which fails.

        restMetadataMockMvc.perform(post("/api/metadata")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metadata)))
            .andExpect(status().isBadRequest());

        List<Metadata> metadataList = metadataRepository.findAll();
        assertThat(metadataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMetadata() throws Exception {
        // Initialize the database
        metadataRepository.saveAndFlush(metadata);

        // Get all the metadataList
        restMetadataMockMvc.perform(get("/api/metadata?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].originalValue").value(hasItem(DEFAULT_ORIGINAL_VALUE.toString())))
            .andExpect(jsonPath("$.[*].verifiedValue").value(hasItem(DEFAULT_VERIFIED_VALUE.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].updationDate").value(hasItem(DEFAULT_UPDATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())));
    }

    @Test
    @Transactional
    public void getMetadata() throws Exception {
        // Initialize the database
        metadataRepository.saveAndFlush(metadata);

        // Get the metadata
        restMetadataMockMvc.perform(get("/api/metadata/{id}", metadata.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(metadata.getId().intValue()))
            .andExpect(jsonPath("$.originalValue").value(DEFAULT_ORIGINAL_VALUE.toString()))
            .andExpect(jsonPath("$.verifiedValue").value(DEFAULT_VERIFIED_VALUE.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.updationDate").value(DEFAULT_UPDATION_DATE.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMetadata() throws Exception {
        // Get the metadata
        restMetadataMockMvc.perform(get("/api/metadata/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMetadata() throws Exception {
        // Initialize the database
        metadataService.save(metadata);

        int databaseSizeBeforeUpdate = metadataRepository.findAll().size();

        // Update the metadata
        Metadata updatedMetadata = metadataRepository.findOne(metadata.getId());
        updatedMetadata
            .originalValue(UPDATED_ORIGINAL_VALUE)
            .verifiedValue(UPDATED_VERIFIED_VALUE);

        restMetadataMockMvc.perform(put("/api/metadata")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMetadata)))
            .andExpect(status().isOk());

        // Validate the Metadata in the database
        List<Metadata> metadataList = metadataRepository.findAll();
        assertThat(metadataList).hasSize(databaseSizeBeforeUpdate);
        Metadata testMetadata = metadataList.get(metadataList.size() - 1);
        assertThat(testMetadata.getOriginalValue()).isEqualTo(UPDATED_ORIGINAL_VALUE);
        assertThat(testMetadata.getVerifiedValue()).isEqualTo(UPDATED_VERIFIED_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingMetadata() throws Exception {
        int databaseSizeBeforeUpdate = metadataRepository.findAll().size();

        // Create the Metadata

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMetadataMockMvc.perform(put("/api/metadata")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metadata)))
            .andExpect(status().isCreated());

        // Validate the Metadata in the database
        List<Metadata> metadataList = metadataRepository.findAll();
        assertThat(metadataList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMetadata() throws Exception {
        // Initialize the database
        metadataService.save(metadata);

        int databaseSizeBeforeDelete = metadataRepository.findAll().size();

        // Get the metadata
        restMetadataMockMvc.perform(delete("/api/metadata/{id}", metadata.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Metadata> metadataList = metadataRepository.findAll();
        assertThat(metadataList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Metadata.class);
        Metadata metadata1 = new Metadata();
        metadata1.setId(1L);
        Metadata metadata2 = new Metadata();
        metadata2.setId(metadata1.getId());
        assertThat(metadata1).isEqualTo(metadata2);
        metadata2.setId(2L);
        assertThat(metadata1).isNotEqualTo(metadata2);
        metadata1.setId(null);
        assertThat(metadata1).isNotEqualTo(metadata2);
    }
}
