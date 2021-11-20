package com.yoga.utility.baidu.aip.controller;

import com.yoga.core.base.BaseController;
import com.yoga.core.base.BaseDto;
import com.yoga.core.data.ApiResult;
import com.yoga.core.data.ApiResults;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.setting.annotation.Settable;
import com.yoga.utility.baidu.aip.ao.BaiduAiqConfig;
import com.yoga.utility.baidu.aip.ao.SimilarImage;
import com.yoga.utility.baidu.aip.dto.AddSimilarDto;
import com.yoga.utility.baidu.aip.dto.DeleteSimilarDto;
import com.yoga.utility.baidu.aip.dto.SaveSettingDto;
import com.yoga.utility.baidu.aip.dto.SearchSimilarDto;
import com.yoga.utility.baidu.aip.service.ImageSearchService;
import com.yoga.utility.baidu.aip.vo.SimilarImageVo;
import com.yoga.utility.feie.service.FeiePrintService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Settable
@Controller
@Api(tags = "百度图像搜索")
@RequestMapping("/admin/utility/baidu/aiq")
public class ImageSearchController extends BaseController {

    @Autowired
    private ImageSearchService imageSearchService;

    @ApiIgnore
    @Settable(module = ImageSearchService.ModuleName, key = ImageSearchService.AiqConfig, name = "系统设置-百度图像搜索设置")
    @RequestMapping("/setting")
    public String showSetting(ModelMap model, @Valid BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        model.put("config", imageSearchService.readConfig(dto.getTid()));
        return "/admin/utility/baidu/aip";
    }

    @ApiIgnore
    @ResponseBody
    @RequiresAuthentication
    @RequestMapping("/setting/save.json")
    public ApiResult saveSetting(@Valid SaveSettingDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        imageSearchService.saveConfig(dto.getTid(), new BaiduAiqConfig(dto.getAppId(), dto.getApiKey(), dto.getSecretKey()));
        return new ApiResult();
    }

    @ResponseBody
    @RequiresAuthentication
    @PostMapping("/similar/add.json")
    @ApiOperation(value = "添加相似图图库")
    public ApiResult addSimilar(@Valid @ModelAttribute AddSimilarDto dto, @ModelAttribute("image") @RequestParam(value = "image") MultipartFile image, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        byte[] bytes = IOUtils.toByteArray(image.getInputStream());
        String[] tags = null;
        if (dto.getTags() != null) tags = Arrays.stream(dto.getTags()).map(it-> it.toString()).toArray(String[]::new);
        imageSearchService.addSimilar(dto.getTid(), bytes, dto.getBrief(), tags);
        return new ApiResult();
    }
    @ResponseBody
    @RequiresAuthentication
    @PostMapping("/similar/search.json")
    @ApiOperation(value = "搜索相似图")
    public ApiResults<SimilarImageVo> searchSimilar(@Valid @ModelAttribute SearchSimilarDto dto, @ModelAttribute("image") @RequestParam(value = "image") MultipartFile image, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        byte[] bytes = IOUtils.toByteArray(image.getInputStream());
        String[] tags = null;
        if (dto.getTags() != null) tags = Arrays.stream(dto.getTags()).map(it-> it.toString()).toArray(String[]::new);
        List<SimilarImage> images = imageSearchService.searchSimilar(dto.getTid(), bytes, tags, dto.getCountLimit() == null ? 10 : dto.getCountLimit());
        return new ApiResults<>(images, SimilarImageVo.class);
    }
    @ResponseBody
    @RequiresAuthentication
    @PostMapping("/similar/delete.json")
    @ApiOperation(value = "删除相似图图库")
    public ApiResult deleteSimilar(@Valid @ModelAttribute DeleteSimilarDto dto, @ModelAttribute("image") @RequestParam(value = "image") MultipartFile image, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        byte[] bytes = IOUtils.toByteArray(image.getInputStream());
        String[] tags = null;
        if (dto.getTags() != null) tags = Arrays.stream(dto.getTags()).map(it-> it.toString()).toArray(String[]::new);
        imageSearchService.deleteSimilar(dto.getTid(), bytes, tags);
        return new ApiResult();
    }
}
