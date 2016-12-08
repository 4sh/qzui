package qzui.domain;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import qzui.job.LogJob;

public class LogJobDescriptor extends JobDescriptor {

    @Override
    public JobDetail buildJobDetail() {
        return JobBuilder.newJob(LogJob.class)
                .withIdentity(getName(), getGroup())
                .usingJobData(new JobDataMap(getData()))
                .build();
    }
}