package com.example.letsplay.ui.main.profile

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.example.letsplay.entity.auth.PhotoDto
import com.example.letsplay.entity.auth.UserDto
import com.example.letsplay.entity.common.ImageBody
import com.example.letsplay.ui.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.io.File
import android.view.*
import com.bumptech.glide.Glide
import com.example.letsplay.R
import com.example.letsplay.helper.ConstantsExtra
import com.example.letsplay.helper.Logger
import com.example.letsplay.helper.utility.DateUtility
import com.example.letsplay.helper.utility.gone
import com.example.letsplay.helper.utility.visible
import com.example.letsplay.ui.questionnaire.QuestionnaireActivity
import java.util.*
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import android.database.Cursor
import android.net.Uri
import androidx.core.content.ContextCompat
import com.example.letsplay.entity.profile.FriendsInfo
import com.example.letsplay.ui.main.SearchListener
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.view_profile_match_item.view.*


class ProfileFragment : BaseFragment(), ProfileContract.View{

    private var imageId: Int = -1

    lateinit var userDto: UserDto

    private lateinit var searchListener: SearchListener

    companion object{
        fun newInstance(nickname: String?): ProfileFragment{
            val fragment = ProfileFragment()
            val arguments = Bundle()
            arguments.putString(ConstantsExtra.NICKNAME, nickname)
            fragment.arguments = arguments
            return fragment
        }
    }

    override val presenter: ProfileContract.Presenter by inject { parametersOf(this) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (arguments?.getString(ConstantsExtra.NICKNAME) == null){
            searchListener = context as SearchListener
        }
    }

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

