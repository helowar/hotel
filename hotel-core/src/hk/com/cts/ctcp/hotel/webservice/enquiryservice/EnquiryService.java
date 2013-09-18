package hk.com.cts.ctcp.hotel.webservice.enquiryservice;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;// parasoft-suppress UC.UIMPORT "必须引入"
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.1.3-hudson-390- Generated source version:
 * 2.0
 * 
 */
@WebService(name = "EnquiryService", targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com")
public interface EnquiryService {

    /**
     * 
     * @param sDateTo
     * @param sApiKey
     * @param sHotelCode
     * @param sDateFm
     * @return returns java.util.List<hk.com.cts.ctcp.hotel.webservice.enquiryService.ClassQtyData>
     */
    @WebMethod
    @WebResult(name = "enqClassQtyReturn", 
            targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com")
    @RequestWrapper(localName = "enqClassQty", 
            targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com", 
            className = "hk.com.cts.ctcp.hotel.webservice.enquiryservice.EnqClassQty")
    @ResponseWrapper(localName = "enqClassQtyResponse", 
            targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com", 
            className = "hk.com.cts.ctcp.hotel.webservice.enquiryservice.EnqClassQtyResponse")
    public List<ClassQtyData> enqClassQty(
        @WebParam(name = "sApiKey", 
                targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com") 
                String sApiKey,
        @WebParam(name = "sHotelCode", 
                targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com") 
                String sHotelCode,
        @WebParam(name = "sDateFm", 
                targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com") 
                String sDateFm,
        @WebParam(name = "sDateTo", 
                targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com") 
                String sDateTo);

    /**
     * 
     * @param sDateTo
     * @param sApiKey
     * @param sHotelCode
     * @param sDateFm
     * @return returns java.util.List
     *         <hk.com.cts.ctcp.hotel.webservice.enquiryService.ClassNationAmtData>
     */
    @WebMethod
    @WebResult(name = "enqClassNationAmtReturn", 
            targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com")
    @RequestWrapper(localName = "enqClassNationAmt", 
            targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com", 
            className = "hk.com.cts.ctcp.hotel.webservice.enquiryservice.EnqClassNationAmt")
    @ResponseWrapper(localName = "enqClassNationAmtResponse", 
            targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com", 
            className = "hk.com.cts.ctcp.hotel.webservice.enquiryservice.EnqClassNationAmtResponse")
    public List<ClassNationAmtData> enqClassNationAmt(
        @WebParam(name = "sApiKey", 
                targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com") 
                String sApiKey,
        @WebParam(name = "sHotelCode", 
                targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com") 
                String sHotelCode,
        @WebParam(name = "sDateFm", 
                targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com") 
                String sDateFm,
        @WebParam(name = "sDateTo", 
                targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com") 
                String sDateTo);

    /**
     * 
     * @param sClassCode
     * @param sDateTo
     * @param sApiKey
     * @param sHotelCode
     * @param sDateFm
     * @return returns java.util.List<hk.com.cts.ctcp.hotel.webservice.enquiryService.NationAmtData>
     */
    @WebMethod
    @WebResult(name = "enqNationAmtReturn", 
            targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com")
    @RequestWrapper(localName = "enqNationAmt", 
            targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com", 
            className = "hk.com.cts.ctcp.hotel.webservice.enquiryservice.EnqNationAmt")
    @ResponseWrapper(localName = "enqNationAmtResponse", 
            targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com", 
            className = "hk.com.cts.ctcp.hotel.webservice.enquiryservice.EnqNationAmtResponse")
    public List<NationAmtData> enqNationAmt(
        @WebParam(name = "sApiKey", 
                targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com") 
                String sApiKey,
        @WebParam(name = "sHotelCode", 
                targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com") 
                String sHotelCode,
        @WebParam(name = "sDateFm", 
                targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com") 
                String sDateFm,
        @WebParam(name = "sDateTo", 
                targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com") 
                String sDateTo,
        @WebParam(name = "sClassCode",
                targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com") 
                String sClassCode);

    /**
     * 
     * @param sApiKey
     * @param sTxnNo
     * @return returns java.util.List<hk.com.cts.ctcp.hotel.webservice.enquiryService.CustInfoData>
     */
    @WebMethod
    @WebResult(name = "enqCustInfoReturn", 
            targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com")
    @RequestWrapper(localName = "enqCustInfo", 
            targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com", 
            className = "hk.com.cts.ctcp.hotel.webservice.enquiryservice.EnqCustInfo")
    @ResponseWrapper(localName = "enqCustInfoResponse", 
            targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com", 
            className = "hk.com.cts.ctcp.hotel.webservice.enquiryservice.EnqCustInfoResponse")
    public List<CustInfoData> enqCustInfo(
        @WebParam(name = "sApiKey", 
                targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com") 
                String sApiKey,
        @WebParam(name = "sTxnNo", 
                targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com") 
                String sTxnNo);

    /**
     * 
     * @param sApiKey
     * @param sTxnNo
     * @return returns hk.com.cts.ctcp.hotel.webservice.enquiryService.TxnStatusData
     */
    @WebMethod
    @WebResult(name = "enqTxnStatusReturn", 
            targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com")
    @RequestWrapper(localName = "enqTxnStatus", 
            targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com", 
            className = "hk.com.cts.ctcp.hotel.webservice.enquiryservice.EnqTxnStatus")
    @ResponseWrapper(localName = "enqTxnStatusResponse", 
            targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com", 
            className = "hk.com.cts.ctcp.hotel.webservice.enquiryservice.EnqTxnStatusResponse")
    public TxnStatusData enqTxnStatus(
        @WebParam(name = "sApiKey", 
                targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com")
                String sApiKey,
        @WebParam(name = "sTxnNo", 
                targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com") 
                String sTxnNo);

    /**
     * 
     * @param sApiKey
     * @param sTxnNo
     * @return returns java.util.List<hk.com.cts.ctcp.hotel.webservice.enquiryService.TxnDtlData>
     */
    @WebMethod
    @WebResult(name = "enqTxnDtlReturn", 
            targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com")
    @RequestWrapper(localName = "enqTxnDtl",
            targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com", 
            className = "hk.com.cts.ctcp.hotel.webservice.enquiryservice.EnqTxnDtl")
    @ResponseWrapper(localName = "enqTxnDtlResponse", 
            targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com", 
            className = "hk.com.cts.ctcp.hotel.webservice.enquiryservice.EnqTxnDtlResponse")
    public List<TxnDtlData> enqTxnDtl(
        @WebParam(name = "sApiKey", 
                targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com") 
                String sApiKey,
        @WebParam(name = "sTxnNo", 
                targetNamespace = "http://enquiry.hotel.ws.ctcp.cts.com") 
                String sTxnNo);

}
