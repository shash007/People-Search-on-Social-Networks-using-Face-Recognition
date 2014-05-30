package com.example.face_recog;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

/**
 * Rekognition SDK 
 * This Java SDK is intent for develops who wants to integrate Rekognition API into their 
 * Android application. Each public function in this class represents an API call.
 * Every time you call a function, it will send API request and 
 * wait for response and call the callback function your passed in 
 * once the response is received.
 *
 * Usage:
 *   1. Update the value of sAPI_KEY and sAPI_SECRET to your API key and API secret.
 *   2. Create a callback object with the callback function. 
 *      For example: 
 *      RekoSDK.APICallback callback = new RekoSDK.APICallback(){
 *			public void gotResponse(String sResponse){
 *				print(sResponse);
 *			}
 *		};
 *   3. Call it in your code as simple as
 *      Reko.face_train(callback);
 *      and You're done! 
 * @author      eng@orbe.us     
 * @version     1.0 
 * @since       2013-08-04
 */
public class RekoSDK {
	
	// API Key and API Secret 
	static private String sAPI_KEY = "nWAe5timKSqrUDg3";
	static private String sAPI_SECRET = "FA4S8xeIyMC2rtG6";
	
	public interface APICallback{
        public void gotResponse(String sResponse);
    }

	/**
	 * Make a custom API call with the params your set.
	 * @param params key value pair of you own
	 * @param callbackFunc call back object
	 */
	public static void custom_request(List<NameValuePair> params, final APICallback callbackFunc) {
		callAPICallInAnotherThread(params, callbackFunc);
	}
	
	/**
	 * Face Detect API
	 * For more details:
	 * http://v2.rekognition.com/index.php/developer/docs#facedetect
	 * @param sURL URL to the image for face detection  
	 * @param callbackFunc callback object
	 */
	public static void face_detect(final String sURL, final APICallback callbackFunc) {
		List<NameValuePair> params = getBasicParameters();
		// Call API passing image URL
		addImageDataValuePair(params, sURL, null);
		// Use face_detection jobs to detect face attributes, 
		// you can use different jobs to fulfill different tasks; 
		// you can refer to the documentation pages on ReKognition.com to learn more jobs 
		params.add(new BasicNameValuePair("jobs", "face_aggressive_part_gender_age_glass_smile_emotion")); 
		callAPICallInAnotherThread(params, callbackFunc);
	}
	
	/**
	 * Face Detect API
	 * For more details:
	 * http://v2.rekognition.com/index.php/developer/docs#facedetect
	 * @param b byte array of the image for detection  
	 * @param callbackFunc callback object
	 */
	public static void face_detect(byte[] b, final APICallback callbackFunc) {
		List<NameValuePair> params = getBasicParameters();
		// Call this API with the image data
		addImageDataValuePair(params, null, b);
		params.add(new BasicNameValuePair("jobs", "face_aggressive_part_gender_age_glass_smile_emotion"));
		callAPICallInAnotherThread(params, callbackFunc);
	}

	/**
    * FaceTrain API add a face
 	* For more details: 
	* http://v2.rekognition.com/index.php/developer/docs#facetrain
	* @param sName name of the face in this image
	* @param b byte array of the image to add
	* @param callbackFunc callback object
	*/
	public static void face_add(String sName, byte[] b, final APICallback callbackFunc) {
		face_add(sName, null, b, null, null, callbackFunc);
	}
	
	/**
	* FaceTrain API add a face
	* For more details: 
	* http://v2.rekognition.com/index.php/developer/docs#facetrain
	* @param sName name of the face in this image
	* @param b byte array of the image to add
	* @param name_space Namespace for your app. Example: facebookapp. You can use this field to differentiate your apps. default will be used if it is not set
	* @param user_id User ID of your app. The uploaded file will be added for this user. default will be used if it is not set.
	* @param callbackFunc callback object
	*/
	public static void face_add(String sName, byte[] b, String name_space, String user_id, final APICallback callbackFunc) {
		face_add(sName, null, b, name_space, user_id, callbackFunc);
	}
	
