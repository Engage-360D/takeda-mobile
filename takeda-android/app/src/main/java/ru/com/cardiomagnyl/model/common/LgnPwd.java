package ru.com.cardiomagnyl.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import ru.com.cardiomagnyl.model.base.BaseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "email",
        "plainPassword"
})
public class LgnPwd extends BaseModel {

    @JsonProperty("email")
    private String email;
    @JsonProperty("plainPassword")
    private String plainPassword;

    public LgnPwd() {
    }

    public LgnPwd(String email, String plainPassword) {
        this.email = email;
        this.plainPassword = plainPassword;
    }

    /**
     * @return The email
     */
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email
     */
    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The plainPassword
     */
    @JsonProperty("plainPassword")
    public String getPlainPassword() {
        return plainPassword;
    }

    /**
     * @param plainPassword The plainPassword
     */
    @JsonProperty("plainPassword")
    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }

}