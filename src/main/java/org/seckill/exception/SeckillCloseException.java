package org.seckill.exception;

/**
 * @author xiaofan
 * @date 2018/2/5 下午8:54
 */
public class SeckillCloseException extends SeckillException{

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
