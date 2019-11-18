package com.example.letsplay.ui.auth.register

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.letsplay.ui.common.BaseFragment
import com.example.letsplay.R
import com.example.letsplay.enitity.auth.OtpResponse
import com.example.letsplay.enitity.common.City
import com.example.letsplay.helper.DialogListAdapter
import com.example.letsplay.helper.Logger
import com.example.letsplay.helper.utility.visible
import com.example.letsplay.ui.auth.ContentChangedListener
import com.example.letsplay.ui.auth.login.LoginFragment
import com.example.letsplay.ui.auth.otp.OtpCheckFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.custom_alert_dialog.view.*
import kotlinx.android.synthetic.main.registration_fragment.*
import kotlinx.android.synthetic.main.table_cell_optional.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class RegistrationFragment: BaseFragment(),
    RegistrationContract.View {

    internal var dialog: AlertDialog? = null
    internal var builder: MaterialAlertDialogBuilder? = null
    internal var dialogView: View? = null
    internal var adapter: DialogListAdapter? = null
    internal var context: Context? = null
    private var maskedTextChangedListener: MaskedTextChangedListener? = null
    internal lateinit var cityList: ArrayList<String>
    internal lateinit var cityCodeList: ArrayList<String>
    internal var cityCode: String? = null
    internal var phoneNumber: String? = null

    override val presenter: RegistrationContract.Presenter by inject { parametersOf(this) }

    private lateinit var listener: ContentChangedListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.listener = context as ContentChangedListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.registration_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context = activity

        cityName.spinner.visible()
        cityName.value.editText?.isFocusable = false
        cityName.value.editText?.isClickable = true
        cityName.value.hint = getString(R.string.select_city)

        cityName.value.editText?.setOnClickListener {
            selectCity()
        }

        val editText = phone!!.editText!!
        editText.setText("+7 (")
        maskedTextChangedListener = MaskedTextChangedListener(
            "+7 ([000]) [0000000]",
            true,
            editText,
            null,
            object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(maskFilled: Boolean, extractedValue: String) {
                    phoneNumber = "7$extractedValue"
                }
            }
        )
        editText.addTextChangedListener(maskedTextChangedListener)
        Selection.setSelection(editText.text, editText.text.toString().length);

        next.setOnClickListener {
            if (!cityName.value.editText?.text.toString().equals("") && phone.editText?.text.toString().length > 4 &&
                    !password.editText?.text.toString().equals("")){
                if (password.editText?.text.toString().equals(password_again.editText?.text.toString())){
                    next.isEnabled = false
                    presenter.createUser(cityCode, password.editText?.text.toString(), phoneNumber)
                }else{
                    Toast.makeText(context, getString(R.string.doesnot_match_error), Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(context, getString(R.string.field_must_not_be_empty), Toast.LENGTH_LONG).show()
            }
        }

        have_account.setOnClickListener {
            listener.onContentChange(LoginFragment.newInstance(), false)
        }

        createCustomDialog()

        password.editText?.setText("Test1234!")
        password_again.editText?.setText("Test1234!")

    }

    private fun selectCity() {
        showCustomDialog(null, null)
        presenter.getCities()
        cityName.value.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                dialog?.cancel()
                if (adapter != null) {
                    cityCode = cityCodeList[adapter?.itemId!!]
                    adapter = null
                }
            }
        })
    }

    private fun createCustomDialog(){
        builder = MaterialAlertDialogBuilder(context!!)
        val layoutInflater = layoutInflater
        dialogView = layoutInflater.inflate(R.layout.custom_alert_dialog, null)
        val search = dialogView?.findViewById<EditText>(R.id.search)
        builder?.setView(dialogView)
        search!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                adapter?.filter?.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable) {}
        })
        dialog = builder?.create()
    }

    private fun showCustomDialog(list: ArrayList<String>?, editField: EditText?) {
        if (list != null){
            dialogView?.search!!.visibility = View.VISIBLE
            dialogView?.rv!!.visibility = View.VISIBLE
            dialogView?.cancel!!.visibility = View.VISIBLE
            dialogView?.progress_bar!!.visibility = View.GONE
            adapter = DialogListAdapter(context!!, list, editField)
            dialogView?.rv!!.adapter = adapter
            dialogView?.rv!!.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context!!, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
            if (adapter?.itemCount!! > 8){
                dialogView?.findViewById<EditText>(R.id.search)!!.visibility = View.VISIBLE
            }else{
                dialogView?.findViewById<EditText>(R.id.search)!!.visibility = View.GONE
            }
            dialogView?.cancel!!.setOnClickListener {
                dialog?.cancel()
            }
        }else{
            dialogView?.search!!.visibility = View.GONE
            dialogView?.rv!!.visibility = View.GONE
            dialogView?.cancel!!.visibility = View.GONE
            dialogView?.progress_bar!!.visibility = View.VISIBLE
        }
        dialog?.show()
    }

    override fun showRegistrationError(msg: String?) {
        next.isEnabled = true
        Toast.makeText(context, "$msg", Toast.LENGTH_LONG).show()
    }

    override fun showRegistrationSuccess(dto: OtpResponse) {
        next.isEnabled = true
        listener.onContentChange(OtpCheckFragment.newInstance(dto))
    }

    override fun onGetCitiesError(msg: String?) {
        Toast.makeText(context, "$msg", Toast.LENGTH_LONG).show()
    }

    override fun onGetCitiesSuccess(cities: List<City>) {
        cityList = ArrayList()
        cityCodeList = ArrayList()
        for (i in cities.indices) {
            cities[i].name.let { cityList.add(it) }
            cities[i].code.let { cityCodeList.add(it) }
        }
        activity?.runOnUiThread {
            showCustomDialog(cityList, cityName.value.editText)
            adapter?.notifyDataSetChanged()
        }
    }

    override fun openLoginFragment(msg: String?) {
        Toast.makeText(context, "$msg", Toast.LENGTH_LONG).show()
        replaceFragment(R.id.fragment_container, LoginFragment.newInstance(), true)
    }

    companion object{
        fun newInstance(): RegistrationFragment {
            return RegistrationFragment()
        }
    }
}