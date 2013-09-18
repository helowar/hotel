<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<script type="text/javascript"">
function hideDiv(){
document.getElementById('orderStatesDiv').style.display="none";
}
</script>

<div id="orderStatesDiv" class="order_hotel_pop03" style="display:none;position:absolute;z-index:999;opacity: 100;">
   <h2><span class="ht_pop_close" onclick="hideDiv()" >立即关闭</span>订单状态说明</h2>
   <table cellpadding="0" cellspacing="0">
      <tr>
         <th width="15%">订单状态</th>
         <th width="40%"  style="padding-left: 80px;">状态说明</th>
         <th width="23%" align="padding-left: 40px;">网站</th>
         <th width="22%" align="padding-left: 40px;">电话</th>
      </tr>
      <tr>
         <td><span class="orange">未提交</span></td>
         <td>您的预订尚未完成，订单未生效；如需受理该订单，请致电芒果网客户服务中心：40066-40066。</td>
         <td valign="top">可取消</td>
         <td valign="top">可修改、取消</td>
      </tr>
      <tr>
         <td><span class="orange">处理中</span></td>
         <td>芒果网正在受理您的订单。</td>
        <td valign="top">不可修改、可取消（担保预付需致电）</td>
         <td valign="top">可修改、取消</td>
      </tr>
      <tr>
         <td><span class="orange">预订成功</span></td>
         <td>您预订的酒店已确认。</td>
         <td valign="top">可取消（担保预付需致电）</td>
         <td valign="top">可修改、取消</td>
      </tr>
      <tr>
         <td><span class="orange">交易成功</span></td>
         <td>您预订的酒店已成功入住。</td>
          <td valign="top">不可修改取消</td>
          <td valign="top">不可修改取消</td>
      </tr>
      <tr>
          <td><span class="orange">预订取消</span></td>
          <td>您取消订单或未按期入住酒店。</td>
          <td valign="top">不可修改取消</td>
          <td valign="top">不可修改取消</td>
      </tr>
   </table>
</div>