            context?.let {
                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setRequestedSize(400, 400)
                    .setAspectRatio(1, 1)
                    .setFixAspectRatio(true)
                    .start(it, this)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.getUser(arguments?.getString(ConstantsExtra.NICKNAME))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val mCropImageUri = result.uri
                val realPath = getRealPathFromURI(mCropImageUri)
                realPath?.let {
                    val photoFile = File(realPath)
                    val imageBody = ImageBody("image/jpeg", photoFile.path)
                    presenter.uploadPhoto(imageBody)
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            } else if (resultCode == RESULT_CANCELED){
                Logger.msg("error")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (arguments?.getString(ConstantsExtra.NICKNAME) == null) {
            inflater.inflate(R.menu.main_menu, menu)
        }
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
        presenter.getUser(arguments?.getString(ConstantsExtra.NICKNAME))
    }

    override fun onPhotoUploadError(msg: String?) {
        Toast.makeText(context, "$msg", Toast.LENGTH_LONG).show()
    }

    override fun onGetUserSuccess(userDto: UserDto) {
        this.userDto = userDto

        context?.let {
            if (userDto.friendsInfo != null){
                when(userDto.friendsInfo.status!!){
                    "PENDING" -> {
                        friendRequest.visible()
                        searchFriends.gone()
                        accept.setOnClickListener {
                            accept.isEnabled = false
                            presenter.approveFriend(userDto.nickname!!)
                        }
                        reject.setOnClickListener {
                            reject.isEnabled = false
                            presenter.rejectFriend(userDto.nickname!!)
                        }
                    }
                    "REQUESTED" -> {
                        (searchFriends as MaterialButton).strokeColor = ContextCompat.getColorStateList(it, R.color.gray)
                        (searchFriends as MaterialButton).setTextColor(ContextCompat.getColor(it, R.color.gray))
                        (searchFriends as MaterialButton).text = getString(R.string.cancel_request)
                        friendRequest.gone()
                        searchFriends.visible()
                        searchFriends.setOnClickListener {
                            searchFriends.isEnabled = false
                            presenter.cancelFriend(userDto.nickname!!)
                        }
                    }
                    "APPROVED" -> {
                        (searchFriends as MaterialButton).strokeColor = ContextCompat.getColorStateList(it, R.color.gray)
                        (searchFriends as MaterialButton).setTextColor(ContextCompat.getColor(it, R.color.gray))
                        (searchFriends as MaterialButton).text = getString(R.string.remove_friend)
                        friendRequest.gone()
                        searchFriends.visible()
                        searchFriends.setOnClickListener {
                            searchFriends.isEnabled = false
                            presenter.unfriendFriend(userDto.nickname!!)
                        }
                    }
                    else -> {
                        (searchFriends as MaterialButton).text = getString(R.string.add_friend)
                        (searchFriends as MaterialButton).strokeColor = ContextCompat.getColorStateList(it, R.color.colorAccent)
                        (searchFriends as MaterialButton).setTextColor(ContextCompat.getColor(it, R.color.colorAccent))
                        searchFriends.setOnClickListener {
                            searchFriends.isEnabled = false
                            presenter.addFriend(userDto.nickname!!)
                        }
                    }
                }
            }else{
                searchFriends.setOnClickListener {
                    searchListener.onOpenSearch(true)
                }
            }
        }

        for (i in userDto.userPhotos.indices){
            if (userDto.userPhotos.get(i).avatar!!){
                imageId = userDto.userPhotos.get(i).id!!
                presenter.getPhoto(imageId)
            }
        }

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

        region.text = userDto.cityDto!!.cityName

        if (userDto.dateOfBirth != null){
            val age = getAge(DateUtility.stringToCalendar(DateUtility.getDashedYMDdateFormat(),
                userDto.dateOfBirth), Calendar.getInstance())

            genderAge.text = resources.getQuantityString(R.plurals.profile_gender_age,
                age.toInt(), userDto.gender?.translation, age)
        }else{
            genderAge.text = userDto.gender?.translation
        }

        friends.text = userDto.numberOfFriends.toString()

        rating.text = userDto.attendanceRating.toString()

        currentGame.title.text = getString(R.string.current_match)
        lastGame.title.text = getString(R.string.played_match)

        userDto.userGamesDto?.let {
            currentGame.matchCount.text = it.activeGames.toString()
            lastGame.matchCount.text = it.completedGames.toString()
            if (it.activeGames == 0){
                currentGame.match.gone()
            }else{
                currentGame.match.text = getString(R.string.current_match_date, it.nextGame)
            }
            if (it.completedGames == 0)
                lastGame.match.gone()
            else
                lastGame.match.text = getString(R.string.played_match_date, it.lastGame)
        }
    }

    override fun onGetUserError(msg: String?) {
        msg?.let {
            if (!msg.contains("INCOMPLETE_ACCOUNT"))
                Toast.makeText(context, "$msg", Toast.LENGTH_LONG).show()
        }
    }

    override fun onGetImageSuccess(url: GlideUrl) {
        Logger.msg("API " + url)
        val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)

        Glide.with(context!!).load(url)
            .apply(options)
            .into(profilePhoto);
    }

    override fun onGetImageError(msg: String?) {
        Toast.makeText(context, "$msg", Toast.LENGTH_LONG).show()
    }

    override fun onFriendOperationSuccess(friendsInfo: FriendsInfo) {
        searchFriends.isEnabled = true
        accept.isEnabled = true
        reject.isEnabled = true
        presenter.getUser(userDto.nickname)
    }

    override fun onFriendOperationError(msg: String?) {
        searchFriends.isEnabled = true
        accept.isEnabled = true
        reject.isEnabled = true
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

    private fun getRealPathFromURI(contentUri: Uri): String? {
        when (contentUri.scheme) {
            "file" -> return contentUri.path
            "content" -> {
                var cursor: Cursor? = null
                try {
                    val proj = arrayOf(MediaStore.Images.Media.DATA)
                    cursor = context?.contentResolver!!.query(contentUri, proj, null, null, null)
                    val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    cursor.moveToFirst()
                    val result = cursor.getString(column_index)
                    cursor.close()
                    return result
                } catch (e: Exception) {
                    return null
                }
            }
            else -> return null
        }
    }
}