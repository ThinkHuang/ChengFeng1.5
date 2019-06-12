package com.beautifulsoup.chengfeng.listener;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import com.beautifulsoup.chengfeng.constant.RedisConstant;
import com.beautifulsoup.chengfeng.dao.CommunityNoticeMapper;
import com.beautifulsoup.chengfeng.dao.ProperNoticeMapper;
import com.beautifulsoup.chengfeng.pojo.CommunityNotice;
import com.beautifulsoup.chengfeng.pojo.ProperNotice;
import com.beautifulsoup.chengfeng.utils.JsonSerializableUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChengfengReadyListener implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("》》》》》》》》》》城风已就绪《《《《《《《《《《");
        CommunityNoticeMapper communityNoticeMapper = event.getApplicationContext().getBean(CommunityNoticeMapper.class);
        ProperNoticeMapper properNoticeMapper = event.getApplicationContext().getBean(ProperNoticeMapper.class);
        StringRedisTemplate stringRedisTemplate=event.getApplicationContext().getBean(StringRedisTemplate.class);
        List<CommunityNotice> communityNotices = communityNoticeMapper.selectAllCommunities();
        List<ProperNotice> properNotices = properNoticeMapper.selectAllPropers();
        ZSetOperations<String, String> zset = stringRedisTemplate.opsForZSet();
        HashOperations<String, Object, Object> hash = stringRedisTemplate.opsForHash();
        communityNotices.parallelStream()
                .forEach(communityNotice ->{
                    zset.add(RedisConstant.COMMUNITY_NOTICE_ORDER+communityNotice.getCommunityId(),RedisConstant.COMMUNITY_NOTICE_PREFIX+communityNotice.getId(),
                            new DateTime(communityNotice.getShowtime()).getMillis());
                    hash.put(RedisConstant.COMMUNITY_NOTICES+communityNotice.getCommunityId(),RedisConstant.COMMUNITY_NOTICE_PREFIX+communityNotice.getId(),
                            JsonSerializableUtil.obj2String(communityNotice));
                });
        properNotices.parallelStream()
                .forEach(properNotice -> {
                    zset.add(RedisConstant.PROPER_NOTICE_ORDER+properNotice.getUserId(),RedisConstant.PROPER_NOTICE_PREFIX+properNotice.getId(),
                            new DateTime(properNotice.getShowtime()).getMillis());
                    hash.put(RedisConstant.PROPER_NOTICES+properNotice.getUserId(),RedisConstant.PROPER_NOTICE_PREFIX+properNotice.getId(),
                            JsonSerializableUtil.obj2String(properNotice));
                });
    }
}
