/*
 * Copyright (c) 2000-2003 Netspective Communications LLC. All rights reserved.
 *
 * Netspective Communications LLC ("Netspective") permits redistribution, modification and use of this file in source
 * and binary form ("The Software") under the Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the canonical license and must be accepted
 * before using The Software. Any use of The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only (as Java .class files or a .jar file
 *    containing the .class files) and only as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software development kit, other library,
 *    or development tool without written consent of Netspective. Any modified form of The Software is bound by these
 *    same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of The License, normally in a plain
 *    ASCII text file unless otherwise agreed to, in writing, by Netspective.
 *
 * 4. The names "Netspective", "Axiom", "Commons", "Junxion", and "Sparx" are trademarks of Netspective and may not be
 *    used to endorse products derived from The Software without without written consent of Netspective. "Netspective",
 *    "Axiom", "Commons", "Junxion", and "Sparx" may not appear in the names of products derived from The Software
 *    without written consent of Netspective.
 *
 * 5. Please attribute functionality where possible. We suggest using the "powered by Netspective" button or creating
 *    a "powered by Netspective(tm)" link to http://www.netspective.com for each application using The Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF HE HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 */
package com.medigy.persist.model.invoice;

import com.medigy.persist.TestCase;
import com.medigy.persist.model.party.Party;
import com.medigy.persist.reference.custom.invoice.InvoiceStatusType;
import com.medigy.persist.reference.custom.invoice.InvoiceTermType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TestInvoice  extends TestCase
{
    private static final Log log = LogFactory.getLog(TestInvoice.class);

    public void testInvoice()
    {
        Session session = openSession();
        final Transaction transaction = session.beginTransaction();
        final Invoice invoice = new Invoice();
        invoice.setDescription("New invoice");

        invoice.addInvoiceAttribute("Attribute 1", (long) 123);
        invoice.addInvoiceAttribute("Attribute 2", "hello world");

        final InvoiceTermType termType = new InvoiceTermType();
        termType.setCode("PAYMENT");
        termType.setLabel("Payment - net days");
        termType.setParty(Party.Cache.SYS_GLOBAL_PARTY.getEntity());
        session.save(termType);

        final InvoiceTerm term = new InvoiceTerm();
        term.setInvoice(invoice);
        term.setType(termType);
        term.setTermValue(new Long(30));
        invoice.getInvoiceTerms().add(term);
        session.save(invoice);

        Calendar cal = new GregorianCalendar();
        cal.set(2005, 1, 5);
        final InvoiceStatus status = new InvoiceStatus();
        status.setType(InvoiceStatusType.Cache.ON_HOLD.getEntity());
        status.setInvoiceStatusDate(cal.getTime());
        invoice.addInvoiceStatus(status);

        cal.set(2005, 1, 1);
        final InvoiceStatus newStatus = new InvoiceStatus();
        newStatus.setType(InvoiceStatusType.Cache.CREATED.getEntity());
        newStatus.setInvoiceStatusDate(cal.getTime());
        invoice.addInvoiceStatus(newStatus);

        cal.set(2005, 1, 11);
        final InvoiceStatus voidStatus = new InvoiceStatus();
        voidStatus.setType(InvoiceStatusType.Cache.VOID.getEntity());
        voidStatus.setInvoiceStatusDate(cal.getTime());
        invoice.addInvoiceStatus(voidStatus);

        final InvoiceItem item1 = new InvoiceItem();
        item1.setAmount(new Float(100.50));
        invoice.addInvoiceItem(item1);

        final InvoiceItem item2 = new InvoiceItem();
        item2.setAmount(new Float(200.00));
        invoice.addInvoiceItem(item2);

        session.update(invoice);
        transaction.commit();
        session.close();

        session = openSession();
        final Long invoiceId = invoice.getInvoiceId();
        Invoice savedInvoice = (Invoice) session.createCriteria(Invoice.class).add(Restrictions.eq("invoiceId", invoiceId)).uniqueResult();

        assertEquals(1, savedInvoice.getInvoiceTerms().size());
        InvoiceTerm savedInvoiceTerm = (InvoiceTerm) savedInvoice.getInvoiceTerms().toArray()[0];
        assertEquals(savedInvoiceTerm.getTermValue(), new Long(30));

        assertEquals("New invoice", savedInvoice.getDescription());
        log.info("VALID: Invoice");
        assertEquals(3, savedInvoice.getInvoiceStatuses().size());
        log.info("VALID: Invoice status count");
        assertEquals(savedInvoice.getInvoiceStatuses().get(0).getType().getInvoiceStatusTypeId(), InvoiceStatusType.Cache.VOID.getEntity().getInvoiceStatusTypeId());
        assertEquals(savedInvoice.getInvoiceStatuses().get(2).getType().getInvoiceStatusTypeId(), InvoiceStatusType.Cache.CREATED.getEntity().getInvoiceStatusTypeId());
        assertEquals(voidStatus.getInvoiceStatusId(), savedInvoice.getCurrentInvoiceStatus().getInvoiceStatusId());
        assertEquals(savedInvoice.getCurrentInvoiceStatus().getType(), InvoiceStatusType.Cache.VOID.getEntity());
        log.info("VALID: Current Invoice status type");
        assertEquals(1, savedInvoice.getInvoiceTerms().size());
        log.info("VALID: Invoice term count");
        assertEquals(((InvoiceTerm) savedInvoice.getInvoiceTerms().toArray()[0]).getType(), termType);
        log.info("VALID: Invoice term type");
        session.close();
    }


}
