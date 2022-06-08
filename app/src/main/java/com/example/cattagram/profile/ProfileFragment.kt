package com.example.cattagram.profile

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cattagram.R
import com.example.cattagram.databinding.FragmentMainPageBinding
import com.example.cattagram.databinding.FragmentProfileBinding
import com.example.cattagram.mainpage.MainPageActivity
import com.example.cattagram.picture.AddNewPictureActivity
import com.example.cattagram.retrofit.Api
import com.example.cattagram.retrofit.GetNewImagesResponse
import com.example.cattagram.retrofit.ProfileImgResponse
import com.example.cattagram.retrofit.UserGetUserDetailsResponse
import com.example.cattagram.search.SearchActivity
import com.example.cattagram.viewadapters.MainPagePicAdapter
import com.example.cattagram.viewadapters.ProfileAdapter
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    val adapter by lazy { ProfileAdapter() }
    var layoutManager: GridLayoutManager? = null

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
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val sharedPref = requireActivity().getSharedPreferences("shared", Context.MODE_PRIVATE)
        val userId: Int = sharedPref.getInt("userId", -1)

        layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

        val retrofit = imgRequest()
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("user_id", "$userId")
            .build()

        _binding!!.menuProfil.btnEdit.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        _binding!!.menuBottom.ivProfile.setOnClickListener {
            startActivity(Intent(activity, ProfileActivity::class.java))
        }

        _binding!!.menuBottom.ivHome.setOnClickListener {
            startActivity(Intent(activity, MainPageActivity::class.java))
            requireActivity().finish()
        }

        _binding!!.menuBottom.ivAdd.setOnClickListener {
            startActivity(Intent(activity, AddNewPictureActivity::class.java))
        }

        _binding!!.menuBottom.ivSearch.setOnClickListener {
            startActivity(Intent(activity, SearchActivity::class.java))
        }

        retrofit?.getUserImages(requestBody)?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    var responseBodyString = response.body()!!.string()

                    responseBodyString = responseBodyString.replace("\\", "")
                    responseBodyString = responseBodyString.dropLast(1).drop(1)
                    Log.d("PROFILE SUCCESS", "Response: $responseBodyString")

                    val gson = Gson().newBuilder().serializeNulls().create()
                    val jsonArray = JSONArray(responseBodyString)

                    var items = mutableListOf<ProfileImgResponse>()
                    for (i in 0 until jsonArray.length()) {
                        val item = gson.fromJson(jsonArray[i].toString(), ProfileImgResponse::class.java)
                        items.add(i, item)
                    }

                    adapter.setData(items)
                    _binding!!.menuProfil.profileRV.adapter = adapter
                    _binding!!.menuProfil.profileRV.layoutManager = layoutManager
                }
                catch (e: NullPointerException){
                    Toast.makeText(activity, "Server problems, try again later", Toast.LENGTH_SHORT).show()
                    Log.d("PROFILE SUCCESS", "Failed with $response")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("PROFILE FAILURE", "$t")
                Toast.makeText(activity, "Server problems, try again later", Toast.LENGTH_SHORT).show()
            }
        })

        retrofit?.getUserDetailsUser(requestBody)?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    var responseBodyString = response.body()!!.string()

                    responseBodyString = responseBodyString.replace("\\", "")
                    responseBodyString = responseBodyString.dropLast(1).drop(1)
                    Log.d("PROFILE SUCCESS", "Response: $responseBodyString")

                    val gson = Gson().newBuilder().serializeNulls().create()
                    val jsonArray = JSONArray(responseBodyString)

                    var items = mutableListOf<UserGetUserDetailsResponse>()
                    for (i in 0 until jsonArray.length()) {
                        val item = gson.fromJson(jsonArray[i].toString(), UserGetUserDetailsResponse::class.java)
                        items.add(i, item)
                    }


                    _binding!!.menuProfil.profUsername.text = items[0].username
                    Picasso.get().load(items[0].avatar).fit().into(_binding!!.menuProfil.profPic)
                }
                catch (e: NullPointerException){
                    Toast.makeText(activity, "Server problems, try again later", Toast.LENGTH_SHORT).show()
                    Log.d("PROFILE SUCCESS", "Failed with $response")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("PROFILE FAILURE", "$t")
                Toast.makeText(activity, "Server problems, try again later", Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}