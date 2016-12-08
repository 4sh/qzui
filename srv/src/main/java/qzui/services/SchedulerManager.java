package qzui.services;

import com.google.common.base.Optional;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import qzui.domain.JobDefinition;
import qzui.domain.JobDescriptor;
import restx.factory.Component;

import java.util.Set;

@Component
public class SchedulerManager {

    private final Scheduler scheduler;
    private final Set<JobDefinition> definitions;

    public SchedulerManager(Scheduler scheduler, Set<JobDefinition> definitions) {
        this.scheduler = scheduler;
        this.definitions = definitions;
    }

    public JobDescriptor addJob(String group, JobDescriptor jobDescriptor) {
        try {
            jobDescriptor.setGroup(group);
            Set<Trigger> triggers = jobDescriptor.buildTriggers();
            JobDetail jobDetail = jobDescriptor.buildJobDetail();
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

    public Optional<JobDescriptor> getJob(String group, String name) {
        try {
            JobDetail jobDetail = scheduler.getJobDetail(new JobKey(name, group));
            if (jobDetail == null) {
                return Optional.absent();
            }
            for (JobDefinition definition : definitions) {
                if (definition.acceptJobClass(jobDetail.getJobClass())) {
                    return Optional.of(definition.buildDescriptor(
                            jobDetail, scheduler.getTriggersOfJob(jobDetail.getKey())));
                }
            }
            throw new IllegalStateException("can't find job definition for " + jobDetail
                    + " - available job definitions: " + definitions);
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
