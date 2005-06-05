// **************************************************************************
// Browser functions
// **************************************************************************

function Browser()
{
    //Browsercheck (needed)
    this.ver = navigator.appVersion;
    this.agent = navigator.userAgent;
    this.dom = document.getElementById? true : false;
    this.ie4 = (document.all && !this.dom)? true : false;
    this.ie5 = (this.ver.indexOf("MSIE 5")>-1 && this.dom)? true : false;
    this.ie6 = (this.ver.indexOf("MSIE 6")>-1 && this.dom)? true : false;
    this.ie7 = (this.ver.indexOf("MSIE 7")>-1 && this.dom)? true : false;
    this.ie = this.ie4 || this.ie5 || this.ie6 || this.ie7;
    this.mac = this.agent.indexOf("Mac") > -1;
    this.opera5 = this.agent.indexOf("Opera 5") > -1;
    this.ns6 = (this.dom && parseInt(this.ver) >= 5) ? true : false;
    this.ns4 = (document.layers && !this.dom)? true : false;
    this.browser = (this.ie7 || this.ie6 || this.ie5 || this.ie4 || this.ns4 || this.ns6 || this.opera5 || this.dom);

    return this;
}

var browser = new Browser();
if(! browser.ie6 && ! browser.ie7 && ! browser.ns6)
{
    alert("You are running an unsupported browser. Please upgrade to IE 6 or higher or FireFox 1.0 or higher");
}

// **************************************************************************
// Cookie functions
// **************************************************************************

// name - name of the cookie
// value - value of the cookie
// [expires] - expiration date of the cookie (defaults to end of current session)
// [path] - path for which the cookie is valid (defaults to path of calling document)
// [domain] - domain for which the cookie is valid (defaults to domain of calling document)
// [secure] - Boolean value indicating if the cookie transmission requires a secure transmission
// * an argument defaults when it is assigned null as a placeholder
// * a null placeholder is not required for trailing omitted arguments

function setCookie(name, value, expires, path, domain, secure)
{
    var curCookie = name + "=" + escape(value) +
      ((expires) ? "; expires=" + expires.toGMTString() : "") +
      ((path) ? "; path=" + path : "") +
      ((domain) ? "; domain=" + domain : "") +
      ((secure) ? "; secure" : "");
    document.cookie = curCookie;
}

// name - name of the desired cookie
// * return string containing value of specified cookie or null if cookie does not exist
function getCookie(name)
{
    var dc = document.cookie;
    var prefix = name + "=";
    var begin = dc.indexOf("; " + prefix);
    if (begin == -1)
    {
        begin = dc.indexOf(prefix);
        if (begin != 0) return null;
    }
    else
        begin += 2;

    var end = document.cookie.indexOf(";", begin);
    if (end == -1)
        end = dc.length;

    return unescape(dc.substring(begin + prefix.length, end));
}

// name - name of the cookie
// [path] - path of the cookie (must be same as path used to create cookie)
// [domain] - domain of the cookie (must be same as domain used to create cookie)
// * path and domain default if assigned null or omitted if no explicit argument proceeds
function deleteCookie(name, path, domain)
{
    if (getCookie(name))
    {
        document.cookie = name + "=" +
        ((path) ? "; path=" + path : "") +
        ((domain) ? "; domain=" + domain : "") +
        "; expires=Thu, 01-Jan-70 00:00:01 GMT";
    }
}

// **************************************************************************
// Getting URL parameters through Javascript made easier
// **************************************************************************

location.getParameter = function(sParam)
{
    var sKey = sParam + "=";

    var oParams = this.search.substring(1).split("&");
    for(var i = 0; i < oParams.length; i++)
        if(oParams[i].indexOf(sKey) == 0)
            return oParams[i].substring(sKey.length);
    return null;
};

location.getParameterMap = function()
{
	var oParams = this.search.substring(1).split("&");

	var oMap = { }
	for(var i = 0; i < oParams.length; i++) {
		var pair = oParams[i].split("=");
		oMap[pair[0]] = pair[1];
	}

	return oMap;
};

location.getParametersCount = function()
{
	return this.search.substring(1).split("&").length;
};

function constructUrlWithAdditionalParams(url, addParamsArray, retainParams)
{
    if(addParamsArray == null || addParamsArray.length == 0)
        return constructUrlWithParamsRetained(url, retainParams);

    var addParamsText = ""; // array is name,value,name,value,etc
    for(var i = 0; i < addParamsArray.length; i += 2)
    {
        var name = addParamsArray[0+i];
        var value = addParamsArray[1+i];
        addParamsText += (i > 0 ? "&" : "") + name + "=" + value;
    }

    return constructUrlWithParamsRetained((url.indexOf('?') > -1 ? (url + "&") : (url + "?")) + addParamsText, retainParams);
}

function constructUrlWithParamsRetained(url, retainParams)
{
    if(retainParams == null || retainParams == "")
        return url;

    if(location.getParametersCount() > 0)
    {
        var curPageParams = location.getParameterMap();
        var newUrl = url.indexOf('?') > -1 ? (url + "&") : (url + "?");
        if(retainParams == "*")
        {
            for(var paramName in curPageParams)
                newUrl += paramName + "=" + curPageParams[paramName] + "&";
        }
        else
        {
            var keepParams = typeof(retainParams) == 'array' ? retainParams : retainParams.split(",");
            for(var i = 0; i < keepParams.length; i++)
            {
                var paramName = keepParams[i];
                if(curParams[paramName] != null)
                    newUrl += paramName + "=" + curPageParams[paramName] + "&";
            }
        }
        return newUrl;
    }
    else
        return url;
}

// **************************************************************************
// Forms management
// **************************************************************************

var anyControlChangedEventCalled = false;
var submittedDialogValid = false;
var cancelBubbleOnError = true;

//****************************************************************************
// FieldType class
//****************************************************************************

function FieldType(name, onFinalizeDefn, onValidate, onChange, onFocus, onBlur, onKeyPress, onClick)
{
	this.type = name;
	this.finalizeDefn = onFinalizeDefn;
	this.isValid = onValidate;
	this.getFocus = onFocus;
	this.valueChanged = onChange;
	this.keyPress = onKeyPress;
	this.loseFocus = onBlur;
	this.click = onClick;
}

var FIELD_TYPES = new Array();

function addFieldType(name, onFinalizeDefn, onValidate, onChange, onFocus, onBlur, onKeyPress, onClick)
{
	FIELD_TYPES[name] = new FieldType(name, onFinalizeDefn, onValidate, onChange, onFocus, onBlur, onKeyPress, onClick);
}

//****************************************************************************
// Dialog class
//****************************************************************************

function Dialog(form, allowClientValidation, clearTextOnValidateError, showDataChangedMessageOnLeave)
{
	this.form = form;
	this.fields = new Array();                 // straight list (simple array)
	this.fieldsByControlName = new Array();    // hash -- value is field
	this.allowClientValidation = allowClientValidation;
	this.clearTextOnValidateError = clearTextOnValidateError;
	this.showDataChangedMessageOnLeave = showDataChangedMessageOnLeave;

	// the remaining are object-based methods
	this.createField = Dialog_createField;
	this.finalizeContents = Dialog_finalizeContents;
	this.isValid = Dialog_isValid;
	this.allowValidation = Dialog_allowValidation;
	this.isShowDataChangedMessageOnLeave = Dialog_isShowDataChangedMessageOnLeave;

    form.dialog = this; // associate the extra dialog information with the form that it's attached to

	return this;
}

function Dialog_createField(control, type, flags)
{
    var field = new DialogField(control, type, flags);

	field.fieldIndex = this.fields.length;
	field.dialog = this;

	this.fields[field.fieldIndex] = field;
	this.fieldsByControlName[field.control.name] = field;

	return field;
}

function Dialog_finalizeContents()
{
	var dialogFields = this.fields;
	for(var i = 0; i < dialogFields.length; i++)
		dialogFields[i].finalizeContents(this);
}

function Dialog_allowValidation()
{
	return this.allowClientValidation;
}

function Dialog_isShowDataChangedMessageOnLeave()
{
    return this.showDataChangedMessageOnLeave;
}

function Dialog_isValid()
{
	var dialogFields = this.fields;
	for(var i = 0; i < dialogFields.length; i++)
	{
		var field = dialogFields[i];
		if(field.requiresPreSubmit)
			field.doPreSubmit();
	}

	if(! this.allowValidation())
		return true;

	var isValid = true;
	for(var i = 0; i < dialogFields.length; i++)
	{
		var field = dialogFields[i];
		if(! field.isValid())
		{
			isValid = false;
			break;
		}
	}

	if (isValid)
	{
	    submittedDialogValid = true;
        for(var i = 0; i < dialogFields.length; i++)
        {
            var field = dialogFields[i];
            if(field.encryption != null)
                this.control.value = field.encryption.getEncryptedValue(this.control.value);
        }
	}

	return isValid;
}

