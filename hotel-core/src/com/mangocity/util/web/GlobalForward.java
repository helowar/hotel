/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.mangocity.util.web;

/**
 */
public interface GlobalForward {

    /**
     * ��½��ת��
     */
    static String FORWARD_TO_LOGIN = "forwardToLogin";

    /**
     * ��ҳ��ת��
     */
    static String FORWARD_TO_HOME = "forwardToHome";

    /**
     * XML���
     */
    static String FORWARD_BY_XML = "forwardByXml";

    /**
     * ���URL������ת
     */
    static String FORWARD_BY_URL = "forwardByURL";

    static String FORWARD_TO_ERROR = "forwardToError";

    /**
     * 弹出操作结果提示窗口
     */
    static String FORWARD_TO_MSGBOX = "forwardToMsgBox";

    /**
     * 操作结果提示页面
     */
    static String FORWARD_TO_MSG = "forwardToMsg";

    /**
     * 操作结果提示页面Ex
     */
    static String FORWARD_TO_MSG_EX = "forwardToMsgEx";
    
    /**
     * 操作结果提示页面,并关闭当前页 add by shengwei.zuo 2009-10-26
     */
    static String FORWARD_TO_MSG_CLOSE = "forwardToMsgClose";

}
