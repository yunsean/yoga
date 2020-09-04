package com.yoga.admin.shiro;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ShiroXMLReader {

    public Map<String, String> filterChainDefinitionMap(String directory) {
        Map<String, String> map = null;
        Document doc = null;
        InputStream in = null;
        try {
            SAXReader saxReader = new SAXReader();
            in = this.getClass().getResourceAsStream(directory);
            doc = saxReader.read(in);
            Element root = doc.getRootElement();
            map = this.putElementIntoMap(root, new String[]{"anon", "authc"});
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    private Map<String, String> putElementIntoMap(Element root, String[] filters) {
        Map<String, String> resMap = new HashMap<>();
        for (String filter : filters) {
            @SuppressWarnings("unchecked")
            Iterator<Element> authcIte = root.elementIterator(filter);
            while (authcIte.hasNext()) {
                Element sub = authcIte.next();
                @SuppressWarnings("unchecked")
                List<Element> urlList = sub.elements();
                for (Element url : urlList)
                    resMap.put(url.getStringValue(), filter);
            }
        }
        return resMap;
    }
}