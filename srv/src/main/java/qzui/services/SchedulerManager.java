package qzui.services;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import qzui.domain.JobDescriptor;
import restx.factory.Component;

import java.util.Set;

@Component
public class SchedulerManager {

    private final Scheduler scheduler;

    public SchedulerManager(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public JobDescriptor addJob(String group, JobDescriptor jobDescriptor) {
        try {
            jobDescriptor.setGroup(group);
            Set<Trigger> triggers = jobDescriptor.buildTriggers();
            JobDetail jobDetail = jobDescriptor.toJobDetail();
            if (triggers.isEmpty()) {
                scheduler.addJob(jobDetail, false);
            } else {
                scheduler.scheduleJob(jobDetail, triggers, false);
            }
            return jobDescriptor;
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<JobKey> getJobKeys() {
        try {
            return scheduler.getJobKeys(GroupMatcher.anyJobGroup());
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<JobKey> getJobKeysByGroup(String group) {
        try {
            return scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group));
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteJob(String group, String name) {
        try {
            scheduler.deleteJob(new JobKey(name, group));
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
