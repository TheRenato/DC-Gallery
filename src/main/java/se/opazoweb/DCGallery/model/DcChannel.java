package se.opazoweb.DCGallery.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class DcChannel {

    @Id
    private String channelId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name="server_channel")
    private String serverId;


    @OneToMany(mappedBy = "CHANNEL_ID")
    private Set<DcImage> dcImages;

    public DcChannel(String serverId, String channelId) {
        this.serverId = serverId;
        this.channelId = channelId;
    }

    public DcChannel() {

    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
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
}
