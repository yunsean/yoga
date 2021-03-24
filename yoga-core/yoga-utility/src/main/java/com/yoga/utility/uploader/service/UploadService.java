package com.yoga.utility.uploader.service;

import com.lowagie.text.pdf.PdfReader;
import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.core.property.PropertiesService;
import com.yoga.core.utils.DateUtil;
import com.yoga.core.utils.StringUtil;
import com.yoga.utility.image.ImageConvertService;
import com.yoga.utility.uploader.mapper.UploadFileMapper;
import com.yoga.utility.uploader.model.UploadFile;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service("globalFileUploadService")
public class UploadService extends BaseService {

    @Autowired
    private ImageConvertService imageConvertService;
    @Autowired
    private PropertiesService propertiesService;
    @Autowired
    private UploadFileMapper fileMapper;

    public String resize(String original, Integer width, Integer height) {
        if (width != 0 || height != 0) {
            String path = imageConvertService.drawImg(original, width, height);
            if (path != null && (new File(path)).exists()) {
                new File(original).delete();
                return path;
            }
        }
        return original;
    }

    public File generateFilePath(long tenantId, String name) {
        int index = 0;
        String now = DateUtil.format(new Date(), "yyyyMMddHHmmssSSS");
        String parentPath = propertiesService.getFileLocalPath(tenantId);
        new File(parentPath).mkdirs();
        if (name == null) name = "";
        while (true) {
            int dot = name.lastIndexOf('.');
            String base = (dot == -1) ? name : name.substring(0, dot);
            String extension = (dot == -1) ? "" : name.substring(dot);
            String newName = base;
            newName = base+ "_" + now;
            if (index > 0) newName += "_" + index;
            newName += extension;
            File destFile = new File(parentPath, newName);
            if (!destFile.exists()) return destFile;
            else index++;
        }
    }

    public List<UploadFile> list(Collection<Long> fileIds) {
        if (fileIds == null || fileIds.size() < 1) return new ArrayList<>();
        return new MapperQuery<>(UploadFile.class)
                .andIn("id", fileIds)
                .query(fileMapper);
    }

    public UploadFile add(long tenantId, String filename, String file, String purpose, String contentType, Long size) {
        try { if (size == null) size = new File(file).length(); } catch (Exception ex) {}
        try { if (StringUtil.isBlank(filename)) filename = new File(file).getName(); } catch (Exception ex) {}
        //try { if (contentType == null) contentType = filename.substring(filename.lastIndexOf(".") + 1); } catch (Exception ex) {}
        String url = file.replace(propertiesService.getFileLocalPath(), propertiesService.getFileRemotePath());
        url = url.replace("\\", "/");
        if (StringUtil.isBlank(contentType)) contentType = getFileType(file);
        UploadFile uploadFile = new UploadFile(tenantId, filename, file, url, purpose, contentType, size);
        fileMapper.insert(uploadFile);
        return uploadFile;
    }

    private static final HashMap<String, String> mFileTypes = new HashMap<String, String>();
    static {
        // images
        mFileTypes.put("FFD8FF", "jpg");
        mFileTypes.put("89504E47", "png");
        mFileTypes.put("47494638", "gif");
        mFileTypes.put("49492A00", "tif");
        mFileTypes.put("424D", "bmp");
        // pdf
        mFileTypes.put("255044462D312E", "pdf");
        // word
        mFileTypes.put("D0CF11E0", "doc");
        mFileTypes.put("504B0304", "docx");
        mFileTypes.put("504B0304", "xlsx");
    }
    public static String getFileType(String filePath) {
        System.out.println(getFileHeader(filePath));  //返回十六进制  如：504B0304
        String type = mFileTypes.get(getFileHeader(filePath));
        if (type == null) return "UNKNOWN";
        if (type.equals("jpg") || type.equals("png") || type.equals("gif") || type.equals("tif") || type.equals("bmp")) return "IMAGE";
        if (type.equals("pdf")) return "PDF";
        if (type.equals("doc") || type.equals("docx") || type.equals("xlsx")) return "WORD";
        return "UNKNOWN";
    }
    public static String getFileHeader(String filePath) {
        FileInputStream is = null;
        String value = null;
        try {
            is = new FileInputStream(filePath);
            byte[] b = new byte[4];
            /*
             * int read() 从此输入流中读取一个数据字节。int read(byte[] b) 从此输入流中将最多 b.length
             * 个字节的数据读入一个 byte 数组中。 int read(byte[] b, int off, int len)
             * 从此输入流中将最多 len 个字节的数据读入一个 byte 数组中。
             */
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return value;
    }
    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }

