package ru.com.cardiomagnil.ca_model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import ru.com.cardiomagnil.ca_model.base.BaseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        // common fields
        "email",
        "firstname",
        "lastname",
        "birthday",
        "vkontakteId",
        "facebookId",
        "specializationExperienceYears",
        "specializationGraduationDate",
        "specializationInstitutionAddress",
        "specializationInstitutionName",
        "specializationInstitutionPhone",
        "specializationName",
        // received fields
        "id",
        "isEnabled",
        "roles",
        // sent fields
        "plainPassword",
        "isDoctor",
        "isSubscribed"
})
@DatabaseTable(tableName = "user")
public class Ca_User extends BaseModel {

    @DatabaseField(id = true, canBeNull = false, dataType = DataType.INTEGER, columnName = "id")
    @JsonProperty("id")
    private int id;

    @DatabaseField(dataType = DataType.STRING, columnName = "email")
    @JsonProperty("email")
    private String email = null;

    @DatabaseField(dataType = DataType.STRING, columnName = "firstname")
    @JsonProperty("firstname")
    private String firstname = null;

    @DatabaseField(dataType = DataType.STRING, columnName = "lastname")
    @JsonProperty("lastname")
    private String lastname = null;

    @DatabaseField(dataType = DataType.STRING, columnName = "birthday")
    @JsonProperty("birthday")
    private String birthday = null;

    @DatabaseField(dataType = DataType.INTEGER, columnName = "vkontakte_id")
    @JsonProperty("vkontakteId")
    private int vkontakteId;

    @DatabaseField(dataType = DataType.INTEGER, columnName = "facebook_id")
    @JsonProperty("facebookId")
    private int facebookId;

    @DatabaseField(dataType = DataType.INTEGER, columnName = "specialization_experience_years")
    @JsonProperty("specializationExperienceYears")
    private int specializationExperienceYears;

    @DatabaseField(dataType = DataType.STRING, columnName = "specialization_graduation_date")
    @JsonSerialize(include = JsonSerialize.Inclusion.ALWAYS)
    @JsonProperty("specializationGraduationDate")
    private String specializationGraduationDate = null;

    @DatabaseField(dataType = DataType.STRING, columnName = "specialization_institution_address")
    @JsonProperty("specializationInstitutionAddress")
    private String specializationInstitutionAddress = null;

    @DatabaseField(dataType = DataType.STRING, columnName = "specialization_institution_name")
    @JsonProperty("specializationInstitutionName")
    private String specializationInstitutionName = null;

    @DatabaseField(dataType = DataType.STRING, columnName = "specialization_institution_phone")
    @JsonProperty("specializationInstitutionPhone")
    private String specializationInstitutionPhone = null;

    @DatabaseField(dataType = DataType.STRING, columnName = "specialization_name")
    @JsonProperty("specializationName")
    private String specializationName = null;

    @DatabaseField(dataType = DataType.BOOLEAN, columnName = "is_enabled")
    @JsonProperty("isEnabled")
    private boolean isEnabled;

    // implemented manually many_to_many
    @JsonProperty("roles")
    private Collection<String> roles = new ArrayList<>();

    // links
    @DatabaseField(dataType = DataType.STRING, columnName = "region")
    @JsonProperty("region")
    private String region;

    @JsonProperty("links")
    private JsonNode links;

    @JsonProperty("plainPassword")
    private String plainPassword;

    @JsonProperty("isDoctor")
    private boolean isDoctor;

    @JsonProperty("isSubscribed")
    private boolean isSubscribed;

    /**
     * @return The id
     */
    @JsonProperty("id")
    public int getId() {
        return id;
    }

    /**
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(int id) {
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

    /**
     * @return The roles
     */
    @JsonProperty("roles")
    public List<String> getRoles() {
        return roles == null ? null : new ArrayList<String>(roles);
    }

    /**
     * @param roles The roles
     */
    @JsonProperty("roles")
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    /**
     * @return The links
     */
    @JsonProperty("links")
    public JsonNode getLinks() {
        return links;
    }

    /**
     * @param links The links
     */
    @JsonProperty("links")
    public void setLinks(JsonNode links) {
        this.links = links;
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

    @JsonProperty("isDoctor")
    public void setIsDoctor(boolean isDoctor) {
        this.isDoctor = isDoctor;
    }

    @JsonProperty("isSubscribed")
    public void setIsSubscribed(boolean isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    public static void unPackLinks(ObjectNode objectNodePacked) {
        if (objectNodePacked != null) {
            JsonNode jsonNode = objectNodePacked.get("links");
            objectNodePacked.remove("links");
            objectNodePacked.putAll((ObjectNode) jsonNode);
        }
    }

    public static void packLinks(ObjectNode objectNodeUnpacked) {
        if (objectNodeUnpacked != null) {
            ObjectNode nodeLinks = JsonNodeFactory.instance.objectNode();
            nodeLinks.put("region", objectNodeUnpacked.get("region"));
            objectNodeUnpacked.remove("region");
            objectNodeUnpacked.put("links", nodeLinks);
        }
    }

    public static void cleanReceivedFields(ObjectNode objectNodeUncleaned) {
        objectNodeUncleaned.remove(Arrays.asList("id", "isEnabled", "roles", "vkontakteId", "facebookId"));
    }

}