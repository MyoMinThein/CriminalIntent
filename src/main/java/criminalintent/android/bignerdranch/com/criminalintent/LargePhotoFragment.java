package criminalintent.android.bignerdranch.com.criminalintent;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;


public class LargePhotoFragment extends Fragment {
    public static File mPhotoFile;

    private ImageView mPhotoView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public static LargePhotoFragment newInstance(File crimeId) {
        crimeId = mPhotoFile;
        return new LargePhotoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_large_photo, container, false);
        mPhotoView = (ImageView) v.findViewById(R.id.crime_photo_large);
        updatePhotoView();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_large_photo, container, false);

    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            mPhotoView.setImageURI( Uri.fromFile(mPhotoFile));
        }
    }


}





