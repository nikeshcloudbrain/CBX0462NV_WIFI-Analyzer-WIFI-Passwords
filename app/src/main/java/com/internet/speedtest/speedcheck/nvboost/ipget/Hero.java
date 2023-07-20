package com.internet.speedtest.speedcheck.nvboost.ipget;

/* loaded from: classes2.dex */
public class Hero {
    private String city;
    private String country_code;
    private String country_name;
    private String ip;
    private String latitude;
    private String longitude;
    private String metro_code;
    private String region_code;
    private String region_name;
    private String time_zone;
    private String zip_code;

    public Hero(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11) {
        this.ip = str;
        this.country_code = str2;
        this.country_name = str3;
        this.region_name = str5;
        this.region_code = str4;
        this.city = str6;
        this.zip_code = str7;
        this.latitude = str9;
        this.longitude = str10;
        this.metro_code = str11;
        this.time_zone = str8;
    }

    public String getMetro_code() {
        return this.metro_code;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public String getZip_code() {
        return this.zip_code;
    }

    public String getCity() {
        return this.city;
    }

    public String getRegion_name() {
        return this.region_name;
    }

    public String getRegion_code() {
        return this.region_code;
    }

    public String getCountry_name() {
        return this.country_name;
    }

    public String getCountry_code() {
        return this.country_code;
    }

    public String getIp() {
        return this.ip;
    }

    public String getTime_zone() {
        return this.time_zone;
    }
}
