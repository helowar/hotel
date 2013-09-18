package com.mangocity.hotel.base.service.assistant;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlFavourableDecrease;
import com.mangocity.util.bean.DateComponent;

/**
 */
public class CutDate implements Serializable {

    public static Map cut(DateComponent cutComponent, List oldList) {
        Map result = new HashMap();
        List tempList = new ArrayList();
        boolean hasAdd = false;
        for (int j = 0; j < oldList.size(); j++) {
            DateComponent oldRecord = (DateComponent) oldList.get(j);
            if (cutComponent.getBeginDate().before(oldRecord.getBeginDate())) {
                // 1/如果新元素在已排序的list的两个之间,就直接把新元素放在中间
                if (cutComponent.getEndDate().before(oldRecord.getBeginDate())
                    || compareDate(cutComponent.getEndDate(), oldRecord.getBeginDate())) {
                    // 如果new的结束日期和old的开始日期相等，则old的开始日期要延后一天
                    if (compareDate(cutComponent.getEndDate(), oldRecord.getBeginDate())) {
                        // 如果old延后一天的开始日期比结束日期要大，则这条old记录要被删掉
                        if (new Date(oldRecord.getBeginDate().getTime() + 1 * 24 * 60 * 60 * 1000)
                            .after(oldRecord.getEndDate())) {
                            tempList.add(oldList.get(j));
                        } else {
                            oldRecord.setBeginDate(new Date(oldRecord.getBeginDate().getTime() + 1
                                * 24 * 60 * 60 * 1000));
                        }
                    }
                    oldList.add(cutComponent);
                    for (int m = oldList.size() - 1; m > j; m--) {
                        oldList.set(m, oldList.get(m - 1));
                    }
                    oldList.set(j, cutComponent);

                    if (0 != tempList.size()) {
                        oldList.removeAll(tempList);
                    }

                    hasAdd = true;
                    break;
                }
                // 3/如果new的开始在old的开始之前，new的结束在old的开始结束之间或old的结束之后，可能跨越多个old，又分为两种情况：
                // a/new的结束在跨越的某个对象的开始和结束之间
                // b/new的结束在跨越的某两个对象的空隙
                else {
                    for (int k = j; k < oldList.size(); k++) {
                        DateComponent ko = (DateComponent) oldList.get(k);
                        if (cutComponent.getEndDate().before(ko.getBeginDate())
                            || compareDate(cutComponent.getEndDate(), ko.getBeginDate())) {
                            // 如果new的结束日期和old的开始日期相等，则old的开始日期要延后一天
                            if (compareDate(cutComponent.getEndDate(), ko.getBeginDate())) {
                                // 如果old延后一天的开始日期比结束日期要大，则这条old记录要被删掉
                                if (new Date(ko.getBeginDate().getTime() + 1 * 24 * 60 * 60 * 1000)
                                    .after(ko.getEndDate())) {
                                    tempList.add(oldList.get(k));
                                } else {
                                    ko.setBeginDate(new Date(ko.getBeginDate().getTime() + 1 * 24
                                        * 60 * 60 * 1000));
                                }
                            }
                            break;
                            // 如果new的结束日期和old的结束日期之前，则old的开始日期要在new的开始日期的后一天
                        } else if (cutComponent.getEndDate().before(ko.getEndDate())) {
                            ko.setBeginDate(new Date(cutComponent.getEndDate().getTime() + 1 * 24
                                * 60 * 60 * 1000));
                            break;
                        } else {
                            tempList.add(oldList.get(k));
                        }
                    }
                    if (null != tempList && 0 != tempList.size()) {
                        oldList.removeAll(tempList);
                    }
                    oldList.add(cutComponent);
                    hasAdd = true;
                    for (int mm = oldList.size() - 1; mm > j; mm--) {
                        oldList.set(mm, oldList.get(mm - 1));
                    }
                    oldList.set(j, cutComponent);
                    break;
                }
            } else if (cutComponent.getBeginDate().before(oldRecord.getEndDate())) {

                if (cutComponent.getEndDate().before(oldRecord.getEndDate())
                    || compareDate(cutComponent.getEndDate(), oldRecord.getEndDate())) {
                    boolean flag = false;
                    // 如果new的起始日期等于old的起始日期
                    if (compareDate(cutComponent.getBeginDate(), oldRecord.getBeginDate())) {
                        // 如果new的结束日期等于old的结束日期，则删掉old
                        if (compareDate(cutComponent.getEndDate(), oldRecord.getEndDate())) {
                            tempList.add(oldRecord);
                        } else {// 否则，old的开始日期为new的结束日期的后面一天
                            oldRecord.setBeginDate(new Date(cutComponent.getEndDate().getTime() + 1
                                * 24 * 60 * 60 * 1000));
                            flag = true;
                        }

                    }// 如果new的结束日期等于old的结束日期，则old的结束日期为new的开始日期的前面一天
                    else if (compareDate(cutComponent.getEndDate(), oldRecord.getEndDate())) {
                        oldRecord.setEndDate(new Date(cutComponent.getBeginDate().getTime() - 1
                            * 24 * 60 * 60 * 1000));
                    } else {// 如果new的开始日期大于old的开始日期，new的结束日期小于old的结束日期
                        DateComponent someNew = new DateComponent();
                        someNew.setBeginDate(new Date(cutComponent.getEndDate().getTime() + 1 * 24
                            * 60 * 60 * 1000));
                        someNew.setEndDate(oldRecord.getEndDate());
                        // someNew是old被从中间截断后最后面的那段
                        someNew.setId(oldRecord.getId());
                        oldList.add(someNew);
                        for (int m = oldList.size() - 1; m > j + 1; m--) {
                            oldList.set(m, oldList.get(m - 1));
                        }
                        oldList.set(j + 1, someNew);
                        oldRecord.setEndDate(new Date(cutComponent.getBeginDate().getTime() - 1
                            * 24 * 60 * 60 * 1000));
                    }
                    if (null != tempList || 0 < tempList.size()) {
                        oldList.removeAll(tempList);
                    }
                    oldList.add(cutComponent);
                    hasAdd = true;
                    for (int m = oldList.size() - 1; m > j; m--) {
                        oldList.set(m, oldList.get(m - 1));
                    }
                    if (oldList.contains(oldRecord)) {
                        if (flag) {
                            oldList.set(j + 1, oldRecord);
                            oldList.set(j, cutComponent);
                        } else {
                            oldList.set(j + 1, cutComponent);
                        }
                    } else {
                        oldList.set(j, cutComponent);
                    }
                } else {
                    for (int k = j; k < oldList.size(); k++) {
                        DateComponent ko = (DateComponent) oldList.get(k);
                        if (cutComponent.getEndDate().before(ko.getBeginDate())
                            || compareDate(cutComponent.getEndDate(), ko.getBeginDate())) {
                            if (compareDate(cutComponent.getEndDate(), ko.getBeginDate())) {
                                // 如果old延后一天的开始日期比结束日期要大，则这条old记录要被删掉
                                if (new Date(ko.getBeginDate().getTime() + 1 * 24 * 60 * 60 * 1000)
                                    .after(ko.getEndDate())) {
                                    tempList.add(oldList.get(k));
                                } else {
                                    ko.setBeginDate(new Date(ko.getBeginDate().getTime() + 1 * 24
                                        * 60 * 60 * 1000));
                                }
                            }
                            break;
                        } else if (cutComponent.getEndDate().before(ko.getEndDate())) {
                            ko.setBeginDate(new Date(cutComponent.getEndDate().getTime() + 1 * 24
                                * 60 * 60 * 1000));
                            break;
                        } else {
                            if (k == j) {
                                if (compareDate(cutComponent.getBeginDate(), ko.getBeginDate())) {
                                    tempList.add(ko);
                                    continue;
                                }
                            } else {
                                tempList.add(ko);
                            }

                        }
                    }
                    if (!compareDate(cutComponent.getBeginDate(), oldRecord.getBeginDate())) {
                        oldRecord.setEndDate(new Date(cutComponent.getBeginDate().getTime() - 1
                            * 24 * 60 * 60 * 1000));
                    }

                    if (null != tempList && 0 != tempList.size()) {
                        oldList.removeAll(tempList);
                    }
                    oldList.add(cutComponent);
                    hasAdd = true;
                    for (int m = oldList.size() - 1; m > j; m--) {
                        oldList.set(m, oldList.get(m - 1));
                    }
                    if (oldList.contains(oldRecord)) {
                        oldList.set(j + 1, cutComponent);
                    } else {
                        oldList.set(j, cutComponent);
                    }
                }

                break;
            }
            // 如果new的起始日期等于old的结束日期
            else if (compareDate(cutComponent.getBeginDate(), oldRecord.getEndDate())) {
                // 如果old的结束日期提前一天小于old的开始日期，则old要删掉
                if (new Date(oldRecord.getEndDate().getTime() - 1 * 24 * 60 * 60 * 1000)
                    .before(oldRecord.getBeginDate())) {
                    tempList.add(oldRecord);
                } else {
                    oldRecord.setEndDate(new Date(oldRecord.getEndDate().getTime() - 1 * 24 * 60
                        * 60 * 1000));
                }
            }
        }
        if (false == hasAdd) {
            oldList.add(cutComponent);
        }
        result.put("remove", tempList);
        result.put("update", oldList);
        return result;
    }

