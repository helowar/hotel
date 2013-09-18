package hk.com.cts.ctcp.hotel.webservice.enquiryservice;

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
 *         &lt;element name=&quot;enqTxnStatusReturn&quot; 
 *         type=&quot;{http://enquiry.hotel.ws.ctcp.cts.com}TxnStatusData&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "enqTxnStatusReturn" })
@XmlRootElement(name = "enqTxnStatusResponse")
public class EnqTxnStatusResponse {

    @XmlElement(required = true)
    protected TxnStatusData enqTxnStatusReturn;

    /**
     * Gets the value of the enqTxnStatusReturn property.
     * 
     * @return possible object is {@link TxnStatusData }
     * 
     */
    public TxnStatusData getEnqTxnStatusReturn() {
        return enqTxnStatusReturn;
    }

    /**
     * Sets the value of the enqTxnStatusReturn property.
     * 
     * @param value
     *            allowed object is {@link TxnStatusData }
     * 
     */
    public void setEnqTxnStatusReturn(TxnStatusData value) {
        this.enqTxnStatusReturn = value;
    }

}
