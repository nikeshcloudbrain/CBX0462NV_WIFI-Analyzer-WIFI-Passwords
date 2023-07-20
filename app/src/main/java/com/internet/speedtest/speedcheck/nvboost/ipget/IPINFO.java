package com.internet.speedtest.speedcheck.nvboost.ipget;

import androidx.core.app.NotificationCompat;
import com.google.gson.annotations.SerializedName;

public class IPINFO {
    @SerializedName("city")
    private String city;
    @SerializedName("country")
    private String country;
    @SerializedName("countryCode")
    private String countryCode;
    @SerializedName("org")
    private String f0org;
    @SerializedName("as")
    private String f155as;
    @SerializedName("isp")
    private String isp;
    @SerializedName("lat")
    private String lat;
    @SerializedName("lon")
    private String lon;
    @SerializedName("query")
    private String query;
    @SerializedName("region")
    private String region;
    @SerializedName("regionName")
    private String regionName;
    @SerializedName(NotificationCompat.CATEGORY_STATUS)
    private String status;
    @SerializedName("timezone")
    private String timezone;
    @SerializedName("zip")
    private String zip;
    @SerializedName("mobile")
    private boolean mobile;
    @SerializedName("proxy")
    private boolean proxy;
    @SerializedName("hosting")
    private boolean hosting;
    public void setZip(String str) {
        this.zip = str;
    }

    public String getZip() {
        return this.zip;
    }

    public void setCountry(String str) {
        this.country = str;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCity(String str) {
        this.city = str;
    }

    public String getCity() {
        return this.city;
    }

    public void setOrg(String str) {
        this.f0org = str;
    }

    public String getOrg() {
        return this.f0org;
    }

    public void setTimezone(String str) {
        this.timezone = str;
    }

    public String getTimezone() {
        return this.timezone;
    }

    public void setIsp(String str) {
        this.isp = str;
    }

    public String getIsp() {
        return this.isp;
    }

    public void setQuery(String str) {
        this.query = str;
    }

    public String getQuery() {
        return this.query;
    }

    public void setRegionName(String str) {
        this.regionName = str;
    }

    public String getRegionName() {
        return this.regionName;
    }

    public void setLon(String str) {
        this.lon = str;
    }

    public String getLon() {
        return this.lon;
    }

    public void setAs(String str) {
        this.f155as = str;
    }

    public String getAs() {
        return this.f155as;
    }

    public void setCountryCode(String str) {
        this.countryCode = str;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setRegion(String str) {
        this.region = str;
    }

    public String getRegion() {
        return this.region;
    }

    public void setLat(String str) {
        this.lat = str;
    }

    public String getLat() {
        return this.lat;
    }

    public void setStatus(String str) {
        this.status = str;
    }

    public String getStatus() {
        return this.status;
    }

    public boolean isMobile() {
        return mobile;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }

    public boolean isProxy() {
        return proxy;
    }

    public void setProxy(boolean proxy) {
        this.proxy = proxy;
    }

    public boolean isHosting() {
        return hosting;
    }

    public void setHosting(boolean hosting) {
        this.hosting = hosting;
    }
}
