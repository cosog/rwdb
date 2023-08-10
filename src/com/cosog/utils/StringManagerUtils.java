package com.cosog.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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

import oracle.sql.CLOB;

public class StringManagerUtils {
	private final static String DATEPATTERN = "yyyy-MM-dd";
	
	public static boolean isNotNull(String value) {
        boolean flag = false;
        //if (value != "" || value != null || StringUtils.isNotBlank(value)) {
        if (value != null && value.trim().length() > 0 && (!"".equals(value.trim())) && !"null".equalsIgnoreCase(value)) {
            flag = true;
        }
        return flag;
    }

    public static boolean isNum(String str) {
        return str != null && str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }
    
    /**
     * 判断字符串是否是数字
     */
    public static boolean isNumber(String value) {
        return isInteger(value) || isDouble(value);
    }
    
    /**
     * 判断字符串是否是数字
     */
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是数字
     */
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
            // TODO Auto-generated catch block
            e.printStackTrace();
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
        // Calendar cal = Calendar.getInstance();
        // DateFormat df = DateFormat.getDateTimeInstance();
        SimpleDateFormat f = new SimpleDateFormat(DATEPATTERN);
        // DateFormat df = DateFormat.getDateInstance();
        time = f.format(now);
        return time;
    }

    public static String getCurrentTime(String format) {
        String time = null;
        Date now = new Date();
        // Calendar cal = Calendar.getInstance();
        // DateFormat df = DateFormat.getDateTimeInstance();
        SimpleDateFormat f = new SimpleDateFormat(format);
        // DateFormat df = DateFormat.getDateInstance();
        time = f.format(now);
        return time;
    }
    
  //把数据库中blob类型转换成String类型
    public static String convertBlobToString(Blob blob) {
        String result = "";
        try {
            ByteArrayInputStream msgContent = (ByteArrayInputStream) blob.getBinaryStream();
            byte[] byte_data = new byte[msgContent.available()];
            msgContent.read(byte_data, 0, byte_data.length);
            result = new String(byte_data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String convertBLOBtoString(Blob BlobContent) {
        byte[] msgContent = null;
        try {
            msgContent = BlobContent.getBytes(1, (int) BlobContent.length());
        } catch (SQLException e1) {
            e1.printStackTrace();
        } // BLOB转换为字节数组
        String newStr = ""; // 返回字符串
        long BlobLength; // BLOB字段长度
        try {
            BlobLength = BlobContent.length(); // 获取BLOB长度
            if (msgContent == null || BlobLength == 0) // 如果为空，返回空值
            {
                return "";
            } else // 处理BLOB为字符串
            {
                newStr = new String(BlobContent.getBytes(1, msgContent.length), "gb2312"); // 简化处理，只取前900字节
                return newStr;
            }
        } catch (Exception e) // oracle异常捕获
        {
            e.printStackTrace();
        }
        return newStr;
    }

    //将字符串转为指定小数点位数的浮点型
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

    //BLOB转字符串	
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
    //CLOB转字符串
    public static String CLOBtoString(oracle.sql.CLOB clob) throws SQLException, IOException {
        if (clob == null) {
            return "";
        }
        char[] buffer = null;
        buffer = new char[(int) clob.length()];
        clob.getCharacterStream().read(buffer);
        return String.valueOf(buffer).replaceAll("\r\n", "\n").replaceAll("\n", "");

    }

    //Clob转字符串
    public static String CLOBtoString2(Clob clob) throws SQLException, IOException {
        if (clob == null) {
            return "";
        }
//        BufferedReader reader = null;
//        InputStreamReader is = new InputStreamReader(clob.getAsciiStream());
//        reader = new BufferedReader(is);
//        String result = "";
//        String line = "";
//        while ((line = reader.readLine()) != null) {
//            result += line;
//        }
//        is.close();
//        reader.close();
        
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

        path = path.substring(0, index);
        if (path.startsWith("zip")) { // 当class文件在war中时，此时返回zip:D:/...这样的路径
            path = path.substring(4);
        } else if (path.startsWith("file")) { // 当class文件在class文件中时，此时返回file:/D:/...这样的路径
            path = path.substring(5);
        } else if (path.startsWith("jar")) { // 当class文件在jar文件里面时，此时返回jar:file:/D:/...这样的路径
            path = path.substring(9);
        }
        try {
            path = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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
        }
        return fileContent; //.replaceAll(" ", "");
    }
	
	/**
     * 通过URLConnect的方式发送post请求，并返回响应结果
     * 
     * @param url
     *            请求地址
     * @param params
     *            参数列表，例如name=小明&age=8里面的中文要经过Uri.encode编码
     * @param encoding
     *            编码格式
     * @return 服务器响应结果
     */
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
            System.out.println("发送 POST 请求出现异常！" + e);
            System.out.println("url:" + url + ",param:" + param);
            e.printStackTrace();
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
            }
        }
        return "";
    }
}
