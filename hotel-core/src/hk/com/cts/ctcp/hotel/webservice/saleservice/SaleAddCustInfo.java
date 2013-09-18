package hk.com.cts.ctcp.hotel.webservice.saleservice;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name=&quot;aInfo&quot; 
 *         type=&quot;{http://sale.hotel.ws.ctcp.cts.com}CustInfo&quot; 
 *         maxOccurs=&quot;unbounded&quot;/&gt;
 *         &lt;element name=&quot;sRmk&quot; 
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
@XmlType(name = "", propOrder = { "sApiKey", "sTxnNo", "aInfo", "sRmk" })
@XmlRootElement(name = "saleAddCustInfo")
public class SaleAddCustInfo {

    @XmlElement(required = true)
    protected String sApiKey;

    @XmlElement(required = true)
    protected String sTxnNo;

    @XmlElement(required = true)
    protected List<CustInfo> aInfo;

    @XmlElement(required = true)
    protected String sRmk;

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
     * Gets the value of the aInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is
     * why there is not a <CODE>set</CODE> method for the aInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getAInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link CustInfo }
     * 
     * 
     */
    public List<CustInfo> getAInfo() {
        if (null == aInfo) {
            aInfo = new ArrayList<CustInfo>();
        }
        return this.aInfo;
    }

    /**
     * Gets the value of the sRmk property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSRmk() {
        return sRmk;
    }

    /**
     * Sets the value of the sRmk property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSRmk(String value) {
        this.sRmk = value;
    }

}
