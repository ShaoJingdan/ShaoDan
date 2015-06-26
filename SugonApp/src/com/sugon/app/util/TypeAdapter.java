package com.sugon.app.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.sugon.app.MainActivity;
import com.sugon.app.R;
import com.sugon.app.dto.Brand;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class TypeAdapter extends BaseAdapter{
	private int product_click_position=-1;
	private Context context;
	private List<Brand> BrandList;
	private List<Integer> list=new ArrayList<Integer>();
	private List<String> types=new ArrayList<String>();
	private List<String> select_types=new ArrayList<String>();
	private String type_item="";
	 public TypeAdapter(Context context, List<Brand> brandList) {
		super();
		
		this.context = context;
		BrandList = brandList;
		for(int i=0;i<BrandList.size();i++){
			select_types.add("");
			list.add(1);
		}
		
	}

	@Override
	public int getCount() {
		return BrandList.size();
	}

	@Override
	public Object getItem(int position) {
		return BrandList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null || (holder = (ViewHolder)convertView.getTag()).flag != position ){
			holder = new ViewHolder();
			if(convertView != null)
			System.out.println("(holder = (ViewHolder)convertView.getTag()).flag等于================"+((ViewHolder)convertView.getTag()).flag);
			
			//isShow[position] = true; //该分类已经显示了
			convertView = LayoutInflater.from(context).inflate(R.layout.listview_item, null);
			//parent.setBackgroundColor(Color.BLUE);
			
			Brand brand = BrandList.get(position);
			 final XGridView gridView=(XGridView) convertView.findViewById(R.id.gridView);
			 if(list.get(position)%2==0){
				 gridView.setVisibility(View.VISIBLE);
			 }
			 else
			 gridView.setVisibility(View.GONE);
			 
			 holder.amountView=(Button) convertView.findViewById(R.id.list_item_amount);
			 final Button amountView=(Button) convertView.findViewById(R.id.list_item_amount);
			 holder.amountView.setTag(position);
			 if(select_types.get(position).equals("")){
				 amountView.setText("选择产品类型");
			 }
			 else
				 amountView.setText(select_types.get(position));
			 holder.amountView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					//select_types.set(position, "");
					int temp=list.get(position);
					
					temp++;
					list.set(position, temp);
					if(temp%2==0){
						gridView.setVisibility(View.VISIBLE);
					}
					else
					gridView.setVisibility(View.GONE);
					System.out.println("dddddddddddddddddddddddddddddddddddddddddd"+list.get(position));
					System.out.println("你点击的position是"+position);
					product_click_position=position;
					
				}
			});
			
			
			holder.gridView=gridView;
			holder.titleView = (TextView)convertView.findViewById(R.id.list_item_title);
			
			types =brand.types;
			ArrayList<HashMap<String, Object>> gridview_list = new ArrayList<HashMap<String, Object>>();
			
			for(int i=0;i<types.size();i++)
			{
				HashMap<String, Object> map = new HashMap<String, Object>();
				System.out.println("hhhhhhhhhhhhhhhhh"+types.get(i));
				map.put("product_type", types.get(i));
				gridview_list.add(map);
			}
			final SimpleAdapter adapter = new SimpleAdapter(context, gridview_list, R.layout.gridview_item,
	                new String[] {"product_type"}, new int[] {R.id.gv_item_text});
			/*holder.gridView.setAdapter(new myAdapter(context, gridview_list, R.layout.gridview_item,
	                new String[] {"product_type"}, new int[] {R.id.gv_item_text}));*/
			holder.gridView.setAdapter(adapter);
			holder.gridView.setOnItemClickListener(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position,
						long arg3) {
					List<String> types_2=new ArrayList<String>();
					Brand brand = BrandList.get(product_click_position);
					types_2 =brand.types;
					type_item=select_types.get(product_click_position);
					type_item+=types_2.get(position);
					select_types.set(product_click_position, type_item);
					//select_types.add(types_2.get(position));
					//amountView.setText(types_2.get(position));
					System.out.println("88888888888888888888888888888888888"+type_item);
					System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+position);
					amountView.setText(type_item);
					for(int i=0;i<parent.getCount();i++){
						View v=parent.getChildAt(i);
						if (position == i) {
						 view.setBackgroundColor(Color.YELLOW);
						
						System.out.println("0000000000000000000000000000000"+view.getTag());
						if(view.getTag()==null){
							
						}else if(view.getTag()=="0"){
							
						}
						else{
							
						}
						 
						 } else {
						
						}
						
						
					}
					
				}
			});
			
			
			
			
			holder.titleView.setText(brand.name);
			
			
			convertView.setTag(holder);
		}
		
		return convertView;
		
	}
	public final class ViewHolder{

		public TextView  titleView;
		public Button  amountView;
		public XGridView gridView;
		public TextView gv_item_text;
		int flag = -1;
	}

}