	/**
	* FaceTrain API add a face
	* For more details: 
	* http://v2.rekognition.com/index.php/developer/docs#facetrain
	* @param sName name of the face in this image
	* @param sImageURL URL to the image to add
	* @param callbackFunc callback object
	*/	
	public static void face_add(String sName, String sImageURL, final APICallback callbackFunc) {
		face_add(sName, sImageURL, null, null, null, callbackFunc);
	}
	
	/**
	* FaceTrain API add a face
	* For more details: 
	* http://v2.rekognition.com/index.php/developer/docs#facetrain
	* @param sName name of the face in this image
	* @param sImageURL URL to the image to add
	* @param name_space Namespace for your app. Example: facebookapp. You can use this field to differentiate your apps. default will be used if it is not set
	* @param user_id User ID of your app. The uploaded file will be added for this user. default will be used if it is not set.
	* @param callbackFunc callback object
	*/
	public static void face_add(String sName, String sImageURL, String name_space, String user_id, final APICallback callbackFunc) {
		face_add(sName, sImageURL, null, name_space, user_id, callbackFunc);
	}
	
	/** 
	* FaceTrain API: trigger train event
	* For more details: 
	* http://v2.rekognition.com/index.php/developer/docs#facetrain
	* @param callbackFunc callback object
	*/
	public static void face_train(final APICallback callbackFunc) {
		face_train(null, null, callbackFunc);
	}
	
	/**
	* FaceTrain API: trigger train event
	* For more details: 
	* http://v2.rekognition.com/index.php/developer/docs#facetrain
	* @param name_space Namespace for your app. Example: facebookapp. You can use this field to differentiate your apps. default will be used if it is not set
	* @param user_id User ID of your app. The uploaded file will be added for this user. default will be used if it is not set.
	* @param callbackFunc callback object
	*/
	public static void face_train(String name_space, String user_id, final APICallback callbackFunc) {
		String sJobsValue = "face_train";
		List<NameValuePair> params = getBasicParameters();
		params.add(new BasicNameValuePair("jobs", sJobsValue));
		addNameSpaceAndUserID(params, name_space, user_id);
		callAPICallInAnotherThread(params, callbackFunc);
	}
	
	/**
	 *  Face Crawl API: Using Facebook photos for face training.
	 * For more details: 
	 * http://v2.rekognition.com/index.php/developer/docs#facecrawl
	 * @param friendsFbIDs list of your friends' Facebook ID to crawl 
	 * @param sMyFbID my facebook id
	 * @param sMyAccessToken my facebook acess token
	 * @param callbackFunc callback object
	 */
	public static void face_crawl(String[] friendsFbIDs, String sMyFbID, String sMyAccessToken, final APICallback callbackFunc) {
		String sIDs = TextUtils.join(";", friendsFbIDs);
		String sJobsValue = "face_crawl_[" + sIDs + "]";  
		List<NameValuePair> params = getBasicParameters();
		params.add(new BasicNameValuePair("jobs", sJobsValue));
		params.add(new BasicNameValuePair("fb_id", sMyFbID));
		params.add(new BasicNameValuePair("access_token", sMyAccessToken));
		callAPICallInAnotherThread(params, callbackFunc);
	}

	/**
	 * Face Recognize: Recognizing people in a new image, after Rekognition is trained with the name tags.
	 * For more details: 
	 * http://v2.rekognition.com/index.php/developer/docs#facerekognize
	 * @param sImageURL URL of the image to recognize
	 * @param callbackFunc callback object 
	 */
	public static void face_recognize(String sImageURL, final APICallback callbackFunc) {
		List<NameValuePair> params = getBasicParameters();
		params.add(new BasicNameValuePair("jobs", "face_recognize"));
		addImageDataValuePair(params, sImageURL, null);
		callAPICallInAnotherThread(params, callbackFunc);
	}
	
	/**
	 * Face Recognize: Recognizing people in a new image, after Rekognition is trained with the name tags.
	 * For more details: 
	 * http://v2.rekognition.com/index.php/developer/docs#facerekognize
	 * @param b byte array of the image to recognize
	 * @param callbackFunc callback object 
	 */
	public static void face_recognize(byte[] b, final APICallback callbackFunc) {
		List<NameValuePair> params = getBasicParameters();
		params.add(new BasicNameValuePair("jobs", "face_recognize"));
		addImageDataValuePair(params, null, b);
		callAPICallInAnotherThread(params, callbackFunc);
	}
	
