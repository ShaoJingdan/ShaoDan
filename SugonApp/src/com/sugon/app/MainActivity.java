package com.sugon.app;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sugon.app.dto.Brand;
import com.sugon.app.util.Request4Image;
import com.sugon.app.util.TypeAdapter;
import com.sugon.app.util.XGridView;
import com.sugon.app.util.XListView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ViewSwitcher.ViewFactory;

public class MainActivity extends Activity implements ViewFactory, OnTouchListener{
	

	
	private ImageSwitcher is_markettop;
	//选中的当前图片序号
    private int currentPosition; 
    private int bitcount = 3;
    //点点数组  
    static private ImageView[] tips;
    private Bitmap[] bitmaps;
    //装载点点的容器 
    private LinearLayout ll_viewgroup; 
    
    private float downX;
    public static MyHandler mHandler;
    private Bitmap default_bitmap;
    private String urls[];
    private static SwitchImageThread switchThread = null;
    
    private LinearLayout sugon_ProductType;
    private LinearLayout else_ProductType;
    private XListView lv_brand;
    private List<Brand> Brand_type;
    private ScrollView scrollView;
    private boolean flag=false;
    private XGridView gridView;
	private List<String> types=new ArrayList<String>();
    
	private int flag0=-1;
	private List<Integer> list=new ArrayList<Integer>();

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		View content=LayoutInflater.from(MainActivity.this).inflate(R.layout.listview_item, null);
		XGridView gridView=(XGridView) content.findViewById(R.id.gridView);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long arg3) {
				System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
				for(int i=0;i<parent.getCount();i++){
					View v=parent.getChildAt(i);
					if (position == i) {
					 view.setBackgroundColor(Color.YELLOW);
					 } else {
					v.setBackgroundColor(Color.TRANSPARENT);
					}
					
				}
			}
		});
		Button list_item_amount=(Button) content.findViewById(R.id.list_item_amount);
		else_ProductType=(LinearLayout) content.findViewById(R.id.Ll_product_type);
		list_item_amount.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				/*click_count++;
				if(click_count%2==0){
					System.out.println("数量"+click_count);
					flag=true;
					gridView.setVisibility(View.VISIBLE);
					
				}
				else
					flag=false;
				System.out.println("数量"+click_count);
				gridView.setVisibility(View.GONE);*/
				
			}
		});
		sugon_ProductType=(LinearLayout) findViewById(R.id.Ll_ProductType);
		sugon_ProductType.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent intent = new Intent();        
				intent.putExtra("url", "www.baidu.com");
				intent.setClass(MainActivity.this, TypeActivity.class);
				startActivity(intent);
			}
		});
		
		//Init
		Brand_type=new ArrayList<Brand>();
		scrollView=(ScrollView) findViewById(R.id.scrollview);
		scrollView.smoothScrollTo(0, 0);
		lv_brand=(XListView) findViewById(R.id.lv_brand);
		lv_brand.setOnItemClickListener(new OnItemClickListener() {
			

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/* int click_count=1;
				 gridView=(XGridView) view.findViewById(R.id.gridView);
				System.out.println("dddddddddddddddddddddddddd");
				parent.setBackgroundColor(Color.GRAY);
				view.setBackgroundColor(Color.BLUE);
				System.out.println("位置"+position);
				System.out.println("id"+id);
				mHandler.sendEmptyMessage(1);
				gridView.setVisibility(View.VISIBLE);*/
				/*click_count++;
				if(click_count%2==0){
					System.out.println("数量"+click_count);
					flag=true;
					gridView.setVisibility(View.VISIBLE);
					
				}
				else
					flag=false;
				System.out.println("数量"+click_count);
				gridView.setVisibility(View.GONE);
				*/
				/*flag0=position;
				gridView=(XGridView) view.findViewById(R.id.gridView);
				int temp=list.get(flag0);
				temp++;
				list.set(flag0, temp);
				if(temp%2==0){
					gridView.setVisibility(View.VISIBLE);
				}
				else
				gridView.setVisibility(View.GONE);
				System.out.println("dddddddddddddddddddddddddddddddddddddddddd"+list.get(flag0));
				System.out.println("你点击的position是"+flag0);*/
			}
			
			
		});
		
		is_markettop = (ImageSwitcher)findViewById(R.id.is_markettop);
		ll_viewgroup = (LinearLayout)findViewById(R.id.ll_viewGroup);
		//绑定imageswitch的事件及工厂
		is_markettop.setFactory(this);
		is_markettop.setOnTouchListener(this);
		//初始设置imageswitch和ll_viewgroup
		currentPosition = 0;
		is_markettop.setImageResource(R.drawable.loading);
		Drawable d= getResources().getDrawable(R.drawable.loading); //xxx根据自己的情况获取drawable
		BitmapDrawable bd = (BitmapDrawable) d;
		default_bitmap = bd.getBitmap();
		mHandler = new MyHandler();
		if(switchThread == null){
			switchThread = new SwitchImageThread();
			switchThread.start();
		}
		if(!switchThread.isAlive())
			switchThread.start();
		new SetDataListThread().start();

	}

	private void switchRight(){
		if(currentPosition < bitcount - 1){  
        	is_markettop.setInAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.right_in));  
        	is_markettop.setOutAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.left_out));  
            currentPosition ++ ;  
            setdrawable(); 
        }else{  
        	is_markettop.setInAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.right_in));  
            is_markettop.setOutAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.left_out));  
            currentPosition = 0;  
            setdrawable();
        }
	}

	@SuppressWarnings("deprecation")
	@Override
	//设置图片显示比例
	public View makeView() {
		final ImageView i = new ImageView(this);  
        i.setBackgroundColor(0xff000000);  
        i.setScaleType(ImageView.ScaleType.CENTER_CROP);  
        i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));  
        return i; 
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {  
        case MotionEvent.ACTION_DOWN:{  
            //手指按下的X坐标  
            downX = event.getX();  
            break;  
        }  
        case MotionEvent.ACTION_UP:{  
            float lastX = event.getX();  
            //抬起的时候的X坐标大于按下的时候就显示上一张图片  
            if(lastX > downX){  
                if(currentPosition > 0){   
                    is_markettop.setInAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.left_in));  
                    is_markettop.setOutAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.right_out));  
                    currentPosition --;  
                    setdrawable();
                }else{  
                	is_markettop.setInAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.left_in));  
                    is_markettop.setOutAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.right_out));  
                    currentPosition = bitcount - 1;    //循环显示图片，这个currentPosition记录的是图片的编号
                    setdrawable();
                }  
            }   
              
            if(lastX < downX){    //下一张图片
                if(currentPosition < bitcount - 1){  
                	is_markettop.setInAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.right_in));  
                	is_markettop.setOutAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.left_out));  
                    currentPosition ++ ;  
                    setdrawable();
                }else{  
                	is_markettop.setInAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.right_in));  
                    is_markettop.setOutAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.left_out));  
                    currentPosition = 0;  //刚才显示的是最后一张
                    setdrawable();
                }  
            }
            if(lastX == downX){
            	Intent intent = new Intent();        
				intent.putExtra("url", "www.baidu.com");
				intent.setClass(MainActivity.this, DetailActivity.class);
				startActivity(intent);
            	
            }
            	
            }  
              
            break;  
        }  
  
        return true;  
	}
	
	public void setdrawable(){
		if(bitmaps.length == 0)
			return;
		if(bitmaps == null){
			/*if(tiponce){
				//makeToast("请检查网络连接！");
				tiponce = false;
			}*/
			return;
		}
		//Log.d("setdrawable","ok---"+currentPosition+"---"+bitmaps.length);
		if(bitmaps[currentPosition % bitmaps.length] == null)
			is_markettop.setImageDrawable(DrawableFromBitmap(default_bitmap));
		is_markettop.setImageDrawable(DrawableFromBitmap(bitmaps[currentPosition % bitmaps.length]));
		 setViewgroupBackground(currentPosition);
	}
	public Drawable DrawableFromBitmap(Bitmap bitmap) {
		if(bitmap == null)
			Log.d("setdrawable","null");
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		// 因为BtimapDrawable是Drawable的子类，最终直接使用bd对象即可。
		return bd;
		}
	private void setViewgroupBackground(int selectItems) {
		for(int i=0; i<bitcount; i++){    
            if(i == selectItems){    
                tips[i].setBackgroundResource(R.drawable.point_yellow);    
            }else{    
                tips[i].setBackgroundResource(R.drawable.point_white);    
            }
        }    
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	
	}
	private class SetDataListThread extends Thread {
			
			@Override
			public synchronized void run() {
				System.out.println("jggggggggggggggggggggggggggggggggggggggggggggggggggggggg");
				
				try {
	            bitmaps = new Bitmap[bitcount];
					
					for(int i = 0 ; i < bitcount ; i ++){
						bitmaps[i] = default_bitmap;
					}
					urls = new String[bitcount];
					urls[0]="www.taoxueapp.com//sys_image//435ce45eeb014b3f4bf2e9364a3940f6.png";
					urls[1]="www.taoxueapp.com//sys_image//4a6f02cd0ae74acba327af8c0ac4afe.png";
					urls[2]="www.taoxueapp.com//sys_image//270ebd83b4be467a45f28f9f06c74a5b.png";
					mHandler.sendEmptyMessage(44);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				try{
					List<String> test_types=new ArrayList<String>();
					List<String> test_types_2=new ArrayList<String>();
					for(int i=0;i<8;i++) {
						test_types.add("iphone"+i);
					}
					for(int i=0;i<5;i++){
						test_types_2.add("iphone"+i);
					}
					
					for(int i=0;i<3;i++) {
						
						//System.out.println("category count:" + category.getString("items"));
						Brand_type.add(new Brand(i+5,"品牌"+i,test_types
								));
					}
						for(int i=3;i<6;i++){
							Brand_type.add(new Brand(i+5,"品牌"+i,test_types_2
									));
						}
						
					
					mHandler.sendEmptyMessage(0);
					
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
	}
	public class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				lv_brand.setAdapter(new TypeAdapter(MainActivity.this,Brand_type));
				for(int i=0;i<Brand_type.size();i++){
					
					list.add(1);
				}
				break;
			case 1:
				
				break;
			case 3:
				switchRight();
				
				break;
			case 44:
				//填充imageswitcher的图片
				
				tips = new ImageView[bitcount];
				ll_viewgroup.removeAllViews();
				for(int i=0; i<bitcount; i++){  
		            ImageView mImageView = new ImageView(MainActivity.this);  
		            tips[i] = mImageView;  
		            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		           //左右边距
		            layoutParams.rightMargin = 3;  
		            layoutParams.leftMargin = 3;  
		              
		            mImageView.setBackgroundResource(R.drawable.point_white);  
		            ll_viewgroup.addView(mImageView, layoutParams);  
		        } 
				setViewgroupBackground(currentPosition);
				
				new setBitmapsThread(urls).start();
				
				break;
			default:
				break;
			}

		}
	}
	
	//设置bitmaps内容的线程
			private class setBitmapsThread extends Thread{
				String[] picUrls;
				public setBitmapsThread(String[] picUrls){
					this.picUrls = picUrls;
				}
				@Override
				public void run() {
					//可能的解决方案。。。百度的。。。
					Looper.prepare();
					for(int i = 0 ; i < picUrls.length ; i ++){
						
						/*String s = picUrls[i];
						int k = s.split("/").length;
//						Log.d("url",k+"");
						String ss = s.split("/")[k-1];
//						Log.d("url",ss);
						final String s1 = ss.split("\\.")[0];
						Log.d("url",s1);*/
						
							new Request4Image(picUrls[i]) 
							{
								@Override
								protected void onPostExecute(Bitmap result) 
								{
									if(result==null) 
										return;
									//cachebitmap = result;
									//这里祥说的同步块，防止多个线程同时访问bitmaps，造成冲突
										for(int j = 0 ;j < picUrls.length ; j ++){
											if(bitmaps[j].equals(default_bitmap)){
												bitmaps[j] = result;
												Log.d("url",picUrls[j]);
												break;
											}
										}
									
									
								}
							}.execute();
						
						
						
						//bitmaps[i] = cachebitmap;
					}
					
				}
				
				
			}
			private class SwitchImageThread extends Thread{

				@Override
				public void run() {
					while(!Thread.currentThread().isInterrupted()){
						//Log.d("switch","right");
						
						try {
							Thread.sleep(5000);
							
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						mHandler.sendEmptyMessage(3);
					}
				}
				
			}
			
	
}
