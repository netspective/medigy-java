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
package com.medigy.persist.model.health.form;

import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.reference.custom.health.form.QuestionType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Question extends AbstractTopLevelEntity
{
    public static final String PK_COLUMN_NAME = "question_id";

    private Long questionId;
    private String question;
    private Question parentQuestion;
    private QuestionType type;
    private String answer;

    private Form parentForm;
    private List<MultipleChoiceItem> choices = new ArrayList<MultipleChoiceItem>();
    private List<Question> childQuestions = new ArrayList<Question>();

    @Column(length = 1024)
    public String getAnswer()
    {
        return answer;
    }

    public void setAnswer(final String answer)
    {
        this.answer = answer;
    }

    @ManyToOne
    @JoinColumn(referencedColumnName = Form.PK_COLUMN_NAME, name = "parentForm")
    public Form getParentForm()
    {
        return parentForm;
    }

    public void setParentForm(final Form parentForm)
    {
        this.parentForm = parentForm;
    }

    @ManyToOne
    @JoinColumn(name = QuestionType.PK_COLUMN_NAME, nullable = false)
    public QuestionType getType()
    {
        return type;
    }

    public void setType(final QuestionType type)
    {
        this.type = type;
    }

    @OneToMany(mappedBy = "parentQuestion", cascade = CascadeType.ALL)
    public List<Question> getChildQuestions()
    {
        return childQuestions;
    }

    public void setChildQuestions(final List<Question> childQuestions)
    {
        this.childQuestions = childQuestions;
    }

    @ManyToOne
    @JoinColumn(name = "parent_question_id", referencedColumnName = PK_COLUMN_NAME)
    public Question getParentQuestion()
    {
        return parentQuestion;
    }

    public void setParentQuestion(final Question parentQuestion)
    {
        this.parentQuestion = parentQuestion;
    }

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    public List<MultipleChoiceItem> getChoices()
    {
        return choices;
    }

    public void setChoices(final List<MultipleChoiceItem> choices)
    {
        this.choices = choices;
    }

    @Id(generate = GeneratorType.AUTO)
    public Long getQuestionId()
    {
        return questionId;
    }

    public void setQuestionId(final Long questionId)
    {
        this.questionId = questionId;
    }

    @Column(length = 512, nullable = false)
    public String getQuestion()
    {
        return question;
    }

    public void setQuestion(final String question)
    {
        this.question = question;
    }
}
