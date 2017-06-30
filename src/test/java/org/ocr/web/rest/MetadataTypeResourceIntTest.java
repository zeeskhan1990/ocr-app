package org.ocr.web.rest;

import org.ocr.OcrApp;

import org.ocr.domain.MetadataType;
import org.ocr.repository.MetadataTypeRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MetadataTypeResource REST controller.
 *
 * @see MetadataTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OcrApp.class)
public class MetadataTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private MetadataTypeRepository metadataTypeRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMetadataTypeMockMvc;

    private MetadataType metadataType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MetadataTypeResource metadataTypeResource = new MetadataTypeResource(metadataTypeRepository);
        this.restMetadataTypeMockMvc = MockMvcBuilders.standaloneSetup(metadataTypeResource)
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
    public static MetadataType createEntity(EntityManager em) {
        MetadataType metadataType = new MetadataType()
            .name(DEFAULT_NAME);
        return metadataType;
    }

    @Before
    public void initTest() {
        metadataType = createEntity(em);
    }

    @Test
    @Transactional
    public void createMetadataType() throws Exception {
        int databaseSizeBeforeCreate = metadataTypeRepository.findAll().size();

        // Create the MetadataType
        restMetadataTypeMockMvc.perform(post("/api/metadata-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metadataType)))
            .andExpect(status().isCreated());

        // Validate the MetadataType in the database
        List<MetadataType> metadataTypeList = metadataTypeRepository.findAll();
        assertThat(metadataTypeList).hasSize(databaseSizeBeforeCreate + 1);
        MetadataType testMetadataType = metadataTypeList.get(metadataTypeList.size() - 1);
        assertThat(testMetadataType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createMetadataTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = metadataTypeRepository.findAll().size();

        // Create the MetadataType with an existing ID
        metadataType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetadataTypeMockMvc.perform(post("/api/metadata-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metadataType)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<MetadataType> metadataTypeList = metadataTypeRepository.findAll();
        assertThat(metadataTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMetadataTypes() throws Exception {
        // Initialize the database
        metadataTypeRepository.saveAndFlush(metadataType);

        // Get all the metadataTypeList
        restMetadataTypeMockMvc.perform(get("/api/metadata-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metadataType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getMetadataType() throws Exception {
        // Initialize the database
        metadataTypeRepository.saveAndFlush(metadataType);

        // Get the metadataType
        restMetadataTypeMockMvc.perform(get("/api/metadata-types/{id}", metadataType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(metadataType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMetadataType() throws Exception {
        // Get the metadataType
        restMetadataTypeMockMvc.perform(get("/api/metadata-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMetadataType() throws Exception {
        // Initialize the database
        metadataTypeRepository.saveAndFlush(metadataType);
        int databaseSizeBeforeUpdate = metadataTypeRepository.findAll().size();

        // Update the metadataType
        MetadataType updatedMetadataType = metadataTypeRepository.findOne(metadataType.getId());
        updatedMetadataType
            .name(UPDATED_NAME);

        restMetadataTypeMockMvc.perform(put("/api/metadata-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMetadataType)))
            .andExpect(status().isOk());

        // Validate the MetadataType in the database
        List<MetadataType> metadataTypeList = metadataTypeRepository.findAll();
        assertThat(metadataTypeList).hasSize(databaseSizeBeforeUpdate);
        MetadataType testMetadataType = metadataTypeList.get(metadataTypeList.size() - 1);
        assertThat(testMetadataType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingMetadataType() throws Exception {
        int databaseSizeBeforeUpdate = metadataTypeRepository.findAll().size();

        // Create the MetadataType

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMetadataTypeMockMvc.perform(put("/api/metadata-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metadataType)))
            .andExpect(status().isCreated());

        // Validate the MetadataType in the database
        List<MetadataType> metadataTypeList = metadataTypeRepository.findAll();
        assertThat(metadataTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMetadataType() throws Exception {
        // Initialize the database
        metadataTypeRepository.saveAndFlush(metadataType);
        int databaseSizeBeforeDelete = metadataTypeRepository.findAll().size();

        // Get the metadataType
        restMetadataTypeMockMvc.perform(delete("/api/metadata-types/{id}", metadataType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MetadataType> metadataTypeList = metadataTypeRepository.findAll();
        assertThat(metadataTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetadataType.class);
        MetadataType metadataType1 = new MetadataType();
        metadataType1.setId(1L);
        MetadataType metadataType2 = new MetadataType();
        metadataType2.setId(metadataType1.getId());
        assertThat(metadataType1).isEqualTo(metadataType2);
        metadataType2.setId(2L);
        assertThat(metadataType1).isNotEqualTo(metadataType2);
        metadataType1.setId(null);
        assertThat(metadataType1).isNotEqualTo(metadataType2);
    }
}
