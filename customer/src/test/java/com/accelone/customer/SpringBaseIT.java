package com.accelone.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@AutoConfigureMockMvc
@ContextConfiguration
public class SpringBaseIT {
    @Autowired
    public MockMvc mockMvc;

    private static ObjectMapper objectMapper;

    public void initialize() {
        JavaTimeModule module = new JavaTimeModule();
        objectMapper = new ObjectMapper();
    }

    @SuppressWarnings("rawtypes")
    protected Object createObjectListFromFile(String dir, String file, Class clazz) {
        StringBuilder fileStr =  new StringBuilder("/data/").append(dir).append(file);
        ClassPathResource resoruce =  new ClassPathResource(fileStr.toString());
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class,clazz);
        List tempList;
        try (InputStream inputStream = resoruce.getInputStream()) {
            tempList =  objectMapper.readValue(inputStream,listType);
        } catch (IOException ex) {
            throw  new RuntimeException("Failed reading File on: "+fileStr,ex);
        }

        return tempList;


    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Object createObjectFromFile(String dir, String file, Class clazz) {
        StringBuilder fileStr =  new StringBuilder("/data/").append(dir).append(file);
        ClassPathResource resource =  new ClassPathResource(fileStr.toString());
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream,clazz);
        } catch (IOException ex) {
            throw  new RuntimeException("Failed reading File on: "+fileStr,ex);
        }

    }
}