//****************************************************************************
// DialogField class
//****************************************************************************

// These constants MUST be kept identical to what is in com.medigy.presentation.form.FieldFlags

var FLDFLAG_REQUIRED                           = 1;
var FLDFLAG_PRIMARYKEY                         = FLDFLAG_REQUIRED * 2;
var FLDFLAG_PRIMARYKEY_GENERATED               = FLDFLAG_PRIMARYKEY * 2;
var FLDFLAG_UNAVAILABLE                        = FLDFLAG_PRIMARYKEY_GENERATED * 2;
var FLDFLAG_READONLY                           = FLDFLAG_UNAVAILABLE * 2;
var FLDFLAG_INITIAL_FOCUS                      = FLDFLAG_READONLY * 2;
var FLDFLAG_PERSIST                            = FLDFLAG_INITIAL_FOCUS * 2;
var FLDFLAG_CREATE_ADJACENT_AREA               = FLDFLAG_PERSIST * 2;
var FLDFLAG_INPUT_HIDDEN                       = FLDFLAG_CREATE_ADJACENT_AREA * 2;
var FLDFLAG_BROWSER_READONLY                   = FLDFLAG_INPUT_HIDDEN * 2;
var FLDFLAG_DOUBLEENTRY                        = FLDFLAG_BROWSER_READONLY * 2;
var FLDFLAG_SCANNABLE                          = FLDFLAG_DOUBLEENTRY * 2;
var FLDFLAG_AUTOBLUR                           = FLDFLAG_SCANNABLE * 2;
var FLDFLAG_SUBMIT_ONBLUR                      = FLDFLAG_AUTOBLUR * 2;
var FLDFLAG_CREATE_ADJACENT_AREA_HIDDEN        = FLDFLAG_SUBMIT_ONBLUR * 2;
var FLDFLAG_CLEAR_TEXT_ON_VALIDATE_ERROR       = FLDFLAG_CREATE_ADJACENT_AREA_HIDDEN * 2;
var FLDFLAG_ALWAYS_SHOW_HINT                   = FLDFLAG_CLEAR_TEXT_ON_VALIDATE_ERROR * 2;

// These constants MUST be kept identical to what is in com.netspective.sparx.form.field.SelectField
var SELECTSTYLE_RADIO      = 0;
var SELECTSTYLE_COMBO      = 1;
var SELECTSTYLE_LIST       = 2;
var SELECTSTYLE_MULTICHECK = 3;
var SELECTSTYLE_MULTILIST  = 4;
var SELECTSTYLE_MULTIDUAL  = 5;
var SELECTSTYLE_POPUP      = 6;

var DATE_DTTYPE_DATEONLY = 0;
var DATE_DTTYPE_TIMEONLY = 1;
var DATE_DTTYPE_BOTH     = 2;
var DATE_DTTYPE_MONTH_YEAR_ONLY = 3;

function DialogField(control, type, flags, hintElement, adjacentAreaElement)
{
    this.dialog = null;    // this will be set when the field is added using Dialog_registerField
	this.fieldIndex = -1;  // this will be set when the field is added using Dialog_registerField

	this.control = control;
    this.control.field = this; // if we have access to the control, we will have access to the field
	this.type = type;
	this.flags = flags;

	this.customHandlers = new FieldType("Custom", null, null, null, null, null, null, null);
	this.dependentConditions = new Array();
	this.style = null;
	this.requiresPreSubmit = false;
	this.currentlyVisible = true;
	this.encryption = null;

	this.getLabelElement = DialogField_getLabelElement;
	this.labelElement = this.getLabelElement();
	this.hintElement = hintElement;
	this.adjacentAreaElement = adjacentAreaElement;

	this.originalControlClassName = this.control.className;
	this.originalLabelClassName = this.labelElement == null ? null : this.labelElement.className;

	this.getControlIndexInForm = DialogField_getControlIndexInForm;
	this.controlIndexInForm = this.getControlIndexInForm();

	this.evaluateConditionals = DialogField_evaluateConditionals;
	this.finalizeContents = DialogField_finalizeContents;
	this.isValid = DialogField_isValid;
	this.doPreSubmit = DialogField_doPreSubmit;
	this.alertRequired = DialogField_alertRequired;
	this.isRequired = DialogField_isRequired;
	this.isReadOnly = DialogField_isReadOnly;
	this.alertMessage = DialogField_alertMessage;
	this.isVisible = DialogField_isVisible;
	this.setVisible = DialogField_setVisible;
	this.setRequired = DialogField_setRequired;
	this.setValue = DialogField_setValue;
	this.setReadOnly = DialogField_setReadOnly;
	this.isClearTextOnValidateError = DialogField_isClearTextOnValidateError;
	this.submitOnblur = DialogField_submitOnblur;
	this.focusNext = DialogField_focusNext;
	this.getFieldContainerElement = DialogField_getFieldContainerElement;
	this.isHideHintUntilFocus = DialogField_isHideHintUntilFocus;
	this.getLabel = DialogField_getLabel;
	this.appendToClassName = DialogField_appendToClassName;
	this.resetClassName = DialogField_resetClassName;
	this.createAdjancentArea = DialogField_createAdjancentArea;
	this.populateAdjacentArea = DialogField_populateAdjacentArea;
}

function DialogField_createAdjancentArea()
{
    // it's possible that we were provided with an adjacent area element so don't do anything if it's already available
	if(this.adjacentAreaElement != null)
	    return;

    this.adjacentAreaElement = document.createElement("span");
    this.adjacentAreaElement.className = "control-adjacent";
    if((this.flags & FLDFLAG_CREATE_ADJACENT_AREA_HIDDEN) != 0) this.adjacentAreaElement.visibility = 'hide';

    // seems the DOM doesn't have an "insertAfter" method so we simulate one (adjacent area is right next to the control)
    var parent = this.control.parentNode;
    var refChild = this.control.nextSibling;
    if(refChild != null)
        parent.insertBefore(this.adjacentAreaElement, refChild);
    else
        parent.appendChild(this.adjacentAreaElement);
}

function DialogField_populateAdjacentArea(content)
{
    if(this.adjacentAreaElement == null)
        this.createAdjancentArea();

    // remove any content that might already be in the node
    while(this.adjacentAreaElement.childNodes.length > 0)
        this.adjacentAreaElement.removeChild(this.adjacentAreaElement.childNodes[0]);

    // we probably just want to clear the adjacent area
    if(content == null)
        return;

    if(typeof(content) == 'array')
    {
        for(var i = 0; i < content.lenght; i++)
            this.adjacentAreaElement.appendChild(content[i]);
    }
    else if(typeof(content) == 'string')
        this.adjacentAreaElement.appendChild(document.createTextNode(content));
    else
        // probably just a plain DOM node
        this.adjacentAreaElement.appendChild(content);
}

function DialogField_setValue(value)
{
    this.control.value = value;
}

function DialogField_getLabel()
{
    return this.labelElement == null ? (this.control.name + " does not have a label.") : this.labelElement.childNodes[0].nodeValue;
}

function DialogField_getLabelElement()
{
    var objLabels = document.getElementsByTagName("LABEL");
    for (var i = 0; i < objLabels.length; i++)
    {
        if (objLabels[i].htmlFor == this.control.id)
            return objLabels[i];
    }

    return null;
}

function DialogField_isRequired()
{
	return (this.flags & FLDFLAG_REQUIRED) != 0 && this.isVisible();
}

function getElementStyle(elemID, IEStyleProp, CSSStyleProp)
{
    var elem = document.getElementById(elemID);
    if (elem.currentStyle)
    {
        return elem.currentStyle[IEStyleProp];
    }
    else if (window.getComputedStyle)
    {
        var compStyle = window.getComputedStyle(elem, "");
        return compStyle.getPropertyValue(CSSStyleProp);
    }
    return "";
}

function DialogField_appendToClassName(text, permanent)
{
    if(this.labelElement != null)
        this.labelElement.className = (this.originalLabelClassName == null || this.originalLabelClassName == '') ? text : (this.originalLabelClassName + "-" + text);
    this.control.className = (this.originalControlClassName == null || this.originalControlClassName == '') ? text : (this.originalControlClassName + "-" + text);
    //alert(this.control + " '" + this.control.className + "'");
}

function DialogField_resetClassName(blanks)
{
    if(blanks)
    {
        this.originalLabelClassName = "";
        this.originalControlClassName = "";
    }

    if(this.labelElement != null)
       this.labelElement.className = (this.originalLabelClassName == null || this.originalLabelClassName == '') ? "" : this.originalLabelClassName;
    this.control.className = (this.originalControlClassName == null || this.originalControlClassName == '') ? "" : this.originalControlClassName;
}

