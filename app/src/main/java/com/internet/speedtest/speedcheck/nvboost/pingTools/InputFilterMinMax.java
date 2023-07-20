package com.internet.speedtest.speedcheck.nvboost.pingTools;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;

/* loaded from: classes2.dex */
public class InputFilterMinMax implements InputFilter {
    Context context;
    String errorMsg;
    private int max;
    private int min;

    private boolean isInRange(int i, int i2, int i3) {
        if (i2 > i) {
            if (i3 < i || i3 > i2) {
                return false;
            }
            return true;
        } else if (i3 < i2 || i3 > i) {
            return false;
        } else {
            return true;
        }
    }

    public InputFilterMinMax(int i, int i2) {
        this.min = i;
        this.max = i2;
    }

    public InputFilterMinMax(String str, String str2, Context context, String str3) {
        this.min = Integer.parseInt(str);
        this.max = Integer.parseInt(str2);
        this.context = context;
        this.errorMsg = str3;
    }

    @Override // android.text.InputFilter
    public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
        try {
            if (isInRange(this.min, this.max, Integer.parseInt(charSequence.toString()))) {
                return null;
            }
            return "";
        } catch (NumberFormatException e) {
            return charSequence;
        }
    }
}
