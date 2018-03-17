package challange.vanhack.com.vanhack.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import challange.vanhack.com.vanhack.OrdersActivity;
import challange.vanhack.com.vanhack.R;
import challange.vanhack.com.vanhack.service.CartFactory;
import challange.vanhack.com.vanhack.service.ServiceFactory;
import challange.vanhack.com.vanhack.service.VanhackService;
import challange.vanhack.com.vanhack.service.model.CartItem;
import challange.vanhack.com.vanhack.service.model.Product;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ProductsFragment extends Fragment {

    private RecyclerView recyclerView;

    public ProductsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ProductsFragment newInstance() {
        ProductsFragment fragment = new ProductsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products_list, container, false);

        recyclerView = (RecyclerView) view;
        return view;
    }

    public void request(String sid) {
        VanhackService service = ServiceFactory.createRetrofitService(VanhackService.class, VanhackService.SERVICE_ENDPOINT);
        service.getProductsBySid(sid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Product>>() {
                    @Override
                    public final void onCompleted() {
                    }

                    @Override
                    public final void onError(Throwable e) {
                    }

                    @Override
                    public final void onNext(List<Product> response) {
                        List<Product> list = new ArrayList<>();
                        for (Product p : response) {
                            Product product = new Product();
                            product.setId(p.getId());
                            product.setName(p.getName());
                            product.setDescription(p.getDescription());
                            product.setPrice(p.getPrice());
                            product.setStoreId(p.getStoreId());
                            list.add(product);
                        }
                        recyclerView.setAdapter(new MyItemRecyclerViewAdapter(list));
                    }
                });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

        private List<Product> mValues;

        public MyItemRecyclerViewAdapter(List<Product> items) {
            mValues = items;
        }

        public void updateList(List<Product> items) {
            mValues.clear();
            mValues = items;
            notifyDataSetChanged();
        }

        @Override
        public MyItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_products, parent, false);
            return new MyItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mContentView.setText(mValues.get(position).getName());

            if (mValues.get(position).getDescription() != null &&
                    mValues.get(position).getDescription().length() > 0) {
                holder.mDetailView.setText(mValues.get(position).getDescription());
            } else {
                holder.mDetailView.setVisibility(View.GONE);
            }

            holder.mPriceView.setText("$" + mValues.get(position).getPrice());
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mContentView;
            public final TextView mDetailView;
            public final TextView mPriceView;
            public final Button button;

            public Product mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mDetailView = (TextView) view.findViewById(R.id.detail);
                mContentView = (TextView) view.findViewById(R.id.content);
                mPriceView = (TextView) view.findViewById(R.id.price);
                button = view.findViewById(R.id.addBtn);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showQuantityPicker(getActivity(), v, mItem);
                    }
                });
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

    private void showQuantityPicker(final Context mContext, final View view, final Product p) {
        RelativeLayout linearLayout = new RelativeLayout(mContext);
        final NumberPicker aNumberPicker = new NumberPicker(mContext);
        aNumberPicker.setMaxValue(50);
        aNumberPicker.setMinValue(1);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
        RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        numPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        linearLayout.setLayoutParams(params);
        linearLayout.addView(aNumberPicker, numPicerParams);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("Quantity");
        alertDialogBuilder.setView(linearLayout);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                CartFactory.addItem(mContext, new CartItem(p,
                                        aNumberPicker.getValue()));

                                Snackbar.make(view,
                                        p.getName() + "("+aNumberPicker.getValue()+")"
                                        +" added to the cart.", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}


