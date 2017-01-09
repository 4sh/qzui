package qzui;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import restx.factory.*;

import static org.slf4j.LoggerFactory.getLogger;

@Module
public class QuartzModule {

    private static final Logger logger = getLogger(QuartzModule.class);

    @Provides
    public Scheduler quartzScheduler(Factory factory) throws SchedulerException {
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.setJobFactory((bundle, sched) -> {
            JobDetail jobDetail = bundle.getJobDetail();
            Class<? extends Job> jobClass = jobDetail.<Job>getJobClass();
            return factory.getComponent(Name.of(jobClass));
        });
        return scheduler;
    }

    @Provides
    public AutoStartable schedulerStarter(final Scheduler scheduler) {
        return () -> {
            try {
                logger.info("Starting quartz scheduler");
                scheduler.start();
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Provides
    public AutoCloseable schedulerCloser(final Scheduler scheduler) {
        return () -> {
            logger.info("Stopping quartz scheduler");
            scheduler.shutdown();
        };
    }
}
