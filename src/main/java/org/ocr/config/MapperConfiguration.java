package org.ocr.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    /**
     * Support for Model Mapper
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
