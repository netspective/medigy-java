package com.medigy.presentation.model;

import wicket.markup.html.form.IChoiceRenderer;

import java.util.List;

public interface IChoiceList
{
    public List getChoices();
    public IChoiceRenderer getRenderer();
}
