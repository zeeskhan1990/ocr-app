package org.ocr.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.ocr.domain.Metadata;
import org.ocr.domain.MetadataType;
import org.ocr.repository.MetadataTypeRepository;
import org.ocr.service.dto.MetadataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    /**
     * Support for Model Mapper
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper appModelMapper = new ModelMapper();
//        appModelMapper.addMappings(new PropertyMap<Metadata, MetadataDTO>() {
//            @Override
//            protected void configure() {
//                map().setMetadataTypeDTO(source.getMetadataTypeDTO().getName());
//            }
//        });
        return appModelMapper;
    }
}