    public static Map cut(List cutList, List oldList) {
        Map result = new HashMap();
        List tempList = new ArrayList();
        for (int i = 0; i < cutList.size(); i++) {
            DateComponent newRecord = (DateComponent) cutList.get(i);
            boolean hasAdd = false;
            for (int j = 0; j < oldList.size(); j++) {
                DateComponent oldRecord = (DateComponent) oldList.get(j);
                if (newRecord.getBeginDate().before(oldRecord.getBeginDate())) {
                    // 1/如果新元素在已排序的list的两个之间,就直接把新元素放在中间
                    if (newRecord.getEndDate().before(oldRecord.getBeginDate())
                        || compareDate(newRecord.getEndDate(), oldRecord.getBeginDate())) {
                        oldList.add(newRecord);
                        for (int m = oldList.size() - 1; m > j; m--) {
                            oldList.set(m, oldList.get(m - 1));
                        }
                        oldList.set(j, newRecord);
                        hasAdd = true;
                        break;
                    }
                    // 3/如果new的开始在old的开始之前，new的结束在old的开始结束之间或old的结束之后，可能跨越多个old，又分为两种情况：
                    // a/new的结束在跨越的某个对象的开始和结束之间
                    // b/new的结束在跨越的某两个对象的空隙
                    else {
                        for (int k = j; k < oldList.size(); k++) {
                            DateComponent ko = (DateComponent) oldList.get(k);
                            if (newRecord.getEndDate().before(ko.getBeginDate())
                                || compareDate(newRecord.getEndDate(), ko.getBeginDate())) {
                                break;
                            } else if (newRecord.getEndDate().before(ko.getEndDate())) {
                                ko.setBeginDate(newRecord.getEndDate());
                                break;
                            } else {
                                tempList.add(oldList.get(k));
                            }
                        }
                        if (null != tempList && 0 != tempList.size()) {
                            oldList.removeAll(tempList);
                        }
                        oldList.add(newRecord);
                        hasAdd = true;
                        for (int mm = oldList.size() - 1; mm > j; mm--) {
                            oldList.set(mm, oldList.get(mm - 1));
                        }
                        oldList.set(j, newRecord);
                        break;
                    }
                } else if (newRecord.getBeginDate().before(oldRecord.getEndDate())) {

                    if (newRecord.getEndDate().before(oldRecord.getEndDate())
                        || compareDate(newRecord.getEndDate(), oldRecord.getEndDate())) {
                        boolean flag = false;
                        if (compareDate(newRecord.getBeginDate(), oldRecord.getBeginDate())) {
                            if (compareDate(newRecord.getEndDate(), oldRecord.getEndDate())) {
                                tempList.add(oldRecord);
                            } else {
                                oldRecord.setBeginDate(newRecord.getEndDate());
                                flag = true;
                            }

                        } else if (compareDate(newRecord.getEndDate(), oldRecord.getEndDate())) {
                            oldRecord.setEndDate(newRecord.getBeginDate());
                        } else {
                            DateComponent someNew = new DateComponent();
                            someNew.setBeginDate(newRecord.getEndDate());
                            someNew.setEndDate(oldRecord.getEndDate());
                            // someNew是old被从中间截断后最后面的那段
                            someNew.setId(oldRecord.getId());
                            oldList.add(someNew);
                            for (int m = oldList.size() - 1; m > j + 1; m--) {
                                oldList.set(m, oldList.get(m - 1));
                            }
                            oldList.set(j + 1, someNew);
                            oldRecord.setEndDate(newRecord.getBeginDate());
                        }
                        if (null != tempList || 0 < tempList.size()) {
                            oldList.removeAll(tempList);
                        }
                        oldList.add(newRecord);
                        hasAdd = true;
                        for (int m = oldList.size() - 1; m > j; m--) {
                            oldList.set(m, oldList.get(m - 1));
                        }
                        if (oldList.contains(oldRecord)) {
                            if (flag) {
                                oldList.set(j + 1, oldRecord);
                                oldList.set(j, newRecord);
                            } else {
                                oldList.set(j + 1, newRecord);
                            }
                        } else {
                            oldList.set(j, newRecord);
                        }
                    } else {
                        for (int k = j; k < oldList.size(); k++) {
                            DateComponent ko = (DateComponent) oldList.get(k);
                            if (newRecord.getEndDate().before(ko.getBeginDate())
                                || compareDate(newRecord.getEndDate(), ko.getBeginDate())) {
                                break;
                            } else if (newRecord.getEndDate().before(ko.getEndDate())) {
                                ko.setBeginDate(newRecord.getEndDate());
                                break;
                            } else {
                                if (k == j) {
                                    if (compareDate(newRecord.getBeginDate(), ko.getBeginDate())) {
                                        tempList.add(ko);
                                        continue;
                                    }
                                } else {
                                    tempList.add(ko);
                                }

                            }
                        }
                        if (!compareDate(newRecord.getBeginDate(), oldRecord.getBeginDate())) {
                            oldRecord.setEndDate(newRecord.getBeginDate());
                        }

                        if (null != tempList && 0 != tempList.size()) {
                            oldList.removeAll(tempList);
                        }
                        oldList.add(newRecord);
                        hasAdd = true;
                        for (int m = oldList.size() - 1; m > j; m--) {
                            oldList.set(m, oldList.get(m - 1));
                        }
                        if (oldList.contains(oldRecord)) {
                            oldList.set(j + 1, newRecord);
                        } else {
                            oldList.set(j, newRecord);
                        }
                    }

                    break;
                }
            }
            if (false == hasAdd) {
                oldList.add(newRecord);
            }
        }
        result.put("remove", tempList);
        result.put("update", oldList);
        return result;
    }

