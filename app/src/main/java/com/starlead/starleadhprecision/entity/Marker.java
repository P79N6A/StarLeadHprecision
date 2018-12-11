package com.starlead.starleadhprecision.entity;

import java.io.Serializable;
import java.util.Objects;

public class Marker implements Serializable {
    int MarkerId;
    int PipelineId;
    String MarkerSN;
    String MarkerName;
    double MarkerLng;
    double MarkerLat;
    float MarkerElevation;
    String MarkerImage;
    String MarkerType;
    String MarkerDescription;
    String MarkerLevel;
    String MarkerStatus;
    int MarkerDisplayOrder;
    String MarkerNote;


    public Marker(int markerId, int pipelineId, String markerSN, String markerName, double markerLng, double markerLat, float markerElevation, String markerImage, String markerType, String markerDescription, String markerLevel, String markerStatus, int markerDisplayOrder, String markerNote) {
        MarkerId = markerId;
        PipelineId = pipelineId;
        MarkerSN = markerSN;
        MarkerName = markerName;
        MarkerLng = markerLng;
        MarkerLat = markerLat;
        MarkerElevation = markerElevation;
        MarkerImage = markerImage;
        MarkerType = markerType;
        MarkerDescription = markerDescription;
        MarkerLevel = markerLevel;
        MarkerStatus = markerStatus;
        MarkerDisplayOrder = markerDisplayOrder;
        MarkerNote = markerNote;
    }

    public Marker(int markerId, int pipelineId, double markerLng, double markerLat, String markerImage) {
        MarkerId = markerId;
        PipelineId = pipelineId;
        MarkerLng = markerLng;
        MarkerLat = markerLat;
        MarkerImage = markerImage;
    }

    public Marker() {
    }

    public String getMarkerSN() {
        return MarkerSN;
    }

    public void setMarkerSN(String markerSN) {
        MarkerSN = markerSN;
    }

    public int getMarkerId() {
        return MarkerId;
    }

    public void setMarkerId(int markerId) {
        MarkerId = markerId;
    }

    public int getPipelineId() {
        return PipelineId;
    }

    public void setPipelineId(int pipelineId) {
        PipelineId = pipelineId;
    }

    public String getMarkerName() {
        return MarkerName;
    }

    public void setMarkerName(String markerName) {
        MarkerName = markerName;
    }

    public double getMarkerLng() {
        return MarkerLng;
    }

    public void setMarkerLng(double markerLng) {
        MarkerLng = markerLng;
    }

    public double getMarkerLat() {
        return MarkerLat;
    }

    public void setMarkerLat(double markerLat) {
        MarkerLat = markerLat;
    }

    public float getMarkerElevation() {
        return MarkerElevation;
    }

    public void setMarkerElevation(float markerElevation) {
        MarkerElevation = markerElevation;
    }

    public String getMarkerImage() {
        return MarkerImage;
    }

    public void setMarkerImage(String markerImage) {
        MarkerImage = markerImage;
    }

    public String getMarkerType() {
        return MarkerType;
    }

    public void setMarkerType(String markerType) {
        MarkerType = markerType;
    }

    public String getMarkerDescription() {
        return MarkerDescription;
    }

    public void setMarkerDescription(String markerDescription) {
        MarkerDescription = markerDescription;
    }

    public String getMarkerLevel() {
        return MarkerLevel;
    }

    public void setMarkerLevel(String markerLevel) {
        MarkerLevel = markerLevel;
    }

    public String getMarkerStatus() {
        return MarkerStatus;
    }

    public void setMarkerStatus(String markerStatus) {
        MarkerStatus = markerStatus;
    }

    public int getMarkerDisplayOrder() {
        return MarkerDisplayOrder;
    }

    public void setMarkerDisplayOrder(int markerDisplayOrder) {
        MarkerDisplayOrder = markerDisplayOrder;
    }

    public String getMarkerNote() {
        return MarkerNote;
    }

    public void setMarkerNote(String markerNote) {
        MarkerNote = markerNote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Marker marker = (Marker) o;
        return MarkerId == marker.MarkerId &&
                PipelineId == marker.PipelineId;
    }



    @Override
    public int hashCode() {

        return Objects.hash(MarkerId, PipelineId);
    }
}
