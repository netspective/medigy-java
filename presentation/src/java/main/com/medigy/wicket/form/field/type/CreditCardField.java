package com.medigy.wicket.form.field.type;

import wicket.markup.html.form.TextField;

/**
 * Credit card field definition class.  A credit card field definition object
 * defines the validation rules for a credit card account number field.
 */
public class CreditCardField extends TextField
{
    // Mask attributes
    static protected final String VALIDATE_MASKS = "validateMasks";
    static protected final String AMEX_MASKS = "applyAmexMasks";
    static protected final String BANKCARD_MASKS = "applyBankCardMasks";
    static protected final String DINERSCLUB_MASKS = "applyDinersClubMasks";
    static protected final String DISCOVER_MASKS = "applyDiscoverMasks";
    static protected final String ENROUTE_MASKS = "applyEnRouteMasks";
    static protected final String JCB_MASKS = "applyJcbMasks";
    static protected final String MASTERCARD_MASKS = "applyMasterCardMasks";
    static protected final String VISA_MASKS = "applyVisaMasks";
    static protected final String ZERO_LENGTH_STRING = "";

    // class attributes
    private boolean validateMasks = false;
    private boolean applyAmexMasks = false;
    private boolean applyBankCardMasks = false;
    private boolean applyDinersClubMasks = false;
    private boolean applyDiscoverMasks = false;
    private boolean applyEnRouteMasks = false;
    private boolean applyJcbMasks = false;
    private boolean applyMasterCardMasks = false;
    private boolean applyVisaMasks = false;

    // account number masks

    private static final String amexMasks[] =       {"34xxxxxxxxxxxxx",
                                                     "37xxxxxxxxxxxxx"};
    private static final String bankCardMasks[] =   {"56xxxxxxxxxxxxxx"};
    private static final String dinersClubMasks[] = {"300xxxxxxxxxxx",
                                                     "301xxxxxxxxxxx",
                                                     "302xxxxxxxxxxx",
                                                     "303xxxxxxxxxxx",
                                                     "304xxxxxxxxxxx",
                                                     "305xxxxxxxxxxx",
                                                     "36xxxxxxxxxxxx",
                                                     "38xxxxxxxxxxxx"};
    private static final String discoverMasks[] =   {"6011xxxxxxxxxxxx"};
    private static final String enRouteMasks[] =    {"2014xxxxxxxxxxx",
                                                     "2149xxxxxxxxxxx"};
    private static final String jcbMasks[] =        {"3xxxxxxxxxxxxxxx",
                                                     "1800xxxxxxxxxxx",
                                                     "2131xxxxxxxxxxx"};
    private static final String masterCardMasks[] = {"51xxxxxxxxxxxxxx",
                                                     "52xxxxxxxxxxxxxx",
                                                     "53xxxxxxxxxxxxxx",
                                                     "54xxxxxxxxxxxxxx",
                                                     "55xxxxxxxxxxxxxx"};
    private static final String visaMasks[] =       {"4xxxxxxxxxxxxxxx",
                                                     "4xxxxxxxxxxxx"};


    /**
     * Constructor
     *
     * @param componentId
     */
    public CreditCardField(final String componentId)
    {
        super(componentId);
    }


    public void updateModel()
    {
        String input = getInput();

        // TODO - validation code

        setModelObject(input);
    }

    /**
     * Accessor method for the validate masks attribute.  The validate masks
     * attribute defines whether the field should be validated against
     * standard account number mask(s).<p>
     *
     * If this attribute value is set to <code>true</code>, the field
     * will examine the following optional attributes to determine which
     * masks will be included in the set of valid masks for the field:<p>
     *
     * <ul>
     * <li>applyAmexMasks</li>
     * <li>applyBankCardMasks</li>
     * <li>applyDinersClubMasks</li>
     * <li>applyDiscoverMasks</li>
     * <li>applyEnRouteMasks</li>
     * <li>applyJcbMasks</li>
     * <li>applyMasterCardMasks</li>
     * <li>applyVisaMasks</li>
     *</ul>
     *
     * The value <code>false</code> will be assumed for any optional
     * applyXXXMasks attribute not explicitly defined for the field definition.
     * Regardless of whether the value for an optional attribute is explicitly
     * or implicitly set to <code>false</code>, a value of <code>false</code>
     * for the validateMasks attribute will override any value set for the
     * optional attributes contained in the above list.<p>
     *
     * @return  validate masks attribute
     */
    public boolean isValidateMasks()
    {
        return this.validateMasks;
    }

