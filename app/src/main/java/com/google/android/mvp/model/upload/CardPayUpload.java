package com.google.android.mvp.model.upload;

import java.io.Serializable;

/**
 * 卡密支付
 */
public class CardPayUpload implements Serializable {
    private String cardno;

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }
}
