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
 *         &lt;element name=&quot;comAppRegReturn&quot; 
 *         	type=&quot;{http://common.hotel.ws.ctcp.cts.com}AppRegData&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "comAppRegReturn" })
@XmlRootElement(name = "comAppRegResponse")
public class ComAppRegResponse {

    @XmlElement(required = true)
    protected AppRegData comAppRegReturn;

    /**
     * Gets the value of the comAppRegReturn property.
     * 
     * @return possible object is {@link AppRegData }
     * 
     */
    public AppRegData getComAppRegReturn() {
        return comAppRegReturn;
    }

    /**
     * Sets the value of the comAppRegReturn property.
     * 
     * @param value
     *            allowed object is {@link AppRegData }
     * 
     */
    public void setComAppRegReturn(AppRegData value) {
        this.comAppRegReturn = value;
    }

}
