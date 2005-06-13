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
 * @author Shahid N. Shah
 */

/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.form.person.patient;

import com.medigy.persist.reference.type.GenderType;
import com.medigy.persist.reference.type.MaritalStatusType;
import com.medigy.wicket.form.BaseForm;
import com.medigy.wicket.form.FieldFlags;
import com.medigy.wicket.form.FieldGroupLabel;
import com.medigy.wicket.form.TextField;
import com.medigy.wicket.panel.DefaultFormPanel;
import wicket.IFeedback;

public class PatientRegistrationFormPanel extends DefaultFormPanel
{
    private TextField personId;

    public PatientRegistrationFormPanel(final String componentName)
    {
        super(componentName);
    }

    protected Form createForm(final String componentName, final IFeedback feedback)
    {
        return new Form(componentName, feedback);
    }

    protected class Form extends BaseForm
    {

        public Form(final String componentName, final IFeedback feedback)
        {
            super(componentName, feedback);
            add(new FieldGroupLabel("patientId"));

            addLabeledTextField("personId", FieldFlags.REQUIRED);
            addLabeledTextField("account");
            addLabeledTextField("chartNumber");

            addLabeledTextField("lastName", FieldFlags.REQUIRED);
            addLabeledTextField("firstName", FieldFlags.REQUIRED);
            addLabeledTextField("middleName");
            addLabeledSelectField("suffix");

            addLabeledTextField("socialSecurityNumber", FieldFlags.REQUIRED);
            addLabeledTextField("dateOfBirth", FieldFlags.REQUIRED);

            addLabeledSelectField("gender", GenderType.class);
            addLabeledSelectField("maritalStatus", MaritalStatusType.class);
            addLabeledSelectField("bloodType");

            addLabeledRadioChoiceField("ethnicity");  //TODO: convert to checkbox

            addLabeledTextField("responsibleParty");
            addLabeledSelectField("relationshipToResponsible");
            addLabeledTextField("relationshipToResponsibleOtherRelationship");

            addLabeledTextField("driversLicenseNumber");
            addLabeledTextField("driversLicenseState");

            addLabeledTextField("miscNotes");

            add(new FieldGroupLabel("contactMethods"));

            addLabeledTextField("homePhone");
            addLabeledTextField("workPhone");
            addLabeledTextField("cellPhone");
            addLabeledTextField("pager");
            addLabeledTextField("alternate");

            addLabeledTextField("homeAddress");
            addLabeledTextField("homeAddress2");
            addLabeledTextField("city");
            addLabeledTextField("state");
            addLabeledTextField("zip");

            addLabeledTextField("email");

            add(new FieldGroupLabel("employment"));

            addLabeledTextField("employerId");
            addLabeledSelectField("employmentStatus");
            addLabeledTextField("occupation");
            addLabeledTextField("employerPhoneNumber");

            add(new FieldGroupLabel("insurance"));

            addLabeledSelectField("insuranceSequence");
            addLabeledTextField("insuranceProduct");
            addLabeledTextField("insurancePlan");

            add(new FieldGroupLabel("generalPlanInformation"));
            addLabeledSelectField("patientRelationshipToInsured");
            addLabeledTextField("patientRelationshipToInsuredPtherRelationship");
            addLabeledTextField("insuredPersonId");
            addLabeledTextField("employer");
            addLabeledTextField("groupName");
            addLabeledTextField("groupNumber");
            addLabeledTextField("memberNumber");

            add(new FieldGroupLabel("coverageInformation"));
            addLabeledTextField("coverageBeginDate");
            addLabeledTextField("coverageEndDate");
            addLabeledTextField("individualDeductible");
            addLabeledTextField("familyDeductible");
            addLabeledTextField("individualDeductibleRemaining");
            addLabeledTextField("familyDeductibleRemaining");
            addLabeledTextField("percentagePay");
            addLabeledTextField("threshold");
            addLabeledTextField("officeVisitCoPay");
        }

        /**
		 * @see wicket.markup.html.form.Form#onSubmit()
		 */
		public final void onSubmit()
		{
            System.out.println("\n*****************  form submitted  ******************\n");
//			if (signIn(getUsername(), getPassword()))
//			{
//				// If login has been called because the user was not yet
//				// logged in, than continue to the original destination,
//				// otherwise to the Home page
//				if (getPage().continueToOriginalDestination())
//				{
//					// HTTP redirect response has been committed. No more data
//					// shall be written to the response.
//					setResponsePage(null);
//				}
//				else
//				{
//					setResponsePage(getApplicationSettings().getDefaultPageFactory()
//							.newPage(getApplicationPages().getHomePage(),
//									(PageParameters) null));
//				}
//			}
//			else
//			{
//				// Try the component based localizer first. If not found try the
//				// application localizer. Else use the default
//				final String errmsg = getLocalizer().getString("loginError", this,
//						"Unable to sign you in");
//
//				error(errmsg);
//			}
		}
    }
}
