package se.opazoweb.DCGallery.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Set;

@Entity
public class DcServer {

    @Id
    private String serverId;
    private String serverName;

    @OneToMany(mappedBy = "SERVER_ID")
    private Set<DcChannel> dcChannels;

    public DcServer() {
    }

    public DcServer(String serverName, String serverId) {
        this.serverName = serverName;
        this.serverId = serverId;
    }

    public DcServer(String serverId, String serverName, Set<DcChannel> dcChannels) {
        this.serverId = serverId;
        this.serverName = serverName;
        this.dcChannels = dcChannels;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public Set<DcChannel> getDcChannels() {
        return dcChannels;
    }

    public void setDcChannels(Set<DcChannel> dcChannels) {
        this.dcChannels = dcChannels;
    }
}
