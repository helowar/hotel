package hk.com.cts.ctcp.hotel.webservice.saleservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;saleRollbackReturn&quot; 
 *         type=&quot;{http://sale.hotel.ws.ctcp.cts.com}BasicData&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "saleRollbackReturn" })
@XmlRootElement(name = "saleRollbackResponse")
public class SaleRollbackResponse {

    @XmlElement(required = true)
    protected BasicData saleRollbackReturn;

    /**
     * Gets the value of the saleRollbackReturn property.
     * 
     * @return possible object is {@link BasicData }
     * 
     */
    public BasicData getSaleRollbackReturn() {
        return saleRollbackReturn;
    }

    /**
     * Sets the value of the saleRollbackReturn property.
     * 
     * @param value
     *            allowed object is {@link BasicData }
     * 
     */
    public void setSaleRollbackReturn(BasicData value) {
        this.saleRollbackReturn = value;
    }

}