    /**
     * Setter method for the validate masks attribute.  The validate masks
     * attribute defines whether the field should be validated against
     * standard account number mask(s).<p>
     *
     * If this attribute value is set to <code>true</code>, the field
     * will examine the following optional attributes to determine which
     * masks will be included in the set of valid masks for the field:<p>
     *
     * <ul>
     * <li>applyAmexMasks</li>
     * <li>applyBankCardMasks</li>
     * <li>applyDinersClubMasks</li>
     * <li>applyDiscoverMasks</li>
     * <li>applyEnRouteMasks</li>
     * <li>applyJcbMasks</li>
     * <li>applyMasterCardMasks</li>
     * <li>applyVisaMasks</li>
     *</ul>
     *
     * The value <code>false</code> will be assumed for any optional
     * applyXXXMasks attribute not explicitly defined for the field definition.
     * Regardless of whether the value for an optional attribute is explicitly
     * or implicitly set to <code>false</code>, a value of <code>false</code>
     * for the validateMasks attribute will override any value set for the
     * optional attributes contained in the above list.<p>
     *
     * @param  validateMasks  validate masks attribute
     */
    public void setValidateMasks(boolean validateMasks)
    {
        this.validateMasks = validateMasks;
    }

    /**
     * Accessor method for the apply Amex masks attribute.  The apply
     * Amex masks attribute defines whether the field should be
     * validated against the standard Amex credit card account
     * number mask(s).<p>
     *
     * This value defaults to <code>false</code> if not explicitly set in the
     * field definition XML file.<p>
     *
     * @return  apply Amex masks attribute
     */
    public boolean isApplyAmexMasks()
    {
        return this.applyAmexMasks;
    }

    /**
     * Setter method for the apply Amex masks attribute.  The apply Amex
     * masks attribute defines whether the field should be validated against
     * the standard Amex credit card account number mask(s).<p>
     *
     * This value defaults to <code>false</code> if not explicitly set in the
     * field definition XML file.<p>
     *
     * @param  applyAmexMasks  apply Amex masks attribute
     */
    public void setApplyAmexMasks(boolean applyAmexMasks)
    {
        this.applyAmexMasks = applyAmexMasks;
    }

    /**
     * Accessor method for the apply Bank Card masks attribute.  The apply
     * Bank Card masks attribute defines whether the field should be
     * validated against the standard Bank Card credit card account
     * number mask(s).<p>
     *
     * This value defaults to <code>false</code> if not explicitly set in the
     * field definition XML file.<p>
     *
     * @return  apply Bank Card masks attribute
     */
    public boolean isApplyBankCardMasks()
    {
        return this.applyBankCardMasks;
    }

    /**
     * Setter method for the apply Bank Card masks attribute.  The apply Bank
     * Card masks attribute defines whether the field should be validated against
     * the standard Bank Card credit card account number mask(s).<p>
     *
     * This value defaults to <code>false</code> if not explicitly set in the
     * field definition XML file.<p>
     *
     * @param  applyBankCardMasks  apply Bank Card masks attribute
     */
    public void setApplyBankCardMasks(boolean applyBankCardMasks)
    {
        this.applyBankCardMasks = applyBankCardMasks;
    }

    /**
     * Accessor method for the apply Diner's Club masks attribute.  The apply
     * Diner's Club masks attribute defines whether the field should be
     * validated against the standard Diner's Club credit card account
     * number mask(s).<p>
     *
     * This value defaults to <code>false</code> if not explicitly set in the
     * field definition XML file.<p>
     *
     * @return  apply Diner's Club masks attribute
     */
    public boolean isApplyDinersClubMasks()
    {
        return this.applyDinersClubMasks;
    }

