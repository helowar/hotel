package hk.com.cts.ctcp.hotel.webservice.enquiryservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ClassQtyData complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;ClassQtyData&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base=&quot;{http://enquiry.hotel.ws.ctcp.cts.com}BasicData&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;NMaxQty&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot;/&gt;
 *         &lt;element name=&quot;NMinNite&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot;/&gt;
 *         &lt;element name=&quot;NQty&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot;/&gt;
 *         &lt;element name=&quot;SClassCode&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;SClassDesc&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;SCutoff&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;SDate&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;SDupNameFlg&quot; 
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
@XmlType(name = "ClassQtyData", propOrder = { "nMaxQty", "nMinNite", "nQty", "sClassCode",
    "sClassDesc", "sCutoff", "sDate", "sDupNameFlg" })
public class ClassQtyData extends BasicData {

    @XmlElement(name = "NMaxQty")
    protected int nMaxQty;

    @XmlElement(name = "NMinNite")
    protected int nMinNite;

    @XmlElement(name = "NQty")
    protected int nQty;

    @XmlElement(name = "SClassCode", required = true, nillable = true)
    protected String sClassCode;

    @XmlElement(name = "SClassDesc", required = true, nillable = true)
    protected String sClassDesc;

    @XmlElement(name = "SCutoff", required = true, nillable = true)
    protected String sCutoff;

    @XmlElement(name = "SDate", required = true, nillable = true)
    protected String sDate;

    @XmlElement(name = "SDupNameFlg", required = true, nillable = true)
    protected String sDupNameFlg;

    /**
     * Gets the value of the nMaxQty property.
     * 
     */
    public int getNMaxQty() {
        return nMaxQty;
    }

    /**
     * Sets the value of the nMaxQty property.
     * 
     */
    public void setNMaxQty(int value) {
        this.nMaxQty = value;
    }

    /**
     * Gets the value of the nMinNite property.
     * 
     */
    public int getNMinNite() {
        return nMinNite;
    }

    /**
     * Sets the value of the nMinNite property.
     * 
     */
    public void setNMinNite(int value) {
        this.nMinNite = value;
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
     * Gets the value of the sClassDesc property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSClassDesc() {
        return sClassDesc;
    }

    /**
     * Sets the value of the sClassDesc property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSClassDesc(String value) {
        this.sClassDesc = value;
    }

    /**
     * Gets the value of the sCutoff property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSCutoff() {
        return sCutoff;
    }

    /**
     * Sets the value of the sCutoff property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSCutoff(String value) {
        this.sCutoff = value;
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
     * Gets the value of the sDupNameFlg property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSDupNameFlg() {
        return sDupNameFlg;
    }

    /**
     * Sets the value of the sDupNameFlg property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSDupNameFlg(String value) {
        this.sDupNameFlg = value;
    }

}
