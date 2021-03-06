package edu.gvsu.cis.campbjos.imgine.common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static java.lang.String.format;

public class Host {

    @SerializedName("ip")
    @Expose
    public final String ip;
    @SerializedName("port")
    @Expose
    public final Integer port;
    @SerializedName("username")
    public final String username;

    private Host(String ip, Integer port, String username) {
        this.ip = ip;
        this.port = port;
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Host host = (Host) o;

        if (ip != null ? !ip.equals(host.ip) : host.ip != null) return false;
        if (port != null ? !port.equals(host.port) : host.port != null) return false;
        return username != null ? username.equals(host.username) : host.username == null;

    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + (port != null ? port.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return format("%s@%s:%s", username, ip, port);
    }

    public static class Builder {
        private String ip;
        private Integer port;
        private String username;

        public Builder setIp(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder setPort(Integer port) {
            this.port = port;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Host createHost() {
            return new Host(ip, port, username);
        }
    }
}
