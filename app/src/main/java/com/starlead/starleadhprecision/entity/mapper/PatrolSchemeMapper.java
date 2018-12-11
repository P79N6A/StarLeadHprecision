package com.starlead.starleadhprecision.entity.mapper;

import com.starlead.starleadhprecision.entity.PatrolScheme;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatrolSchemeMapper implements RowMapper<PatrolScheme> {
    @Override
    public List<PatrolScheme> mapRow(JSONObject jsonObject) throws Exception {
        List<PatrolScheme> list = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("patrolschemelist");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            PatrolScheme patrolScheme = new PatrolScheme();
            if (object.has("patrolSchemeId")) {
                patrolScheme.setPatrolSchemeId(object.getInt("patrolSchemeId"));
            }
            if (object.has("patrolSchemeGroupId")) {
                patrolScheme.setPatrolSchemeGroupId(object.getInt("patrolSchemeGroupId"));
            }
            if (object.has("patrolSchemeSN")) {
                patrolScheme.setPatrolSchemeSN(object.getString("patrolSchemeSN"));
            }
            if (object.has("patrolSchemeName")) {
                patrolScheme.setPatrolSchemeName(object.getString("patrolSchemeName"));
            }
            if (object.has("patrolSchemePlanTime")) {
                patrolScheme.setPatrolSchemePlanTime(new Date(object.getString("patrolSchemePlanTime")));
            }
            if (object.has("patrolSchemePlanExecutionTime")) {
                patrolScheme.setPatrolSchemePlanExecutionTime(new Date(object.getString("patrolSchemePlanExecutionTime")));
            }
            if (object.has("patrolSchemeStatus")) {
                patrolScheme.setPatrolSchemeStatus(object.getString("patrolSchemeStatus"));
            }
            if (object.has("patrolSchemeDescription")) {
                patrolScheme.setPatrolSchemeDescription(object.getString("patrolSchemeDescription"));
            }
            if (object.has("patrolSchemeDisplayOrder")) {
                patrolScheme.setPatrolSchemeDisplayOrder(object.getInt("patrolSchemeDisplayOrder"));
            }
            if (object.has("patrolSchemeNote")) {
                patrolScheme.setPatrolSchemeNote(object.getString("patrolSchemeNote"));
            }
            list.add(patrolScheme);
        }
        return list;
    }
}
