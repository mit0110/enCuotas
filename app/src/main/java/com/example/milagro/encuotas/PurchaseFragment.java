package com.example.milagro.encuotas;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.junit.runner.Describable;

import java.io.Serializable;
import java.text.DecimalFormat;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PurchaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PurchaseFragment extends Fragment {
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String LOG_KEY = Fragment.class.getSimpleName();
  private static final String PURCHASE = "purchase";
  private static Purchase mPurchase;

  public PurchaseFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param purchase Parameter 1.
   * @return A new instance of fragment PurchaseFragment.
   */
  public static PurchaseFragment newInstance(Purchase purchase) {
    PurchaseFragment fragment = new PurchaseFragment();
    Bundle args = new Bundle();
    args.putSerializable(PURCHASE, (Serializable) purchase);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mPurchase = (Purchase) getArguments().getSerializable(PURCHASE);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    if (container == null) {
      // We have different layouts, and in one of them this
      // fragment's containing frame doesn't exist.  The fragment
      // may still be created from its saved state, but there is
      // no reason to try to create its view hierarchy because it
      // won't be displayed.  Note this is not needed -- we could
      // just run the code below, where we would create and return
      // the view hierarchy; it would just never be used.
      return null;
    }

    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_purchase, container, false);
    TextView title = (TextView) view.findViewById(R.id.fragment_title);
    title.setText(mPurchase.getTitle());

    DecimalFormat format = new DecimalFormat("#.00");
    TextView value= (TextView) view.findViewById(R.id.fragment_value);
    value.setText(format.format(mPurchase.getRealValue()));

    TextView usdTitle = (TextView) view.findViewById(R.id.fragment_usd_title);
    usdTitle.setText(R.string.usd_value_title);

    TextView usdValue = (TextView) view.findViewById(R.id.fragment_usd_value);
    usdValue.setText(format.format(mPurchase.getRealUsdValue()));

    return view;
  }
}
