package com.example.denis.orderandtracksimply;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.Fragment;

public class AboutUsFragment extends android.support.v4.app.Fragment
{
    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    int pageNumber;

    static AboutUsFragment newInstance(int page)
    {
        AboutUsFragment pageFragment = new AboutUsFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_a_u, null);

        TextView tvPage = (TextView) view.findViewById(R.id.tvPage);
        tvPage.setText("Page " + pageNumber);

        return view;
    }
}