	/**
	 * Face Visualize: Displaying the index of training images of all (or subset) of tags that you have added or crawled.
	 * For more details
	 * http://v2.rekognition.com/index.php/developer/docs#facevirtualize
	 * @param sTags tags you want to visualize, the entire data set will be visualized if not specified
	 * @param callbackFunc callback object 
	 */
	public static void face_visualize(String[] sTags, final APICallback callbackFunc) {
		face_visualize(sTags, null, null, callbackFunc);
	}
	
	/**
	 * Face Visualize: Displaying the index of training images of all (or subset) of tags that you have added or crawled.
	 * For more details
	 * http://v2.rekognition.com/index.php/developer/docs#facevirtualize
	 * @param sTags tags you want to visualize, the entire data set will be visualized if not specified
	 * @param name_space Namespace for your app. Example: facebookapp. You can use this field to differentiate your apps. default will be used if it is not set
	 * @param user_id User ID of your app. The uploaded file will be added for this user. default will be used if it is not set.
	 * @param callbackFunc callback object 
	 */
	public static void face_visualize(String[] sTags, String name_space, String user_id, final APICallback callbackFunc) {
		List<NameValuePair> params = getBasicParameters();
		String sJobValue = "face_visualize[" + TextUtils.join(";", sTags) + "]";
		params.add(new BasicNameValuePair("jobs", sJobValue));
		addNameSpaceAndUserID(params, name_space, user_id);
		callAPICallInAnotherThread(params, callbackFunc);
	}
	
	/**
	 *  Face Search: helps you find similar faces in your database based on your input face image.
	 * For more details
	 * http://v2.rekognition.com/index.php/developer/docs#facesearch 
 	 * @param sTags You can narrow down the search by providing tags in this format: face_search[tag1;tag2;tag3], then it will only search in the mentioned tags/clusters. 
	 * @param sImageURL URL of the image you search
	 * @param callbackFunc callback object 
	 */
	public static void face_search(String[] sTags, String sImageURL, final APICallback callbackFunc) {
		face_search(sTags, sImageURL, null, null, null, -1, callbackFunc);
	}
	
	/**
	 * Face Search: helps you find similar faces in your database based on your input face image.
	 * For more details
	 * http://v2.rekognition.com/index.php/developer/docs#facesearch
	 * @param sTags You can narrow down the search by providing tags in this format: face_search[tag1;tag2;tag3], then it will only search in the mentioned tags/clusters.
	 * @param b byte array of the image you search
	 * @param callbackFunc callback object 
	 */
	public static void face_search(String[] sTags, byte[] b, final APICallback callbackFunc) {
		face_search(sTags, null, b, null, null, -1, callbackFunc);
	}
	
	/**
	 * Face Search: helps you find similar faces in your database based on your input face image.
	 * For more details
	 * http://v2.rekognition.com/index.php/developer/docs#facesearch
	 * @param sTags You can narrow down the search by providing tags in this format: face_search[tag1;tag2;tag3], then it will only search in the mentioned tags/clusters.
	 * @param sImageURL URL of the image you search
	 * @param name_space Namespace for your app. Example: facebookapp. You can use this field to differentiate your apps. default will be used if it is not set
	 * @param user_id User ID of your app. The uploaded file will be added for this user. default will be used if it is not set.
	 * @param callbackFunc callback object 
	 */
	public static void face_search(String[] sTags, String sImageURL, String name_space, String user_id, int num_return, final APICallback callbackFunc) {
		face_search(sTags, sImageURL, null, name_space, user_id, num_return, callbackFunc);
	}
	
