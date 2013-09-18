package hk.com.cts.ctcp.hotel.webservice.enquiryservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for TxnStatusData complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;TxnStatusData&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base=&quot;{http://enquiry.hotel.ws.ctcp.cts.com}BasicData&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;SStatus&quot;
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;STxnNo&quot;
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TxnStatusData", propOrder = { "sStatus", "sTxnNo" })
public class TxnStatusData extends BasicData {
	
	private static final long serialVersionUID = -3742100439768790709L;

    @XmlElement(name = "SStatus", required = true, nillable = true)
    protected String sStatus;

    @XmlElement(name = "STxnNo", required = true, nillable = true)
    protected String sTxnNo;

    /**
     * Gets the value of the sStatus property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSStatus() {
        return sStatus;
    }

    /**
     * Sets the value of the sStatus property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSStatus(String value) {
        this.sStatus = value;
    }

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
