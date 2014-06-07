import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VenueTimepiece extends HttpServlet
{
	private static String 	FOURSQUARE_CLIENT_ID="JXLUOZXBK14KGPEXTFGTX0FSW5IWVHC1D1QZMN0TN422RWVC",
							FOURSQUARE_CLIENT_SECRET="***",
							FOURSQUARE_REDIRECT_URL="https://yogash1290.appspot.com/venuetp/oauth/foursquare/",
							FACEBOOK_APP_ID="482991465179537",
							FACEBOOK_APP_SECRET="***",
							FACEBOOK_REDIRECT_URL="https://yogash1290.appspot.com/venuetp/oauth/facebook/";

//	https://foursquare.com/oauth2/authenticate?client_id=JXLUOZXBK14KGPEXTFGTX0FSW5IWVHC1D1QZMN0TN422RWVC&response_type=code&redirect_uri=https://yogash1290.appspot.com/venuetp/oauth/foursquare/
//	https://www.facebook.com/dialog/oauth?client_id=482991465179537&redirect_uri=https://yogash1290.appspot.com/venuetp/oauth/facebook/&scope=publish_actions,publish_stream&state=HelloWorld
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	{
		try
		{
			if(req.getRequestURI().equals("/venuetp/"))
				resp.sendRedirect("https://foursquare.com/oauth2/authenticate?client_id="+FOURSQUARE_CLIENT_ID+"&response_type=code&redirect_uri="+FOURSQUARE_REDIRECT_URL);
			
			else if(req.getRequestURL().toString().equals(FOURSQUARE_REDIRECT_URL))
			{
				String access_token=Foursquare.getAccessToken(FOURSQUARE_CLIENT_ID, FOURSQUARE_CLIENT_SECRET, FOURSQUARE_REDIRECT_URL, req.getParameter("code"));
				resp.getWriter().println(				
						readFromURL("http://yogash1290.appspot.com/venuetp/app.html")
						.replace("foursquare_access_token_here", access_token)
						);
			}
			else if(req.getRequestURL().toString().equals(FACEBOOK_REDIRECT_URL))
			{
				String state=req.getParameter("state");
				if(state.equals("share"))//share
				{
					String facebook_access_token=facebook.getAccessToken(FACEBOOK_APP_ID, FACEBOOK_APP_SECRET, FACEBOOK_REDIRECT_URL, req.getParameter("code"));
					resp.getWriter().println(
							readFromURL("http://yogash1290.appspot.com/venuetp/share.html")
							.replace("facebook_access_token_here", facebook_access_token)
							);
				}
				else
					resp.sendRedirect("https://foursquare.com/oauth2/authenticate?client_id="+FOURSQUARE_CLIENT_ID+"&response_type=code&redirect_uri="+FOURSQUARE_REDIRECT_URL);
			}
		}catch(Exception e){e.printStackTrace();
			try{
				resp.getWriter().println("<!--"+e.getLocalizedMessage()+"-->");
			}catch(Exception ee){}
		}
	}
	
	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
	
	
	public static String readFromURL(String rURL) {
		String res = "";
		try {
			URL url = new URL(rURL);

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			// connection.setRequestProperty("Accept-Charset", "UTF-8");
			InputStream in = connection.getInputStream();
			byte buff[] = new byte[200];
			int ch;
			while ((ch = in.read(buff)) != -1)
				res += new String(buff, 0, ch, "UTF-8");
		} catch (Exception e) {
			return e.getMessage();
		}
		return res;
	}

}
