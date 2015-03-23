package ru.com.cardiomagnyl.model.institution;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import ru.com.cardiomagnyl.model.base.BaseModel;
import ru.com.cardiomagnyl.model.base.BaseModelHelper;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "specialization",
        "address",
//        "googleAddress",
//        "region",
        "parsedTown",
//        "parsedStreet",
//        "parsedHouse",
//        "parsedCorpus",
//        "parsedBuilding",
//        "parsedRegion",
        "lat",
        "lng",
        "priority"
})
@DatabaseTable(tableName = "institution")
public class Institution extends BaseModel implements Parcelable, ClusterItem, BaseModelHelper<String> {

    @Override
    public LatLng getPosition() {
        return new LatLng(lat, lng);
    }

    @DatabaseField(id = true, canBeNull = false, dataType = DataType.STRING, columnName = "id")
    @JsonProperty("id")
    private String id;

    @DatabaseField(dataType = DataType.STRING, columnName = "name")
    @JsonProperty("name")
    private String name;

    @DatabaseField(dataType = DataType.STRING, columnName = "specialization")
    @JsonProperty("specialization")
    private String specialization;

    @DatabaseField(dataType = DataType.STRING, columnName = "address")
    @JsonProperty("address")
    private String address;
//
//    @DatabaseField(dataType = DataType.STRING, columnName = "google_address")
//    @JsonProperty("googleAddress")
//    private String googleAddress;
//
//    @DatabaseField(dataType = DataType.STRING, columnName = "region")
//    @JsonProperty("region")
//    private String region;

    @DatabaseField(dataType = DataType.STRING, columnName = "parsed_town")
    @JsonProperty("parsedTown")
    private String parsedTown;

//    @DatabaseField(dataType = DataType.STRING, columnName = "parsed_street")
//    @JsonProperty("parsedStreet")
//    private String parsedStreet;
//
//    @DatabaseField(dataType = DataType.STRING, columnName = "parsed_house")
//    @JsonProperty("parsedHouse")
//    private String parsedHouse;
//
//    @DatabaseField(dataType = DataType.STRING, columnName = "parsed_corpus")
//    @JsonProperty("parsedCorpus")
//    private String parsedCorpus;
//
//    @DatabaseField(dataType = DataType.STRING, columnName = "parsed_building")
//    @JsonProperty("parsedBuilding")
//    private String parsedBuilding;
//
//    @DatabaseField(dataType = DataType.STRING, columnName = "parsed_region")
//    @JsonProperty("parsedRegion")
//    private String parsedRegion;

    @DatabaseField(dataType = DataType.DOUBLE, columnName = "lat")
    @JsonProperty("lat")
    private double lat;

    @DatabaseField(dataType = DataType.DOUBLE, columnName = "lng")
    @JsonProperty("lng")
    private double lng;

    @DatabaseField(dataType = DataType.INTEGER, columnName = "priority")
    @JsonProperty("priority")
    private int priority;

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
     * @return The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The specialization
     */
    @JsonProperty("specialization")
    public String getSpecialization() {
        return specialization;
    }

    /**
     * @param specialization The specialization
     */
    @JsonProperty("specialization")
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    /**
     * @return The address
     */
    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    /**
     * @param address The address
     */
    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

//    /**
//     * @return The googleAddress
//     */
//    @JsonProperty("googleAddress")
//    public String getGoogleAddress() {
//        return googleAddress;
//    }
//
//    /**
//     * @param googleAddress The googleAddress
//     */
//    @JsonProperty("googleAddress")
//    public void setGoogleAddress(String googleAddress) {
//        this.googleAddress = googleAddress;
//    }
//
//    /**
//     * @return The region
//     */
//    @JsonProperty("region")
//    public String getRegion() {
//        return region;
//    }
//
//    /**
//     * @param region The region
//     */
//    @JsonProperty("region")
//    public void setRegion(String region) {
//        this.region = region;
//    }

    /**
     * @return The parsedTown
     */
    @JsonProperty("parsedTown")
    public String getParsedTown() {
        return parsedTown;
    }