/**
 * Changes the label and input appearance based on the field's REQUIRED status
 */
function DialogField_setRequired(required)
{
    if (required)
    {
        this.flags = this.flags | FLDFLAG_REQUIRED;
        this.originalLabelClassName = "required";
        this.originalControlClassName = "required";
        this.resetClassName();
    }
    else
    {
        this.flags = this.flags & ~FLDFLAG_REQUIRED;
        this.resetClassName(true);
    }
}

function DialogField_setReadOnly(readOnly)
{
    if (readOnly)
    {
        this.flags = this.flags | FLDFLAG_BROWSER_READONLY;
        this.control.readOnly = true;
        this.originalLabelClassName = "read-only";
        this.originalControlClassName = "read-only";
        this.resetClassName();
    }
    else
    {
        this.flags = this.flags & ~FLDFLAG_BROWSER_READONLY;
        this.control.readOnly = false;
        this.resetClassName(true);
    }
}


function DialogField_isReadOnly()
{
	return ((this.flags & FLDFLAG_READONLY) != 0) || ((this.flags & FLDFLAG_BROWSER_READONLY) != 0);
}

function DialogField_isClearTextOnValidateError()
{
	return ((this.flags & FLDFLAG_CLEAR_TEXT_ON_VALIDATE_ERROR) != 0);
}

function DialogField_isHideHintUntilFocus()
{
	return ((this.flags & FLDFLAG_ALWAYS_SHOW_HINT) == 0);
}

function DialogField_finalizeContents()
{
	if(this.type != null)
	{
		if(this.type.finalizeDefn != null)
			this.type.finalizeDefn(dialog, this);
	}

	if(this.style != null && this.style == SELECTSTYLE_MULTIDUAL)
		this.requiresPreSubmit = true;

	if(this.dependentConditions.length > 0)
		this.evaluateConditionals(dialog);

	if((this.flags & FLDFLAG_INITIAL_FOCUS) != 0)
	{
        if(browser.ie5 || browser.ie6)
        {
            if (this.control.isContentEditable && this.isVisible())
                this.control.focus();
        }
        else
        {
            if (this.isVisible())
                this.control.focus();
        }
	}

    // setup the initial appearance for special fields
    this.setRequired((this.flags & FLDFLAG_REQUIRED) != 0);
}

function DialogField_evaluateConditionals(dialog)
{
	if(this.isReadOnly())
		return;

	var conditionalFields = this.dependentConditions;
	for(var i = 0; i < conditionalFields.length; i++)
		conditionalFields[i].evaluate(dialog, this.control);
}

function DialogField_alertMessage(message)
{
    alert(this.getLabel() + ":   " + message);

	if (this.isClearTextOnValidateError())
	    this.control.value = "";

	this.control.focus();
	handleCancelBubble(this.control);
}

function DialogField_alertRequired()
{
    alert(this.getLabel() + " is required.");
	if(this.control != null) this.control.focus();
}

function DialogField_isValid()
{
	if(this.isReadOnly())
		return true;

	// now see if there are any type-specific validations to perform
	var fieldType = this.type;
	if(fieldType != null && fieldType.isValid != null)
	{
		if (this.customHandlers.isValid != null)
		{
			var valid = true;
			if (this.customHandlers.isValidType == 'extends')
				valid = fieldType.isValid(this, this.control);
			if (valid)
			{
				valid = this.customHandlers.isValid(this, this.control);
			}
			return valid;
		}
		else
		{
			return fieldType.isValid(this, this.control);
		}
	}

	// no type-specific validation found so try and do a generic one
	if(this.isRequired())
	{
		if(eval("typeof this.control.value") != "undefined")
		{
			if(this.control.value.length == 0)
			{
				this.alertRequired(this.control);
				return false;
			}
		}
	}

	return true;
}

function DialogField_doPreSubmit()
{
	if(this.style != null && this.style == SELECTSTYLE_MULTIDUAL)
	{
		// Select all items in multidual elements. If items aren't selected,
		// they won't be posted.

		// It's possible this control may not be rendered as a SELECT element
		// even though the field type was set to SELECT.
		// Depending on the browser, the evaluation of control.options.length could
		// generate a run-time error.
		if ( typeof(this.control) == "undefined" || this.control.options == null )
		    return;
		for (var i = 0; i < this.control.options.length; i++)
		{
			this.control.options[i].selected = true;
		}
	}
}

function DialogField_getFieldContainerElement()
{
    alert("DialogField.getFieldContainerElement() is not implemented yet");
    return null;
}

function DialogField_isVisible()
{
    return this.currentlyVisible == true;
}

function DialogField_setVisible(dialog, visible)
{
    this.currentlyVisible = visible;
    this.evaluateConditionals(dialog);

    // now find the children and hide them too
	var dialogFields = dialog.fields;
	var regExp = new RegExp("^" + field.qualifiedName + '\\.');

	for(var i=0; i<dialogFields.length; i++)
	{
		if(regExp.test(dialogFields[i].qualifiedName))
			dialogFields[i].setVisible(visible);
	}
}

function DialogField_submitOnblur()
{
    if(! this.isSubmitOnBlur)
        return;

	if (this.control.value != '')
	{
	    alert("DialogField.submitOnBlur() was called but the function is not implemented yet.");

	    //TODO: should validation be disabled?
        //TODO: should this be set? this.showDataChangedMessageOnLeave = false;

        // var vFieldName = "_d."+ dialog.name + ".";
        // document.forms[control.form.name].elements[vFieldName].value = field.name;
        // control.form.submit();
    }
}

function DialogField_getControlIndexInForm()
{
	var index = -1, i = 0, found = false;
	var form = this.control.form;
	if(form == null)
	{
	    //alert(this.control.id + " has no form associated with it?");
	    return -1;
	}
	while ( i < form.length && index == -1 )
    	if (form[i] == this.control)
	    	index = i;
	    else
		    i++;
	return index;
}

function DialogField_focusNext()
{
	var form = this.control.form;
    form[( this.controlIndexInForm + 1 ) % form.length].focus();
}

function setAllCheckboxes(sourceCheckbox, otherCheckboxesPrefix)
{
	var isChecked = sourceCheckbox.checked;

	for(var f = 0; f < document.forms.length; f++)
	{
		var form = document.forms[f];
		var elements = form.elements;
		for(var i = 0; i < elements.length; i++)
		{
			var control = form.elements[i];
			if(control.name.indexOf(otherCheckboxesPrefix) == 0)
				control.checked = isChecked;
		}
	}
}

function DialogFieldConditionalFlag(source, partner, expression, flag, applyFlag)
{
	this.source = source;
	this.partner = partner;
	this.expression = expression;
    this.flag = flag;
    this.applyFlag = applyFlag; // if applyFlag is true, then the flag is set. If applyFlag is false, then the flag is cleared.
	// the remaining are object-based methods
	this.evaluate = DialogFieldConditionalFlag_evaluate;
}

function DialogFieldConditionalFlag_evaluate(dialog, control)
{
    if(control == null)
    {
        alert("control is null in DialogFieldConditionalFlag.evaluate(dialog, control)");
        return;
    }
    var condSource = dialog.fieldsByControlName[this.source];
    if(eval(this.expression) == true)
	{
	    // set the flag on the dialog
	    if ((this.flag & FLDFLAG_REQUIRED) != 0)
	    {
	        if (this.applyFlag == true)
    		    condSource.setRequired(true);
    		else
    		    condSource.setRequired(false);
        }
        if ((this.flag & FLDFLAG_BROWSER_READONLY) != 0)
        {
            if (this.applyFlag == true)
                condSource.setReadOnly(true);
            else
                condSource.setReadOnly(false);
        }

	}
	else
	{
	    // the evaluation condition was false
	    if ((this.flag & FLDFLAG_REQUIRED) != 0)
	    {
    		condSource.setRequired(false);
    		if (this.applyFlag == true)
    		    condSource.setRequired(false);
    		else
    		    condSource.setRequired(true);
        }
        if ((this.flag & FLDFLAG_BROWSER_READONLY) != 0)
        {
            if (this.applyFlag == true)
                condSource.setReadOnly(false);
            else
                condSource.setReadOnly(true);
        }
	}
}

function DialogFieldConditionalClear(source, partner, expression)
{
	this.source = source;
	this.partner = partner;
	this.expression = expression;

	// the remaining are object-based methods
	this.evaluate = DialogFieldConditionalClear_evaluate;
}

