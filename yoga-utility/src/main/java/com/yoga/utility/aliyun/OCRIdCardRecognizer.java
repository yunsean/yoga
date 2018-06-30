package com.yoga.utility.aliyun;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.utils.StrUtil;
import com.yoga.tenant.setting.Settable;
import com.yoga.tenant.setting.service.SettingService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Service
@Settable(module = OCRIdCardRecognizer.ModuleName, key = OCRIdCardRecognizer.IDCardRecognizeAppId, name = "身份证识别App Code", type = String.class)
public class OCRIdCardRecognizer {

    @Autowired
    private SettingService settingService;

    public final static String ModuleName = "api_aliyun_idcard";
    public final static String IDCardRecognizeAppId = "aliyun.idcard.appid";

    public OCRIdCardResult recognize(long tenantId, String url) {
        String apiServer = "http://dm-51.data.aliyun.com/rest/160601/ocr/ocr_idcard.json";
        String appcode = settingService.get(tenantId, ModuleName, IDCardRecognizeAppId, String.class);
        if (StrUtil.isBlank(appcode)) throw new BusinessException("尚未配置身份证识别APPCode！");
        JSONObject image = new JSONObject();
        image.put("dataType", 50);
        image.put("dataValue", encodeBase64Url(url));
        JSONObject configure = new JSONObject();
        configure.put("dataType", 50);
        JSONObject side = new JSONObject();
        side.put("side", "face");
        configure.put("dataValue", side.toString());
        JSONObject input = new JSONObject();
        input.put("image", image);
        input.put("configure", configure);
        JSONArray inputs = new JSONArray();
        inputs.add(input);
        JSONObject root = new JSONObject();
        root.put("inputs", inputs);
        try {
            String json = root.toJSONString();
            StringEntity entity = new StringEntity(json);
            CloseableHttpClient httpClient = createHttpsClient();
            HttpPost httpPost = new HttpPost(apiServer);
            httpPost.setEntity(entity);
            httpPost.setHeader("Authorization", "APPCODE " + appcode);
            httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
            httpPost.setHeader("Expect", "100-continue");
            HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode < 200 || statusCode > 299) throw new BusinessException("身份证识别错误！");
            String response = entity2String(httpResponse.getEntity());
            httpClient.close();
            httpPost.releaseConnection();
            JSONObject jsonObject = JSONObject.parseObject(response);
            JSONArray outputs = jsonObject.getJSONArray("outputs");
            String output = outputs.getJSONObject(0).getJSONObject("outputValue").getString("dataValue");
            JSONObject out = JSONObject.parseObject(output);
            if (!out.getBoolean("success")) throw new BusinessException("身份证识别失败！");
            String address = out.getString("address");
            String name = out.getString("name");
            String nationality = out.getString("nationality");
            String num = out.getString("num");
            String sex = out.getString("sex");
            String birth = out.getString("birth");
            OCRIdCardResult result = new OCRIdCardResult(num, sex, birth, nationality, name, address);
            return result;
        } catch (BusinessException e){
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("识别身份证信息错误！");
        }
    }

    private static CloseableHttpClient createHttpsClient()
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(null, (chain, authType) -> true).build();
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext);
        return HttpClients.custom()
                .setUserAgent("curl/7.53.1")
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .build();
    }
    private static String entity2String(HttpEntity entity) {
        StringBuilder content = new StringBuilder();
        try (InputStream inputStream = entity.getContent();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public static String encodeBase64Url(String path) {
        try {
            File file = new File(path);
            if (file.exists()) return encodeBase64File(path);
            byte[] buffer = getImageFromNetByUrl(path);
            return new BASE64Encoder().encode(buffer);
        } catch (Exception ex) {
            throw new RuntimeException("读取图片文件失败！");
        }
    }
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return new BASE64Encoder().encode(buffer);
    }
    public static byte[] getImageFromNetByUrl(String strUrl) throws Exception {
        URL url = new URL(strUrl);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5 * 1000);
        InputStream inStream = conn.getInputStream();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len = inStream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }
}
