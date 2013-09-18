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
 *         &lt;element name=&quot;saleAddItemReturn&quot; 
 *         type=&quot;{http://sale.hotel.ws.ctcp.cts.com}AddItemData&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "saleAddItemReturn" })
@XmlRootElement(name = "saleAddItemResponse")
public class SaleAddItemResponse {

    @XmlElement(required = true)
    protected AddItemData saleAddItemReturn;

    /**
     * Gets the value of the saleAddItemReturn property.
     * 
     * @return possible object is {@link AddItemData }
     * 
     */
    public AddItemData getSaleAddItemReturn() {
        return saleAddItemReturn;
    }

    /**
     * Sets the value of the saleAddItemReturn property.
     * 
     * @param value
     *            allowed object is {@link AddItemData }
     * 
     */
    public void setSaleAddItemReturn(AddItemData value) {
        this.saleAddItemReturn = value;
    }

}
