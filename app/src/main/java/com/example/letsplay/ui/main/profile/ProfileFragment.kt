package com.example.letsplay.ui.main.profile

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.letsplay.R
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
import com.example.letsplay.helper.Logger
import com.example.letsplay.helper.utility.BitmapUtility

class ProfileFragment : BaseFragment(), ProfileContract.View{

    private val GALLERY_REQUEST_CODE = 123
    private var imageId: Int = -1

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

                    val photoFile = File(activity!!.filesDir, "photo.jpg")

                    BitmapUtility.saveBitmapToFile(selectedImage, photoFile.absolutePath)

                    val imageBody = ImageBody("jpg", photoFile.path)
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

    override fun onPhotoUploadSuccess(photoDto: PhotoDto) {

    }

    override fun onPhotoUploadError(msg: String?) {
        Toast.makeText(context, "$msg", Toast.LENGTH_LONG).show()
    }

    override fun onGetUserSuccess(userDto: UserDto) {
        if (!userDto.firstName.equals("") && !userDto.lastName.equals("")){
            name.text = getString(R.string.profile_full_name, userDto.firstName, userDto.nickname, userDto.lastName)
        }else{
            name.text = userDto.nickname
        }
        region.text = userDto.cityName

        for (i in userDto.userPhotos.indices){
            if (userDto.userPhotos.get(i).avatar!!){
                Logger.msg("here1")
                imageId = userDto.userPhotos.get(i).id!!
            }
        }

        if (userDto.dateOfBirth != null){
            Logger.msg("here2true")

        }else{
            Logger.msg("here2")
            genderAge.text = userDto.gender
        }

        if (imageId != -1){
            presenter.getPhoto(imageId)
        }


    }

    override fun onGetUserError(msg: String?) {
        Toast.makeText(context, "$msg", Toast.LENGTH_LONG).show()
    }

    override fun onGetImageSuccess(photoDto: PhotoDto) {
//        Picasso.with(context).load()
    }

    override fun onGetImageError(msg: String?) {
        Toast.makeText(context, "$msg", Toast.LENGTH_LONG).show()
    }
}