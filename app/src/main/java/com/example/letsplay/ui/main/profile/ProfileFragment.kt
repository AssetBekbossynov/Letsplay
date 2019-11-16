package com.example.letsplay.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.letsplay.R
import com.example.letsplay.enitity.auth.UserDto
import com.example.letsplay.ui.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class ProfileFragment : BaseFragment(), ProfileContract.View{

    companion object{
        fun newInstance(): ProfileFragment{
            return ProfileFragment()
        }
    }

    override val presenter: ProfileContract.Presenter by inject { parametersOf(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.getUser()
    }

    override fun onGetUserSuccess(userDto: UserDto) {
        if (userDto.firstName != null && userDto.lastName != null){
            name.text = getString(R.string.profile_full_name, userDto.firstName, userDto.nickname, userDto.lastName)
        }else{
            name.text = userDto.nickname
        }
        region.text = userDto.cityName

        if (userDto.dateOfBirth != null){

        }else{
            genderAge.text = userDto.gender
        }

    }

    override fun onGetUserError(msg: String?) {
        Toast.makeText(context, "$msg", Toast.LENGTH_LONG).show()
    }

}