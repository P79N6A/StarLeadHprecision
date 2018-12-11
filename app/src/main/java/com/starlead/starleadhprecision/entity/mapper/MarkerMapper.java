package com.starlead.starleadhprecision.entity.mapper;


import com.starlead.starleadhprecision.entity.Marker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MarkerMapper implements RowMapper<Marker> {
    @Override
    public List<Marker> mapRow(JSONObject jsonObject) throws Exception {

        List<Marker> list = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("markers");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            Marker marker = new Marker();

            if (object.has("markerId")) {
                marker.setMarkerId(object.getInt("markerId"));
            }
            if (object.has("markerDisplayOrder")) {
                marker.setMarkerDisplayOrder(object.getInt("markerDisplayOrder"));
            }
            if (object.has("pipelineId")) {
                marker.setPipelineId(object.getInt("pipelineId"));
            }
            if (object.has("markerName")) {
                marker.setMarkerName(object.getString("markerName"));
            }
            if (object.has("markerSN")) {
                marker.setMarkerSN(object.getString("markerSN"));
            }
            if (object.has("markerImage")) {
                marker.setMarkerImage(object.getString("markerImage"));
            }
            if (object.has("markerLat")) {
                marker.setMarkerLat(object.getDouble("markerLat"));
            }
            if (object.has("markerLng")) {
                marker.setMarkerLng(object.getDouble("markerLng"));
            }
            if (object.has("markerLng")) {
                marker.setMarkerLng(object.getDouble("markerLng"));
            }
            if (object.has("markerElevation")) {
                marker.setMarkerElevation((float) object.getDouble("markerElevation"));
            }
            if (object.has("markerLevel")) {
                marker.setMarkerLevel(object.getString("markerLevel"));
            }
            if (object.has("markerStatus")) {
                marker.setMarkerStatus(object.getString("markerStatus"));
            }
            if (object.has("markerType")) {
                marker.setMarkerType(object.getString("markerType"));
            }
            if (object.has("markerNote")) {
                marker.setMarkerNote(object.getString("markerNote"));
            }
            list.add(marker);
        }
        return list;
    }
}
