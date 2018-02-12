package org.seckill.exception;

/**
 * @author xiaofan
 * @date 2018/2/5 下午8:52
 */
public class RepeatKillException extends SeckillException{

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
