package com.yoga.admin.shiro;

import com.yoga.core.redis.RedisOperator;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class RedisSessionDAO extends CachingSessionDAO {
    private static Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);

    private static final String keyPrefix = "shiro_redis_session:";
    private static final String deleteChannel = "shiro_redis_session:delete";

    @Autowired
    private RedisOperator redisOperator;
    @Autowired
    private JedisPool jedisPool;

    @PostConstruct
    public void installSessionCleaner() {
        new Thread(()-> {
            jedisPool.getResource().subscribe(new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    try {
                        Serializable sessionId = SerializationUtils.sessionIdFromString(message);
                        if (sessionId == null) return;
                        Session session = getCachedSession(sessionId);
                        if (session == null) return;
                        uncache(session);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }, deleteChannel);
        }).start();
    }

    public static final String getRedisSessionKeyPrefix() {
        return keyPrefix;
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        assignSessionId(session, sessionId);
        String key = SerializationUtils.sessionKey(keyPrefix, session);
        String value = SerializationUtils.sessionToString(session);
        long timeout = session.getTimeout();
        if (timeout <= 0) redisOperator.set(key, value);
        else redisOperator.set(key, value, timeout);
        return sessionId;
    }

    @Override
    public Session readSession(Serializable sessionId) throws UnknownSessionException {
        Session session = getCachedSession(sessionId);
        if (session != null && session.getAttribute(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY) != null && (Boolean) session.getAttribute(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY)) {
            return session;
        }
        return doReadSession(sessionId);
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        try {
            String value = redisOperator.get(SerializationUtils.sessionKey(keyPrefix, sessionId));
            if (value != null) {
                Session session = SerializationUtils.sessionFromString(value);
                super.cache(session, session.getId());
                return session;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    protected void doUpdate(Session session) {
        if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) return;
        String key = SerializationUtils.sessionKey(keyPrefix, session);
        String value = SerializationUtils.sessionToString(session);
        long timeout = session.getTimeout();
        if (timeout <= 0) redisOperator.set(key, value);
        else redisOperator.set(key, value, timeout);
    }

    @Override
    protected void doDelete(Session session) {
        redisOperator.remove(SerializationUtils.sessionKey(keyPrefix, session));
        //TODO redis publish偶尔会卡死
        //jedisPool.getResource().publish(deleteChannel, SerializationUtils.sessionIdToString(session));
        uncache(session);
    }

    public void close(String sessionId) {
        try {
            redisOperator.remove(SerializationUtils.sessionKey(keyPrefix, sessionId));
            Session session = readSession(sessionId);
            if (session != null) {
                uncache(session);
                session.setTimeout(1);
                session.stop();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void close(Session session) {
        try {
            redisOperator.remove(SerializationUtils.sessionKey(keyPrefix, session.getId().toString()));
            uncache(session);
            session.setTimeout(1);
            session.stop();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<String> keys = redisOperator.keys(keyPrefix + "*");
        Collection<String> values = redisOperator.mget(keys);
        List<Session> sessions = new LinkedList<Session>();
        for (String value : values) {
            sessions.add(SerializationUtils.sessionFromString(value));
        }
        return sessions;
    }

    public void setRedisOperator(RedisOperator redisOperator) {
        this.redisOperator = redisOperator;
    }
}