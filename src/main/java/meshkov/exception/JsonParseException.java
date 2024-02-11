package meshkov.exception;

public class JsonParseException extends Exception{

    @Override
    public String getMessage() {
        return "There is error during parse";
    }
}
