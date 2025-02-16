package com.healthsystem.bigmoduleapi.quartzConfig;

import com.healthsystem.bigmoduleapi.job.HealthTipsJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail healthTipsJobDetail() {
        return JobBuilder.newJob(HealthTipsJob.class)
                .withIdentity("job1", "group1")
                .storeDurably()
                .build();
    }
//   凌晨0点执行
//    @Bean
//    public Trigger healthTipsTrigger() {
//        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 0 * * ?");
//
//        return TriggerBuilder.newTrigger()
//                .forJob(healthTipsJobDetail())
//                .withIdentity("trigger1", "triggerGroup1")
//                .startNow()
//                .withSchedule(cronScheduleBuilder)
//                .build();
//    }
    @Bean
        public Trigger healthTipsTrigger() {
            SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(86400) // 每隔一天执行一次
                .repeatForever();

            return TriggerBuilder.newTrigger()
                .forJob(healthTipsJobDetail())
                .withIdentity("trigger1", "triggerGroup1")
                .startNow()
                .withSchedule(scheduleBuilder)
                .build();
        }
}