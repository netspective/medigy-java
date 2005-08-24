package com.medigy.presentation.scheduler.component;

public class SummaryComponent extends AbstractScheduleDeskComponent
{
    private Integer patientCount;

    public SummaryComponent(final String componentId)
    {
        super(componentId);
    }

    public void setPatientCount(final Integer patientCount)
    {
        this.patientCount = patientCount;
    }

    public Integer getPatientCount()
    {
        return patientCount;
    }
}
