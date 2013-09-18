package hk.com.cts.ctcp.hotel.webservice.commonservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for BasicData complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;BasicData&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;NRet&quot; 
 *       	  type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot;/&gt;
 *         &lt;element name=&quot;SMessage&quot; 
 *        	  type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BasicData", propOrder = { "nRet", "sMessage" })
public class BasicData {

    @XmlElement(name = "NRet")
    protected int nRet;

    @XmlElement(name = "SMessage", required = true, nillable = true)
    protected String sMessage;

    /**
     * Gets the value of the nRet property.
     * 
     */
    public int getNRet() {
        return nRet;
    }

    /**
     * Sets the value of the nRet property.
     * 
     */
    public void setNRet(int value) {
        this.nRet = value;
    }

    /**
     * Gets the value of the sMessage property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSMessage() {
        return sMessage;
    }

    /**
     * Sets the value of the sMessage property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSMessage(String value) {
        this.sMessage = value;
    }

}
