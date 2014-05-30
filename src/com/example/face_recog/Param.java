package com.example.face_recog;

import android.os.Parcel;
import android.os.Parcelable;

public class Param implements Parcelable {

	
	String url, name, pic, type,address, associations,education, company;
	
	

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAssociations() {
		return associations;
	}

	public void setAssociations(String associations) {
		this.associations = associations;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	

	@Override
	public String toString() {
		return "Param [url=" + url + ", name=" + name + ", pic=" + pic
				+ ", type=" + type + ", address=" + address + ", associations="
				+ associations + ", education=" + education + ", company="
				+ company + "]";
	}

	public Param(Parcel in) {
		// TODO Auto-generated constructor stub
        this.url =  in.readString();;
		this.name =  in.readString();;
		this.pic =  in.readString();;
		this.type = in.readString();
		this.address = in.readString();
		this.associations =in.readString();
		this.education = in.readString();
		this.company =in.readString();
		
	}
	public Param(String url2, String name2, String pic2, String type2) {
		// TODO Auto-generated constructor stub
	   this.url = url2;
	   this.name = name2;
	   this.pic = pic2;
	   this.type = type2;
	   
	}
	
	public Param(String url2, String name2, String pic2, String type2, String location) {
		// TODO Auto-generated constructor stub
	   this.url = url2;
	   this.name = name2;
	   this.pic = pic2;
	   this.type = type2;
	   this.education = location;
	   
	}
	

	public Param(String url, String name, String pic, String type,
			String address, String associations, String education,
			String company) {
		super();
		this.url = url;
		this.name = name;
		this.pic = pic;
		this.type = type;
		this.address = address;
		this.associations = associations;
		this.education = education;
		this.company = company;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		 dest.writeString(url);
		 dest.writeString(name);
		 dest.writeString(pic);
		 dest.writeString(type);
		 dest.writeString(address);
		 dest.writeString(associations);
		 dest.writeString(education);
		 dest.writeString(company);
		
	}
	
		
    public static final Parcelable.Creator<Param> CREATOR = new Parcelable.Creator<Param>() {
        public Param createFromParcel(Parcel in) {
            return new Param(in);
        }

        public Param[] newArray(int size) {
            return new Param[size];
        }
    };
}
