package yuan.cam.b.util;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 描述: Twitter的分布式自增ID雪花算法snowflake (Java版)
 **/
@AllArgsConstructor
@Data
public class IdWorkerUtil {

    /**
     * 起始的时间戳
     */
    private final static long START_STMP = 1607155485176L;

    /**
     * 每一部分占用的位数
     */
    private final static long SEQUENCE_BIT = 12; //序列号占用的位数
    private final static long MACHINE_BIT = 5;   //机器标识占用的位数
    private final static long DATACENTER_BIT = 5;//数据中心占用的位数

    /**
     * 每一部分的最大值
     */
    private final static long MAX_DATACENTER_NUM = -1L ^ (-1L << DATACENTER_BIT);
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    /**
     * 数据中心
     */
    private static long datacenterId;

    /**
     * 机器标识
     */
    private static long machineId;

    /**
     * 控制序列号
     */
    private static long sequence = 0L;

    /**
     * 实际使用的序列号
     */
    private static long actualSequence = 0L;

    /**
     * 上一次时间戳
     */
    private static long lastStmp = -1L;

    private IdWorkerUtil(long dataCenterId, long machineId) {
        if (dataCenterId > MAX_DATACENTER_NUM || dataCenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.datacenterId = dataCenterId;
        this.machineId = machineId;
    }

    /**
     * 产生下一个ID
     * @return
     */
    public synchronized long nextId() {
        long currStmp = getNewstmp();
        if (currStmp < lastStmp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }
        /**
         * 时间不连续出来全是偶数
         */
        if (currStmp == lastStmp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStmp = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }
        // 上面那个控制序列号sequence，控制了一毫秒内不会超过MAX_SEQUENCE(4096)个，超过会等待，直到下一毫秒才会继续
        // 上面那个序列号sequence如果一毫秒只生成一个id，那么它永远都是0，那么取模永远都是0，插入的表也就可以理解为都是0表（或规律的那几张表），达不到均匀分布在各表的目的
        // 所以用下面这个序列号actualSequence来生成均匀的取模id，达到均匀分布在各表的目的
        actualSequence = (actualSequence + 1) & MAX_SEQUENCE;

        lastStmp = currStmp;

        return (currStmp - START_STMP) << TIMESTMP_LEFT //时间戳部分
                | datacenterId << DATACENTER_LEFT       //数据中心部分
                | machineId << MACHINE_LEFT             //机器标识部分
                | actualSequence;                             //序列号部分
    }

    private long getNextMill() {
        long mill = getNewstmp();
        while (mill <= lastStmp) {
            mill = getNewstmp();
        }
        return mill;
    }

    private long getNewstmp() {
        return System.currentTimeMillis();
    }

    private static IdWorkerUtil worker = new IdWorkerUtil(1,1);
    public static long getId() {
        return worker.nextId();
    }
}