function DialogFieldConditionalClear_evaluate(dialog, control)
{
	if(control == null)
	{
		alert("control is null in DialogFieldConditionalClear.evaluate(control)");
		return;
	}

	var condSource = dialog.fieldsByControlName[this.source];
	if(eval(this.expression) == true)
	{
        condSource.setValue('');
	}
}

//****************************************************************************
// DialogFieldConditionalDisplay class
//****************************************************************************

function DialogFieldConditionalDisplay(source, partner, expression)
{
	this.source = source;
	this.partner = partner;
	this.expression = expression;

	// the remaining are object-based methods
	this.evaluate = DialogFieldConditionalDisplay_evaluate;
}

function DialogFieldConditionalDisplay_evaluate(dialog, control)
{
	// first find the field area that we need to hide/show
	// -- if an ID with the entire field row is found (a primary field)
	//    then go ahead and use that
	// -- if no primary field row is found, find the actual control and
	//    use that to hide/show

	if(control == null)
	{
		alert("control is null in DialogFieldConditionalDisplay.evaluate(control)");
		return;
	}

	var condSource = dialog.fieldsByControlName[this.source];
	var fieldAreaElem = condSource.getFieldContainerElement(dialog);
	if(fieldAreaElem == null || (typeof fieldAreaElem == "undefined"))
	{
		fieldAreaElem = condSource.getControl();
		if(fieldAreaElem == null || (typeof fieldAreaElem == "undefined"))
		{
			alert ('Neither source element "' + fieldAreaId + '" or "'+ condSource.control.id +'" found in conditional partner.');
			return;
		}
	}

	// now that we have the fieldArea that we want to show/hide go ahead
	// and evaluate the js expression to see if the field should be shown
	// or hidden. remember, the expression is evaluted in the current context
	// which means the word "control" refers to the control that is the
	// the conditional "partner" (not the source)
	if(eval(this.expression) == true)
	{
		condSource.setVisible(dialog, true);
		//fieldAreaElem.className = 'section_field_area_conditional_expanded';
		if (fieldAreaElem.style)
			fieldAreaElem.style.display = '';
		else
			fieldAreaElem.visibility = 'show';
	}
	else
	{
		condSource.setVisible(dialog, false);
		//fieldAreaElem.className = 'section_field_area_conditional';
		if (fieldAreaElem.style)
			fieldAreaElem.style.display = 'none';
		else
			fieldAreaElem.visibility = 'hide';
	}
}


function evaluateQuestions(control, field)
{
	var listString = control.value;
	// Split string at the comma
	var questionList = listString.split(",");
	// Begin loop through the querystring
	for(var i = 0; i < questionList.length; i++)
	{
		if (questionList[i] == field.source)
		return true;
	}
	return false;
}

//****************************************************************************
// Event handlers
//****************************************************************************

function handleCancelBubble(control)
{
	if (cancelBubbleOnError)
	{
		var field = control.field;
		if (field != null && field.doubleEntry == "yes")
		{
			field.firstEntryValue = "";
		}
	}
}

function controlOnClick(control, event)
{
	field = control.field;
	if(typeof field == "undefined" || field == null || field.type == null) return;

	if (field.customHandlers.click != null)
	{
		var retval = true;
		if (field.customHandlers.clickType == 'extends')
		{
			if (field.type.click != null)
				retval = field.type.click(field, control);
		}
		if (retval)
			field.customHandlers.click(field, control);
		return retval;
	}
	else
	{
		if (field.type.click != null)
			return field.type.click(field, control);
		else
			return true;
	}
}

function controlOnKeypress(control, event)
{
	field = control.field;
	if(typeof field == "undefined" || field == null || field.type == null) return;

	if (field.customHandlers.keyPress != null)
	{
		var retval = true;
		if (field.customHandlers.keyPressType == 'extends')
		{
			if (field.type.keyPress != null)
				retval =  field.type.keyPress(field, control);
		}

		if (retval)
			retval =  field.customHandlers.keyPress(field, control);
		return retval;
	}
	else
	{
		if (field.type.keyPress != null)
			return field.type.keyPress(field, control, event);
		else
			return true;
	}
}

function controlOnFocus(control, event)
{
	field = control.field;
	if(typeof field == "undefined" || field == null || field.type == null) return;

    field.appendToClassName("focused");

    if (field.isHideHintUntilFocus())
    {
        if (field.hintElement != null)
        {
            if (field.hintElement.style)
                field.hintElement.style.display = 'block';
            else
                field.hintElement.visibility = 'show';
        }
    }

	if (field.customHandlers.getFocus != null)
	{
		var retval = true;
		if (field.customHandlers.getFocusType == 'extends')
		{
			if (field.type.getFocus != null)
				retval =  field.type.getFocus(field, control);
		}
		if (retval)
			retval =  field.customHandlers.getFocus(field, control);
		return retval;
	}
	else
	{
		if (field.type.getFocus != null)
			return field.type.getFocus(field, control);
		else
			return true;
	}

}

function controlOnChange(control, event)
{
	anyControlChangedEventCalled = true;
	field = control.field;
	if(typeof field == "undefined" || field == null) return;

	if (field.scannable == 'yes')
	{
		field.isScanned = false;
		var validScan = scanField_changeDisplayValue(field, control);
		if(! validScan)
		{
			event.cancelBubble = true;
			event.returnValue = false;
			return false;
		}
	}

	if(field.dependentConditions.length > 0)
	{
		var conditionalFields = field.dependentConditions;
		for(var i = 0; i < conditionalFields.length; i++)
			conditionalFields[i].evaluate(control.field.getDialog(), control);
	}

	if(field.type == null) return;
	if (field.customHandlers.valueChanged != null)
	{
		var retval = true;
		if (field.customHandlers.valueChangedType == 'extends')
		{
			if (field.type.valueChanged != null)
				retval = field.type.valueChanged(field, control);
		}
		if (retval)
			retval =  field.customHandlers.valueChanged(field, control);
		return retval;
	}
	else
	{
		if (field.type.valueChanged != null)
			return field.type.valueChanged(field, control);
		else
			return true;
	}
}

function controlOnBlur(control, event)
{
	field = control.field;
	if(typeof field == "undefined" || field == null || field.type == null) return;

    field.resetClassName();

    if (field.isHideHintUntilFocus())
    {
        if (field.hintElement != null)
        {
            if (field.hintElement.style)
                field.hintElement.style.display = 'none';
            else
                field.hintElement.visibility = 'hide';
        }
    }

	var retval = true;
	if (field.customHandlers.loseFocus != null)
	{
		if (field.customHandlers.loseFocusType == 'extends')
		{
			if (field.type.loseFocus != null)
			retval = field.type.loseFocus(field, control);
		}
		if (retval)
			retval =  field.customHandlers.loseFocus(field, control);
	}
	else
	{
		if (field.type.loseFocus != null)
			retval = field.type.loseFocus(field, control);
	}

	if(field != null)
		field.submitOnblur();

	return retval;
}

//****************************************************************************
// Keyboard-management utility functions
//****************************************************************************

var KEYCODE_ENTER          = 13;
var KEYCODE_TAB            = 9;
var KEYCODE_BS             = 8;

var NUM_KEYS_RANGE         = [48,  57];
var PERIOD_KEY_RANGE       = [46,  46];
var SLASH_KEY_RANGE        = [47,  47];
var DASH_KEY_RANGE         = [45,  45];
var UPPER_ALPHA_KEYS_RANGE = [65,  90];
var LOW_ALPHA_KEYS_RANGE   = [97, 122];
var UNDERSCORE_KEY_RANGE   = [95,  95];
var COLON_KEY_RANGE        = [58, 58];
var ENTER_KEY_RANGE        = [13, 13];

//****************************************************************************
// Field-specific validation and keypress filtering functions
//****************************************************************************

function CurrencyField_onKeyPress(field, control, event)
{
	return keypressAcceptRanges(field, control, [NUM_KEYS_RANGE, DASH_KEY_RANGE, PERIOD_KEY_RANGE], event);
}

function CurrencyField_isValid(field, control)
{
	if(field.isRequired() && control.value.length == 0)
	{
		field.alertRequired(control);
		return false;
	}
	if (control.value.length > 0)
	{
		var test = testCurrency(field, control);
		if (test == false)
		{
			field.alertMessage(control, field.text_format_err_msg);
			return false;
		}
	}
	return true;
}

function CurrencyField_valueChanged(field, control)
{
	return formatCurrency(field, control);
}

function BooleanField_onClick(field, control)
{
	if (control.type == 'checkbox' || control.type == 'radio')
	{
		if(field.dependentConditions.length > 0)
		{
			var conditionalFields = field.dependentConditions;
			for(var i = 0; i < conditionalFields.length; i++)
			conditionalFields[i].evaluate(field.getDialog(), control);
		}
	}
	return true;
}

