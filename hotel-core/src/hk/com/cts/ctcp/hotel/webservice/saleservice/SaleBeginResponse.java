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
 *         &lt;element name=&quot;saleBeginReturn&quot; 
 *         type=&quot;{http://sale.hotel.ws.ctcp.cts.com}BeginData&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "saleBeginReturn" })
@XmlRootElement(name = "saleBeginResponse")
public class SaleBeginResponse {

    @XmlElement(required = true)
    protected BeginData saleBeginReturn;

    /**
     * Gets the value of the saleBeginReturn property.
     * 
     * @return possible object is {@link BeginData }
     * 
     */
    public BeginData getSaleBeginReturn() {
        return saleBeginReturn;
    }

    /**
     * Sets the value of the saleBeginReturn property.
     * 
     * @param value
     *            allowed object is {@link BeginData }
     * 
     */
    public void setSaleBeginReturn(BeginData value) {
        this.saleBeginReturn = value;
    }

}
