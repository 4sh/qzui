package qzui.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = HttpJobDescriptor.class, name = "http"),
        @JsonSubTypes.Type(value = LogJobDescriptor.class, name = "log")
})
public abstract class JobDescriptor {

    private String name;
    private String group;

    @JsonProperty("triggers")
    private List<TriggerDescriptor> triggerDescriptors = new ArrayList<>();

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public List<TriggerDescriptor> getTriggerDescriptors() {
        return triggerDescriptors;
    }

    public JobDescriptor setName(final String name) {
        this.name = name;
        return this;
    }

    public JobDescriptor setGroup(final String group) {
        this.group = group;
        return this;
    }

    public JobDescriptor setTriggerDescriptors(final List<TriggerDescriptor> triggerDescriptors) {
        this.triggerDescriptors = triggerDescriptors;
        return this;
    }

    @JsonIgnore
    public Set<Trigger> buildTriggers() {
        Set<Trigger> triggers = new LinkedHashSet<>();
        for (TriggerDescriptor triggerDescriptor : triggerDescriptors) {
            triggers.add(triggerDescriptor.buildTrigger());
        }
        return triggers;
    }

    /**
     * Build job details from descriptor which will be persisted into the configured JobStore.
     */

    public abstract JobDetail toJobDetail();

    /**
     * Fill a job descriptor from a persisted JobDetails (into the configured JobStore)
     */

    public abstract <T extends JobDescriptor> T fillFromJobDetail(JobDetail detail);

    @Override
    public String toString() {
        return "JobDescriptor{" +
                "name='" + name + '\'' +
                ", group='" + group + '\'' +
                ", triggerDescriptors=" + triggerDescriptors +
                '}';
    }
}
