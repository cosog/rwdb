package com.cosog.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Proxy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.cosog.main.AgileCalculate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import oracle.sql.CLOB;

public class StringManagerUtils {
	private final static String DATEPATTERN = "yyyy-MM-dd";
	private static final Logger logger = Logger.getLogger(AgileCalculate.class.getName());
	public static boolean isNotNull(String value) {
        boolean flag = false;
        if (value != null && value.trim().length() > 0 && (!"".equals(value.trim())) && !"null".equalsIgnoreCase(value)) {
            flag = true;
        }
        return flag;
    }

    public static boolean isNum(String str) {
        return str != null && str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }
    
    public static boolean isNumber(String value) {
        return isInteger(value) || isDouble(value);
    }
    
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            if (value.contains("."))
                return true;
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
	public static Date stringToDate(String strDate) {
        Date s_date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(DATEPATTERN);
        try {
            s_date = (Date) sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            StringManagerUtils.printLogFile(logger, "error", e, "error");;
        }
        return s_date;
    }
	
	public static String getCurrentDayMonth(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        return formatter.format(stringToDate(date));
    }

    public static String getCurrentDayYear(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        return formatter.format(stringToDate(date));
    }

    public static String getCurrentMonth() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        return formatter.format(new Date());
    }

    public static String getCurrentTime() {
        String time = null;
        Date now = new Date();
        SimpleDateFormat f = new SimpleDateFormat(DATEPATTERN);
        time = f.format(now);
        return time;
    }

    public static String getCurrentTime(String format) {
        String time = null;
        Date now = new Date();
        SimpleDateFormat f = new SimpleDateFormat(format);
        time = f.format(now);
        return time;
    }
    
    public static String convertBlobToString(Blob blob) {
        String result = "";
        try {
            ByteArrayInputStream msgContent = (ByteArrayInputStream) blob.getBinaryStream();
            byte[] byte_data = new byte[msgContent.available()];
            msgContent.read(byte_data, 0, byte_data.length);
            result = new String(byte_data);
        } catch (SQLException e) {
            e.printStackTrace();
            StringManagerUtils.printLogFile(logger, "error", e, "error");;
        }
        return result;
    }

    public static String convertBLOBtoString(Blob BlobContent) {
        byte[] msgContent = null;
        try {
            msgContent = BlobContent.getBytes(1, (int) BlobContent.length());
        } catch (SQLException e1) {
            e1.printStackTrace();
            logger.error("error", e1);
        }
        String newStr = "";
        long BlobLength;
        try {
            BlobLength = BlobContent.length(); 
            if (msgContent == null || BlobLength == 0)
            {
                return "";
            } else 
            {
                newStr = new String(BlobContent.getBytes(1, msgContent.length), "gb2312"); // 绠�鍖栧鐞嗭紝鍙彇鍓�900瀛楄妭
                return newStr;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            StringManagerUtils.printLogFile(logger, "error", e, "error");;
        }
        return newStr;
    }

    public static float stringToFloat(String value, int bit) {
        StringBuffer buf = new StringBuffer();
        buf.append("0.");
        for (int i = 1; i <= bit; i++) {
            buf.append("0");
        }
        String sbit = buf.toString();
        DecimalFormat sumd = new DecimalFormat(sbit);
        float sum = 0;
        if (StringManagerUtils.isNotNull(value)) {
            sum = Float.parseFloat(value);
        }
        String sumstr = "";
        sumstr = sumd.format(sum);
        sum = Float.parseFloat(sumstr);
        return sum;
    }

    public static String floatToString(float value, int bit) {
        StringBuffer buf = new StringBuffer();
        buf.append("0.");
        for (int i = 1; i <= bit; i++) {
            buf.append("0");
        }
        String sbit = buf.toString();
        DecimalFormat sumd = new DecimalFormat(sbit);
        float sum = 0;
        if (StringManagerUtils.isNotNull(value + "")) {
            sum = Float.parseFloat(value + "");
        }
        String sumstr = "";
        sumstr = sumd.format(sum);
        return sumstr;
    }

    public static float stringToFloat(String value) {
        float sum = 0;
        if (StringManagerUtils.isNotNull(value)) {
            try {
                sum = Float.parseFloat(value);
            } catch (Exception e) {
                return 0;
            }
        }
        return sum;
    }

    public static double stringToDouble(String value) {
        double sum = 0;
        if (StringManagerUtils.isNotNull(value)) {
            try {
                sum = Double.parseDouble(value);
            } catch (Exception e) {
                return 0;
            }
        }
        return sum;
    }

    public static boolean stringToBoolean(String value) {
        boolean result = false;
        try {
            if (StringManagerUtils.isNum(value) && StringManagerUtils.stringToInteger(value) > 0) {
                result = true;
            } else {
                result = Boolean.parseBoolean(value);
            }
        } catch (Exception e) {
            return false;
        }
        return result;
    }

    public static int stringToInteger(String value) {
        int sum = 0;
        if (StringManagerUtils.isNotNull(value)) {
            try {
                if (value.contains(".")) {
                    sum = (int) Math.floor(Double.parseDouble(value));
                } else {
                    sum = Integer.parseInt(value);
                }
            } catch (Exception e) {
                return 0;
            }
        }
        return sum;
    }

    public static String BLOBtoString(oracle.sql.BLOB blob) throws SQLException, IOException {
        byte[] bytes;
        BufferedInputStream bis = null;
        bis = new BufferedInputStream(blob.getBinaryStream());
        bytes = new byte[(int) blob.length()];
        int len = bytes.length;
        int offest = 0;
        int read = 0;
        while (offest < len && (read = bis.read(bytes, offest, len - offest)) > 0) {
            offest += read;
        }
        return new String(bytes);

    }
    
    public static String CLOBtoString(oracle.sql.CLOB clob) throws SQLException, IOException {
        if (clob == null) {
            return "";
        }
        char[] buffer = null;
        buffer = new char[(int) clob.length()];
        clob.getCharacterStream().read(buffer);
        return String.valueOf(buffer).replaceAll("\r\n", "\n").replaceAll("\n", "");

    }

    public static String CLOBtoString2(Clob clob) throws SQLException, IOException {
        if (clob == null) {
            return "";
        }
        
        String  result = clob.getSubString((long)1,(int)clob.length());
        return result;
    }
    
	public String getFilePath(String index4Str, String path0) {
        URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
        String path = url.toString();
        int index = path.indexOf(index4Str);
        if (index == -1) {
            index = path.indexOf("WEB-INF");
        }

        if (index == -1) {
            index = path.indexOf("bin");
        }
        
        if(index == -1){
        	path="";
        }else{
        	path = path.substring(0, index);
        }

        if (path.startsWith("zip")) {
            path = path.substring(4);
        } else if (path.startsWith("file")) {
            path = path.substring(5);
        } else if (path.startsWith("jar")) {
            path = path.substring(9);
        }
        try {
            path = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            StringManagerUtils.printLogFile(logger, "error", e, "error");;
        }
        path = path + path0 + index4Str;
        return path;
    }
	
	public static String readFile(String path, String encode) {
        String fileContent = "";
        try {
            File f = new File(path);
            if(!f.exists()){
            	if(path.startsWith("/")){
            		path=path.substring(1);
    			}else{
    				path="/"+path;
    			}
            	f = new File(path);
            }
            if (f.isFile() && f.exists()) {
                FileInputStream fs = new FileInputStream(f);
                InputStreamReader read = new InputStreamReader(fs, encode);
                BufferedReader reader = new BufferedReader(read);
                String line;
                while ((line = reader.readLine()) != null) {
                    fileContent += line;
                }
                fs.close();
                read.close();
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            StringManagerUtils.printLogFile(logger, "error", e, "error");;
        }
        return fileContent;
    }
	
	// 把json格式的字符串写到文件
    public static boolean writeFile(String filePath, String sets) {
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter writer = null;
        Boolean result = false;
        try {
            fileOutputStream = new FileOutputStream(filePath, false);
            writer = new OutputStreamWriter(fileOutputStream, "UTF-8");
            writer.write(sets);
            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            StringManagerUtils.printLogFile(logger, "error", e, "error");;
            result = false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            StringManagerUtils.printLogFile(logger, "error", e, "error");;
            result = false;
        } catch (IOException e) {
            e.printStackTrace();
            StringManagerUtils.printLogFile(logger, "error", e, "error");;
            result = false;
        } finally {
            try {
                writer.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                StringManagerUtils.printLogFile(logger, "error", e, "error");;
            }
        }

        return result;
    }
    
    /**
     * 向文件中写入内容
     * @param filepath 文件路径与名称
     * @param newstr  写入的内容
     * @return
     * @throws IOException
     */
    public static boolean writeFileContent(String filepath, String newstr) throws IOException {
        Boolean bool = false;
        String filein = newstr + "\r\n"; //新写入的行，换行
        String temp = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
            File file = new File(filepath); //文件路径(包括文件名称)
            //将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();

            //文件原有内容
            for (int i = 0;
                (temp = br.readLine()) != null; i++) {
                buffer.append(temp);
                // 行与行之间的分隔符 相当于"\n"
                buffer = buffer.append(System.getProperty("line.separator"));
            }
            buffer.append(filein);

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buffer.toString().toCharArray());
            pw.flush();
            bool = true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            StringManagerUtils.printLogFile(logger, "error", e, "error");;
        } finally {
            //不要忘记关闭
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return bool;
    }
    
    public static String toPrettyFormat(String json) {
        if (!StringManagerUtils.isNotNull(json)) {
            return json;
        }
        if (json.indexOf("'") != -1) {
            json = json.replaceAll("'", "\\'");
        }
        if (json.indexOf("\"") != -1) {
            json = json.replaceAll("\"", "\\\"");
        }

        if (json.indexOf("\r\n") != -1) { 
            json = json.replaceAll("\r\n", "\\u000d\\u000a");
        }
        if (json.indexOf("\n") != -1) {
            json = json.replaceAll("\n", "\\u000a");
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonObject);
    }
    public static String jsonStringFormat(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(json);
        String prettyJsonString = gson.toJson(je);
        return prettyJsonString;
    }
    
    public static File createJsonFile(String jsonString, String filePath) {
        boolean flag = true;

        String fullPath = filePath;
        File file = null;
        try {
            file = new File(fullPath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            if (jsonString.indexOf("'") != -1) {
                jsonString = jsonString.replaceAll("'", "\\'");
            }
            if (jsonString.indexOf("\"") != -1) {
                jsonString = jsonString.replaceAll("\"", "\\\"");
            }

            if (jsonString.indexOf("\r\n") != -1) {
                jsonString = jsonString.replaceAll("\r\n", "\\u000d\\u000a");
            }
            if (jsonString.indexOf("\n") != -1) {
                jsonString = jsonString.replaceAll("\n", "\\u000a");
            }

            // 格式化json字符串
            jsonString = toPrettyFormat(jsonString);

            // 将格式化后的字符串写入文件
            FileOutputStream fos = new FileOutputStream(file);
            Writer write = new OutputStreamWriter(fos, "UTF-8");
            write.write(jsonString);
            write.flush();
            fos.close();
            write.close();
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
            StringManagerUtils.printLogFile(logger, "error", e, "error");;
        }

        // 返回是否成功的标记
        return file;
    }
    
    public static String sendPostMethod(String url, String param, String encoding, int connectTimeout, int readTimeout) {
        PrintWriter out = null;
        BufferedReader in = null;
        HttpURLConnection conn = null;
        OutputStreamWriter os = null;
        InputStreamReader is = null;
        String result = "";
        if (!StringManagerUtils.isNotNull(encoding)) {
            encoding = "utf-8";
        }
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //超时设置
            if (connectTimeout > 0) {
                conn.setConnectTimeout(1000 * connectTimeout); //连接主机超时设置
            }
            if (readTimeout > 0) {
                conn.setReadTimeout(1000 * readTimeout); //从主机读取数据超时设置
            }

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            os = new OutputStreamWriter(conn.getOutputStream(), encoding);
            out = new PrintWriter(os);
            out.print(param);
            out.flush();

            // 定义BufferedReader输入流来读取URL的响应
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                is = new InputStreamReader(conn.getInputStream(), encoding); in = new BufferedReader(is);
                String line;
                while ((line = in .readLine()) != null) {
                    result += line;
                }
                return result;
            } else if (conn.getResponseCode() >= 400) {
                String errorInfo = "";
                is = new InputStreamReader(conn.getInputStream(), encoding); in = new BufferedReader(is);
                String line;
                while ((line = in .readLine()) != null) {
                    errorInfo += line;
                }
                System.out.println("错误信息：" + errorInfo);
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
        	printLog("发送 POST 请求出现异常！" + e);
        	printLog("url:" + url + ",param:" + param);
            e.printStackTrace();
            StringManagerUtils.printLogFile(logger, "url:" + url + ",param:" + param,"info");
            StringManagerUtils.printLogFile(logger, "发送 POST 请求出现异常！", e, "error");;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
                if (out != null) {
                    out.close();
                }
                if ( in != null) {
                    in .close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                logger.error("error", ex);
            }
        }
        return "";
    }
    
    public static long getTimeDifference(String fromDateStr, String toDateStr, String format) {
        long result = 0;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            long from = 0;
            if (StringManagerUtils.isNotNull(fromDateStr)) {
                Date fromDate = simpleDateFormat.parse(fromDateStr);
                from = fromDate.getTime();
            }
            Date toDate = simpleDateFormat.parse(toDateStr);
            long to = toDate.getTime();
            result = to - from;
        } catch (ParseException e) {
        	StringManagerUtils.printLogFile(logger, "error", e, "error");;
            e.printStackTrace();
            result = 0;
        }
        return result;
    }
    
    public static void printLog(String x) {
        if(Config.getInstance().configFile.getLog().getStdout()){
        	System.out.println(StringManagerUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss") + ":" + x);
        }
    }
    
    public static void printLogFile(org.apache.log4j.Logger logger,String x,String level){
    	if(logger!=null && Config.getInstance().configFile.getLog().getFile()){
    		if("debug".equalsIgnoreCase(level)){
    			logger.debug(x);
    		}else if("warn".equalsIgnoreCase(level)){
    			logger.warn(x);
    		}else if("error".equalsIgnoreCase(level)){
    			logger.error(x);
    		}else if("fatal".equalsIgnoreCase(level)){
    			logger.fatal(x);
    		}else{
    			logger.info(x);
    		}
        }
    }
    
    public static void printLogFile(org.apache.log4j.Logger logger, Object message, Throwable t,String level){
    	if(logger!=null && Config.getInstance().configFile.getLog().getFile()){
    		if("error".equalsIgnoreCase(level)){
    			logger.error(message,t);
    		}else{
    			logger.info(message);
    		}
    	}
    }
    
    public static void printLogFile(org.slf4j.Logger logger,String x,String level){
    	if(logger!=null && Config.getInstance().configFile.getLog().getFile()){
    		if("debug".equalsIgnoreCase(level)){
    			logger.debug(x);
    		}else if("warn".equalsIgnoreCase(level)){
    			logger.warn(x);
    		}else if("error".equalsIgnoreCase(level)){
    			logger.error(x);
    		}else{
    			logger.info(x);
    		}
        }
    }
    
    public static void printLogFile(org.slf4j.Logger logger, String message, Throwable t,String level){
    	if(logger!=null && Config.getInstance().configFile.getLog().getFile()){
    		if("error".equalsIgnoreCase(level)){
    			logger.error(message, t);;
    		}else{
    			logger.info(message);
    		}
    	}
    }
    
    public static String join(Object objarr[], String sign) {
        StringBuffer result = new StringBuffer();
        int length = objarr != null ? objarr.length : 0;
        for (int i = 0; objarr != null && i < length; i++) {
            result.append(objarr[i] + "");
            if (i < length - 1) {
                result.append(sign);
            }
        }
        return result.toString();
    }
    
    public static String join(long objarr[], String sign) {
        StringBuffer result = new StringBuffer();
        int length = objarr != null ? objarr.length : 0;
        for (int i = 0; objarr != null && i < length; i++) {
            result.append(objarr[i] + "");
            if (i < length - 1) {
                result.append(sign);
            }
        }
        return result.toString();
    }

    public static String join(List < Object > objarr, String sign) {
        StringBuffer result = new StringBuffer();
        int length = objarr != null ? objarr.size() : 0;
        for (int i = 0; objarr != null && i < length; i++) {
            result.append(objarr.get(i) + "");
            if (i < length - 1) {
                result.append(sign);
            }
        }
        return result.toString();
    }
    
    public static String join_long(List < Long > objarr, String sign) {
        StringBuffer result = new StringBuffer();
        int length = objarr != null ? objarr.size() : 0;
        for (int i = 0; objarr != null && i < length; i++) {
            result.append(objarr.get(i) + "");
            if (i < length - 1) {
                result.append(sign);
            }
        }
        return result.toString();
    }

    public static String joinStringArr(String objarr[], String sign) {
        StringBuffer result = new StringBuffer();
        int length = objarr != null ? objarr.length : 0;
        for (int i = 0; objarr != null && i < length; i++) {
            result.append("\"" + objarr[i] + "\"");
            if (i < length - 1) {
                result.append(sign);
            }
        }
        return result.toString();
    }

    public static String joinStringArr(List < String > objarr, String sign) {
        StringBuffer result = new StringBuffer();
        int length = objarr != null ? objarr.size() : 0;
        for (int i = 0; objarr != null && i < length; i++) {
            result.append("\"" + objarr.get(i) + "\"");
            if (i < length - 1) {
                result.append(sign);
            }
        }
        return result.toString();
    }

    public static String joinStringArr2(List < String > objarr, String sign) {
        StringBuffer result = new StringBuffer();
        int length = objarr != null ? objarr.size() : 0;
        for (int i = 0; objarr != null && i < length; i++) {
            result.append("'" + objarr.get(i) + "'");
            if (i < length - 1) {
                result.append(sign);
            }
        }
        return result.toString();
    }
    
    public static String joinStringArr2(String[] objarr, String sign) {
        StringBuffer result = new StringBuffer();
        int length = objarr != null ? objarr.length : 0;
        for (int i = 0; objarr != null && i < length; i++) {
            result.append("'" + objarr[i] + "'");
            if (i < length - 1) {
                result.append(sign);
            }
        }
        return result.toString();
    }
    
    public static boolean existOrNot(String data[], String key) {
        boolean flag = false;
        if(data!=null){
        	for (String d: data) {
                if (d.equalsIgnoreCase(key)) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    public static boolean existOrNot(String data[], String key, boolean caseSensitive) {
        boolean flag = false;
        if(data!=null){
        	for (int i = 0; i < data.length; i++) {
                boolean match = false;
                if (caseSensitive) {
                    match = data[i].equals(key);
                } else {
                    match = data[i].equalsIgnoreCase(key);
                }
                if (match) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }
    
    public static boolean existOrNot_long(List < Long > list, long value) {
        boolean flag = false;
        if(list!=null){
        	for (int i = 0; i < list.size(); i++) {
                if (list.get(i)==value) {
                	flag=true;
                	break;
                }
            }
        }
        return flag;
    }

    public static boolean existOrNot(List < String > list, String key, boolean caseSensitive) {
        boolean flag = false;
        if(list!=null){
        	for (int i = 0; i < list.size(); i++) {
                boolean match = false;
                if (caseSensitive) {
                    match = list.get(i).equals(key);
                } else {
                    match = list.get(i).equalsIgnoreCase(key);
                }
                if (match) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }
}
