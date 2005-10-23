package com.medigy.persist.model.invoice.attribute;

import com.medigy.persist.model.common.EffectiveDates;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Invoice attribute for a date duration
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class InvoiceDurationAttribute extends InvoiceAttribute
{
    private EffectiveDates effectiveDates = new EffectiveDates();

    @Basic(temporalType = TemporalType.DATE)
    @Column(nullable =  false)
    public Date getFromDate()
    {
        return effectiveDates.getFromDate();
    }

    public void setFromDate(final Date fromDate)
    {
        effectiveDates.setFromDate(fromDate);
    }

    @Basic(temporalType = TemporalType.DATE)
    @Column(nullable =  false)
    public Date getThroughDate()
    {
        return effectiveDates.getThroughDate();
    }

    public void setThroughDate(final Date throughDate)
    {
        effectiveDates.setThroughDate(throughDate);
    }

}
