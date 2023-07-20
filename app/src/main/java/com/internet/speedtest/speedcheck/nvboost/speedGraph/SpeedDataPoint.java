package com.internet.speedtest.speedcheck.nvboost.speedGraph;

public class SpeedDataPoint {
    private long timestamp;
    private float downloadSpeed;
    private float uploadSpeed;

    public SpeedDataPoint(long timestamp, float downloadSpeed, float uploadSpeed) {
        this.timestamp = timestamp;
        this.downloadSpeed = downloadSpeed;
        this.uploadSpeed = uploadSpeed;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public float getDownloadSpeed() {
        return downloadSpeed;
    }

    public float getUploadSpeed() {
        return uploadSpeed;
    }
}
