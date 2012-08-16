package net.networksaremadeofstring.android.readingtravel;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;

public class CurrentLocation extends SherlockActivity 
{

	Drawable image;
	Handler handler;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);
        
        handler = new Handler() 
		{
			public void handleMessage(Message msg) 
			{
				((ImageView)findViewById(R.id.streetviewImage)).setImageDrawable(image);
				//((ImageView)findViewById(R.id.streetviewImage)).setAlpha(128);
			}
		};
		
        ((Thread) new Thread(){
			public void run()
			{
				Display display = getWindowManager().getDefaultDisplay();
				
				DisplayMetrics metrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(metrics);
				Log.i("Metrics", Float.toString(metrics.density));
				int width = (int) (display.getWidth() * metrics.density);
				Log.i("Thread", "Running thread");
				image = ImageOperations("http://cbk2.google.com/cbk?output=thumbnail&thumbfov=120&w="+ width +"&h=300&thumb=1&ll=51.460482,-0.975965","image.jpg");
				Log.i("Thread", "Finished thread");
				handler.sendEmptyMessage(1);
			}
        }).start();
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.activity_current_location, menu);
        return true;
    }

    public Drawable ImageOperations(String url, String saveFilename) 
	{
    	Log.i("ImageOperations", "Started ImageOperations");
		try 
		{
			InputStream is = (InputStream) this.fetch(url);
			Drawable d = Drawable.createFromStream(is, "src");
			Log.i("ImageOperations", "finished ImageOperations");
			return d;
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
			return null;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
    
    private Object fetch(String address) throws MalformedURLException,IOException 
    {
		URL url = new URL(address);
		Object content = url.getContent();
		return content;
	}
    
}