function TextField_onFocus(field, control)
{
	if (field.readonly == 'yes')
		control.blur();

	return true;
}

function TextField_valueChanged(field, control)
{
	if (field.uppercase == 'yes')
	{
		control.value = control.value.toUpperCase();
	}

	if(control.value == "")
		return true;

	return TextField_isValid(field, control);
}

function TextField_onKeyPress(field, control, event)
{
	if (field.identifier == 'yes')
	{
		return keypressAcceptRanges(field, control, [NUM_KEYS_RANGE, UPPER_ALPHA_KEYS_RANGE, UNDERSCORE_KEY_RANGE], event);
	}
	return true;
}

function TextField_isValid(field, control)
{
	if(field.isRequired() && control.value.length == 0)
	{
		field.alertRequired(control);
		return false;
	}

    /* 12.14.04 Bug# 490/768 JH - When there is no data yet in the system a value
        in the array of objects can be null, so the code below can complain generating
        a run-time error.
    */
    if (control.value != null && control.value.length > 0)
	{
		if (field.validValues)
		{
			var valid = false;
			for (k in field.validValues)
			{
				if (field.validValues[k] == control.value)
					valid = true;
			}
			if (valid == false)
			{
				field.alertMessage(control, "value '" + control.value + "' is not valid. ");
				if (this.isClearTextOnValidateError())
				{
				    control.value = "";
				}
				return false;
			}
		}

		if (field.text_format_pattern != null && (typeof field.text_format_pattern != "undefined")
			&& field.text_format_pattern != "")
		{
			var test = testText(field, control);
			if (test == false)
			{
				field.alertMessage(control, field.text_format_err_msg);
				if (this.isClearTextOnValidateError())
				{
				    control.value = "";
				}
				return false;
			}
		}
	}

	return true;
}

function PhoneField_valueChanged(field, control)
{
	return formatPhone(field, control);
}

function PhoneField_isValid(field, control)
{
	if(field.isRequired() && control.value.length == 0)
	{
		field.alertRequired(control);
		return false;
	}
	if (control.value.length > 0)
	{
		var test = testPhone(field, control);
		if (test == false)
		{
			field.alertMessage(control, field.text_format_err_msg);
			return false;
		}
	}
	return true;
}

function SocialSecurityField_valueChanged(field, control)
{
	return formatSSN(field, control);
}

function SocialSecurityField_isValid(field, control)
{
	if(field.isRequired() && control.value.length == 0)
	{
		field.alertRequired(control);
		return false;
	}
	if (control.value.length > 0)
	{
		var test = testSSN(field, control);
		if (test == false)
		{
			field.alertMessage(control, "Social Security Number must be in the correct format: 999-99-9999");
			return false;
		}
	}
	return true;
}

function IntegerField_onKeyPress(field, control, event)
{
	return keypressAcceptRanges(field, control, [NUM_KEYS_RANGE, DASH_KEY_RANGE], event);
}

function IntegerField_isValid(field, control)
{
	if(field.isRequired() && control.value.length == 0)
	{
		field.alertRequired(control);
		return false;
	}

	var intValue = control.value - 0;
	if(isNaN(intValue))
	{
		field.alertMessage(control, "'"+ control.value +"' is an invalid integer.");
		return false;
	}

	if(field.minValue != "" && control.value < field.minValue)
	{
		field.alertMessage(control, "Minimum value is " + field.minValue);
		return false;
	}
	if(field.maxValue != "" && control.value > field.maxValue)
	{
		field.alertMessage(control, "Maximum value is " + field.maxValue);
		return false;
	}

	return true;
}

function FloatField_onKeyPress(field, control, event)
{
	return keypressAcceptRanges(field, control, [NUM_KEYS_RANGE, DASH_KEY_RANGE, PERIOD_KEY_RANGE], event);
}

function FloatField_isValid(field, control)
{
	if(field.isRequired() && control.value.length == 0)
	{
		field.alertRequired(control);
		return false;
	}

	var floatValue = control.value - 0;
	if(isNaN(floatValue))
	{
		field.alertMessage(control, "'"+ control.value +"' is an invalid decimal.");
		return false;
	}
	return true;
}

function MemoField_isValid(field, control)
{
	if(field.isRequired() && control.value.length == 0)
	{
		field.alertRequired(control);
		return false;
	}

	maxlimit = field.maxLength;
	if (control.value.length > maxlimit)
	{
		field.alertMessage(control, "Maximum number of characters allowed is " + maxlimit);
		return false;
	}
	return true;
}

function MemoField_onKeyPress(field, control, event)
{
	maxlimit = field.maxLength;
	if (control.value.length >= maxlimit)
	{
		field.alertMessage(control, "Maximum number of characters allowed is " + maxlimit);
		return false;
	}
	return true;
}

function DateField_popupCalendar()
{
	showCalendar(this.getControl(), 0);
}

function DateField_finalizeDefn(dialog, field)
{
	field.popupCalendar = DateField_popupCalendar;
	field.dateFmtIsKnownFormat = false;
	field.dateItemDelim = null;
	field.dateItemDelimKeyRange = null;
	if (field.dateDataType == DATE_DTTYPE_DATEONLY)
	{
		if (field.dateFormat == "MM/dd/yyyy" || field.dateFormat == "MM/dd/yy")
		{
			field.dateItemDelim = '/';
			field.dateItemDelimKeyRange = SLASH_KEY_RANGE;
			field.dateFmtIsKnownFormat = true;
		}
		else if (field.dateFormat == "MM-dd-yyyy" || field.dateFormat == "MM-dd-yy")
		{
			field.dateItemDelim = '-';
			field.dateItemDelimKeyRange = DASH_KEY_RANGE;
			field.dateFmtIsKnownFormat = true;
		}
	}
}

function DateField_isValid(field, control)
{
	if(field.isRequired() && control.value.length == 0)
	{
		field.alertRequired(control);
		return false;
	}

	return DateField_valueChanged(field, control);
}

function DateField_valueChanged(field, control)
{
	if (field.dateDataType == DATE_DTTYPE_DATEONLY && field.dateFmtIsKnownFormat)
	{
		var result = formatDate(field, control, field.dateItemDelim, field.dateStrictYear);
		control.value = result[1];
		return result[0];
	}
	else if (field.dateDataType == DATE_DTTYPE_TIMEONLY)
	{
		var result = formatTime(field, control);
		return result;
	}
	else if (field.dateDataType == DATE_DTTYPE_MONTH_YEAR_ONLY)
	{
	    var result = formatMonthYearDate(field, control, field.dateItemDelim, field.dateStrictYear);
		control.value = result[1];
		return result[0];
	}
	return true;
}

function DateField_onKeyPress(field, control, event)
{
	if (field.dateDataType == DATE_DTTYPE_DATEONLY && field.dateFmtIsKnownFormat)
	{
		return keypressAcceptRanges(field, control, [NUM_KEYS_RANGE, field.dateItemDelimKeyRange], event);
	}
	else if (field.dateDataType == DATE_DTTYPE_TIMEONLY)
	{
		return keypressAcceptRanges(field, control, [NUM_KEYS_RANGE, COLON_KEY_RANGE], event);
	}
	return true;
}

function SocialSecurityField_onKeyPress(field, control, event)
{
	return keypressAcceptRanges(field, control, [NUM_KEYS_RANGE, DASH_KEY_RANGE], event);
}

function SelectField_onClick(field, control)
{
    var fieldControl;
    // The onclick event handling is only here for RADIO buttons because the onChange doesn't work for them.
    if (field.style == SELECTSTYLE_RADIO)
    {
        // the 'control' object sent from the browser event actually represents just the one radio button and not
        // the array so get the array control object by using the common name shared by all the radio buttons.
        fieldControl = field.getControl();
        if(field.dependentConditions.length > 0)
        {
            var conditionalFields = field.dependentConditions;
            for(var i = 0; i < conditionalFields.length; i++)
                conditionalFields[i].evaluate(field.getDialog(), fieldControl);
        }
    }
    return true;
}

function SelectField_loseFocus(field, control)
{
	if(control.value == "")
		return true;

	//return SelectField_isValid(field, control);
	return this.isValid(field, control);
}

