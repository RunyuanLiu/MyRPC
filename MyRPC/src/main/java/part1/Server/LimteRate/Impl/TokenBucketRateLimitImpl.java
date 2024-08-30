package part1.Server.LimteRate.Impl;

import part1.Server.LimteRate.RateLimit;

/**
 * @ClassName TokenBucketRateLimiImpl
 * @Description TODO
 * @Author 氟西汀
 * @Date 2024/7/4 11:30
 * @Version 1.0
 */

public class TokenBucketRateLimitImpl implements RateLimit {
//    产生令牌的速度
    private static int RATE;
//    桶容量
    private static int CAPACITY;
//    当前桶容量
    private static int curCapacity;
//    时间戳
    private volatile long timeStamp = System.currentTimeMillis();
    public TokenBucketRateLimitImpl(int rate,int capacity){
        RATE = rate;
        CAPACITY = capacity;
        curCapacity = capacity;
    }

    @Override
    public synchronized boolean getToken() {
//        如果当前桶有剩余，则直接返回
        if (curCapacity>0){
            curCapacity --;
            return true;
        }
//        如果桶无剩余
        long current  = System.currentTimeMillis();
//        如果距离上一次请求的事件大于RATE的时间
        if (current-timeStamp>=RATE){
//            计算这段时间间隔中生成的令牌，如果大于2，桶容量加上（计算的令牌-1）
//            如果==1，就不做操作（因为这一次操作要消耗一个令牌）
            if((current-timeStamp)/RATE>=2){
                curCapacity += (int)(current-timeStamp)/RATE-1;
            }
//            确保桶内的令牌容量<=10
            if (curCapacity>CAPACITY) curCapacity = CAPACITY;
//            刷新时间戳
            timeStamp = current;
            return true;
        }
        return false;
    }
}
