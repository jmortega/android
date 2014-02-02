package com.proyecto.spaincomputing;

import com.proyecto.spaincomputing.coverflow.CoverFlow;
import com.proyecto.spaincomputing.coverflow.ReflectingImageAdapter;
import com.proyecto.spaincomputing.coverflow.ResourceImageAdapter;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;

/****
 * The Class CoverFlowTestingActivity.
 */
public class CoverFlowActivity extends ActionMainActivity {

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
	public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_coverflow_view);

        final CoverFlow coverFlow1 = (CoverFlow) findViewById(R.id.coverflow);
        setupCoverFlow(coverFlow1, false);
        final CoverFlow reflectingCoverFlow = (CoverFlow) findViewById(R.id.coverflowReflect);
        setupCoverFlow(reflectingCoverFlow, true);
    }

    /**
     * Setup cover flow.
     * 
     * @param mCoverFlow
     *            the m cover flow
     * @param reflect
     *            the reflect
     */
    private void setupCoverFlow(final CoverFlow mCoverFlow, final boolean reflect) {
        BaseAdapter coverImageAdapter;
        if (reflect) {
            coverImageAdapter = new ReflectingImageAdapter(new ResourceImageAdapter(this));
        } else {
            coverImageAdapter = new ResourceImageAdapter(this);
        }
        mCoverFlow.setAdapter(coverImageAdapter);
        mCoverFlow.setSelection(2, true);
        setupListeners(mCoverFlow);
    }

    /**
     * Sets the up listeners.
     * 
     * @param mCoverFlow
     *            the new up listeners
     */
    private void setupListeners(final CoverFlow mCoverFlow) {
        mCoverFlow.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView< ? > parent, final View view, final int position, final long id) {

            }

        });
        mCoverFlow.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView< ? > parent, final View view, final int position, final long id) {

            }

            @Override
            public void onNothingSelected(final AdapterView< ? > parent) {
             
            }
        });
    }

}
