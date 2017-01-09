package qzui.services;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import qzui.domain.JobDescriptor;
import qzui.domain.QuartzJob;
import qzui.domain.TriggerDescriptor;
import restx.factory.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SchedulerManager {

    private final Scheduler scheduler;
    private final Set<QuartzJob> jobs;

    public SchedulerManager(Scheduler scheduler, Set<QuartzJob> jobs) {
        this.scheduler = scheduler;
        this.jobs = jobs;
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

    public Optional<JobDescriptor> getJobDescriptor(String group, String name) {
        try {
            JobKey jobKey = JobKey.jobKey(name, group);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            return jobs.stream()
                    .filter(job -> Objects.equals(job.getClass().getName(), jobDetail.getJobClass().getName()))
                    .findFirst()
                    .map((job) -> {
                        List<TriggerDescriptor> triggerDescriptor = triggers.stream()
                                .map(trigger -> new TriggerDescriptor().setWhen(trigger.getNextFireTime().toString()))
                                .collect(Collectors.toList());
                        return job.buildDescriptor()
                                .setGroup(group)
                                .setName(name)
                                .setTriggerDescriptors(triggerDescriptor)
                                .fillFromJobDetail(jobDetail);
                    });
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
