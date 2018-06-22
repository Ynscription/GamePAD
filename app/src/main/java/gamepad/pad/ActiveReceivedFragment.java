package gamepad.pad;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;


public class ActiveReceivedFragment extends Fragment {

    private RecyclerView _recyclerView;
    private RecyclerView.LayoutManager _layoutManager;
    private ReceivedListAdapter _adapter;
    private long _userID;

    private ActiveReceivedFragment.OnFragmentInteractionListener mListener;

    public ActiveReceivedFragment() {
        // Required empty public constructor
    }


    public static ActiveReceivedFragment newInstance(long userId) {
        ActiveReceivedFragment fragment = new ActiveReceivedFragment();
        Bundle args = new Bundle();
        args.putLong("user", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _userID = getArguments().getLong("user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_active_received, container, false);

        _recyclerView = (RecyclerView) v.findViewById(R.id.active_received_list);
        _recyclerView.setHasFixedSize(true);

        _layoutManager = new LinearLayoutManager(v.getContext());
        _recyclerView .setLayoutManager(_layoutManager);

        _adapter = new ReceivedListAdapter(v.getContext(), _userID);
        _recyclerView.setAdapter(_adapter);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}


class ReceivedListAdapter extends  RecyclerView.Adapter<ReceivedListAdapter.ReceivedViewHolder> {

    private Context _context;
    private SimpleDateFormat _dateFormat;
    private GameListing [] _itemList;
    private long _userID;

    public static  class ReceivedViewHolder extends RecyclerView.ViewHolder {
        public TextView _head;
        public TextView _body;

        public ReceivedViewHolder (View v) {
            super(v);
            _head = (TextView)v.findViewById(R.id.list_item_Head);
            _body = (TextView)v.findViewById(R.id.list_item_Body);
        }

    }

    public ReceivedListAdapter(Context context, long userID) {
        _userID = userID;
        _context = context;


    }

    public  void update () {

        _itemList = DBConnection.db(_context).getReceivedByUser(_userID);

    }

    @Override
    public ReceivedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new ReceivedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReceivedListAdapter.ReceivedViewHolder holder, int position) {

        holder._head.setText(_itemList[position].getName());
        /*long diff = _dateList[position].getTime() - System.currentTimeMillis();
        long daysLeft = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);*/
        holder._body.setText(_itemList[position].getPrice() + "€ por día");
    }

    @Override
    public int getItemCount() {
        if (_itemList == null)
            return 0;

        return _itemList.length;
    }
}