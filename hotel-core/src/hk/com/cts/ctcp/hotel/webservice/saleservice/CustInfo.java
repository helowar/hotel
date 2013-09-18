package hk.com.cts.ctcp.hotel.webservice.saleservice;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for CustInfo complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;CustInfo&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;NItemNo&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot;/&gt;
 *         &lt;element name=&quot;SName&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;SPhone&quot; 
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
@XmlType(name = "CustInfo", propOrder = { "nItemNo", "sName", "sPhone" })
public class CustInfo implements Serializable {
	
	private static final long serialVersionUID = -1224024761297826917L;

    @XmlElement(name = "NItemNo")
    protected int nItemNo;

    @XmlElement(name = "SName", required = true, nillable = true)
    protected String sName;

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
     * Gets the value of the sName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSName() {
        return sName;
    }

    /**
     * Sets the value of the sName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSName(String value) {
        this.sName = value;
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
