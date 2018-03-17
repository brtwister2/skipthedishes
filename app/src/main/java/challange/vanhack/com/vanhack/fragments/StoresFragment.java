package challange.vanhack.com.vanhack.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import challange.vanhack.com.vanhack.ProductsActivity;
import challange.vanhack.com.vanhack.R;
import challange.vanhack.com.vanhack.dummy.DummyContent;
import challange.vanhack.com.vanhack.service.VanhackService;
import challange.vanhack.com.vanhack.service.ServiceFactory;
import challange.vanhack.com.vanhack.service.model.Store;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StoresFragment extends Fragment {

    private RecyclerView recyclerView;

    public StoresFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static StoresFragment newInstance() {
        StoresFragment fragment = new StoresFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stores_list, container, false);

        // Set the adapter
        recyclerView = (RecyclerView) view;
        request("1");

        return view;
    }

    public void request(String cid){
        VanhackService service = ServiceFactory.createRetrofitService(VanhackService.class, VanhackService.SERVICE_ENDPOINT);
        service.getStoresByCid(cid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Store>>() {
                    @Override
                    public final void onCompleted() {}

                    @Override
                    public final void onError(Throwable e) {}

                    @Override
                    public final void onNext(List<Store> response) {
                        List<DummyContent.DummyItem> dummyItemList = new ArrayList<>();
                        for(Store store: response){
                            dummyItemList.add(new DummyContent.DummyItem(store.getId() + "",
                                    store.getName(), store.getAddress()));
                        }
                        recyclerView.setAdapter(new MyItemRecyclerViewAdapter(dummyItemList));
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

        private List<DummyContent.DummyItem> mValues;

        public MyItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        public void updateList(List<DummyContent.DummyItem> items){
            mValues.clear();
            mValues = items;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_stores, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mContentView.setText(mValues.get(position).content);
            holder.mDetailView.setText(mValues.get(position).details);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mContentView;
            public final TextView mDetailView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mDetailView = (TextView) view.findViewById(R.id.detail);
                mContentView = (TextView) view.findViewById(R.id.content);
                mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ProductsActivity.class);
                        intent.putExtra("sid", mItem.id);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }



}


