package com.example.androidproject.ui.profile

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.androidproject.databinding.FragmentProfileBinding
import java.io.BufferedReader
import java.io.InputStreamReader

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fullName.observe(viewLifecycleOwner) {
            binding.profileName.text = it
        }

        viewModel.position.observe(viewLifecycleOwner) {
            binding.profileDomain.text = it
        }

        val adapter = HistoryAdapter()
        binding.progressRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.progressRecyclerView.adapter = adapter

        viewModel.historyList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        // Khởi tại sweet alert dialog
        val loadingDialog = SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE)
        loadingDialog.setCancelable(false)
        loadingDialog.show()

        // Giả lập quá trình tải dữ liệu 2 giây
        Handler(Looper.getMainLooper()).postDelayed({
            val jsonString = loadJSONFromAsset("profile_data.json")
            if (jsonString != null) {
                viewModel.loadProfileData(jsonString)
            }
            loadingDialog.dismiss()  // Ẩn SweetAlert sau khi dữ liệu đã được load
        }, 2000)

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Hành trình"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Hoạt động"))
    }

    private fun loadJSONFromAsset(filename: String): String? {
        return try {
            val inputStream = requireContext().assets.open(filename)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String? = bufferedReader.readLine()
            while (line != null) {
                stringBuilder.append(line)
                line = bufferedReader.readLine()
            }
            bufferedReader.close()
            stringBuilder.toString()
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }
}
