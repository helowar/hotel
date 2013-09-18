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
 *         &lt;element name=&quot;saleCalAmtReturn&quot; 
 *         type=&quot;{http://sale.hotel.ws.ctcp.cts.com}CalAmtData&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "saleCalAmtReturn" })
@XmlRootElement(name = "saleCalAmtResponse")
public class SaleCalAmtResponse {

    @XmlElement(required = true)
    protected CalAmtData saleCalAmtReturn;

    /**
     * Gets the value of the saleCalAmtReturn property.
     * 
     * @return possible object is {@link CalAmtData }
     * 
     */
    public CalAmtData getSaleCalAmtReturn() {
        return saleCalAmtReturn;
    }

    /**
     * Sets the value of the saleCalAmtReturn property.
     * 
     * @param value
     *            allowed object is {@link CalAmtData }
     * 
     */
    public void setSaleCalAmtReturn(CalAmtData value) {
        this.saleCalAmtReturn = value;
    }

}
