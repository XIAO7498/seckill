package org.seckill.service.impl;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccesskilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * @author xiaofan
 * @date 2018/2/5 下午9:02
 */

@Service
public class SeckillServiceImpl implements SeckillService{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccesskilledDao successkilledDao;

    private final String slat = "dfhawhto3468rt21435907rwert*&^*%$%**($%&*(^jg";

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = getById(seckillId);
        if (seckill == null) {
            return new Exposer(false, seckillId);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();

        if (nowTime.getTime() < startTime.getTime()
                || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }

        String md5 = getMD5(seckillId);
        return new Exposer(true,md5,seckillId);
    }

    private String getMD5(long seckillId){
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {
        if(md5 == null || !md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }

        Date nowTime = new Date();
        int updateCount = seckillDao.reduceNumber(seckillId,nowTime);
        try {
            if(updateCount <= 0){
                throw new SeckillCloseException("seckill is closed");
            }else{
                int insertCount = successkilledDao.insertSuccessKilled(seckillId,userPhone);
                if(insertCount <= 0){
                    throw new RepeatKillException("seckill repeated");
                }else{
                    SuccessKilled successKilled = successkilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS,successKilled);
                }
            }
        }catch (SeckillCloseException e1){
            throw e1;
        }catch (RepeatKillException e2){
            throw e2;
        }
        catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }

    }
}
