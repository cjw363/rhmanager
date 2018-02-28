package com.cjw.rhmanager.utils;

import com.alibaba.fastjson.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

public class CommUtil {
	/**
	 * 随机生成六位数验证码 
	 * @return
	 */
	public static int getRandomNum(){
		 Random r = new Random();
		 return r.nextInt(900000)+100000;
		 //(Math.random()*(999999-100000)+100000)
	}
	
	/**
	 * 检测字符串是否不为空(null,"","null")
	 * @param s
	 * @return 不为空则返回true，否则返回false
	 */
	public static boolean notEmpty(String s){
		return s!=null && !"".equals(s) && !"null".equals(s);
	}
	
	/**
	 * 检测字符串是否为空(null,"","null")
	 * @param s
	 * @return 为空则返回true，不否则返回false
	 */
	public static boolean isEmpty(String s){
		return s==null || "".equals(s) || "null".equals(s);
	}
	
	/**
	 * 字符串转换为字符串数组
	 * @param str 字符串
	 * @param splitRegex 分隔符
	 * @return
	 */
	public static String[] str2StrArray(String str,String splitRegex){
		if(isEmpty(str)){
			return null;
		}
		return str.split(splitRegex);
	}
	
	/**
	 * 用默认的分隔符(,)将字符串转换为字符串数组
	 * @param str	字符串
	 * @return
	 */
	public static String[] str2StrArray(String str){
		return str2StrArray(str,",\\s*");
	}
	
	/**
	 * 按照yyyy-MM-dd HH:mm:ss的格式，日期转字符串
	 * @param date
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String date2Str(Date date){
		return date2Str(date,"yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 按照yyyy-MM-dd HH:mm:ss的格式，字符串转日期
	 * @param date
	 * @return
	 */
	public static Date str2Date(String date){
		if(notEmpty(date)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				return sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return new Date();
		}else{
			return null;
		}
	}
	
	/**
	 * 按照参数format的格式，日期转字符串
	 * @param date
	 * @param format
	 * @return
	 */
	public static String date2Str(Date date,String format){
		if(date!=null){
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		}else{
			return "";
		}
	}
	public static String long2DateStr(Object time,String format){
		if(time == null || "".equals(time)){
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);  
		String d = sdf.format(time); 
		return d;
	}
	public static String get32UUID() {
		String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
		return uuid;
	}

    public static String getIpAddress(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    public static String paramByString(Object object){
    	if(object != null && !"".equals(object)){
    		StringBuffer result = new StringBuffer("(");
    		String[] objs = object.toString().split(",");
    		for(String str : objs){
    			result.append("'"+str+"',");   			
    		}
    		result.setCharAt(result.length()-1, ')');
    		return result.toString();
    	}
    	return null;
    }
    
    public static String paramByLong(Object object){
    	if(object != null && !"".equals(object)){
    		StringBuffer result = new StringBuffer("(");
    		String[] objs = object.toString().split(",");
    		for(String str : objs){
    			result.append(str+",");
    		}
    		result.setCharAt(result.length()-1, ')');
    		return result.toString();
    	}
    	return null;
    }
    
    public static int paramConvert(String param,int initValue){
    	return param==null ? initValue : Integer.parseInt(param);
    }
    
    public static String getFieldNameByLevel(String level){
		String fieldName=null;
		if(level.equals("Ⅰ级")){
			fieldName="LEVEL1_DESC";
		}else if(level.equals("Ⅱ级")){
			fieldName="LEVEL2_DESC";
		}else if(level.equals("Ⅲ级")){
			fieldName="LEVEL3_DESC";
		}else if(level.equals("Ⅳ级")){
			fieldName="LEVEL4_DESC";
		}
		return fieldName;
	}
    /* 
     * 将时间转换为时间戳
     */    
    public static String dateToStamp(String s) {
    	Date date;
        String res;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = simpleDateFormat.parse(s);
			long ts = date.getTime();
			res = String.valueOf(ts).substring(0, 10);
			return res;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
    }
    /* 
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s+"000");
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
    
    public static JSONObject doPost(String url, Map<String,String> map, String charset) throws Exception{

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost( url );
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for( String key : map.keySet() ){
            params.add(new BasicNameValuePair( key , map.get(key)));
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, charset);
        httpPost.setEntity(entity);
        HttpResponse httpResponse = httpClient.execute(httpPost);

        JSONObject jsonObject = null;
        if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity,"UTF-8");
            jsonObject = JSONObject.parseObject(result);
        }
        return jsonObject;
    }
    
    public static ParamData xml2map(String xml) {
        try {
            org.dom4j.Document doc = DocumentHelper.parseText(xml);
            Element body = doc.getRootElement();
            ParamData vo = buildXmlBody2map(body);
            return vo;
        } catch (DocumentException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    private static ParamData buildXmlBody2map(Element body) {
        ParamData vo = new ParamData();
        if (body != null) {
            List<Element> elements = body.elements();
            for (Element element : elements) {
                String key = element.getName();
                if (key!=null&&!"".equals(key)) {
                    if (element.elements().size() > 0) {
                        vo.put(element.getName(), buildXmlBody2map(element));
                    } else {
                        String type = element.attributeValue("type", "java.lang.String");
                        String text = element.getText().trim();
                        Object value = null;
                        if (String.class.getCanonicalName().equals(type)) {
                            value = text;
                        } else if (Character.class.getCanonicalName().equals(type)) {
                            value = new Character(text.charAt(0));
                        } else if (Boolean.class.getCanonicalName().equals(type)) {
                            value = new Boolean(text);
                        } else if (Short.class.getCanonicalName().equals(type)) {
                            value = Short.parseShort(text);
                        } else if (Integer.class.getCanonicalName().equals(type)) {
                            value = Integer.parseInt(text);
                        } else if (Long.class.getCanonicalName().equals(type)) {
                            value = Long.parseLong(text);
                        } else if (Float.class.getCanonicalName().equals(type)) {
                            value = Float.parseFloat(text);
                        } else if (Double.class.getCanonicalName().equals(type)) {
                            value = Double.parseDouble(text);
                        } else if (java.math.BigInteger.class.getCanonicalName().equals(type)) {
                            value = new java.math.BigInteger(text);
                        } else if (java.math.BigDecimal.class.getCanonicalName().equals(type)) {
                            value = new java.math.BigDecimal(text);
                        } else if (Map.class.getCanonicalName().equals(type)) {
                            value = buildXmlBody2map(element);
                        } else {
                        }
                        vo.put(key, value);
                    }
                }
            }
        }
        return vo;
    }

    public static String resultMapToXml(String result){
        Map<Object,Object> resultmap = new HashMap<Object,Object>();
        resultmap.put("CODE", "00");
        resultmap.put("MESSAGE", result);
        resultmap.put("TIME", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
	    String resultXml ="";
	    //        String resultXml = XmlUtils.converterKitData(resultmap);
        return resultXml;
    }
}