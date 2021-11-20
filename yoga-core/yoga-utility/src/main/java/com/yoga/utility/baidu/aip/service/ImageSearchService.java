package com.yoga.utility.baidu.aip.service;

import com.baidu.aip.imagesearch.AipImageSearch;
import com.yoga.core.exception.BusinessException;
import com.yoga.setting.service.SettingService;
import com.yoga.utility.baidu.aip.ao.BaiduAiqConfig;
import com.yoga.utility.baidu.aip.ao.SimilarImage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ImageSearchService {

    public final static String ModuleName = "api_baidu_aip";
    public final static String AiqConfig = "aip_config";


    @Value("${yoga.baidu.aip.global:false}")
    private Boolean globalConfig;
    @Value("${yoga.baidu.aip.appId:24930814}")
    private String appId;
    @Value("${yoga.baidu.aip.apiKey:CgBXQHBKl0P9kG965KBLQ2Ga}")
    private String apiKey;
    @Value("${yoga.baidu.aip.secretKey:Ru8bPXAIDnXPX79jRp8BPaomwYQMEckX}")
    private String secretKey;

    @Autowired
    private SettingService settingService;

    private AipImageSearch globalImageSearch = null;
    private Map<Long, AipImageSearch> imageSearchs = new HashMap<>();
    private Map<Long, Long> lastTryCreate = new HashMap<>();

    public BaiduAiqConfig readConfig(long tenantId) {
        return settingService.get(tenantId, ModuleName, AiqConfig, BaiduAiqConfig.class);
    }
    public void saveConfig(long tenantId, BaiduAiqConfig config) {
        settingService.save(tenantId, ModuleName, AiqConfig, config.toString(), "" + config.getAppId() + "/" + config.getApiKey());
        imageSearchs.remove(tenantId);
    }

    public void addSimilar(long tenantId, byte[] image, String brief, String[] tags) {
        AipImageSearch api = getApi(tenantId);
        if (api == null) throw new BusinessException("尚未设置图像搜索！");
        HashMap<String, String> options = new HashMap<>();
        if (tags != null && tags.length > 0) options.put("tags", Arrays.stream(tags).collect(Collectors.joining(",")));
        JSONObject result = api.similarAdd(image, brief, options);
        int code = result.optInt("error_code", 0);
        String message = result.optString("error_msg");
        if (code != 0) throw new BusinessException(message == null ? "调用接口错误！" : message);
    }
    public void addSimilar(long tenantId, byte[] image, String brief, int imageGroup) {
        AipImageSearch api = getApi(tenantId);
        if (api == null) throw new BusinessException("尚未设置图像搜索！");
        HashMap<String, String> options = new HashMap<String, String>() {{
            put("tags", String.valueOf(imageGroup));
        }};
        JSONObject result = api.similarAdd(image, brief, options);
        int code = result.optInt("error_code", 0);
        String message = result.optString("error_msg");
        if (code != 0) throw new BusinessException(message == null ? "调用接口错误！" : message);
    }
    public List<SimilarImage> searchSimilar(long tenantId, byte[] image, String[] tags, int resultLimit) {
        AipImageSearch api = getApi(tenantId);
        if (api == null) throw new BusinessException("尚未设置图像搜索！");
        HashMap<String, String> options = new HashMap<>();
        if (tags != null && tags.length > 0) {
            options.put("tags", Arrays.stream(tags).collect(Collectors.joining(",")));
            options.put("tag_logic", "or");
        }
        if (resultLimit > 0) {
            options.put("rn", String.valueOf(resultLimit));
        }
        JSONObject result = api.similarSearch(image, options);
        int code = result.optInt("error_code", 0);
        String message = result.optString("error_msg");
        if (code != 0) throw new BusinessException(message == null ? "调用接口错误！" : message);
        JSONArray array = result.getJSONArray("result");
        List<SimilarImage> images = new ArrayList<>();
        for (int i = 0; i < array.length() && i < resultLimit; i++) {
            JSONObject item = array.getJSONObject(i);
            String brief = item.optString("brief");
            double score = item.optDouble("score", 0.0);
            images.add(new SimilarImage(brief, score));
        }
        return images;
    }
    public List<SimilarImage> searchSimilar(long tenantId, byte[] image, int imageGroup, int resultLimit) {
        AipImageSearch api = getApi(tenantId);
        if (api == null) throw new BusinessException("尚未设置图像搜索！");
        HashMap<String, String> options = new HashMap<String, String>() {{
            put("tags", String.valueOf(imageGroup));
        }};
        if (resultLimit > 0) {
            options.put("rn", String.valueOf(resultLimit));
        }
        JSONObject result = api.similarSearch(image, options);
        int code = result.optInt("error_code", 0);
        String message = result.optString("error_msg");
        if (code != 0) throw new BusinessException(message == null ? "调用接口错误！" : message);
        JSONArray array = result.getJSONArray("result");
        List<SimilarImage> images = new ArrayList<>();
        for (int i = 0; i < array.length() && i < resultLimit; i++) {
            JSONObject item = array.getJSONObject(i);
            double score = item.optDouble("score", 0.0);
            String brief = item.optString("brief");
            images.add(new SimilarImage(brief, score));
        }
        return images;
    }
    public SimilarImage searchMostSimilar(long tenantId, byte[] image, String[] tags, Double floorScore, boolean allowNull) {
        List<SimilarImage> images = searchSimilar(tenantId, image, tags, 1);
        if (images.isEmpty() && !allowNull) throw new BusinessException("未找到相似图片！");
        else if (images.isEmpty()) return null;
        else if (floorScore != null && images.get(0).getScore() < floorScore && !allowNull) throw new BusinessException("图片相似度不足！");
        else if (floorScore != null && images.get(0).getScore() < floorScore) return null;
        else return images.get(0);
    }
    public SimilarImage searchMostSimilar(long tenantId, byte[] image, int imageGroup, Double floorScore, boolean allowNull) {
        List<SimilarImage> images = searchSimilar(tenantId, image, imageGroup, 1);
        if (images.isEmpty() && !allowNull) throw new BusinessException("未找到相似图片！");
        else if (images.isEmpty()) return null;
        else if (floorScore != null && images.get(0).getScore() < floorScore && !allowNull) throw new BusinessException("图片相似度不足！");
        else if (floorScore != null && images.get(0).getScore() < floorScore) return null;
        else return images.get(0);
    }
    public void deleteSimilar(long tenantId, byte[] image, String[] tags) {
        AipImageSearch api = getApi(tenantId);
        if (api == null) throw new BusinessException("尚未设置图像搜索！");
        HashMap<String, String> options = new HashMap<>();
        if (tags != null && tags.length > 0) {
            options.put("tags", Arrays.stream(tags).collect(Collectors.joining(",")));
            options.put("tag_logic", "or");
        }
        JSONObject result = api.similarDeleteByImage(image, options);
        int code = result.optInt("error_code", 0);
        String message = result.optString("error_msg");
        if (code != 0) throw new BusinessException(message == null ? "调用接口错误！" : message);
    }
    public void deleteSimilar(long tenantId, byte[] image, int imageGroup) {
        AipImageSearch api = getApi(tenantId);
        if (api == null) throw new BusinessException("尚未设置图像搜索！");
        HashMap<String, String> options = new HashMap<String, String>() {{
            put("tags", String.valueOf(imageGroup));
        }};
        JSONObject result = api.similarDeleteByImage(image, options);
        int code = result.optInt("error_code", 0);
        String message = result.optString("error_msg");
        if (code != 0) throw new BusinessException(message == null ? "调用接口错误！" : message);
    }

    private synchronized AipImageSearch getApi(long tenantId) {
        if (globalConfig && globalImageSearch != null) {
            return globalImageSearch;
        } else if (globalConfig) {
            globalImageSearch = new AipImageSearch(appId, apiKey, secretKey);
            return globalImageSearch;
        }
        AipImageSearch api = imageSearchs.get(tenantId);
        if (api != null) return api;
        Long latest = lastTryCreate.get(tenantId);
        if (latest != null && System.currentTimeMillis() - latest < 10_000) return null;
        lastTryCreate.put(tenantId, System.currentTimeMillis());
        BaiduAiqConfig config = readConfig(tenantId);
        if (config == null) return null;
        api = new AipImageSearch(config.getAppId(), config.getApiKey(), config.getSecretKey());
        imageSearchs.put(tenantId, api);
        return api;
    }
}