    /**
     * @param parsedTown The parsedTown
     */
    @JsonProperty("parsedTown")
    public void setParsedTown(String parsedTown) {
        this.parsedTown = parsedTown;
    }

//    /**
//     * @return The parsedStreet
//     */
//    @JsonProperty("parsedStreet")
//    public String getParsedStreet() {
//        return parsedStreet;
//    }
//
//    /**
//     * @param parsedStreet The parsedStreet
//     */
//    @JsonProperty("parsedStreet")
//    public void setParsedStreet(String parsedStreet) {
//        this.parsedStreet = parsedStreet;
//    }
//
//    /**
//     * @return The parsedHouse
//     */
//    @JsonProperty("parsedHouse")
//    public String getParsedHouse() {
//        return parsedHouse;
//    }
//
//    /**
//     * @param parsedHouse The parsedHouse
//     */
//    @JsonProperty("parsedHouse")
//    public void setParsedHouse(String parsedHouse) {
//        this.parsedHouse = parsedHouse;
//    }
//
//    /**
//     * @return The parsedCorpus
//     */
//    @JsonProperty("parsedCorpus")
//    public String getParsedCorpus() {
//        return parsedCorpus;
//    }
//
//    /**
//     * @param parsedCorpus The parsedCorpus
//     */
//    @JsonProperty("parsedCorpus")
//    public void setParsedCorpus(String parsedCorpus) {
//        this.parsedCorpus = parsedCorpus;
//    }
//
//    /**
//     * @return The parsedBuilding
//     */
//    @JsonProperty("parsedBuilding")
//    public String getParsedBuilding() {
//        return parsedBuilding;
//    }
//
//    /**
//     * @param parsedBuilding The parsedBuilding
//     */
//    @JsonProperty("parsedBuilding")
//    public void setParsedBuilding(String parsedBuilding) {
//        this.parsedBuilding = parsedBuilding;
//    }
//
//    /**
//     * @return The parsedRegion
//     */
//    @JsonProperty("parsedRegion")
//    public String getParsedRegion() {
//        return parsedRegion;
//    }
//
//    /**
//     * @param parsedRegion The parsedRegion
//     */
//    @JsonProperty("parsedRegion")
//    public void setParsedRegion(String parsedRegion) {
//        this.parsedRegion = parsedRegion;
//    }

    /**
     * @return The lat
     */
    @JsonProperty("lat")
    public double getLat() {
        return lat;
    }

    /**
     * @param lat The lat
     */
    @JsonProperty("lat")
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * @return The lng
     */
    @JsonProperty("lng")
    public double getLng() {
        return lng;
    }

    /**
     * @param lng The lng
     */
    @JsonProperty("lng")
    public void setLng(double lng) {
        this.lng = lng;
    }

    /**
     * @return The priority
     */
    @JsonProperty("priority")
    public int getPriority() {
        return priority;
    }

    /**
     * @param priority The priority
     */
    @JsonProperty("priority")
    public void setPriority(int priority) {
        this.priority = priority;
    }

    ///////////////////////////////////////////////////////////////////////
    // implements Parcelable
    ///////////////////////////////////////////////////////////////////////

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.specialization);
        dest.writeString(this.address);
//        dest.writeString(this.googleAddress);
//        dest.writeString(this.region);
//        dest.writeString(this.parsedTown);
//        dest.writeString(this.parsedStreet);
//        dest.writeString(this.parsedHouse);
//        dest.writeString(this.parsedCorpus);
//        dest.writeString(this.parsedBuilding);
//        dest.writeString(this.parsedRegion);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeInt(this.priority);
    }

    public Institution() {
    }

    private Institution(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.specialization = in.readString();
        this.address = in.readString();
//        this.googleAddress = in.readString();
//        this.region = in.readString();
//        this.parsedTown = in.readString();
//        this.parsedStreet = in.readString();
//        this.parsedHouse = in.readString();
//        this.parsedCorpus = in.readString();
//        this.parsedBuilding = in.readString();
//        this.parsedRegion = in.readString();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.priority = in.readInt();
    }

    public static final Parcelable.Creator<Institution> CREATOR = new Parcelable.Creator<Institution>() {
        public Institution createFromParcel(Parcel source) {
            return new Institution(source);
        }

        public Institution[] newArray(int size) {
            return new Institution[size];
        }
    };

    ///////////////////////////////////////////////////////////////////////

}