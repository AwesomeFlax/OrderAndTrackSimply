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
import java.util.ArrayList;

public class fragmentOrders extends Fragment
{
    //everything I need to change/use
    Button button, but_R, but_L;
    TextView order, client, status, count;
    int c_page = 1;

    private static String TAG = fragmentOrders.class.getSimpleName();
    ArrayList<String> data;

    // Progress dialog
    private ProgressDialog pDialog;

    // temporary string to show the parsed response
    private String jsonResponse;

    public fragmentOrders()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_orders, null);

        //catching button/views
        button = (Button) v.findViewById(R.id.button);
        but_R = (Button) v.findViewById(R.id.but_R);
        but_L = (Button) v.findViewById(R.id.but_L);

        order = (TextView) v.findViewById(R.id.order);
        client = (TextView) v.findViewById(R.id.client);
        status = (TextView) v.findViewById(R.id.status);

        count = (TextView) v.findViewById(R.id.count);

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

        //setting pages scroll
        but_L.setOnClickListener(Scrolling);
        but_R.setOnClickListener(Scrolling);

        return v;
    }

    //Method to make json array request where response starts with [
    private void makeJsonArrayRequest()
    {
        showpDialog();
        data = new ArrayList<>();
        String urlJsonArry = "http://10.0.3.2/HttpServ/hs/order-services/list";
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
                                //parsing data we receive
                                JSONObject person = (JSONObject) response.get(i);

                                /*String order = new String(person.getString("order").getBytes("ISO-8859-1"), "UTF-8");
                                String client = new String(person.getString("client").getBytes("ISO-8859-1"), "UTF-8");
                                String status = new String(person.getString("status").getBytes("ISO-8859-1"), "UTF-8");*/

                                String order = person.getString("order");
                                String client = person.getString("client");
                                String status = person.getString("status");

                                jsonResponse = "";

                                //packing into array list
                                jsonResponse += order + "%";
                                jsonResponse += client + "%";
                                jsonResponse += status;

                                data.add(jsonResponse);
                            }

                            String one_note[] = data.get(0).split("%");  // splitting data
                            order.setText(one_note[0]);
                            client.setText("Клиент: " + one_note[1]);
                            status.setText("Статус: " + one_note[2]);

                            count.setText("1/" + data.size());
                            c_page = 1;

                            but_L.setEnabled(true);
                            but_R.setEnabled(true);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                        /*catch (UnsupportedEncodingException e)
                        {
                            e.printStackTrace();
                        }*/

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

    void setActualData(int n)
    {
        String one_note[] = data.get(n - 1).split("%");  // splitting data
        order.setText(one_note[0]);
        client.setText("Клиент: " + one_note[1]);
        status.setText("Статус: " + one_note[2]);

        count.setText(n + "/" + data.size());
        c_page = n;
    }

    View.OnClickListener Scrolling = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.but_L:
                    if (c_page > 1)
                    {
                        setActualData(c_page - 1);
                    }
                    break;

                case R.id.but_R:
                    if (c_page < data.size())
                    {
                        setActualData(c_page + 1);
                    }
                    break;
            }
        }
    };

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
