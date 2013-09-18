package hk.com.cts.ctcp.hotel.webservice.enquiryservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for TxnDtlData complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;TxnDtlData&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base=&quot;{http://enquiry.hotel.ws.ctcp.cts.com}BasicData&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;NLine&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot;/&gt;
 *         &lt;element name=&quot;NListAmt&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}float&quot;/&gt;
 *         &lt;element name=&quot;NNetAmt&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}float&quot;/&gt;
 *         &lt;element name=&quot;SAmendStatus&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;SClassCode&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;SCustName&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;SDate&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;SHotelCode&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;SNationCode&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;SPrtFlg&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;STxnDate&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;STxnNo&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;SVoucher&quot; 
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
@XmlType(name = "TxnDtlData", propOrder = { "nLine", "nListAmt", "nNetAmt", "sAmendStatus",
    "sClassCode", "sCustName", "sDate", "sHotelCode", "sNationCode", "sPrtFlg", "sTxnDate",
    "sTxnNo", "sVoucher" })
public class TxnDtlData extends BasicData {
	
	private static final long serialVersionUID = 7198017952429499270L;

    @XmlElement(name = "NLine")
    protected int nLine;

    @XmlElement(name = "NListAmt")
    protected float nListAmt;

    @XmlElement(name = "NNetAmt")
    protected float nNetAmt;

    @XmlElement(name = "SAmendStatus", required = true, nillable = true)
    protected String sAmendStatus;

    @XmlElement(name = "SClassCode", required = true, nillable = true)
    protected String sClassCode;

    @XmlElement(name = "SCustName", required = true, nillable = true)
    protected String sCustName;

    @XmlElement(name = "SDate", required = true, nillable = true)
    protected String sDate;

    @XmlElement(name = "SHotelCode", required = true, nillable = true)
    protected String sHotelCode;

    @XmlElement(name = "SNationCode", required = true, nillable = true)
    protected String sNationCode;

    @XmlElement(name = "SPrtFlg", required = true, nillable = true)
    protected String sPrtFlg;

    @XmlElement(name = "STxnDate", required = true, nillable = true)
    protected String sTxnDate;

    @XmlElement(name = "STxnNo", required = true, nillable = true)
    protected String sTxnNo;

    @XmlElement(name = "SVoucher", required = true, nillable = true)
    protected String sVoucher;

    /**
     * Gets the value of the nLine property.
     * 
     */
    public int getNLine() {
        return nLine;
    }

    /**
     * Sets the value of the nLine property.
     * 
     */
    public void setNLine(int value) {
        this.nLine = value;
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
     * Gets the value of the nNetAmt property.
     * 
     */
    public float getNNetAmt() {
        return nNetAmt;
    }

    /**
     * Sets the value of the nNetAmt property.
     * 
     */
    public void setNNetAmt(float value) {
        this.nNetAmt = value;
    }

    /**
     * Gets the value of the sAmendStatus property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSAmendStatus() {
        return sAmendStatus;
    }

    /**
     * Sets the value of the sAmendStatus property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSAmendStatus(String value) {
        this.sAmendStatus = value;
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
     * Gets the value of the sCustName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSCustName() {
        return sCustName;
    }

    /**
     * Sets the value of the sCustName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSCustName(String value) {
        this.sCustName = value;
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
     * Gets the value of the sPrtFlg property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSPrtFlg() {
        return sPrtFlg;
    }

    /**
     * Sets the value of the sPrtFlg property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSPrtFlg(String value) {
        this.sPrtFlg = value;
    }

    /**
     * Gets the value of the sTxnDate property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSTxnDate() {
        return sTxnDate;
    }

    /**
     * Sets the value of the sTxnDate property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSTxnDate(String value) {
        this.sTxnDate = value;
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
     * Gets the value of the sVoucher property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSVoucher() {
        return sVoucher;
    }

    /**
     * Sets the value of the sVoucher property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSVoucher(String value) {
        this.sVoucher = value;
    }

}
