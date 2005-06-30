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

import com.medigy.persist.reference.custom.person.EthnicityType;
import com.medigy.persist.reference.type.BloodType;
import com.medigy.persist.reference.type.GenderType;
import com.medigy.persist.reference.type.MaritalStatusType;
import com.medigy.persist.reference.type.PersonNamePrefixType;
import com.medigy.presentation.model.TestServiceParameterModel;
import com.medigy.service.dto.person.RegisterPatientParameters;
import com.medigy.service.person.PatientRegistrationService;
import com.medigy.wicket.DefaultApplication;
import com.medigy.wicket.form.BaseForm;
import com.medigy.wicket.form.FieldGroupLabel;
import com.medigy.wicket.form.TextField;
import com.medigy.wicket.panel.DefaultFormPanel;
import wicket.IFeedback;
import wicket.model.BoundCompoundPropertyModel;

public class PatientRegistrationFormPanel extends DefaultFormPanel
{
    private TextField personId;

    public PatientRegistrationFormPanel(final String componentName)
    {
        super(componentName);
    }

    protected InputForm createForm(final String componentName, final IFeedback feedback)
    {
        final PatientRegistrationService service = (PatientRegistrationService) ((DefaultApplication) getApplication()).getService(PatientRegistrationService.class);
        final RegisterPatientParameters params = service.getNewPatientParameters();
        BoundCompoundPropertyModel model = new BoundCompoundPropertyModel(new TestServiceParameterModel());
        InputForm form = new InputForm(componentName, feedback, model, params);
        return form;
    }

    protected class InputForm extends BaseForm
    {
        public InputForm(final String componentName, final IFeedback feedback, BoundCompoundPropertyModel model, RegisterPatientParameters params)
        {
            super(componentName, model, feedback);

            add(new FieldGroupLabel("patientId"));

            TestServiceParameterModel serviceBean = (TestServiceParameterModel)model.getObject(null);

            addLabeledField("personId", serviceBean.getClass());
            addLabeledField("account", serviceBean.getClass());
            addLabeledField("chartNumber", serviceBean.getClass());
            addLabeledField("lastName", serviceBean.getClass());
            addLabeledField("firstName", serviceBean.getClass());
            addLabeledField("middleName", serviceBean.getClass());

            addLabeledSelectField("suffix", PersonNamePrefixType.class);

            addLabeledField("socialSecurityNumber", serviceBean.getClass());
            addLabeledField("dateOfBirth", serviceBean.getClass());

            addLabeledSelectField("gender", GenderType.class);
            addLabeledSelectField("maritalStatus", MaritalStatusType.class);
            addLabeledSelectField("bloodType", BloodType.class);
            addLabeledRadioChoiceField("ethnicity", EthnicityType.class);
            addLabeledField("responsibleParty", serviceBean.getClass());
            addLabeledSelectField("relationshipToResponsible", BaseForm.RELATIONSHIP_TO_RESPONSIBLE_CHOICES);
            addLabeledField("relationshipToResponsibleOtherRelationship", serviceBean.getClass());
            addLabeledField("driversLicenseNumber", serviceBean.getClass());
            addLabeledField("driversLicenseState", serviceBean.getClass());
            addLabeledField("miscNotes", serviceBean.getClass());
            addLabeledField("homePhone", serviceBean.getClass());
            addLabeledField("workPhone", serviceBean.getClass());

            add(new FieldGroupLabel("contactMethods"));

            addLabeledField("homePhone2", serviceBean.getClass());
            addLabeledField("workPhone2", serviceBean.getClass());
            addLabeledField("cellPhone", serviceBean.getClass());
            addLabeledField("pager", serviceBean.getClass());
            addLabeledField("alternate", serviceBean.getClass());
            addLabeledField("homeAddress", serviceBean.getClass());
            addLabeledField("homeAddress2", serviceBean.getClass());
            addLabeledField("city", serviceBean.getClass());
            addLabeledField("state", serviceBean.getClass());
            addLabeledField("zip", serviceBean.getClass());
            addLabeledField("email", serviceBean.getClass());

            add(new FieldGroupLabel("employment"));

            addLabeledField("employerId", serviceBean.getClass());
            addLabeledSelectField("employmentStatus", BaseForm.EMPLOYMENT_STATUS_CHOICES);
            addLabeledField("occupation", serviceBean.getClass());
            addLabeledField("employerPhoneNumber", serviceBean.getClass());

            add(new FieldGroupLabel("insurance"));

            addLabeledSelectField("insuranceSequence", BaseForm.INSURANCE_SEQUENCE_CHOICES);
            addLabeledField("insuranceProduct", serviceBean.getClass());
            addLabeledField("insurancePlan", serviceBean.getClass());

            add(new FieldGroupLabel("generalPlanInformation"));

            addLabeledSelectField("patientRelationshipToInsured", BaseForm.PATIENT_RELATIONSHIP_TO_INSURED_CHOICES);
            addLabeledField("patientRelationshipToInsuredPtherRelationship", serviceBean.getClass());
            addLabeledField("insuredPersonId", serviceBean.getClass());
            addLabeledField("employer", serviceBean.getClass());
            addLabeledField("groupName", serviceBean.getClass());
            addLabeledField("groupNumber", serviceBean.getClass());
            addLabeledField("memberNumber", serviceBean.getClass());

            add(new FieldGroupLabel("coverageInformation"));

            addLabeledField("coverageBeginDate", serviceBean.getClass());
            addLabeledField("coverageEndDate", serviceBean.getClass());
            addLabeledField("individualDeductible", serviceBean.getClass());
            addLabeledField("familyDeductible", serviceBean.getClass());
            addLabeledField("individualDeductibleRemaining", serviceBean.getClass());
            addLabeledField("familyDeductibleRemaining", serviceBean.getClass());
            addLabeledField("percentagePay", serviceBean.getClass());
            addLabeledField("threshold", serviceBean.getClass());
            addLabeledField("officeVisitCoPay", serviceBean.getClass());
        }

		public final void onSubmit()
		{
            info("Saved model " + getModelObject());
		}

    }
}