function SelectField_isValid(field, control)
{
	var style = field.style;
	if (style == SELECTSTYLE_POPUP)
	{
		if (field.isRequired() && control.value.length == 0)
		{
			field.alertRequired(control);
			return false;
		}

		if (control.value.length > 0 && field.choicesCaption)
		{
			var valid = -1;

			for (var i=0; i < field.choicesCaption.length; i++)
			{
				if (field.choicesCaption[i] == control.value)
					valid = i;
			}
			if (valid < 0)
			{
				field.alertMessage(control, "Entered field value '" + control.value + "' is not valid. ");
				if (this.isClearTextOnValidateError())
				{
				    control.value = "";
				}
				return false;
			}
			else
			{
				if(this.createAdjancentArea != null)
				{
					// alert("Adjacent set to " + field.choicesValue[valid]);
					this.createAdjancentArea.childNodes.length = 0;
					this.createAdjancentArea.appendChild(document.createTextNode(field.choicesValue[valid]));
				}
				return true;
			}
		}
	}

	if(field.isRequired())
	{
		if(style == SELECTSTYLE_RADIO)
		{
			if(control.value == '')
			{
				field.alertRequired(control[0]);
				return false;
			}
		}
		else if(style == SELECTSTYLE_COMBO)
		{
			if(field.isRequired() && control.options[control.selectedIndex].value.length == 0)
		{
			field.alertRequired(control);
			return false;
		}
		}
		else if(style == SELECTSTYLE_LIST || style == SELECTSTYLE_MULTILIST)
		{
			var selectedCount = getSelectedCount(control);
			if(selectedCount == 0)
			{
				field.alertRequired(control);
				return false;
			}
		}
		else if(style == SELECTSTYLE_MULTICHECK)
		{
			var selectedCount = getCheckedCount(control);
			if(selectedCount == 0)
			{
				field.alertRequired(control[0]);
				return false;
			}
		}
		else if(style == SELECTSTYLE_MULTIDUAL)
		{
			var selectedCount = getSelectedCount(control);
			if(selectedCount == 0)
			{
				field.alertRequired(control);
				return false;
			}
		}
	}
	return true;
}

addFieldType("com.medigy.wicket.form.TextField", null, TextField_isValid, null, TextField_onFocus, TextField_valueChanged, null, null);
addFieldType("com.netspective.sparx.form.field.type.SelectField", null, SelectField_isValid, null, null, SelectField_loseFocus, null, SelectField_onClick);
addFieldType("com.netspective.sparx.form.field.type.BooleanField", null, null, null, null, null, null, BooleanField_onClick);
addFieldType("com.netspective.sparx.form.field.type.MemoField", null, MemoField_isValid, null, null, null, MemoField_onKeyPress);
addFieldType("com.netspective.sparx.form.field.type.DateTimeField", DateField_finalizeDefn, DateField_isValid, null, null, DateField_valueChanged, DateField_onKeyPress, null);
addFieldType("com.netspective.sparx.form.field.type.IntegerField", null, IntegerField_isValid, null, null, null, IntegerField_onKeyPress, null);
addFieldType("com.netspective.sparx.form.field.type.FloatField", null, FloatField_isValid, null, null, null, FloatField_onKeyPress, null);
addFieldType("com.netspective.sparx.form.field.type.SocialSecurityField", null, SocialSecurityField_isValid, null, null, SocialSecurityField_valueChanged, SocialSecurityField_onKeyPress, null);
addFieldType("com.netspective.sparx.form.field.type.PhoneField", null, PhoneField_isValid, null, null, PhoneField_valueChanged, null, null);
addFieldType("com.netspective.sparx.form.field.type.CurrencyField", null, CurrencyField_isValid, CurrencyField_valueChanged, null, null, null, null);

//****************************************************************************
// Date Formatting
//****************************************************************************

var VALID_NUMBERS =  ["0","1","2","3","4","5","6","7","8","9"];

function testText(field, control)
{
	var pattern = field.text_format_pattern;
	if (control.value == '' || pattern == '')
		return true;
   	return pattern.test(control.value);
}

function testCurrency(field, control)
{
	if (control.value == '')
		return true;
	var pattern = field.text_format_pattern;
	return pattern.test(control.value) ;
}

function formatCurrency(field, control)
{
	var test = testCurrency(field, control);
	if (test == false)
	{
		field.alertMessage(control, this.field.text_format_err_msg);
		return false;
	}
	else
	{
		if (control.value != '')
		{
			value = control.value;
			var pattern = field.text_format_pattern;
			if (pattern.exec(value))
			{
				match = pattern.exec(value)
				if (field.negative_pos == "after")
				{
					if (match[1] == "")
						match[1] = field.currency_symbol;
					if (typeof match[3] == "undefined")
						match[3] = ".00";
					control.value = match[1] + match[2] + match[3];
				}
				else if (field.negative_pos == "before")
				{
					if (match[2] == "")
						match[2] = field.currency_symbol;
					if (typeof match[4] == "undefined")
						match[4] = ".00";
					control.value = match[1] + match[2] + match[3] + match[4];
				}
			}
		}
	}
	return true;
}

function testPhone(field, control)
{
	if (control.value == '')
		return true;
	var phonePattern = field.text_format_pattern;
	return phonePattern.test(control.value) ;
}

function formatPhone(field, control)
{
	var test = testPhone(field, control);
	if (test == false)
	{
		field.alertMessage(control, field.text_format_err_msg);
		return false;
	}
	else
	{
		if (control.value != '')
		{
			var phoneStr = control.value;
			if (field.phone_format_type == 'dash')
			{
				phoneStr = phoneStr.replace(field.text_format_pattern, "$1-$2-$3$4");
			}
			else
			{
				phoneStr = phoneStr.replace(field.text_format_pattern, "($1) $2-$3$4");
			}
			control.value = phoneStr;
		}
	}
	return true;
}

function testSSN(field, control)
{
	if (control.value == '')
		return true;
	var ssnPattern = field.text_format_pattern ;
	return ssnPattern.test(control.value);
}

function formatSSN(field, control)
{
	var test = testSSN(field, control);
	if (test == false)
	{
		field.alertMessage(control, "Social Security Number must be in the correct format: 999-99-9999");
		return false;
	}
	if (control.value != '')
	{
		var ssn = control.value;
		ssn = ssn.replace(field.text_format_pattern, "$1-$2-$3");
		control.value = ssn;
	}
	return true;
}


function testTime(field, control)
{
	var inTime = control.value;
	if (inTime == '')
		return true;
	var hr = null;
	var min = null;
	if (inTime.length == 5 && inTime.indexOf(":") == 2)
	{
		hr = inTime.substring(0, 2);
		min = inTime.substring(3);
		if (hr > 23 || min > 59)
		{
			field.alertMessage(control, "Time field must have a valid value");
			return false;
		}
		return true;
	}
	else if (inTime.length == 4 && inTime.indexOf(":") == 1)
	{
		hr = inTime.substring(0, 1);
		min = inTime.substring(2);
		if (hr > 23 || min > 59)
		{
			field.alertMessage(control, "Time field must have a valid value");
			return false;
		}
		return true;
	}
	field.alertMessage(control, "Time field must have the correct format: " + field.dateFormat);
	return false;
}

function formatTime(field, control)
{
	var inTime = control.value;
	newTime = inTime;
	if (field.timeStrict == false && inTime.indexOf(":") == -1)
	{
		if (inTime.length == 4)
		{
			newTime = inTime.substring(0, 2) + ":"  + inTime.substring(2);
		}
		else if (inTime.length == 3)
		{
			newTime = inTime.substring(0, 1) + ":" + inTime.substring(1);
		}
		control.value = newTime;
	}
	return testTime(field, control);
}


function formatMonthYearDate(field, control, delim, strictYear)
{
    var formattedDate;
    if (delim == null)
        delim = "/";

    var today = new Date();
    var currentYear = today.getFullYear().toString();
    var fmtMessage = "Date must be in correct format: MM" + delim + "YYYY'";

    // matches 2 or 4 digit years
    var yearMatchExpr = "(\\d{4}|\\d{2})";
    if (strictYear)
        yearMatchExpr = "(\\d{4})";

    var regEx = new RegExp("(\\d{1,2})(" + delim + ")?" + yearMatchExpr);
    var m = regEx.exec(control.value);
    if (m != null)
    {
        // remember that the first index is the whole value!
        if (m.length == 3)
        {
            // make sure the month is valid
            var month = parseInt(m[1]);
            var year = parseInt(m[2]);

            if ( (month < 1) || (month > 12) )
            {
                field.alertMessage(control, "Month value must be between 1 and 12");
                return [false,control.value];
            }
            // if the year entered was a 2-digit year convert it to a four digit one
            if (m[2].length == 2)
                m[2] = currentYear.substring(0,2) + m[2];
            formattedDate = m[1] + delim + m[2];
        }
        else
        {
            var month = parseInt(m[1]);
            var year = parseInt(m[2]);
            if ( (month < 1) || (month > 12) )
            {
                field.alertMessage(control, "Month value must be between 1 and 12");
                return [false,control.value];
            }
            if (m[3].length == 2)
                m[3] = currentYear.substring(0,2) + m[3];
            formattedDate = m[1] + delim + m[3];
        }
        //control.value = formattedDate;

    }
    else
    {
        field.alertMessage(control, fmtMessage);
        return [false,control.value];
    }
    return [true,formattedDate];
}

