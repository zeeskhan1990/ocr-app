package org.ocr.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import org.ocr.domain.enumeration.ExtractionStatus;

/**
 * Documents entity to store each individual
 * document that is being processed
 */
@ApiModel(description = "Documents entity to store each individual document that is being processed")
@Entity
@Table(name = "document")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Document  extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Lob
    @Column(name = "document_bytes", nullable = false)
    private byte[] documentBytes;

    @Column(name = "document_content_type", nullable = false)
    private String documentContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "extraction_status")
    private ExtractionStatus extractionStatus;

    @OneToMany(mappedBy = "document")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Metadata> metadatas = new HashSet<>();

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getDocumentBytes() {
        return documentBytes;
    }

    public Document document(byte[] document) {
        this.documentBytes = document;
        return this;
    }

    public void setDocumentBytes(byte[] documentBytes) {
        this.documentBytes = documentBytes;
    }

    public String getDocumentContentType() {
        return documentContentType;
    }

    public Document documentContentType(String documentContentType) {
        this.documentContentType = documentContentType;
        return this;
    }

    public void setDocumentContentType(String documentContentType) {
        this.documentContentType = documentContentType;
    }

    public ExtractionStatus getExtractionStatus() {
        return extractionStatus;
    }

    public Document extractionStatus(ExtractionStatus extractionStatus) {
        this.extractionStatus = extractionStatus;
        return this;
    }

    public void setExtractionStatus(ExtractionStatus extractionStatus) {
        this.extractionStatus = extractionStatus;
    }

    public Set<Metadata> getMetadatas() {
        return metadatas;
    }

    public Document setMetadatas(Set<Metadata> metadatas) {
        this.metadatas = metadatas;
        return this;
    }

    public Document addMetadata(Metadata metadata) {
        this.metadatas.add(metadata);
        metadata.setDocument(this);
        return this;
    }

    public Document removeMetadata(Metadata metadata) {
        this.metadatas.remove(metadata);
        metadata.setDocument(null);
        return this;
    }

    public User getUser() {
        return user;
    }

    public Document user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Document document = (Document) o;
        if (document.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), document.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Document{" +
            "id=" + getId() +
            ", documentBytes='" + getDocumentBytes() + "'" +
            ", documentContentType='" + documentContentType + "'" +
            ", extractionStatus='" + getExtractionStatus() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
