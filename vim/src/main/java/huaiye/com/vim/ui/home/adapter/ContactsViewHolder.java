package huaiye.com.vim.ui.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import huaiye.com.vim.R;
import huaiye.com.vim.common.recycle.LiteViewHolder;
import huaiye.com.vim.common.views.CheckableLinearLayout;
import huaiye.com.vim.dao.AppDatas;
import huaiye.com.vim.dao.msgs.User;

public class ContactsViewHolder extends LiteViewHolder {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_checklayout)
    CheckableLinearLayout tv_checklayout;
    @BindView(R.id.iv_choice)
    ImageView iv_choice;
    @BindView(R.id.iv_user_head)
    ImageView iv_user_head;
    @BindView(R.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R.id.tv_user_master)
    TextView tv_user_master;
    @BindView(R.id.tv_choose_added)
    TextView tv_choose_added;

    private RequestOptions requestOptions;

    private int nJoinStatus;

    public static boolean mIsChoice;
    public static OnLoadMoreListener mOnLoadMoreListener;

    public ContactsViewHolder(Context context, View view, View.OnClickListener ocl) {
        super(context, view, ocl);
        requestOptions = new RequestOptions();
        requestOptions.centerCrop()
                .dontAnimate()
                .format(DecodeFormat.PREFER_RGB_565)
                .placeholder(R.drawable.default_image_personal)
                .error(R.drawable.default_image_personal)
                .optionalTransform(new CircleCrop());
        itemView.setOnClickListener(ocl);
    }

    @Override
    public void bindData(Object holder, int position, Object data, int size, List datas, Object extr) {
        User user = (User) data;
        itemView.setTag(user);

        tv_title.setText(user.pinyin);
        if (position > 0) {
            User last = (User) datas.get(position - 1);
            if (last.pinyin.equals(user.pinyin)) {
                tv_title.setVisibility(View.GONE);
            } else {
                tv_title.setVisibility(View.VISIBLE);
            }
        } else {
            tv_title.setVisibility(View.VISIBLE);
        }
        Glide.with(context)
                .load(AppDatas.Constants().getAddressWithoutPort() + user.strHeadUrl)
                .apply(requestOptions)
                .into(iv_user_head);
        tv_user_name.setText(user.strUserName);

        if (user.isSelected) {
            nJoinStatus = user.nJoinStatus;
        }
        if (nJoinStatus == 2) {
            tv_choose_added.setText("已添加");
            tv_choose_added.setVisibility(View.GONE);
            iv_choice.setVisibility(View.VISIBLE);
            iv_choice.setImageResource(R.drawable.shijian_xuanze_unclick);
            user.nJoinStatus = 2;
            nJoinStatus = 0;
        } else {
            if (mIsChoice) {
                iv_choice.setVisibility(View.VISIBLE);
                if (user.isSelected) {
                    iv_choice.setImageResource(R.drawable.ic_choice_checked);
                } else {
                    iv_choice.setImageResource(R.drawable.ic_choice);
                }
            }
        }

        itemView.setOnClickListener(ocl);

        if (position >= datas.size() - 1 && mOnLoadMoreListener != null) {
            try {
                mOnLoadMoreListener.onLoadMore();
            } catch (Exception e) {

            }
        }

    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

}
