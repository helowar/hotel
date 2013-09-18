package com.mangocity.proxy.payment.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import mango.it.alipay.util.Md5Encrypt;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.EncodingUtil;

import com.mangocity.util.log.MyLog;

/**
 * MD5加密以及验证加密信息工具类 功能：1.加密 2.加密验证
 * @author guguoqing
 * 
 */
public class ServletSignUtil {
	private static final MyLog log = MyLog.getLogger(ServletSignUtil.class);
	/**
	 * 加密数据之前，对参数进行字母排序和拼装。
	 * 
	 * @param params
	 *            进行签名的数据
	 * @param privateKey
	 *            key密钥
	 * 
	 * @return 加密后的密文
	 */
	public static String buildSign(Map<String, String> params, String privateKey) {
		// 如果map为空，则不进行任何操作
		if (params == null) {
			return "";
		}
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		StringBuffer paramsStrBuf = new StringBuffer();
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			if (key == null || key.equalsIgnoreCase("sign")
					|| key.equalsIgnoreCase("sign_type")) {
				continue;
			}
			String value = params.get(key);
			if (value == null || value.trim().length() == 0 || "null".equals(value)) {
				continue;
			}
			paramsStrBuf.append((i == 0 ? "" : "&"));
			paramsStrBuf.append(key);
			paramsStrBuf.append("=");
			paramsStrBuf.append(value);

		}
		return Md5Encrypt.md5(paramsStrBuf.append(privateKey).toString());
	}

	/**
	 * 加密信息验证
	 * 
	 * @param sign
	 *            密文
	 * @param Params
	 *            进行签名的数据
	 * @param key
	 *            密钥
	 * @return 验签结果
	 */
	public static boolean verifySign(String sign, Map<String, String> Params,
			String key) {
		boolean verifyResult = false;
		String verifySign = buildSign(Params, key);
		log.info("sign=" + sign + ",verifySign=" + verifySign);
		if (sign.toLowerCase().equals(verifySign.toLowerCase())) {
			verifyResult = true;
		}
		return verifyResult;
	}
	
	/**
	 * 创建HttpClient请求
	 * 
	 * @param postURL
	 *             请求URL
	 * @param xmlStr
	 *             请求字符串
	 * @return
	 *             请求结果
	 */
	public static String HttpClientPost(String postURL, String xmlStr) throws Exception{
	    HttpClient httpclient = new HttpClient();
        //创建post对象,指定远程服务地址
        PostMethod post = new PostMethod(postURL);
        try{
            //将参数用UTF-8编码格式encode
            NameValuePair nvReqData = new NameValuePair("xmlStr", URLEncoder.encode(xmlStr,"UTF-8"));
            NameValuePair[] nameValuePair = new NameValuePair []{nvReqData};
            post.setRequestBody(nameValuePair);
            //设置链接超时
            httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
            //设置读取远程服务返回数据超时时间
            post.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,20*1000);
            // 网络连接返回值
            int iConn = httpclient.executeMethod(post);
            if(iConn != 200) {
                return "HttpConnectionException = " + iConn;
            }
            //对远程服务返回数据用UTF-8进行encoding
            return EncodingUtil.getString(post.getResponseBody(),"UTF-8");
        }catch(HttpException he){
            throw he;
        }catch(IOException ie){
            throw ie;
        }catch(Exception e){
            throw e;
        }finally{
            post.releaseConnection();
        }
	}
}
