package hk.com.cts.ctcp.hotel.webservice.saleservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for CalAmtData complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;CalAmtData&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base=&quot;{http://sale.hotel.ws.ctcp.cts.com}BasicData&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;NNetAmt&quot; 
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
@XmlType(name = "CalAmtData", propOrder = { "nNetAmt" })
public class CalAmtData extends BasicData {
	
	private static final long serialVersionUID = 9027800791536671400L;

    @XmlElement(name = "NNetAmt")
    protected float nNetAmt;

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

}
