package com.proyecto.spaincomputing;

import java.util.ArrayList;

import com.proyecto.spaincomputing.coverflow.FancyCoverFlow;
import com.proyecto.spaincomputing.coverflow.FancyCoverFlowAdapter;
import com.proyecto.spaincomputing.entity.UniversidadBean;
import com.proyecto.spaincomputing.singleton.MySingleton;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;



public class CoverFlowReflectionActivity extends ActionMainActivity {

	private ArrayList<UniversidadBean> listado=new ArrayList<UniversidadBean>();
	
    // =============================================================================
    // Supertype overrides
    // =============================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.layout_cover_flow_reflection);

        FancyCoverFlow fancyCoverFlow = (FancyCoverFlow) findViewById(R.id.fancyCoverFlow);
        fancyCoverFlow.setReflectionEnabled(true);
        fancyCoverFlow.setReflectionRatio(0.3f);
        fancyCoverFlow.setReflectionGap(0);

        
        listado=inicializarDatos();
        
        fancyCoverFlow.setAdapter(new ViewGroupExampleAdapter(listado));
    }
    
    public  ArrayList<UniversidadBean> inicializarDatos(){
		  
		  listado=MySingleton.getInstance().getUniversitiesList();
		  
		  return listado;
	}

    // =============================================================================
    // Private classes
    // =============================================================================

    private static class ViewGroupExampleAdapter extends FancyCoverFlowAdapter {

        // =============================================================================
        // Private members
        // =============================================================================

        private int[] images;

        // =============================================================================
        // Supertype overrides
        // =============================================================================

        public ViewGroupExampleAdapter(ArrayList<UniversidadBean> listado) {
			int i=0;
			
			images=new int[listado.size()];
			
        	for(UniversidadBean ub:listado){
        	 
           	 images[i]=ub.getIdImagen();
           	 i++;
        	}
		}

		@Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Integer getItem(int i) {
            return images[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getCoverFlowItem(int i, View reuseableView, ViewGroup viewGroup) {
            CustomViewGroup customViewGroup = null;

            if (reuseableView != null) {
                customViewGroup = (CustomViewGroup) reuseableView;
            } else {
                customViewGroup = new CustomViewGroup(viewGroup.getContext());
                customViewGroup.setLayoutParams(new FancyCoverFlow.LayoutParams(600, 500));
            }

            customViewGroup.getImageView().setImageResource(this.getItem(i));

            return customViewGroup;
        }
    }

    private static class CustomViewGroup extends LinearLayout {

        // =============================================================================
        // Child views
        // =============================================================================

        private ImageView imageView;

        // =============================================================================
        // Constructor
        // =============================================================================

        private CustomViewGroup(Context context) {
            super(context);

            this.setOrientation(HORIZONTAL);
            this.setWeightSum(10);

            this.imageView = new ImageView(context);
            
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            this.imageView.setLayoutParams(layoutParams);
         
            this.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            this.imageView.setAdjustViewBounds(true);


            this.addView(this.imageView);

        }

        // =============================================================================
        // Getters
        // =============================================================================

        private ImageView getImageView() {
            return imageView;
        }
    }
}
