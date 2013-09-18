package com.mangocity.hotel.ejb.search.construct;

import java.util.Date;
import java.util.List;
import java.util.Map;


import com.mangocity.hotel.info.search.service.api.HotelCommonQueryRemote;
import com.mangocity.hotel.info.search.service.api.HotelSearchRemote;
import com.mangocity.hotel.info.search.service.api.HotelSortAndFilterRemote;
import com.mangocity.hotel.info.search.service.api.QuotationQueryRemote;
import com.mangocity.hotel.search.comm.AbstractConstructHotel;
import com.mangocity.hotel.search.dto.AddBedDTO;
import com.mangocity.hotel.search.dto.AdditionalCompDTO;
import com.mangocity.hotel.search.dto.AlerttypeInfoDTO;
import com.mangocity.hotel.search.dto.AroundInfoCompDTO;
import com.mangocity.hotel.search.dto.CommentSummaryDTO;
import com.mangocity.hotel.search.dto.GeographicalpositionDTO;
import com.mangocity.hotel.search.dto.HotelInfoDTO;
import com.mangocity.hotel.search.dto.HotelQuotationDTO;
import com.mangocity.hotel.search.dto.HotelQuotationQueryInfoDTO;
import com.mangocity.hotel.search.dto.HotelSummaryInfoDTO;
import com.mangocity.hotel.search.dto.HtlBaseQueryDTO;
import com.mangocity.hotel.search.dto.HtlChargeBreakfastDTO;
import com.mangocity.hotel.search.dto.HtlChildWelcomePriceDTO;
import com.mangocity.hotel.search.dto.HtlQuotationQueryDTO;
import com.mangocity.hotel.search.dto.HtlSortAndFilterDTO;
import com.mangocity.hotel.search.dto.PaginationSupport;
import com.mangocity.hotel.search.dto.RoomTypeDTO;
import com.mangocity.hotel.search.dto.SaleInfoCompDTO;
import com.mangocity.hotel.search.dto.SalesPromoDTO;
import com.mangocity.hotel.search.util.DateUtil;
import com.mangocity.hotel.search.util.HotelHandlerUtil;
import com.mangocity.hotel.ejb.search.construct.WebHotelStubCombinationTool;
import com.mangocity.hotel.search.util.HotelStubUtil;
import java.util.Collections;
import java.util.HashMap;


public class WebConstructHotel extends AbstractConstructHotel
{
  private HotelSearchRemote hotelSearchRemote = null;

  private HotelSortAndFilterRemote sortAndFilterRemote = null;

  private QuotationQueryRemote quotationQueryRemote = null;

  private HotelCommonQueryRemote hotelCommonQueryRemote = null;

  protected PaginationSupport support = null;

  protected List<HotelQuotationQueryInfoDTO> quotationList = Collections.emptyList();

  protected List<HotelInfoDTO> hotelInfoList = null;

  protected Map<String, HotelInfoDTO> hotelMap = new HashMap();

  protected Map<Long, RoomTypeDTO> roomTypeMap = new HashMap();

  protected HtlQuotationQueryDTO quotationQueryDTO = null;

  private boolean commentSummaryNeed = false;

  private boolean roomtypeListNeed = false;

  private boolean additionalNeed = false;

  private boolean saleNeed = false;

  private boolean aroundInfoNeed = false;

  private List<CommentSummaryDTO> summaryList = null;

  private List<Object[]> preSaleList = null;

  private List<SalesPromoDTO> salepromosList = null;

  private List<AlerttypeInfoDTO> arlertInfoList = null;

  private GeographicalpositionDTO geographicalpositionDTO = null;

  private AroundInfoCompDTO aroundInfoCompDTO = null;

  private Map<String, List<HtlChildWelcomePriceDTO>> welcomePriceMap = null;

  private Map<String, List<HtlChargeBreakfastDTO>> addBreakFastMap = null;

  private Map<String, Map<String, List<AddBedDTO>>> addRoomBedMap = null;

  protected List<String> resultList = null;

