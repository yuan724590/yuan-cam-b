package yuan.cam.bb.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;

public class RedisHelper {

    //锁名称
    public static final String LOCK_PREFIX = "redis_lock";
    //加锁失效时间，毫秒
    public static final int LOCK_EXPIRE = 300; // ms

    @Autowired
    RedisTemplate redisTemplate;


    /**
     * 最终加强分布式锁
     * @param key key值
     * @return 是否获取到
     */
    public boolean lock(String key){
        String lock = LOCK_PREFIX + key;
        // 利用lambda表达式
        return (Boolean) redisTemplate.execute((RedisCallback) connection -> {

            long expireAt = System.currentTimeMillis() + LOCK_EXPIRE + 1;
            Boolean acquire = connection.setNX(lock.getBytes(), String.valueOf(expireAt).getBytes());


            if (acquire) {
                return true;
            } else {

                byte[] value = connection.get(lock.getBytes());

                if (Objects.nonNull(value) && value.length > 0) {

                    long expireTime = Long.parseLong(new String(value));
                    // 如果锁已经过期
                    if (expireTime < System.currentTimeMillis()) {
                        // 重新加锁，防止死锁
                        byte[] oldValue = connection.getSet(lock.getBytes(), String.valueOf(System.currentTimeMillis() + LOCK_EXPIRE + 1).getBytes());
                        return Long.parseLong(new String(oldValue)) < System.currentTimeMillis();
                    }
                }
            }
            return false;
        });
    }

    /**
     * 删除锁
     * @param key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
