package qzui.domain;

public class HttpConfiguration {

    public static final String TRUST_ALL_CERTS_PROPERTY = "http.ssl.trustAllCerts";
    public static final String TRUST_ALL_HOSTS_PROPERTY = "http.ssl.trustAllHosts";
    public static final String FOLLOW_REDIRECT_PROPERTY = "http.followRedirect";

    public static final String TRUST_ALL_CERTS_FIELD = "httpSSLTrustAllCerts";
    public static final String TRUST_ALL_HOSTS_FIELD = "httpSSLTrustAllHosts";
    public static final String FOLLOW_REDIRECT_FIELD = "httpFollowRedirect";

    private boolean trustAllCerts;

    private boolean trustAllHosts;

    private boolean followRedirect;

    public boolean isTrustAllCerts() {
        return trustAllCerts;
    }

    public boolean isTrustAllHosts() {
        return trustAllHosts;
    }

    public HttpConfiguration setTrustAllCerts(final boolean trustAllCerts) {
        this.trustAllCerts = trustAllCerts;
        return this;
    }

    public HttpConfiguration setTrustAllHosts(final boolean trustAllHosts) {
        this.trustAllHosts = trustAllHosts;
        return this;
    }

    public boolean isFollowRedirect() {
        return followRedirect;
    }

    public HttpConfiguration setFollowRedirect(final boolean followRedirect) {
        this.followRedirect = followRedirect;
        return this;
    }
}
