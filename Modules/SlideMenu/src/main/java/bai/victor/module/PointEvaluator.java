package bai.victor.module;

import android.animation.TypeEvaluator;
import android.graphics.PointF;
import android.util.Log;

import static android.R.attr.y;

/**
 * Created by Victor-Bai on 2017/7/11.
 */

public class PointEvaluator implements TypeEvaluator<PointF>{
    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        float x = startValue.x + fraction * (endValue.x - startValue.x);
        float y = startValue.y + fraction * (endValue.y - startValue.y);
        return new PointF(x, y);
    }
}
