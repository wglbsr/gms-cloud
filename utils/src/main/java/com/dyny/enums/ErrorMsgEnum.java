package com.dyny.enums;

/**
 * @project: lms
 * @description:
 * @author: wanggl
 * @create: 2018-11-25 10:00
 **/
public enum ErrorMsgEnum {

    TIMEOUT(111, "超时!"),
    NO_AUTH(112, "没有权限!"),
    WRONG_STATE(113, "错误的状态!"),
    PHONE_EXIST(114, "该号码已经注册!"),
    PHONE_NOT_EXIST(115, "该号码不存在!"),
    WRONG_CHECK_CODE(100, "验证码错误!");

    private Integer code;
    private String name;

    private ErrorMsgEnum(Integer state, String name) {
        this.code = state;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static ErrorMsgEnum getEnumByCode(int code) {
        for (ErrorMsgEnum errorMsgEnum : values()) {
            if (errorMsgEnum.getCode() == code) {
                return errorMsgEnum;
            }
        }
        return null;
    }

}