	/**
	 * Face Search: helps you find similar faces in your database based on your input face image.
	 * For more details
	 * http://v2.rekognition.com/index.php/developer/docs#facesearch
	 * @param sTags You can narrow down the search by providing tags in this format: face_search[tag1;tag2;tag3], then it will only search in the mentioned tags/clusters.
	 * @param b byte array of the image you search
	 * @param name_space Namespace for your app. Example: facebookapp. You can use this field to differentiate your apps. default will be used if it is not set
	 * @param user_id User ID of your app. The uploaded file will be added for this user. default will be used if it is not set.
	 * @param callbackFunc callback object 
	 */
	public static void face_search(String[] sTags, byte[] b, String name_space, String user_id, int num_return, final APICallback callbackFunc) {
		face_search(sTags, null, b, name_space, user_id, num_return, callbackFunc);
	}
	
	/**
	 * Face Inner Search: helps you find similar faces in your database based on your input face image.
	 * For more details
	 * http://v2.rekognition.com/index.php/developer/docs#facesearch
	 * @param sTags You can narrow down the search by providing tags in this format: face_search[tag1;tag2;tag3], then it will only search in the mentioned tags/clusters.
	 * @param query_tag The tag of the query image in your database.
	 * @param img_index The image that you have enrolled using ::FaceAdd.
	 * @param callbackFunc callback object  
	 */
	public static void face_inner_search(String[] sTags, String query_tag, String img_index, final APICallback callbackFunc) {
		face_inner_search(sTags, query_tag, img_index, null, null, -1, callbackFunc);
	}
	
	/**
	 * Face Inner Search: helps you find similar faces in your database based on your input face image.
	 * For more details
	 * http://v2.rekognition.com/index.php/developer/docs#facesearch
	 * @param sTags You can narrow down the search by providing tags in this format: face_search[tag1;tag2;tag3], then it will only search in the mentioned tags/clusters.
	 * @param query_tag The tag of the query image in your database.
	 * @param img_index The image that you have enrolled using ::FaceAdd.
	 * @param name_space Namespace for your app. Example: facebookapp. You can use this field to differentiate your apps. default will be used if it is not set
	 * @param user_id User ID of your app. The uploaded file will be added for this user. default will be used if it is not set.
	 * @param callbackFunc callback object  
	 */
	public static void face_inner_search(String[] sTags, String query_tag, String img_index, String name_space, String user_id, int num_return, final APICallback callbackFunc) {
		List<NameValuePair> params = getBasicParameters();
		String sJobValue = "face_inner_search[" + TextUtils.join(";", sTags) + "]";
		params.add(new BasicNameValuePair("jobs", sJobValue));
		params.add(new BasicNameValuePair("query_tag", query_tag));
		params.add(new BasicNameValuePair("img_index", img_index));
		addNameSpaceAndUserID(params, name_space, user_id);
		if ( num_return >= 0) {
			params.add(new BasicNameValuePair("num_return", Integer.toString(num_return)));
		}
		callAPICallInAnotherThread(params, callbackFunc);
	}
	
	/**
	 * Face Delete: Deleting the wrong training images
	 * For more details
	 * http://v2.rekognition.com/index.php/developer/docs#face_delete
	 * @param sTag tag you are deleting image
	 * @param img_indexs indexs of the images you want to delete
	 * @param callbackFunc callback object
	 */
	public static void face_delete(String sTag, String[] img_indexs, final APICallback callbackFunc) {
		face_delete(sTag, img_indexs, null, null, callbackFunc);
	}
	
	/**
	 * Face Delete: Deleting the wrong training images
	 * For more details
	 * http://v2.rekognition.com/index.php/developer/docs#face_delete
	 * @param sTag tag you are deleting image
	 * @param img_indexs indexs of the images you want to delete
	 * @param name_space Namespace for your app. Example: facebookapp. You can use this field to differentiate your apps. default will be used if it is not set
	 * @param user_id User ID of your app. The uploaded file will be added for this user. default will be used if it is not set.
	 * @param callbackFunc callback object
	*/
	public static void face_delete(String sTag, String[] img_indexs, String name_space, String user_id, final APICallback callbackFunc) {
		List<NameValuePair> params = getBasicParameters();
		String sTagValue = "";
		if (sTag != null ) {
			sTagValue= "[" + sTag + "]";
		}
		String sImageValue = "";
		if (img_indexs != null ) {
			sImageValue = "{" + TextUtils.join(";", img_indexs) + "}";
		}
		String sJobValue = "face_delete" + sTagValue + sImageValue;
		params.add(new BasicNameValuePair("jobs", sJobValue));
		addNameSpaceAndUserID(params, name_space, user_id);
		callAPICallInAnotherThread(params, callbackFunc);
	}

