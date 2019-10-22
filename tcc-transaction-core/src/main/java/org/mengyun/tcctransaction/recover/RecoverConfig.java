package org.mengyun.tcctransaction.recover;

import java.util.Set;

/**
 * Created by changming.xie on 6/1/16.
 */
public interface RecoverConfig {

    /**
     * 最大重试次数
     */
    public int getMaxRetryCount();

    /**
     * 恢复间隔时间，单位：秒
     */
    public int getRecoverDuration();

    /**
     * cron 表达式
     */
    public String getCronExpression();

    /**
     * @return 延迟取消异常集合
     */
    public Set<Class<? extends Exception>> getDelayCancelExceptions();

    /**
     * 设置延迟取消异常集合
     *
     * @param delayRecoverExceptions 延迟取消异常集合
     */
    public void setDelayCancelExceptions(Set<Class<? extends Exception>> delayRecoverExceptions);

    public int getAsyncTerminateThreadPoolSize();
}
