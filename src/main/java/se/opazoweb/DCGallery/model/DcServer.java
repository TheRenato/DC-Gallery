package se.opazoweb.DCGallery.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class DcServer {

    @Id
    private String id;
    private String serverName;

    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="dcServer")
    private Set<DcChannel> dcChannels = new HashSet<>();

    public DcServer() {
    }

    public DcServer(String serverName, String id) {
        this.serverName = serverName;
        this.id = id;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getid() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }

    public Set<DcChannel> getDcChannels() {
        return dcChannels;
    }

    public void setDcChannels(Set<DcChannel> dcChannels) {
        this.dcChannels = dcChannels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DcServer dcServer = (DcServer) o;

        return id != null ? id.equals(dcServer.id) : dcServer.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
