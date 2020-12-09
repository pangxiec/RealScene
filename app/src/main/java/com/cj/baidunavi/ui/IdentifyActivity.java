package com.cj.baidunavi.ui;

import android.content.Intent;
import android.view.MotionEvent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.cj.baidunavi.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import static org.opencv.core.Core.FONT_HERSHEY_PLAIN;
import static org.opencv.core.Core.FONT_HERSHEY_SIMPLEX;

public class IdentifyActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener
{
    private CameraBridgeViewBase openCvCameraView;
    private CascadeClassifier cascadeClassifier;
    private Mat grayscaleImage;
    private int absoluteFaceSize;

    public static int CAMERA_FRONT = 0;
    public static int CAMERA_BACK = 1;
    private  int camera_scene = CAMERA_BACK;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status){
            switch (status){
                case LoaderCallbackInterface.SUCCESS:
                    initializeOpenCVDependencies();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    private void initializeOpenCVDependencies(){
        try{
            InputStream is = getResources().openRawResource(R.raw.cascade);
            File casadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(casadeDir,"lbpcascade_frontalface.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1){
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();

            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());

        } catch (Exception e) {
            Log.e("OpenCVActivity","Error Loading casade",e);
        }

        openCvCameraView.enableView();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_identify);

        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relative);

        openCvCameraView = new JavaCameraView(this, CameraBridgeViewBase.CAMERA_ID_BACK);
        openCvCameraView.setCvCameraViewListener(this);


        final Button button = new Button(IdentifyActivity.this);

        button.setText("切换摄像头");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (camera_scene == CAMERA_FRONT) {
                    relativeLayout.removeAllViews();
                    openCvCameraView.disableView();
                    openCvCameraView = null;
                    cascadeClassifier = null;

                    openCvCameraView = new JavaCameraView(IdentifyActivity.this, CameraBridgeViewBase.CAMERA_ID_BACK);
                    openCvCameraView.setCvCameraViewListener(IdentifyActivity.this);
                    openCvCameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_BACK);

                    camera_scene = CAMERA_BACK;

                    relativeLayout.addView(openCvCameraView);
                    relativeLayout.addView(button);

                    initializeOpenCVDependencies();
                } else {
                    relativeLayout.removeAllViews();
                    openCvCameraView.disableView();
                    openCvCameraView = null;
                    cascadeClassifier = null;

                    openCvCameraView = new JavaCameraView(IdentifyActivity.this, CameraBridgeViewBase.CAMERA_ID_FRONT);
                    openCvCameraView.setCvCameraViewListener(IdentifyActivity.this);
                    openCvCameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT);

                    camera_scene = CAMERA_FRONT;

                    relativeLayout.addView(openCvCameraView);
                    relativeLayout.addView(button);

                    initializeOpenCVDependencies();
                }
            }
        });

        relativeLayout.addView(openCvCameraView);
        relativeLayout.addView(button);

        if (camera_scene == CAMERA_FRONT) {
            openCvCameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT);

        } else if (camera_scene == CAMERA_BACK) {
            openCvCameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_BACK);

        }
    }

    @Override
    public void onCameraViewStarted(int width, int height)
    {
        grayscaleImage = new Mat(height, width, CvType.CV_8UC4);

        absoluteFaceSize = (int)(height * 0.2);
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(Mat aInputFrame)
    {
        final int IN_WIDTH = 300;
        final int IN_HEIGHT = 300;
        final float WH_RATIO = (float)IN_WIDTH / IN_HEIGHT;
        final double IN_SCALE_FACTOR = 0.007843;
        final double MEAN_VAL = 127.5;
        final double THRESHOLD = 0.2;

        Imgproc.cvtColor(aInputFrame, grayscaleImage, Imgproc.COLOR_RGBA2RGB);


        //使前置的图像也是正的
        if (camera_scene == CAMERA_FRONT) {
            Core.flip(aInputFrame, aInputFrame, 1);
            Core.flip(grayscaleImage, grayscaleImage, 1);
        }


        MatOfRect faces = new MatOfRect();

        if (cascadeClassifier != null){
            cascadeClassifier.detectMultiScale(grayscaleImage,faces,1.1,2,2,new Size(absoluteFaceSize,absoluteFaceSize),new Size());
        }

//        Mat blob = Dnn.blobFromImage(aInputFrame, IN_SCALE_FACTOR,
//                new Size(IN_WIDTH, IN_HEIGHT),
//                new Scalar(MEAN_VAL, MEAN_VAL, MEAN_VAL), false);
//        net.setInput(blob);
//        Mat detections = net.forward();
//        detections = detections.reshape(1, (int)detections.total() / 7);

        Rect[] faceArray = faces.toArray();
        String label = "library";
        for (int i = 0; i < faceArray.length; i++) {
//            Log.e("TAG", "onCameraFrame: " + faceArray.length);
            Imgproc.rectangle(aInputFrame, faceArray[i].tl(), faceArray[i].br(),
                    new Scalar(0, 255, 0, 255), 1);

            Imgproc.putText(aInputFrame, label, faceArray[0].tl(), FONT_HERSHEY_PLAIN,
                    7, new Scalar(255,0, 0, 0),3,8,false);
//            Imgproc.//jiaoyou
        }
        return aInputFrame;
    }

    @Override
    public void onResume(){
        super.onResume();
        if (!OpenCVLoader.initDebug()){
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallback);

        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

    }

    //点击屏幕跳转
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                Intent intent = new Intent(IdentifyActivity.this,LibraryActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}
