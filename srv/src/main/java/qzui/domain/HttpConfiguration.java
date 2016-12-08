package qzui.domain;

public class HttpConfiguration {

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