  protected int hotelCount = 0;

  public void informationNeed()
  {
    if (HotelStubUtil.IsRequest(this.requestMap, HotelStubUtil.HotelConfigProperties.ROOMTYPELISTSERVER.name()))
    {
      this.roomtypeListNeed = true;
      queryQuotation();
    }

    if (HotelStubUtil.IsRequest(this.requestMap, HotelStubUtil.HotelConfigProperties.COMMENTSUMMARYSERVER.name()))
    {
      this.commentSummaryNeed = true;
      this.summaryList = this.hotelCommonQueryRemote.getHotelComments(this.resultList);
    }

    if (HotelStubUtil.IsRequest(this.requestMap, HotelStubUtil.HotelConfigProperties.SALESERVER.name()))
    {
      this.saleNeed = true;
      if (this.quotationQueryDTO != null) {
        SaleInfoCompDTO saleInfoCompDTO = this.hotelCommonQueryRemote.querySaleInfo(this.quotationQueryDTO);
        if (null != saleInfoCompDTO) {
          this.arlertInfoList = saleInfoCompDTO.getArlerttypeInfoList();
          this.preSaleList = saleInfoCompDTO.getPreSalesList();
          this.salepromosList = saleInfoCompDTO.getSalePromosList();
        }

      }

    }

    if (HotelStubUtil.IsRequest(this.requestMap, HotelStubUtil.HotelConfigProperties.AROUNDINFOSERVER.name()))
    {
      this.aroundInfoNeed = true;
      this.aroundInfoCompDTO = this.hotelCommonQueryRemote.queryAroundInfo(this.resultList, (String)this.queryDTO.getCityCodeList().get(0));
    }

    if (HotelStubUtil.IsRequest(this.requestMap, HotelStubUtil.HotelConfigProperties.ADDITIONALSERVER.name()))
    {
      this.additionalNeed = true;
      AdditionalCompDTO additionalCompDTO = this.hotelCommonQueryRemote.queryAdditionalInfo(this.quotationQueryDTO);
      if (null != additionalCompDTO) {
        this.addBreakFastMap = additionalCompDTO.getAdBreakfastInfoMap();
        this.addRoomBedMap = additionalCompDTO.getAddBedInfoMap();
        this.welcomePriceMap = additionalCompDTO.getWelcomePriceInfoMap();
      }
    }
  }

