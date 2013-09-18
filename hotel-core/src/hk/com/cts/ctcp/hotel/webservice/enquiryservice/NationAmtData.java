package hk.com.cts.ctcp.hotel.webservice.enquiryservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for NationAmtData complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;NationAmtData&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base=&quot;{http://enquiry.hotel.ws.ctcp.cts.com}BasicData&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;NBaseAmt&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}float&quot;/&gt;
 *         &lt;element name=&quot;NListAmt&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}float&quot;/&gt;
 *         &lt;element name=&quot;SDate&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;SNationCode&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;SNationDesc&quot; 
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
@XmlType(name = "NationAmtData", propOrder = { "nBaseAmt", "nListAmt", "sDate", "sNationCode",
    "sNationDesc" })
public class NationAmtData extends BasicData {

    @XmlElement(name = "NBaseAmt")
    protected float nBaseAmt;

    @XmlElement(name = "NListAmt")
    protected float nListAmt;

    @XmlElement(name = "SDate", required = true, nillable = true)
    protected String sDate;

    @XmlElement(name = "SNationCode", required = true, nillable = true)
    protected String sNationCode;

    @XmlElement(name = "SNationDesc", required = true, nillable = true)
    protected String sNationDesc;

    /**
     * Gets the value of the nBaseAmt property.
     * 
     */
    public float getNBaseAmt() {
        return nBaseAmt;
    }

    /**
     * Sets the value of the nBaseAmt property.
     * 
     */
    public void setNBaseAmt(float value) {
        this.nBaseAmt = value;
    }

    /**
     * Gets the value of the nListAmt property.
     * 
     */
    public float getNListAmt() {
        return nListAmt;
    }

    /**
     * Sets the value of the nListAmt property.
     * 
     */
    public void setNListAmt(float value) {
        this.nListAmt = value;
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
     * Gets the value of the sNationDesc property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSNationDesc() {
        return sNationDesc;
    }

    /**
     * Sets the value of the sNationDesc property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSNationDesc(String value) {
        this.sNationDesc = value;
    }

}
