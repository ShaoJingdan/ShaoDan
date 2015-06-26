package com.sugon.app.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sugon.app.R;
import com.sugon.app.util.TypeAdapter.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class myAdapter extends SimpleAdapter{
	private Context context;

	public myAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return super.getCount();
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return super.getDropDownView(position, convertView, parent);
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return super.getItem(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return super.getItemId(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 final ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.gridview_item, null);
			
			holder.gv_item_text=(TextView) convertView.findViewById(R.id.gv_item_text);
			convertView.setTag(holder);
			//holder.gv_item_text=gv_item_text;
			//holder.gv_item_text.setTag("0");
			holder.gv_item_text.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(holder.gv_item_text.getTag()==null){
						holder.gv_item_text.setTag("0");
						holder.gv_item_text.setBackgroundColor(Color.YELLOW);
					}
					if(holder.gv_item_text.getTag()=="0"){
						holder.gv_item_text.setTag("1");
						holder.gv_item_text.setBackgroundColor(Color.BLACK);
					}
					else{
						holder.gv_item_text.setTag("0");
						holder.gv_item_text.setBackgroundColor(Color.YELLOW);
					}
					
				}
			});
		}
		else
			holder=(ViewHolder)convertView.getTag();
		
		return super.getView(position, convertView, parent);
	}
	public  class ViewHolder{
		public TextView gv_item_text;
	}



	

}
