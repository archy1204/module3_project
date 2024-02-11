package meshkov.service.imp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import meshkov.exception.JsonParseException;
import meshkov.service.JsonService;

@Slf4j
public class JsonServiceImp implements JsonService {
    private ObjectMapper objectMapper;

    public JsonServiceImp(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String createJson(Object object) throws JsonParseException {
        try {
            log.debug("Parsing {} to Json", object);
            return objectMapper.writeValueAsString(object);
        } catch(JsonProcessingException exception) {
            log.error("Error during parse");
            throw new JsonParseException();
        }
    }

    @Override
    public Object createObject(String json, Class clazz) throws JsonParseException {
        try {
            log.debug("Parsing Json to {} object", clazz);
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("Error during parse");
            throw new JsonParseException();
        }
    }
}
