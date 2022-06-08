package com.example.cattagram.picture

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cattagram.databinding.FragmentShowPictureBinding
import com.example.cattagram.retrofit.Api
import com.example.cattagram.retrofit.GetCommentsResponse
import com.example.cattagram.retrofit.GetOneImgResponse
import com.example.cattagram.retrofit.ImagesGetUserDetailsResponse
import com.example.cattagram.viewadapters.CommentsAdapter
import com.example.cattagram.viewadapters.MainPagePicAdapter
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


class ShowPictureFragment : Fragment() {
    private var param1: Int? = null
    private var param2: String? = null

    private var _binding: FragmentShowPictureBinding? = null
    private val binding get() = _binding!!

    val adapter by lazy { CommentsAdapter() }
    var layoutManager: RecyclerView.LayoutManager? = null

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
            param1 = it.getInt(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowPictureBinding.inflate(inflater, container, false)

        val retrofit = imgRequest()
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("img_id", "$param1")
            .build()

        val sharedPref = requireActivity().getSharedPreferences("shared", Context.MODE_PRIVATE)
        val userId: Int = sharedPref.getInt("userId", -1)

        retrofit?.getOneImage(requestBody)?.enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    var responseBodyString = response.body()!!.string()

                    responseBodyString = responseBodyString.replace("\\", "")
                    responseBodyString = responseBodyString.dropLast(1).drop(1)
                    Log.d("IMG SUCCESS", "Response: $responseBodyString")

                    val gson = Gson().newBuilder().serializeNulls().create()
                    val jsonArray = JSONArray(responseBodyString)
                    val item = gson.fromJson(jsonArray[0].toString(), GetOneImgResponse::class.java)

                    Picasso.get().load(item.link).fit().into(_binding!!.pictureIv)
                    Log.d("IMG SUCCESS", "JSON: $item")
                }
                catch (e: NullPointerException){
                    Toast.makeText(activity, "Server problems, try again later", Toast.LENGTH_SHORT).show()
                    Log.d("IMG SUCCESS", "Failed with $response")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("IMG FAILURE", "$t")
                Toast.makeText(activity, "Server problems, try again later", Toast.LENGTH_SHORT).show()
            }
        })


        retrofit?.getUserDetailsImage(requestBody)?.enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    var responseBodyString = response.body()!!.string()

                    responseBodyString = responseBodyString.replace("\\", "")
                    responseBodyString = responseBodyString.dropLast(1).drop(1)

                    Log.d("IMG SUCCESS", "Response: $responseBodyString")

                    val gson = Gson().newBuilder().serializeNulls().create()
                    val jsonArray = JSONArray(responseBodyString)
                    val item = gson.fromJson(jsonArray[0].toString(), ImagesGetUserDetailsResponse::class.java)

                    Picasso.get().load(item.avatar).fit().into(_binding!!.profilePicIv)
                    _binding!!.usernameTv.text = item.username

                    Log.d("USER SUCCESS", "JSON: $item")
                }
                catch (e: NullPointerException){
                    Toast.makeText(activity, "Server problems, try again later", Toast.LENGTH_SHORT).show()
                    Log.d("USER SUCCESS", "Failed with $response")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("USER FAILURE", "$t")
                Toast.makeText(activity, "Server problems, try again later", Toast.LENGTH_SHORT).show()
            }
        })


        retrofit?.getComments(requestBody)?.enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    var responseBodyString = response.body()!!.string()

                    if(responseBodyString == "\"[]\"") {
                        _binding!!.noCommentsLayout.visibility = View.VISIBLE
                    }
                    else {
                        _binding!!.noCommentsLayout.visibility = View.GONE
                        responseBodyString = responseBodyString.replace("\\", "")
                        responseBodyString = responseBodyString.dropLast(1).drop(1)

                        Log.d("COMMENT SUCCESS", "Response: $responseBodyString")

                        val gson = Gson().newBuilder().serializeNulls().create()
                        val jsonArray = JSONArray(responseBodyString)
                        var commentsList = mutableListOf<GetCommentsResponse>()

                        for (i in 0 until jsonArray.length()) {
                            val item = gson.fromJson(jsonArray[i].toString(), GetCommentsResponse::class.java)
                            commentsList.add(i, item)
                        }

                        Log.d("COMMENTS SUCCESS", "JSON: ${commentsList[0]}")

                        layoutManager = LinearLayoutManager(context)
                        adapter.setData(commentsList)
                        _binding!!.commentRV.adapter = adapter
                        _binding!!.commentRV.layoutManager = layoutManager
                    }
                }
                catch (e: NullPointerException){
                    Toast.makeText(activity, "Server problems, try again later", Toast.LENGTH_SHORT).show()
                    Log.d("COMMENTS SUCCESS", "Failed with $response")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(activity, "Server problems, try again later", Toast.LENGTH_SHORT).show()
                Log.d("COMMENTS SUCCESS", "Failed with $t")
            }
        })


        _binding!!.btnAddComment.setOnClickListener {
            val commentText = _binding!!.etComment.text.toString()

            val requestBody2: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", "$userId")
                .addFormDataPart("img_id", "$param1")
                .addFormDataPart("comment", "$commentText")
                .build()

            retrofit?.uploadComment(requestBody2)?.enqueue(object: Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    adapter.notifyDataSetChanged()
                    var responseBodyString = response.body()!!.string()

                    Log.d("UPLOAD SUCCESS", "response: $responseBodyString")

                    if (responseBodyString == "{\"response\":200}") {
                        _binding!!.etComment.text.clear()
                        Toast.makeText(activity, "Successfully added comment", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(activity, "Server problems, try again later", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(activity, "Server problems, try again later", Toast.LENGTH_SHORT).show()
                    Log.d("UPLOAD SUCCESS", "Failed with $t")
                }
            })
        }

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: Int, param2: String) =
            ShowPictureFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}