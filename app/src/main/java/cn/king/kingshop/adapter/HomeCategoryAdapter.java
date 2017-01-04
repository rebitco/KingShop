package cn.king.kingshop.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.king.kingshop.R;
import cn.king.kingshop.bean.Campaign;
import cn.king.kingshop.bean.HomeCampaign;

/**
 * Created by king on 2017/1/3.
 */

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {

    private static final int VIEW_TYPE_RIGHT = 0;
    private static final int VIEW_TYPE_LEFT = 1;
    private ViewHolder mHolder;

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<HomeCampaign> mDatas;
    private OnCampaignClickListener mListener;

    public HomeCategoryAdapter(List<HomeCampaign> datas, Context context) {
        mDatas = datas;
        this.mContext = context;
    }

    public void setOnCampaignClickListener(OnCampaignClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mLayoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_RIGHT) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.template_home_cardview2, parent, false));
        } else {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.template_home_cardview, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        this.mHolder = holder;

        HomeCampaign homeCampaign = mDatas.get(position);
        //标题
        holder.textTitle.setText(homeCampaign.getTitle());
        //广告图 cardView
        Picasso.with(mContext).load(homeCampaign.getCpOne().getImgUrl()).into(holder.imageViewBig);
        Picasso.with(mContext).load(homeCampaign.getCpTwo().getImgUrl()).into(holder.imageViewSmallTop);
        Picasso.with(mContext).load(homeCampaign.getCpThree().getImgUrl()).into(holder.imageViewSmallBottom);
    }

    @Override
    public int getItemCount() {
        if (mDatas == null || mDatas.size() <= 0) {
            return 0;
        }

        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return VIEW_TYPE_RIGHT;
        } else {
            return VIEW_TYPE_LEFT;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textTitle;
        ImageView imageViewBig;
        ImageView imageViewSmallTop;
        ImageView imageViewSmallBottom;

        public ViewHolder(View itemView) {
            super(itemView);

            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            imageViewBig = (ImageView) itemView.findViewById(R.id.imgview_big);
            imageViewSmallTop = (ImageView) itemView.findViewById(R.id.imgview_small_top);
            imageViewSmallBottom = (ImageView) itemView.findViewById(R.id.imgview_small_bottom);

            imageViewBig.setOnClickListener(this);
            imageViewSmallTop.setOnClickListener(this);
            imageViewSmallBottom.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                anim(v);
            }
        }
    }

    /**
     * cardView点击的动画(涟漪效果)
     * @param v
     */
    private void anim(final View v) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "rotationY", 0.0F, 360.0F)
                                    .setDuration(1000);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                HomeCampaign homeCampaign = mDatas.get(mHolder.getLayoutPosition());
                switch (v.getId()) {
                   case R.id.imgview_big:
                        mListener.onClick(v, homeCampaign.getCpOne());
                       break;
                   case R.id.imgview_small_top:
                        mListener.onClick(v, homeCampaign.getCpTwo());
                       break;
                   case R.id.imgview_small_bottom:
                        mListener.onClick(v, homeCampaign.getCpThree());
                       break;
                }
            }
        });
        animator.start();
    }

    public interface OnCampaignClickListener {
       void onClick(View v, Campaign campaign);
   }
}
