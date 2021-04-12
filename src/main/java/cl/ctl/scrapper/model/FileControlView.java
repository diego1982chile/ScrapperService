package cl.ctl.scrapper.model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by root on 30-03-21.
 */
public class FileControlView {

    String id;
    String logo;
    String client;
    String chain;
    String frequency;
    String filename;

    String status;

    String errorMsg;


    public FileControlView(String logo, String client, String chain, String frequency, String filename, String status) {
        this.id = UUID.randomUUID().toString();
        this.logo = logo;
        this.client = client;
        this.chain = chain;
        this.frequency = frequency;
        this.filename = filename;
        this.status = status;
    }

    public String getLogo() {
        return logo;
    }


    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileControlView that = (FileControlView) o;

        if (client != null ? !client.equals(that.client) : that.client != null) return false;
        if (chain != null ? !chain.equals(that.chain) : that.chain != null) return false;
        return frequency != null ? frequency.equals(that.frequency) : that.frequency == null;

    }

    @Override
    public int hashCode() {
        int result = client != null ? client.hashCode() : 0;
        result = 31 * result + (chain != null ? chain.hashCode() : 0);
        result = 31 * result + (frequency != null ? frequency.hashCode() : 0);
        return result;
    }
}
