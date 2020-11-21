package DTOs;

import java.time.Instant;
import java.util.Objects;


public class TollEvent
{
    private long imgId;
    private String reg;
    private Instant time;

    public TollEvent(long imgId, String reg) {
        this.imgId = imgId;
        this.reg = reg;
        this.time = time.now();
    }

    public long getImgId() {
        return imgId;
    }

    public void setImgId(long imgId) {
        this.imgId = imgId;
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + (int) (this.imgId ^ (this.imgId >>> 32));
        hash = 11 * hash + Objects.hashCode(this.reg);
        hash = 11 * hash + Objects.hashCode(this.time);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TollEvent other = (TollEvent) obj;
        if (this.imgId != other.imgId) {
            return false;
        }
        if (!Objects.equals(this.reg, other.reg)) {
            return false;
        }
        if (!Objects.equals(this.time, other.time)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TollEvent{" + "imgId=" + imgId + ", reg=" + reg + ", time=" + time + '}';
    }
    
}
