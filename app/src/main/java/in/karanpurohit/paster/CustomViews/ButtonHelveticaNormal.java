package in.karanpurohit.paster.CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import in.karanpurohit.paster.R;

/**
 * Created by karan on 7/11/16.
 */

public class ButtonHelveticaNormal extends Button {

    public ButtonHelveticaNormal(Context context) {
        super(context);
        initFont();
    }

    public ButtonHelveticaNormal(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFont();
    }

    public ButtonHelveticaNormal(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFont();
    }

    void initFont(){
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                getContext ().getString (R.string.helveticaBold));
        setTypeface(tf);
    }
}
