package org.springframework.data.redis.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.util.StringUtils;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yoshidan on 2015/09/28.
 */
public class RedisState<K,V> implements InitializingBean, DisposableBean{

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisState.class);

    private static final String SYNCHRONIZING = "1";

    /** redis template . */
    private final RedisTemplate<K,V> redisTemplate;

    /** interval msec */
    private int interval = 5000;

    /** alive. */
    private boolean alive = true;

    /** lister. */
    private Timer timer = new Timer(true);

    /**
     * Constructor.
     *
     * @param redisTemplate to set
     */
    public RedisState(RedisTemplate<K, V> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    /**
     * @return RedisTemplate
     */
    public RedisTemplate<K,V> getRedisTemplate(){
        return this.redisTemplate;
    }

    /**
     * @param interval to set
     */
    public void setInterval(int interval) {
        this.interval = interval;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        timer.schedule(new Listener(),0,interval);
    }

    @Override
    public void destroy() throws Exception {
        timer.cancel();
    }

    /**
     * @return true alive / false dead
     */
    public boolean isAlive() {
        return alive;
    }

    private class Listener extends TimerTask {

        @Override
        public void run() {

            RedisCallbackFunction function = (connection) -> {
                try {
                    Properties prop = connection.info("replication");
                    String status = prop.getProperty("master_sync_in_progress");
                    boolean ready = ! SYNCHRONIZING.equals(status);
                    if(!ready){
                        LOGGER.error("slave full resync from master");
                    }
                    return ready;
                } catch (Throwable t) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.error("redis connection error : " + t.getMessage(), t);
                    } else {
                        LOGGER.error("redis connection error : " + t.getMessage());
                    }
                    return false;
                }
            };
            alive = redisTemplate.execute(function);

        }
    }

    @FunctionalInterface
    private interface RedisCallbackFunction extends RedisCallback<Boolean> {}



}
