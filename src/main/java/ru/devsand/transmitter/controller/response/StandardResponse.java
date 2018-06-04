package ru.devsand.transmitter.controller.response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import static ru.devsand.transmitter.controller.response.ResponseStatus.ERROR;
import static ru.devsand.transmitter.controller.response.ResponseStatus.SUCCESS;

public class StandardResponse {

    private ResponseStatus status;
    private String message;
    private JsonElement data;

    public StandardResponse(ResponseStatus status) {
        this.status = status;
    }

    public StandardResponse(ResponseStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public StandardResponse(ResponseStatus status, Object data) {
        this.status = status;
        this.data = new Gson().toJsonTree(data);
    }

    public static StandardResponse withData(Object data) {
        return new StandardResponse(SUCCESS, data);
    }

    public static StandardResponse errorResponse(String message) {
        return new StandardResponse(ERROR, message);
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }

}
