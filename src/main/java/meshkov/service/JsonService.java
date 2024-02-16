package meshkov.service;

import meshkov.dto.StudentRequest;
import meshkov.dto.StudentResponse;
import meshkov.exception.JsonParseException;

public interface JsonService {

    String createJson(Object object) throws JsonParseException;

    Object createObject(String json, Class clazz) throws JsonParseException;

}
