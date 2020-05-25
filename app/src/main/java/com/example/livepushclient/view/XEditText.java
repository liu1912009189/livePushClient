package com.example.livepushclient.view;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by liule on 2020/5/25.
 * ----2020/5/25-------liule----xxxxxxxxx--
 */
public class XEditText extends androidx.appcompat.widget.AppCompatEditText {
    // 保存设置的所有输入限制
    private List<InputFilter> inputFilters;

    public XEditText(Context context) {
        super(context);
        init();
    }

    public XEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public XEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inputFilters = new ArrayList<>();
    }

    /**
     * 设置允许输入的最大字符数
     */
    public void setMaxLengthFilter(int maxLength) {
        inputFilters.add(new InputFilter.LengthFilter(maxLength));
        setFilters(inputFilters.toArray(new InputFilter[inputFilters.size()]));
    }

    /**
     * 设置可输入小数位
     */
    public void setDecimalFilter(int num) {
        inputFilters.add(new SignedDecimalFilter(0, num));
        setFilters(inputFilters.toArray(new InputFilter[inputFilters.size()]));
    }

    /**
     * 设置最大值
     *
     * @param maxmum       允许的最大值
     * @param numOfDecimal 允许的小数位
     */
    public void setMaxmumFilter(int min,double maxmum, int numOfDecimal) {
        inputFilters.add(new MaxmumFilter(min, maxmum, numOfDecimal));
        setFilters(inputFilters.toArray(new InputFilter[inputFilters.size()]));
    }

    /**
     * 设置只能输入整数
     *
     * @param min 输入整数的最小值
     */
    public void setIntergerFilter(int min) {
        inputFilters.add(new SignedIntegerFilter(min));
        setFilters(inputFilters.toArray(new InputFilter[inputFilters.size()]));
    }

    /**
     * 只能够输入手机号码
     */
    public void setTelFilter() {
        inputFilters.add(new TelephoneNumberInputFilter());
        setFilters(inputFilters.toArray(new InputFilter[inputFilters.size()]));
    }

    /**
     * 小数位数限制
     */
    private static final class SignedDecimalFilter implements InputFilter {

        private final Pattern pattern;

        SignedDecimalFilter(int min, int numOfDecimals) {
            pattern = Pattern.compile("^" + (min < 0 ? "-?" : "")
                    + "[0-9]*\\.?[0-9]" + (numOfDecimals > 0 ? ("{0," + numOfDecimals + "}$") : "*"));
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.equals(".")) {
                if (dstart == 0 || !(dest.charAt(dstart - 1) >= '0' && dest.charAt(dstart - 1) <= '9') || dest.charAt(0) == '0') {
                    return "";
                }
            }
            if (source.equals("0") && (dest.toString()).contains(".") && dstart == 0) { //防止在369.369的最前面输入0变成0369.369这种不合法的形式
                return "";
            }
            StringBuilder builder = new StringBuilder(dest);
            builder.delete(dstart, dend);
            builder.insert(dstart, source);
            if (!pattern.matcher(builder.toString()).matches()) {
                return "";
            }

            return source;
        }
    }

    /**
     * 限制输入最大值
     */
    private static final class MaxmumFilter implements InputFilter {

        private final Pattern pattern;
        private final double maxNum;

        MaxmumFilter(int min, double maxNum, int numOfDecimals) {
            pattern = Pattern.compile("^" + (min < 0 ? "-?" : "")
                    + "[0-9]*\\.?[0-9]" + (numOfDecimals > 0 ? ("{0," + numOfDecimals + "}$") : "*"));
            this.maxNum = maxNum;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.equals(".")) {
                if (dstart == 0 || !(dest.charAt(dstart - 1) >= '0' && dest.charAt(dstart - 1) <= '9') || dest.charAt(0) == '0') {
                    return "";
                }
            }
            if (source.equals("0") && (dest.toString()).contains(".") && dstart == 0) {
                return "";
            }

            StringBuilder builder = new StringBuilder(dest);
            builder.delete(dstart, dend);
            builder.insert(dstart, source);
            if (!pattern.matcher(builder.toString()).matches()) {
                return "";
            }

            if (!TextUtils.isEmpty(builder)) {
                double num = Double.parseDouble(builder.toString());
                if (num > maxNum) {
                    return "";
                }
            }
            return source;
        }
    }

    /**
     * 限制整数
     */
    private static final class SignedIntegerFilter implements InputFilter {
        private final Pattern pattern;

        SignedIntegerFilter(int min) {
            pattern = Pattern.compile("^" + (min < 0 ? "-?" : "") + "[0-9]*$");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            StringBuilder builder = new StringBuilder(dest);
            builder.insert(dstart, source);
            if (!pattern.matcher(builder.toString()).matches()) {
                return "";
            }
            return source;
        }
    }

    /**
     * 限制电话号码
     */
    private static class TelephoneNumberInputFilter implements InputFilter {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            StringBuilder builder = new StringBuilder(dest);
            builder.insert(dstart, source);
            int length = builder.length();
            if (length == 1) {
                if (builder.charAt(0) == '1') {
                    return source;
                } else {
                    return "";
                }
            }

            if (length > 0 && length <= 11) {
                int suffixSize = length - 2;
                Pattern pattern = Pattern.compile("^1[3-9]\\d{" + suffixSize + "}$");
                if (pattern.matcher(builder.toString()).matches()) {
                    return source;
                } else {
                    return "";
                }
            }

            return "";
        }
    }
}

