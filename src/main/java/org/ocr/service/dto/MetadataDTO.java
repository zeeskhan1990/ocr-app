package org.ocr.service.dto;

import org.ocr.domain.Metadata;
import org.ocr.web.rest.validation.Create;
import org.ocr.web.rest.validation.Update;

import javax.validation.constraints.NotNull;

/**
 * A DTO representing a user, with his authorities.
 */
public class MetadataDTO {

    private Long id;

    @NotNull(groups = {Create.class})
    private String originalValue;

    @NotNull(groups = {Update.class})
    private String verifiedValue;

    @NotNull(groups = {Create.class, Update.class})
    private MetadataTypeDTO metadataTypeDTO;

    @NotNull(groups = {Create.class, Update.class})
    private Long documentId;

    public MetadataDTO() {

    }

    public MetadataDTO(Metadata metadata) {
        this(metadata.getId(), metadata.getOriginalValue(), metadata.getVerifiedValue(),
            new MetadataTypeDTO(metadata.getMetadataType().getId(), metadata.getMetadataType().getName()),
            metadata.getDocument().getId());
    }

    public MetadataDTO(Long id, String originalValue, String verifiedValue, MetadataTypeDTO metadataTypeDTO, Long documentId) {
        this.id = id;
        this.originalValue = originalValue;
        this.verifiedValue = verifiedValue;
        this.metadataTypeDTO = metadataTypeDTO;
        this.documentId = documentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
    }

    public String getVerifiedValue() {
        return verifiedValue;
    }

    public void setVerifiedValue(String verifiedValue) {
        this.verifiedValue = verifiedValue;
    }

    public MetadataTypeDTO getMetadataTypeDTO() {
        return metadataTypeDTO;
    }

    public void setMetadataTypeDTO(MetadataTypeDTO metadataTypeDTO) {
        this.metadataTypeDTO = metadataTypeDTO;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    @Override
    public String toString() {
        return "MetadataDTO{" +
            "id=" + id +
            ", originalValue='" + originalValue + '\'' +
            ", verifiedValue='" + verifiedValue + '\'' +
            ", metadataTypeDTO='" + metadataTypeDTO + '\'' +
            ", documentId=" + documentId +
            '}';
    }
}
