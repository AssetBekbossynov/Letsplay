package com.example.letsplay.ui.main.profile

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.example.letsplay.enitity.auth.PhotoDto
import com.example.letsplay.enitity.auth.UserDto
import com.example.letsplay.enitity.common.ImageBody
import com.example.letsplay.ui.common.BaseFragment
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.io.File
import android.graphics.Bitmap
import android.view.*
import com.example.letsplay.R
import com.example.letsplay.helper.ConstantsExtra
import com.example.letsplay.helper.Logger
import com.example.letsplay.helper.utility.BitmapUtility
import com.example.letsplay.helper.utility.DateUtility
import com.example.letsplay.helper.utility.gone
import com.example.letsplay.helper.utility.visible
import com.example.letsplay.ui.questionnaire.QuestionnaireActivity
import java.util.*


class ProfileFragment : BaseFragment(), ProfileContract.View{

    private val GALLERY_REQUEST_CODE = 123
    private var imageId: Int = -1

    lateinit var userDto: UserDto

    companion object{
        fun newInstance(): ProfileFragment{
            return ProfileFragment()
        }
    }

    override val presenter: ProfileContract.Presenter by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profilePhoto.setOnClickListener {

            val options = arrayOf<CharSequence>(getString(R.string.take_photo), getString(R.string.choose_from_gallery), getString(R.string.cancel))

            val builder = AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.choose_photo))
            builder.setItems(options, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    if (options[which].equals(getString(R.string.take_photo))) {
                        val takePicture = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, 0);

                    } else if (options[which].equals(getString(R.string.choose_from_gallery))) {
                        val pickPhoto = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto , 1);

                    } else if (options[which].equals(getString(R.string.cancel))) {
                        dialog?.dismiss()
                    }
                }
            })
            builder.show()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.getUser()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == RESULT_OK && data != null) {
                    val selectedImage = data.extras?.get("data") as Bitmap

                    val photoFile = File(activity!!.filesDir, "photo.png")

                    BitmapUtility.saveBitmapToFile(selectedImage, photoFile.absolutePath)

                    val imageBody = ImageBody("png", photoFile.path)
                    Logger.msg("wtf gotoupload")
                    presenter.uploadPhoto(imageBody)
                    Picasso.with(context).load(photoFile)
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .into(profilePhoto)
                }
                1 -> if (resultCode == RESULT_OK && data != null) {
                    val selectedImage = data.data
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    if (selectedImage != null) {
                        val cursor = context?.contentResolver!!.query(
                            selectedImage,
                            filePathColumn, null, null, null
                        )
                        if (cursor != null) {
                            cursor.moveToFirst()
                            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                            val mediaPath = cursor.getString(columnIndex)
                            // Set the Image in ImageView for Previewing the Media
                            val imageBody = ImageBody("jpg", mediaPath)
                            presenter.uploadPhoto(imageBody)
                            Picasso.with(context).load(File(mediaPath))
                                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                .into(profilePhoto)
//                            imgView.setImageBitmap(BitmapFactory.decodeFile(mediaPath))
                            cursor.close()
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.apply {
            findItem(R.id.act_edit)?.apply {
                setIcon(R.drawable.ic_edit)
                isVisible = true
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.act_edit -> {
                val intent = Intent(context, QuestionnaireActivity::class.java)
                intent.putExtra(ConstantsExtra.USER_DTO, userDto)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPhotoUploadSuccess(photoDto: PhotoDto) {
        Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
    }

    override fun onPhotoUploadError(msg: String?) {
        Toast.makeText(context, "$msg", Toast.LENGTH_LONG).show()
    }

    override fun onGetUserSuccess(userDto: UserDto) {
        this.userDto = userDto

        var nameLabel = ""

        if (!userDto.lastName.equals("")){
            nameLabel = nameLabel + userDto.lastName
        }

        if (!userDto.firstName.equals("")){
            if (nameLabel.equals(""))
                nameLabel = nameLabel + userDto.firstName
            else
                nameLabel = nameLabel + " " + userDto.firstName
        }

        if (nameLabel.equals("")){
            name.gone()
        }else{
            name.visible()
            name.text = nameLabel
        }

        nickname.text = userDto.nickname

        region.text = userDto.cityName

        val age = getAge(DateUtility.stringToCalendar(DateUtility.getDashedYMDdateFormat(),
            userDto.dateOfBirth), Calendar.getInstance())

        if (userDto.dateOfBirth != null){
            genderAge.text = resources.getQuantityString(R.plurals.profile_gender_age,
                age.toInt(), userDto.gender, age)
        }else{
            genderAge.text = userDto.gender
        }

        for (i in userDto.userPhotos.indices){
            if (userDto.userPhotos.get(i).avatar!!){
                imageId = userDto.userPhotos.get(i).id!!
            }
        }

//        if (imageId != -1){
//            presenter.getPhoto(imageId)
//        }
    }

    override fun onGetUserError(msg: String?) {
        msg?.let {
            if (!msg.contains("INCOMPLETE_ACCOUNT"))
                Toast.makeText(context, "$msg", Toast.LENGTH_LONG).show()
        }
    }

    override fun onGetImageSuccess(bitmap: Bitmap) {
        profilePhoto.setImageBitmap(bitmap)
    }

    override fun onGetImageError(msg: String?) {
        Toast.makeText(context, "$msg", Toast.LENGTH_LONG).show()
    }

    private fun getAge(dob: Calendar, today: Calendar): String {
        var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        val ageInt = age

        return ageInt.toString()
    }
}