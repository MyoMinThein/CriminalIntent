package criminalintent.android.bignerdranch.com.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by E5-471G on 12/28/2016.
 */

public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private static int mCurrentPosition = -1;
    private static final int REQUEST_CRIME = 1;
    private boolean mSubtitleVisible;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view
                .findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        updateUI();
        registerForContextMenu(mCrimeRecyclerView);
        return view;
    }



    @Override
    public boolean onContextItemSelected(MenuItem item){
        //Toast.makeText(getContext(), mCurrentPosition +" is deleted!", Toast.LENGTH_LONG).show();
        if(item.getTitle()=="Delete" && mCurrentPosition>-1) {
                mAdapter.deleteCrime(mCurrentPosition);
                Toast.makeText(getContext(), mCurrentPosition + " is deleted!", Toast.LENGTH_LONG).show();

        }
        else if(item.getTitle()=="menu 11") {
            Toast.makeText(getContext(), "Menu 11", Toast.LENGTH_LONG).show();
        }
        else if(item.getTitle()=="menu 12") {
            Toast.makeText(getContext(), "Menu 12", Toast.LENGTH_LONG).show();
        }
        else {Toast.makeText(getContext(), "Menu Unknown", Toast.LENGTH_LONG).show();}

        //mAdapter.notifyItemRangeRemoved(mCurrentPosition, CrimeLab.get(getActivity()).getCrimes().size());
        mAdapter.notifyItemRangeRemoved(mCurrentPosition, mAdapter.getItemCount());
        updateUI();
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity
                        .newIntent(getActivity(), crime.getId());
                startActivity(intent);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        if(crimeCount<1){
            Toast.makeText(getContext(), "There are no crimes", Toast.LENGTH_LONG).show();
        }
        String subtitle = getResources()
                .getQuantityString(R.plurals.subtitle_plural, crimeCount, crimeCount);
        if (!mSubtitleVisible) {
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }


    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();


        if (mAdapter == null) {
            Toast.makeText(getContext(),"mAdapter == null ", Toast.LENGTH_SHORT).show();
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setCrimes(crimes);
            //Toast.makeText(getContext(),"mCurrentPosition => " + mCurrentPosition, Toast.LENGTH_SHORT).show();
            if (mCurrentPosition == -1) {
                Toast.makeText(getContext(),"mCurrentPosition == -1 ", Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(),"else else " + mCurrentPosition, Toast.LENGTH_SHORT).show();
                mAdapter.notifyItemChanged(mCurrentPosition);
            }

        }
        updateSubtitle();
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;
        private Crime mCrime;

        public CrimeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView)
                    itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView)
                    itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox)
                    itemView.findViewById(R.id.list_item_crime_solved_check_box);
            mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Set the crime's solved property
                    mCrime.setSolved(isChecked);
                }
            });
            itemView.setOnCreateContextMenuListener(this);
        }



        public void bindCrime(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        @Override
        public void onClick(View v) {
            //Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getId());
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            //startActivity(intent);
            startActivityForResult(intent, REQUEST_CRIME);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderIcon(R.drawable.content_option);
            menu.setHeaderTitle("Context Menu");
            menu.add(0,v.getId(), 0, "Delete");
            menu.add(0,v.getId(), 0, "menu 11");
            menu.add(0,v.getId(), 0, "menu 12");
            try {
                mCurrentPosition = getAdapterPosition();
                //Toast.makeText(getContext(), "getAdapterPosition() " + getAdapterPosition(), Toast.LENGTH_LONG).show();
                //Toast.makeText(getContext(), "getLayoutPosition() " + getLayoutPosition(), Toast.LENGTH_LONG).show();
            }catch (Exception e){
                Toast.makeText(getContext(), "Exception " + e.toString(), Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CRIME) {
            Toast.makeText(getContext(), "REQUEST_CRIME ", Toast.LENGTH_LONG).show();
            updateUI();
        } else
        {
            Toast.makeText(getContext(), "ELSE ", Toast.LENGTH_LONG).show();
            updateUI();
        }


    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }

        public void deleteCrime(int position) {
            Crime crime = mCrimes.get(position);
            String uuid = crime.getId().toString();
            CrimeLab.get(getActivity()).removeCrime(uuid);

        }
    }
}
