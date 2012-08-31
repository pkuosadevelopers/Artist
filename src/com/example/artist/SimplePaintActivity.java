package com.example.artist;

import java.io.File;
import java.util.Calendar;

import com.example.artist.MediaUtil.ImageInfo;

import android.util.DisplayMetrics;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.net.Uri;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.*;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

public class SimplePaintActivity extends Activity
        implements View.OnClickListener,
        PenColorPickerDialog.OnColorChangedListener,
        BkColorPickerDialog.OnColorChangedListener,
        SeekBar.OnSeekBarChangeListener{ 
	
	//使用ServiceConnection来监听Service状态的变化   
    private ServiceConnection conn = new ServiceConnection() {  
          
        public void onServiceDisconnected(ComponentName name) {  
            // TODO Auto-generated method stub   
            audioService = null;  
        }  
          
        public void onServiceConnected(ComponentName name, IBinder binder) {  
            //这里我们实例化audioService,通过binder来实现 
            audioService = ((AudioService.AudioBinder)binder).getService();  
              
        }  
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  
        mChoice = this.getIntent().getExtras().getInt("choice");
        	
        dm = new DisplayMetrics();    
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        setContentView(R.layout.view);
        mViewA = (PaintView)this.findViewById(R.id.PaintView);
        
        mEmboss = new EmbossMaskFilter(new float[] { 1, 1, 1 },
                                       0.4f, 6, 3.5f);

        mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
        mEffect = new DashPathEffect(new float[]{20,20,20,20},1);

        mbBkColor = (Button)this.findViewById(R.id.BkColor);
        mbClear = (Button)this.findViewById(R.id.Clear);
        mbPenColor = (Button)this.findViewById(R.id.PenColor);
        mbSize = (Button)this.findViewById(R.id.Size);
        mbEmboss = (Button)this.findViewById(R.id.Emboss);
        mbBlur = (Button)this.findViewById(R.id.Blur);
        mbDash = (Button)this.findViewById(R.id.Dash);
        mbSrcATop = (Button)this.findViewById(R.id.SrcATop);
        mbErase = (Button)this.findViewById(R.id.Erase);
        mbFreeCurve = (Button)this.findViewById(R.id.FreeCurve);
        mbLine = (Button)this.findViewById(R.id.Line);
        mbOval = (Button)this.findViewById(R.id.Oval);
        mbRect = (Button)this.findViewById(R.id.Rect);
        mbDecal = (Button)this.findViewById(R.id.Decal);
        mbChoose = (Button)this.findViewById(R.id.Choose);
        mbSave = (Button)this.findViewById(R.id.Save);
        mbLoad = (Button)this.findViewById(R.id.Load);
        mbBack = (Button)this.findViewById(R.id.Back);
        
        mbBkColor.setOnClickListener(this);
        mbClear.setOnClickListener(this);
        mbPenColor.setOnClickListener(this);
        mbSize.setOnClickListener(this);
        mbEmboss.setOnClickListener(this);
        mbBlur.setOnClickListener(this);
        mbDash.setOnClickListener(this);
        mbSrcATop.setOnClickListener(this);
        mbErase.setOnClickListener(this);
        mbFreeCurve.setOnClickListener(this);
        mbLine.setOnClickListener(this);
        mbOval.setOnClickListener(this);
        mbRect.setOnClickListener(this);
        mbDecal.setOnClickListener(this);
        mbChoose.setOnClickListener(this);
        mbSave.setOnClickListener(this);
        mbLoad.setOnClickListener(this);
        mbBack.setOnClickListener(this);
        
        Intent intent = new Intent();  
        intent.setClass(this, AudioService.class);
        startService(intent);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        
        switch(mChoice)
        {
        case 1: 
        	mbEmboss.setEnabled(false);
        	mbBlur.setEnabled(false);
        	mbDash.setEnabled(false);
        	mbSrcATop.setEnabled(false);
        	
        	mbLine.setEnabled(false);
        	mbOval.setEnabled(false);
        	mbRect.setEnabled(false);
        	mbDecal.setEnabled(false);
        	
        	mbSave.setEnabled(false);
        	mbLoad.setEnabled(false);
        	
        	mbBkColor.setEnabled(false);
        	mbChoose.setEnabled(false);
        	break;
        case 2:
        	mbEmboss.setEnabled(false);
        	mbBlur.setEnabled(false);
        	mbDash.setEnabled(false);
        	mbSrcATop.setEnabled(false);
        	
        	mbSave.setEnabled(false);
        	mbLoad.setEnabled(false);
        	
        	mbBkColor.setEnabled(false);
        	mbChoose.setEnabled(false);
        	break;
        case 3:
        	mbEmboss.setEnabled(false);
        	mbBlur.setEnabled(false);
        	mbDash.setEnabled(false);
        	mbSrcATop.setEnabled(false);
        	
        	mbBkColor.setEnabled(false);
        	mbChoose.setEnabled(false);
        	break;
        case 4:
        	mbEmboss.setEnabled(false);
        	mbBlur.setEnabled(false);
        	mbSrcATop.setEnabled(false);
        	break;
        case 5:
        	break;
        }
    }
    
    private MaskFilter  mEmboss;
    private MaskFilter  mBlur;
    private PathEffect mEffect;
    private DisplayMetrics dm;
    private PaintView mViewA;

    private Button mbBkColor;
    private Button mbClear;
    private Button mbPenColor;
    private Button mbSize;
    private Button mbEmboss;
    private Button mbBlur;
    private Button mbDash;
    private Button mbSrcATop;
    private Button mbErase;
    private Button mbFreeCurve;
    private Button mbLine;
    private Button mbOval;
    private Button mbRect;
    private Button mbDecal;
    private Button mbChoose;
    private Button mbSave;
    private Button mbLoad;
    private Button mbBack;
    
    private int mChoice;
    private EditText mSize;
    private SeekBar mSeekBar;
    private AudioService audioService;
    
    public void showDialog()
    {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog, (ViewGroup) findViewById(R.id.dialog));
        mSize = (EditText)layout.findViewById(R.id.penSize);
        mSeekBar = (SeekBar)layout.findViewById(R.id.seek);
        mSeekBar.setOnSeekBarChangeListener(this);
        mSize.setText(String.valueOf(mViewA.getmPaint().getStrokeWidth()));
        new AlertDialog.Builder(this).setTitle("画笔粗细").setView(layout)
        .setPositiveButton("确定", new OnClickListener(){
        	public void onClick(DialogInterface dialog, int which)
        	{
        		mViewA.getmPaint().setStrokeWidth(Float.valueOf(mSize.getText().toString()));
        	}
        }).setNegativeButton("取消", null).show();
    }
    
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
        mSize.setText(String.valueOf(progress));
        mViewA.getmPaint().setStrokeWidth(progress);
    }
    
    public void penColorChanged(int color) {
        mViewA.getmPaint().setColor(color);
    }
    
    public void bkColorChanged(int color) {
    	mViewA.changeBk(color);
    	mViewA.setmPicture(false);
    }
    
    protected void onSaveClick()
    {
        String strFilePath = getStrokeFilePath();
        Bitmap bmp = mViewA.getCanvasSnapshot();
        if (null != bmp)
        {
            BitmapUtil.saveBitmapToSDCard(bmp, strFilePath);
        }
    }
    
    protected void onLoadClick()
    {
        String strFilePath = getStrokeFilePath();
        Bitmap bmp = BitmapUtil.loadBitmapFromSDCard(strFilePath);
        if (null != bmp)
        {
            mViewA.setForeBitmap(bmp);
        }
    }
    
    public String getStrokeFilePath()
    {
        java.io.File sdcarddir = android.os.Environment.getExternalStorageDirectory();
        String strDir = sdcarddir.getPath() + "/DCIM/sketchpad/";
        String strFileName = getStrokeFileName();
        File file = new File(strDir);
        if (!file.exists())
        {
            file.mkdirs();
        }
        
        String strFilePath = strDir + strFileName;
        
        return strFilePath;
    }
    
    public String getStrokeFileName()
    {
        String strFileName = "";
        
        Calendar rightNow = Calendar.getInstance();
        int year = rightNow.get(Calendar.YEAR);
        int month = rightNow.get(Calendar.MONDAY);
        int date = rightNow.get(Calendar.DATE);
        int hour = rightNow.get(Calendar.HOUR);
        //int minute = rightNow.get(Calendar.MINUTE);
        //int second = rightNow.get(Calendar.SECOND);
        
        strFileName = String.format("%02d%02d%02d%02d.png", year, month, date, hour);
        return strFileName;
    }
    
    public void onClick(View v)
    {
    	mViewA.getmPaint().setXfermode(null);
        mViewA.getmPaint().setAlpha(0xFF);
        mViewA.setmEraser(false);

        switch (v.getId()) {
        	case R.id.BkColor:
        		new BkColorPickerDialog(this, this, mViewA.getmPaint().getColor()).show();
        		break;
        	case R.id.Clear:
        		mViewA.clear();
        		break;
            case R.id.PenColor:
                new PenColorPickerDialog(this, this, mViewA.getmPaint().getColor()).show();
                break;
            case R.id.Emboss:
                if (mViewA.getmPaint().getMaskFilter() != mEmboss) {
                    mViewA.getmPaint().setMaskFilter(mEmboss);
                } else {
                    mViewA.getmPaint().setMaskFilter(null);
                }
                break;
            case R.id.Blur:
                if (mViewA.getmPaint().getMaskFilter() != mBlur) {
                    mViewA.getmPaint().setMaskFilter(mBlur);
                } else {
                    mViewA.getmPaint().setMaskFilter(null);
                }
                break;
            case R.id.Dash:
                if (mViewA.getmPaint().getPathEffect() != mEffect) {
                    mViewA.getmPaint().setPathEffect(mEffect);
                } else {
                    mViewA.getmPaint().setPathEffect(null);
                }
                break;
            case R.id.Erase:	
            	mViewA.setmEraser(true);
            	mViewA.setmShape(1);
            	mViewA.getmPaint().setMaskFilter(null);
            	mViewA.getmPaint().setPathEffect(null);
                mViewA.getmPaint().setXfermode(new PorterDuffXfermode(
                                                        PorterDuff.Mode.CLEAR));
                break;
            case R.id.SrcATop:
                mViewA.getmPaint().setXfermode(new PorterDuffXfermode(
                                                    PorterDuff.Mode.SRC_ATOP));
                mViewA.getmPaint().setAlpha(0x80);
                break;
            case R.id.Size:
            	showDialog();
            	break;
            case R.id.FreeCurve:
            	mViewA.setmShape(1);
            	break;
            case R.id.Line:
            	mViewA.setmShape(2);
            	break;
            case R.id.Oval:
            	mViewA.setmShape(3);
            	break;
            case R.id.Rect:
            	mViewA.setmShape(4);
            	break;
            case R.id.Decal:
            	mViewA.setmShape(5);
            	break;
            case R.id.Save:
            	onSaveClick();
            	break;
            case R.id.Load:
            	onLoadClick();
            	break;
            case R.id.Choose:
            	Intent intent = new Intent(this, PictureSelectActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.Back:
            	Intent intent1 = new Intent();
                intent1.setClass(SimplePaintActivity.this, StartActivity.class);
                startActivity(intent1);
                finish();
                break;
                }
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) 
        {
        case 0: 
            if (RESULT_OK == resultCode) 
            {
                try
                {
                    Bundle bundle = data.getExtras();
                    ImageInfo imgInfo = PictureSelectActivity.getImageInfoFromBundle(bundle);
                    Uri uri = imgInfo.imageUri;
                    
                    ContentResolver cr = this.getContentResolver();
                    Bitmap bmp = BitmapFactory.decodeStream(cr.openInputStream(uri)); 
                    if (null != bmp)
                    {
                    	mViewA.setmPicture(true);
                        mViewA.setBackBitmap(bmp);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            break;
        }
    }
    
    protected void onDestroy()
    {
    	super.onDestroy();
    	Intent intent = new Intent();  
        intent.setClass(this, AudioService.class);
    	unbindService(conn);
    	stopService(intent);
    }

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
}
