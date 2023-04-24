package com.liberty52.product.global.adapter.portone.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PortOnePaymentInfo {
    private String imp_uid;
    private String merchant_uid;
    private String pg_provider;
    private String pg_tid;
    private String name;
    private Long amount;
    private String card_name;
    private String card_number;
    private Integer card_quota;
    private Long paid_at;
    private String customer_uid;

    public static PortOnePaymentInfo testOf(String merchant_uid, String pName, Long amount, String customer_uid) {
        return new PortOnePaymentInfo(
                "IMP_TEST", merchant_uid, "html5_inicis", "PG_TID_TEST", pName, amount,
                "현대카드", "1234123412341234", 0,
                0L, customer_uid
        );
    }
}