package org.springframework.data.redis.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.Observable;

/**
 * Created by yoshidan on 2015/09/28.
 */
public class RedisObservable<K,V> extends Observable implements InitializingBean, DisposableBean{

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisObservable.class);

    /** redis template . */
    private final RedisTemplate<K,V> redisTemplate;

    /** interval msec */
    private int checkInterval = 5000;

    /** lister thread. */
    private Listener listener = new Listener();

    /**
     * Constructor.
     *
     * @param redisTemplate to set
     */
    public RedisObservable(RedisTemplate<K,V> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    /**
     * @param checkInterval to set
     */
    public void setCheckInterval(int checkInterval) {
        this.checkInterval = checkInterval;
    }

    /**
     * @return checkInterval
     */
    protected int getCheckInterval(){
        return this.checkInterval;
    }

    /**
     * @return RedisTemplate
     */
    protected RedisTemplate<K,V> getRedisTemplate(){
        return this.redisTemplate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        listener.start();
    }

    @Override
    public void destroy() throws Exception {
        listener.running = false;
    }


    private class Listener extends Thread {

        private boolean alive = true;

        private boolean running = true;

        @Override
        public void run() {
            RedisConnectionFactory factory = getRedisTemplate().getConnectionFactory();
            while(running) {
                RedisConnection connection = RedisConnectionUtils.getConnection(factory);
                try {
                    connection.ping();
                    if( !alive ) {
                        setChanged();
                        alive = !alive;
                    }
                } catch (Throwable t) {
                    LOGGER.error("redis connection error : " + t.getMessage());
                    if(LOGGER.isDebugEnabled()){
                        LOGGER.debug(t.getMessage(),t);
                    }
                    if( alive ) {
                        setChanged();
                        alive = !alive;
                    }
                } finally {
                    RedisConnectionUtils.releaseConnection(connection, factory);
                    notifyObservers(alive);
                    sleep();
                }
            }
        }


        /**
         * Sleep
         */
        private void sleep(){
            try {
                Thread.sleep(getCheckInterval());
            }catch(InterruptedException e){
                LOGGER.warn(e.getMessage(),e);
            }
        }
    }



}
