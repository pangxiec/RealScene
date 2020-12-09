package com.cj.baidunavi;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by 大头 on 2020/5/6.
 */
public class PoiUp2Dialog extends Dialog {
    private final String name;
    private final LatLng position;
    private PoiUp2Dialog.PoiUpListener poiUpListener;

    public PoiUp2Dialog( Context context,  String name,  LatLng position) {
        super(context);
        this.name = name;
        this.position = position;
    }

    public final String getName() {
        return this.name;
    }

    protected void onCreate( Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_poiup);
        WindowManager.LayoutParams lp= getWindow().getAttributes();
        if (lp != null) {
            lp.width=WindowManager.LayoutParams.MATCH_PARENT;
            lp.height= (int) (150*Resources.getSystem().getDisplayMetrics().density);
            lp.gravity= Gravity.BOTTOM;
        }
        getWindow().setAttributes(lp);
        TextView var3 = this.findViewById(R.id.tv_name);
        String var4 = this.name;
        var3.setText(var4 != null ? var4 : "");
        findViewById(R.id.tv_to).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PoiUp2Dialog.PoiUpListener var10000 = PoiUp2Dialog.this.getPoiUpListener();
                if (var10000 != null) {
                    var10000.clicked(PoiUp2Dialog.this.getName(), PoiUp2Dialog.this.position);
                }
            }
        });
    }

    public final PoiUp2Dialog.PoiUpListener getPoiUpListener() {
        return this.poiUpListener;
    }

    public final void setPoiUpListener( PoiUp2Dialog.PoiUpListener var1) {
        this.poiUpListener = var1;
    }


    public interface PoiUpListener {
        void clicked( String var1,  LatLng var2);
    }


}
