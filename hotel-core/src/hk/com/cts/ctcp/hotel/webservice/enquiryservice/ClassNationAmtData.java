package hk.com.cts.ctcp.hotel.webservice.enquiryservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ClassNationAmtData complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;ClassNationAmtData&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base=&quot;{http://enquiry.hotel.ws.ctcp.cts.com}BasicData&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;NBaseAmt&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}float&quot;/&gt;
 *         &lt;element name=&quot;NListAmt&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}float&quot;/&gt;
 *         &lt;element name=&quot;SClassCode&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;SDate&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;SNation&quot; 
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
@XmlType(name = "ClassNationAmtData", propOrder = { "nBaseAmt", "nListAmt", "sClassCode", "sDate",
    "sNation", "sNationDesc" })
public class ClassNationAmtData extends BasicData {

    @XmlElement(name = "NBaseAmt")
    protected float nBaseAmt; // 香港酒店的底价(中科已不提供这个数据)

    @XmlElement(name = "NListAmt")
    protected float nListAmt; // 中科提供的他们底价(现在芒果网取这个作为底价)

    @XmlElement(name = "SClassCode", required = true, nillable = true)
    protected String sClassCode;

    @XmlElement(name = "SDate", required = true, nillable = true)
    protected String sDate;

    @XmlElement(name = "SNation", required = true, nillable = true)
    protected String sNation;

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
     * Gets the value of the sNation property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSNation() {
        return sNation;
    }

    /**
     * Sets the value of the sNation property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSNation(String value) {
        this.sNation = value;
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