	/**
	 * Face Rename: Changing tags, assigning an image to a tag, or merging two tags.
	 * For more details
	 * http://v2.rekognition.com/index.php/developer/docs#face_rename
	 * @param sTag The tag you want to change, or the tag of which image that you want to rename.
	 * @param new_tag the new or target tag, if the new_tag is already exist, then tag will be merged with new_tag
	 * @param name_space Namespace for your app. Example: facebookapp. You can use this field to differentiate your apps. default will be used if it is not set
	 * @param user_id User ID of your app. The uploaded file will be added for this user. default will be used if it is not set.
	 * @param callbackFunc callback object
	 */
	public static void face_rename(String sTag, String new_tag, String name_space, String user_id, final APICallback callbackFunc) {
		face_rename(sTag, new_tag, name_space, user_id, null, callbackFunc);
	}


	/**
	 * Face Rename: Changing tags, assigning an image to a tag, or merging two tags.
	 * For more details
	 * http://v2.rekognition.com/index.php/developer/docs#face_rename
	 * @param sTag The tag you want to change, or the tag of which image that you want to rename.
	 * @param new_tag the new or target tag, if the new_tag is already exist, then tag will be merged with new_tag
	 * @param name_space Namespace for your app. Example: facebookapp. You can use this field to differentiate your apps. default will be used if it is not set
	 * @param user_id User ID of your app. The uploaded file will be added for this user. default will be used if it is not set.
	 * @param img_index Image index. It is the same index you receive when you call FaceAdd
	 * @param callbackFunc callback object
	 */
	public static void face_rename(String sTag, String new_tag, String name_space, String user_id, String img_index, final APICallback callbackFunc) {
		List<NameValuePair> params = getBasicParameters();
		String sJobValue = "face_rename";
		params.add(new BasicNameValuePair("jobs", sJobValue));
		params.add(new BasicNameValuePair("tag", sTag));
		params.add(new BasicNameValuePair("new_tag", new_tag));
		addNameSpaceAndUserID(params, name_space, user_id);
		if (img_index != null) {
			params.add(new BasicNameValuePair("img_index", img_index));
		}
		callAPICallInAnotherThread(params, callbackFunc);
	}
	
	/**
	 * Face Stats: Showing you some insights of your data sets
	 * For more details 
	 * http://v2.rekognition.com/index.php/developer/docs#face_stats
	 * @param callbackFunc callback object
	 */
	public static void face_stats(final APICallback callbackFunc) {
		List<NameValuePair> params = getBasicParameters();
		String sJobValue = "face_name_space_stats";
		params.add(new BasicNameValuePair("jobs", sJobValue));
		callAPICallInAnotherThread(params, callbackFunc);
	} 
	
	/**
	 * Face Stats: Showing you some insights of your datasets
	 * For more details 
	 * http://v2.rekognition.com/index.php/developer/docs#face_stats
	 * @param name_space Namespace for your app. Example: facebookapp. You can use this field to differentiate your apps. default will be used if it is not set
	 * @param callbackFunc callback object
	 */
	public static void face_stats(String name_space, final APICallback callbackFunc) {
		List<NameValuePair> params = getBasicParameters();
		String sJobValue = "face_user_id_stats";
		params.add(new BasicNameValuePair("jobs", sJobValue));
		if (name_space != null) {
			params.add(new BasicNameValuePair("name_space", name_space));
		}
		callAPICallInAnotherThread(params, callbackFunc);
	}
	
	/**
	 * Scene Understanding: We offer scene understanding technology to better organize online images
	 * For more details
	 * http://v2.rekognition.com/index.php/developer/docs#scenecatagorize
 	 * @param sImageURL URL of the image you want to understand
	 * @param callbackFunc callback object
	 */
	public static void scene_understand(String sImageURL, final APICallback callbackFunc) {
		List<NameValuePair> params = getBasicParameters();
		String sJobValue = "scene";
		params.add(new BasicNameValuePair("jobs", sJobValue));
		addImageDataValuePair(params, sImageURL, null);
		callAPICallInAnotherThread(params, callbackFunc);
	}
	