    public UploadFile get(long tenantId, long id) {
        UploadFile file = fileMapper.selectByPrimaryKey(id);
        if (file == null || file.getTenantId() != tenantId) throw new BusinessException("文件不存在！");
        return file;
    }

    public void delete(long tenantId, long id) {
        UploadFile file = fileMapper.selectByPrimaryKey(id);
        if (file == null || file.getTenantId() != tenantId) throw new BusinessException("文件不存在！");
        new File(file.getLocalPath()).delete();
        fileMapper.deleteByPrimaryKey(id);
    }

    public List<String> file2Image(long tenantId, long fileId, int dpi) {
        UploadFile uploadFile = get(tenantId, fileId);
        if (uploadFile == null) throw new BusinessException("文件不存在！");
        File file = new File(uploadFile.getLocalPath());
        if (!file.exists()) throw new BusinessException("物理文件不存在！");
        String cachePath = propertiesService.getFileLocalPath();
        if (cachePath.length() < 1 || !cachePath.endsWith(File.separator)) cachePath = cachePath + File.separator;
        cachePath = cachePath + "pdfimages";
        new File(cachePath).mkdirs();
        deleteOldCache(cachePath);
        List<String> imageFiles = pdf2Image(uploadFile.getLocalPath(), cachePath, dpi);
        List<String> imageUrls = imageFiles.stream().map(it-> it.replace(propertiesService.getFileLocalPath(), propertiesService.getFileRemotePath())).collect(Collectors.toList());
        return imageUrls;
    }
    public List<String> pdf2Image(String pdfFilePath, String dstImgFolder, int dpi) {
        File file = new File(pdfFilePath);
        PDDocument pdDocument;
        try {
            int dot = file.getName().lastIndexOf('.');
            String imagePDFName = dot > 0 ? file.getName().substring(0, dot) : file.getName();
            String imgFolderPath = dstImgFolder + File.separator + imagePDFName + File.separator;
            File folder = new File(imgFolderPath);
            if (folder.exists()) {
                Collection<File> images = FileUtils.listFiles(folder, new String[]{"png"}, false);
                if (images.size() > 0) {
                    return images.stream()
                            .map(image-> image.getAbsolutePath())
                            .sorted()
                            .collect(Collectors.toList());
                }
            }
            FileUtils.deleteQuietly(new File(imgFolderPath));
            if (createDirectory(imgFolderPath)) {
                pdDocument = PDDocument.load(file);
                PDFRenderer renderer = new PDFRenderer(pdDocument);
                PdfReader reader = new PdfReader(pdfFilePath);
                int pages = reader.getNumberOfPages();
                List<String> imageFiles = new ArrayList<>();
                for (int i = 0; i < pages; i++) {
                    StringBuffer imgFilePath = new StringBuffer();
                    imgFilePath.append(imgFolderPath);
                    imgFilePath.append(String.format("%05d", i));
                    imgFilePath.append(".png");
                    File dstFile = new File(imgFilePath.toString());
                    BufferedImage image = renderer.renderImageWithDPI(i, dpi);
                    ImageIO.write(image, "png", dstFile);
                    imageFiles.add(imgFilePath.toString());
                    logger.info(imgFilePath.toString());
                }
                return imageFiles;
            } else {
                throw new BusinessException("PDF文档转PNG图片失败：" + "创建" + imgFolderPath + "失败");
            }
        } catch (IOException e) {
            try {
                File imageFile = new File(pdfFilePath);
                Image image = ImageIO.read(imageFile);
                if (image == null || image.getWidth(null) < 10) throw new BusinessException("不支持的文件格式！");
                return new ArrayList<String>(){{
                    add(pdfFilePath);
                }};
            } catch (Exception ex) {
                throw new BusinessException(e.getMessage());
            }
        }
    }
    public static void deleteOldCache(String strPath) {
        Calendar deleteBefore = Calendar.getInstance();
        deleteBefore.add(Calendar.DATE, -1);
        long beforeAt = deleteBefore.getTimeInMillis();
        File dir = new File(strPath);
        File[] files = dir.listFiles();
        if (files == null) return;
        Arrays.stream(files).forEach(file -> {
            if (file.lastModified() < beforeAt) {
                try { FileUtils.deleteDirectory(file); } catch (Exception ex) {}
            }
        });
    }
    private static boolean createDirectory(String folder) {
        File dir = new File(folder);
        if (dir.exists()) return true;
        else return dir.mkdirs();
    }
}
