package com.beautifulsoup.chengfeng.tasks.scheduler;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;

import com.beautifulsoup.chengfeng.annotation.NotUsed;
import com.beautifulsoup.chengfeng.tasks.job.ChengfengJob;

@NotUsed
public class ChengfengScheduler {

    @Bean
    public JobDetail jobDetail(){
        return JobBuilder.newJob(ChengfengJob.class).withIdentity("chengfengJob").usingJobData("name","BeautifulSoup")
                .storeDurably().build();
    }

    @Bean
    public Trigger trigger(){
        SimpleScheduleBuilder scheduleBuilder=SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(5).repeatForever();
        return TriggerBuilder.newTrigger().forJob(jobDetail())
                .withIdentity("chengfengTrigger").withSchedule(scheduleBuilder).build();
    }



}