    public static boolean compareDate(Date nD, Date oD) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String so = fmt.format(oD);
        String sn = fmt.format(nD);
        return so.equals(sn);
    }

    public static List sort(List sourcelist) {
        if (null == sourcelist) {
            return null;
        }

        for (int i = 1; i < sourcelist.size(); i++) {
            DateComponent aRecord = (DateComponent) sourcelist.get(i);
            for (int j = 0; j < i; j++) {
                DateComponent bRecord = (DateComponent) sourcelist.get(j);
                if (bRecord.getBeginDate().after(aRecord.getBeginDate())) {
                    for (int m = i; m > j; m--) {
                        sourcelist.set(m, sourcelist.get(m - 1));
                    }
                    sourcelist.set(j, aRecord);
                    break;
                }
            }

        }

        return sourcelist;
    }

    // 判断时间段是否重叠，允许某个的起始日期等于另一个的结束日期
    public static boolean compareConflict(List sourcelist) {
        for (int i = 0; i < sourcelist.size(); i++) {
            DateComponent aRecord = (DateComponent) sourcelist.get(i);
            for (int j = i + 1; j < sourcelist.size(); j++) {
                DateComponent bRecord = (DateComponent) sourcelist.get(j);
                if (!(aRecord.getEndDate().before(bRecord.getBeginDate()) || aRecord.getBeginDate()
                    .after(bRecord.getEndDate()))) {
                    return true;
                }
            }
        }
        return false;
    }

	/**
     *  优惠立减同个时间段不用星期不用覆盖  add by zhijie.gu hotel2.9.3  2009-10-28 
     */
    public static Map cutWithWeek(DateComponent cutComponent, List oldList){
    	
    	Map result = new HashMap();
        List tempList = new ArrayList();
        boolean hasAdd = false;
        for (int j = 0; j < oldList.size(); j++) {
            DateComponent oldRecord = (DateComponent) oldList.get(j);
            if (cutComponent.getBeginDate().before(oldRecord.getBeginDate())) {
                // 1/如果新元素在已排序的list的两个之间,就直接把新元素放在中间
                if (cutComponent.getEndDate().before(oldRecord.getBeginDate())
                    || compareDate(cutComponent.getEndDate(), oldRecord.getBeginDate())) {
                    // 如果new的结束日期和old的开始日期相等，则old的开始日期要延后一天
                    if (compareDate(cutComponent.getEndDate(), oldRecord.getBeginDate())) {
                        // 如果old延后一天的开始日期比结束日期要大，则这条old记录要被删掉
                        if (new Date(oldRecord.getBeginDate().getTime() + 1 * 24 * 60 * 60 * 1000)
                            .after(oldRecord.getEndDate())) {
                        	if(cutComponent.getWeeks().equals(oldRecord.getWeeks())){
                        		tempList.add(oldList.get(j));
                        	}
                            
                        } else {
                        	
                        	if(cutComponent.getWeeks().equals(oldRecord.getWeeks())){
                        		 oldRecord.setBeginDate(new Date(oldRecord.getBeginDate().getTime() + 1
                                         * 24 * 60 * 60 * 1000));
                        	}
                           
                        }
                    }
                    oldList.add(cutComponent);
                    for (int m = oldList.size() - 1; m > j; m--) {
                        oldList.set(m, oldList.get(m - 1));
                    }
                    oldList.set(j, cutComponent);

                    if (0 != tempList.size()) {
                        oldList.removeAll(tempList);
                    }

                    hasAdd = true;
                    break;
                }
                // 3/如果new的开始在old的开始之前，new的结束在old的开始结束之间或old的结束之后，可能跨越多个old，又分为两种情况：
                // a/new的结束在跨越的某个对象的开始和结束之间
                // b/new的结束在跨越的某两个对象的空隙
                else {
                    for (int k = j; k < oldList.size(); k++) {
                        DateComponent ko = (DateComponent) oldList.get(k);
                        if (cutComponent.getEndDate().before(ko.getBeginDate())
                            || compareDate(cutComponent.getEndDate(), ko.getBeginDate())) {
                            // 如果new的结束日期和old的开始日期相等，则old的开始日期要延后一天
                            if (compareDate(cutComponent.getEndDate(), ko.getBeginDate())) {
                                // 如果old延后一天的开始日期比结束日期要大，则这条old记录要被删掉
                                if (new Date(ko.getBeginDate().getTime() + 1 * 24 * 60 * 60 * 1000)
                                    .after(ko.getEndDate())) {
                                	if(cutComponent.getWeeks().equals(ko.getWeeks())){
                                		tempList.add(oldList.get(k));
                                	}
                                    
                                } else {
                                	if(cutComponent.getWeeks().equals(ko.getWeeks())){
                                		ko.setBeginDate(new Date(ko.getBeginDate().getTime() + 1 * 24
                                                * 60 * 60 * 1000));
                                	}
                                    
                                }
                            }
                            break;
                            // 如果new的结束日期和old的结束日期之前，则old的开始日期要在new的开始日期的后一天
                        } else if (cutComponent.getEndDate().before(ko.getEndDate())) {
                        	if(cutComponent.getWeeks().equals(ko.getWeeks())){
                        		 ko.setBeginDate(new Date(cutComponent.getEndDate().getTime() + 1 * 24
                                         * 60 * 60 * 1000));
                        	}
                           
                            break;
                        } else {
                        	
                        	if(cutComponent.getWeeks().equals(ko.getWeeks())){
                        		tempList.add(oldList.get(k));
                        	}
                            
                        }
                    }
                    if (null != tempList && 0 != tempList.size()) {
                        oldList.removeAll(tempList);
                    }
                    oldList.add(cutComponent);
                    hasAdd = true;
                    for (int mm = oldList.size() - 1; mm > j; mm--) {
                        oldList.set(mm, oldList.get(mm - 1));
                    }
                    oldList.set(j, cutComponent);
                    break;
                }
            } else if (cutComponent.getBeginDate().before(oldRecord.getEndDate())) {

                if (cutComponent.getEndDate().before(oldRecord.getEndDate())
                    || compareDate(cutComponent.getEndDate(), oldRecord.getEndDate())) {
                    boolean flag = false;
                    // 如果new的起始日期等于old的起始日期
                    if (compareDate(cutComponent.getBeginDate(), oldRecord.getBeginDate())) {
                        // 如果new的结束日期等于old的结束日期，则删掉old
                    	if (compareDate(cutComponent.getEndDate(), oldRecord.getEndDate())) {
                    		
                    		if(cutComponent.getWeeks().equals(oldRecord.getWeeks())){
                    			
                    			tempList.add(oldRecord);
                    			
                    		}
                            
                        }else{// 否则，old的开始日期为new的结束日期的后面一天
                        	
                        	if(cutComponent.getWeeks().equals(oldRecord.getWeeks())){
                        		
                        		 oldRecord.setBeginDate(new Date(cutComponent.getEndDate().getTime() + 1
                                         * 24 * 60 * 60 * 1000));
                                    
                        	}
                        	 flag = true;
                           
                        }

                    }// 如果new的结束日期等于old的结束日期，则old的结束日期为new的开始日期的前面一天
                    else if (compareDate(cutComponent.getEndDate(), oldRecord.getEndDate())) {
                    	if(cutComponent.getWeeks().equals(oldRecord.getWeeks())){
                    		 oldRecord.setEndDate(new Date(cutComponent.getBeginDate().getTime() - 1
                                     * 24 * 60 * 60 * 1000));
                    	}
                       
                    } else {// 如果new的开始日期大于old的开始日期，new的结束日期小于old的结束日期
                    	if(cutComponent.getWeeks().equals(oldRecord.getWeeks())){
                    		DateComponent someNew = new DateComponent();
                            someNew.setBeginDate(new Date(cutComponent.getEndDate().getTime() + 1 * 24
                                * 60 * 60 * 1000));
                            someNew.setEndDate(oldRecord.getEndDate());
                            // someNew是old被从中间截断后最后面的那段
                            someNew.setId(oldRecord.getId());
                            oldList.add(someNew);
                            for (int m = oldList.size() - 1; m > j + 1; m--) {
                                oldList.set(m, oldList.get(m - 1));
                            }
                            oldList.set(j + 1, someNew);
                            oldRecord.setEndDate(new Date(cutComponent.getBeginDate().getTime() - 1
                                * 24 * 60 * 60 * 1000));
                    	}
                        
                    }
                    if (null != tempList || 0 < tempList.size()) {
                        oldList.removeAll(tempList);
                    }
                    oldList.add(cutComponent);
                    hasAdd = true;
                    for (int m = oldList.size() - 1; m > j; m--) {
                        oldList.set(m, oldList.get(m - 1));
                    }
                    if (oldList.contains(oldRecord)) {
                        if (flag) {
                            oldList.set(j + 1, oldRecord);
                            oldList.set(j, cutComponent);
                        } else {
                            oldList.set(j + 1, cutComponent);
                        }
                    } else {
                        oldList.set(j, cutComponent);
                    }
                } else {
                    for (int k = j; k < oldList.size(); k++) {
                        DateComponent ko = (DateComponent) oldList.get(k);
                        if (cutComponent.getEndDate().before(ko.getBeginDate())
                            || compareDate(cutComponent.getEndDate(), ko.getBeginDate())) {
                            if (compareDate(cutComponent.getEndDate(), ko.getBeginDate())) {
                                // 如果old延后一天的开始日期比结束日期要大，则这条old记录要被删掉
                                if (new Date(ko.getBeginDate().getTime() + 1 * 24 * 60 * 60 * 1000)
                                    .after(ko.getEndDate())) {
                                	if(cutComponent.getWeeks().equals(ko.getWeeks())){
                                		tempList.add(oldList.get(k));
                                	}
                                    
                                } else {
                                	if(cutComponent.getWeeks().equals(ko.getWeeks())){
                                		ko.setBeginDate(new Date(ko.getBeginDate().getTime() + 1 * 24
                                                * 60 * 60 * 1000));
                                	}
                                    
                                }
                            }
                            break;
                        } else if (cutComponent.getEndDate().before(ko.getEndDate())) {
                        	if(cutComponent.getWeeks().equals(ko.getWeeks())){
                        		ko.setBeginDate(new Date(cutComponent.getEndDate().getTime() + 1 * 24
                                        * 60 * 60 * 1000));
                        	}
                            break;
                        } else {
                            if (k == j) {
                                if (compareDate(cutComponent.getBeginDate(), ko.getBeginDate())) {
                                	if(cutComponent.getWeeks().equals(ko.getWeeks())){
                                		tempList.add(ko);
                                	}
                                    
                                    continue;
                                }
                            } else {
                            	if(cutComponent.getWeeks().equals(ko.getWeeks())){
                            		 tempList.add(ko);
                            	}
                               
                            }

                        }
                    }
                    if (!compareDate(cutComponent.getBeginDate(), oldRecord.getBeginDate())) {
                    	if(cutComponent.getWeeks().equals(oldRecord.getWeeks())){
                    		 oldRecord.setEndDate(new Date(cutComponent.getBeginDate().getTime() - 1
                                     * 24 * 60 * 60 * 1000));
                    	}
                       
                    }

                    if (null != tempList && 0 != tempList.size()) {
                        oldList.removeAll(tempList);
                    }
                    oldList.add(cutComponent);
                    hasAdd = true;
                    for (int m = oldList.size() - 1; m > j; m--) {
                        oldList.set(m, oldList.get(m - 1));
                    }
                    if (oldList.contains(oldRecord)) {
                        oldList.set(j + 1, cutComponent);
                    } else {
                        oldList.set(j, cutComponent);
                    }
                }

                break;
            }
            // 如果new的起始日期等于old的结束日期
            else if (compareDate(cutComponent.getBeginDate(), oldRecord.getEndDate())) {
                // 如果old的结束日期提前一天小于old的开始日期，则old要删掉
                if (new Date(oldRecord.getEndDate().getTime() - 1 * 24 * 60 * 60 * 1000)
                    .before(oldRecord.getBeginDate())) {
                	if(cutComponent.getWeeks().equals(oldRecord.getWeeks())){
                		if(cutComponent.getWeeks().equals(oldRecord.getWeeks())){
                			tempList.add(oldRecord);
                		}
                		
                	}
                    
                } else {
                	if(cutComponent.getWeeks().equals(oldRecord.getWeeks())){
                		if(cutComponent.getWeeks().equals(oldRecord.getWeeks())){
                			oldRecord.setEndDate(new Date(oldRecord.getEndDate().getTime() - 1 * 24 * 60
                                    * 60 * 1000));
                		}
                		
                	}
                    
                }
            }
        }
        if (false == hasAdd) {
            oldList.add(cutComponent);
        }
        result.put("remove", tempList);
        result.put("update", oldList);
        return result;
    	
    }
    
  
    
   //根据时间和星期来排序
    public static List sort(List sourcelist,HtlFavourableDecrease htlFavourableDecrease) {
        if (null == sourcelist) {
            return null;
        }

        for (int i = 1; i < sourcelist.size(); i++) {
            DateComponent aRecord = (DateComponent) sourcelist.get(i);
            for (int j = 0; j < i; j++) {
                DateComponent bRecord = (DateComponent) sourcelist.get(j);
                if (bRecord.getBeginDate().after(aRecord.getBeginDate())) {
                	for (int m = i; m > j; m--) {
                        sourcelist.set(m, sourcelist.get(m - 1));
                    }
                    sourcelist.set(j, aRecord);
                    break;
                }
            }

        }
        
        //改变原有记录中起止时间相同的排序（起止时间相同，星期不同的记录）， 把星期跟新记录相同的记录放在起止时间相同、星期不同的记录的排序的第一。
        for (int ii = 0; ii < sourcelist.size(); ii++) {
        	for (int jj = ii+1; jj < sourcelist.size(); jj++) {
        		DateComponent bRecord = (DateComponent) sourcelist.get(ii);
            	 DateComponent aRecord = (DateComponent) sourcelist.get(jj);
            	 if (!(htlFavourableDecrease.getEndDate().before(aRecord.getBeginDate()) 
            			 || htlFavourableDecrease.getBeginDate().after(aRecord.getEndDate()))){
                 	
                 	if(htlFavourableDecrease.getWeek().equals(aRecord.getWeeks())){
                 		sourcelist.set(ii,aRecord);
                 		sourcelist.set(jj,bRecord);
                 	}
                 	
                 }
            }
        	
        }
        

        return sourcelist;
    }
    
    //根据时间和星期来排序
    public static List sort(List sourcelist,Date beginDate,Date endDate,String week) {
        if (null == sourcelist) {
            return null;
        }

        for (int i = 1; i < sourcelist.size(); i++) {
            DateComponent aRecord = (DateComponent) sourcelist.get(i);
            for (int j = 0; j < i; j++) {
                DateComponent bRecord = (DateComponent) sourcelist.get(j);
                if (bRecord.getBeginDate().after(aRecord.getBeginDate())) {
                	for (int m = i; m > j; m--) {
                        sourcelist.set(m, sourcelist.get(m - 1));
                    }
                    sourcelist.set(j, aRecord);
                    break;
                }
            }

        }
        
        //改变原有记录中起止时间相同的排序（起止时间相同，星期不同的记录）， 把星期跟新记录相同的记录放在起止时间相同、星期不同的记录的排序的第一。
        for (int ii = 0; ii < sourcelist.size(); ii++) {
        	for (int jj = ii+1; jj < sourcelist.size(); jj++) {
        		DateComponent bRecord = (DateComponent) sourcelist.get(ii);
            	 DateComponent aRecord = (DateComponent) sourcelist.get(jj);
            	 if (!(endDate.before(aRecord.getBeginDate()) 
            			 || beginDate.after(aRecord.getEndDate()))){
                 	
                 	if(week.equals(aRecord.getWeeks())){
                 		sourcelist.set(ii,aRecord);
                 		sourcelist.set(jj,bRecord);
                 	}
                 	
                 }
            }
        	
        }
        

        return sourcelist;
    }

    
    
    // 判断时间段(包括星期)是否重叠，允许某个的起始日期等于另一个的结束日期
    public static boolean compareConflictWithWeek(List sourcelist) {
        for (int i = 0; i < sourcelist.size(); i++) {
            DateComponent aRecord = (DateComponent) sourcelist.get(i);
            for (int j = i + 1; j < sourcelist.size(); j++) {
                DateComponent bRecord = (DateComponent) sourcelist.get(j);
                if (!(aRecord.getEndDate().before(bRecord.getBeginDate()) || aRecord.getBeginDate()
                    .after(bRecord.getEndDate()))) {
                	if(aRecord.getWeeks().equals(bRecord.getWeeks())){
                		
                		return true;
                		
                	}	
                	
                }
            }
        }
        return false;
    }
    
    public static Map cutForRoomStatusSchedule(DateComponent cutComponent, List oldList) {
        Map result = new HashMap();
        List tempList = new ArrayList();
        boolean hasAdd = false;
        for (int j = 0; j < oldList.size(); j++) {
            DateComponent oldRecord = (DateComponent) oldList.get(j);
            if (cutComponent.getBeginDate().before(oldRecord.getBeginDate())) {
                // 1/如果新元素在已排序的list的两个之间,就直接把新元素放在中间
                if (cutComponent.getEndDate().before(oldRecord.getBeginDate())
                    || compareDate(cutComponent.getEndDate(), oldRecord.getBeginDate())) {
                    // 如果new的结束日期和old的开始日期相等，则old的开始日期要延后一天
                    if (compareDate(cutComponent.getEndDate(), oldRecord.getBeginDate())) {
                        // 如果old延后一天的开始日期比结束日期要大，则这条old记录要被删掉
                        if (new Date(oldRecord.getBeginDate().getTime() + 1 * 24 * 60 * 60 * 1000)
                            .after(oldRecord.getEndDate())) {
                            tempList.add(oldList.get(j));
                        } else {
                            oldRecord.setBeginDate(new Date(oldRecord.getBeginDate().getTime() + 1
                                * 24 * 60 * 60 * 1000));
                        }
                    }
                    oldList.add(cutComponent);
                    for (int m = oldList.size() - 1; m > j; m--) {
                        oldList.set(m, oldList.get(m - 1));
                    }
                    oldList.set(j, cutComponent);

                    if (0 != tempList.size()) {
                        oldList.removeAll(tempList);
                    }

                    hasAdd = true;
                    break;
                }
                // 3/如果new的开始在old的开始之前，new的结束在old的开始结束之间或old的结束之后，可能跨越多个old，又分为两种情况：
                // a/new的结束在跨越的某个对象的开始和结束之间
                // b/new的结束在跨越的某两个对象的空隙
                else {
                    for (int k = j; k < oldList.size(); k++) {
                        DateComponent ko = (DateComponent) oldList.get(k);
                        if (cutComponent.getEndDate().before(ko.getBeginDate())
                            || compareDate(cutComponent.getEndDate(), ko.getBeginDate())) {
                            // 如果new的结束日期和old的开始日期相等，则old的开始日期要延后一天
                            if (compareDate(cutComponent.getEndDate(), ko.getBeginDate())) {
                                // 如果old延后一天的开始日期比结束日期要大，则这条old记录要被删掉
                                if (new Date(ko.getBeginDate().getTime() + 1 * 24 * 60 * 60 * 1000)
                                    .after(ko.getEndDate())) {
                                    tempList.add(oldList.get(k));
                                } else {
                                    ko.setBeginDate(new Date(ko.getBeginDate().getTime() + 1 * 24
                                        * 60 * 60 * 1000));
                                }
                            }
                            break;
                            // 如果new的结束日期和old的结束日期之前，则old的开始日期要在new的开始日期的后一天
                        } else if (cutComponent.getEndDate().before(ko.getEndDate())) {
                            ko.setBeginDate(new Date(cutComponent.getEndDate().getTime() + 1 * 24
                                * 60 * 60 * 1000));
                            break;
                        } else {
                            tempList.add(oldList.get(k));
                        }
                    }
                    if (null != tempList && 0 != tempList.size()) {
                        oldList.removeAll(tempList);
                    }
                    oldList.add(cutComponent);
                    hasAdd = true;
                    for (int mm = oldList.size() - 1; mm > j; mm--) {
                        oldList.set(mm, oldList.get(mm - 1));
                    }
                    oldList.set(j, cutComponent);
                    break;
                }
            } else if (cutComponent.getBeginDate().before(oldRecord.getEndDate())) {

                if (cutComponent.getEndDate().before(oldRecord.getEndDate())
                    || compareDate(cutComponent.getEndDate(), oldRecord.getEndDate())) {
                    boolean flag = false;
                    // 如果new的起始日期等于old的起始日期
                    if (compareDate(cutComponent.getBeginDate(), oldRecord.getBeginDate())) {
                        // 如果new的结束日期等于old的结束日期，则删掉old
                        if (compareDate(cutComponent.getEndDate(), oldRecord.getEndDate())) {
                            tempList.add(oldRecord);
                        } else {// 否则，old的开始日期为new的结束日期的后面一天
                            oldRecord.setBeginDate(new Date(cutComponent.getEndDate().getTime() + 1
                                * 24 * 60 * 60 * 1000));
                            flag = true;
                        }

                    }// 如果new的结束日期等于old的结束日期，则old的结束日期为new的开始日期的前面一天
                    else if (compareDate(cutComponent.getEndDate(), oldRecord.getEndDate())) {
                        oldRecord.setEndDate(new Date(cutComponent.getBeginDate().getTime() - 1
                            * 24 * 60 * 60 * 1000));
                    } else {// 如果new的开始日期大于old的开始日期，new的结束日期小于old的结束日期
                        DateComponent someNew = new DateComponent();
                        someNew.setBeginDate(new Date(cutComponent.getEndDate().getTime() + 1 * 24
                            * 60 * 60 * 1000));
                        someNew.setEndDate(oldRecord.getEndDate());
                        // someNew是old被从中间截断后最后面的那段
                        someNew.setId(oldRecord.getId());
                        oldList.add(someNew);
                        for (int m = oldList.size() - 1; m > j + 1; m--) {
                            oldList.set(m, oldList.get(m - 1));
                        }
                        oldList.set(j + 1, someNew);
                        oldRecord.setEndDate(new Date(cutComponent.getBeginDate().getTime() - 1
                            * 24 * 60 * 60 * 1000));
                    }
                    if (null != tempList || 0 < tempList.size()) {
                        oldList.removeAll(tempList);
                    }
                    oldList.add(cutComponent);
                    hasAdd = true;
                    for (int m = oldList.size() - 1; m > j; m--) {
                        oldList.set(m, oldList.get(m - 1));
                    }
                    if (oldList.contains(oldRecord)) {
                        if (flag) {
                            oldList.set(j + 1, oldRecord);
                            oldList.set(j, cutComponent);
                        } else {
                            oldList.set(j + 1, cutComponent);
                        }
                    } else {
                        oldList.set(j, cutComponent);
                    }
                } else {
                    for (int k = j; k < oldList.size(); k++) {
                        DateComponent ko = (DateComponent) oldList.get(k);
                        if (cutComponent.getEndDate().before(ko.getBeginDate())
                            || compareDate(cutComponent.getEndDate(), ko.getBeginDate())) {
                            if (compareDate(cutComponent.getEndDate(), ko.getBeginDate())) {
                                // 如果old延后一天的开始日期比结束日期要大，则这条old记录要被删掉
                                if (new Date(ko.getBeginDate().getTime() + 1 * 24 * 60 * 60 * 1000)
                                    .after(ko.getEndDate())) {
                                    tempList.add(oldList.get(k));
                                } else {
                                    ko.setBeginDate(new Date(ko.getBeginDate().getTime() + 1 * 24
                                        * 60 * 60 * 1000));
                                }
                            }
                            break;
                        } else if (cutComponent.getEndDate().before(ko.getEndDate())) {
                            ko.setBeginDate(new Date(cutComponent.getEndDate().getTime() + 1 * 24
                                * 60 * 60 * 1000));
                            break;
                        } else {
                            if (k == j) {
                                if (compareDate(cutComponent.getBeginDate(), ko.getBeginDate())) {
                                    tempList.add(ko);
                                    continue;
                                }
                            } else {
                                tempList.add(ko);
                            }

                        }
                    }
                    if (!compareDate(cutComponent.getBeginDate(), oldRecord.getBeginDate())) {
                        oldRecord.setEndDate(new Date(cutComponent.getBeginDate().getTime() - 1
                            * 24 * 60 * 60 * 1000));
                    }

                    if (null != tempList && 0 != tempList.size()) {
                        oldList.removeAll(tempList);
                    }
                    oldList.add(cutComponent);
                    hasAdd = true;
                    for (int m = oldList.size() - 1; m > j; m--) {
                        oldList.set(m, oldList.get(m - 1));
                    }
                    if (oldList.contains(oldRecord)) {
                        oldList.set(j + 1, cutComponent);
                    } else {
                        oldList.set(j, cutComponent);
                    }
                }

                break;
            }
            // 如果new的起始日期等于old的结束日期
            else if (compareDate(cutComponent.getBeginDate(), oldRecord.getEndDate())) {
                // 如果old的结束日期提前一天小于old的开始日期，则old要删掉
                if (new Date(oldRecord.getEndDate().getTime() - 1 * 24 * 60 * 60 * 1000)
                    .before(oldRecord.getBeginDate())) {
                    tempList.add(oldRecord);
                    
                    oldList.remove(oldRecord);
                } else {
                    oldRecord.setEndDate(new Date(oldRecord.getEndDate().getTime() - 1 * 24 * 60
                        * 60 * 1000));
                }
            }
        }
        if (false == hasAdd) {
            oldList.add(cutComponent);
        }
        result.put("remove", tempList);
        result.put("update", oldList);
        return result;
    }

}
