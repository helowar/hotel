package hk.com.cts.ctcp.hotel.webservice.commonservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for AppRegData complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;AppRegData&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base=&quot;{http://common.hotel.ws.ctcp.cts.com}BasicData&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;SApiKey&quot; 
 *         	type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AppRegData", propOrder = { "sApiKey" })
public class AppRegData extends BasicData {

    @XmlElement(name = "SApiKey", required = true, nillable = true)
    protected String sApiKey;

    /**
     * Gets the value of the sApiKey property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSApiKey() {
        return sApiKey;
    }

    /**
     * Sets the value of the sApiKey property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSApiKey(String value) {
        this.sApiKey = value;
    }

}