function formatDate(field, control, delim, strictYear)
{
	if (delim == null)
		delim = "/";

	var inDate = control.value;
	var today = new Date();
	var currentDate = today.getDate();
	var currentMonth = today.getMonth() + 1;
	var currentYear = today.getYear();
	var fmtMessage = "Date must be in correct format: 'D', 'M" + delim + "D', 'M" + delim + "D" + delim + "Y', or 'M" + delim + "D" + delim + "YYYY'";

	inDate = inDate.toLowerCase();
	var a = splitNotInArray(inDate, VALID_NUMBERS);
	for (i in a)
	{
		a[i] = '' + a[i];
	}
	if (a.length == 0)
	{
		if (inDate.length > 0)
			field.alertMessage(control, fmtMessage);
		return [true, inDate];
	}
	if (a.length == 1)
	{
		if ((a[0].length == 6) || (a[0].length == 8))
		{
			a[2] = a[0].substring(4);
			a[1] = a[0].substring(2,4);
			a[0] = a[0].substring(0,2);
		}
		else
		{
			if (a[0] == 0)
			{
				a[0] = currentMonth;
				a[1] = currentDate;
			}
			else
			{
				a[1] = a[0];
				a[0] = currentMonth;
			}
		}
	}
	if (a.length == 2)
	{
		if (a[0] <= (currentMonth - 3))
			a[2] = currentYear + 1;
		else
			a[2] = currentYear;
	}

	if (strictYear != true)
	{
		if (a[2] < 100 && a[2] > 10)
			a[2] = "19" + a[2];
		if (a[2] < 1000)
			a[2] = "20" + a[2];
	}
	if ( (a[0] < 1) || (a[0] > 12) )
	{
		field.alertMessage(control, "Month value must be between 1 and 12");
		return [false, inDate];
	}
	if ( (a[1] < 1) || (a[1] > 31) )
	{
		field.alertMessage(control, "Day value must be between 1 and 31");
		return [false, inDate];
	}
	if ( (a[2] < 1800) || (a[2] > 2999) )
	{
		field.alertMessage(control, "Year must be between 1800 and 2999");
		return [false, inDate];
	}
	return [true, padZeros(a[0],2) + delim + padZeros(a[1],2) + delim + a[2]];
}

// --------------------------------------------
function getDoubleEntries(field, control)
{
	if (field.successfulEntry) return true;
	if (field.scannable == 'yes' && field.isScanned)
	{
		field.successfulEntry = true;
		return true;
	}

	if(field.firstEntryValue == "")
	{
		field.firstEntryValue = control.value;
		if(field.firstEntryValue == "") field.successfulEntry = true;
		control.value = "";
		control.focus();
		return false;
	}
	else
	{
		if (field.firstEntryValue != control.value)
		{
			control.value = "";
			field.alertMessage(control, "Double Entries do not match.  Previous entry = '"
				+ field.firstEntryValue + "'");
			field.firstEntryValue = "";
		}
		else
		{
			field.successfulEntry = true;
			field.firstEntryValue = "";
			return true;
		}
	}
}

function doubleEntry(field, control)
{
	var result = getDoubleEntries(field, control);
	window.event.cancelBubble = true;
	window.event.returnValue = false;
	return result;
}

// --------------------------------------------
function scanField_changeDisplayValue(field, control)
{
	var beginPattern = new RegExp("^" + field.scanStartCode, field.scanCodeIgnoreCase);
	var endPattern   = new RegExp(field.scanStopCode + "$", field.scanCodeIgnoreCase);

	var newValue = control.value.replace(beginPattern, "");
	newValue = newValue.replace(endPattern, "");

	field.isScanned = (beginPattern.test(control.value) && endPattern.test(control.value)) ? true : false;

	if(field.scanPartnerField != "")
	{
		var partnerField = dialog.fieldsByControlName[field.scanPartnerField];
		var partnerControl = partnerField.getControl();
		partnerControl.value = (field.isScanned) ? 'yes' : 'no';
	}

	if(field.isScanned && field.scanFieldCustomScript != "")
	{
		newValue = field.scanFieldCustomScript(field, control, newValue);
	}

	control.value = newValue;
	return (newValue != "") ? true : false;
}

//****************************************************************************
// Event handlers
//****************************************************************************

function documentOnLeave()
{
    if(document.forms.length == 0)
        return;

	if(document.forms[0].dialog.isShowDataChangedMessageOnLeave() && anyControlChangedEventCalled && ! submittedDialogValid)
		return "You have changed data on this page. If you leave, you will lose the data.";
}

// --------------------------------------------

function documentOnKeyDown()
{
    // TODO: this only works in IE right now, fix it
    if(! window.event)
        return;

    var control = window.event.srcElement;
    var field = control.field;

    if(window.event.keyCode == KEYCODE_ENTER)
    {
        if(field != null && field.doubleEntry == "yes")
            window.event.keyCode = KEYCODE_TAB;
    }

	return true;
}

// --------------------------------------------

function documentOnKeyUp()
{
    // TODO: this only works in IE right now, fix it
    if(! window.event)
        return;

	var control = window.event.srcElement;
	var field = control.field;

	if(field != null && field.autoBlur == "yes")
	{
		field.numCharsEntered++;
		var excRegExp = new RegExp(field.autoBlurExcRegExp, "g");
		var adjustedVal = control.value.replace(excRegExp, "");

		var beginPattern = new RegExp("^" + field.scanStartCode, field.scanCodeIgnoreCase);
		if(control.value.search(beginPattern) != -1)
		{
			var startCodeLength = field.scanStartCode.indexOf("|") != -1 ?
				field.scanStartCode.indexOf("|") : field.scanStartCode.length;
			var stopCodeLength  = field.scanStopCode.indexOf("|") != -1 ?
				field.scanStopCode.indexOf("|") : field.scanStopCode.length;

			if((adjustedVal.length == field.autoBlurLength + startCodeLength + stopCodeLength)
				 && field.numCharsEntered >= field.autoBlurLength -1)
			{
				field.numCharsEntered = 0;

				if (field.scannable == 'yes')
				{
					field.isScanned = false;
					var validScan = scanField_changeDisplayValue(field, control);
					if(! validScan)
					{
						control.value = "";
						return false;
					}
				}

				field.focusNext();
			}
		}
		else
		{
			if(adjustedVal.length == field.autoBlurLength && field.numCharsEntered >= field.autoBlurLength -1)
			{
				field.numCharsEntered = 0;
				field.focusNext();
			}
		}
	}
}

document.onkeydown = documentOnKeyDown;
document.onkeyup   = documentOnKeyUp;
window.onbeforeunload = documentOnLeave;

dialogLibraryLoaded = true;


/**
 * THE FOLLOWING SECTION CONTAINS JAVASCRIPT FUNCTIONS THAT ARE MORE GENERIC IN NATURE
 * ----------------------------------------------------------------------------------------------------
 */

/**
 * ------------------------------------------------------------------------------------------------------
 * DESCRIPTION:
 *     This function checks a group of checkboxes with the same name to see if a
 *     checked checkbox exists with a particular value.
 * ------------------------------------------------------------------------------------------------------
 * INPUT:
 *     checkbox:    checkbox(s) form element
 *     value:       value to look for
 * ------------------------------------------------------------------------------------------------------
 * RETURNS:
 *     True if one or more checked checkbox has the value else False
 * ------------------------------------------------------------------------------------------------------
 */
function checkedCheckedValue(checkbox, value)
{
    if (checkbox.length)
    {
        // multiple checkboxes with the same name
        for(var i = 0; i < checkbox.length; i++)
        {
            if (checkbox[i].checked && checkbox[i].value == value)
                return true;
        }
    }
    else
    {
        // only one checkbox
        if (checkbox.checked && checkbox.value == value)
            return true;
    }
    return false;
}

/**
 * ------------------------------------------------------------------------------------------------------
 * DESCRIPTION:
 *     This function gets the total number of checkboxes that are checked
 * ------------------------------------------------------------------------------------------------------
 * INPUT:
 *     checkBox:    checkbox(s) form element
 * ------------------------------------------------------------------------------------------------------
 * RETURNS:
 *     Zero if no checkboxes are checked
 * ------------------------------------------------------------------------------------------------------
 */
function getCheckedCount(checkbox)
{
    var selectedCount = 0;
    if (checkbox.length)
    {
        // multiple checkboxes with same name
        for(var c = 0; c < checkbox.length; c++)
        {
            if(checkbox[c].checked)
                selectedCount++;
        }
    }
    else
    {
        // one checkbox
        if (checkbox.checked)
            selectedCount = 1;
    }
    return selectedCount;
}

