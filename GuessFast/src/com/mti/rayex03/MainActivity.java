package com.mti.rayex03;

import java.text.NumberFormat;
import java.util.Random;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements OnClickListener,
		TextWatcher {

	private EditText inputNumber;
	private TextView highLowCorrectDisplay;
	private TextView textView2;
	private Button guess_button;
	private TextView textView3;
	public static int minNum;
	public static int maxNum;
	int input;
	int correctNumber;
	String minString, maxString;
	int minInt, maxInt;
	private TextView textView1;
	SharedPreferences pref;
	Random r = new Random();
	public static boolean isValueChanged;
	int guesses;
	NumberFormat nf = NumberFormat.getNumberInstance();
	private CheckBox showAnswer;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		isValueChanged = false;
	
		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);
		textView3 = (TextView) findViewById(R.id.textView3);
		inputNumber = (EditText) findViewById(R.id.inputNumber);
		guess_button = (Button) findViewById(R.id.guess_button);
		guess_button.setText("Start Game");
		highLowCorrectDisplay = (TextView) findViewById(R.id.highLowCorrectDisplay);
		highLowCorrectDisplay.setText("Number of Guesses: " + guesses);
		guess_button.setOnClickListener(this);
		minNum = 0;
		maxNum = 100;
		correctNumber = minNum + r.nextInt(maxNum - minNum + 1);
		inputNumber.addTextChangedListener(this);
		addListenerOnChkWindows();
		textView3.setText("Enter a number between "
				+ String.valueOf(nf.format(minNum)) + " and "
				+ String.valueOf(nf.format(maxNum)));
	

	}

	public void addListenerOnChkWindows() {

		showAnswer = (CheckBox) findViewById(R.id.checkBox1);
		showAnswer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (((CheckBox) v).isChecked() == true) {

					textView2.setText(String.valueOf(nf.format(correctNumber)));

				} else if (((CheckBox) v).isChecked() == false) {
					textView2.setText("");

				}
			}

		});
	}

	@Override
	public void onClick(View v) {
		if (v == guess_button) {
			String inputNumberValue = inputNumber.getText().toString();
			inputNumber.setText("");
			if (inputNumberValue.length() > 0) {
				
				String inputFormatted = String.valueOf(inputNumberValue).replaceAll("[^\\d.]+",
				"");
				input = Integer.parseInt(inputFormatted);
				if (input < minNum || input > maxNum) {
					highLowCorrectDisplay.setText("Out Of Range!");
				} else if (input > correctNumber) {
					highLowCorrectDisplay.setText("Too High!");
					guesses++;
					guess_button.setText("Guess Again");
				} else if (input < correctNumber) {
					highLowCorrectDisplay.setText("Too Low!");
					guesses++;
					guess_button.setText("Guess Again");
				} else if (input == correctNumber) {
					highLowCorrectDisplay.setText("Correct!");
					guesses = 0;
					guess_button.setText("Start Game");
					correctNumber = minNum + r.nextInt(maxNum - minNum + 1);
				}

			} else
				highLowCorrectDisplay.setText("Enter a number");

			textView1.setText("Number of Guesses:" + String.valueOf(nf.format(guesses)));
			
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so int
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			break;

		case R.id.action_about:
			startActivity(new Intent(this, AboutActivity.class));
			break;

		default:

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		if (isValueChanged == false) {
			super.onResume();
			showAnswer.setChecked(false);
			textView2.setText("");
		} else if (isValueChanged == true) {
			super.onResume();
			newGameAlert();
			pref = PreferenceManager.getDefaultSharedPreferences(this);
			minString = pref.getString(
					getString(R.string.activity_settings_min_key), "0");
			maxString = pref.getString(
					getString(R.string.activity_settings_max_key), "100");
			String minFormatted = minString.replaceAll("[^\\d.]+",
			"");
	String maxFormatted = maxString.replaceAll("[^\\d.]+",
			"");
			minInt = Integer.parseInt(minFormatted);
			maxInt = Integer.parseInt(maxFormatted);
			minNum = minInt;
			maxNum = maxInt;
			guess_button.setText("Start Game");
			guesses = 0;
			correctNumber = minNum + r.nextInt(maxNum - minNum + 1);
			isValueChanged = false;
			showAnswer.setChecked(false);
			textView2.setText("");
			
		}

		textView1.setText("Number of Guesses: " + guesses);
		highLowCorrectDisplay.setText("");
		textView3.setText("Enter a number between "
				+ String.valueOf(nf.format(minNum)) + " and "
				+ String.valueOf(nf.format(maxNum)));

	}

	public void newGameAlert() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("A new game has been started");
		alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();
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
			inputNumber.removeTextChangedListener(this);
			inputNumber.setText(userInputBuilder.toString());
			inputNumber.setTextKeepState(userInputBuilder.toString());
			Selection.setSelection(inputNumber.getText(), userInputBuilder
					.toString().length());
			inputNumber.addTextChangedListener(this);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

}

