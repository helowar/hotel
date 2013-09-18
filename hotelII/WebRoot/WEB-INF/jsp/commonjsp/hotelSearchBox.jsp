<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
 <!--==S search box-->
        <div class="requery">
            <div class="requerytit"><em class="feedback">共为您找到<strong>996</strong>家酒店</em><h2>预订酒店</h2></div>
        <form action="" method="get">
            <div class="hn-form">
                <ul>
                    <li class="w158"><label class="input_label"><input id="id_startCity" name="cityName" type="text" class="w144 greytxt MGcity" value="深圳" datatype="hotel" /><span id="scitytip" class="hidetxt" style="display:none;">选择城市</span><a id="scityIcon" href="#" class="cityIcon"></a></label></li>
                    <li class="w158"><label class="input_label"><input id="id_startDate" name="" type="text" class="w144 required greytxt calendar" value="入住日期" /><span id="sdatetip" class="hidetxt" style="display:none;">入住</span><span class="holitip"></span><a href="#" class="dateIcon"></a></label>
                    </li>
                    <li class="w158"><label class="input_label"><input id="id_backDate" name="" type="text" class="w144 required greytxt calendar" value="离店日期" /><span id="edatetip" class="hidetxt" style="display:none;">离店</span><span class="holitip"></span><a href="#" class="dateIcon"></a></label>
                     </li>
                    
                    <li class="w150"><label class="input_label"><input id="hoteladdress" name="" type="text" class="w134 greytxt" value="酒店名称查询" /></label></li>
                    <li class="w92"><a href="javascript:void(0);" class="btn92x26a">重新搜索</a></li>
                </ul>
                
               
            </div>
            <div id="conditions" class="conditions">
                <dl class="odd">
                    <dt>酒店星级：</dt>
                    <dd>
                        <input name="" type="checkbox" value="" class="iradio" /><label>不限</label>
                        <input name="" type="checkbox" value="" class="iradio" /><label>5星/豪华型</label>
                        <input name="" type="checkbox" value="" class="iradio" /><label>4星/高档型</label>
                        <input name="" type="checkbox" value="" class="iradio" /><label>3星/舒适型</label>
                        <input name="" type="checkbox" value="" class="iradio" /><label>2星/经济型</label>
                        <input name="" type="checkbox" value="" class="iradio" /><label>2星级以下/公寓</label>
                    </dd>
                </dl>
                
                <dl>
                    <dt>酒店设施：</dt>
                    <dd>
                        <input name="" type="checkbox" value="" class="iradio" /><label>不限</label>
                        <input name="" type="checkbox" value="" class="iradio" /><label>免费宽带</label>
                        <input name="" type="checkbox" value="" class="iradio" /><label>房间配电脑</label>
                        <input name="" type="checkbox" value="" class="iradio" /><label>免费停车场</label>
                        <input name="" type="checkbox" value="" class="iradio" /><label>游泳池</label>
                        <input name="" type="checkbox" value="" class="iradio" /><label>健身房</label>
                        <input name="" type="checkbox" value="" class="iradio" /><label>会议室</label>
                        <input name="" type="checkbox" value="" class="iradio" /><label>新开业/装修</label>
                    </dd>
                </dl>
                
                <dl class="odd">
                    <dt>酒店品牌：</dt>
                    <dd>
                        <input name="" type="radio" value="" class="iradio" /><label>不限</label>
                        <input name="" type="radio" value="" class="iradio" /><label>如家</label>
                        <input name="" type="radio" value="" class="iradio" /><label>汉庭</label>
                        <input name="" type="radio" value="" class="iradio" /><label>洲际</label>
                        <input name="" type="radio" value="" class="iradio" /><label>雅高</label>
                        <input name="" type="radio" value="" class="iradio" /><label>首旅建国</label>
                        <input name="" type="radio" value="" class="iradio" /><label>港中旅维景</label>
                        <a href="#">更多品牌</a>
                    </dd>
                </dl>
                
                <dl>
                    <dt>酒店价格：</dt>
                    <dd>
                        <input name="" type="radio" value="" class="iradio" /><label>不限</label>
                        <input name="" type="radio" value="" class="iradio" /><label>200元以下</label>
                        <input name="" type="radio" value="" class="iradio" /><label>200-500元</label>
                        <input name="" type="radio" value="" class="iradio" /><label>500-800元</label>
                        <input name="" type="radio" value="" class="iradio" /><label>800元以上</label>
                        <a href="#">自定义价格</a>
                    </dd>
                </dl>
                
                <dl class="odd">
                    <dt>地理位置：</dt>
                    <dd>
                        <a href="#">商业区</a>
                        <a href="#">行政区</a>
                        <a href="#">交通枢纽</a>
                        <a href="#">大学医院</a>
                        <a href="#">地铁周边</a>
                    </dd>
                </dl>
                       
           </div>
        </form>
            <div class="reqbot"></div>
        </div>
        <a id="pushpull" class="contract pull" href="javascript:void(0);">展开筛选条件</a>
        <!--==E search box-->
