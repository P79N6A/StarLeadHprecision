package com.starlead.starleadhprecision.entity;

import java.util.Objects;

public class PatrolSchemeGroup {
    int PatrolSchemeGroupId;
    int PatPatrolSchemeGroupId;
    String PatrolSchemeGroupSN;
    String PatrolSchemeGroupName;
    int PatrolSchemeGroupDisplayOrder;
    String PatrolSchemeGroupNote;


    public PatrolSchemeGroup(int patrolSchemeGroupId, int patPatrolSchemeGroupId, String patrolSchemeGroupSN, String patrolSchemeGroupName, int patrolSchemeGroupDisplayOrder, String patrolSchemeGroupNote) {
        PatrolSchemeGroupId = patrolSchemeGroupId;
        PatPatrolSchemeGroupId = patPatrolSchemeGroupId;
        PatrolSchemeGroupSN = patrolSchemeGroupSN;
        PatrolSchemeGroupName = patrolSchemeGroupName;
        PatrolSchemeGroupDisplayOrder = patrolSchemeGroupDisplayOrder;
        PatrolSchemeGroupNote = patrolSchemeGroupNote;
    }

    public PatrolSchemeGroup() {
    }

    public int getPatrolSchemeGroupId() {
        return PatrolSchemeGroupId;
    }

    public void setPatrolSchemeGroupId(int patrolSchemeGroupId) {
        PatrolSchemeGroupId = patrolSchemeGroupId;
    }

    public int getPatPatrolSchemeGroupId() {
        return PatPatrolSchemeGroupId;
    }

    public void setPatPatrolSchemeGroupId(int patPatrolSchemeGroupId) {
        PatPatrolSchemeGroupId = patPatrolSchemeGroupId;
    }

    public String getPatrolSchemeGroupSN() {
        return PatrolSchemeGroupSN;
    }

    public void setPatrolSchemeGroupSN(String patrolSchemeGroupSN) {
        PatrolSchemeGroupSN = patrolSchemeGroupSN;
    }

    public String getPatrolSchemeGroupName() {
        return PatrolSchemeGroupName;
    }

    public void setPatrolSchemeGroupName(String patrolSchemeGroupName) {
        PatrolSchemeGroupName = patrolSchemeGroupName;
    }

    public int getPatrolSchemeGroupDisplayOrder() {
        return PatrolSchemeGroupDisplayOrder;
    }

    public void setPatrolSchemeGroupDisplayOrder(int patrolSchemeGroupDisplayOrder) {
        PatrolSchemeGroupDisplayOrder = patrolSchemeGroupDisplayOrder;
    }

    public String getPatrolSchemeGroupNote() {
        return PatrolSchemeGroupNote;
    }

    public void setPatrolSchemeGroupNote(String patrolSchemeGroupNote) {
        PatrolSchemeGroupNote = patrolSchemeGroupNote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatrolSchemeGroup that = (PatrolSchemeGroup) o;
        return PatrolSchemeGroupId == that.PatrolSchemeGroupId &&
                PatPatrolSchemeGroupId == that.PatPatrolSchemeGroupId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(PatrolSchemeGroupId, PatPatrolSchemeGroupId);
    }
}