    /**
     * Setter method for the apply Diner's Club masks attribute.  The apply
     * Diner's Club masks attribute defines whether the field should be
     * validated against the standard Diner's Club credit card account
     * number mask(s).<p>
     *
     * This value defaults to <code>false</code> if not explicitly set in the
     * field definition XML file.<p>
     *
     * @param  applyDinersClubMasks  apply Diner's Club masks attribute
     */
    public void setApplyDinersClubMasks(boolean applyDinersClubMasks)
    {
        this.applyDinersClubMasks = applyDinersClubMasks;
    }

    /**
     * Accessor method for the apply Discover masks attribute.  The apply
     * Discover masks attribute defines whether the field should be
     * validated against the standard Discover credit card account
     * number mask(s).<p>
     *
     * This value defaults to <code>false</code> if not explicitly set in the
     * field definition XML file.<p>
     *
     * @return  apply Discover masks attribute
     */
    public boolean isApplyDiscoverMasks()
    {
        return this.applyDiscoverMasks;
    }

    /**
     * Setter method for the apply Discover masks attribute.  The apply Discover
     * masks attribute defines whether the field should be validated against
     * the standard Discover credit card account number mask(s).<p>
     *
     * This value defaults to <code>false</code> if not explicitly set in the
     * field definition XML file.<p>
     *
     * @param  applyDiscoverMasks  apply Discover masks attribute
     */
    public void setApplyDiscoverMasks(boolean applyDiscoverMasks)
    {
        this.applyDiscoverMasks = applyDiscoverMasks;
    }

    /**
     * Accessor method for the apply enRoute masks attribute.  The apply
     * enRoute masks attribute defines whether the field should be
     * validated against the standard enRoute credit card account
     * number mask(s).<p>
     *
     * This value defaults to <code>false</code> if not explicitly set in the
     * field definition XML file.<p>
     *
     * @return  apply enRoute masks attribute
     */
    public boolean isApplyEnRouteMasks()
    {
        return this.applyEnRouteMasks;
    }

    /**
     * Setter method for the apply enRoute masks attribute.  The apply enRoute
     * masks attribute defines whether the field should be validated against
     * the standard enRoute credit card account number mask(s).<p>
     *
     * This value defaults to <code>false</code> if not explicitly set in the
     * field definition XML file.<p>
     *
     * @param  applyEnRouteMasks  apply enRoute masks attribute
     */
    public void setApplyEnRouteMasks(boolean applyEnRouteMasks)
    {
        this.applyEnRouteMasks = applyEnRouteMasks;
    }

    /**
     * Accessor method for the apply JCB masks attribute.  The apply
     * JCB masks attribute defines whether the field should be
     * validated against the standard JCB credit card account
     * number mask(s).<p>
     *
     * This value defaults to <code>false</code> if not explicitly set in the
     * field definition XML file.<p>
     *
     * @return  apply JCB masks attribute
     */
    public boolean isApplyJcbMasks()
    {
        return this.applyJcbMasks;
    }

    /**
     * Setter method for the apply JCB masks attribute.  The apply JCB
     * masks attribute defines whether the field should be validated against
     * the standard JCB credit card account number mask(s).<p>
     *
     * This value defaults to <code>false</code> if not explicitly set in the
     * field definition XML file.<p>
     *
     * @param  applyJcbMasks  apply JCB masks attribute
     */
    public void setApplyJcbMasks(boolean applyJcbMasks)
    {
        this.applyJcbMasks = applyJcbMasks;
    }

    /**
     * Accessor method for the apply Master Card masks attribute.  The apply
     * Master Card masks attribute defines whether the field should be
     * validated against the standard Master Card credit card account
     * number mask(s).<p>
     *
     * This value defaults to <code>false</code> if not explicitly set in the
     * field definition XML file.<p>
     *
     * @return  apply Master Card masks attribute
     */
    public boolean isApplyMasterCardMasks()
    {
        return this.applyMasterCardMasks;
    }

