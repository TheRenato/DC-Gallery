package se.opazoweb.DCGallery.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class DcChannel {

    @Id
    private String channelId;

    private String channelName;

    @ManyToOne(fetch=FetchType.LAZY)
    DcServer dcServer;

    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="dcChannel")
    private Set<DcImage> dcImages = new HashSet<>();

    public DcChannel(String channelId, String channelName, DcServer dcServer) {
        this.channelId = channelId;
        this.dcServer = dcServer;
        this.channelName = channelName;
    }

    public DcChannel() {

    }

    public DcServer getDcServer() {
        return dcServer;
    }

    public void setDcServer(DcServer dcServer) {
        this.dcServer = dcServer;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Set<DcImage> getDcImages() {
        return dcImages;
    }

    public void setDcImages(Set<DcImage> dcImages) {
        this.dcImages = dcImages;
    }

    public void addImage(DcImage newImage) {
        this.dcImages.add(newImage);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DcChannel dcChannel = (DcChannel) o;

        return channelId != null ? channelId.equals(dcChannel.channelId) : dcChannel.channelId == null;
    }

    @Override
    public int hashCode() {
        return channelId != null ? channelId.hashCode() : 0;
    }
}
