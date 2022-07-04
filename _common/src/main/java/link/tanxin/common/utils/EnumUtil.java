package link.tanxin.common.utils;

import link.tanxin.common.request.CodeEnum;

public class EnumUtil {

    /** 通过code获取枚举*/
    public static <T extends CodeEnum> T getEnumByCode(Integer code, Class<T> enumClass) {
        for (T each : enumClass.getEnumConstants()) {
            if(code.equals(each.getCode())){
                return  each;
            }
        }
        return null;
    }
}
