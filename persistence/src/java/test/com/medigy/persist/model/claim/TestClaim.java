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
package com.medigy.persist.model.claim;

import com.medigy.persist.TestCase;
import com.medigy.persist.model.invoice.Invoice;
import com.medigy.persist.model.invoice.Payment;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.custom.claim.ClaimType;
import com.medigy.persist.reference.custom.health.DiagnosisType;
import com.medigy.persist.reference.custom.invoice.InvoiceStatusType;
import com.medigy.persist.reference.type.GenderType;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class TestClaim extends TestCase
{

    public void testClaim()
    {
        Session session = openSession();
        Transaction transaction = session.beginTransaction();

        final Invoice invoice = new Invoice();
        invoice.addInvoiceStatus(InvoiceStatusType.Cache.CREATED.getEntity());
        invoice.setInvoiceDate(new Date());

        session.save(invoice);

        final Claim claim = new Claim();
        claim.setInvoice(invoice);
        claim.setType(ClaimType.Cache.SELFPAY.getEntity());

        final ClaimItem item1 = new ClaimItem();
        item1.setClaimAmount(new Float(1000));
        item1.setClaim(claim);
        item1.addDiagnosticCode(DiagnosisType.Cache.ICD9_CODE.getEntity(), null);
        item1.addDiagnosticCode(DiagnosisType.Cache.ICD9_CODE.getEntity(), null);

        final ClaimItem item2 = new ClaimItem();
        item2.setClaimAmount(new Float(500));
        item2.setClaim(claim);
        item2.addDiagnosticCode(DiagnosisType.Cache.ICD9_CODE.getEntity(), null);

        claim.addClaimItem(item1);
        claim.addClaimItem(item2);
        session.save(claim);
        transaction.commit();
        session.close();

        session = openSession();
        transaction = session.beginTransaction();
        Claim savedClaim = (Claim) session.createCriteria(Claim.class).add(Restrictions.eq("claimId", claim.getClaimId())).uniqueResult();
        assertEquals(savedClaim.getType().getClaimTypeId(), ClaimType.Cache.SELFPAY.getEntity().getClaimTypeId());
        assertEquals(2, claim.getClaimItems().size());
        final ClaimItem savedClaimItem1 = savedClaim.getClaimItems().get(0);
        final ClaimItem savedClaimItem2 = savedClaim.getClaimItems().get(0);
        final List<ClaimItemDiagnosisCode> diagnosticCodes = savedClaimItem1.getDiagnosticCodes();
        assertEquals(2, diagnosticCodes.size());

        final ClaimSettlement item1settlementA = new ClaimSettlement();
        final Date settledDate = new Date();
        item1settlementA.setSettledDate(settledDate);

        final ClaimSettlement item1settlementB = new ClaimSettlement();
        item1settlementB.setSettledDate(settledDate);

        final Person payer = new Person();
        payer.setLastName("Hackett");
        payer.setFirstName("Ryan");
        payer.addGender(GenderType.Cache.MALE.getEntity());
        final Calendar instance = Calendar.getInstance();
        instance.set(1980, 1 , 1);
        payer.setBirthDate(instance.getTime());
        session.save(payer);

        final Organization  payee = new Organization();
        payee.setOrganizationName("Acme Billing service");
        session.save(payee);

        Calendar cal = new GregorianCalendar();
        cal.setTime(settledDate);
        cal.add(Calendar.DAY_OF_MONTH, -5);
        final Date paymentDate = cal.getTime();
        final  Payment checkPayment = new Payment();
        checkPayment.setPersonalCheckPayment(new Float(1000), "123");
        checkPayment.setEffectiveDate(paymentDate);
        checkPayment.setPayer(payer);
        checkPayment.setPayee(payee);
        session.save(checkPayment);
        final  Payment cashPayment = new Payment();
        cashPayment.setCashPayment(new Float(100));
        cashPayment.setEffectiveDate(paymentDate);
        cashPayment.setPayer(payer);
        cashPayment.setPayee(payee);
        session.save(cashPayment);

        // $500 applied towards one settlement and $500 towards another
        item1settlementA.addSettlementAmount(checkPayment, new Float(500));
        item1settlementB.addSettlementAmount(checkPayment, new Float(500));
        item1settlementB.addSettlementAmount(cashPayment);

        savedClaimItem1.addClaimSettlement(item1settlementA);
        savedClaimItem2.addClaimSettlement(item1settlementB);
        session.save(savedClaimItem1);
        session.save(savedClaimItem2);
        transaction.commit();
        session.close();

        session = openSession();
        savedClaim = (Claim) session.createCriteria(Claim.class).add(Restrictions.eq("claimId", claim.getClaimId())).uniqueResult();
        final List<ClaimItem> claimItems = savedClaim.getClaimItems();
        assertEquals(2, claimItems.get(0).getClaimSettlements().size());
        assertEquals(1, claimItems.get(1).getClaimSettlements().size());
        assertEquals(2, claimItems.get(1).getClaimSettlements().get(0).getSettlementAmounts().size());
    }
}
