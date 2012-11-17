package com.nikmesoft.android.nearfood.handlers;

public class CheckInHandler extends BaseHandler{
	Integer id_place,id_checkin;
	
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
			id_place=0;
		}if (localName.equalsIgnoreCase("id_checkin")){
			id_checkin=0;
		}
		
	}

	@Override
	protected Object returnNotErrorCode() {
		if(id_place!=null&&id_checkin!=null){
			Integer[] res = {id_place,id_checkin};
			return res;
		}
		return null;
	}

}
