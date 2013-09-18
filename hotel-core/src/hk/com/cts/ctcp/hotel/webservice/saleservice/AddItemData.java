package hk.com.cts.ctcp.hotel.webservice.saleservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for AddItemData complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;AddItemData&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base=&quot;{http://sale.hotel.ws.ctcp.cts.com}BasicData&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;NBaseAmt&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}float&quot;/&gt;
 *         &lt;element name=&quot;NItemNo&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot;/&gt;
 *         &lt;element name=&quot;NListAmt&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}float&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddItemData", propOrder = { "nBaseAmt", "nItemNo", "nListAmt" })
public class AddItemData extends BasicData {

    @XmlElement(name = "NBaseAmt")
    protected float nBaseAmt;

    @XmlElement(name = "NItemNo")
    protected int nItemNo;

    @XmlElement(name = "NListAmt")
    protected float nListAmt;

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

}
