
package ru.com.cardiomagnyl.model.pill_proxy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)

@JsonPropertyOrder({
        "user"
})
public class PillLinksProxy {

    @JsonProperty("user")
    private String user;

    /**
     * @return The user
     */
    @JsonProperty("user")
    public String getUser() {
        return user;
    }

    /**
     * @param user The user
     */
    @JsonProperty("user")
    public void setUser(String user) {
        this.user = user;
    }

}