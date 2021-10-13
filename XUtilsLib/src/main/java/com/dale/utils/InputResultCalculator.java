package com.dale.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.TextView;

import java.util.List;

/**
 * @createTime ：2019/3/16
 * @function description : edit text 结果计算
 */
public class InputResultCalculator {

    private boolean[] results;

    public InputResultCalculator(List<TextView> inputs, ResultObserver observer) {
        this.mObserve = observer;
        this.results = new boolean[inputs.size()];

        for (int i = 0; i < inputs.size(); i++) {

            TextView editText = inputs.get(i);

            results[i] = !TextUtils.isEmpty(editText.getText().toString());

            int finalI = i;

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    results[finalI] = !TextUtils.isEmpty(s);
                    if (mObserve != null) {
                        boolean ok = true;
                        for (int i1 = 0; i1 < results.length; i1++) {
                            ok = results[i1] && ok;
                            if (!ok) {
                                mObserve.sendResult(ok);
                                return;
                            }
                        }
                        mObserve.sendResult(ok);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private ResultObserver mObserve;

    public interface ResultObserver {
        void sendResult(boolean ok);
    }
}
