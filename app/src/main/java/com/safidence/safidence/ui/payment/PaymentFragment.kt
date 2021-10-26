package com.safidence.safidence.ui.payment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.safidence.safidence.R
import com.safidence.safidence.api.ApiHelper
import com.safidence.safidence.api.ApiServiceImpl
import com.safidence.safidence.data.model.BodyDuePayment
import com.safidence.safidence.data.model.ResponseTenantUnits
import com.safidence.safidence.data.prefs.SavePref
import com.safidence.safidence.databinding.FragmentPaymentBinding
import com.safidence.safidence.ui.base.ViewModelFactory

class PaymentFragment : Fragment() {

    private lateinit var paymentViewModel: PaymentViewModel
    private var _binding: FragmentPaymentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initViews()
        setupObserver()

        paymentViewModel.getTenantUnits(SavePref(requireContext()).getAccessToken())
        showProgressDialog()
        return root
    }

    private fun setupViewModel() {
        paymentViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(PaymentViewModel::class.java)
    }

    private fun initViews() {
        binding.tvTitle.text = "Hello ".plus(SavePref(requireContext()).getUserName()).plus("!")
        binding.cvCreditCard.setOnClickListener {
            findNavController().navigate(R.id.action_nav_payment_to_nav_credit_card, setArguments())
        }
        binding.cvCashPayment.setOnClickListener {
            findNavController().navigate(R.id.action_nav_payment_to_nav_cash_payment, setArguments())
        }
        binding.cvBankDeposit.setOnClickListener {
            findNavController().navigate(R.id.action_nav_payment_to_nav_bank_deposit, setArguments())
        }
        binding.cvCheque.setOnClickListener {
            findNavController().navigate(R.id.action_nav_payment_to_nav_cheque, setArguments())
        }
    }

    private fun setArguments():Bundle {
        val bundle = Bundle()
        bundle.putString("due_date", data.due_date)
        bundle.putString("due_payment", data.rent)
        bundle.putString("pay_till", data.pay_till)
        bundle.putString("unit_id", unitId.toString())
        return bundle
    }

    private fun setupObserver() {
        paymentViewModel.getResponseTenantUnits().observe(viewLifecycleOwner, Observer {
            if (it.status == "success") {
                setUnitsSpinner(it)
                binding.loading.visibility = View.GONE
            }
        })

        paymentViewModel.getResponse().observe(viewLifecycleOwner, Observer {
            dismissDialog()
            if (it.status == "success") {
                setPaymentDetails(it.body)
            }
        })

        paymentViewModel.getExceptionResponse().observe(viewLifecycleOwner, Observer {
            dismissDialog()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    private lateinit var data: BodyDuePayment
    private fun setPaymentDetails(body: BodyDuePayment) {
        binding.tvDueDate.text = "Due Date: ".plus(body.due_date)
        binding.tvDuePayment.text = "Due Payment: ".plus(body.rent)
        data = body
    }

    private var unitId = 0
    private fun setUnitsSpinner(selections: ResponseTenantUnits) {
        val unitTitles = ArrayList<String>()
        for (item in selections.body) {
            unitTitles.add(item.name)
        }

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item, unitTitles
        )
        binding.acTvUnits.setAdapter(adapter)
        binding.acTvUnits.setText(unitTitles[0], false)

        //Call web service to get due payment details
        val token = SavePref(requireContext()).getAccessToken()
        unitId = selections.body[0].id
        paymentViewModel.getDuePayment(token, unitId)

        binding.acTvUnits.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, position, p3 ->

                unitId = selections.body[position].id
                paymentViewModel.getDuePayment(token, unitId)
                showProgressDialog()
            }
    }

    private lateinit var progressDialog: ProgressDialog
    private fun showProgressDialog() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage(resources.getString(R.string.please_wait))
        progressDialog.show()
    }

    private fun dismissDialog() {
        if (this::progressDialog.isInitialized)
            progressDialog.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}