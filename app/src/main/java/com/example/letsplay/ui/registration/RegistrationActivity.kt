package com.example.letsplay.ui.registration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.letsplay.R
import com.example.letsplay.enitity.auth.CreateUserResponse
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class RegistrationActivity : AppCompatActivity(), RegistrationContract.View {

    override val presenter: RegistrationContract.Presenter by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        presenter.createUser("asd", "asd", "asd")

    }

    override fun showRegistrationError(msg: String?) {
        Toast.makeText(this, "Error message: $msg", Toast.LENGTH_LONG).show()
    }

    override fun showRegistrationSuccess(response: CreateUserResponse) {
        Toast.makeText(this, "Success $response", Toast.LENGTH_LONG).show()
    }
}