  public void combinationHotel()
  {
    this.handler.init();

    this.handler.handleTotalCount(this.hotelCount);

    if ((null == this.resultList) || (this.resultList.size() == 0)) return;

    informationNeed();

    if (null != this.hotelInfoList) {
      for (int index = 0; index < this.hotelInfoList.size(); index++) {
        HotelInfoDTO tempHotelDTO = (HotelInfoDTO)this.hotelInfoList.get(index);
        this.hotelMap.put(tempHotelDTO.getHotelId(), tempHotelDTO);

        for (RoomTypeDTO roomtype : tempHotelDTO.getRoomTypeList().values()) {
          if (roomtype.getRoomTypeId() != 0L) {
            this.roomTypeMap.put(Long.valueOf(roomtype.getRoomTypeId()), roomtype);
            if (this.additionalNeed) HotelStubUtil.setAddBed(this.addRoomBedMap, roomtype);
          }
        }
        if (this.commentSummaryNeed)
          HotelStubUtil.setCommentToHotel(this.summaryList, tempHotelDTO);
        if (this.saleNeed) {
          HotelStubUtil.setPreSalesToHotel(this.preSaleList, tempHotelDTO);
        }
        if (this.aroundInfoNeed) {
          HotelStubUtil.setAroundHotel(this.aroundInfoCompDTO.getAroundHotelMap(), tempHotelDTO);
          HotelStubUtil.setAroundView(this.aroundInfoCompDTO.getAroundViewMap(), tempHotelDTO);
          HotelStubUtil.setAroundTraffic(this.aroundInfoCompDTO.getAroundTrafficlMap(), tempHotelDTO);
        }

        if (this.additionalNeed) {
          HotelStubUtil.setWelcomePrice(this.welcomePriceMap, tempHotelDTO);
          HotelStubUtil.setBreakFast(this.addBreakFastMap, tempHotelDTO);
        }

        tempHotelDTO.setHotelRank(this.resultList.indexOf(tempHotelDTO.getHotelId()));
        tempHotelDTO.setCheckInDate(this.queryDTO.getCheckInDate().toString());
        tempHotelDTO.setCheckOutDate(this.queryDTO.getCheckOutDate().toString());
        tempHotelDTO.setGeographicalpositionDTO(this.geographicalpositionDTO);
        this.handler.handleHotelInfo(tempHotelDTO);
      }

      this.handler.endHandleQueryHotel();
      this.summaryList = null;
      this.preSaleList = null;
    }

    if (this.roomtypeListNeed)
    {
      Date checkInDate = this.queryDTO.getCheckInDate();

      int totalDays = DateUtil.getDay(checkInDate, this.queryDTO.getCheckOutDate());

      long curHotelId = 0L;

      HotelInfoDTO curHotel = null;

      long curRoomTypeId = 0L;

      RoomTypeDTO curRoom = null;

      String curQuotationId = "";

      HotelQuotationDTO curComm = null;

      boolean bFirst = true;

      boolean[] bedFill = { false, false, false };
      if (this.quotationList == null) return;

      for (HotelQuotationQueryInfoDTO tempQuotationDTO : this.quotationList)
      {
        long newHotelId = tempQuotationDTO.getHotelId().longValue();

        long newRoomTypeId = tempQuotationDTO.getRoomtypeId().longValue();

        String newQuotationId = tempQuotationDTO.getCommodityId() + "_" + tempQuotationDTO.getPaymethod();

        if (curHotelId != newHotelId)
        {
          if (!bFirst)
          {
            WebHotelStubCombinationTool.finishFillSaleInfos(curComm, totalDays, bedFill, checkInDate, this.handler);

            WebHotelStubCombinationTool.finishFillCommodity(curComm, curRoom, curHotel, this.quotationQueryDTO, this.handler);

            WebHotelStubCombinationTool.finishFillRoomInfo(curRoom, curHotel, this.handler);

            if (null == curHotel) return;

            HotelSummaryInfoDTO curHotelSumInfo = new HotelSummaryInfoDTO();
            curHotelSumInfo.setHotelId(Long.valueOf(Long.parseLong(curHotel.getHotelId())));
            curHotelSumInfo.setCanbook(curHotel.isCanbook());
            curHotelSumInfo.setLowestPrice(curHotel.getLowestPrice());
            curHotelSumInfo.setHighestPrice(curHotel.getHighestPrice());
            curHotelSumInfo.setQuotationId(curHotel.getQuotationId());
            if (curHotel.isHasBreakfast()) {
              curHotelSumInfo.setHasBreakfast(true);
            }

        
            String cooperateChannel=curHotel.getCooperateChannel();
            if(cooperateChannel!=null && !"".equals(cooperateChannel)){
            String[] hdlTypes = cooperateChannel.split(",");
            if ((null != hdlTypes) && (hdlTypes.length > 0)) {
              for (String hdlType : hdlTypes) {
                curHotelSumInfo.setHdltype(HotelHandlerUtil.hdlTypeTransformation(curHotelSumInfo.getHdltype(), hdlType));
              }
            }
            }
            this.handler.endHandleHotelInfo(curHotelSumInfo);
          }
          else {
            bFirst = false;
          }

          curHotelId = newHotelId;

          curRoomTypeId = newRoomTypeId;

          curQuotationId = newQuotationId;

          curHotel = (HotelInfoDTO)this.hotelMap.get(String.valueOf(curHotelId));

          curRoom = WebHotelStubCombinationTool.fillRoomTypeInfo(this.roomTypeMap, newRoomTypeId);

          if ((curRoom == null) || (curRoom.getRoomTypeId() == 0L)) {
            continue;
          }
          curRoom.setHotelId(newHotelId);

          this.handler.handleRoomType(curRoom);

          curComm = WebHotelStubCombinationTool.fillCommodityInfo(this.salepromosList, this.arlertInfoList, tempQuotationDTO, newQuotationId, curRoom, this.quotationQueryDTO);

          this.handler.handleQuotation(curComm);
        }
        else if (curRoomTypeId != newRoomTypeId)
        {
          curRoomTypeId = newRoomTypeId;

          curQuotationId = newQuotationId;

          WebHotelStubCombinationTool.finishFillSaleInfos(curComm, totalDays, bedFill, checkInDate, this.handler);

          WebHotelStubCombinationTool.finishFillCommodity(curComm, curRoom, curHotel, this.quotationQueryDTO, this.handler);

          WebHotelStubCombinationTool.finishFillRoomInfo(curRoom, curHotel, this.handler);

          curRoom = WebHotelStubCombinationTool.fillRoomTypeInfo(this.roomTypeMap, newRoomTypeId);

          if ((curRoom == null) || (curRoom.getRoomTypeId() == 0L)) {
            continue;
          }
          curRoom.setHotelId(newHotelId);
          this.handler.handleRoomType(curRoom);

          curComm = WebHotelStubCombinationTool.fillCommodityInfo(this.salepromosList, this.arlertInfoList, tempQuotationDTO, newQuotationId, curRoom, this.quotationQueryDTO);

          this.handler.handleQuotation(curComm);
        }
        else if (!curQuotationId.equals(newQuotationId))
        {
          curQuotationId = newQuotationId;

          WebHotelStubCombinationTool.finishFillSaleInfos(curComm, totalDays, bedFill, checkInDate, this.handler);

          WebHotelStubCombinationTool.finishFillCommodity(curComm, curRoom, curHotel, this.quotationQueryDTO, this.handler);

          curComm = WebHotelStubCombinationTool.fillCommodityInfo(this.salepromosList, this.arlertInfoList, tempQuotationDTO, newQuotationId, curRoom, this.quotationQueryDTO);

          this.handler.handleQuotation(curComm);
        }

        WebHotelStubCombinationTool.fillSaleInfo(tempQuotationDTO, curComm, this.quotationQueryDTO);

        bedFill[(java.lang.Integer.parseInt(tempQuotationDTO.getBedtype()) - 1)] = true;
      }

      if (!bFirst)
      {
        WebHotelStubCombinationTool.finishFillSaleInfos(curComm, totalDays, bedFill, checkInDate, this.handler);

        WebHotelStubCombinationTool.finishFillCommodity(curComm, curRoom, curHotel, this.quotationQueryDTO, this.handler);

        WebHotelStubCombinationTool.finishFillRoomInfo(curRoom, curHotel, this.handler);

        if (null == curHotel) return;

        HotelSummaryInfoDTO curHotelSumInfo = new HotelSummaryInfoDTO();
        curHotelSumInfo.setHotelId(Long.valueOf(curHotel.getHotelId()));
        curHotelSumInfo.setCanbook(curHotel.isCanbook());
        curHotelSumInfo.setLowestPrice(curHotel.getLowestPrice());
        curHotelSumInfo.setHighestPrice(curHotel.getHighestPrice());
        curHotelSumInfo.setQuotationId(curHotel.getQuotationId());
        if (curHotel.isHasBreakfast()) {
          curHotelSumInfo.setHasBreakfast(true);
        }

        if ("".equals(curHotelSumInfo.getHdltype()))
          curHotelSumInfo.setHdltype(curHotel.getCooperateChannel());
        else if (curHotelSumInfo.getHdltype().indexOf(curHotel.getCooperateChannel()) == -1) {
          curHotelSumInfo.setHdltype(curHotelSumInfo.getHdltype() + "," + curHotel.getCooperateChannel());
        }
        this.handler.endHandleHotelInfo(curHotelSumInfo);
      }
    }
  }

