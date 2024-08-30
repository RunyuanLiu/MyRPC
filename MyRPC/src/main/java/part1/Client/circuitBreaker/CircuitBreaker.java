package part1.Client.circuitBreaker;

import org.checkerframework.checker.units.qual.C;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName CircuitBreaker
 * @Description 实现熔断逻辑
 * 1、失败N次后进行熔断：当失败次数达到阈值时，熔断器进入打开状态，拒绝请求
 * 2、打开状态持续x时间，在打开状态持续x时间后，熔断器进入半开状态，允许部分请求通过
 * 3、回复M%的请求：在半开状态下，熔断器允许请求通过，并根据请求的成功率决定是否恢复到闭合状态或者重新进入打开状态
 * 4、如果M%的请求都成功：恢复到闭合状态，正常处理请求
 * 5、否则再熔断Y时间，如果请求失败，则进入打开状态，等待Y时间，然后再次进入半开状态
 * @Author 氟西汀
 * @Date 2024/7/4 16:30
 * @Version 1.0
 */

public class CircuitBreaker {
//    当前状态
    private CircuitBreakerState state = CircuitBreakerState.CLOSED;
//    失败次数
    private AtomicInteger failure_Count = new AtomicInteger(0);
//    成功次数
    private AtomicInteger success_Count = new AtomicInteger(0);
//    请求次数
    private AtomicInteger request_Count = new AtomicInteger(0);
//    失败次数阈值
    private final int failureTheshold;
//    半开启->关闭状态的成功次数比例
    private final double halfOpenSuccessRate;
//    恢复时间
    private final int retryTimePeriod;
//    上一次失败时间
    private long lastFailureTime = 0;

    public CircuitBreaker(int failureTheshold, double halfOpenSuccessRate, int retryTimePeriod) {
        this.failureTheshold = failureTheshold;
        this.halfOpenSuccessRate = halfOpenSuccessRate;
        this.retryTimePeriod = retryTimePeriod;
    }
//    查看当前熔断器是否允许通过
    public synchronized boolean allowRequest(){
        long currTime = System.currentTimeMillis();
        switch (state) {
            case OPEN:
                if (currTime - lastFailureTime > retryTimePeriod) {
                    state = CircuitBreakerState.HALF_OPEN;
                    resetCounts();
                    return true;
                }
                return false;
            case HALF_OPEN:
                request_Count.incrementAndGet();
                return true;
            case CLOSED:
            default:
                return true;
        }
        }
    //        记录成功的次数
    public synchronized void recordSuccess(){
        if (state==CircuitBreakerState.HALF_OPEN){
            success_Count.incrementAndGet();
            if (success_Count.get()>=halfOpenSuccessRate*request_Count.get()){
                state = CircuitBreakerState.CLOSED;
                resetCounts();
            }
        }else {
            resetCounts();
        }
    }
    //        记录失败的次数
    public synchronized void recordFailture(){
        failure_Count.incrementAndGet();
        lastFailureTime = System.currentTimeMillis();
        if (state == CircuitBreakerState.HALF_OPEN){
            state = CircuitBreakerState.OPEN;
            lastFailureTime = System.currentTimeMillis();
        }else if (failure_Count.get()>failureTheshold){
            state = CircuitBreakerState.OPEN;
        }
    }
//    重置次数
    private void resetCounts(){
        failure_Count.set(0);
        success_Count.set(0);
        request_Count.set(0);
    }
    public CircuitBreakerState getState(){
        return state;
    }
}
enum CircuitBreakerState {
    CLOSED,OPEN,HALF_OPEN;
}