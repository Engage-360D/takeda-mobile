package ru.com.cardiomagnil.ca_model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import ru.com.cardiomagnil.ca_model.base.BaseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "email",
        "plainPassword"
})
public class Ca_UserLgnPwd extends BaseModel {

    @JsonProperty("email")
    private String email;
    @JsonProperty("plainPassword")
    private String plainPassword;

    public Ca_UserLgnPwd() {
    }

    public Ca_UserLgnPwd(String email, String plainPassword) {
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