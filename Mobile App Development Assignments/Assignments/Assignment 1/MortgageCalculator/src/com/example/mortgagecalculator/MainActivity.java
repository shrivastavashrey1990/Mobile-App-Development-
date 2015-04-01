package com.example.mortgagecalculator;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu; 
import android.text.Editable; 
import android.text.TextWatcher; 
import android.widget.EditText; 
import android.widget.SeekBar; 
import android.widget.SeekBar.OnSeekBarChangeListener; 
import android.widget.TextView; 
public class MainActivity extends Activity {

	private static final String Total_Payment = "TOTAL_PAYMENT";    
	private static final String Annual_IR = "Annual_IR";    
	private double currentLoanAmount;  
	private double currentAnnualIR;  
	private EditText TenEditText;  
	private EditText TenTotalPaymentEditText;  
	private EditText FifteenEditText;  
	private EditText fifteenTotalPaymenteditText;  
	private EditText LoanAmountEditText;  
	private EditText ThirtyEditText;  
	private EditText ThirtyTotalPaymentEditText;   

	private TextView AIRtextView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		if(savedInstanceState == null ) //the app just started running
		{
			currentLoanAmount = 0.0;          
			currentAnnualIR = 4; 
		}
		else  // app is being restored from memory, not executed from scratch 
		{

			currentLoanAmount=savedInstanceState.getDouble(Total_Payment);
			currentAnnualIR=savedInstanceState.getInt(Annual_IR);
		}
		TenEditText=(EditText)findViewById(R.id.TenEditText);
		TenTotalPaymentEditText=(EditText)findViewById(R.id.TenTotalPaymentEditText);

		FifteenEditText=(EditText)findViewById(R.id.FifteenEditText);
		fifteenTotalPaymenteditText=(EditText)findViewById(R.id.fifteenTotalPaymenteditText);

		ThirtyEditText=(EditText)findViewById(R.id.ThirtyEditText);
		ThirtyTotalPaymentEditText=(EditText)findViewById(R.id.ThirtyTotalPaymentEditText);


		LoanAmountEditText=(EditText)findViewById(R.id.LoanAmountEditText);

		AIRtextView=(TextView)findViewById(R.id.AIRtextView);

		LoanAmountEditText.addTextChangedListener(LoanAmountEditTextWatcher);
		SeekBar customSeekBar=(SeekBar)findViewById(R.id.customSeekBar);
		customSeekBar.setOnSeekBarChangeListener(customSeekBarListener); 
	}

	private void updateStandard()
	{
		//calculation for ten years
		double monthlyInterestRate= currentAnnualIR/(12*100);

		double tenYearMonthlyPayment= 
				currentLoanAmount * monthlyInterestRate/(1-Math.pow((1+monthlyInterestRate),-120));

		double tenYearTotalPayment=tenYearMonthlyPayment*120; 
		TenEditText.setText(String.format("%.02f", tenYearMonthlyPayment));
		TenTotalPaymentEditText.setText(String.format("%.02f",tenYearTotalPayment ));

		//Calculation for fifteen years
		double fifteenYearMonthlyPayment= 
				currentLoanAmount * monthlyInterestRate/(1-Math.pow((1+monthlyInterestRate),-180));

		double fifteenYearTotalPayment=fifteenYearMonthlyPayment*120; 
		FifteenEditText.setText(String.format("%.02f", fifteenYearMonthlyPayment));
		fifteenTotalPaymenteditText.setText(String.format("%.02f",fifteenYearTotalPayment ));

		//Calculation for thirty years
		double thirtyYearMonthlyPayment=
				currentLoanAmount * monthlyInterestRate/ (1-Math.pow((1+monthlyInterestRate),-360));

		double thirtyYearTotalPayment=thirtyYearMonthlyPayment*120; 
		ThirtyEditText.setText(String.format("%.02f", thirtyYearMonthlyPayment));
		ThirtyTotalPaymentEditText.setText(String.format("%.02f",thirtyYearTotalPayment ));

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putDouble(Total_Payment,currentLoanAmount );
		outState.putDouble(Annual_IR,currentAnnualIR );
	}

	// called when the user changes the position of SeekBar 
	private OnSeekBarChangeListener customSeekBarListener =     
			new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			//sets currentAnnualIR to position of seekbar's thumb
			double temp=seekBar.getProgress();
			currentAnnualIR=temp*(0.3);
			updateCustom();
			updateStandard();
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub

		}

	};

	private TextWatcher LoanAmountEditTextWatcher = new TextWatcher() 
	{

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int count,
				int before) {
			// TODO Auto-generated method stub
			try
			{
				currentLoanAmount=Double.parseDouble(s.toString());
			}
			catch(NumberFormatException e)
			{
				currentLoanAmount=0.0;
			}
			updateStandard();
			updateCustom();

		}

	};
	private void updateCustom()
	{
		AIRtextView.setText(currentAnnualIR+ "%");

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
