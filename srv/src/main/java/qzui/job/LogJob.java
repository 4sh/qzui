package qzui.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(LogJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("{} - {}", context.getJobDetail().getKey(),
                context.getJobDetail().getJobDataMap().getWrappedMap());
    }
}