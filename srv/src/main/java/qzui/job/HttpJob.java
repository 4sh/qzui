package qzui.job;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.base.Strings;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qzui.domain.HttpJobDescriptor;
import qzui.domain.JobDescriptor;
import restx.factory.Component;

import java.util.Optional;

@Component
public class HttpJob extends QuartzJob {

    private static final Logger logger = LoggerFactory.getLogger(HttpJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        HttpJobDescriptor descriptor = buildDescriptor().fillFromJobDetail(context.getJobDetail());
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
            String trustAllHosts = System.getProperty("http.ssl.trustAllHosts");
            if (httpConfiguration.isTrustAllHosts()
                    || (!Strings.isNullOrEmpty(trustAllHosts) && Boolean.parseBoolean(trustAllHosts))) {
                request.trustAllHosts();
            }
            String trustAllCerts = System.getProperty("http.ssl.trustAllCerts");
            if (httpConfiguration.isTrustAllCerts()
                    || (!Strings.isNullOrEmpty(trustAllCerts) && Boolean.parseBoolean(trustAllCerts))) {
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

    @Override
    public <T extends JobDescriptor> T buildDescriptor() {
        return (T) new HttpJobDescriptor();
    }
}