  public void filterHotel()
  {
  }

  public void queryQuotation()
  {
    this.quotationQueryDTO = new HtlQuotationQueryDTO();
    HotelStubUtil.copyProperties(this.quotationQueryDTO, this.queryDTO);
    this.quotationQueryDTO.setHotelIdList(this.resultList);
    this.quotationList = this.quotationQueryRemote.queryHotelQuotation(this.quotationQueryDTO);
  }

  public void searchHotelIds()
  {
    HtlBaseQueryDTO baseQueryDTO = new HtlBaseQueryDTO();
    HotelStubUtil.copyProperties(baseQueryDTO, this.queryDTO);

    if ((null != baseQueryDTO.getHotelIdList()) && (baseQueryDTO.getHotelIdList().size() > 0)) {
      this.resultList = baseQueryDTO.getHotelIdList();
      this.hotelCount = baseQueryDTO.getHotelIdList().size();
    } else {
      this.resultList = this.hotelSearchRemote.searchHotelIds(baseQueryDTO);

      if (null != this.resultList)
        this.hotelCount = this.resultList.size();
    }
  }

  public void sortAndFilterHotel()
  {
    if ((null == this.resultList) || (this.resultList.size() == 0)) {
      return;
    }
    HtlSortAndFilterDTO sortAndFilterDTO = new HtlSortAndFilterDTO();
    HotelStubUtil.copyProperties(sortAndFilterDTO, this.queryDTO);
    sortAndFilterDTO.setHotelIdList(this.resultList);

    this.support = this.sortAndFilterRemote.sortAndFilterHotelsForPage(sortAndFilterDTO);

    if (this.support == null) {
      this.resultList = null;
      this.hotelCount = 0;
    } else {
      this.resultList = this.support.getHotellList();
      this.hotelCount = this.support.getHotelCount();
      this.geographicalpositionDTO = this.support.getGeographicalpositionDTO();
    }
  }

