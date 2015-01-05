package ru.com.cardiomagnil.ca_model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

import ru.com.cardiomagnil.ca_model.base.BaseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "email",
        "firstname",
        "lastname",
        "birthday",
        "region",
        "vkontakteId",
        "facebookId",
        "specializationExperienceYears",
        "specializationGraduationDate",
        "specializationInstitutionAddress",
        "specializationInstitutionName",
        "specializationInstitutionPhone",
        "specializationName",
        "roles",
        "isEnabled"
})
public class Ca_User extends BaseModel {

    @JsonProperty("id")
    private String id;
    @JsonProperty("email")
    private String email;
    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("lastname")
    private String lastname;
    @JsonProperty("birthday")
    private String birthday;
    @JsonProperty("region")
    private String region;
    @JsonProperty("vkontakteId")
    private int vkontakteId;
    @JsonProperty("facebookId")
    private int facebookId;
    @JsonProperty("specializationExperienceYears")
    private int specializationExperienceYears;
    @JsonProperty("specializationGraduationDate")
    private String specializationGraduationDate;
    @JsonProperty("specializationInstitutionAddress")
    private String specializationInstitutionAddress;
    @JsonProperty("specializationInstitutionName")
    private String specializationInstitutionName;
    @JsonProperty("specializationInstitutionPhone")
    private String specializationInstitutionPhone;
    @JsonProperty("specializationName")
    private String specializationName;
    @JsonProperty("roles")
    private List<String> roles = new ArrayList<String>();
    @JsonProperty("isEnabled")
    private boolean isEnabled;

    /**
     * @return The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
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
     * @return The firstname
     */
    @JsonProperty("firstname")
    public String getFirstname() {
        return firstname;
    }

    /**
     * @param firstname The firstname
     */
    @JsonProperty("firstname")
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @return The lastname
     */
    @JsonProperty("lastname")
    public String getLastname() {
        return lastname;
    }

    /**
     * @param lastname The lastname
     */
    @JsonProperty("lastname")
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * @return The birthday
     */
    @JsonProperty("birthday")
    public String getBirthday() {
        return birthday;
    }

    /**
     * @param birthday The birthday
     */
    @JsonProperty("birthday")
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * @return The region
     */
    @JsonProperty("region")
    public String getRegion() {
        return region;
    }

    /**
     * @param region The region
     */
    @JsonProperty("region")
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return The vkontakteId
     */
    @JsonProperty("vkontakteId")
    public int getVkontakteId() {
        return vkontakteId;
    }

    /**
     * @param vkontakteId The vkontakteId
     */
    @JsonProperty("vkontakteId")
    public void setVkontakteId(int vkontakteId) {
        this.vkontakteId = vkontakteId;
    }

    /**
     * @return The facebookId
     */
    @JsonProperty("facebookId")
    public int getFacebookId() {
        return facebookId;
    }

    /**
     * @param facebookId The facebookId
     */
    @JsonProperty("facebookId")
    public void setFacebookId(int facebookId) {
        this.facebookId = facebookId;
    }

    /**
     * @return The specializationExperienceYears
     */
    @JsonProperty("specializationExperienceYears")
    public int getSpecializationExperienceYears() {
        return specializationExperienceYears;
    }

    /**
     * @param specializationExperienceYears The specializationExperienceYears
     */
    @JsonProperty("specializationExperienceYears")
    public void setSpecializationExperienceYears(int specializationExperienceYears) {
        this.specializationExperienceYears = specializationExperienceYears;
    }

    /**
     * @return The specializationGraduationDate
     */
    @JsonProperty("specializationGraduationDate")
    public String getSpecializationGraduationDate() {
        return specializationGraduationDate;
    }

    /**
     * @param specializationGraduationDate The specializationGraduationDate
     */
    @JsonProperty("specializationGraduationDate")
    public void setSpecializationGraduationDate(String specializationGraduationDate) {
        this.specializationGraduationDate = specializationGraduationDate;
    }

    /**
     * @return The specializationInstitutionAddress
     */
    @JsonProperty("specializationInstitutionAddress")
    public String getSpecializationInstitutionAddress() {
        return specializationInstitutionAddress;
    }

    /**
     * @param specializationInstitutionAddress The specializationInstitutionAddress
     */
    @JsonProperty("specializationInstitutionAddress")
    public void setSpecializationInstitutionAddress(String specializationInstitutionAddress) {
        this.specializationInstitutionAddress = specializationInstitutionAddress;
    }

    /**
     * @return The specializationInstitutionName
     */
    @JsonProperty("specializationInstitutionName")
    public String getSpecializationInstitutionName() {
        return specializationInstitutionName;
    }

    /**
     * @param specializationInstitutionName The specializationInstitutionName
     */
    @JsonProperty("specializationInstitutionName")
    public void setSpecializationInstitutionName(String specializationInstitutionName) {
        this.specializationInstitutionName = specializationInstitutionName;
    }

    /**
     * @return The specializationInstitutionPhone
     */
    @JsonProperty("specializationInstitutionPhone")
    public String getSpecializationInstitutionPhone() {
        return specializationInstitutionPhone;
    }

    /**
     * @param specializationInstitutionPhone The specializationInstitutionPhone
     */
    @JsonProperty("specializationInstitutionPhone")
    public void setSpecializationInstitutionPhone(String specializationInstitutionPhone) {
        this.specializationInstitutionPhone = specializationInstitutionPhone;
    }

    /**
     * @return The specializationName
     */
    @JsonProperty("specializationName")
    public String getSpecializationName() {
        return specializationName;
    }

    /**
     * @param specializationName The specializationName
     */
    @JsonProperty("specializationName")
    public void setSpecializationName(String specializationName) {
        this.specializationName = specializationName;
    }

    /**
     * @return The roles
     */
    @JsonProperty("roles")
    public List<String> getRoles() {
        return roles;
    }

    /**
     * @param roles The roles
     */
    @JsonProperty("roles")
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    /**
     * @return The isEnabled
     */
    @JsonProperty("isEnabled")
    public boolean isIsEnabled() {
        return isEnabled;
    }

    /**
     * @param isEnabled The isEnabled
     */
    @JsonProperty("isEnabled")
    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

}