    /**
     * Setter method for the apply Master Card masks attribute.  The apply
     * Master Card masks attribute defines whether the field should be
     * validated against the standard Master Card credit card account
     * number mask(s).<p>
     *
     * This value defaults to <code>false</code> if not explicitly set in the
     * field definition XML file.<p>
     *
     * @param  applyMasterCardMasks  apply Master Card masks attribute
     */
    public void setApplyMasterCardMasks(boolean applyMasterCardMasks)
    {
        this.applyMasterCardMasks = applyMasterCardMasks;
    }

    /**
     * Accessor method for the apply Visa masks attribute.  The apply
     * Visa masks attribute defines whether the field should be
     * validated against the standard Visa credit card account
     * number mask(s).<p>
     *
     * This value defaults to <code>false</code> if not explicitly set in the
     * field definition XML file.<p>
     *
     * @return  apply Visa masks attribute
     */
    public boolean isApplyVisaMasks()
    {
        return this.applyVisaMasks;
    }

    /**
     * Setter method for the apply Visa masks attribute.  The apply Visa
     * masks attribute defines whether the field should be validated against
     * the standard Visa credit card account number mask(s).<p>
     *
     * This value defaults to <code>false</code> if not explicitly set in the
     * field definition XML file.<p>
     *
     * @param  applyVisaMasks  apply Visa masks attribute
     */
    public void setApplyVisaMasks(boolean applyVisaMasks)
    {
        this.applyVisaMasks = applyVisaMasks;
    }


