package ru.com.cardiomagnil.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "code",
        "message"
})
public class Error {

    @JsonProperty("code")
    private int code;
    @JsonProperty("message")
    private String message;

    public Error() {
    }

    public Error(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return The code
     */
    @JsonProperty("code")
    public int getCode() {
        return code;
    }

    /**
     * @param code The code
     */
    @JsonProperty("code")
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return The message
     */
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

}