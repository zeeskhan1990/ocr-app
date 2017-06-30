package org.ocr.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Metadata entity meant to store all the extracted data
 * points from a document
 */
@ApiModel(description = "Metadata entity meant to store all the extracted data points from a document")
@Entity
@Table(name = "metadata")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Metadata extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "original_value", nullable = false)
    private String originalValue;

    @Column(name = "verified_value")
    private String verifiedValue;

    @OneToOne
    @JoinColumn
    private MetadataType metadataType;

    @ManyToOne
    private Document document;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalValue() {
        return originalValue;
    }

    public Metadata originalValue(String originalValue) {
        this.originalValue = originalValue;
        return this;
    }

    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
    }

    public String getVerifiedValue() {
        return verifiedValue;
    }

    public Metadata verifiedValue(String verifiedValue) {
        this.verifiedValue = verifiedValue;
        return this;
    }

    public void setVerifiedValue(String verifiedValue) {
        this.verifiedValue = verifiedValue;
    }

    public MetadataType getMetadataType() {
        return metadataType;
    }

    public Metadata metadataType(MetadataType metadataType) {
        this.metadataType = metadataType;
        return this;
    }

    public void setMetadataType(MetadataType metadataType) {
        this.metadataType = metadataType;
    }

    public Document getDocument() {
        return document;
    }

    public Metadata document(Document document) {
        this.document = document;
        return this;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Metadata metadata = (Metadata) o;
        if (metadata.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), metadata.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Metadata{" +
            "id=" + getId() +
            ", originalValue='" + getOriginalValue() + "'" +
            ", verifiedValue='" + getVerifiedValue() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
