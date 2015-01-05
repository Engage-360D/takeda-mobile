package ru.com.cardiomagnil.ca_model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;

import ru.com.cardiomagnil.ca_api.Status;
import ru.com.cardiomagnil.ca_model.base.BaseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "links",
        "data",
        "linked",
        "error"
})
public class Ca_Response extends BaseModel {

    @JsonProperty("links")
    private JsonNode links;
    @JsonProperty("data")
    private JsonNode data;
    @JsonProperty("linked")
    private JsonNode linked;
    @JsonProperty("error")
    private Ca_Error error;

    public Ca_Response() {
    }

    private Ca_Response(
            JsonNode links,
            JsonNode data,
            JsonNode linked,
            Ca_Error error) {
        this.links = links;
        this.data = data;
        this.linked = linked;
        this.error = error;
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
     * @return The data
     */
    @JsonProperty("data")
    public JsonNode getData() {
        return data;
    }

    /**
     * @param data The data
     */
    @JsonProperty("data")
    public void setData(JsonNode data) {
        this.data = data;
    }

    /**
     * @return The linked
     */
    @JsonProperty("linked")
    public JsonNode getLinked() {
        return linked;
    }

    /**
     * @param linked The linked
     */
    @JsonProperty("linked")
    public void setLinked(JsonNode linked) {
        this.linked = linked;
    }

    /**
     * @return The error
     */
    @JsonProperty("error")
    public Ca_Error getError() {
        if (!isSuccessful() && error == null) {
            int errorCode = Status.NO_DATA_ERROR;
            error = new Ca_Error(errorCode, Status.getDescription(errorCode));
        }
        return error;
    }

    /**
     * @param error The error
     */
    @JsonProperty("error")
    public void setError(Ca_Error error) {
        this.error = error;
    }

    public boolean isSuccessful() {
        return data != null;
    }

    public static class Builder {
        // FIXME: fields must be final
        private final JsonNode mBuilderData;
        private final Ca_Error mBuilderError;
        private JsonNode mBuilderLinks;
        private JsonNode mBuilderLinked;

        public Builder(JsonNode builderData) {
            mBuilderData = builderData;
            mBuilderError = null;
        }

        public Builder(Ca_Error builderError) {
            mBuilderData = null;
            mBuilderError = builderError;
        }

        public Builder setLinks(JsonNode builderLinks) {
            mBuilderLinks = builderLinks;
            return this;
        }

        public Builder setLinked(JsonNode builderLinked) {
            mBuilderLinked = builderLinked;
            return this;
        }

        public Ca_Response create() {
            return new Ca_Response(
                    mBuilderLinks,
                    mBuilderData,
                    mBuilderLinked,
                    mBuilderError);
        }
    }

}
