create or replace procedure sp_htlSynToHtlQuery is

  l_errmsg varchar2(512); --错误信息

  l_tmpIndex integer;
  l_count    integer;

  l_bed        number(1);
  l_bed1       number(1);
  l_bed2       number(1);
  l_bed3       number(1);
  l_roomState  varchar2(1);
  l_roomState1 varchar2(1);
  l_roomState2 varchar2(1);
  l_roomState3 varchar2(1);

  l_city           varchar2(32);
  l_userType       varchar(64);
  l_priceName      varchar2(256);
  l_roomName       varchar2(256);
  l_hdlType        varchar2(2);
  l_distChannel    varchar(32);
  l_commodityCount integer;
  l_quotaNumber    integer;
  l_hasBook        varchar2(2);
  l_hasOverDraft   varchar2(2);

  l_time timestamp;

  l_countBed integer;
  l_freeNet  integer;

begin
  -- add by chenkeming
  -- 定时job更新htlQuery等表的数据

  l_time := systimestamp;

  l_count := 0;
  for curLog in (select * from htlquery_temp_log tl order by tl.tempid) loop
    l_count := l_count + 1;
  
    if curLog.LOGTYPE = 1 then
      -- insert into htl_price
    
      -- 获取床型及相应的房态
      l_bed1 := 0;
      l_bed2 := 0;
      l_bed3 := 0;
      for curRoomId in (select p.room_id
                          from htl_price p
                         where p.price_id = curLog.priceid) loop
        for curRoomState in (select r.room_state
                               from htl_room r
                              where r.room_id = curRoomId.room_id) loop
          l_tmpIndex := instr(curRoomState.room_state, '1:');
          if (l_tmpIndex > 0) then
            l_bed1       := 1;
            l_roomState1 := substr(curRoomState.room_state,
                                   l_tmpIndex + 2,
                                   1);
          end if;
          l_tmpIndex := instr(curRoomState.room_state, '2:');
          if (l_tmpIndex > 0) then
            l_bed2       := 1;
            l_roomState2 := substr(curRoomState.room_state,
                                   l_tmpIndex + 2,
                                   1);
          end if;
          l_tmpIndex := instr(curRoomState.room_state, '3:');
          if (l_tmpIndex > 0) then
            l_bed3       := 1;
            l_roomState3 := substr(curRoomState.room_state,
                                   l_tmpIndex + 2,
                                   1);
          end if;
        end loop;
      end loop;
    
      -- 获取城市编码
      l_city     := '';
      l_userType := '';
      for curHotel in (select h.city, h.sale_channel
                         from htl_hotel h
                        where h.hotel_id = curLog.HOTELID) loop
        l_city     := curHotel.city;
        l_userType := curHotel.sale_channel;
      end loop;
    
      -- 销售渠道
      l_distChannel := '';
      l_hdlType     := '0';
      for curExt in (select he.isctshotel, he.cooperate_channel
                       from htl_hotel_ext he
                      where he.hotel_id = curLog.HOTELID
                        and he.isctshotel = '1') loop
        l_hdlType := curExt.cooperate_channel;
        if curExt.isctshotel = '1' then
          for curDsply in (select *
                             from HTL_CTL_DSPLY ctl
                            where ctl.hotel_id = curLog.HOTELID
                              and ctl.room_type_id = curLog.ROOMTYPEID
                              and ctl.price_type_id = curLog.COMMODITYID
                              and ctl.pay_method = curLog.PAYMETHOD) loop
          
            if curDsply.CC = 1 then
              l_distChannel := l_distChannel || 'CC,';
            end if;
            if curDsply.WEB = 1 then
              l_distChannel := l_distChannel || 'WEB,';
            end if;
            if curDsply.TP = 1 then
              l_distChannel := l_distChannel || 'TP,';
            end if;
            if curDsply.TMC = 1 then
              l_distChannel := l_distChannel || 'TMC,';
            end if;
            if curDsply.AGENT = 1 then
              l_distChannel := l_distChannel || 'AGENT,';
            end if;
          
          end loop;
        end if;
      end loop;
    
      -- 价格类型名称
      l_priceName := '';
      for curPriceName in (select pt.price_type
                             from htl_price_type pt
                            where pt.price_type_id = curLog.COMMODITYID) loop
        l_priceName := curPriceName.price_type;
      end loop;
    
      -- 房型名称
      l_roomName := '';
      for curRoomName in (select rt.room_name
                            from htl_roomtype rt
                           where rt.room_type_id = curLog.ROOMTYPEID) loop
        l_roomName := curRoomName.room_name;
      end loop;
    
      -- 获取免费宽带信息
      select (case
               when exists (select hi.internet_id
                       from htl_internet hi
                      where hi.room_type_id = curLog.roomtypeid
                        and hi.begin_date <= curLog.abledate
                        and hi.end_date >= curLog.abledate) then
                1
               else
                0
             end)
        into l_freeNet
        from dual;
    
      -- 获取商品数量
      l_commodityCount := 0;
      for curFav in (select fp.favourable_type,
                            fp.continue_night,
                            fp.donate_night,
                            fp.packagerate_night
                       from htl_favourableclause f, Htl_Favoura_Parameter fp
                      where f.id = fp.favourable_clause_id
                        and f.hotel_id = curLog.HOTELID
                        and f.price_type_id = curLog.commodityid
                        and f.begin_date <= curLog.abledate
                        and f.end_date >= curLog.abledate
                        and (fp.FAVOURABLE_TYPE = '1' or
                            fp.FAVOURABLE_TYPE = '3')) loop
        if curFav.favourable_type = '1' then
          l_commodityCount := nvl(curFav.continue_night, 0) +
                              nvl(curFav.donate_night, 0);
        else
          l_commodityCount := nvl(curFav.packagerate_night, 0);
        end if;
      end loop;
    
      for i in 1 .. 3 loop
        if i = 1 then
          l_bed       := l_bed1;
          l_roomState := l_roomState1;
        elsif i = 2 then
          l_bed       := l_bed2;
          l_roomState := l_roomState2;
        else
          l_bed       := l_bed3;
          l_roomState := l_roomState3;
        end if;
      
        if l_bed = 1 then
        
          if (l_roomState = '0' or l_roomState = '1' or l_roomState = '2') then
            l_hasBook      := '1';
            l_hasOverDraft := '1';
          elsif (l_roomState = '3') then
            l_hasBook      := '1';
            l_hasOverDraft := '0';
          else
            l_hasBook      := '0';
            l_hasOverDraft := '0';
          end if;
        
          -- 获取配额数量
          l_quotaNumber := 0;
          for curQty in (select (nvl(BUY_QUOTA_ABLE_NUM, 0) +
                                nvl(COMMON_QUOTA_ABLE_NUM, 0) +
                                nvl(CASUAL_QUOTA_ABLE_NUM, 0)) -
                                (nvl(BUY_QUOTA_OUTOFDATE_NUM, 0) +
                                nvl(COMMON_QUOTA_OUTOFDATE_NUM, 0) +
                                nvl(CASUAL_QUOTA_OUTOFDATE_NUM, 0)) as qty
                           from htl_quota_new qn
                          where qn.hotel_id = curLog.hotelid
                            and qn.roomtype_id = curLog.roomtypeid
                            and qn.able_sale_date = curLog.abledate
                            and qn.bed_id = i) loop
            l_quotaNumber := curQty.qty;
          end loop;
        
          if (upper(l_city) = 'PEK') then
            insert into htlquery_pek
              (queryid,
               abledate,
               distchannel,
               usertype,
               closeflag,
               paymethod,
               commodityid,
               commodityname,
               commoditycount,
               bedtype,
               roomtypeid,
               roomtypename,
               hotelid,
               hdltype,
               priceid,
               saleprice,
               salesroomprice,
               breakfasttype,
               breakfastnumber,
               breakfastprice,
               currency,
               paytoprepay,
               bookstartdate,
               bookenddate,
               morningtime,
               eveningtime,
               continuum_in_end,
               continuum_in_start,
               must_in,
               restrict_in,
               continue_dates_relation,
               need_assure,
               quotanumber,
               hasbook,
               hasoverdraft,
               closereason,
               continueday,
               FORMULA,
               COMMISSION,
               COMMISSIONRATE,
               freeNet)
            values
              (SEQ_HTLQUERY_PEK.Nextval,
               curLog.abledate,
               l_distChannel,
               l_userType,
               curLog.closeflag,
               curLog.paymethod,
               curLog.commodityid,
               l_priceName,
               l_commodityCount,
               ('' || i),
               curLog.roomtypeid,
               l_roomName,
               curLog.hotelid,
               l_hdltype,
               curLog.priceId,
               curLog.saleprice,
               curLog.salesroomprice,
               curLog.breakfasttype,
               curLog.breakfastnumber,
               curLog.breakfastprice,
               curLog.currency,
               curLog.paytoprepay,
               curLog.bookstartdate,
               curLog.bookenddate,
               curLog.morningtime,
               curLog.eveningtime,
               curLog.continuum_in_end,
               curLog.continuum_in_start,
               curLog.must_in,
               curLog.restrict_in,
               curLog.continue_dates_relation,
               curLog.need_assure,
               l_quotaNumber,
               l_hasBook,
               l_hasOverDraft,
               curLog.closereason,
               curLog.continueday,
               curLog.FORMULA,
               curLog.COMMISSION,
               curLog.COMMISSIONRATE,
               l_freeNet);
          elsif (upper(l_city) = 'SHA') then
            insert into htlquery_sha
              (queryid,
               abledate,
               distchannel,
               usertype,
               closeflag,
               paymethod,
               commodityid,
               commodityname,
               commoditycount,
               bedtype,
               roomtypeid,
               roomtypename,
               hotelid,
               hdltype,
               priceid,
               saleprice,
               salesroomprice,
               breakfasttype,
               breakfastnumber,
               breakfastprice,
               currency,
               paytoprepay,
               bookstartdate,
               bookenddate,
               morningtime,
               eveningtime,
               continuum_in_end,
               continuum_in_start,
               must_in,
               restrict_in,
               continue_dates_relation,
               need_assure,
               quotanumber,
               hasbook,
               hasoverdraft,
               closereason,
               continueday,
               FORMULA,
               COMMISSION,
               COMMISSIONRATE,
               freeNet)
            values
              (SEQ_HTLQUERY_SHA.Nextval,
               curLog.abledate,
               l_distChannel,
               l_userType,
               curLog.closeflag,
               curLog.paymethod,
               curLog.commodityid,
               l_priceName,
               l_commodityCount,
               ('' || i),
               curLog.roomtypeid,
               l_roomName,
               curLog.hotelid,
               l_hdltype,
               curLog.priceId,
               curLog.saleprice,
               curLog.salesroomprice,
               curLog.breakfasttype,
               curLog.breakfastnumber,
               curLog.breakfastprice,
               curLog.currency,
               curLog.paytoprepay,
               curLog.bookstartdate,
               curLog.bookenddate,
               curLog.morningtime,
               curLog.eveningtime,
               curLog.continuum_in_end,
               curLog.continuum_in_start,
               curLog.must_in,
               curLog.restrict_in,
               curLog.continue_dates_relation,
               curLog.need_assure,
               l_quotaNumber,
               l_hasBook,
               l_hasOverDraft,
               curLog.closereason,
               curLog.continueday,
               curLog.FORMULA,
               curLog.COMMISSION,
               curLog.COMMISSIONRATE,
               l_freeNet);
          elsif (upper(l_city) = 'CAN') then
            insert into htlquery_can
              (queryid,
               abledate,
               distchannel,
               usertype,
               closeflag,
               paymethod,
               commodityid,
               commodityname,
               commoditycount,
               bedtype,
               roomtypeid,
               roomtypename,
               hotelid,
               hdltype,
               priceid,
               saleprice,
               salesroomprice,
               breakfasttype,
               breakfastnumber,
               breakfastprice,
               currency,
               paytoprepay,
               bookstartdate,
               bookenddate,
               morningtime,
               eveningtime,
               continuum_in_end,
               continuum_in_start,
               must_in,
               restrict_in,
               continue_dates_relation,
               need_assure,
               quotanumber,
               hasbook,
               hasoverdraft,
               closereason,
               continueday,
               FORMULA,
               COMMISSION,
               COMMISSIONRATE,
               freeNet)
            values
              (SEQ_HTLQUERY_CAN.Nextval,
               curLog.abledate,
               l_distChannel,
               l_userType,
               curLog.closeflag,
               curLog.paymethod,
               curLog.commodityid,
               l_priceName,
               l_commodityCount,
               ('' || i),
               curLog.roomtypeid,
               l_roomName,
               curLog.hotelid,
               l_hdltype,
               curLog.priceId,
               curLog.saleprice,
               curLog.salesroomprice,
               curLog.breakfasttype,
               curLog.breakfastnumber,
               curLog.breakfastprice,
               curLog.currency,
               curLog.paytoprepay,
               curLog.bookstartdate,
               curLog.bookenddate,
               curLog.morningtime,
               curLog.eveningtime,
               curLog.continuum_in_end,
               curLog.continuum_in_start,
               curLog.must_in,
               curLog.restrict_in,
               curLog.continue_dates_relation,
               curLog.need_assure,
               l_quotaNumber,
               l_hasBook,
               l_hasOverDraft,
               curLog.closereason,
               curLog.continueday,
               curLog.FORMULA,
               curLog.COMMISSION,
               curLog.COMMISSIONRATE,
               l_freeNet);
          elsif (upper(l_city) = 'SZX') then
            insert into htlquery_szx
              (queryid,
               abledate,
               distchannel,
               usertype,
               closeflag,
               paymethod,
               commodityid,
               commodityname,
               commoditycount,
               bedtype,
               roomtypeid,
               roomtypename,
               hotelid,
               hdltype,
               priceid,
               saleprice,
               salesroomprice,
               breakfasttype,
               breakfastnumber,
               breakfastprice,
               currency,
               paytoprepay,
               bookstartdate,
               bookenddate,
               morningtime,
               eveningtime,
               continuum_in_end,
               continuum_in_start,
               must_in,
               restrict_in,
               continue_dates_relation,
               need_assure,
               quotanumber,
               hasbook,
               hasoverdraft,
               closereason,
               continueday,
               FORMULA,
               COMMISSION,
               COMMISSIONRATE,
               freeNet)
            values
              (SEQ_HTLQUERY_SZX.Nextval,
               curLog.abledate,
               l_distChannel,
               l_userType,
               curLog.closeflag,
               curLog.paymethod,
               curLog.commodityid,
               l_priceName,
               l_commodityCount,
               ('' || i),
               curLog.roomtypeid,
               l_roomName,
               curLog.hotelid,
               l_hdltype,
               curLog.priceId,
               curLog.saleprice,
               curLog.salesroomprice,
               curLog.breakfasttype,
               curLog.breakfastnumber,
               curLog.breakfastprice,
               curLog.currency,
               curLog.paytoprepay,
               curLog.bookstartdate,
               curLog.bookenddate,
               curLog.morningtime,
               curLog.eveningtime,
               curLog.continuum_in_end,
               curLog.continuum_in_start,
               curLog.must_in,
               curLog.restrict_in,
               curLog.continue_dates_relation,
               curLog.need_assure,
               l_quotaNumber,
               l_hasBook,
               l_hasOverDraft,
               curLog.closereason,
               curLog.continueday,
               curLog.FORMULA,
               curLog.COMMISSION,
               curLog.COMMISSIONRATE,
               l_freeNet);
          else
            insert into htlquery_other
              (queryid,
               abledate,
               distchannel,
               usertype,
               closeflag,
               paymethod,
               commodityid,
               commodityname,
               commoditycount,
               bedtype,
               roomtypeid,
               roomtypename,
               hotelid,
               hdltype,
               priceid,
               saleprice,
               salesroomprice,
               breakfasttype,
               breakfastnumber,
               breakfastprice,
               currency,
               paytoprepay,
               bookstartdate,
               bookenddate,
               morningtime,
               eveningtime,
               continuum_in_end,
               continuum_in_start,
               must_in,
               restrict_in,
               continue_dates_relation,
               need_assure,
               quotanumber,
               hasbook,
               hasoverdraft,
               closereason,
               continueday,
               FORMULA,
               COMMISSION,
               COMMISSIONRATE,
               freeNet)
            values
              (SEQ_HTLQUERY_OTHER.Nextval,
               curLog.abledate,
               l_distChannel,
               l_userType,
               curLog.closeflag,
               curLog.paymethod,
               curLog.commodityid,
               l_priceName,
               l_commodityCount,
               ('' || i),
               curLog.roomtypeid,
               l_roomName,
               curLog.hotelid,
               l_hdltype,
               curLog.priceId,
               curLog.saleprice,
               curLog.salesroomprice,
               curLog.breakfasttype,
               curLog.breakfastnumber,
               curLog.breakfastprice,
               curLog.currency,
               curLog.paytoprepay,
               curLog.bookstartdate,
               curLog.bookenddate,
               curLog.morningtime,
               curLog.eveningtime,
               curLog.continuum_in_end,
               curLog.continuum_in_start,
               curLog.must_in,
               curLog.restrict_in,
               curLog.continue_dates_relation,
               curLog.need_assure,
               l_quotaNumber,
               l_hasBook,
               l_hasOverDraft,
               curLog.closereason,
               curLog.continueday,
               curLog.FORMULA,
               curLog.COMMISSION,
               curLog.COMMISSIONRATE,
               l_freeNet);
          end if;
        
        end if;
      
      end loop;
    
    elsif curLog.logType = 2 then
      -- update htl_price
    
      -- 获取城市编码
      l_city := '';
      for curHotel in (select h.city
                         from htl_hotel h
                        where h.hotel_id = curLog.HOTELID) loop
        l_city := curHotel.city;
      end loop;
    
      if (upper(l_city) = 'PEK') then
        update htlquery_pek hq
           set closeflag               = curLog.closeflag,
               saleprice               = curLog.saleprice,
               salesroomprice          = curLog.salesroomprice,
               breakfasttype           = curLog.breakfasttype,
               breakfastnumber         = curLog.breakfastnumber,
               breakfastprice          = curLog.breakfastprice,
               paytoprepay             = curLog.paytoprepay,
               bookstartdate           = curLog.bookstartdate,
               bookenddate             = curLog.bookenddate,
               morningtime             = curLog.morningtime,
               eveningtime             = curLog.eveningtime,
               continuum_in_end        = curLog.continuum_in_end,
               continuum_in_start      = curLog.continuum_in_start,
               must_in                 = curLog.must_in,
               restrict_in             = curLog.restrict_in,
               continue_dates_relation = curLog.continue_dates_relation,
               need_assure             = curLog.need_assure,
               closereason             = curLog.closereason,
               continueday             = curLog.continueday,
               FORMULA                 = curLog.FORMULA,
               COMMISSION              = curLog.COMMISSION,
               COMMISSIONRATE          = curLog.COMMISSIONRATE
         where hq.priceid = curLog.priceid;
      elsif (upper(l_city) = 'SHA') then
        update htlquery_sha hq
           set closeflag               = curLog.closeflag,
               saleprice               = curLog.saleprice,
               salesroomprice          = curLog.salesroomprice,
               breakfasttype           = curLog.breakfasttype,
               breakfastnumber         = curLog.breakfastnumber,
               breakfastprice          = curLog.breakfastprice,
               paytoprepay             = curLog.paytoprepay,
               bookstartdate           = curLog.bookstartdate,
               bookenddate             = curLog.bookenddate,
               morningtime             = curLog.morningtime,
               eveningtime             = curLog.eveningtime,
               continuum_in_end        = curLog.continuum_in_end,
               continuum_in_start      = curLog.continuum_in_start,
               must_in                 = curLog.must_in,
               restrict_in             = curLog.restrict_in,
               continue_dates_relation = curLog.continue_dates_relation,
               need_assure             = curLog.need_assure,
               closereason             = curLog.closereason,
               continueday             = curLog.continueday,
               FORMULA                 = curLog.FORMULA,
               COMMISSION              = curLog.COMMISSION,
               COMMISSIONRATE          = curLog.COMMISSIONRATE
         where hq.priceid = curLog.priceid;
      elsif (upper(l_city) = 'CAN') then
        update htlquery_can hq
           set closeflag               = curLog.closeflag,
               saleprice               = curLog.saleprice,
               salesroomprice          = curLog.salesroomprice,
               breakfasttype           = curLog.breakfasttype,
               breakfastnumber         = curLog.breakfastnumber,
               breakfastprice          = curLog.breakfastprice,
               paytoprepay             = curLog.paytoprepay,
               bookstartdate           = curLog.bookstartdate,
               bookenddate             = curLog.bookenddate,
               morningtime             = curLog.morningtime,
               eveningtime             = curLog.eveningtime,
               continuum_in_end        = curLog.continuum_in_end,
               continuum_in_start      = curLog.continuum_in_start,
               must_in                 = curLog.must_in,
               restrict_in             = curLog.restrict_in,
               continue_dates_relation = curLog.continue_dates_relation,
               need_assure             = curLog.need_assure,
               closereason             = curLog.closereason,
               continueday             = curLog.continueday,
               FORMULA                 = curLog.FORMULA,
               COMMISSION              = curLog.COMMISSION,
               COMMISSIONRATE          = curLog.COMMISSIONRATE
         where hq.priceid = curLog.priceid;
      elsif (upper(l_city) = 'SZX') then
        update htlquery_szx hq
           set closeflag               = curLog.closeflag,
               saleprice               = curLog.saleprice,
               salesroomprice          = curLog.salesroomprice,
               breakfasttype           = curLog.breakfasttype,
               breakfastnumber         = curLog.breakfastnumber,
               breakfastprice          = curLog.breakfastprice,
               paytoprepay             = curLog.paytoprepay,
               bookstartdate           = curLog.bookstartdate,
               bookenddate             = curLog.bookenddate,
               morningtime             = curLog.morningtime,
               eveningtime             = curLog.eveningtime,
               continuum_in_end        = curLog.continuum_in_end,
               continuum_in_start      = curLog.continuum_in_start,
               must_in                 = curLog.must_in,
               restrict_in             = curLog.restrict_in,
               continue_dates_relation = curLog.continue_dates_relation,
               need_assure             = curLog.need_assure,
               closereason             = curLog.closereason,
               continueday             = curLog.continueday,
               FORMULA                 = curLog.FORMULA,
               COMMISSION              = curLog.COMMISSION,
               COMMISSIONRATE          = curLog.COMMISSIONRATE
         where hq.priceid = curLog.priceid;
      else
        update htlquery_other hq
           set closeflag               = curLog.closeflag,
               saleprice               = curLog.saleprice,
               salesroomprice          = curLog.salesroomprice,
               breakfasttype           = curLog.breakfasttype,
               breakfastnumber         = curLog.breakfastnumber,
               breakfastprice          = curLog.breakfastprice,
               paytoprepay             = curLog.paytoprepay,
               bookstartdate           = curLog.bookstartdate,
               bookenddate             = curLog.bookenddate,
               morningtime             = curLog.morningtime,
               eveningtime             = curLog.eveningtime,
               continuum_in_end        = curLog.continuum_in_end,
               continuum_in_start      = curLog.continuum_in_start,
               must_in                 = curLog.must_in,
               restrict_in             = curLog.restrict_in,
               continue_dates_relation = curLog.continue_dates_relation,
               need_assure             = curLog.need_assure,
               closereason             = curLog.closereason,
               continueday             = curLog.continueday,
               FORMULA                 = curLog.FORMULA,
               COMMISSION              = curLog.COMMISSION,
               COMMISSIONRATE          = curLog.COMMISSIONRATE
         where hq.priceid = curLog.priceid;
      end if;
    
    elsif curLog.logType = 3 then
      -- delete from htl_price
    
      -- 获取城市编码
      l_city := '';
      for curHotel in (select h.city
                         from htl_hotel h
                        where h.hotel_id = curLog.HOTELID) loop
        l_city := curHotel.city;
      end loop;
    
      if (upper(l_city) = 'PEK') then
        delete from htlquery_pek hq where hq.priceid = curLog.priceid;
      elsif (upper(l_city) = 'SHA') then
        delete from htlquery_sha hq where hq.priceid = curLog.priceid;
      elsif (upper(l_city) = 'CAN') then
        delete from htlquery_can hq where hq.priceid = curLog.priceid;
      elsif (upper(l_city) = 'SZX') then
        delete from htlquery_szx hq where hq.priceid = curLog.priceid;
      else
        delete from htlquery_other hq where hq.priceid = curLog.priceid;
      end if;
    
    elsif curLog.logType = 5 then
      -- update htl_room
    
      -- 获取城市编码
      l_city := '';
      for curHotel in (select h.city
                         from htl_hotel h
                        where h.hotel_id = curLog.HOTELID) loop
        l_city := curHotel.city;
      end loop;
    
      -- 获取床型及相应的房态
      l_bed1     := 0;
      l_bed2     := 0;
      l_bed3     := 0;
      l_tmpIndex := instr(curLog.roomState, '1:');
      if (l_tmpIndex > 0) then
        l_bed1       := 1;
        l_roomState1 := substr(curLog.roomState, l_tmpIndex + 2, 1);
      end if;
      l_tmpIndex := instr(curLog.roomState, '2:');
      if (l_tmpIndex > 0) then
        l_bed2       := 1;
        l_roomState2 := substr(curLog.roomState, l_tmpIndex + 2, 1);
      end if;
      l_tmpIndex := instr(curLog.roomState, '3:');
      if (l_tmpIndex > 0) then
        l_bed3       := 1;
        l_roomState3 := substr(curLog.roomState, l_tmpIndex + 2, 1);
      end if;
    
      for i in 1 .. 3 loop
        if i = 1 then
          l_bed       := l_bed1;
          l_roomState := l_roomState1;
        elsif i = 2 then
          l_bed       := l_bed2;
          l_roomState := l_roomState2;
        else
          l_bed       := l_bed3;
          l_roomState := l_roomState3;
        end if;
      
        if l_bed = 1 then
          if (l_roomState = '0' or l_roomState = '1' or l_roomState = '2') then
            l_hasBook      := '1';
            l_hasOverDraft := '1';
          elsif (l_roomState = '3') then
            l_hasBook      := '1';
            l_hasOverDraft := '0';
          else
            l_hasBook      := '0';
            l_hasOverDraft := '0';
          end if;
        
          if (upper(l_city) = 'PEK') then
          
            select (case
                     when exists (select hq.queryid
                             from htlquery_pek hq
                            where hq.hotelid = curLog.hotelid
                              and hq.ROOMTYPEID = curLog.ROOMTYPEID
                              and hq.ABLEDATE = curLog.ABLEDATE
                              and hq.bedType = ('' || i)) then
                      1
                     else
                      0
                   end)
              into l_countBed
              from dual;
            if l_countBed = 0 then
              for j in 1 .. 3 loop
                if (j <> i and l_countBed = 0) then
                  for cur_q in (select *
                                  from htlquery_pek hq
                                 where hq.hotelid = curLog.hotelid
                                   and hq.ROOMTYPEID = curLog.ROOMTYPEID
                                   and hq.ABLEDATE = curLog.ABLEDATE
                                   and hq.bedType = ('' || j)) loop
                    l_countBed := l_countBed + 1;
                    insert into htlquery_pek
                      (queryid,
                       abledate,
                       distchannel,
                       usertype,
                       closeflag,
                       paymethod,
                       commodityid,
                       commodityname,
                       commoditycount,
                       bedtype,
                       roomtypeid,
                       roomtypename,
                       hotelid,
                       hdltype,
                       priceid,
                       saleprice,
                       salesroomprice,
                       breakfasttype,
                       breakfastnumber,
                       breakfastprice,
                       currency,
                       paytoprepay,
                       bookstartdate,
                       bookenddate,
                       morningtime,
                       eveningtime,
                       continuum_in_end,
                       continuum_in_start,
                       must_in,
                       restrict_in,
                       continue_dates_relation,
                       need_assure,
                       quotanumber,
                       hasbook,
                       hasoverdraft,
                       closereason,
                       continueday,
                       FORMULA,
                       COMMISSION,
                       COMMISSIONRATE,
                       freeNet)
                    values
                      (SEQ_HTLQUERY_PEK.Nextval,
                       cur_q.abledate,
                       cur_q.distchannel,
                       cur_q.usertype,
                       cur_q.closeflag,
                       cur_q.paymethod,
                       cur_q.commodityid,
                       cur_q.commodityname,
                       cur_q.commoditycount,
                       ('' || i),
                       cur_q.roomtypeid,
                       cur_q.roomtypename,
                       cur_q.hotelid,
                       cur_q.hdltype,
                       cur_q.priceId,
                       cur_q.saleprice,
                       cur_q.salesroomprice,
                       cur_q.breakfasttype,
                       cur_q.breakfastnumber,
                       cur_q.breakfastprice,
                       cur_q.currency,
                       cur_q.paytoprepay,
                       cur_q.bookstartdate,
                       cur_q.bookenddate,
                       cur_q.morningtime,
                       cur_q.eveningtime,
                       cur_q.continuum_in_end,
                       cur_q.continuum_in_start,
                       cur_q.must_in,
                       cur_q.restrict_in,
                       cur_q.continue_dates_relation,
                       cur_q.need_assure,
                       0,
                       cur_q.hasbook,
                       cur_q.hasoverdraft,
                       cur_q.closereason,
                       cur_q.continueday,
                       cur_q.FORMULA,
                       cur_q.COMMISSION,
                       cur_q.COMMISSIONRATE,
                       cur_q.freeNet);
                  end loop;
                end if;
              end loop;
            end if;
          
            update htlquery_pek hq
               set hasbook = l_hasBook, hasoverdraft = l_hasOverDraft
             where hq.hotelid = curLog.hotelid
               and hq.ROOMTYPEID = curLog.ROOMTYPEID
               and hq.ABLEDATE = curLog.ABLEDATE
               and hq.bedType = ('' || i);
          elsif (upper(l_city) = 'SHA') then
          
            select (case
                     when exists (select hq.queryid
                             from htlquery_sha hq
                            where hq.hotelid = curLog.hotelid
                              and hq.ROOMTYPEID = curLog.ROOMTYPEID
                              and hq.ABLEDATE = curLog.ABLEDATE
                              and hq.bedType = ('' || i)) then
                      1
                     else
                      0
                   end)
              into l_countBed
              from dual;
            if l_countBed = 0 then
              for j in 1 .. 3 loop
                if (j <> i and l_countBed = 0) then
                  for cur_q in (select *
                                  from htlquery_sha hq
                                 where hq.hotelid = curLog.hotelid
                                   and hq.ROOMTYPEID = curLog.ROOMTYPEID
                                   and hq.ABLEDATE = curLog.ABLEDATE
                                   and hq.bedType = ('' || j)) loop
                    l_countBed := l_countBed + 1;
                    insert into htlquery_sha
                      (queryid,
                       abledate,
                       distchannel,
                       usertype,
                       closeflag,
                       paymethod,
                       commodityid,
                       commodityname,
                       commoditycount,
                       bedtype,
                       roomtypeid,
                       roomtypename,
                       hotelid,
                       hdltype,
                       priceid,
                       saleprice,
                       salesroomprice,
                       breakfasttype,
                       breakfastnumber,
                       breakfastprice,
                       currency,
                       paytoprepay,
                       bookstartdate,
                       bookenddate,
                       morningtime,
                       eveningtime,
                       continuum_in_end,
                       continuum_in_start,
                       must_in,
                       restrict_in,
                       continue_dates_relation,
                       need_assure,
                       quotanumber,
                       hasbook,
                       hasoverdraft,
                       closereason,
                       continueday,
                       FORMULA,
                       COMMISSION,
                       COMMISSIONRATE,
                       freeNet)
                    values
                      (SEQ_HTLQUERY_SHA.Nextval,
                       cur_q.abledate,
                       cur_q.distchannel,
                       cur_q.usertype,
                       cur_q.closeflag,
                       cur_q.paymethod,
                       cur_q.commodityid,
                       cur_q.commodityname,
                       cur_q.commoditycount,
                       ('' || i),
                       cur_q.roomtypeid,
                       cur_q.roomtypename,
                       cur_q.hotelid,
                       cur_q.hdltype,
                       cur_q.priceId,
                       cur_q.saleprice,
                       cur_q.salesroomprice,
                       cur_q.breakfasttype,
                       cur_q.breakfastnumber,
                       cur_q.breakfastprice,
                       cur_q.currency,
                       cur_q.paytoprepay,
                       cur_q.bookstartdate,
                       cur_q.bookenddate,
                       cur_q.morningtime,
                       cur_q.eveningtime,
                       cur_q.continuum_in_end,
                       cur_q.continuum_in_start,
                       cur_q.must_in,
                       cur_q.restrict_in,
                       cur_q.continue_dates_relation,
                       cur_q.need_assure,
                       0,
                       cur_q.hasbook,
                       cur_q.hasoverdraft,
                       cur_q.closereason,
                       cur_q.continueday,
                       cur_q.FORMULA,
                       cur_q.COMMISSION,
                       cur_q.COMMISSIONRATE,
                       cur_q.freeNet);
                  end loop;
                end if;
              end loop;
            end if;
          
            update htlquery_sha hq
               set hasbook = l_hasBook, hasoverdraft = l_hasOverDraft
             where hq.hotelid = curLog.hotelid
               and hq.ROOMTYPEID = curLog.ROOMTYPEID
               and hq.ABLEDATE = curLog.ABLEDATE
               and hq.bedType = ('' || i);
          elsif (upper(l_city) = 'CAN') then
          
            select (case
                     when exists (select hq.queryid
                             from htlquery_can hq
                            where hq.hotelid = curLog.hotelid
                              and hq.ROOMTYPEID = curLog.ROOMTYPEID
                              and hq.ABLEDATE = curLog.ABLEDATE
                              and hq.bedType = ('' || i)) then
                      1
                     else
                      0
                   end)
              into l_countBed
              from dual;
            if l_countBed = 0 then
              for j in 1 .. 3 loop
                if (j <> i and l_countBed = 0) then
                  for cur_q in (select *
                                  from htlquery_can hq
                                 where hq.hotelid = curLog.hotelid
                                   and hq.ROOMTYPEID = curLog.ROOMTYPEID
                                   and hq.ABLEDATE = curLog.ABLEDATE
                                   and hq.bedType = ('' || j)) loop
                    l_countBed := l_countBed + 1;
                    insert into htlquery_can
                      (queryid,
                       abledate,
                       distchannel,
                       usertype,
                       closeflag,
                       paymethod,
                       commodityid,
                       commodityname,
                       commoditycount,
                       bedtype,
                       roomtypeid,
                       roomtypename,
                       hotelid,
                       hdltype,
                       priceid,
                       saleprice,
                       salesroomprice,
                       breakfasttype,
                       breakfastnumber,
                       breakfastprice,
                       currency,
                       paytoprepay,
                       bookstartdate,
                       bookenddate,
                       morningtime,
                       eveningtime,
                       continuum_in_end,
                       continuum_in_start,
                       must_in,
                       restrict_in,
                       continue_dates_relation,
                       need_assure,
                       quotanumber,
                       hasbook,
                       hasoverdraft,
                       closereason,
                       continueday,
                       FORMULA,
                       COMMISSION,
                       COMMISSIONRATE,
                       freeNet)
                    values
                      (SEQ_HTLQUERY_CAN.Nextval,
                       cur_q.abledate,
                       cur_q.distchannel,
                       cur_q.usertype,
                       cur_q.closeflag,
                       cur_q.paymethod,
                       cur_q.commodityid,
                       cur_q.commodityname,
                       cur_q.commoditycount,
                       ('' || i),
                       cur_q.roomtypeid,
                       cur_q.roomtypename,
                       cur_q.hotelid,
                       cur_q.hdltype,
                       cur_q.priceId,
                       cur_q.saleprice,
                       cur_q.salesroomprice,
                       cur_q.breakfasttype,
                       cur_q.breakfastnumber,
                       cur_q.breakfastprice,
                       cur_q.currency,
                       cur_q.paytoprepay,
                       cur_q.bookstartdate,
                       cur_q.bookenddate,
                       cur_q.morningtime,
                       cur_q.eveningtime,
                       cur_q.continuum_in_end,
                       cur_q.continuum_in_start,
                       cur_q.must_in,
                       cur_q.restrict_in,
                       cur_q.continue_dates_relation,
                       cur_q.need_assure,
                       0,
                       cur_q.hasbook,
                       cur_q.hasoverdraft,
                       cur_q.closereason,
                       cur_q.continueday,
                       cur_q.FORMULA,
                       cur_q.COMMISSION,
                       cur_q.COMMISSIONRATE,
                       cur_q.freeNet);
                  end loop;
                end if;
              end loop;
            end if;
          
            update htlquery_can hq
               set hasbook = l_hasBook, hasoverdraft = l_hasOverDraft
             where hq.hotelid = curLog.hotelid
               and hq.ROOMTYPEID = curLog.ROOMTYPEID
               and hq.ABLEDATE = curLog.ABLEDATE
               and hq.bedType = ('' || i);
          elsif (upper(l_city) = 'SZX') then
          
            select (case
                     when exists (select hq.queryid
                             from htlquery_szx hq
                            where hq.hotelid = curLog.hotelid
                              and hq.ROOMTYPEID = curLog.ROOMTYPEID
                              and hq.ABLEDATE = curLog.ABLEDATE
                              and hq.bedType = ('' || i)) then
                      1
                     else
                      0
                   end)
              into l_countBed
              from dual;
            if l_countBed = 0 then
              for j in 1 .. 3 loop
                if (j <> i and l_countBed = 0) then
                  for cur_q in (select *
                                  from htlquery_szx hq
                                 where hq.hotelid = curLog.hotelid
                                   and hq.ROOMTYPEID = curLog.ROOMTYPEID
                                   and hq.ABLEDATE = curLog.ABLEDATE
                                   and hq.bedType = ('' || j)) loop
                    l_countBed := l_countBed + 1;
                    insert into htlquery_szx
                      (queryid,
                       abledate,
                       distchannel,
                       usertype,
                       closeflag,
                       paymethod,
                       commodityid,
                       commodityname,
                       commoditycount,
                       bedtype,
                       roomtypeid,
                       roomtypename,
                       hotelid,
                       hdltype,
                       priceid,
                       saleprice,
                       salesroomprice,
                       breakfasttype,
                       breakfastnumber,
                       breakfastprice,
                       currency,
                       paytoprepay,
                       bookstartdate,
                       bookenddate,
                       morningtime,
                       eveningtime,
                       continuum_in_end,
                       continuum_in_start,
                       must_in,
                       restrict_in,
                       continue_dates_relation,
                       need_assure,
                       quotanumber,
                       hasbook,
                       hasoverdraft,
                       closereason,
                       continueday,
                       FORMULA,
                       COMMISSION,
                       COMMISSIONRATE,
                       freeNet)
                    values
                      (SEQ_HTLQUERY_SZX.Nextval,
                       cur_q.abledate,
                       cur_q.distchannel,
                       cur_q.usertype,
                       cur_q.closeflag,
                       cur_q.paymethod,
                       cur_q.commodityid,
                       cur_q.commodityname,
                       cur_q.commoditycount,
                       ('' || i),
                       cur_q.roomtypeid,
                       cur_q.roomtypename,
                       cur_q.hotelid,
                       cur_q.hdltype,
                       cur_q.priceId,
                       cur_q.saleprice,
                       cur_q.salesroomprice,
                       cur_q.breakfasttype,
                       cur_q.breakfastnumber,
                       cur_q.breakfastprice,
                       cur_q.currency,
                       cur_q.paytoprepay,
                       cur_q.bookstartdate,
                       cur_q.bookenddate,
                       cur_q.morningtime,
                       cur_q.eveningtime,
                       cur_q.continuum_in_end,
                       cur_q.continuum_in_start,
                       cur_q.must_in,
                       cur_q.restrict_in,
                       cur_q.continue_dates_relation,
                       cur_q.need_assure,
                       0,
                       cur_q.hasbook,
                       cur_q.hasoverdraft,
                       cur_q.closereason,
                       cur_q.continueday,
                       cur_q.FORMULA,
                       cur_q.COMMISSION,
                       cur_q.COMMISSIONRATE,
                       cur_q.freeNet);
                  end loop;
                end if;
              end loop;
            end if;
          
            update htlquery_szx hq
               set hasbook = l_hasBook, hasoverdraft = l_hasOverDraft
             where hq.hotelid = curLog.hotelid
               and hq.ROOMTYPEID = curLog.ROOMTYPEID
               and hq.ABLEDATE = curLog.ABLEDATE
               and hq.bedType = ('' || i);
          else
          
            select (case
                     when exists (select hq.queryid
                             from htlquery_other hq
                            where hq.hotelid = curLog.hotelid
                              and hq.ROOMTYPEID = curLog.ROOMTYPEID
                              and hq.ABLEDATE = curLog.ABLEDATE
                              and hq.bedType = ('' || i)) then
                      1
                     else
                      0
                   end)
              into l_countBed
              from dual;
            if l_countBed = 0 then
              for j in 1 .. 3 loop
                if (j <> i and l_countBed = 0) then
                  for cur_q in (select *
                                  from htlquery_other hq
                                 where hq.hotelid = curLog.hotelid
                                   and hq.ROOMTYPEID = curLog.ROOMTYPEID
                                   and hq.ABLEDATE = curLog.ABLEDATE
                                   and hq.bedType = ('' || j)) loop
                    l_countBed := l_countBed + 1;
                    insert into htlquery_other
                      (queryid,
                       abledate,
                       distchannel,
                       usertype,
                       closeflag,
                       paymethod,
                       commodityid,
                       commodityname,
                       commoditycount,
                       bedtype,
                       roomtypeid,
                       roomtypename,
                       hotelid,
                       hdltype,
                       priceid,
                       saleprice,
                       salesroomprice,
                       breakfasttype,
                       breakfastnumber,
                       breakfastprice,
                       currency,
                       paytoprepay,
                       bookstartdate,
                       bookenddate,
                       morningtime,
                       eveningtime,
                       continuum_in_end,
                       continuum_in_start,
                       must_in,
                       restrict_in,
                       continue_dates_relation,
                       need_assure,
                       quotanumber,
                       hasbook,
                       hasoverdraft,
                       closereason,
                       continueday,
                       FORMULA,
                       COMMISSION,
                       COMMISSIONRATE,
                       freeNet)
                    values
                      (SEQ_HTLQUERY_OTHER.Nextval,
                       cur_q.abledate,
                       cur_q.distchannel,
                       cur_q.usertype,
                       cur_q.closeflag,
                       cur_q.paymethod,
                       cur_q.commodityid,
                       cur_q.commodityname,
                       cur_q.commoditycount,
                       ('' || i),
                       cur_q.roomtypeid,
                       cur_q.roomtypename,
                       cur_q.hotelid,
                       cur_q.hdltype,
                       cur_q.priceId,
                       cur_q.saleprice,
                       cur_q.salesroomprice,
                       cur_q.breakfasttype,
                       cur_q.breakfastnumber,
                       cur_q.breakfastprice,
                       cur_q.currency,
                       cur_q.paytoprepay,
                       cur_q.bookstartdate,
                       cur_q.bookenddate,
                       cur_q.morningtime,
                       cur_q.eveningtime,
                       cur_q.continuum_in_end,
                       cur_q.continuum_in_start,
                       cur_q.must_in,
                       cur_q.restrict_in,
                       cur_q.continue_dates_relation,
                       cur_q.need_assure,
                       0,
                       cur_q.hasbook,
                       cur_q.hasoverdraft,
                       cur_q.closereason,
                       cur_q.continueday,
                       cur_q.FORMULA,
                       cur_q.COMMISSION,
                       cur_q.COMMISSIONRATE,
                       cur_q.freeNet);
                  end loop;
                end if;
              end loop;
            end if;
          
            update htlquery_other hq
               set hasbook = l_hasBook, hasoverdraft = l_hasOverDraft
             where hq.hotelid = curLog.hotelid
               and hq.ROOMTYPEID = curLog.ROOMTYPEID
               and hq.ABLEDATE = curLog.ABLEDATE
               and hq.bedType = ('' || i);
          end if;
        
        else
        
          if (upper(l_city) = 'PEK') then
          
            select (case
                     when exists (select hq.queryid
                             from htlquery_pek hq
                            where hq.hotelid = curLog.hotelid
                              and hq.ROOMTYPEID = curLog.ROOMTYPEID
                              and hq.ABLEDATE = curLog.ABLEDATE
                              and hq.bedType = ('' || i)) then
                      1
                     else
                      0
                   end)
              into l_countBed
              from dual;
            if l_countBed > 0 then
            
              delete from htlquery_pek hq
               where hq.hotelid = curLog.hotelid
                 and hq.ROOMTYPEID = curLog.ROOMTYPEID
                 and hq.ABLEDATE = curLog.ABLEDATE
                 and hq.bedType = ('' || i);
            
            end if;
          
          elsif (upper(l_city) = 'SHA') then
          
            select (case
                     when exists (select hq.queryid
                             from htlquery_sha hq
                            where hq.hotelid = curLog.hotelid
                              and hq.ROOMTYPEID = curLog.ROOMTYPEID
                              and hq.ABLEDATE = curLog.ABLEDATE
                              and hq.bedType = ('' || i)) then
                      1
                     else
                      0
                   end)
              into l_countBed
              from dual;
            if l_countBed > 0 then
            
              delete from htlquery_sha hq
               where hq.hotelid = curLog.hotelid
                 and hq.ROOMTYPEID = curLog.ROOMTYPEID
                 and hq.ABLEDATE = curLog.ABLEDATE
                 and hq.bedType = ('' || i);
            
            end if;
          
          elsif (upper(l_city) = 'CAN') then
          
            select (case
                     when exists (select hq.queryid
                             from htlquery_can hq
                            where hq.hotelid = curLog.hotelid
                              and hq.ROOMTYPEID = curLog.ROOMTYPEID
                              and hq.ABLEDATE = curLog.ABLEDATE
                              and hq.bedType = ('' || i)) then
                      1
                     else
                      0
                   end)
              into l_countBed
              from dual;
            if l_countBed > 0 then
            
              delete from htlquery_can hq
               where hq.hotelid = curLog.hotelid
                 and hq.ROOMTYPEID = curLog.ROOMTYPEID
                 and hq.ABLEDATE = curLog.ABLEDATE
                 and hq.bedType = ('' || i);
            
            end if;
          
          elsif (upper(l_city) = 'SZX') then
          
            select (case
                     when exists (select hq.queryid
                             from htlquery_szx hq
                            where hq.hotelid = curLog.hotelid
                              and hq.ROOMTYPEID = curLog.ROOMTYPEID
                              and hq.ABLEDATE = curLog.ABLEDATE
                              and hq.bedType = ('' || i)) then
                      1
                     else
                      0
                   end)
              into l_countBed
              from dual;
            if l_countBed > 0 then
            
              delete from htlquery_szx hq
               where hq.hotelid = curLog.hotelid
                 and hq.ROOMTYPEID = curLog.ROOMTYPEID
                 and hq.ABLEDATE = curLog.ABLEDATE
                 and hq.bedType = ('' || i);
            
            end if;
          
          else
          
            select (case
                     when exists (select hq.queryid
                             from htlquery_other hq
                            where hq.hotelid = curLog.hotelid
                              and hq.ROOMTYPEID = curLog.ROOMTYPEID
                              and hq.ABLEDATE = curLog.ABLEDATE
                              and hq.bedType = ('' || i)) then
                      1
                     else
                      0
                   end)
              into l_countBed
              from dual;
            if l_countBed > 0 then
            
              delete from htlquery_other hq
               where hq.hotelid = curLog.hotelid
                 and hq.ROOMTYPEID = curLog.ROOMTYPEID
                 and hq.ABLEDATE = curLog.ABLEDATE
                 and hq.bedType = ('' || i);
            
            end if;
          
          end if;
        
        end if;
      end loop;
    
    elsif curLog.logType = 6 then
      -- delete from htl_room
    
      -- 获取城市编码
      l_city := '';
      for curHotel in (select h.city
                         from htl_hotel h
                        where h.hotel_id = curLog.HOTELID) loop
        l_city := curHotel.city;
      end loop;
    
      if (upper(l_city) = 'PEK') then
        delete from htlquery_pek hq
         where hq.hotelid = curLog.hotelid
           and hq.ROOMTYPEID = curLog.ROOMTYPEID
           and hq.ABLEDATE = curLog.ABLEDATE;
      elsif (upper(l_city) = 'SHA') then
        delete from htlquery_sha hq
         where hq.hotelid = curLog.hotelid
           and hq.ROOMTYPEID = curLog.ROOMTYPEID
           and hq.ABLEDATE = curLog.ABLEDATE;
      elsif (upper(l_city) = 'CAN') then
        delete from htlquery_can hq
         where hq.hotelid = curLog.hotelid
           and hq.ROOMTYPEID = curLog.ROOMTYPEID
           and hq.ABLEDATE = curLog.ABLEDATE;
      elsif (upper(l_city) = 'SZX') then
        delete from htlquery_szx hq
         where hq.hotelid = curLog.hotelid
           and hq.ROOMTYPEID = curLog.ROOMTYPEID
           and hq.ABLEDATE = curLog.ABLEDATE;
      else
        delete from htlquery_other hq
         where hq.hotelid = curLog.hotelid
           and hq.ROOMTYPEID = curLog.ROOMTYPEID
           and hq.ABLEDATE = curLog.ABLEDATE;
      end if;
    
    elsif (curLog.logType = 7 or curLog.logType = 8 or curLog.logType = 9) then
      -- insert into, update, delete from htl_quota_new
      if curLog.logType = 9 then
        l_quotaNumber := 0;
      else
        l_quotaNumber := curLog.quotaNumber;
      end if;
    
      -- 获取城市编码
      l_city := '';
      for curHotel in (select h.city
                         from htl_hotel h
                        where h.hotel_id = curLog.HOTELID) loop
        l_city := curHotel.city;
      end loop;
    
      if (upper(l_city) = 'PEK') then
        update htlquery_pek hq
           set quotaNumber = l_quotaNumber
         where hq.hotelid = curLog.hotelid
           and hq.ROOMTYPEID = curLog.ROOMTYPEID
           and hq.ABLEDATE = curLog.ABLEDATE
           and hq.bedType = curLog.bedType
           and (hq.paymethod =
               decode(curLog.quotaShareType, 1, 'pay', 3, 'pay', 'pre_pay') or
               hq.paymethod = decode(curLog.quotaShareType,
                                      2,
                                      'pre_pay',
                                      3,
                                      'pre_pay',
                                      'pay'));
      elsif (upper(l_city) = 'SHA') then
        update htlquery_sha hq
           set quotaNumber = l_quotaNumber
         where hq.hotelid = curLog.hotelid
           and hq.ROOMTYPEID = curLog.ROOMTYPEID
           and hq.ABLEDATE = curLog.ABLEDATE
           and hq.bedType = curLog.bedType
           and (hq.paymethod =
               decode(curLog.quotaShareType, 1, 'pay', 3, 'pay', 'pre_pay') or
               hq.paymethod = decode(curLog.quotaShareType,
                                      2,
                                      'pre_pay',
                                      3,
                                      'pre_pay',
                                      'pay'));
      elsif (upper(l_city) = 'CAN') then
        update htlquery_can hq
           set quotaNumber = l_quotaNumber
         where hq.hotelid = curLog.hotelid
           and hq.ROOMTYPEID = curLog.ROOMTYPEID
           and hq.ABLEDATE = curLog.ABLEDATE
           and hq.bedType = curLog.bedType
           and (hq.paymethod =
               decode(curLog.quotaShareType, 1, 'pay', 3, 'pay', 'pre_pay') or
               hq.paymethod = decode(curLog.quotaShareType,
                                      2,
                                      'pre_pay',
                                      3,
                                      'pre_pay',
                                      'pay'));
      elsif (upper(l_city) = 'SZX') then
        update htlquery_szx hq
           set quotaNumber = l_quotaNumber
         where hq.hotelid = curLog.hotelid
           and hq.ROOMTYPEID = curLog.ROOMTYPEID
           and hq.ABLEDATE = curLog.ABLEDATE
           and hq.bedType = curLog.bedType
           and (hq.paymethod =
               decode(curLog.quotaShareType, 1, 'pay', 3, 'pay', 'pre_pay') or
               hq.paymethod = decode(curLog.quotaShareType,
                                      2,
                                      'pre_pay',
                                      3,
                                      'pre_pay',
                                      'pay'));
      else
        update htlquery_other hq
           set quotaNumber = l_quotaNumber
         where hq.hotelid = curLog.hotelid
           and hq.ROOMTYPEID = curLog.ROOMTYPEID
           and hq.ABLEDATE = curLog.ABLEDATE
           and hq.bedType = curLog.bedType
           and (hq.paymethod =
               decode(curLog.quotaShareType, 1, 'pay', 3, 'pay', 'pre_pay') or
               hq.paymethod = decode(curLog.quotaShareType,
                                      2,
                                      'pre_pay',
                                      3,
                                      'pre_pay',
                                      'pay'));
      end if;
    
    end if;
  
    delete from htlquery_temp_log tl where tl.tempid = curLog.tempId;
  
    if mod(l_count, 1000) = 0 and l_count >= 1000 then
      commit;
    end if;
  
  end loop;

  for curLog in (select * from htlquery_temp_log1 tl order by tl.tempid) loop
  
    if curLog.LOGTYPE = 1 then
      -- htl_hotel的sale_channel
    
      -- 获取城市编码
      l_city := '';
      for curHotel in (select h.city
                         from htl_hotel h
                        where h.hotel_id = curLog.HOTEL_ID) loop
        l_city := curHotel.city;
      end loop;
    
      if (upper(l_city) = 'PEK') then
        update htlquery_pek hq
           set userType = curLog.saleChannel
         where hq.hotelid = curLog.hotel_id;
      elsif (upper(l_city) = 'SHA') then
        update htlquery_sha hq
           set userType = curLog.saleChannel
         where hq.hotelid = curLog.hotel_id;
      elsif (upper(l_city) = 'CAN') then
        update htlquery_can hq
           set userType = curLog.saleChannel
         where hq.hotelid = curLog.hotel_id;
      elsif (upper(l_city) = 'SZX') then
        update htlquery_szx hq
           set userType = curLog.saleChannel
         where hq.hotelid = curLog.hotel_id;
      else
        update htlquery_other hq
           set userType = curLog.saleChannel
         where hq.hotelid = curLog.hotel_id;
      end if;
    
    elsif curLog.LOGTYPE = 2 then
      -- HTL_CTL_DSPLY表
    
      -- 获取城市编码
      l_city := '';
      for curHotel in (select h.city
                         from htl_hotel h
                        where h.hotel_id = curLog.HOTEL_ID) loop
        l_city := curHotel.city;
      end loop;
    
      if (upper(l_city) = 'PEK') then
        update htlquery_pek hq
           set distChannel = curLog.distChannel
         where hq.hotelid = curLog.hotel_id
           and hq.COMMODITYID = curLog.PRICE_TYPE_ID;
      elsif (upper(l_city) = 'SHA') then
        update htlquery_sha hq
           set distChannel = curLog.distChannel
         where hq.hotelid = curLog.hotel_id
           and hq.COMMODITYID = curLog.PRICE_TYPE_ID;
      elsif (upper(l_city) = 'CAN') then
        update htlquery_can hq
           set distChannel = curLog.distChannel
         where hq.hotelid = curLog.hotel_id
           and hq.COMMODITYID = curLog.PRICE_TYPE_ID;
      elsif (upper(l_city) = 'SZX') then
        update htlquery_szx hq
           set distChannel = curLog.distChannel
         where hq.hotelid = curLog.hotel_id
           and hq.COMMODITYID = curLog.PRICE_TYPE_ID;
      else
        update htlquery_other hq
           set distChannel = curLog.distChannel
         where hq.hotelid = curLog.hotel_id
           and hq.COMMODITYID = curLog.PRICE_TYPE_ID;
      end if;
    
    elsif curLog.LOGTYPE = 3 or curLog.LOGTYPE = 4 then
      -- Htl_Favoura_Parameter表
    
      for curFav in (select *
                       from htl_favourableclause f
                      where f.id = curLog.favourable_clause_id) loop
      
        -- 获取商品数量
        if curLog.LOGTYPE = 3 then
          if curLog.FAVOURABLE_TYPE = '1' then
            l_commodityCount := nvl(curLog.continue_night, 0) +
                                nvl(curLog.donate_night, 0);
          else
            l_commodityCount := nvl(curLog.packagerate_night, 0);
          end if;
        else
          l_commodityCount := 0;
        end if;
      
        -- 获取城市编码
        l_city := '';
        for curHotel in (select h.city
                           from htl_hotel h
                          where h.hotel_id = curLog.HOTEL_ID) loop
          l_city := curHotel.city;
        end loop;
      
        if (upper(l_city) = 'PEK') then
          update htlquery_pek hq
             set hq.COMMODITYCOUNT = l_commodityCount
           where hq.hotelid = curFav.HOTEL_ID
             and hq.COMMODITYID = curFav.PRICE_TYPE_ID
             and hq.ABLEDATE >= curFav.BEGIN_DATE
             and hq.ABLEDATE <= curFav.END_DATE;
        elsif (upper(l_city) = 'SHA') then
          update htlquery_sha hq
             set hq.COMMODITYCOUNT = l_commodityCount
           where hq.hotelid = curFav.HOTEL_ID
             and hq.COMMODITYID = curFav.PRICE_TYPE_ID
             and hq.ABLEDATE >= curFav.BEGIN_DATE
             and hq.ABLEDATE <= curFav.END_DATE;
        elsif (upper(l_city) = 'CAN') then
          update htlquery_can hq
             set hq.COMMODITYCOUNT = l_commodityCount
           where hq.hotelid = curFav.HOTEL_ID
             and hq.COMMODITYID = curFav.PRICE_TYPE_ID
             and hq.ABLEDATE >= curFav.BEGIN_DATE
             and hq.ABLEDATE <= curFav.END_DATE;
        elsif (upper(l_city) = 'SZX') then
          update htlquery_szx hq
             set hq.COMMODITYCOUNT = l_commodityCount
           where hq.hotelid = curFav.HOTEL_ID
             and hq.COMMODITYID = curFav.PRICE_TYPE_ID
             and hq.ABLEDATE >= curFav.BEGIN_DATE
             and hq.ABLEDATE <= curFav.END_DATE;
        else
          update htlquery_other hq
             set hq.COMMODITYCOUNT = l_commodityCount
           where hq.hotelid = curFav.HOTEL_ID
             and hq.COMMODITYID = curFav.PRICE_TYPE_ID
             and hq.ABLEDATE >= curFav.BEGIN_DATE
             and hq.ABLEDATE <= curFav.END_DATE;
        end if;
      
      end loop;
    
    elsif curLog.LOGTYPE = 5 then
      -- htl_hotel_ext
    
      -- 获取城市编码
      l_city := '';
      for curHotel in (select h.city
                         from htl_hotel h
                        where h.hotel_id = curLog.HOTEL_ID) loop
        l_city := curHotel.city;
      end loop;
    
      if (upper(l_city) = 'PEK') then
        update htlquery_pek hq
           set hdlType = curLog.hdlType
         where hq.hotelid = curLog.hotel_id;
      elsif (upper(l_city) = 'SHA') then
        update htlquery_sha hq
           set hdlType = curLog.hdlType
         where hq.hotelid = curLog.hotel_id;
      elsif (upper(l_city) = 'CAN') then
        update htlquery_can hq
           set hdlType = curLog.hdlType
         where hq.hotelid = curLog.hotel_id;
      elsif (upper(l_city) = 'SZX') then
        update htlquery_szx hq
           set hdlType = curLog.hdlType
         where hq.hotelid = curLog.hotel_id;
      else
        update htlquery_other hq
           set hdlType = curLog.hdlType
         where hq.hotelid = curLog.hotel_id;
      end if;
    
    elsif curLog.LOGTYPE = 6 then
      -- htl_internet
    
      -- 获取城市编码
      l_city := '';
      for curHotel in (select h.city
                         from htl_hotel h
                        where h.hotel_id = curLog.HOTEL_ID) loop
        l_city := curHotel.city;
      end loop;
    
      if (upper(l_city) = 'PEK') then
        update htlquery_pek hq
           set freeNet = 0
         where hq.hotelid = curLog.hotel_id
           and hq.roomtypeid = curLog.PRICE_TYPE_ID;
      elsif (upper(l_city) = 'SHA') then
        update htlquery_sha hq
           set freeNet = 0
         where hq.hotelid = curLog.hotel_id
           and hq.roomtypeid = curLog.PRICE_TYPE_ID;
      elsif (upper(l_city) = 'CAN') then
        update htlquery_can hq
           set freeNet = 0
         where hq.hotelid = curLog.hotel_id
           and hq.roomtypeid = curLog.PRICE_TYPE_ID;
      elsif (upper(l_city) = 'SZX') then
        update htlquery_szx hq
           set freeNet = 0
         where hq.hotelid = curLog.hotel_id
           and hq.roomtypeid = curLog.PRICE_TYPE_ID;
      else
        update htlquery_other hq
           set freeNet = 0
         where hq.hotelid = curLog.hotel_id
           and hq.roomtypeid = curLog.PRICE_TYPE_ID;
      end if;
    
      for cur_i in (select *
                      from htl_internet hi
                     where hi.room_type_id = curLog.PRICE_TYPE_ID
                       and hi.begin_date <= trunc(sysdate + 60)
                       and hi.end_date >= trunc(sysdate)
                       and hi.contract_id is not null
                       and exists
                     (select c.contract_id
                              from htl_contract c
                             where c.hotel_id = curLog.hotel_id
                               and c.begin_date <= trunc(sysdate + 60)
                               and c.end_date >= trunc(sysdate)
                               and c.contract_id = hi.contract_id)
                     order by hi.modify_time) loop
        if (upper(l_city) = 'PEK') then
          update htlquery_pek hq
             set freeNet = 1
           where hq.roomtypeid = curLog.PRICE_TYPE_ID
             and hq.abledate >= cur_i.begin_date
             and hq.abledate <= cur_i.end_date;
        elsif (upper(l_city) = 'SHA') then
          update htlquery_sha hq
             set freeNet = 1
           where hq.roomtypeid = curLog.PRICE_TYPE_ID
             and hq.abledate >= cur_i.begin_date
             and hq.abledate <= cur_i.end_date;
        elsif (upper(l_city) = 'CAN') then
          update htlquery_can hq
             set freeNet = 1
           where hq.roomtypeid = curLog.PRICE_TYPE_ID
             and hq.abledate >= cur_i.begin_date
             and hq.abledate <= cur_i.end_date;
        elsif (upper(l_city) = 'SZX') then
          update htlquery_szx hq
             set freeNet = 1
           where hq.roomtypeid = curLog.PRICE_TYPE_ID
             and hq.abledate >= cur_i.begin_date
             and hq.abledate <= cur_i.end_date;
        else
          update htlquery_other hq
             set freeNet = 1
           where hq.roomtypeid = curLog.PRICE_TYPE_ID
             and hq.abledate >= cur_i.begin_date
             and hq.abledate <= cur_i.end_date;
        end if;
      end loop;
    
    end if;
  
    delete from htlquery_temp_log1 tl where tl.tempid = curLog.tempId;
    commit;
  
  end loop;

  if l_count > 0 then
    insert into err_log
      (errdate, errmsg)
    values
      (sysdate,
       'exit htl_ii.sp_htlSynToHtlQuery, l_count:' || l_count ||
       ',timestamp used:' || (systimestamp - l_time));
  end if;

exception
  when others then
    l_errmsg := substr(sqlerrm, 1, 300);
    insert into err_log
    values
      (sysdate, 'htl_ii.sp_htlSynToHtlQuery:' || l_errmsg);
  
end sp_htlSynToHtlQuery;
