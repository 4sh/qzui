package qzui.job;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.base.Strings;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qzui.domain.HttpJobDescriptor;

import java.util.Optional;

public class HttpJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(HttpJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        HttpJobDescriptor descriptor = new HttpJobDescriptor().fillFromJobDetail(context.getJobDetail());
        HttpRequest request = new HttpRequest(descriptor.getUrl(), descriptor.getMethod());

        String body = notNullOrEmpty(descriptor.getBody()).orElse("");
        notNullOrEmpty(descriptor.getContentType()).ifPresent(request::contentType);
        setCrendentials(descriptor, request);
        applyHttpConfiguration(descriptor, request);

        request.send(body);
        int code = request.code();
        String responseBody = request.body();

        logger.info("{} {} => {}\n{}", descriptor.getMethod(), descriptor.getUrl(), code, responseBody);
    }

    private void applyHttpConfiguration(HttpJobDescriptor descriptor, HttpRequest request) {
        Optional.ofNullable(descriptor.getHttpConfiguration()).ifPresent((httpConfiguration) -> {
            if (httpConfiguration.isTrustAllHosts()) {
                request.trustAllHosts();
            }
            if (httpConfiguration.isTrustAllCerts()) {
                request.trustAllCerts();
            }
            request.followRedirects(httpConfiguration.isFollowRedirect());
        });
    }

    private void setCrendentials(HttpJobDescriptor descriptor, HttpRequest request) {
        if (!Strings.isNullOrEmpty(descriptor.getLogin()) && !Strings.isNullOrEmpty(descriptor.getPwdHash())) {
            request.basic(descriptor.getLogin(), descriptor.getPwdHash());
        }
    }

    private Optional<String> notNullOrEmpty(String str) {
        if (Strings.isNullOrEmpty(str)) {
            return Optional.empty();
        }
        return Optional.of(str);
    }
}
