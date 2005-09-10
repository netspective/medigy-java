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
package com.medigy.app.pbs.page.entity.person;

import com.medigy.app.pbs.panel.common.AddressListPanel;
import com.medigy.app.pbs.panel.common.PhoneListPanel;
import com.medigy.persist.model.insurance.InsurancePolicy;
import com.medigy.persist.model.insurance.ResponsiblePartySelection;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.model.person.PersonIdentifier;
import wicket.markup.html.basic.Label;
import wicket.markup.html.basic.MultiLineLabel;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.panel.Panel;
import wicket.model.IModel;
import wicket.model.Model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class PatientProfilePanel extends Panel
{
    public PatientProfilePanel(final String id, final IModel model)
    {
        super(id, model);

        Person patient = (Person) getModelObject();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        final GregorianCalendar calendar = new GregorianCalendar();
        final int currentYear = calendar.get(Calendar.YEAR);
        calendar.setTime(patient.getBirthDate());
        final int birthYear = calendar.get(Calendar.YEAR);
        // for now only
        add(new Label("fullName", new Model(patient.getFullName())));
        add(new Label("gender", new Model(patient.getCurrentGenderType().getLabel())));
        add(new Label("age", new Model(currentYear - birthYear)));
        add(new Label("birthdate", new Model(sdf.format(patient.getBirthDate()))));
        add(new Label("maritalStatus", new Model(patient.getCurrentMaritalStatus().getLabel())));
        final ResponsiblePartySelection respPersonSelection = patient.getDefaultResponsiblePartySelection();
        if (respPersonSelection.getPeopleRelationship() != null)
        {
            final String label = respPersonSelection.getPeopleRelationship().getSecondaryPersonRole().getType().getLabel();
            final String fullName = respPersonSelection.getPeopleRelationship().getSecondaryPersonRole().getPerson().getFullName();
            add(new Label("responsiblePerson", new Model(fullName + "(" + label + ")")));
        }
        else
        {
            add(new Label("responsiblePerson", new Model("Self")));
        }

        // Add person identifiers
        add(new ListView("identifiers", patient.getPersonIdentifiers())
        {
            public void populateItem(final ListItem listItem)
            {
                final PersonIdentifier identifier = (PersonIdentifier) listItem.getModelObject();
                listItem.add(new MultiLineLabel("identifierName", identifier.getType().getLabel()));
                listItem.add(new Label("identifierValue", new Model(identifier.getIdentifierValue())));
            }
        });

        // Add insurance policies
        add(new ListView("insurancePolicies", patient.getInsurancePolicies())
        {
            public void populateItem(final ListItem listItem)
            {
                final InsurancePolicy policy = (InsurancePolicy) listItem.getModelObject();
                listItem.add(new MultiLineLabel("type", policy.getType().getLabel()));
                listItem.add(new Label("carrierName", new Model(policy.getInsuranceProvider().getOrganizationName())));
                listItem.add(new MultiLineLabel("policyNumber", policy.getPolicyNumber()));
            }
        });
        System.out.println("Phone numbers: " + patient.getPhoneNumbers().size());
        add(new AddressListPanel("addressList", patient.getAddresses()));
        add(new PhoneListPanel("phoneList", patient.getPhoneNumbers()));
    }
}
