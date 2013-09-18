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
 *         &lt;element name=&quot;sApiKey&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;sTxnNo&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;sHotelCode&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;sDate&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;sClassCode&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;sNationCode&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;nQty&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "sApiKey", "sTxnNo", "sHotelCode", "sDate", "sClassCode",
    "sNationCode", "nQty" })
@XmlRootElement(name = "saleAddItem")
public class SaleAddItem {

    @XmlElement(required = true)
    protected String sApiKey;

    @XmlElement(required = true)
    protected String sTxnNo;

    @XmlElement(required = true)
    protected String sHotelCode;

    @XmlElement(required = true)
    protected String sDate;

    @XmlElement(required = true)
    protected String sClassCode;

    @XmlElement(required = true)
    protected String sNationCode;

    protected int nQty;

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
     * Gets the value of the sDate property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSDate() {
        return sDate;
    }

    /**
     * Sets the value of the sDate property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSDate(String value) {
        this.sDate = value;
    }

    /**
     * Gets the value of the sClassCode property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSClassCode() {
        return sClassCode;
    }

    /**
     * Sets the value of the sClassCode property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSClassCode(String value) {
        this.sClassCode = value;
    }

    /**
     * Gets the value of the sNationCode property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSNationCode() {
        return sNationCode;
    }

    /**
     * Sets the value of the sNationCode property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSNationCode(String value) {
        this.sNationCode = value;
    }

    /**
     * Gets the value of the nQty property.
     * 
     */
    public int getNQty() {
        return nQty;
    }

    /**
     * Sets the value of the nQty property.
     * 
     */
    public void setNQty(int value) {
        this.nQty = value;
    }

}
