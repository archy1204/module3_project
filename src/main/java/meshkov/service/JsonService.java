package meshkov.service;

import meshkov.exception.JsonParseException;

public interface JsonService {

    String createJson(Object object) throws JsonParseException;

    Object createObject(String json, Class clazz) throws JsonParseException;
}
