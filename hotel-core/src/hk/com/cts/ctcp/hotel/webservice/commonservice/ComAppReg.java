package hk.com.cts.ctcp.hotel.webservice.commonservice;

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
 *         &lt;element name=&quot;sAgentCode&quot; 
 *         	type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "sAgentCode" })
@XmlRootElement(name = "comAppReg")
public class ComAppReg {

    @XmlElement(required = true)
    protected String sAgentCode;

    /**
     * Gets the value of the sAgentCode property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSAgentCode() {
        return sAgentCode;
    }

    /**
     * Sets the value of the sAgentCode property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSAgentCode(String value) {
        this.sAgentCode = value;
    }

}
