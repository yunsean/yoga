package com.yoga.admin.shiro;

import com.yoga.core.redis.RedisOperator;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Configuration
public class ShiroConfiguration {
    private static Logger logger = LoggerFactory.getLogger(ShiroConfiguration.class);

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(ApplicationContext applicationContext, SessionManager sessionManager, SubjectFactory subjectFactory, Authenticator authenticator) {
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        dwsm.setAuthenticator(authenticator);
        dwsm.setRealms(allRealms(applicationContext));
        dwsm.setCacheManager(getEhCacheManager());
        dwsm.setSessionManager(sessionManager);
        dwsm.setSubjectFactory(subjectFactory);
        return dwsm;
    }

    private List<Realm> allRealms(ApplicationContext applicationContext) {
        List<Realm> realms = new ArrayList<>();
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(Realm.class));
        logger.info("查找Shiro授权服务");
        Set<BeanDefinition> definitions = provider.findCandidateComponents("com.yoga.**");
        for (BeanDefinition definition : definitions) {
            try {
                String beanName = definition.getBeanClassName();
                Class c = Class.forName(beanName);
                Realm realm = (Realm) applicationContext.getBean(c);
                realms.add(realm);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        logger.info("查找Shiro授权服务结束");
        return realms;
    }

    @Bean(name = "subjectFactory")
    public DefaultWebSubjectFactory getDefaultWebSubjectFactory() {
        return new DefaultWebSubjectFactory() {
            @Override
            public Subject createSubject(SubjectContext context) {
                return super.createSubject(context);
            }
        };
    }

    @Bean
    public RedisSessionDAO getRedisSessionDAO(RedisOperator redisOperator) {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisOperator(redisOperator);
        return redisSessionDAO;
    }

    @Autowired
    @Bean(name = "sessionManager")
    public DefaultWebSessionManager getDefaultWebSessionManager(RedisOperator redisOperator) {
        DefaultWebSessionManager dwsm = new DefaultWebSessionManager() {
            @Override
            public Serializable getSessionId(SessionKey key) {
                Serializable id = null;
                if (WebUtils.isWeb(key)) {
                    ServletRequest request = WebUtils.getRequest(key);
                    if (request instanceof HttpServletRequest) {
                        id = ((HttpServletRequest) request).getHeader("token");
                        if (id == null) id = ((HttpServletRequest) request).getParameter("token");
                    }
                }
                if (id == null)id = super.getSessionId(key);
                return id;
            }
        };
        RedisSessionDAO sessionDAO = this.getRedisSessionDAO(redisOperator);
        dwsm.setSessionDAO(sessionDAO);
        return dwsm;
    }

    @Bean
    public EhCacheManager getEhCacheManager() {
        EhCacheManager em = new EhCacheManager();
        em.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return em;
    }

    @Bean(name = "authenticator")
    public ModularRealmAuthenticator getAuthenticator() {
        return new MultiRealmAuthenticator();
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setOrder(Integer.MIN_VALUE);
        return filterRegistration;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(securityManager);
        return aasa;
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new FilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/admin/toLogin");
        shiroFilterFactoryBean.setSuccessUrl("/admin");
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        Map<String, String> filterChainDefinitionMap = new ShiroXMLReader().filterChainDefinitionMap("/shiro-conf.xml");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

}
