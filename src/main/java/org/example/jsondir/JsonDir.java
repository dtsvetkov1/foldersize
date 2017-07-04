package org.example.jsondir;

/**
 * Created by striker on 7/4/17.
 */
import java.io.IOException;

import org.springframework.extensions.webscripts.*;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonDir extends AbstractWebScript
{
    String str = "abc";



    public void execute(WebScriptRequest req, WebScriptResponse res)
            throws IOException
    {
        try
        {
            // build a json object
            JSONObject obj = new JSONObject();

            // put some data on it
            obj.put("str", str);

            // build a JSON string and send it back
            String jsonString = obj.toString();
            res.getWriter().write(jsonString);
        }
        catch(JSONException e)
        {
            throw new WebScriptException("Unable to serialize JSON");
        }
    }
}