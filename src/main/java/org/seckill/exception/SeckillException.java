package org.seckill.exception;

/**
 * @author xiaofan
 * @date 2018/2/5 下午8:57
 */
public class SeckillException extends RuntimeException{

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
