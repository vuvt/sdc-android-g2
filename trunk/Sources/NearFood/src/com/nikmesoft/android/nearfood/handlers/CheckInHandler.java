package com.nikmesoft.android.nearfood.handlers;

import android.util.Log;


public class CheckInHandler extends BaseHandler{
	Integer id_place=-1,id_checkin=-1;
	
	@Override
	protected void processNotEndErrorCode(String localName) {
		if (localName.equalsIgnoreCase("id_place")){
			id_place=Integer.parseInt(builder.toString().trim());
		}if (localName.equalsIgnoreCase("id_checkin")){
			id_checkin=Integer.parseInt(builder.toString().trim());
		}
	}

	@Override
	public void processNotStartErrorCode(String localName) {
		if (localName.equalsIgnoreCase("id_place")){
			id_place=-1;
		}if (localName.equalsIgnoreCase("id_checkin")){
			id_checkin=-1;
		}
		
	}

	@Override
	protected Object returnNotErrorCode() {
		if(id_place!=-1&&id_checkin!=-1){
			Integer[] res = {id_place,id_checkin};

			Log.d("id_place", "id_place: "+id_place);
			Log.d("id_checkin", "id_checkin: "+id_checkin);
			return res;
		}
		return null;
	}

}
