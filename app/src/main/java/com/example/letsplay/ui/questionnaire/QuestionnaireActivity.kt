package com.example.letsplay.ui.questionnaire

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.letsplay.R
import com.example.letsplay.enitity.auth.UserDto
import com.example.letsplay.enitity.common.City
import com.example.letsplay.enitity.profile.UserUpdateRequest
import com.example.letsplay.helper.ConstantsExtra
import com.example.letsplay.helper.DialogListAdapter
import com.example.letsplay.helper.utility.visible
import com.example.letsplay.ui.common.BaseActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_questionnaire.*
import kotlinx.android.synthetic.main.custom_alert_dialog.view.*
import kotlinx.android.synthetic.main.table_cell_optional.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.util.*
import kotlin.collections.ArrayList


class QuestionnaireActivity : BaseActivity(), QuestionnaireContract.View {

    override val presenter: QuestionnaireContract.Presenter by inject { parametersOf(this) }

    internal var dialog: AlertDialog? = null
    internal var builder: MaterialAlertDialogBuilder? = null
    internal var dialogView: View? = null
    internal var adapter: DialogListAdapter? = null
    internal var context: Context? = null

    internal lateinit var cityList: ArrayList<String>
    internal lateinit var cityCodeList: ArrayList<String>
    internal lateinit var genderList: ArrayList<String>

    internal var cityCode: String? = null

    lateinit var userDtoOld: UserDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire)

        createSupportActionBar(toolbar as Toolbar, getString(R.string.edit_profile))

        userDtoOld = intent.getParcelableExtra(ConstantsExtra.USER_DTO)

        initializeViews()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        menu?.findItem(R.id.act_save)?.isVisible = true

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.act_save -> {
                val userUpdateRequest = UserUpdateRequest(dateOfBirth.value.editText?.text.toString(),
                    gender.value.editText?.text.toString().toUpperCase(),
                    cityCode!!,
                    name.value.editText?.text.toString(),
                    surname.value.editText?.text.toString(),
                    nickname.value.editText?.text.toString())
                presenter.updateUser(userUpdateRequest)
                Toast.makeText(this, "will be saved", Toast.LENGTH_LONG).show()
            }
        }
        return super.onOptionsItemSelected(item)
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
        runOnUiThread {
            showCustomDialog(cityList, city.value.editText)
            adapter?.notifyDataSetChanged()
        }
    }

    override fun onUserUpdateSuccess(userDto: UserDto) {
        finish()
    }

    override fun onUserUpdateError(msg: String?) {
        Toast.makeText(context, "$msg", Toast.LENGTH_LONG).show()
    }

    private fun initializeViews(){
        nickname.value.hint = getString(R.string.nickname)
        name.value.hint = getString(R.string.name)
        surname.value.hint = getString(R.string.surname)
        gender.value.hint = getString(R.string.gender)
        gender.spinner.visible()
        dateOfBirth.value.hint = getString(R.string.birth_date)
        dateOfBirth.spinner.visible()
        dateOfBirth.spinner.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_calendar))
        city.value.hint = getString(R.string.city)
        city.spinner.visible()

        genderList = ArrayList(Arrays.asList(*resources.getStringArray(R.array.gender)))

        gender.value.editText?.isFocusable = false
        gender.value.editText?.isClickable = true
        dateOfBirth.value.editText?.isFocusable = false
        dateOfBirth.value.editText?.isClickable = true
        city.value.editText?.isFocusable = false
        city.value.editText?.isClickable = true

        createCustomDialog()

        gender.value.editText?.setOnClickListener {
            selectGender()

        }

        dateOfBirth.value.editText?.setOnClickListener {
            selectBirthDate()
        }

        city.value.editText?.setOnClickListener {
            selectCity()
        }

        userDtoOld.cityName.let {
            city.value.editText?.setText(it)
        }
        userDtoOld.cityCode.let {
            cityCode = it
        }
    }

    private fun selectGender(){
        showCustomDialog(genderList, gender.value.editText)

        gender.value.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                dialog?.cancel()
            }
        })
    }

    private fun selectBirthDate() { showCustomDialog(dateOfBirth.value.editText) }

    private fun selectCity() {
        showCustomDialog(null, null)
        presenter.getCities()
        city.value.editText?.addTextChangedListener(object : TextWatcher {
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
        builder = MaterialAlertDialogBuilder(this)
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
            adapter = DialogListAdapter(this, list, editField)
            dialogView?.rv!!.adapter = adapter
            dialogView?.rv!!.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
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
}

interface OnItemClick {
    fun show()
}
