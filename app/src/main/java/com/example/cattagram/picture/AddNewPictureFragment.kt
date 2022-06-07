package com.example.cattagram.picture

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cattagram.R
import com.example.cattagram.databinding.FragmentAddNewPictureBinding
import com.example.cattagram.retrofit.Api
import com.example.cattagram.viewadapters.URIPathHelper
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


@Suppress("DEPRECATION")
class AddNewPictureFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentAddNewPictureBinding? = null
    private val binding get() = _binding!!
    var selectedImage: Uri? = null
    var filePath: String? = null
    var file: String? = null

    var SELECT_PICTURE = 200

    private fun imgRequest(): Api? {

        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("accept", "application / json")
                .addHeader("Authorization", "Bearer 1234567asdfgh")
                .build()
            chain.proceed(newRequest)
        }.build()

        val retrofitBuilder = Retrofit.Builder()
            .client(client)
            .baseUrl("http://158.101.165.232:7000/")
            .build()
            .create(Api::class.java)


        return retrofitBuilder
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddNewPictureBinding.inflate(inflater, container, false)

        _binding!!.btnSearchGallery.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_PICK
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE)
        }

        _binding!!.ivAcceptPicture.setOnClickListener {
            val descriptionText = _binding!!.etDescription.text.toString()

            val retrofit = imgRequest()
            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file, RequestBody.create(MediaType
                    .parse("application/octet-stream"),
                    File(filePath)))
                .addFormDataPart("user_id", "135")
                .addFormDataPart("desc", descriptionText)
                .build()

            retrofit?.uploadImage(requestBody)?.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    try {
                        var responseBodyString = response.body()!!.string()

                        Log.d("UPLOAD SUCCESS", "Response: $responseBodyString")
                        if (responseBodyString == "{\"response\":200}") {
                            Toast.makeText(activity, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                            _binding!!.etDescription.text.clear()
                            _binding!!.ivGoBack.setImageDrawable(Drawable.createFromPath("@drawable/lapa"))
                        }
                        else {
                            Toast.makeText(activity, "There's problem with your file, try checking image again", Toast.LENGTH_SHORT).show()
                        }
                    }
                    catch (e: NullPointerException){
                        Log.d("UPLOAD SUCCESS", "NullPointerException, file:///$filePath")
                        Toast.makeText(activity, "Server problems, try again later", Toast.LENGTH_SHORT).show()
                        Log.d("UPLOAD SUCCESS", "Failed with $response")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("UPLOAD FAILURE", "$t")
                    Toast.makeText(activity, "Server problems, try again later", Toast.LENGTH_SHORT).show()
                }
            })
        }


        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            selectedImage = data.data

            val uriPathHelper = URIPathHelper()
            filePath = uriPathHelper.getPath(requireActivity(), selectedImage!!)

            file = File(filePath).name

            val ivSelected: ImageView? = view?.findViewById(R.id.ivShowImage)
            ivSelected?.setImageURI(selectedImage)
            Log.d("UPLOAD", "$filePath")
        }

    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddNewPictureFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}