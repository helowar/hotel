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
 *         &lt;element name=&quot;sApiKey&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;sHotelCode&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;sDateFm&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;sDateTo&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "sApiKey", "sHotelCode", "sDateFm", "sDateTo" })
@XmlRootElement(name = "enqClassNationAmt")
public class EnqClassNationAmt {

    @XmlElement(required = true)
    protected String sApiKey;

    @XmlElement(required = true)
    protected String sHotelCode;

    @XmlElement(required = true)
    protected String sDateFm;

    @XmlElement(required = true)
    protected String sDateTo;

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

    /**
     * Gets the value of the sHotelCode property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSHotelCode() {
        return sHotelCode;
    }

    /**
     * Sets the value of the sHotelCode property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSHotelCode(String value) {
        this.sHotelCode = value;
    }

    /**
     * Gets the value of the sDateFm property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSDateFm() {
        return sDateFm;
    }

    /**
     * Sets the value of the sDateFm property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSDateFm(String value) {
        this.sDateFm = value;
    }

    /**
     * Gets the value of the sDateTo property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSDateTo() {
        return sDateTo;
    }

    /**
     * Sets the value of the sDateTo property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSDateTo(String value) {
        this.sDateTo = value;
    }

}
