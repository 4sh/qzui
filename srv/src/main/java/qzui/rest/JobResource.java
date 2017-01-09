package qzui.rest;

import org.quartz.JobKey;
import qzui.domain.JobDescriptor;
import qzui.services.SchedulerManager;
import restx.annotations.DELETE;
import restx.annotations.GET;
import restx.annotations.POST;
import restx.annotations.RestxResource;
import restx.factory.Component;

import java.util.Set;

@RestxResource
@Component
public class JobResource {

    private final SchedulerManager schedulerManager;

    public JobResource(SchedulerManager schedulerManager) {
        this.schedulerManager = schedulerManager;
    }

    @POST("/groups/{group}/jobs")
    public JobDescriptor addJob(String group, JobDescriptor jobDescriptor) {
        return schedulerManager.addJob(group, jobDescriptor);
    }

    @GET("/jobs")
    public Set<JobKey> getJobKeys() {
        return schedulerManager.getJobKeys();
    }

    @GET("/groups/{group}/jobs")
    public Set<JobKey> getJobKeysByGroup(String group) {
        return schedulerManager.getJobKeysByGroup(group);
    }

    @DELETE("/groups/{group}/jobs/{name}")
    public void deleteJob(String group, String name) {
        schedulerManager.deleteJob(group, name);
    }
}