/*
 * // tutorial // check box to show answer
 * 
 * 
 * // timer public void stage2() { min = 1; max = 200; String inputNumberValue =
 * inputNumber.getText().toString(); String inputNumberValueFormatted =
 * inputNumberValue.replaceAll( "[^\\d.]+", ""); Random random = new Random();
 * int correctNumber = min + ((int) (random.nextLong() * (max - min))); int
 * input = Long.parseLong(inputNumberValueFormatted); if (input < min || input >
 * max) { highLowCorrectDisplay.setText("Enter a number between " + min +
 * " and " + max); } if (input > correctNumber) {
 * highLowCorrectDisplay.setText("Too High!"); }
 * 
 * if (input < correctNumber) { highLowCorrectDisplay.setText("Too Low!"); }
 * 
 * if (input == correctNumber) { highLowCorrectDisplay.setText("Correct!"); }
 * 
 * }
 * 
 * // timer public void stage3() { min = 1; max = 500; String inputNumberValue =
 * inputNumber.getText().toString(); String inputNumberValueFormatted =
 * inputNumberValue.replaceAll( "[^\\d.]+", ""); int input =
 * Long.parseLong(inputNumberValueFormatted); Random random = new Random(); int
 * correctNumber = min + ((int) (random.nextLong() * (max - min))); if (input <
 * min || input > max) { highLowCorrectDisplay.setText("Enter a number between "
 * + min + " and " + max); } if (input > correctNumber) {
 * highLowCorrectDisplay.setText("Too High!"); }
 * 
 * if (input < correctNumber) { highLowCorrectDisplay.setText("Too Low!"); }
 * 
 * if (input == correctNumber) { highLowCorrectDisplay.setText("Correct!"); }
 * 
 * }
 * 
 * // timer public void stage4() { min = 1; max = 1000; String inputNumberValue
 * = inputNumber.getText().toString(); String inputNumberValueFormatted =
 * inputNumberValue.replaceAll( "[^\\d.]+", ""); int input =
 * Long.parseLong(inputNumberValueFormatted); Random random = new Random(); int
 * correctNumber = min + ((int) (random.nextLong() * (max - min))); if (input <
 * min || input > max) { highLowCorrectDisplay.setText("Enter a number between "
 * + min + " and " + max); } if (input > correctNumber) {
 * highLowCorrectDisplay.setText("Too High!"); }
 * 
 * if (input < correctNumber) { highLowCorrectDisplay.setText("Too Low!"); }
 * 
 * if (input == correctNumber) { highLowCorrectDisplay.setText("Correct!"); }
 * 
 * }
 * 
 * // timer public void stage5() { min = 1; max = 1000; String inputNumberValue
 * = inputNumber.getText().toString(); String inputNumberValueFormatted =
 * inputNumberValue.replaceAll( "[^\\d.]+", ""); int input =
 * Long.parseLong(inputNumberValueFormatted); Random random = new Random(); int
 * correctNumber = min + ((int) (random.nextLong() * (max - min))); if (input <
 * min || input > max) { highLowCorrectDisplay.setText("Enter a number between "
 * + min + " and " + max); } if (input > correctNumber) {
 * highLowCorrectDisplay.setText("Too High!"); }
 * 
 * if (input < correctNumber) { highLowCorrectDisplay.setText("Too Low!"); }
 * 
 * if (input == correctNumber) { highLowCorrectDisplay.setText("Correct!"); }
 * 
 * }
 * 
 * // timer, penalty public void stage6() { min = 1; max = 100; String
 * inputNumberValue = inputNumber.getText().toString(); String
 * inputNumberValueFormatted = inputNumberValue.replaceAll( "[^\\d.]+", ""); int
 * input = Long.parseLong(inputNumberValueFormatted); Random random = new
 * Random(); int correctNumber = min + ((int) (random.nextLong() * (max -
 * min))); if (input < min || input > max) {
 * highLowCorrectDisplay.setText("Enter a number between " + min + " and " +
 * max); } if (input > correctNumber) {
 * highLowCorrectDisplay.setText("Too High!"); }
 * 
 * if (input < correctNumber) { highLowCorrectDisplay.setText("Too Low!"); }
 * 
 * if (input == correctNumber) { highLowCorrectDisplay.setText("Correct!"); }
 * 
 * }
 * 
 * // timer, penalty public void stage7() { min = 1; max = 100; String
 * inputNumberValue = inputNumber.getText().toString(); String
 * inputNumberValueFormatted = inputNumberValue.replaceAll( "[^\\d.]+", ""); int
 * input = Long.parseLong(inputNumberValueFormatted); Random random = new
 * Random(); int correctNumber = min + ((int) (random.nextLong() * (max -
 * min))); if (input < min || input > max) {
 * highLowCorrectDisplay.setText("Enter a number between " + min + " and " +
 * max); } if (input > correctNumber) {
 * highLowCorrectDisplay.setText("Too High!"); }
 * 
 * if (input < correctNumber) { highLowCorrectDisplay.setText("Too Low!"); }
 * 
 * if (input == correctNumber) { highLowCorrectDisplay.setText("Correct!"); }
 * 
 * }
 * 
 * // timer, penalty public void stage8() { min = 1; max = 100; String
 * inputNumberValue = inputNumber.getText().toString(); String
 * inputNumberValueFormatted = inputNumberValue.replaceAll( "[^\\d.]+", ""); int
 * input = Long.parseLong(inputNumberValueFormatted); Random random = new
 * Random(); int correctNumber = min + ((int) (random.nextLong() * (max -
 * min))); if (input < min || input > max) {
 * highLowCorrectDisplay.setText("Enter a number between " + min + " and " +
 * max); } if (input > correctNumber) {
 * highLowCorrectDisplay.setText("Too High!"); }
 * 
 * if (input < correctNumber) { highLowCorrectDisplay.setText("Too Low!"); }
 * 
 * if (input == correctNumber) { highLowCorrectDisplay.setText("Correct!"); }
 * 
 * }
 * 
 * // timer, penalty public void stage9() { min = 1; max = 100; String
 * inputNumberValue = inputNumber.getText().toString(); String
 * inputNumberValueFormatted = inputNumberValue.replaceAll( "[^\\d.]+", ""); int
 * input = Long.parseLong(inputNumberValueFormatted); Random random = new
 * Random(); int correctNumber = min + ((int) (random.nextLong() * (max -
 * min))); if (input < min || input > max) {
 * highLowCorrectDisplay.setText("Enter a number between " + min + " and " +
 * max); } if (input > correctNumber) {
 * highLowCorrectDisplay.setText("Too High!"); }
 * 
 * if (input < correctNumber) { highLowCorrectDisplay.setText("Too Low!"); }
 * 
 * if (input == correctNumber) { highLowCorrectDisplay.setText("Correct!"); }
 * 
 * }
 */

