package hk.com.cts.ctcp.hotel.webservice.enquiryservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for CustInfoData complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;CustInfoData&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base=&quot;{http://enquiry.hotel.ws.ctcp.cts.com}BasicData&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;NItemNo&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot;/&gt;
 *         &lt;element name=&quot;SCustName&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;SPhone&quot; 
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
@XmlType(name = "CustInfoData", propOrder = { "nItemNo", "sCustName", "sPhone" })
public class CustInfoData extends BasicData {
	
	private static final long serialVersionUID = 809930998504820746L;

    @XmlElement(name = "NItemNo")
    protected int nItemNo;

    @XmlElement(name = "SCustName", required = true, nillable = true)
    protected String sCustName;

    @XmlElement(name = "SPhone", required = true, nillable = true)
    protected String sPhone;

    /**
     * Gets the value of the nItemNo property.
     * 
     */
    public int getNItemNo() {
        return nItemNo;
    }

    /**
     * Sets the value of the nItemNo property.
     * 
     */
    public void setNItemNo(int value) {
        this.nItemNo = value;
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
     * Gets the value of the sPhone property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSPhone() {
        return sPhone;
    }

    /**
     * Sets the value of the sPhone property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSPhone(String value) {
        this.sPhone = value;
    }

}
