/*Name: Yahya Angawi
  Student ID: D00233709
*/

package DTOs;

import java.util.Objects;

/**
 *
 * @author madwolff
 */
public class Billing {
    
    private String customerName;
    private double cost;

    public Billing(String customerName, double cost) {
        this.customerName = customerName;
        this.cost = cost;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + Objects.hashCode(this.customerName);
        hash = 19 * hash + (int) (Double.doubleToLongBits(this.cost) ^ (Double.doubleToLongBits(this.cost) >>> 32));
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
        final Billing other = (Billing) obj;
        if (Double.doubleToLongBits(this.cost) != Double.doubleToLongBits(other.cost)) {
            return false;
        }
        if (!Objects.equals(this.customerName, other.customerName)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Billing{" + "customerName=" + customerName + ", cost=" + cost + '}';
    }
    
    
}
