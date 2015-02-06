package ru.com.cardiomagnyl.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;

import ru.com.cardiomagnyl.api.Status;
import ru.com.cardiomagnyl.model.base.BaseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "links",
        "data",
        "linked",
        "error"
})
public class Response extends BaseModel {

    @JsonProperty("links")
    private JsonNode links;
    @JsonProperty("data")
    private JsonNode data;
    @JsonProperty("linked")
    private JsonNode linked;
    @JsonProperty("error")
    private Error error;

    public Response() {
    }

    private Response(
            JsonNode links,
            JsonNode data,
            JsonNode linked,
            Error error) {
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
    public Error getError() {
        if (!isSuccessful() && error == null) {
            int errorCode = Status.NO_DATA_ERROR;
            error = new Error(errorCode, Status.getDescription(errorCode));
        }
        return error;
    }

    /**
     * @param error The error
     */
    @JsonProperty("error")
    public void setError(Error error) {
        this.error = error;
    }

    public boolean isSuccessful() {
        return data != null;
    }

    public static class Builder {
        // FIXME: fields must be final
        private final JsonNode mBuilderData;
        private final Error mBuilderError;
        private JsonNode mBuilderLinks;
        private JsonNode mBuilderLinked;

        public Builder(JsonNode builderData) {
            mBuilderData = builderData;
            mBuilderError = null;
        }

        public Builder(Error builderError) {
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

        public Response create() {
            return new Response(
                    mBuilderLinks,
                    mBuilderData,
                    mBuilderLinked,
                    mBuilderError);
        }
    }

}
