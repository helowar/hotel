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
 *         &lt;element name=&quot;saleAddCustInfoReturn&quot; 
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
@XmlType(name = "", propOrder = { "saleAddCustInfoReturn" })
@XmlRootElement(name = "saleAddCustInfoResponse")
public class SaleAddCustInfoResponse {

    @XmlElement(required = true)
    protected BasicData saleAddCustInfoReturn;

    /**
     * Gets the value of the saleAddCustInfoReturn property.
     * 
     * @return possible object is {@link BasicData }
     * 
     */
    public BasicData getSaleAddCustInfoReturn() {
        return saleAddCustInfoReturn;
    }

    /**
     * Sets the value of the saleAddCustInfoReturn property.
     * 
     * @param value
     *            allowed object is {@link BasicData }
     * 
     */
    public void setSaleAddCustInfoReturn(BasicData value) {
        this.saleAddCustInfoReturn = value;
    }

}
