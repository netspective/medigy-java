package com.medigy.persist.model.party;

import com.medigy.persist.model.work.WorkEffort;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Class for relating work/task to a communication event.
 */
@Entity
@Table(name = "Comm_Event_Work")
public class CommunicationEventWork
{
    public static final String PK_COLUMN_NAME = "work_id";

    private Long workId;
    private String description;
    private CommunicationEvent communicationEvent;
    private WorkEffort workEffort;
    // indicates whether or not the workEffort was started by this particular communication communicationEvent
    private Boolean startWorkInd;

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getWorkId()
    {
        return workId;
    }

    public void setWorkId(final Long workId)
    {
        this.workId = workId;
    }

    /**
     * Checks to see if the work has started or not
     * @return
     */
    public Boolean getStartWorkInd()
    {
        return startWorkInd;
    }

    public void setStartWorkInd(final Boolean startWorkInd)
    {
        this.startWorkInd = startWorkInd;
    }

    /**
     * Gets the related work
     * @return
     */
    @ManyToOne
    @JoinColumn(name = WorkEffort.PK_COLUMN_NAME)
    public WorkEffort getWork()
    {
        return workEffort;
    }

    public void setWork(final WorkEffort workEffort)
    {
        this.workEffort = workEffort;
    }

    /**
     * Gets the related communiation event
     * @return
     */
    @ManyToOne
    @JoinColumn(name = CommunicationEvent.PK_COLUMN_NAME)
    public CommunicationEvent getCommunicationEvent()
    {
        return communicationEvent;
    }

    public void setCommunicationEvent(final CommunicationEvent communicationEvent)
    {
        this.communicationEvent = communicationEvent;
    }

    @Column(length = 100, nullable = true)
    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }
}
