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
import com.medigy.persist.reference.type.BloodType;
import com.medigy.persist.reference.type.PersonNamePrefixType;
import com.medigy.persist.reference.custom.person.EthnicityType;
import com.medigy.wicket.form.*;
import com.medigy.wicket.panel.DefaultFormPanel;
import com.medigy.wicket.DefaultApplication;
import wicket.IFeedback;
import com.medigy.presentation.model.TestServiceParameterModel;
import com.medigy.service.person.PatientRegistrationService;
import com.medigy.service.dto.person.RegisterPatientParameters;
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

            addLabeledField("personId", model, serviceBean.getClass());
            addLabeledField("account", model, serviceBean.getClass());
            addLabeledField("chartNumber", model, serviceBean.getClass());
            addLabeledField("lastName", model, serviceBean.getClass());
            addLabeledField("firstName", model, serviceBean.getClass());
            addLabeledField("middleName", model, serviceBean.getClass());

            addLabeledSelectField("suffix", PersonNamePrefixType.class);

            addLabeledField("socialSecurityNumber", model, serviceBean.getClass());
            addLabeledField("dateOfBirth", model, serviceBean.getClass());

            addLabeledSelectField("gender", GenderType.class);
            addLabeledSelectField("maritalStatus", MaritalStatusType.class);
            addLabeledSelectField("bloodType", BloodType.class);
            addLabeledRadioChoiceField("ethnicity", EthnicityType.class);
            addLabeledField("responsibleParty", model, serviceBean.getClass());
            addLabeledSelectField("relationshipToResponsible", BaseForm.RELATIONSHIP_TO_RESPONSIBLE_CHOICES);
            addLabeledField("relationshipToResponsibleOtherRelationship", model, serviceBean.getClass());
            addLabeledField("driversLicenseNumber", model, serviceBean.getClass());
            addLabeledField("driversLicenseState", model, serviceBean.getClass());
            addLabeledField("miscNotes", model, serviceBean.getClass());
            addLabeledField("homePhone", model, serviceBean.getClass());
            addLabeledField("workPhone", model, serviceBean.getClass());

            add(new FieldGroupLabel("contactMethods"));

            addLabeledField("homePhone2", model, serviceBean.getClass());
            addLabeledField("workPhone2", model, serviceBean.getClass());
            addLabeledField("cellPhone", model, serviceBean.getClass());
            addLabeledField("pager", model, serviceBean.getClass());
            addLabeledField("alternate", model, serviceBean.getClass());
            addLabeledField("homeAddress", model, serviceBean.getClass());
            addLabeledField("homeAddress2", model, serviceBean.getClass());
            addLabeledField("city", model, serviceBean.getClass());
            addLabeledField("state", model, serviceBean.getClass());
            addLabeledField("zip", model, serviceBean.getClass());
            addLabeledField("email", model, serviceBean.getClass());

            add(new FieldGroupLabel("employment"));

            addLabeledField("employerId", model, serviceBean.getClass());
            addLabeledSelectField("employmentStatus", BaseForm.EMPLOYMENT_STATUS_CHOICES);
            addLabeledField("occupation", model, serviceBean.getClass());
            addLabeledField("employerPhoneNumber", model, serviceBean.getClass());

            add(new FieldGroupLabel("insurance"));

            addLabeledSelectField("insuranceSequence", BaseForm.INSURANCE_SEQUENCE_CHOICES);
            addLabeledField("insuranceProduct", model, serviceBean.getClass());
            addLabeledField("insurancePlan", model, serviceBean.getClass());

            add(new FieldGroupLabel("generalPlanInformation"));

            addLabeledSelectField("patientRelationshipToInsured", BaseForm.PATIENT_RELATIONSHIP_TO_INSURED_CHOICES);
            addLabeledField("patientRelationshipToInsuredPtherRelationship", model, serviceBean.getClass());
            addLabeledField("insuredPersonId", model, serviceBean.getClass());
            addLabeledField("employer", model, serviceBean.getClass());
            addLabeledField("groupName", model, serviceBean.getClass());
            addLabeledField("groupNumber", model, serviceBean.getClass());
            addLabeledField("memberNumber", model, serviceBean.getClass());

            add(new FieldGroupLabel("coverageInformation"));

            addLabeledField("coverageBeginDate", model, serviceBean.getClass());
            addLabeledField("coverageEndDate", model, serviceBean.getClass());
            addLabeledField("individualDeductible", model, serviceBean.getClass());
            addLabeledField("familyDeductible", model, serviceBean.getClass());
            addLabeledField("individualDeductibleRemaining", model, serviceBean.getClass());
            addLabeledField("familyDeductibleRemaining", model, serviceBean.getClass());
            addLabeledField("percentagePay", model, serviceBean.getClass());
            addLabeledField("threshold", model, serviceBean.getClass());
            addLabeledField("officeVisitCoPay", model, serviceBean.getClass());
        }



		public final void onSubmit()
		{
            info("Saved model " + getModelObject());
		}

    }
}