	/**
	 * Scene Understanding: We offer scene understanding technology to better organize online images
	 * For more details
	 * http://v2.rekognition.com/index.php/developer/docs#scenecatagorize
 	 * @param b byte array of the image you search 
	 * @param callbackFunc callback object
	 */
	public static void scene_understand(byte[] b, final APICallback callbackFunc) {
		List<NameValuePair> params = getBasicParameters();
		String sJobValue = "scene";
		params.add(new BasicNameValuePair("jobs", sJobValue));
		addImageDataValuePair(params, null, b);
		callAPICallInAnotherThread(params, callbackFunc);
	}
	
	/*
	 * Private Methods
	 * */
	
	private static void face_add(String sName, String sImageURL, byte[] b, String name_space, String user_id, final APICallback callbackFunc) {
		String sJobsValue = "face_add_[" + sName + "]";
		List<NameValuePair> params = getBasicParameters();
		params.add(new BasicNameValuePair("jobs", sJobsValue));
		addImageDataValuePair(params, sImageURL, b);
		addNameSpaceAndUserID(params, name_space, user_id);
		callAPICallInAnotherThread(params, callbackFunc);
	}
	
	private static void face_search(String[] sTags, String sImageURL, byte[] b, String name_space, String user_id, int num_return, final APICallback callbackFunc) {
		List<NameValuePair> params = getBasicParameters();
		String sJobValue = "face_search[" + TextUtils.join(";", sTags) + "]";
		params.add(new BasicNameValuePair("jobs", sJobValue));
		addImageDataValuePair(params, sImageURL, b);
		addNameSpaceAndUserID(params, name_space, user_id);
		if (num_return >= 0) {
			params.add(new BasicNameValuePair("num_return", Integer.toString(num_return)));
		}
		callAPICallInAnotherThread(params, callbackFunc);
	}
	
	private static void addImageDataValuePair(List<NameValuePair> params, String sImageURL, byte[] b) {
		if (sImageURL != null) {
			params.add(new BasicNameValuePair("urls", sImageURL));
		}
		else {
			if ( b != null ) {
				final String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
				params.add(new BasicNameValuePair("base64", encodedImage));
			}
		}
	}
	
	private static void addNameSpaceAndUserID(List<NameValuePair> params, String name_space, String user_id) {
		if (name_space != null) {
			params.add(new BasicNameValuePair("name_space", name_space));
		}
		if (user_id != null) {
			params.add(new BasicNameValuePair("user_id", user_id));
		}
	}
	
	private static void callAPICallInAnotherThread(final List<NameValuePair> params, final APICallback callbackFunc) {
		Thread trd = new Thread(new Runnable() {
			  @Override
			  public void run(){
				  String sResponse = getAPIResponse(params);
				  callbackFunc.gotResponse(sResponse);
			  }
			});
		trd.start();
	}
	
	private static List<NameValuePair> getBasicParameters()
	{
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        // api_key
        nameValuePairs.add(new BasicNameValuePair("api_key", sAPI_KEY));
        // api_secret 
        nameValuePairs.add(new BasicNameValuePair("api_secret", sAPI_SECRET)); 
        return nameValuePairs;
	}
	
	private static String getAPIResponse(List<NameValuePair> params) {
		String sResponse = null;
        HttpClient httpclient = new DefaultHttpClient();
        // Please refer http://www.rekognition.com/docs/ for more documentation
        HttpPost httppost = new HttpPost("http://rekognition.com/func/api/index.php");
        try {
        	// parameters of HTTP request
            httppost.setEntity(new UrlEncodedFormEntity(params));
            // Make API call
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity responseEntity = response.getEntity();
            if(responseEntity!=null) {
            	//You can refer to the documentation pages on ReKogntion.com to understand the structure of response
            	sResponse = EntityUtils.toString(responseEntity);
                Log.v("json_result", sResponse);  
            }
        } catch (ClientProtocolException e) {
            Log.v("ClientProtocolException", e.getMessage());
        } catch (IOException e) {
            Log.v("IOException", e.getMessage());
        }
		return sResponse;
	}
}