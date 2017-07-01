package org.ocr.service.dto;
import org.ocr.domain.enumeration.ExtractionStatus;
import org.ocr.web.rest.validation.Create;
import org.ocr.web.rest.validation.Update;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Set;

/**
 * A DTO representing a document
 */
public class DocumentDTO {

    private Long id;

    @NotNull(groups = {Create.class, Update.class})
    private byte[] documentBytes;

    private String documentContentType;

    private ExtractionStatus extractionStatus;

    private Set<MetadataDTO> metadatas;

    public DocumentDTO() {

    }

//    public DocumentDTO(Document documents) {
//        this(documents.getId(), documents.getDocumentBytes(), documents.getDocumentContentType(), documents.getExtractionStatus(), documents.getMetadatas());
//    }

    public DocumentDTO(Long id, byte[] documentBytes, String documentContentType, ExtractionStatus extractionStatus, Set<MetadataDTO> metadatas) {
        this.id = id;
        this.documentBytes = documentBytes;
        this.documentContentType = documentContentType;
        this.extractionStatus = extractionStatus;
        this.metadatas = metadatas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getDocumentBytes() {
        return documentBytes;
    }

    public void setDocumentBytes(byte[] documentBytes) {
        this.documentBytes = documentBytes;
    }

    public String getDocumentContentType() {
        return documentContentType;
    }

    public void setDocumentContentType(String documentContentType) {
        this.documentContentType = documentContentType;
    }

    public ExtractionStatus getExtractionStatus() {
        return extractionStatus;
    }

    public void setExtractionStatus(ExtractionStatus extractionStatus) {
        this.extractionStatus = extractionStatus;
    }

    public Set<MetadataDTO> getMetadatas() {
        return metadatas;
    }

    public void setMetadatas(Set<MetadataDTO> metadatas) {
        this.metadatas = metadatas;
    }

    @Override
    public String toString() {
        return "DocumentDTO{" +
            "id=" + id +
            ", documentBytes=" + Arrays.toString(documentBytes) +
            ", documentContentType='" + documentContentType + '\'' +
            ", extractionStatus=" + extractionStatus +
            ", metadatas=" + metadatas +
            '}';
    }

}
