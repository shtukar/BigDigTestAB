package ru.yandex.shtukarr.bigdigitalstesta.view;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.yandex.shtukarr.bigdigitalstesta.DbHelper;
import ru.yandex.shtukarr.bigdigitalstesta.LinkModel;
import ru.yandex.shtukarr.bigdigitalstesta.R;
import ru.yandex.shtukarr.bigdigitalstesta.RefreshDbBroadcastReceiver;

public class HistoryFragment extends Fragment implements HistoryScreen, SwipeRefreshLayout.OnRefreshListener {
    private static final String BROADCAST_RECEIVER = "ru.yandex.shtukarr.action.REFRESH_DB";
    private RefreshDbBroadcastReceiver receiver;

    private static final String INTENT_URL = "String";
    private static final String INTENT_STATUS = "Status";
    private static final String INTENT_ID = "Id";

    private DbHelper dbHelper;
    private RecyclerView mRecyclerView;
    private LinkListAdapter linkListAdapter;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<LinkModel> linkModelList = new ArrayList<>();
    private TextView textViewEmpty;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_historyt, container, false);
        textViewEmpty = rootView.findViewById(R.id.empty);
        receiver = new RefreshDbBroadcastReceiver(this);
        getContext().registerReceiver(receiver, new IntentFilter(BROADCAST_RECEIVER));

        dbHelper = new DbHelper(getContext());
        linkModelList = dbHelper.getAllLinks();

        if (linkModelList.isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
        }

        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        linkListAdapter = new LinkListAdapter(this);
        mRecyclerView.setAdapter(linkListAdapter);
        linkListAdapter.setList(linkModelList);

        swipeRefreshLayout = rootView.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("TAG", "onDestroyView");
        getContext().unregisterReceiver(receiver);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.sortByDate:
                sortByDateClick();
                break;
            case R.id.sortByStatus:
                sortByStatusClick();
                break;
        }
        return false;
    }

    @Override
    public void onItemSelected(LinkModel linkModel) {
        try {
            Intent intent = new Intent();
            intent.putExtra(INTENT_STATUS, linkModel.getStatus());
            intent.putExtra(INTENT_ID, linkModel.getId());
            intent.putExtra(INTENT_URL, linkModel.getUrl());
            intent.setClassName("ru.yandex.shtukarr.bigdigitalstestb", "ru.yandex.shtukarr.bigdigitalstestb.ImageActivity");
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), getResources().getString(R.string.installSecondApp), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void refresh() {
        onRefresh();
    }

    public void sortByDateClick() {
        if (!linkModelList.isEmpty()) {
            Log.d("TAG", "sortByDateClick");
            Collections.sort(linkModelList, LinkModel.COMPARE_BY_DATE);
            linkListAdapter.setList(linkModelList);
        }
    }

    public void sortByStatusClick() {
        if (!linkModelList.isEmpty()) {
            Log.d("TAG", "sortByStatusClick");
            Collections.sort(linkModelList, LinkModel.COMPARE_BY_STATUS);
            linkListAdapter.setList(linkModelList);
        }
    }

    @Override
    public void onRefresh() {
        Log.d("TAG", "onRefresh");
        linkModelList = dbHelper.getAllLinks();
        if (linkModelList.isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
        } else {
            textViewEmpty.setVisibility(View.GONE);
        }
        linkListAdapter.setList(linkModelList);
        swipeRefreshLayout.setRefreshing(false);
    }
}
