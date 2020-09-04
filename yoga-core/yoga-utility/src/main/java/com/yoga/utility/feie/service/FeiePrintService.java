package com.yoga.utility.feie.service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.utils.DateUtil;
import com.yoga.core.utils.StringUtil;
import com.yoga.setting.service.SettingService;
import com.yoga.utility.feie.dto.AddPrinterResult;
import com.yoga.utility.feie.dto.FeiePrintResult;
import com.yoga.utility.feie.dto.PrintStatusResult;
import com.yoga.utility.feie.dto.PrinterQueueResult;
import com.yoga.utility.feie.model.FeiePrinterConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FeiePrintService {

    @Autowired
    private SettingService settingService;
    @Value("${yoga.feie.user:yunsean@163.com}")
    private String feieUser;
    @Value("${yoga.feie.ukey:tau27QBDJYmhKIqW}")
    private String feieUkey;

    public final static String URL = "http://api.feieyun.cn/Api/Open/";
    public final static String ModuleName = "api_feie_printer";
    public final static String SinglePrinterConfig = "feie_single_printer";

    public FeiePrinterConfig readConfig(long tenantId) {
        FeiePrinterConfig config = settingService.get(tenantId, ModuleName, SinglePrinterConfig, FeiePrinterConfig.class);
        return config;
    }

    public void saveConfig(long tenantId, FeiePrinterConfig config) {
        settingService.save(tenantId, ModuleName, SinglePrinterConfig, config.toString(), "" + feieUser + "/" + config.getName());
    }

    public String print(long tenantId, String content) {
        FeiePrinterConfig config = settingService.get(tenantId, ModuleName, SinglePrinterConfig, FeiePrinterConfig.class);
        if (config == null) throw new BusinessException("尚未配置打印机参数！");
        Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isPermitted("api_feie_printer.print")) throw new BusinessException("无打印权限！");
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)
                .setConnectTimeout(30000)
                .build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("user", feieUser));
        String stime = String.valueOf(System.currentTimeMillis()/1000);
        nvps.add(new BasicNameValuePair("stime", stime));
        nvps.add(new BasicNameValuePair("sig",signature(feieUser, feieUkey, stime)));
        nvps.add(new BasicNameValuePair("apiname","Open_printMsg"));
        nvps.add(new BasicNameValuePair("sn", config.getSn()));
        nvps.add(new BasicNameValuePair("content", content));
        nvps.add(new BasicNameValuePair("times", "1"));
        CloseableHttpResponse response = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if(statecode == 200) {
                HttpEntity httpentity = response.getEntity();
                if (httpentity == null) throw new BusinessException("飞鹅服务器未正确返回");
                String body = EntityUtils.toString(httpentity);
                FeiePrintResult<String> result = new Gson().fromJson(body, new TypeToken<FeiePrintResult<String>>(){}.getType());
                if (result.getRet() != 0 && StringUtil.isNotEmpty(result.getMsg())) throw new BusinessException(result.getMsg());
                else if (result.getRet() != 0) throw new BusinessException("飞鹅服务器返回" + result.getRet());
                return result.getData();
            } else {
                throw new BusinessException(response.getStatusLine().getReasonPhrase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (StringUtil.isBlank(e.getMessage())) throw new BusinessException("访问飞鹅服务器错误");
            else throw new BusinessException(e.getMessage());
        }
        finally{
            try {
                if(response!=null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isCompleted(long tenantId, String orderId) {
        FeiePrinterConfig config = settingService.get(tenantId, ModuleName, SinglePrinterConfig, FeiePrinterConfig.class);
        if (config == null) throw new BusinessException("尚未配置打印机参数！");
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)
                .setConnectTimeout(30000)
                .build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("user", feieUser));
        String stime = String.valueOf(System.currentTimeMillis() / 1000);
        nvps.add(new BasicNameValuePair("stime", stime));
        nvps.add(new BasicNameValuePair("sig", signature(feieUser, feieUkey, stime)));
        nvps.add(new BasicNameValuePair("apiname","Open_queryOrderState"));
        nvps.add(new BasicNameValuePair("orderid", orderId));
        CloseableHttpResponse response = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if(statecode == 200) {
                HttpEntity httpentity = response.getEntity();
                if (httpentity == null) throw new BusinessException("飞鹅服务器未正确返回");
                String body = EntityUtils.toString(httpentity);
                PrintStatusResult result = JSONObject.parseObject(body, PrintStatusResult.class);
                if (result.getRet() != 0 && StringUtil.isNotEmpty(result.getMsg())) throw new BusinessException(result.getMsg());
                else if (result.getRet() != 0) throw new BusinessException("飞鹅服务器返回" + result.getRet());
                return result.isData();
            } else {
                throw new BusinessException(response.getStatusLine().getReasonPhrase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (StringUtil.isBlank(e.getMessage())) throw new BusinessException("访问飞鹅服务器错误");
            else throw new BusinessException(e.getMessage());
        }
        finally {
            try {
                if(response!=null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void cleanQueue(long tenantId) {
        FeiePrinterConfig config = settingService.get(tenantId, ModuleName, SinglePrinterConfig, FeiePrinterConfig.class);
        if (config == null) throw new BusinessException("尚未配置打印机参数！");
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)
                .setConnectTimeout(30000)
                .build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("user", feieUser));
        String stime = String.valueOf(System.currentTimeMillis() / 1000);
        nvps.add(new BasicNameValuePair("stime", stime));
        nvps.add(new BasicNameValuePair("sig", signature(feieUser, feieUkey, stime)));
        nvps.add(new BasicNameValuePair("apiname","Open_delPrinterSqs"));
        nvps.add(new BasicNameValuePair("sn", config.getSn()));
        CloseableHttpResponse response = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if(statecode == 200) {
                HttpEntity httpentity = response.getEntity();
                if (httpentity == null) throw new BusinessException("飞鹅服务器未正确返回");
                String body = EntityUtils.toString(httpentity);
                PrintStatusResult result = JSONObject.parseObject(body, PrintStatusResult.class);
                if (result.getRet() != 0 && StringUtil.isNotEmpty(result.getMsg())) throw new BusinessException(result.getMsg());
                else if (result.getRet() != 0) throw new BusinessException("飞鹅服务器返回" + result.getRet());
                if (!result.isData()) throw new BusinessException("执行失败");
            } else {
                throw new BusinessException(response.getStatusLine().getReasonPhrase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (StringUtil.isBlank(e.getMessage())) throw new BusinessException("访问飞鹅服务器错误");
            else throw new BusinessException(e.getMessage());
        }
        finally {
            try {
                if(response!=null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String printerStatus(long tenantId) {
        FeiePrinterConfig config = settingService.get(tenantId, ModuleName, SinglePrinterConfig, FeiePrinterConfig.class);
        if (config == null) throw new BusinessException("尚未配置打印机参数！");
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)
                .setConnectTimeout(30000)
                .build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("user", feieUser));
        String stime = String.valueOf(System.currentTimeMillis() / 1000);
        nvps.add(new BasicNameValuePair("stime", stime));
        nvps.add(new BasicNameValuePair("sig", signature(feieUser, feieUkey, stime)));
        nvps.add(new BasicNameValuePair("apiname","Open_queryPrinterStatus"));
        nvps.add(new BasicNameValuePair("sn", config.getSn()));
        CloseableHttpResponse response = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if(statecode == 200) {
                HttpEntity httpentity = response.getEntity();
                if (httpentity == null) throw new BusinessException("飞鹅服务器未正确返回");
                String body = EntityUtils.toString(httpentity);
                FeiePrintResult<String> result = new Gson().fromJson(body, new TypeToken<FeiePrintResult<String>>(){}.getType());
                if (result.getRet() != 0 && StringUtil.isNotEmpty(result.getMsg())) throw new BusinessException(result.getMsg());
                else if (result.getRet() != 0) throw new BusinessException("飞鹅服务器返回" + result.getRet());
                String status = result.getData();
                if (status != null) status = status.replace("。", "");
                return status;
            } else {
                throw new BusinessException(response.getStatusLine().getReasonPhrase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (StringUtil.isBlank(e.getMessage())) throw new BusinessException("访问飞鹅服务器错误");
            else throw new BusinessException(e.getMessage());
        }
        finally {
            try {
                if(response!=null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public PrinterQueueResult printerQueue(long tenantId) {
        FeiePrinterConfig config = settingService.get(tenantId, ModuleName, SinglePrinterConfig, FeiePrinterConfig.class);
        if (config == null) throw new BusinessException("尚未配置打印机参数！");
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)
                .setConnectTimeout(30000)
                .build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("user", feieUser));
        String stime = String.valueOf(System.currentTimeMillis() / 1000);
        nvps.add(new BasicNameValuePair("stime", stime));
        nvps.add(new BasicNameValuePair("sig", signature(feieUser, feieUkey, stime)));
        nvps.add(new BasicNameValuePair("apiname","Open_queryOrderInfoByDate"));
        nvps.add(new BasicNameValuePair("sn", config.getSn()));
        nvps.add(new BasicNameValuePair("date", DateUtil.format(new Date(), "yyyy-MM-dd")));
        CloseableHttpResponse response = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if(statecode == 200) {
                HttpEntity httpentity = response.getEntity();
                if (httpentity == null) throw new BusinessException("飞鹅服务器未正确返回");
                String body = EntityUtils.toString(httpentity);
                FeiePrintResult<PrinterQueueResult> result = new Gson().fromJson(body, new TypeToken<FeiePrintResult<PrinterQueueResult>>(){}.getType());
                if (result.getRet() != 0 && StringUtil.isNotEmpty(result.getMsg())) throw new BusinessException(result.getMsg());
                else if (result.getRet() != 0) throw new BusinessException("飞鹅服务器返回" + result.getRet());
                if (result.getData() == null) throw new BusinessException("飞鹅服务器未正确返回");
                return result.getData();
            } else {
                throw new BusinessException(response.getStatusLine().getReasonPhrase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (StringUtil.isBlank(e.getMessage())) throw new BusinessException("访问飞鹅服务器错误");
            else throw new BusinessException(e.getMessage());
        }
        finally {
            try {
                if(response!=null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addPrinter(String sn, String key, String name) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)
                .setConnectTimeout(30000)
                .build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
        HttpPost post = new HttpPost(URL);
        String snlist = sn + "#" + key + "#" + name + "#";
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("user", feieUser));
        String stime = String.valueOf(System.currentTimeMillis()/1000);
        nvps.add(new BasicNameValuePair("stime", stime));
        nvps.add(new BasicNameValuePair("sig",signature(feieUser, feieUkey, stime)));
        nvps.add(new BasicNameValuePair("apiname","Open_printerAddlist"));
        nvps.add(new BasicNameValuePair("printerContent", snlist));
        CloseableHttpResponse response = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if(statecode == 200){
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null) {
                    String body = EntityUtils.toString(httpentity);
                    FeiePrintResult<AddPrinterResult> result = new Gson().fromJson(body, new TypeToken<FeiePrintResult<AddPrinterResult>>(){}.getType());
                    if (result.getRet() != 0 && StringUtil.isNotEmpty(result.getMsg())) throw new BusinessException(result.getMsg());
                    else if (result.getRet() != 0) throw new BusinessException("飞鹅服务器返回" + result.getRet());
                    if (result.getData() != null && result.getData().getOk() != null && result.getData().getOk().stream().filter(text-> text.contains(sn)).count() > 0) return;
                    if (result.getData() != null && result.getData().getNo() != null && result.getData().getNo().stream().filter(text-> text.contains(sn) && text.contains("已被添加过")).count() > 0) return;
                    throw new BusinessException("飞鹅服务器返回失败");
                } else {
                    throw new BusinessException("飞鹅服务器未正常返回");
                }
            } else {
                throw new BusinessException(response.getStatusLine().getReasonPhrase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (StringUtil.isBlank(e.getMessage())) throw new BusinessException("访问飞鹅服务器错误");
            else throw new BusinessException(e.getMessage());
        } finally {
            try {
                if (response != null) response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String signature(String USER,String UKEY,String STIME){
        String s = DigestUtils.sha1Hex(USER+UKEY+STIME);
        return s;
    }


    public String getBarCode(String input) {
        if (input == null || "".equals(input)) return "";
        if(confirm(input)) return getDigitBarCode(input);
        else return getCharacterCode(input);
    }

    /**
     * 	 获取纯数字的条形码 输入字符串转换为条形码demo 最大支持28位纯数字的条形码（按58mm打印机标准）
     * 	26-28位数字条形码，在数字中不可以出现2个及以上连续的0存在
     * 	23-25位数字条形码，在数字中不可以出现3个及以上连续的0存在
     * 	21-22位数字条形码，在数字中不可以出现4个及以上连续的0存在
     * 	19-20位数字条形码，在数字中不可以出现6个及以上连续的0存在
     * 	17-18位数字条形码，在数字中不可以出现8个及以上连续的0存在
     * 	15-16位数字条形码，在数字中不可以出现10个及以上连续的0存在
     * 	[  	少于或等于14位数字的条形码，0的数量没有影响 	]
     */
    private String getDigitBarCode(String input) {
        byte[] codeB = { 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39 }; // 匹配字符集B
        byte[] codeC = { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F,
                0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F, 0x20,
                0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E, 0x2F, 0x30, 0x31,
                0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x3A, 0x3B, 0x3C, 0x3D, 0x3E, 0x3F, 0x40, 0x41, 0x42,
                0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4A, 0x4B, 0x4C, 0x4D, 0x4E, 0x4F, 0x50, 0x51, 0x52, 0x53,
                0x54, 0x55, 0x56, 0x57, 0x58, 0x59, 0x5A, 0x5B, 0x5C, 0x5D, 0x5E, 0x5F, 0x60, 0x61, 0x62, 0x63 }; // 匹配字符集C

        int length = input.length();
        byte[] b = new byte[100];

        for (int i = 0; i < b.length; i++) {
            b[i] = 0x0;
        }

        b[0] = 0x1b;
        b[1] = 0x64;
        b[2] = 0x02;

        // 数字显示在条码的位置
        b[3] = 0x1d;
        b[4] = 0x48;
        b[5] = 0x32; // 0x32打印条形码下的数字， 0x30不打印条形码下的数字

        // 条码高度
        b[6] = 0x1d;
        b[7] = 0x68;
        b[8] = 0x50; // 7F是最大的高度

        // 条码宽度
        b[9] = 0x1d;
        b[10] = 0x77;
        b[11] = 0x02; // 2-6

        b[12] = 0x1d;
        b[13] = 0x6b;
        b[14] = 0x49; // code128
        b[15] = (byte) (length + 2); // 得出条形码长度
        b[16] = 0x7b;
        b[17] = 0x42;

        if (length > 14) {
            b[17] = 0x43;
            int j = 0;
            int key = 18;
            int ss = length / 2;// 初始化数组长度
            String temp = "";
            int iindex = 0;
            for (int i = 0; i < ss; i++) {
                temp = input.substring(j, j + 2);

                iindex = Integer.valueOf(temp);
                j = j + 2;
                if (iindex == 0) {
                    // 判断前面的为字符集B,此时不需要转换字符集
                    if (b[key + i - 1] == 0x30 && b[key + i - 2] == 0x30) {
                        b[key + i] = codeB[0];
                        b[key + i + 1] = codeB[0];
                        key += 1;
                    } else {
                        // 判断条形码开头前两位数都为0时转换字符集B
                        if (b[key + i - 1] == 0x43 && b[key + i - 2] == 0x7B) {
                            b[key + i - 2] = 0x7B;
                            b[key + i - 1] = 0x42;
                            b[key + i] = codeB[0];
                            b[key + i + 1] = codeB[0];
                            key += 1;
                        } else {
                            b[key + i] = 0x7B;
                            b[key + i + 1] = 0x42;
                            b[key + i + 2] = codeB[0];
                            b[key + i + 3] = codeB[0];
                            key += 3;
                        }

                    }
                } else {
                    // 判断前面的为字符集B,此时要转换字符集C
                    if (b[key + i - 1] == 0x30 && b[key + i - 2] == 0x30 && iindex != 89) {
                        b[key + i] = 0x7b;
                        b[key + i + 1] = 0x43;
                        b[key + i + 2] = codeC[iindex];
                        key += 2;
                    } else {
                        b[key + i] = codeC[iindex];
                    }

                }
            }
            int lastKey = getLastIndex(b);
            if (length % 2 > 0) {
                int lastnum = Integer.valueOf(input.substring(input.length() - 1)); // 取得字符串的最后一个数字
                // 判断前面的为字符集B,此时不需要转换字符集
                if (b[lastKey] == 0x30 && b[lastKey - 1] == 0x30) {
                    b[lastKey + 1] = codeB[lastnum];
                } else {
                    b[lastKey + 1] = 0x7b;
                    b[lastKey + 2] = 0x42;
                    b[lastKey + 3] = codeB[lastnum];
                }
            }
        } else { // 1-14位数字的条形码进来这个区间
            String temp = "";
            int iindex = 0;
            for (int i = 0; i < length; i++) {
                temp = input.substring(i, i + 1);
                iindex = Integer.valueOf(temp);
                b[18 + i] = codeB[iindex];
            }
        }

        // 得出条形码长度
        int blength = getLastIndex(b);

        // 得出条形码长度
        int len = (blength - 15);
        b[15] = (byte) (len);

        // (b, 0, len, "UTF-8"));
        String test = "";
        try {
            test = new String(b, 0, blength + 1, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(test);
        return test;
    }

    /**
     *	 获取特殊字符的条形码 输入字符串转换为条形码demo 最大支持14位特殊字符的条形码（按58mm打印机标准）
     *	character
     */
    private String getCharacterCode(String str) {
        int length = str.length();
        byte[] b = new byte[100];

        for (int i = 0; i < b.length; i++) {
            b[i] = 0x0;
        }

        b[0] = 0x1b;
        b[1] = 0x64;
        b[2] = 0x02;

        // 数字显示在条码的位置
        b[3] = 0x1d;
        b[4] = 0x48;
        b[5] = 0x32; // 0x32打印条形码下的数字， 0x30不打印条形码下的数字

        // 条码高度
        b[6] = 0x1d;
        b[7] = 0x68;
        b[8] = 0x50; // 7F是最大的高度

        // 条码宽度
        b[9] = 0x1d;
        b[10] = 0x77;
        b[11] = 0x02; // 2-6

        b[12] = 0x1d;
        b[13] = 0x6b;
        b[14] = 0x49; // code128
        b[15] = (byte) (length + 2); // 得出条形码长度
        b[16] = 0x7b;
        b[17] = 0x42;

        String str2 = strToHex(str);
        List<String> strList = strToList(str2);
        Boolean bn = judgeStr(strList);
        System.out.println("b=" + bn);
        int n = strList.size();
        if (bn) {
            for (int i = 0; i < n; i++) {
                b[18 + i] = Byte.parseByte(strList.get(i).replaceAll("0x", ""), 16);
            }
        } else {
            System.out.println("您输入的字符中含有非法撒字符!");
        }

        // 得出条形码长度
        int blength = getLastIndex(b);
        // 得出条形码长度
        int len = (blength - 15);
        b[15] = (byte) (len);
        // (b, 0, len, "UTF-8"));
        String test = "";
        try {
            test = new String(b, 0, blength + 1, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return test;
    }

    public Boolean confirm(String input) {
        int length = input.length();
        @SuppressWarnings("unused")
        int iindex = 0;
        String temp = "";
        for (int i = 0; i < length; i++) {
            temp = input.substring(i, i + 1);
            try {
                iindex = Integer.valueOf(temp);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    // 获取数组中最后一个不是0x0的元素的下标
    private int getLastIndex(byte[] b) {
        if (b == null || b.length == 0) {
            return 0;
        }
        int blength = 0;
        for (int i = b.length - 1; i >= 0; i--) {
            if (b[i] != 0x0) {
                blength = i;
                break;
            }
        }
        return blength;
    }

    public Boolean judgeStr(List<String> strList) {
        int count = 0;
        int size = strList.size();
        for (int i = 0; i < size; i++) {
            System.err.println(strList.get(i));
            if (getAllList().contains(strList.get(i))) {
                count++;
            } else {
                count = 0;
                System.err.println("您输入的 第  " + (i + 1) + " 个字符为无效字符");
                return false;
            }
        }
        return count == size;
    }

    public List<String> strToList(String str) {
        List<String> strList = new ArrayList<>();
        for (int i = 0; 4 * i < str.length(); i++) {
            strList.add(str.substring(4 * i, 4 * (i + 1)));
        }
        return strList;
    }

    public List<String> getAllList() {
        List<String> allList = new ArrayList<>();
        for (int i = 2; i <= 7; i++) {
            for (int j = 0; j <= 15; j++) {
                allList.add("0x" + i + Integer.toHexString(j).toUpperCase());
            }
        }
        return allList;
    }

    public String strToHex(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            sb.append("0x");
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString().trim();
    }

    public String hexToStr(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "utf-8");// UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }
}
