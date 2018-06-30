package com.yoga.content.article.service;

import com.mongodb.*;
import com.yoga.content.column.model.Column;
import com.yoga.content.column.repo.ColumnRepository;
import com.yoga.content.constants.Contants;
import com.yoga.core.data.CommonPage;
import com.yoga.core.data.PageList;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.property.PropertiesService;
import com.yoga.core.utils.StrUtil;
import com.yoga.core.utils.TypeCastUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class ArticleService {

    @Autowired
    private ColumnRepository columnRepository;
    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private PropertiesService propertiesService;

    private static BasicDBObject returnKeysForFind() {
        BasicDBObject keys = new BasicDBObject();
        keys.put("_id", 1);
        keys.put("columnId", 1);
        keys.put("columnCode", 1);
        keys.put("columnName", 1);
        keys.put("templateId", 1);
        keys.put("templateCode", 1);
        keys.put("title", 1);
        keys.put("authorId", 1);
        keys.put("author", 1);
        keys.put("date", 1);
        return keys;
    }

    public static String clearHtml(String html) {
        String reg_tag = "<[\\s]*?#t#[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?#t#[\\s]*?>".replace("#t#", "script");
        html = Pattern.compile(reg_tag, Pattern.CASE_INSENSITIVE).matcher(html).replaceAll("");
        reg_tag = "<[^>]+>";
        reg_tag = "<[\\s\\S]*?>";
        html = Pattern.compile(reg_tag, Pattern.CASE_INSENSITIVE).matcher(html).replaceAll("");
        html = html.replaceAll(" ", "");
        html = html.replaceAll("\n{1,}", "#");
        html = html.startsWith("#") ? html.substring(1) : html;
        html = html.endsWith("#") ? html.substring(0, html.length() - 1) : html;
        html = html.replaceAll("#", "\n");
        html = html.replaceAll("&nbsp;", " ");
        return html;
    }

    private BasicDBObject generateDBObject(Map<String, String> fields) {
        BasicDBObject content = new BasicDBObject();
        int summaryLength = propertiesService.getCmsSummaryLength();
        if (summaryLength > 0) {
            String summary = null;
            if (fields != null) {
                summary = fields.get("content");
                if (summary == null) summary = fields.get("html");
            }
            if (StrUtil.isNotBlank(summary)) {
                summary = clearHtml(summary);
                if (summary.length() > summaryLength) {
                    summary = summary.substring(0, summaryLength);
                }
                content.put("summary", summary);
            }
        }
        if (fields != null) {
            for (Map.Entry<String, String> entry : fields.entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue();
                char type = 'S';
                int len = key.length();
                if (len > 3 && key.charAt(0) == '(' && key.charAt(2) == ')') {
                    type = key.charAt(1);
                    key = key.substring(3);
                }
                if (len < 1) continue;
                if (StrUtil.isBlank(val)) continue;
                content.put(key, createObject(key, val, type));
            }
        }
        return content;
    }

    public String addContent(long tenantId, long columnId, String templateCode, String title, Long authorId, String author, Map<String, String> fields) {
        Column column = columnRepository.findOne(columnId);
        if (column == null) throw new BusinessException("未找到指定的栏目！");
        BasicDBObject content = generateDBObject(fields);
        content.put("tenantId", tenantId);
        content.put("columnId", columnId);
        content.put("columnCode", column.getCode());
        content.put("columnName", column.getName());
        if (templateCode != null) {
            content.put("templateCode", templateCode);
        } else if (column.getTemplate() != null) {
            content.put("templateCode", column.getTemplate().getCode());
        }
        content.put("title", title);
        if (authorId != null) {
            content.put("authorId", authorId);
        }
        if (author != null) {
            content.put("author", author);
        }
        content.put("date", new Date());
        mongoOperations.insert(content, Contants.getCollectionName(tenantId));
        ObjectId id = content.getObjectId("_id");
        return id.toString();
    }

    private Object createObject(String key, String val, char type) {
        switch (type) {
            case 'n': {
                String[] parts = val.split("\\|");
                List<Double> values = new ArrayList<>();
                for (String part : parts) {
                    values.add(TypeCastUtil.toDouble(part));
                }
                return values;
            }
            case 'N': {
                return TypeCastUtil.toDouble(val);
            }
            case 'd': {
                String[] parts = val.split("\\|");
                List<Date> values = new ArrayList<>();
                for (String part : parts) {
                    values.add(TypeCastUtil.toDate(part));
                }
                return values;
            }
            case 'D': {
                return TypeCastUtil.toDate(val);
            }
            case 's': {
                String[] parts = val.split("\\|");
                List<String> values = new ArrayList<>();
                for (String part : parts) {
                    values.add(part);
                }
                return values;
            }
            case 'S':
            default: {
                return val;
            }
        }
    }

    private BasicDBObject createDBObject(String key, String val, String field, char type) {
        if (field.equals("_id")) {
            return new BasicDBObject(key, new ObjectId(val));
        } else {
            return new BasicDBObject(key, createObject(key, val, type));
        }
    }

    private List<BasicDBObject> generateQueries(Map<String, String> conditions) {
        List<BasicDBObject> queries = new ArrayList<>();
        if (conditions != null) {
            for (Map.Entry<String, String> condition : conditions.entrySet()) {
                String key = condition.getKey();
                String val = condition.getValue();
                char type = 'S';
                int len = key.length();
                if (len > 3 && key.charAt(0) == '(' && key.charAt(2) == ')') {
                    type = key.charAt(1);
                    key = key.substring(3);
                }
                if (len < 1) continue;
                if (StrUtil.isBlank(val)) continue;
                char first = key.charAt(0);
                if (first == '>' || first == '<' || first == '=') {
                    if (len < 2) continue;
                    char second = key.charAt(1);
                    if (second == '=') {
                        if (len < 3) continue;
                        key = key.substring(2);
                        if (first == '>') queries.add(new BasicDBObject(key, createDBObject("$gte", val, key, type)));
                        else if (first == '<')
                            queries.add(new BasicDBObject(key, createDBObject("$lte", val, key, type)));
                        else if (first == '=') queries.add(createDBObject(key, val, key, type));
                    } else {
                        key = key.substring(1);
                        if (first == '>') queries.add(new BasicDBObject(key, createDBObject("$gt", val, key, type)));
                        else if (first == '<')
                            queries.add(new BasicDBObject(key, createDBObject("$lt", val, key, type)));
                        else if (first == '=') queries.add(createDBObject(key, val, key, type));
                    }
                } else if (first == '!') {
                    if (len < 2) continue;
                    char second = key.charAt(1);
                    if (second == '@') {
                        if (len < 3) continue;
                        key = key.substring(2);
                        queries.add(new BasicDBObject(key, createDBObject("$nin", val, key, type)));
                    } else {
                        if (len < 2) continue;
                        key = key.substring(1);
                        queries.add(new BasicDBObject(key, createDBObject("$ne", val, key, type)));
                    }
                } else if (first == '.' || first == '@' || first == '%' || first == '~') {
                    if (len < 2) continue;
                    key = key.substring(1);
                    if (first == '.') {
                        Pattern pattern = Pattern.compile(val);
                        queries.add(new BasicDBObject(key, pattern));
                    } else if (first == '@') {
                        queries.add(new BasicDBObject(key, createDBObject("$in", val, key, type)));
                    } else if (first == '%') {
                        queries.add(new BasicDBObject(key, createDBObject("$mod", val, key, type)));
                    } else {
                        boolean is = true;
                        if (val != null && val.trim().equals("false") || val.trim().equals("0")) is = false;
                        queries.add(new BasicDBObject(key, new BasicDBObject("$exists", is)));
                    }
                } else {
                    queries.add(createDBObject(key, val, key, type));
                }
            }
        }
        return queries;
    }

    public PageList<DBObject> find(long tenantId, boolean onlineOnly, Map<String, String> conditions, String[] fields, String[] sorts, int pageIndex, int pageSize) {
        DBCollection collection = mongoOperations.getCollection(Contants.getCollectionName(tenantId));
        List<BasicDBObject> queries = generateQueries(conditions);
        queries.add(new BasicDBObject("tenantId", tenantId));
        if (onlineOnly) {
            List<BasicDBObject> online = new ArrayList<>();
            online.add(new BasicDBObject("online", 1));
            online.add(new BasicDBObject("online", new BasicDBObject("$exists", false)));
            queries.add(new BasicDBObject("$or", online));
        }
        BasicDBObject query = null;
        if (queries.size() > 0) {
            query = new BasicDBObject("$and", queries);
        }
        BasicDBObject sortFields = new BasicDBObject();
        if (sorts != null) {
            for (String field : sorts) {
                if (field.length() < 1) {
                    sortFields.put("_id", 1);
                } else if (field.charAt(0) == '-') {
                    if (field.length() < 2) {
                        sortFields.put("_id", -1);
                    } else {
                        field = field.substring(1);
                        sortFields.put(field, -1);
                    }
                } else {
                    sortFields.put(field, 1);
                }

            }
        }
        if (sortFields.size() < 1) {
            sortFields.put("sort", -1);
            sortFields.put("date", -1);
        }
        long totalCount = collection.count(query);
        DBObject keys = returnKeysForFind();
        if (fields != null) {
            for (String field : fields) {
                keys.put(field, 1);
            }
        }
        DBCursor cursor = collection.find(query, keys).sort(sortFields).skip(pageIndex * pageSize).limit(pageSize).addOption(Bytes.QUERYOPTION_NOTIMEOUT);
        PageList<DBObject> result = new PageList<>();
        while (cursor.hasNext()) {
            DBObject object = cursor.next();
            result.add(object);
        }
        result.setPage(new CommonPage(pageIndex, pageSize, totalCount));
        return result;
    }

    public long countOf(long tenantId, boolean onlineOnly, Map<String, String> conditions) {
        DBCollection collection = mongoOperations.getCollection(Contants.getCollectionName(tenantId));
        List<BasicDBObject> queries = generateQueries(conditions);
        queries.add(new BasicDBObject("tenantId", tenantId));
        if (onlineOnly) {
            List<BasicDBObject> online = new ArrayList<>();
            online.add(new BasicDBObject("online", 1));
            online.add(new BasicDBObject("online", new BasicDBObject("$exists", false)));
            queries.add(new BasicDBObject("$or", online));
        }
        BasicDBObject query = null;
        if (queries.size() > 0) {
            query = new BasicDBObject("$and", queries);
        }
        long totalCount = collection.count(query);
        return totalCount;
    }

    public long countOf(long tenantId, Long columnId, String columnCode, String templateCode, Long authorId, String title, Date begin, Date end) {
        DBCollection collection = mongoOperations.getCollection(Contants.getCollectionName(tenantId));
        List<BasicDBObject> queries = new ArrayList<>();
        queries.add(new BasicDBObject("tenantId", tenantId));
        if (columnId != null) queries.add(new BasicDBObject("columnId", columnId));
        if (columnCode != null && columnCode.trim().length() > 0)
            queries.add(new BasicDBObject("columnCode", columnCode.trim()));
        if (templateCode != null && templateCode.trim().length() > 0)
            queries.add(new BasicDBObject("templateCode", templateCode.trim()));
        if (authorId != null) queries.add(new BasicDBObject("authorId", authorId));
        if (title != null && title.trim().length() > 0) {
            Pattern pattern = Pattern.compile("^.*" + title.trim() + "8.*$", Pattern.CASE_INSENSITIVE);
            queries.add(new BasicDBObject("title", pattern));
        }
        if (begin != null) queries.add(new BasicDBObject("date", new BasicDBObject("$gte", begin)));
        if (end != null) queries.add(new BasicDBObject("date", new BasicDBObject("$lte", end)));
        BasicDBObject query = new BasicDBObject("$and", queries);
        long count = collection.count(query);
        return count;
    }

    public PageList<DBObject> find(long tenantId, boolean onlineOnly, Long columnId, String columnCode, String templateCode, Long authorId, String title, Date begin, Date end, String[] fields, int pageIndex, int pageSize) {
        DBCollection collection = mongoOperations.getCollection(Contants.getCollectionName(tenantId));
        List<BasicDBObject> queries = new ArrayList<>();
        queries.add(new BasicDBObject("tenantId", tenantId));
        if (columnId != null) queries.add(new BasicDBObject("columnId", columnId));
        if (columnCode != null && columnCode.trim().length() > 0)
            queries.add(new BasicDBObject("columnCode", columnCode.trim()));
        if (templateCode != null && templateCode.trim().length() > 0)
            queries.add(new BasicDBObject("templateCode", templateCode.trim()));
        if (authorId != null) queries.add(new BasicDBObject("authorId", authorId));
        if (title != null && title.trim().length() > 0) {
            Pattern pattern = Pattern.compile("^.*" + title.trim() + "8.*$", Pattern.CASE_INSENSITIVE);
            queries.add(new BasicDBObject("title", pattern));
        }
        if (begin != null) queries.add(new BasicDBObject("date", new BasicDBObject("$gte", begin)));
        if (end != null) queries.add(new BasicDBObject("date", new BasicDBObject("$lte", end)));
        if (onlineOnly) {
            List<BasicDBObject> online = new ArrayList<>();
            online.add(new BasicDBObject("online", 1));
            online.add(new BasicDBObject("online", new BasicDBObject("$exists", false)));
            queries.add(new BasicDBObject("$or", online));
        }
        BasicDBObject query = new BasicDBObject("$and", queries);
        BasicDBObject sort = new BasicDBObject("sort", -1);
        sort.put("date", -1);
        long totalCount = countOf(tenantId, columnId, columnCode, templateCode, authorId, title, begin, end);
        DBObject keys = returnKeysForFind();
        if (fields != null && fields.length > 0) {
            for (String field : fields) {
                keys.put(field, 1);
            }
        }
        DBCursor cursor = collection.find(query, keys).sort(sort).skip(pageIndex * pageSize).limit(pageSize).addOption(Bytes.QUERYOPTION_NOTIMEOUT);
        PageList<DBObject> result = new PageList<>();
        while (cursor.hasNext()) {
            DBObject object = cursor.next();
            result.add(object);
        }
        result.setPage(new CommonPage(pageIndex, pageSize, totalCount));
        return result;
    }

    public DBObject detail(long tenantId, String id) {
        DBCollection collection = mongoOperations.getCollection(Contants.getCollectionName(tenantId));
        ObjectId objectId = new ObjectId(id);
        DBObject object = collection.findOne(objectId);
        if (object == null) throw new BusinessException("指定内容不存在！");
        return object;
    }

    public List<DBObject> details(long tenantId, String[] ids) {
        if (ids.length == 0) throw new BusinessException("未指定内容编码！");
        DBCollection collection = mongoOperations.getCollection(Contants.getCollectionName(tenantId));
        List<DBObject> queries = new ArrayList<>();
        for (String id : ids) {
            queries.add(new BasicDBObject("_id", new ObjectId(id)));
        }
        DBObject query = new BasicDBObject("$or", queries);
        DBCursor cursor = collection.find(query);
        List<DBObject> result = new ArrayList<>();
        while (cursor.hasNext()) {
            DBObject object = cursor.next();
            result.add(object);
        }
        return result;
    }

    public void removeContent(long tenantId, String id) {
        DBObject content = detail(tenantId, id);
        mongoOperations.remove(content, Contants.getCollectionName(tenantId));
    }

    public void updateContent(long tenantId, String articleId, Map<String, String> fields) {
        fields.remove("articleId");
        fields.remove("columnId");
        BasicDBObject content = generateDBObject(fields);
        DBObject query = new BasicDBObject("_id", new ObjectId(articleId));
        DBObject update = new BasicDBObject("$set", content);
        mongoOperations.getCollection(Contants.getCollectionName(tenantId)).update(query, update);
    }

    public void setOnline(long tenantId, String articleId, boolean online) {
        detail(tenantId, articleId);
        double status = online ? 1F : 0F;
        mongoOperations.updateFirst(
                new Query(Criteria.where("_id").is(articleId)),
                Update.update("online", status),
                Contants.getCollectionName(tenantId));
    }

    public void setSort(long tenantId, String articleId, int sort) {
        detail(tenantId, articleId);
        mongoOperations.updateFirst(
                new Query(Criteria.where("_id").is(articleId)),
                Update.update("sort", sort),
                Contants.getCollectionName(tenantId));
    }
}
