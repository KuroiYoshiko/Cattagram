package com.example.cattagram.mainpage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cattagram.databinding.FragmentMainPageBinding
import com.example.cattagram.profile.ProfileActivity
import com.example.cattagram.retrofit.Api
import com.example.cattagram.retrofit.getOneImgResponse
import com.example.cattagram.search.SearchActivity
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MainPageFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentMainPageBinding? = null
    private val binding get() = _binding!!


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
        _binding = FragmentMainPageBinding.inflate(inflater, container, false)

        _binding!!.menuBottom.ivProfile.setOnClickListener {
            startActivity(Intent(activity, ProfileActivity::class.java))
        }

        _binding!!.menuBottom.ivHome.setOnClickListener {
            startActivity(Intent(activity, MainPageActivity::class.java))
            requireActivity().finish()
        }

        _binding!!.menuBottom.ivAdd.setOnClickListener {
            startActivity(Intent(activity, MainPageActivity::class.java))
            requireActivity().finish()
        }

        _binding!!.menuBottom.ivSearch.setOnClickListener {
            startActivity(Intent(activity, SearchActivity::class.java))
            requireActivity().finish()
        }

        val retrofit = imgRequest()
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("img_id", "26")
            .build()



        retrofit?.getOneImage(requestBody)?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    var responseBodyString = response.body()!!.string()

                    responseBodyString = responseBodyString.replace("\\", "")
                    responseBodyString = responseBodyString.dropLast(1).drop(1)
                    Log.d("IMG SUCCESS", "Response: $responseBodyString")

                    val gson = Gson().newBuilder().serializeNulls().create()
                    val jsonArray = JSONArray(responseBodyString)
                    val item = gson.fromJson(jsonArray[0].toString(), getOneImgResponse::class.java)

                    Log.d("IMG SUCCESS", "JSON: ${item.link}")

                    Picasso.get().load(item.link).into(_binding!!.menuNews.pictureIv);

                }
                catch (e: NullPointerException){
                    Log.d("IMG SUCCESS", "NullPointerException")
                    Toast.makeText(activity, "Server problems, try again later", Toast.LENGTH_SHORT).show()
                    Log.d("IMG SUCCESS", "Failed with $response")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("IMG FAILURE", "$t")
                Toast.makeText(activity, "Server problems, try again later", Toast.LENGTH_SHORT).show()
            }
        })

//        _binding!!.mainRV.add


        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}