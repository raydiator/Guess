package com.mti.rayex03;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;

public class SettingsActivity extends PreferenceActivity {
	private SharedPreferences pref;
	private EditTextPreference minInput;
	private EditTextPreference maxInput;
	int minNum;
	int maxNum;
	int input;
	int correctNumber;
	String minString, maxString;
	int minInt, maxInt;
	Preference minPref;
	Preference maxPref;
	DecimalFormat df = new DecimalFormat("###,###,###.###");
	String current_min = String.valueOf(minNum);
	Random r = new Random();
	public static boolean isValueChanged;
	NumberFormat nf = NumberFormat.getNumberInstance();

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new SettingsFragment()).commit();

	}
	
	@SuppressLint("ValidFragment")
	public class SettingsFragment extends PreferenceFragment implements
			OnPreferenceChangeListener, TextWatcher {

		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.layout.preferences);

			minPref = findPreference(getString(R.string.activity_settings_min_key));
			maxPref = findPreference(getString(R.string.activity_settings_max_key));
			minInput = (EditTextPreference) minPref;
			maxInput = (EditTextPreference) maxPref;
			minInput.getEditText().addTextChangedListener(this);
			maxInput.getEditText().addTextChangedListener(this);
			
			minInput.setOnPreferenceChangeListener(this);
			maxInput.setOnPreferenceChangeListener(this);
			
		minInput.setSummary(String.valueOf(nf.format(MainActivity.minNum)));
		maxInput.setSummary(String.valueOf(nf.format(MainActivity.maxNum)));

		}
		
		
		@Override
		public void afterTextChanged(Editable s) {
			if (!s.toString().matches(
					"^\\$(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$")) {
				String userInput = "" + s.toString().replaceAll("[^\\d]", "");
				StringBuilder userInputBuilder = new StringBuilder(userInput);


				if (userInputBuilder.length() == 9) {
					userInputBuilder.insert(3, ',');
					userInputBuilder.insert(7, ',');

				}
				if (userInputBuilder.length() == 8) {
					userInputBuilder.insert(2, ',');
					userInputBuilder.insert(6, ',');

				}
				if (userInputBuilder.length() == 7) {
					userInputBuilder.insert(1, ',');
					userInputBuilder.insert(5, ',');

				}
				if (userInputBuilder.length() == 6) {
					userInputBuilder.insert(3, ',');
				}
				if (userInputBuilder.length() == 5) {
					userInputBuilder.insert(2, ',');
				}
				if (userInputBuilder.length() == 4) {
					userInputBuilder.insert(1, ',');
				}
				
				minInput.getEditText().removeTextChangedListener(this);
				minInput.getEditText().setText(userInputBuilder.toString());
				minInput.getEditText().setTextKeepState(userInputBuilder.toString());
				Selection.setSelection(minInput.getEditText().getText(), userInputBuilder
						.toString().length());
				minInput.getEditText().addTextChangedListener(this);
				
				maxInput.getEditText().removeTextChangedListener(this);
				maxInput.getEditText().setText(userInputBuilder.toString());
		//		maxInput.getEditText().setTextKeepState(userInputBuilder.toString());
				Selection.setSelection(maxInput.getEditText().getText(), userInputBuilder
						.toString().length());
				maxInput.getEditText().addTextChangedListener(this);
			}
			}


		
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}
		


		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {

			pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
			String minString = pref.getString(
					getString(R.string.activity_settings_min_key), "0");
			String maxString = pref.getString(
					getString(R.string.activity_settings_max_key), "100");
			String minFormatted = minString.replaceAll("[^\\d.]+",
					"");
			String maxFormatted = maxString.replaceAll("[^\\d.]+",
					"");
			int minInt = Integer.parseInt(minFormatted);
			int maxInt = Integer.parseInt(maxFormatted);

			if (preference == minPref) {
				if (newValue.toString().length() > 0) {
					String newValueFormatted = ((String) newValue).replaceAll("[^\\d.]+",
							"");
					int newEntry = Integer.parseInt((String) newValueFormatted);
					if (newEntry < maxInt) {
						MainActivity.isValueChanged = true;
						minInput.setSummary(String.valueOf(nf.format(newEntry)));
						
						return true;
					} else {
						minOutOfRangeAlert();
						return false;
					}
				} else {
					blankEntryAlert();
					return false;
				}
			}
			else if (preference == maxPref) {
				if (newValue.toString().length() > 0) {
					String newValueFormatted = ((String) newValue).replaceAll("[^\\d.]+",
							"");
					int newEntry = Integer.parseInt((String) newValueFormatted);
					if (newEntry > minInt) {
						MainActivity.isValueChanged = true;
						maxInput.setSummary(String.valueOf(nf.format(newEntry)));
						return true;
					} else {
						maxOutOfRangeAlert();
						return false;
					}
				} else {
					blankEntryAlert();
					return false;
				}
			} else {
				return false;
			}

		}

		

		public void blankEntryAlert() {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					super.getActivity());
			alertDialogBuilder.setTitle("Invalid Range");
			alertDialogBuilder
					.setMessage("Please enter a number!")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}

		public void minOutOfRangeAlert() {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					super.getActivity());
			alertDialogBuilder.setTitle("Invalid Range");
			alertDialogBuilder
					.setMessage(
							"Please enter a minimum that is lower than the maximum!")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

			AlertDialog alertDialog = alertDialogBuilder.create();

			alertDialog.show();
		}
		
		public void maxOutOfRangeAlert() {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					super.getActivity());

			alertDialogBuilder.setTitle("Invalid Range");

			alertDialogBuilder
					.setMessage(
							"Please enter a minimum that is lower than the maximum!")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog,
										int id) {			
									dialog.cancel();
								}
							});

			AlertDialog alertDialog = alertDialogBuilder.create();

			alertDialog.show();
		}


	}
}
