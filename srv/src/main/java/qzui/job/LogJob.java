package qzui.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qzui.domain.JobDescriptor;
import qzui.domain.LogJobDescriptor;
import restx.factory.Component;

@Component
public class LogJob extends QuartzJob {

    private static final Logger logger = LoggerFactory.getLogger(LogJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("{} - {}", context.getJobDetail().getKey(),
                context.getJobDetail().getJobDataMap().getWrappedMap());
    }

    @Override
    public <T extends JobDescriptor> T buildDescriptor() {
        return (T) new LogJobDescriptor();
    }
}