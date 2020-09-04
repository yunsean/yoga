package com.yoga.content.article.service;

import com.github.pagehelper.PageInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import com.yoga.content.article.ao.FocusCount;
import com.yoga.content.column.model.Column;
import com.yoga.content.column.service.ColumnService;
import com.yoga.content.constants.Contants;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.property.PropertiesService;
import com.yoga.core.utils.NumberUtil;
import com.yoga.core.utils.StringUtil;
import com.yoga.core.utils.TypeCastUtil;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ArticleService {

    @Autowired
    private ColumnService columnService;
    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private PropertiesService propertiesService;

    public String add(long tenantId, long columnId, String templateCode, String title, Long creatorId, String creator, Map<String, ? extends Object> fields) {
        Column column = columnService.get(tenantId, columnId);
        if (column == null) throw new BusinessException("未找到指定的栏目！");
        BasicDBObject content = createObject(fields);
        content.put("tenantId", tenantId);
        content.put("columnId", columnId);
        content.put("columnCode", column.getCode());
        content.put("columnName", column.getName());
        if (templateCode != null) content.put("templateCode", templateCode);
        else if (column.getTemplateCode() != null) content.put("templateCode", column.getTemplateCode());
        content.put("title", title);
        if (creatorId != null) content.put("creatorId", creatorId);
        if (creator != null) content.put("creator", creator);
        content.put("date", new Date());
        createSummary(content);
        mongoOperations.insert(content, Contants.getCollectionName(tenantId));
        ObjectId id = content.getObjectId(DBCollection.ID_FIELD_NAME);
        return id.toString();
    }
    public void remove(long tenantId, String id) {
        MongoCollection<Document> collection = mongoOperations.getCollection(Contants.getCollectionName(tenantId));
        collection.deleteOne(Filters.eq(DBCollection.ID_FIELD_NAME, new ObjectId(id)));
    }

    public long count(long tenantId, boolean onlineOnly, Map<String, String> conditions) {
        Bson filters = createQuery(tenantId, conditions, onlineOnly);
        return count(tenantId, filters);
    }
    public PageInfo<Document> find(long tenantId, boolean onlineOnly, Map<String, String> conditions, String[] fields, String[] sorts, int pageIndex, int pageSize) {
        Bson filters = createQuery(tenantId, conditions, onlineOnly);
        Bson sort = createSort(sorts);
        Bson projection = createProjection(fields);
        return findPagable(tenantId, filters, projection, sort, pageIndex, pageSize);
    }

    public long count(long tenantId, boolean onlineOnly, Long columnId, String columnCode, String templateCode, Long authorId, String title, Date begin, Date end) {
        Bson filters = createQuery(tenantId, onlineOnly, columnId, columnCode, templateCode, authorId, title, begin, end);
        return count(tenantId, filters);
    }
    public PageInfo<Document> find(long tenantId, boolean onlineOnly, Long columnId, String columnCode, String templateCode, Long authorId, String title, Date begin, Date end, String[] fields, int pageIndex, int pageSize) {
        Bson filters = createQuery(tenantId, onlineOnly, columnId, columnCode, templateCode, authorId, title, begin, end);
        Bson sort = createSort(null);
        Bson projection = createProjection(fields);
        return findPagable(tenantId, filters, projection, sort, pageIndex, pageSize);
    }
    public List<Document> find(long tenantId, boolean onlineOnly, Long columnId, String columnCode, String templateCode, Long authorId, String title, Date begin, Date end, String[] fields) {
        Bson filters = createQuery(tenantId, onlineOnly, columnId, columnCode, templateCode, authorId, title, begin, end);
        Bson sort = createSort(null);
        Bson projection = createProjection(fields);
        return findAll(tenantId, filters, projection, sort);
    }

    public Document get(long tenantId, String id) {
        return findOne(tenantId, Filters.eq(DBCollection.ID_FIELD_NAME, new ObjectId(id)), null);
    }
    public List<Document> get(long tenantId, List<String> ids, String[] fields) {
        List<DBObject> queries = new ArrayList<>();
        for (String id : ids) queries.add(new BasicDBObject(DBCollection.ID_FIELD_NAME, new ObjectId(id)));
        Bson query = new BasicDBObject("$or", queries);
        Bson projection = createProjection(fields);
        return findAll(tenantId, query, projection, null);
    }

    public void update(long tenantId, String articleId, Map<String, ? extends Object> fields) {
        MongoCollection<Document> collection = mongoOperations.getCollection(Contants.getCollectionName(tenantId));
        fields.remove("articleId");
        fields.remove("columnId");
        fields.remove("columnCode");
        fields.remove("columnName");
        BasicDBObject update = createObject(fields);
        createSummary(update);
        update(tenantId, articleId, (Bson) update);
    }
    public void setOnline(long tenantId, String articleId, boolean online) {
        BasicDBObject update = new BasicDBObject();
        update.put("online", online ? 1F : 0F);
        update(tenantId, articleId, (Bson) update);
    }
    public void setSort(long tenantId, String articleId, int sort) {
        BasicDBObject update = new BasicDBObject();
        update.put("sort", sort);
        update(tenantId, articleId, (Bson) update);
    }

    private long count(long tenantId, Bson filter) {
        MongoCollection<Document> collection = mongoOperations.getCollection(Contants.getCollectionName(tenantId));
        return collection.countDocuments(filter);
    }
    private Document findOne(long tenantId, Bson filter, Bson projection) {
        MongoCollection<Document> collection = mongoOperations.getCollection(Contants.getCollectionName(tenantId));
        FindIterable<Document> findIterable = collection.find(filter);
        if (projection != null) findIterable.projection(projection);
        findIterable.limit(1);
        try (MongoCursor<Document> cursor = findIterable.iterator()) {
            if (cursor.hasNext()) return cursor.next();
            else return null;
        } catch (Exception ex) {
            return null;
        }
    }
    private List<Document> findAll(long tenantId, Bson filter, Bson projection, Bson sort) {
        MongoCollection<Document> collection = mongoOperations.getCollection(Contants.getCollectionName(tenantId));
        FindIterable<Document> findIterable = collection.find(filter);
        if (projection != null) findIterable.projection(projection);
        if (sort != null) findIterable.sort(sort);
        try (MongoCursor<Document> cursor = findIterable.iterator()) {
            List<Document> documents = new ArrayList<>();
            while (cursor.hasNext()) documents.add(cursor.next());
            return documents;
        } catch (Exception ex) {
            return null;
        }
    }
    private PageInfo<Document> findPagable(long tenantId, Bson filter, Bson projection, Bson sort, int pageIndex, int pageSize) {
        MongoCollection<Document> collection = mongoOperations.getCollection(Contants.getCollectionName(tenantId));
        FindIterable<Document> findIterable;
        if (filter != null) findIterable = collection.find(filter);
        else findIterable = collection.find();
        if (projection != null) findIterable.projection(projection);
        if (sort != null) findIterable.sort(sort);
        long totalCount = collection.countDocuments(filter);
        try (MongoCursor<Document> cursor = findIterable.skip(pageIndex * pageSize).limit(pageSize).iterator()) {
            List<Document> documents = new ArrayList<>();
            while (cursor.hasNext()) documents.add(cursor.next());
            PageInfo<Document> result = new PageInfo<>(documents);
            result.setPageNum(pageIndex + 1);
            result.setPageSize(pageSize);
            result.setTotal(totalCount);
            return result;
        } catch (Exception ex) {
            return null;
        }
    }
    private void update(long tenantId, String id, Bson update) {
        MongoCollection<Document> collection = mongoOperations.getCollection(Contants.getCollectionName(tenantId));
        Bson filter = Filters.and(Filters.eq(DBCollection.ID_FIELD_NAME, new ObjectId(id)));
        UpdateOptions options = new UpdateOptions().upsert(false);
        collection.updateOne(filter, new BasicDBObject("$set", update), options);
    }

    private static String clearHtml(String html) {
        String reg_tag = "<[\\s]*?#t#[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?#t#[\\s]*?>".replace("#t#", "script");
        html = Pattern.compile(reg_tag, Pattern.CASE_INSENSITIVE).matcher(html).replaceAll("");
        reg_tag = "<[^>]+>";
        html = Pattern.compile(reg_tag, Pattern.CASE_INSENSITIVE).matcher(html).replaceAll("");
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

    private <R> List<R> convertToList(Object val, Function<Object, R> mapper) {
        List<R> values = new ArrayList<>();
        if (val instanceof Collection) {
            for (Object part : (Collection) val) values.add(mapper.apply(part));
        } else {
            values.add(mapper.apply(val.toString()));
        }
        return values;
    }
    private <R> R convertToObject(Object val, Function<Object, R> mapper) {
        if (val instanceof Collection) {
            return mapper.apply(((Collection) val).iterator().next());
        } else {
            return mapper.apply(val.toString());
        }
    }
    private Object createObject(char type, Object val) {
        switch (type) {
            case 'n': return convertToList(val, TypeCastUtil::toDouble);
            case 'N': return convertToObject(val, TypeCastUtil::toDouble);
            case 'd': return convertToList(val, TypeCastUtil::toDate);
            case 'D': return convertToObject(val, TypeCastUtil::toDate);
            case 's': return convertToList(val, Object::toString);
            case 'S':
            default: return convertToObject(val, Object::toString);
        }
    }
    private String getField(Map<String, Object> fields, String name) {
        Object value = fields.get(name);
        if (value instanceof Collection) return ((Collection)value).iterator().next().toString();
        else return value.toString();
    }
    private void createSummary(BasicDBObject article) {
        int summaryLength = propertiesService.getCmsSummaryLength();
        if (summaryLength > 0) {
            Object summary = article.get("summary");
            if (summary != null) return;
            Object content = article.get("content");
            if (content == null) content = article.get("html");
            if (content == null) return;
            if (content instanceof Collection) content = ((Collection) content).iterator().next();
            if (StringUtil.isNotBlank(content.toString())) {
                String result = clearHtml(content.toString());
                if (result.length() > summaryLength) summary = result.substring(0, summaryLength);
                article.put("summary", result);
            }
        }
    }
    private BasicDBObject createObject(Map<String, ? extends Object> fields) {
        BasicDBObject article = new BasicDBObject();
        if (fields == null) return article;
        fields.forEach((key, val)-> {
            char type = 'S';
            int len = key.length();
            if (len < 1) return;
            if (len > 3 && key.charAt(0) == '(' && key.charAt(2) == ')') {
                type = key.charAt(1);
                key = key.substring(3);
            }
            if (val == null) return;
            article.put(key, createObject(type, val));
        });
        return article;
    }
    private BasicDBObject createObject(long tenantId, String key, Object val, String field, char type) {
        if (field.equals(DBCollection.ID_FIELD_NAME)) {
            return new BasicDBObject(DBCollection.ID_FIELD_NAME, new ObjectId(val.toString()));
        } else if (field.equals("columnId")) {
            long columnId = NumberUtil.longValue(val.toString());
            if (columnId == 0) return new BasicDBObject(key, createObject(type, val));
            List<Column> columns = columnService.childrenOf(tenantId, columnId, true, false);
            List<BasicDBObject> columnIds = columns.stream().map(it-> new BasicDBObject("columnId", it.getId())).collect(Collectors.toList());
            return new BasicDBObject("$or", columnIds);
        } else {
            return new BasicDBObject(key, createObject(type, val));
        }
    }
    private Bson createQuery(long tenantId, Map<String, String> conditions, boolean onlineOnly) {
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
                char first = key.charAt(0);
                if (first == '>' || first == '<' || first == '=') {
                    if (len < 2) continue;
                    char second = key.charAt(1);
                    if (second == '=') {
                        if (len < 3) continue;
                        key = key.substring(2);
                        if (first == '>') queries.add(new BasicDBObject(key, createObject(tenantId, "$gte", val, key, type)));
                        else if (first == '<') queries.add(new BasicDBObject(key, createObject(tenantId, "$lte", val, key, type)));
                        else queries.add(createObject(tenantId, key, val, key, type));
                    } else {
                        key = key.substring(1);
                        if (first == '>') queries.add(new BasicDBObject(key, createObject(tenantId, "$gt", val, key, type)));
                        else if (first == '<') queries.add(new BasicDBObject(key, createObject(tenantId, "$lt", val, key, type)));
                        else queries.add(createObject(tenantId, key, val, key, type));
                    }
                } else if (first == '!') {
                    if (len < 2) continue;
                    char second = key.charAt(1);
                    if (second == '@') {
                        if (len < 3) continue;
                        key = key.substring(2);
                        queries.add(new BasicDBObject(key, createObject(tenantId, "$nin", val, key, type)));
                    } else {
                        key = key.substring(1);
                        queries.add(new BasicDBObject(key, createObject(tenantId, "$ne", val, key, type)));
                    }
                } else if (first == '.' || first == '@' || first == '%' || first == '~') {
                    if (len < 2) continue;
                    if (val == null) continue;
                    key = key.substring(1);
                    if (first == '.') {
                        Pattern pattern = Pattern.compile(val);
                        queries.add(new BasicDBObject(key, pattern));
                    } else if (first == '@') {
                        queries.add(new BasicDBObject(key, createObject(tenantId, "$in", val, key, type)));
                    } else if (first == '%') {
                        queries.add(new BasicDBObject(key, createObject(tenantId, "$mod", val, key, type)));
                    } else {
                        boolean is = true;
                        if (val.trim().equals("false") || val.trim().equals("0")) is = false;
                        queries.add(new BasicDBObject(key, new BasicDBObject("$exists", is)));
                    }
                } else {
                    queries.add(createObject(tenantId, key, val, key, type));
                }
            }
        }
        if (onlineOnly) {
            List<BasicDBObject> online = new ArrayList<>();
            online.add(new BasicDBObject("online", 1));
            online.add(new BasicDBObject("online", new BasicDBObject("$exists", false)));
            queries.add(new BasicDBObject("$or", online));
        }
        if (queries.size() > 1) return new BasicDBObject("$and", queries);
        else if (queries.size() > 0) return queries.get(0);
        else return null;
    }
    private Bson createSort( String[] sorts) {
        BasicDBObject sortFields = new BasicDBObject();
        if (sorts != null) {
            for (String field : sorts) {
                if (field.length() < 1) {
                    sortFields.put(DBCollection.ID_FIELD_NAME, 1);
                } else if (field.charAt(0) == '-') {
                    if (field.length() < 2) sortFields.put(DBCollection.ID_FIELD_NAME, -1);
                    else sortFields.put(field.substring(1), -1);
                } else {
                    sortFields.put(field, 1);
                }
            }
        }
        if (sortFields.size() < 1) {
            sortFields.put("sort", -1);
            sortFields.put("date", -1);
        }
        return sortFields;
    }
    private Bson createProjection(String[] fields) {
        BasicDBObject keys = new BasicDBObject();
        keys.put(DBCollection.ID_FIELD_NAME, 1);
        keys.put("columnId", 1);
        keys.put("columnCode", 1);
        keys.put("columnName", 1);
        keys.put("templateId", 1);
        keys.put("templateCode", 1);
        keys.put("title", 1);
        keys.put("creatorId", 1);
        keys.put("creator", 1);
        keys.put("date", 1);
        if (fields != null) {
            for (String field : fields) {
                keys.put(field, 1);
            }
        }
        return keys;
    }
    private Bson createQuery(long tenantId, boolean onlineOnly, Long columnId, String columnCode, String templateCode, Long authorId, String title, Date begin, Date end) {
        List<BasicDBObject> queries = new ArrayList<>();
        if (onlineOnly) {
            List<BasicDBObject> online = new ArrayList<>();
            online.add(new BasicDBObject("online", new BasicDBObject("$exists", false)));
            online.add(new BasicDBObject("online", 1));
            queries.add(new BasicDBObject("$or", online));
        }
        if (columnId != null) {
            List<Column> columns = columnService.childrenOf(tenantId, columnId, true, false);
            List<BasicDBObject> columnIds = columns.stream().map(it-> new BasicDBObject("columnId", it.getId())).collect(Collectors.toList());
            queries.add(new BasicDBObject("$or", columnIds));
        }
        if (columnCode != null && columnCode.trim().length() > 0) queries.add(new BasicDBObject("columnCode", columnCode.trim()));
        if (templateCode != null && templateCode.trim().length() > 0) queries.add(new BasicDBObject("templateCode", templateCode.trim()));
        if (authorId != null) queries.add(new BasicDBObject("authorId", authorId));
        if (title != null && title.trim().length() > 0) {
            Pattern pattern = Pattern.compile("^.*" + title.trim() + "8.*$", Pattern.CASE_INSENSITIVE);
            queries.add(new BasicDBObject("title", pattern));
        }
        if (begin != null) queries.add(new BasicDBObject("date", new BasicDBObject("$gte", begin)));
        if (end != null) queries.add(new BasicDBObject("date", new BasicDBObject("$lte", end)));
        return new BasicDBObject("$and", queries);
    }

    public static void normalizeContent(Iterable<Document> objects) {
        if (objects == null) return;
        for (Document dbObject : objects) {
            ObjectId id = (ObjectId) dbObject.get("_id");
            if (id != null) {
                dbObject.put("id", id.toString());
            }
            dbObject.remove("_id");
            dbObject.remove("_class");
        }
    }

    public void updateFocusCount(Collection<FocusCount> focusCounts) {
        for (FocusCount count : focusCounts) {
            BasicDBObject article = new BasicDBObject();
            if (count.getCommentCount() != null) article.put("commentCount", count.getCommentCount());
            if (count.getUpvoteCount() != null) article.put("upvoteCount", count.getUpvoteCount());
            if (count.getFavoriteCount() != null) article.put("favoriteCount", count.getFavoriteCount());
            update(count.getTenantId(), count.getArticleId(), (Bson) article);
        }
    }
}
