package com.yoga.user.shiro;

import com.yoga.core.redis.RedisOperator;
import com.yoga.core.utils.SerializationUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class RedisSessionDAO extends CachingSessionDAO {
    private static Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);

    private static final String AUTHENTICATED_SESSION_KEY = "org.apache.shiro.dictionary.support.DefaultSubjectContext_AUTHENTICATED_SESSION_KEY";
    public static final String key = "shiro_redis_session";
    private static final String keyPrefix = key + ":";
    private String deleteChannel = "shiro_redis_session:delete";

    @Autowired
    private RedisOperator redisOperator;

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
        if (session == null || (session.getAttribute(AUTHENTICATED_SESSION_KEY) != null && !(Boolean) session.getAttribute(AUTHENTICATED_SESSION_KEY))) {
            session = doReadSession(sessionId);
            if (session == null) {
                //throw new UnknownSessionException("There is no session with id [" + sessionId + "]");
            }
            return session;
        }
        return session;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        String value = redisOperator.get(SerializationUtils.sessionKey(keyPrefix, sessionId));
        if (value != null) {
            Session session = SerializationUtils.sessionFromString(value);
            super.cache(session, session.getId());
            return session;
        }
        return null;
    }

    @Override
    protected void doUpdate(Session session) {
        if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
            return;
        }
        String key = SerializationUtils.sessionKey(keyPrefix, session);
        String value = SerializationUtils.sessionToString(session);
        long timeout = session.getTimeout();
        if (timeout <= 0) redisOperator.set(key, value);
        else redisOperator.set(key, value, timeout);
    }

    @Override
    protected void doDelete(Session session) {
        redisOperator.remove(SerializationUtils.sessionKey(keyPrefix, session));
        redisOperator.publish(deleteChannel, SerializationUtils.sessionIdToString(session));
        // 放在其它类里用一个 daemon 线程执行，删除 cache 中的 session
        // jedis.subscribe(new JedisPubSub() {
        //     @Override
        //     public void onMessage(String channel, String message) {
        //         // 1. deserialize message to sessionId
        //         // 2. Session session = getCachedSession(sessionId);
        //         // 3. uncache(session);
        //     }
        // }, deleteChannel);
    }

    public void clearSession() {
        redisOperator.remove(keyPrefix);
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