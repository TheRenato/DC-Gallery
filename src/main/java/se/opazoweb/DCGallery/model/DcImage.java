package se.opazoweb.DCGallery.model;

import javax.persistence.*;

@Entity
public class DcImage {

    @Id
    private String imageId;

    @ManyToOne(fetch=FetchType.LAZY)
    DcChannel dcChannel;

    private String fileName;
    private String fileUrl;
    private String uploadDate;

    public DcImage() {

    }

    public DcImage(String imageId, DcChannel dcChannel, String fileName, String fileUrl, String uploadDate) {
        this.imageId = imageId;
        this.dcChannel = dcChannel;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.uploadDate = uploadDate;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public DcChannel getDcChannel() {
        return dcChannel;
    }

    public void setDcChannel(DcChannel dcChannel) {
        this.dcChannel = dcChannel;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }
}