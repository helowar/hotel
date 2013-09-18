/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.mangocity.util.collections;

import java.io.Serializable;

/**
 * project: MyOA module : version : 1.0
 * 
 * @author zhengxin purpose : you can only get string from map . this map gurantee you never get
 *         null value from string. so you need not to worry about null value appear. created at
 *         2006-6-25 ����09:14:24
 */
public class StringMap extends FormatMap implements Serializable {

    public Object get(Object key) {
        return this.getString(key);
    }

}
