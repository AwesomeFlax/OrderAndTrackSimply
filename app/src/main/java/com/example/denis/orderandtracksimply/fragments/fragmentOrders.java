package com.example.denis.orderandtracksimply.fragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.denis.orderandtracksimply.AppController;
import com.example.denis.orderandtracksimply.R;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import java.io.UnsupportedEncodingException;

public class fragmentOrders extends Fragment
{
    Button button;
    TextView title;

    private static String TAG = fragmentOrders.class.getSimpleName();

    // Progress dialog
    private ProgressDialog pDialog;

    // temporary string to show the parsed response
    private String jsonResponse;

    public fragmentOrders()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_orders, null);

        button = (Button) v.findViewById(R.id.button);
        title = (TextView) v.findViewById(R.id.title);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        //Toast.makeText(getActivity(), "This is my Toast message!", Toast.LENGTH_LONG).show();

        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                makeJsonArrayRequest();
            }
        });

        return v;
    }

    /**
     * Method to make json array request where response starts with [
     */
    private void makeJsonArrayRequest()
    {
        showpDialog();

        String urlJsonArry = "https://api.myjson.com/bins/l9fsx";
        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        Log.d(TAG, response.toString());

                        try
                        {
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";
                            for (int i = 0; i < response.length(); i++)
                            {

                                JSONObject person = (JSONObject) response.get(i);

                                String order = new String(person.getString("order").getBytes("ISO-8859-1"), "UTF-8");
                                String client = new String(person.getString("client").getBytes("ISO-8859-1"), "UTF-8");
                                String status = new String(person.getString("status").getBytes("ISO-8859-1"), "UTF-8");

                                jsonResponse += "Order: " + order + "\n\n";
                                jsonResponse += "Client: " + client + "\n\n";
                                jsonResponse += "Status: " + status + "\n\n\n";
                            }

                            title.setText(jsonResponse);

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                        catch (UnsupportedEncodingException e)
                        {
                            e.printStackTrace();
                        }

                        hidepDialog();
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    private void showpDialog()
    {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog()
    {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
