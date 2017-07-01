package org.ocr.service.dto;

/**
 * Created by zeeshan on 1/7/17.
 */
public class MetadataTypeDTO {
    private Long id;

    private String name;

    public MetadataTypeDTO() {

    }

    public MetadataTypeDTO(Long id, String name) {

        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
