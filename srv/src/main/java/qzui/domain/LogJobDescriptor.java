package qzui.domain;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import qzui.job.LogJob;

public class LogJobDescriptor extends JobDescriptor {

    @Override
    public JobDetail toJobDetail() {
        return JobBuilder.newJob(LogJob.class)
                .withIdentity(getName(), getGroup())
                .build();
    }

    @Override
    public <T extends JobDescriptor> T fillFromJobDetail(JobDetail detail) {
        return (T) this;
    }
}