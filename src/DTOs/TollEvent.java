/*Name: Yahya Angawi
  Student ID: D00233709
*/

package DTOs;

import java.time.Instant;
import java.util.Objects;


public class TollEvent
{
    private long imgId;
    private String reg;
    String timestamp;

    public TollEvent(long imgId, String reg, String timestamp) {
        this.imgId = imgId;
        this.setReg(reg);
        this.timestamp = timestamp;
    }

    public TollEvent(long imgId, String reg) {
        this.imgId = imgId;
        this.reg = reg;
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
        reg = reg.replace(" ", "");
        this.reg = reg;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (int) (this.imgId ^ (this.imgId >>> 32));
        hash = 61 * hash + Objects.hashCode(this.reg);
        hash = 61 * hash + Objects.hashCode(this.timestamp);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
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
        if (!Objects.equals(this.timestamp, other.timestamp)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TollEvent{" + "imgId=" + imgId + ", reg=" + reg + ", timestamp=" + timestamp + '}';
    }
}
