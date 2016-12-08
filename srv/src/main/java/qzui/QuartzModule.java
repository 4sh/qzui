package qzui;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import restx.factory.AutoStartable;
import restx.factory.Module;
import restx.factory.Provides;

import static org.slf4j.LoggerFactory.getLogger;

@Module
public class QuartzModule {

    private static final Logger logger = getLogger(QuartzModule.class);

    @Provides
    public Scheduler scheduler() {
        try {
            return StdSchedulerFactory.getDefaultScheduler();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
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
