package com.yoga.utility.alidns.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.utils.NumberUtil;
import com.yoga.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

//https://help.aliyun.com/document_detail/29821.html
@Service
public class AliDnsService extends BaseService {

    static Logger logger = LoggerFactory.getLogger(AliDnsService.class);

    @Value("${yoga.aliyun.dns.access-key:}")
    private String accessKey;
    @Value("${yoga.aliyun.dns.access-secret:}")
    private String accessSecret;
    @Value("${yoga.aliyun.dns.default-domain:}")
    private String defaultDomain;
    private String regionId = "cn-hangzhou";    //必填固定值，必须为“cn-hanghou”
    private IAcsClient acsClient = null;


    @PostConstruct
    public void construct() {
        if (StringUtil.hasBlank(accessKey, accessSecret)) {
            logger.warn("尚未配置阿里云域名解析账户！");
        }
        try {
            IClientProfile profile = DefaultProfile.getProfile(regionId, accessKey, accessSecret);
            // 若报Can not find endpoint to access异常，请添加以下此行代码
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Alidns", "alidns.aliyuncs.com");
            acsClient = new DefaultAcsClient(profile);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 添加A记录
     * @param rr        主机记录。如果要解析@.exmaple.com，主机记录要填写”@”，而不是空。
     * @param value     记录值。A记录对应的IP地址
     * @param domain    域名名称。为空则将使用默认域名
     * @return          添加域名的recordId
     */
    public String addARecord(String rr, String value, String domain) {
        if (StringUtil.isBlank(domain)) domain = defaultDomain;
        if (StringUtil.isBlank(domain)) throw new BusinessException("为配置默认域名！");
        if (acsClient == null) throw new BusinessException("尚未配置阿里云域名解析账户！");
        try {
            AddDomainRecordRequest request = new AddDomainRecordRequest();
            request.setType("A");
            request.setRR(rr);
            request.setValue(value);
            request.setDomainName(domain);
            AddDomainRecordResponse response = acsClient.getAcsResponse(request);
            return response.getRecordId();
        } catch (ClientException e) {
            e.printStackTrace();
            throw new BusinessException("添加A记录失败：" + e.getLocalizedMessage());
        }
    }

    /**
     * 删除主机记录的A记录
     * @param rr        主机记录。如果要解析@.exmaple.com，主机记录要填写”@”，而不是空。
     * @param domain    域名名称。为空则将使用默认域名
     * @return          删除的记录条数
     */
    public int deleteARecord(String rr, String domain) {
        if (StringUtil.isBlank(domain)) domain = defaultDomain;
        if (StringUtil.isBlank(domain)) throw new BusinessException("为配置默认域名！");
        if (acsClient == null) throw new BusinessException("尚未配置阿里云域名解析账户！");
        try {
            DeleteSubDomainRecordsRequest request = new DeleteSubDomainRecordsRequest();
            request.setType("A");
            request.setRR(rr);
            request.setDomainName(domain);
            DeleteSubDomainRecordsResponse response = acsClient.getAcsResponse(request);
            return NumberUtil.intValue(response.getTotalCount(), 0);
        } catch (ClientException e) {
            e.printStackTrace();
            throw new BusinessException("添加A记录失败：" + e.getLocalizedMessage());
        }
    }

    /**
     * 删除A记录
     * @param recordId  调用addARecord返回的recordId
     */
    public void deleteARecord(String recordId) {
        if (acsClient == null) throw new BusinessException("尚未配置阿里云域名解析账户！");
        try {
            DeleteDomainRecordRequest request = new DeleteDomainRecordRequest();
            request.setRecordId(recordId);
            DeleteDomainRecordResponse response = acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
            throw new BusinessException("删除A记录失败：" + e.getLocalizedMessage());
        }
    }
}
