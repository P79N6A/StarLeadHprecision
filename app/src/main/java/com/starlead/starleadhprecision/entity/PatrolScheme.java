package com.starlead.starleadhprecision.entity;

import java.util.Date;
import java.util.Objects;

public class PatrolScheme {

    int PatrolSchemeId;
    int PatrolSchemeGroupId;
    String PatrolSchemeSN;
    String PatrolSchemeName;
    Date PatrolSchemePlanTime;
    Date PatrolSchemePlanExecutionTime;
    String PatrolSchemeStatus;
    String PatrolSchemeDescription;
    int PatrolSchemeDisplayOrder;
    String PatrolSchemeNote;

    public PatrolScheme(int patrolSchemeId, int patrolSchemeGroupId, String patrolSchemeSN, String patrolSchemeName, Date patrolSchemePlanTime, Date patrolSchemePlanExecutionTime, String patrolSchemeStatus, String patrolSchemeDescription, int patrolSchemeDisplayOrder, String patrolSchemeNote) {
        PatrolSchemeId = patrolSchemeId;
        PatrolSchemeGroupId = patrolSchemeGroupId;
        PatrolSchemeSN = patrolSchemeSN;
        PatrolSchemeName = patrolSchemeName;
        PatrolSchemePlanTime = patrolSchemePlanTime;
        PatrolSchemePlanExecutionTime = patrolSchemePlanExecutionTime;
        PatrolSchemeStatus = patrolSchemeStatus;
        PatrolSchemeDescription = patrolSchemeDescription;
        PatrolSchemeDisplayOrder = patrolSchemeDisplayOrder;
        PatrolSchemeNote = patrolSchemeNote;
    }

    public PatrolScheme() {

    }

    public String getPatrolSchemeStatus() {
        return PatrolSchemeStatus;
    }

    public void setPatrolSchemeStatus(String patrolSchemeStatus) {
        PatrolSchemeStatus = patrolSchemeStatus;
    }

    public String getPatrolSchemeDescription() {
        return PatrolSchemeDescription;
    }

    public void setPatrolSchemeDescription(String patrolSchemeDescription) {
        PatrolSchemeDescription = patrolSchemeDescription;
    }

    public int getPatrolSchemeDisplayOrder() {
        return PatrolSchemeDisplayOrder;
    }

    public void setPatrolSchemeDisplayOrder(int patrolSchemeDisplayOrder) {
        PatrolSchemeDisplayOrder = patrolSchemeDisplayOrder;
    }

    public String getPatrolSchemeNote() {
        return PatrolSchemeNote;
    }

    public void setPatrolSchemeNote(String patrolSchemeNote) {
        PatrolSchemeNote = patrolSchemeNote;
    }

    public int getPatrolSchemeId() {
        return PatrolSchemeId;
    }

    public void setPatrolSchemeId(int patrolSchemeId) {
        PatrolSchemeId = patrolSchemeId;
    }

    public int getPatrolSchemeGroupId() {
        return PatrolSchemeGroupId;
    }

    public void setPatrolSchemeGroupId(int patrolSchemeGroupId) {
        PatrolSchemeGroupId = patrolSchemeGroupId;
    }

    public String getPatrolSchemeSN() {
        return PatrolSchemeSN;
    }

    public void setPatrolSchemeSN(String patrolSchemeSN) {
        PatrolSchemeSN = patrolSchemeSN;
    }

    public String getPatrolSchemeName() {
        return PatrolSchemeName;
    }

    public void setPatrolSchemeName(String patrolSchemeName) {
        PatrolSchemeName = patrolSchemeName;
    }

    public Date getPatrolSchemePlanTime() {
        return PatrolSchemePlanTime;
    }

    public void setPatrolSchemePlanTime(Date patrolSchemePlanTime) {
        PatrolSchemePlanTime = patrolSchemePlanTime;
    }

    public Date getPatrolSchemePlanExecutionTime() {
        return PatrolSchemePlanExecutionTime;
    }

    public void setPatrolSchemePlanExecutionTime(Date patrolSchemePlanExecutionTime) {
        PatrolSchemePlanExecutionTime = patrolSchemePlanExecutionTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatrolScheme that = (PatrolScheme) o;
        return PatrolSchemeId == that.PatrolSchemeId &&
                PatrolSchemeGroupId == that.PatrolSchemeGroupId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(PatrolSchemeId, PatrolSchemeGroupId);
    }
}