  public void sortHotel()
  {
  }

  public void searchHotelBaseInfo()
  {
    if ((null == this.resultList) || (this.resultList.size() == 0)) {
      return;
    }

    this.hotelInfoList = this.hotelSearchRemote.searchHotelInfoByIds(this.resultList);
  }

  public void destroy()
  {
    this.support = null;
    this.quotationList = null;
    this.hotelInfoList = null;
    this.hotelMap = null;
    this.roomTypeMap = null;
    this.quotationQueryDTO = null;
    this.summaryList = null;
    this.preSaleList = null;
    this.quotationQueryDTO = null;
    this.aroundInfoCompDTO = null;
    this.resultList = null;
  }

  public HotelSearchRemote getHotelSearchRemote()
  {
    return this.hotelSearchRemote;
  }

  public void setHotelSearchRemote(HotelSearchRemote hotelSearchRemote)
  {
    this.hotelSearchRemote = hotelSearchRemote;
  }

  public HotelSortAndFilterRemote getSortAndFilterRemote()
  {
    return this.sortAndFilterRemote;
  }

  public void setSortAndFilterRemote(HotelSortAndFilterRemote sortAndFilterRemote)
  {
    this.sortAndFilterRemote = sortAndFilterRemote;
  }

  public QuotationQueryRemote getQuotationQueryRemote()
  {
    return this.quotationQueryRemote;
  }

  public void setQuotationQueryRemote(QuotationQueryRemote quotationQueryRemote)
  {
    this.quotationQueryRemote = quotationQueryRemote;
  }

  public HotelCommonQueryRemote getHotelCommonQueryRemote()
  {
    return this.hotelCommonQueryRemote;
  }

  public void setHotelCommonQueryRemote(HotelCommonQueryRemote hotelCommonQueryRemote)
  {
    this.hotelCommonQueryRemote = hotelCommonQueryRemote;
  }

@Override
public List<HotelInfoDTO> searchCityCodeByHotelIds(List<String> hotelIdList)
{
  if (this.hotelSearchRemote == null) return null;
  return this.hotelSearchRemote.searchCityCodeByHotelId(hotelIdList);
}
}