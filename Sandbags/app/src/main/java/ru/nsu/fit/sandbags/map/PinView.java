package ru.nsu.fit.sandbags.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.ArrayList;
import java.util.List;

import ru.nsu.fit.sandbags.R;

public class PinView extends SubsamplingScaleImageView {

    private final static Integer[] pins_res = {
            R.drawable.pin_0,
            R.drawable.pin_0f,
            R.drawable.pin_1,
            R.drawable.pin_1f,
            R.drawable.pin_2,
            R.drawable.pin_2f,
            R.drawable.pin_3,
            R.drawable.pin_3f,
            R.drawable.pin_4,
            R.drawable.pin_4f,
            R.drawable.pin_5,
            R.drawable.pin_5f,
            R.drawable.pin_6,
            R.drawable.pin_6f,
            R.drawable.pin_7,
            R.drawable.pin_7f,
            R.drawable.pin_8,
            R.drawable.pin_8f,
            R.drawable.pin_9,
            R.drawable.pin_9f,
            R.drawable.pin_10,
            R.drawable.pin_10f
    };

    private final Paint paint = new Paint();
    private final PointF vPin = new PointF();
    private List<PinStruct> sPins = new ArrayList<>();
    private final List<Bitmap> pins = new ArrayList<>();

    public PinView(Context context) {
        this(context, null);
    }

    public PinView(Context context, AttributeSet attr) {
        super(context, attr);
        initialise();
    }

    public void setPins(List<PinStruct> sPins) {
        this.sPins = sPins;
        initialise();
        invalidate();
    }

    public void refreshPins() {
        initialise();
        invalidate();
    }

    private void initialise() {
        float density = getResources().getDisplayMetrics().densityDpi;
        for (int i = 0; i < sPins.size(); i++) {
            Bitmap pin;
            pin = BitmapFactory.decodeResource(
                    this.getResources(),
                    pins_res[sPins.get(i).getNum() * 2 + (sPins.get(i).isFollow() ? 1 : 0)]);
            float w = (density / 420f) * pin.getWidth();
            float h = (density / 420f) * pin.getHeight();
            pins.add(i, Bitmap.createScaledBitmap(pin, (int) w, (int) h, true));
        }
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isReady()) {
            return;
        }

        paint.setAntiAlias(true);
        if (sPins != null) {
            for (int i = 0; i < sPins.size(); i++) {
                if (pins.get(i) != null) {
                    sourceToViewCoord(sPins.get(i).getPoint(), vPin);
                    float vX = vPin.x - (float) (pins.get(i).getWidth() / 2);
                    float vY = vPin.y - pins.get(i).getHeight();
                    canvas.drawBitmap(pins.get(i), vX, vY, paint);
                }
            }
        }
    }
}
