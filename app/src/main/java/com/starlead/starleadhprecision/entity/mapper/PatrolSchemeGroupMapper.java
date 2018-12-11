package com.starlead.starleadhprecision.entity.mapper;

import com.starlead.starleadhprecision.entity.PatrolSchemeGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PatrolSchemeGroupMapper implements RowMapper<PatrolSchemeGroup> {
    @Override
    public List<PatrolSchemeGroup> mapRow(JSONObject jsonObject) throws Exception {
        List<PatrolSchemeGroup> list = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("schemegroups");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            PatrolSchemeGroup patrolSchemeGroup = new PatrolSchemeGroup();

            if (object.has("patPatrolSchemeGroupId")) {
                patrolSchemeGroup.setPatPatrolSchemeGroupId(object.getInt("patPatrolSchemeGroupId"));
            }
            if (object.has("patrolSchemeGroupDisplayOrder")) {
                patrolSchemeGroup.setPatrolSchemeGroupDisplayOrder(object.getInt("patrolSchemeGroupDisplayOrder"));
            }
            if (object.has("patrolSchemeGroupId")) {
                patrolSchemeGroup.setPatrolSchemeGroupId(object.getInt("patrolSchemeGroupId"));
            }
            if (object.has("patrolSchemeGroupName")) {
                patrolSchemeGroup.setPatrolSchemeGroupName(object.getString("patrolSchemeGroupName"));
            }
            if (object.has("patrolSchemeGroupSN")) {
                patrolSchemeGroup.setPatrolSchemeGroupSN(object.getString("patrolSchemeGroupSN"));
            }
            if (object.has("patrolSchemeGroupNote")) {
                patrolSchemeGroup.setPatrolSchemeGroupNote(object.getString("patrolSchemeGroupNote"));
            }
            list.add(patrolSchemeGroup);
        }
        return list;

    }

}
