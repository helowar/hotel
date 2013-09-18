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
 *         &lt;element name=&quot;sTxnNo&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}
 *         string&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "sTxnNo" })
@XmlRootElement(name = "saleRollback")
public class SaleRollback {

    @XmlElement(required = true)
    protected String sTxnNo;

    /**
     * Gets the value of the sTxnNo property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSTxnNo() {
        return sTxnNo;
    }

    /**
     * Sets the value of the sTxnNo property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSTxnNo(String value) {
        this.sTxnNo = value;
    }

}