/**
 * ------------------------------------------------------------------------------------------------------
 * DESCRIPTION:
 *     This function gets the total number of options that are selected in an HTML SELECT element
 * ------------------------------------------------------------------------------------------------------
 * INPUT:
 *     select:    SELECT form element
 * ------------------------------------------------------------------------------------------------------
 * RETURNS:
 *     Zero if no options are selected
 * ------------------------------------------------------------------------------------------------------
 */
function getSelectedCount(select)
{
    var selectedCount = 0;
    var options = select.options;
    for(var o = 0; o < options.length; o++)
    {
        if(options[o].selected)
            selectedCount++;
    }
    return selectedCount;
}

/*
 * ------------------------------------------------------------------------------------------------------
 * DESCRIPTION:
 *     Sorts a select box. Uses a simple sort.
 * ------------------------------------------------------------------------------------------------------
 * INPUT:
 *	   objSelect = A <SELECT> object.
 * ------------------------------------------------------------------------------------------------------
 * RETURNS:
 *     None
 * ------------------------------------------------------------------------------------------------------
 * NOTE:
 *    Refactored from dialog.js and used for SelectField MultiDual support
 * ------------------------------------------------------------------------------------------------------
 */
function SimpleSort(objSelect)
{
	var arrTemp = new Array();
	var objTemp = new Object();
	var valueTemp = new Object();
	for(var i=0; i<objSelect.options.length; i++)
	{
		arrTemp[i] = objSelect.options[i];
	}

	for(var x=0; x<arrTemp.length-1; x++)
	{
		for(var y=(x+1); y<arrTemp.length; y++)
		{
			if(arrTemp[x].text > arrTemp[y].text)
			{
				objTemp = arrTemp[x].text;
				arrTemp[x].text = arrTemp[y].text;
				arrTemp[y].text = objTemp;

				valueTemp = arrTemp[x].value;
				arrTemp[x].value = arrTemp[y].value;
				arrTemp[y].value = valueTemp;
			}
		}
	}
	for(var i=0; i<objSelect.options.length; i++)
	{
		alert(objSelect.options[i].text + " " + objSelect.options[i].value);
	}
}

/*
 * ------------------------------------------------------------------------------------------------------
 * DESCRIPTION:
 *    Removes empty select items. This is a helper function for MoveSelectItems.
 * ------------------------------------------------------------------------------------------------------
 * INPUT:
 *     objSelect = A <SELECT> object.
 *     intStart = The start position (zero-based) search. Optimizes the recursion.
 * ------------------------------------------------------------------------------------------------------
 * RETURNS:
 *     None
 * ------------------------------------------------------------------------------------------------------
 * NOTE:
 *    Refactored from dialog.js and used for SelectField MultiDual support
 * ------------------------------------------------------------------------------------------------------
 */
function RemoveEmpties(objSelect, intStart)
{
	for(var i=intStart; i<objSelect.options.length; i++)
	{
		if (objSelect.options[i].value == "")
		{
			objSelect.options[i] = null;    // This removes item and reduces count
			RemoveEmpties(objSelect, i);
			break;
		}
	}
}

/*
 * ------------------------------------------------------------------------------------------------------
 * DESCRIPTION:
 *    Moves items from one select box to another.
 * ------------------------------------------------------------------------------------------------------
 * INPUT:
 *     strFormName = Name of the form containing the <SELECT> elements
 *     strFromSelect = Name of the left or "from" select list box.
 *     strToSelect = Name of the right or "to" select list box
 *     blnSort = Indicates whether list box should be sorted when an item(s) is added
 * ------------------------------------------------------------------------------------------------------
 * RETURNS:
 *     none
 * ------------------------------------------------------------------------------------------------------
*/
function MoveSelectItems(strFormName, strFromSelect, strToSelect, blnSort)
{
	var dialog = eval("document.forms." + strFormName);
	var objSelectFrom = dialog.elements[strFromSelect];
	var objSelectTo = dialog.elements[strToSelect];
	var intLength = objSelectFrom.options.length;

	for (var i=0; i < intLength; i++)
	{
		if(objSelectFrom.options[i].selected && objSelectFrom.options[i].value != "")
		{
			var objNewOpt = new Option();
			objNewOpt.value = objSelectFrom.options[i].value;
			objNewOpt.text = objSelectFrom.options[i].text;
			objSelectTo.options[objSelectTo.options.length] = objNewOpt;
			objSelectFrom.options[i].value = "";
			objSelectFrom.options[i].text = "";
		}
	}

	if (blnSort) SimpleSort(objSelectTo);
	RemoveEmpties(objSelectFrom, 0);
}

/*
 * ------------------------------------------------------------------------------------------------------
 * DESCRIPTION:
 *    Checks to see if the key pressed is allowed
 * ------------------------------------------------------------------------------------------------------
 * INPUT:
 *     field
 *     control
 *     acceptKeyRanges:     array of ascii values
 *     event:               the key press event
 * ------------------------------------------------------------------------------------------------------
 * RETURNS:
 *     True if the originating key is within the accepted key range else False
 * ------------------------------------------------------------------------------------------------------
 * NOTES:
 *     This function has IE-specific code
 * ------------------------------------------------------------------------------------------------------
 */
function keypressAcceptRanges(field, control, acceptKeyRanges, event)
{
	// the event should have been passed in here but for some reason
	// its null, look for it in the window object (works only in IE)
	if (event == null || typeof event == "undefined")
		event = window.event;
	for (i = 0; i < acceptKeyRanges.length; i++)
	{
		var keyCodeValue = null;
		if (event.keyCode)
			keyCodeValue = event.keyCode;
		else
			keyCodeValue = event.which;

		var keyInfo = acceptKeyRanges[i];
		if(keyCodeValue >= keyInfo[0] && keyCodeValue <= keyInfo[1])
			return true;
	}

	// if we get to here, it means we didn't accept any of the ranges
	if (event.cancelBubble)
	    event.cancelBubble = true;
	event.returnValue = false;
	return false;
}

/**
 * ------------------------------------------------------------------------------------------------------
 * DESCRIPTION:
 *     This function returns a string of exactly count characters left padding with zeros
 * ------------------------------------------------------------------------------------------------------
 * INPUT:
 *     number:  string to pad
 *     count:   number of zero paddings
 * ------------------------------------------------------------------------------------------------------
 * RETURNS:
 *     returns a string of exactly count characters left padding with zeros
 * ------------------------------------------------------------------------------------------------------
 */
function padZeros(number, count)
{
	var padding = "0";
	for (var i=1; i < count; i++)
		padding += "0";
	if (typeof(number) == 'number')
		number = number.toString();
	if (number.length < count)
		number = (padding.substring(0, (count - number.length))) + number;
	if (number.length > count)
		number = number.substring((number.length - count));
	return number;
}

/**
 * ------------------------------------------------------------------------------------------------------
 * DESCRIPTION:
 *     This function splits "string" into multiple tokens at "char"
 * ------------------------------------------------------------------------------------------------------
 * INPUT:
 *     strString:       string to parse
 *     strDelimiter:    the token
 * ------------------------------------------------------------------------------------------------------
 * RETURNS:
 *     returns an array of substrings
 * ------------------------------------------------------------------------------------------------------
 */
function splitOnChar(strString, strDelimiter)
{
	var a = new Array();
	var field = 0;
	for (var i = 0; i < strString.length; i++)
	{
		if ( strString.charAt(i) != strDelimiter )
		{
			if (a[field] == null)
				a[field] = strString.charAt(i);
			else
				a[field] += strString.charAt(i);
		}
		else
		{
			if (a[field] != null)
				field++;
		}
	}
	return a;
}

/**
 * ------------------------------------------------------------------------------------------------------
 * DESCRIPTION:
 *     This function Splits "strString" into multiple tokens at inverse of "array"
 * ------------------------------------------------------------------------------------------------------
 * INPUT:
 *     strString:       string to parse
 *     arrArray:        an array of characters
 * ------------------------------------------------------------------------------------------------------
 * RETURNS:
 *     returns an array of substrings
 * ------------------------------------------------------------------------------------------------------
 */
function splitNotInArray(strString, arrArray)
{
	var a = new Array();
	var field = 0;
	var matched;
	for (var i = 0; i < strString.length; i++)
	{
		matched = 0;
		for (k in arrArray)
		{
			if (strString.charAt(i) == arrArray[k])
			{
				if (a[field] == null || typeof a[field] == "undefined")
					a[field] = strString.charAt(i);
				else
					a[field] += strString.charAt(i);
				matched = 1;
				break;
			}
		}
		if ( matched == 0 && a[field] != null )
			field++;
	}
	return a;
}
