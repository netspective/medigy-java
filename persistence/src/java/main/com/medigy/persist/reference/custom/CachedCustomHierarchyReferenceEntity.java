/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.reference.custom;

public interface CachedCustomHierarchyReferenceEntity  extends CachedCustomReferenceEntity
{
    public CachedCustomHierarchyReferenceEntity getParent();
    public CustomHierarchyReferenceEntity getParentEntity();
}
