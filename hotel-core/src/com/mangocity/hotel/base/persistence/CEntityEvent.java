package com.mangocity.hotel.base.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import com.mangocity.util.*;

/**
 */
public class CEntityEvent implements Serializable {

    public static List setCEntity(List list, String userName, String userId) {

        if (null != list) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof Entity) {
                    if (null == ((Entity) list.get(i)).getID()
                        || 0 == ((Entity) list.get(i)).getID()) {
                        ((CEntity) list.get(i)).setCreate_by(userName);
                        ((CEntity) list.get(i)).setCreate_by_id((null == userId ? "" : userId));
                    }
                    ((CEntity) list.get(i)).setModify_by(userName);
                    ((CEntity) list.get(i)).setModify_by_id((null == userId ? "" : userId));
                    ((CEntity) list.get(i)).setModify_time(new Date());
                }
            }
        }
        return list;
    }

    public static Object setCEntity(Object obj, String userName, String userId) {
        if (obj instanceof Entity) {
            if (null == ((Entity) obj).getID() || 0 == ((Entity) obj).getID()) {
                ((CEntity) obj).setCreate_by(userName);
                ((CEntity) obj).setCreate_by_id((null == userId ? "" : userId));
            }
            ((CEntity) obj).setModify_by(userName);
            ((CEntity) obj).setModify_by_id((null == userId ? "" : userId));
            ((CEntity) obj).setModify_time(new Date());
        }
        return obj;
    }
}