    /**
     * Returns a boolean that indicates if the field is valid.  Also adds
     * one or more Struts <code>ActionError</code> objects to the input
     * Struts <code>ActionErrors</code> collection passed into the method,
     * which indicate the specific nature of any identified validation
     * error.<p>
     *
     * Method performs validation specific to this <code>Field</code>.
     * The base method that calls this method using the template design pattern
     * performs validation common to all <code>Field</code> objects.<p>
     *
     * @param fieldValue  The value to be validated
     *
     * @return <code>true</code> if the field passed validation, or
     *         <code>false</code> if the field value was invalid
     */
    public boolean doIsValid(final String fieldValue)
    {
        int fieldValueLength = fieldValue.length();

        // note: we do not want to return multiple error messages in this
        // case, so return immediately upon detecting a validation error

        // ensure that the field contains numeric content only
        for (int i = 0; i < fieldValueLength; i++)
        {
            if (Character.isDigit(fieldValue.charAt(i)) == false)
            {
                return false;
            }
        }

        this.setValidateMasks(true);
        // validate masks
        if (this.validateMasks && this.areMasksValid(fieldValue) == false)
        {
            return false;
        }

        // determine if the field satisfies the Luhn / Mod 10 algorithm
        if (this.getLuhnParity(fieldValue) % 10 != 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }


    /**
     * Returns a boolean indicating if the specified account value matches at
     * least one of the active account masks.  An active mask is one that
     * has been specified as active in the field definition.<p>
     *
     * e.g., In addition to the required attributes, an example credit card field
     * defintion contains the following attributes:<br>
     * <ul>
     * <li>validateMasks="true"</li>
     * <li>applyVisaMasks="true"</li>
     * <li>applyMasterCardMasks="false"</li>
     * </ul>
     *
     * In the example above, the field will validate the Visa masks only.
     * The Master Card masks are explicitly set to <code>false</code> and
     * all other masks, which are not specified, are implicitly set to
     * <code>false</code>.<p>
     *
     * @param  accountValue  The account value to be validated
     *
     * @return  <code>true</code> if the account code matches at least one
     * active mask, <code>false</code> otherwise
     *
     */
    protected boolean areMasksValid(final String accountValue)
    {
        boolean valid = false;

        for (int i = 0; i < this.amexMasks.length; i++)
        {
            if (this.isMaskValid(accountValue, this.amexMasks[i]))
            {
                valid = true;
                return valid;
            }
        }
        for (int i = 0; i < this.bankCardMasks.length; i++)
        {
            if (this.isMaskValid(accountValue, this.bankCardMasks[i]))
            {
                valid = true;
                return valid;
            }
        }
        for (int i = 0; i < this.dinersClubMasks.length; i++)
        {
            if (this.isMaskValid(accountValue, this.dinersClubMasks[i]))
            {
                valid = true;
                return valid;
            }
        }
        for (int i = 0; i < this.discoverMasks.length; i++)
        {
            if (this.isMaskValid(accountValue, this.discoverMasks[i]))
            {
                valid = true;
                return valid;
            }
        }
        for (int i = 0; i < this.enRouteMasks.length; i++)
        {
            if (this.isMaskValid(accountValue, this.enRouteMasks[i]))
            {
                valid = true;
                return valid;
            }
        }
        for (int i = 0; i < this.jcbMasks.length; i++)
        {
            if (this.isMaskValid(accountValue, this.jcbMasks[i]))
            {
                valid = true;
                return valid;
            }
        }
        for (int i = 0; i < this.masterCardMasks.length; i++)
        {
            if (this.isMaskValid(accountValue, this.masterCardMasks[i]))
            {
                valid = true;
                return valid;
            }
        }
        for (int i = 0; i < this.visaMasks.length; i++)
        {
            if (this.isMaskValid(accountValue, this.visaMasks[i]))
            {
                valid = true;
                return valid;
            }
        }

        return valid;
    }

    /**
     * Returns a boolean that indicates if the specified account number
     * matches an input account number mask.  Masks are in the format
     * Nxxxxxxxxxxxxxxx, where N is a digit that must be an exact match at
     * the specified position, x is a character that indicates that any
     * value at the associated account position is valid, and the length of
     * the mask is the required length for the specified account number.<p>
     *
     * @param  accountValue  credit card account number
     * @param  accountMask  account number mask
     *
     * @return  <code>true</code> if the account number matches the mask, or
     *          <code>false</code> otherwise
     *
     */
    protected boolean isMaskValid(final String accountValue, final String accountMask)
    {
        boolean valid = true;

        if (accountMask == null || accountMask.length() == 0 ||
                accountValue == null || accountValue.length() == 0) {
                    // the validation framework should not allow this case to occur
                    //throw new FieldStoreException("Invalid value passed to " +
                    //      this.getClass().getName() + ".isMaskValid");
                }

        // check the length
        if (accountMask.length() != accountValue.length())
        {
            valid = false;
        }
        else
        {
            // length of the mask matches the length of the account value...
            // walk the account value string and apply the mask
            for (int i = 0; i < accountValue.length(); i++)
            {
                if (Character.isDigit(accountMask.charAt(i)))
                {
                    valid = valid && (accountValue.charAt(i) == accountMask.charAt(i));
                }
            }
        }
        return valid;
    }

    /**
     * Returns the Luhn parity of the specified account.  Luhn parity is
     * built into all standard credit card account numbers.<p>
     *
     * @param  accountValue  credit card account number
     *
     * @return   Luhn parity for the specified account number
     *
     */
    protected int getLuhnParity(String accountValue)
    {

        if (accountValue == null || accountValue.equals(ZERO_LENGTH_STRING))
        {
            return 11;
        }

        int fieldValueLength = accountValue.length();

        int reverseEvenParity = 0;
        int reverseOddParity = 0;

        // calculate the reverse even parity;
        // walk the account string starting at the second to the last character
        // (i.e., the first reverse even index) and move to the left
        for (int i = fieldValueLength - 2; i >= 0; i -= 2)
        {
            int evenValue = Character.getNumericValue(accountValue.charAt(i)) * 2;
            if (evenValue > 9)
            {
                // value is greater than 9, add the digits
                evenValue -= 9;
                // subrtacting 9 is the same thing as adding the digits for the
                // range 10 to 18, which will always be the range for a single
                // base 10 digit multiplied by two whose value is greater than 9
            }
            reverseEvenParity += evenValue;
        }

        // calculate the reverse odd parity;
        // walk the account string starting at the last character (i.e.,
        // the first reverse odd index) and move to the left
        for (int i = fieldValueLength - 1; i >= 0; i -= 2)
        {
            reverseOddParity += Character.getNumericValue(accountValue.charAt(i));
        }

        // return the sum
        return reverseEvenParity + reverseOddParity;
    }
}

